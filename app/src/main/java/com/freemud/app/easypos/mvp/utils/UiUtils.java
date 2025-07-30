package com.freemud.app.easypos.mvp.utils;

import android.app.Activity;
import android.graphics.Point;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StyleSpan;

import com.jess.arms.utils.LogUtils;

import java.lang.reflect.Field;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.customview.widget.ViewDragHelper;
import androidx.drawerlayout.widget.DrawerLayout;

/**
 * Created by shuyuanbo on 2022/2/21.
 * Description:
 */
public class UiUtils {
   public static void setDrawerFullListen(Activity context, DrawerLayout mDrawerLayout) {
      Field leftDraggerField = null;//通过反射获得DrawerLayout类中mLeftDragger字段
      try {
         leftDraggerField = mDrawerLayout.getClass().getDeclaredField("mLeftDragger");
         leftDraggerField.setAccessible(true);//私有属性要允许修改
         ViewDragHelper vdh = (ViewDragHelper) leftDraggerField.get(mDrawerLayout);//获取ViewDragHelper的实例, 通过ViewDragHelper实例获取mEdgeSize字段
         Field edgeSizeField = vdh.getClass().getDeclaredField("mEdgeSize");//依旧是通过反射获取ViewDragHelper类中mEdgeSize字段, 这个字段控制边缘触发范围
         edgeSizeField.setAccessible(true);//依然是私有属性要允许修改
         int edgeSize = edgeSizeField.getInt(vdh);//这里获得mEdgeSize真实值
         LogUtils.debugInfo("mEdgeSize: " + edgeSize);//这里可以打印一下看看值是多少

         //Start 获取手机屏幕宽度，单位px
         Point point = new Point();
         context.getWindowManager().getDefaultDisplay().getRealSize(point);
         //End 获取手机屏幕

         LogUtils.debugInfo("point: " + point.x);//依然可以打印一下看看值是多少
         edgeSizeField.setInt(vdh, Math.max(edgeSize, point.x));//这里设置mEdgeSize的值！！！，Math.max函数取得是最大值，也可以自己指定想要触发的范围值，如: 500,注意单位是px
         //写到这里已经实现了，但是你会发现，如果长按触发范围，侧边栏也会弹出来，而且速度不太同步，稳定

         //Start 解决长按会触发侧边栏
         //长按会触发侧边栏是DrawerLayout类的私有类ViewDragCallback中的mPeekRunnable实现导致，我们通过反射把它置空
         Field leftCallbackField = mDrawerLayout.getClass().getDeclaredField("mLeftCallback");//通过反射拿到私有类ViewDragCallback字段
         leftCallbackField.setAccessible(true);//私有字段设置允许修改
         ViewDragHelper.Callback vdhCallback = (ViewDragHelper.Callback) leftCallbackField.get(mDrawerLayout);//ViewDragCallback类是私有类，我们返回类型定义成他的父类
         Field peekRunnableField = vdhCallback.getClass().getDeclaredField("mPeekRunnable");//我们依然是通过反射拿到关键字段，mPeekRunnable
         peekRunnableField.setAccessible(true);//不解释了
         //定义一个空的实现
         Runnable nullRunnable = new Runnable() {
            @Override
            public void run() {

            }
         };
         peekRunnableField.set(vdhCallback, nullRunnable);//给mPeekRunnable字段置空
         //End 解决长按触发侧边栏
      } catch (NoSuchFieldException | IllegalAccessException e) {
         e.printStackTrace();
      }
   }

   public static void setEtDisableClick(AppCompatEditText et) {
      et.setFocusable(false);
      et.setClickable(true);
      et.setFocusableInTouchMode(false);
      et.setCursorVisible(false);
      et.setInputType(InputType.TYPE_NULL);
   }

   public static SpannableString getBoldStr(String string, String[] array) {
      if(TextUtils.isEmpty(string)){
         return new SpannableString("");
      }
      SpannableString spannedString = new SpannableString(string);
      for (int i = 0; i < array.length; i++) {
         int idx = string.indexOf(array[i]);
         if(idx > -1){
            spannedString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), idx,
                    idx + array[i].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
         }
      }
      return spannedString;
   }
}
