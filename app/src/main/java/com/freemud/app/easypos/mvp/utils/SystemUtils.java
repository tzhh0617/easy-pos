package com.freemud.app.easypos.mvp.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/*******************************************************************************************
 *
 * @author: yuanbo.shu
 * @date： 2022/5/10 16:30
 * @version 1.0
 * @description:
 * Version    Date       ModifiedBy                 Content
 * 1.0      2022/5/10       yuanbo.shu                             
 *******************************************************************************************
 */
public class SystemUtils {
    /**
     * 判断服务是否运行
     */
    public static boolean isServiceRunning(Context context, final String className) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> info = activityManager.getRunningServices(Integer.MAX_VALUE);
        if (info == null || info.size() == 0) return false;
        for (ActivityManager.RunningServiceInfo aInfo : info) {
            if (className.equals(aInfo.service.getClassName())) return true;
        }
        return false;
    }
}
