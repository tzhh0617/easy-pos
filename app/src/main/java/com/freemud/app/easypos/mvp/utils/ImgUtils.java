package com.freemud.app.easypos.mvp.utils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import com.freemud.app.easypos.common.base.MyBaseFragment;
import com.jess.arms.utils.LogUtils;
import com.lwkandroid.imagepicker.ImagePicker;
import com.lwkandroid.imagepicker.data.ImagePickType;
import com.lwkandroid.imagepicker.utils.GlideImagePickerDisplayer;
import com.zzti.fengyongge.imagepicker.PhotoSelectorActivity;
import com.zzti.fengyongge.imagepicker.util.CommonUtils;

import java.io.FileNotFoundException;
import java.io.OutputStream;

import static com.zzti.fengyongge.imagepicker.ImagePickerInstance.IS_SHOW_CAMERA;
import static com.zzti.fengyongge.imagepicker.ImagePickerInstance.LIMIT;

/**
 * Created by shuyuanbo on 2022/2/14.
 * Description:
 */
public class ImgUtils {
   /**
    * 对外图库选择图片,或者拍照选择图片方法
    *
    * @param context
    * @param limit        选择图片张数
    * @param isShowCamera 是否支持拍照
    * @param requestCode
    */
   public static void photoSelect(Context context, int limit, boolean isShowCamera, int requestCode) {
       Intent intent = new Intent(context, PhotoSelectorActivity.class);
       intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
       intent.putExtra(LIMIT, limit);
       intent.putExtra(IS_SHOW_CAMERA, isShowCamera);
       CommonUtils.launchActivityForResult((Activity) context, intent, requestCode);

   }

    public static void photoSelect(Activity context, int limit, boolean isShowCamera, int requestCode) {
        new ImagePicker()
                .pickType(ImagePickType.SINGLE) //设置选取类型(拍照ONLY_CAMERA、单选SINGLE、多选MUTIL)
                .maxNum(limit) //设置最大选择数量(此选项只对多选生效，拍照和单选都是1，修改后也无效)
                .needCamera(true) //是否需要在界面中显示相机入口(类似微信那样)
//              .cachePath(cachePath) //自定义缓存路径(拍照和裁剪都需要用到缓存)
                .doCrop(1, 1, 300, 300) //裁剪功能需要调用这个方法，多选模式下无效，参数：aspectX,aspectY,outputX,outputY
                .displayer(new GlideImagePickerDisplayer()) //自定义图片加载器，默认是Glide实现的,可自定义图片加载器
                .start(context, requestCode); //自定义RequestCode
    }

    public static void photoSelect(MyBaseFragment context, int limit, boolean isShowCamera, int requestCode) {
        new ImagePicker()
                .pickType(ImagePickType.SINGLE) //设置选取类型(拍照ONLY_CAMERA、单选SINGLE、多选MUTIL)
                .maxNum(limit) //设置最大选择数量(此选项只对多选生效，拍照和单选都是1，修改后也无效)
                .needCamera(true) //是否需要在界面中显示相机入口(类似微信那样)
//              .cachePath(cachePath) //自定义缓存路径(拍照和裁剪都需要用到缓存)
                .doCrop(1, 1, 300, 300) //裁剪功能需要调用这个方法，多选模式下无效，参数：aspectX,aspectY,outputX,outputY
                .displayer(new GlideImagePickerDisplayer()) //自定义图片加载器，默认是Glide实现的,可自定义图片加载器
                .start(context, requestCode); //自定义RequestCode
    }

    /**
     * API29 中的最新保存图片到相册的方法
     */
    public static boolean saveImage29(Context context,Bitmap toBitmap) {
        //开始一个新的进程执行保存图片的操作
        Uri insertUri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        //使用use可以自动关闭流
        try {
            OutputStream outputStream = context.getContentResolver().openOutputStream(insertUri, "rw");
            if (toBitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)) {
                LogUtils.debugInfo("保存成功");
                return true;
            } else {
                LogUtils.debugInfo("保存失败");
                return false;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}
