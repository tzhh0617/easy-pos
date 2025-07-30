package com.freemud.app.easypos.mvp.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import com.jess.arms.utils.LogUtils;

import java.util.UUID;

import androidx.core.app.ActivityCompat;

public class DeviceUtils {

    public static String getSerialNumber(Context context) {
        String sn = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return "";
            }

            try {
                sn = Build.getSerial();
            } catch (Exception e) {
                e.printStackTrace();
                String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                sn = androidId;
            }
        }
        if (TextUtils.isEmpty(sn) || sn.equalsIgnoreCase("unknown")) {
            sn = UUID.randomUUID().toString();
        }
        //如果设备硬编码 sn和adnroidId都拿不到 随机uuid 本项目删除即重绑 可适用软编码作为唯一

        return sn;
    }

    public static String getDeviceModel(){
        return Build.BRAND +" " + Build.MODEL;
    }

    public static String getAppName(Context context){
        String appName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            ApplicationInfo applicationInfo = pi.applicationInfo;
            appName = (String) applicationInfo.loadLabel(pm);
        } catch (Exception e) {
            LogUtils.debugInfo("VersionInfo Exception", e.getMessage());
        }
        return appName;
    }


}
