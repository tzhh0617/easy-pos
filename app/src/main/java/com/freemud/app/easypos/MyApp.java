package com.freemud.app.easypos;

import android.content.Context;
import android.os.StrictMode;

import com.google.gson.Gson;
import com.jess.arms.base.BaseApplication;
import com.jess.arms.utils.DataHelper;
import com.scwang.smart.refresh.footer.BallPulseFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshFooter;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshFooterCreator;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshHeaderCreator;
import com.tencent.bugly.crashreport.CrashReport;

import androidx.annotation.NonNull;

/**
 * Created by shuyuanbo on 2022/1/27.
 * Description:
 */
public class MyApp extends BaseApplication {
    private static MyApp mInstance;

    static {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
//                layout.setPrimaryColorsId(R.color.white, R.color.white);
                return new ClassicsHeader(context);
            }
        });
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(@NonNull Context context, @NonNull RefreshLayout layout) {
                return new BallPulseFooter(context);
            }
        });
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        initFileXml();
        CrashReport.initCrashReport(getApplicationContext(), "98aac4bbcd", true);
    }

    public static MyApp getInstance() {
        return mInstance;
    }


    private void initFileXml() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

    }

}
