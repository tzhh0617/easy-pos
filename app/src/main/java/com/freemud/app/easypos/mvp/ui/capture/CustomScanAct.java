package com.freemud.app.easypos.mvp.ui.capture;

import android.os.Bundle;
import android.view.MotionEvent;

import com.freemud.app.easypos.R;
import com.freemud.app.easypos.common.base.MyBaseActivityNoP;
import com.freemud.app.easypos.databinding.ActCustomScanBinding;
import com.jess.arms.di.component.AppComponent;
import com.king.zxing.CaptureHelper;
import com.king.zxing.OnCaptureCallback;

import androidx.annotation.NonNull;

/**
 * Created by shuyuanbo on 2023/3/23.
 * Description:
 */
public class CustomScanAct extends MyBaseActivityNoP<ActCustomScanBinding> implements OnCaptureCallback {

    private CaptureHelper mCaptureHelper;

    @Override
    protected ActCustomScanBinding getContentView() {
        return ActCustomScanBinding.inflate(getLayoutInflater());
    }

    private void initTitle() {
        mBinding.customScanHead.headTitle.setText("扫一维码/二维码");
        mBinding.customScanHead.headTitle.setTextColor(getColor(R.color.black));
        mBinding.customScanHead.headBackIv.setTextColor(getColor(R.color.black));
        mBinding.customScanHead.headBack.setOnClickListener(view -> {
            goBack();
        });
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        initTitle();
        initUI();
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    public void initUI(){
        mCaptureHelper = new CaptureHelper(this,mBinding.customScanSurfaceView,
                mBinding.customScanViewfinderView,mBinding.customScanIvTorch);
        mCaptureHelper.setOnCaptureCallback(this);
        mCaptureHelper.onCreate();
    }


    public CaptureHelper getCaptureHelper(){
        return mCaptureHelper;
    }

    @Override
    public void onResume() {
        super.onResume();
        mCaptureHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mCaptureHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCaptureHelper.onDestroy();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mCaptureHelper.onTouchEvent(event);
        return super.onTouchEvent(event);
    }


    @Override
    public boolean onResultCallback(String result) {
        return false;
    }
}
