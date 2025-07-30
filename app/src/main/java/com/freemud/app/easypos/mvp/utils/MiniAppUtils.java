package com.freemud.app.easypos.mvp.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.jess.arms.utils.LogUtils;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by shuyuanbo on 2023/4/11.
 * Description:
 */
public class MiniAppUtils {

   public static void gotoWechatMiniProgram(Context context,String appId, String gId, String path) {
      IWXAPI api = WXAPIFactory.createWXAPI(context, appId);

      WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
      req.userName = gId; // 填小程序原始id
      req.path = path;                  ////拉起小程序页面的可带参路径，不填默认拉起小程序首页，对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"。
      req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
      api.sendReq(req);
   }

   public static void gotoAlipayMiniProgram(Context context,String appId, String path) {
      String query = "";
      String page = "";
      if (!TextUtils.isEmpty(path) && path.contains("?")) {
         String[] strArr = path.split("\\?");
//         path = strArr[0];
         query = strArr[1];
         page = strArr[0];
         try {
            query = URLEncoder.encode(query, "UTF-8");
            path = URLEncoder.encode(path,"UTF-8");
         } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
         }
      }
      //+"&query="+query
      String url = "alipays://platformapi/startapp?appId="+appId+"&page="+path;
      LogUtils.debugInfo(url);
      Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//      Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(
//              "alipays://platformapi/startapp?appId="+appId+"&page="+page + "&query=" + query));
      context.startActivity(intent);
   }

}
