package com.freemud.app.easypos.mvp.utils.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * Created by shuyuanbo on 2019/9/9.
 * Description:网络相关工具类
 */
public class NetUtils {
    public static NetType getNetWorkType(Context context) {
        //得到连接管理器对象
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) return NetType.NOME;
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        if (activeNetworkInfo == null) return NetType.NOME;
        //如果网络连接，判断该网络类型
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_WIFI)) {
                return NetType.WIFI;//wifi
            } else if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_MOBILE)) {
                return getMobileNetType(context);//mobile
            }
        } else {
            //网络异常
            return NetType.NOME;
        }
        return NetType.NOME;
    }

    public static boolean isNetBad(NetType netType) {
        boolean isNetbad = false;
        switch (netType) {
            case NET_2G:
                isNetbad = true;
                break;
            case NET_3G:
                isNetbad = true;
                break;
            case NET_UNKNOW:
                isNetbad = true;
                break;
            case NET_4G:
                isNetbad = false;
                break;
            case WIFI:
                isNetbad = false;
                break;
            default:
                isNetbad = true;
                break;
        }
        return isNetbad;
    }

    /**
     * 获取移动网络的类型
     *
     * @param context
     * @return 移动网络类型
     */
    private static NetType getMobileNetType(Context context) {
        TelephonyManager mTelephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        assert mTelephonyManager != null;
        int networkType = mTelephonyManager.getNetworkType();
        return getNetworkClass(networkType);
    }

    /**
     * 判断移动网络的类型
     *
     * @param networkType
     * @return 移动网络类型
     */
    private static final NetType getNetworkClass(int networkType) {
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return NetType.NET_2G;
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return NetType.NET_3G;
            case TelephonyManager.NETWORK_TYPE_LTE:
                return NetType.NET_4G;
            default:
                return NetType.NET_UNKNOW;
        }
    }
}
