package com.freemud.app.easypos.mvp.utils.network;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import androidx.lifecycle.LiveData;

/**
 * Created by shuyuanbo on 2019/9/9.
 * Description:liveData内部内存管理 及生命周期管理 防内存泄漏
 */
public class NetWorkLiveData extends LiveData<NetType> {
    private final Context mContext;
    @SuppressLint("StaticFieldLeak")
    private static NetWorkLiveData mNetworkLiveData;
    private NetworkReceiver mNetworkReceiver;
    private final IntentFilter mIntentFilter;

    public NetWorkLiveData(Context context) {
        this.mContext = context;
        mNetworkReceiver = new NetworkReceiver();
        mIntentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
    }

    public static NetWorkLiveData getInstance(Context context) {
        if (mNetworkLiveData == null) {
            mNetworkLiveData = new NetWorkLiveData(context);
        }
        return mNetworkLiveData;
    }

    @Override
    protected void onActive() {
        super.onActive();
        mContext.registerReceiver(mNetworkReceiver, mIntentFilter);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        if (mNetworkReceiver != null)
            mContext.unregisterReceiver(mNetworkReceiver);
    }

    private static class NetworkReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            getInstance(context).setValue(NetUtils.getNetWorkType(context));
        }
    }
}
