package com.freemud.app.easypos.mvp.ui;

import android.annotation.SuppressLint;
import android.app.Presentation;
import android.content.Context;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Display;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;

import com.freemud.app.easypos.databinding.ActivityCommonWebBinding;
import com.freemud.app.easypos.mvp.utils.DeviceUtils;

/**
 * Created by shuyuanbo on 2023/5/9.
 * Description:
 */
public class WebSubScreen extends Presentation {

    private String mUrl;
    private ActivityCommonWebBinding mBinding;

    public WebSubScreen(Context outerContext, Display display, String url) {
        super(outerContext, display);
        this.mUrl = url;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = this.getContentView();
        setContentView(mBinding.getRoot());
        initView();
        loadUrl(mUrl);
    }

    protected ActivityCommonWebBinding getContentView() {
        return ActivityCommonWebBinding.inflate(getLayoutInflater());
    }

    private final Handler hideHandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    hideBg();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };


    @SuppressLint("SetJavaScriptEnabled")
    private void initView() {
        mBinding.image.setVisibility(View.VISIBLE);
        mBinding.image.setOnClickListener(view -> {
//            doCustomScan();
        });
        hideHandler.sendEmptyMessageDelayed(1, 2000);
        mBinding.webview.getSettings().setDomStorageEnabled(true);
        mBinding.webview.getSettings().setJavaScriptEnabled(true);
        mBinding.webview.setWebViewClient(new MyWebViewClient());
        mBinding.webview.getSettings().setAllowFileAccess(true);
        mBinding.webview.getSettings().setMediaPlaybackRequiresUserGesture(false);
        mBinding.webview.addJavascriptInterface(new AndroidJsBridgeInterface(), "FMEPosClass");
        mBinding.webview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
    }

    private void loadUrl(String url) {
        if (url != null && !url.isEmpty()) {
            mBinding.webview.loadUrl(url);
        }
    }

    public void switchUrl(String newUrl) {
        this.mUrl = newUrl;
        loadUrl(newUrl);
    }


    private void hideBg() {
        mBinding.image.setVisibility(View.GONE);
    }

    public String getSn() {
        return DeviceUtils.getSerialNumber(this.getContext());
    }


    private class AndroidJsBridgeInterface {
        @JavascriptInterface
        public void scanCode() {

        }

        @JavascriptInterface
        public void openCash() {

        }

        @JavascriptInterface
        public String getDeviceSn() {
            return getSn();
        }

        @JavascriptInterface
        public void beginPrint(String printModel) {

        }

        @JavascriptInterface
        public String isSubScreen() {
            return "true";
        }
    }

    public class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            //do your custom
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }

        @SuppressLint("WebViewClientOnReceivedSslError")
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }
    }


}
