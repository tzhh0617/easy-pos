package com.freemud.app.easypos.common.base;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;

import com.freemud.app.easypos.R;
import com.freemud.app.easypos.mvp.model.constant.ComponentConstant;
import com.freemud.app.easypos.mvp.utils.DisplayUtils;
import com.freemud.app.easypos.mvp.widget.common.CommonPop;
import com.freemud.app.easypos.mvp.widget.common.LoadingDialog;
import com.jaeger.library.StatusBarUtil;
import com.jess.arms.base.BaseActivityNoP;
import com.jess.arms.utils.ArmsUtils;
import com.mylhyl.circledialog.CircleDialog;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.viewbinding.ViewBinding;

/**
 * Created by shuyuanbo on 2022/1/27.
 * Description:
 */
public abstract class MyBaseActivityNoP<T extends ViewBinding> extends BaseActivityNoP<T> {
    public static final int TRANSITION_NONE = -1;
    public static final int TRANSITION_FADE = 0;
    public static final int TRANSITION_SLIDE = 1;
    public static final int TRANSITION_CIRCUL = 2;
    public static final int TRANSITION_CUSTOM = 3;
    public static final int TRANSITION_SLIDE_PRELOLLIPOP = 4;
    public static final int TRANSITION_SLIDE_BOTTOM = 5;
    public static final int TRANSITION_SLIDE_IN_RIGHT = 6;
    protected int mTransitionMode = TRANSITION_SLIDE;


    private LoadingDialog mLoadingDialog;
    protected int[] dialogTxtPadding;
    protected boolean isFirstLoad = true;

    public void showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
        }
        mLoadingDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFirstLoad) {
            isFirstLoad = false;
        }else {
            onResumeNotFirst();
        }
    }

    protected void onResumeNotFirst() {};

    public void hideLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setColor(this, getColor(getStatusBarColor()),1);
        super.onCreate(savedInstanceState);

        dialogTxtPadding = new int[]{DisplayUtils.dp2px(this, 35),
                0, DisplayUtils.dp2px(this, 35), 0};
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initListener();
    }

    protected int getStatusBarColor() {
        return R.color.white;
    }

    protected void initListener() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLoadingDialog != null) {
            mLoadingDialog = null;
        }
    }

    public void showMessage(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
//        ArmsUtils.snackbarTextWithLong(msg);
        ArmsUtils.makeText(getApplicationContext(), msg);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (judgeClickEtOut(v, ev)) {
                v.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean judgeClickEtOut(View v, MotionEvent event) {
        if (v instanceof AppCompatEditText) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            // 点击EditText的事件，忽略它。
            return !(event.getX() > left) || !(event.getX() < right)
                    || !(event.getY() > top) || !(event.getY() < bottom);
        }
        return false;
    }

    public void showConfirmDialog(String msg, String cancelText, String confirmText, CommonPop.OnDialogCallBack callBack) {
        new CircleDialog.Builder().setWidth(ComponentConstant.DIALOG_WIDTH_PERCENT)
                .setText(msg).setTextColor(this.getColor(R.color.dialog_content))
                .configText(params -> {
                    params.height = DisplayUtils.dp2px(this, ComponentConstant.DIALOG_TEXT_HEIGHT_DP);
//                    params.padding = dialogTxtPadding;
                })
                .setNegative(cancelText, v -> {
                    if (callBack != null) {
                        callBack.onNegative();
                    }
                    return true;
                }).setPositive(confirmText, v -> {
            if (callBack != null) {
                callBack.onPositive();
            }
            return true;
        }).show(this.getSupportFragmentManager());
    }

    public void goBack() {
        finish();
    }

    @Override
    public void onBackPressed() {
        goBack();
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setupWindowAnimations();
    }

    @Override
    public void finish() {
        super.finish();
        setupWindowAnimations();
    }

    protected void setupWindowAnimations() {
//        if( Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
//            performPreLollipopInTransition();
//        }else {
//        performTransition();
//        }
    }

//    @Override
//    public void startActivity(Intent intent) {
//        super.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
//    }
}
