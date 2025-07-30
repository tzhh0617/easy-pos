package com.freemud.app.easypos.common.base;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;

import com.freemud.app.easypos.R;
import com.freemud.app.easypos.mvp.model.constant.ComponentConstant;
import com.freemud.app.easypos.mvp.utils.DisplayUtils;
import com.freemud.app.easypos.mvp.utils.KeyboardUtils;
import com.freemud.app.easypos.mvp.widget.common.CommonPop;
import com.freemud.app.easypos.mvp.widget.common.LoadingDialog;
import com.jaeger.library.StatusBarUtil;
import com.jess.arms.base.BaseActivity2;
import com.jess.arms.mvp.IPresenter;
import com.jess.arms.utils.ArmsUtils;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigButton;
import com.mylhyl.circledialog.callback.ConfigDialog;
import com.mylhyl.circledialog.callback.ConfigItems;
import com.mylhyl.circledialog.params.ButtonParams;
import com.mylhyl.circledialog.params.DialogParams;
import com.mylhyl.circledialog.params.ItemsParams;
import com.mylhyl.circledialog.view.listener.OnRvItemClickListener;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.viewbinding.ViewBinding;

/**
 * Created by shuyuanbo on 2022/1/27.
 * Description:
 */
public abstract class MyBaseActivity<T extends ViewBinding, P extends IPresenter> extends BaseActivity2<T, P> {
    public static final int TRANSITION_NONE = -1;
    public static final int TRANSITION_FADE = 0;
    public static final int TRANSITION_SLIDE = 1;
    public static final int TRANSITION_CIRCUL = 2;
    public static final int TRANSITION_CUSTOM = 3;
    public static final int TRANSITION_SLIDE_PRELOLLIPOP = 4;
    public static final int TRANSITION_SLIDE_BOTTOM = 5;
    public static final int TRANSITION_SLIDE_IN_RIGHT = 6;
    protected int mTransitionMode = TRANSITION_SLIDE;

    public static final int REFRESH_TYPE_DEFAULT = 0;
    public static final int REFRESH_TYPE_SYNC = 1;
    public static final int REFRESH_TYPE_LOAD_MORE = 2;
    protected int refreshType = 0;    //0默认  1刷新 2更多
    protected int mCommonPage = 1;

    private LoadingDialog mLoadingDialog;
    protected int[] dialogTxtPadding;
    protected int mSyncReqCount = 0;
    protected int mReqTotal = 0;

    protected boolean isFirstLoad = true;


    public void showLoading() {
        mReqTotal += 1;
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
        }
        if (mLoadingDialog.isShowing()) return;
        mLoadingDialog.show();
    }

    public void hideLoading() {

        if (mLoadingDialog != null) {
            mReqTotal -= 1;
            if (mReqTotal <= 0) {
                mLoadingDialog.dismiss();
                mLoadingDialog = null;
            }
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

    @Override
    protected void onResume() {
        super.onResume();
        if (isFirstLoad) {
            isFirstLoad = false;
        }else {
            onResumeNotFirst();
        }
    }

    protected int getStatusBarColor() {
        return R.color.white;
    }

    protected void initListener() {
    }

    //重新加载且不是第一次
    protected void onResumeNotFirst() {}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLoadingDialog != null) {
            mLoadingDialog = null;
        }
    }

    /**
     * 子类统一结束带刷新列表的处理方法 兼容
     */
    public void refreshComplete(List list) {
        if (getRefreshLayout() == null) return;
        if (refreshType == REFRESH_TYPE_LOAD_MORE) {
            getRefreshLayout().finishLoadMore();
            refreshType = REFRESH_TYPE_DEFAULT;
            if (list == null || list.size() == 0) {
                mCommonPage -= 1;
            }
            if (mCommonPage < 1) {
                mCommonPage = 1;
            }
        } else if (refreshType == REFRESH_TYPE_SYNC) {
            getRefreshLayout().finishRefresh();
            refreshType = REFRESH_TYPE_DEFAULT;
        }
    }

    public SmartRefreshLayout getRefreshLayout() {
        return null;
    }

    public void showMessage(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }

        ArmsUtils.snackbarTextWithLong(msg,DisplayUtils.dp2px(this,200),DisplayUtils.dp2px(this,100));
//        ArmsUtils.makeText(getApplicationContext(), msg);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (judgeClickEtOut(v, ev)) {
                KeyboardUtils.hideKeyboard(this);
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

    public void showConfirmDialogNoCancel(String title,String msg, String confirmText, CommonPop.OnDialogCallBack callBack) {
        CircleDialog.Builder builder =
        new CircleDialog.Builder().setWidth(ComponentConstant.DIALOG_WIDTH_PERCENT)
                .setTextColor(this.getColor(R.color.black))
                .setText(msg).setTextColor(this.getColor(R.color.dialog_content))
                .configText(params -> {
                    params.height = DisplayUtils.dp2px(this, ComponentConstant.DIALOG_TEXT_HEIGHT_DP);
//                    params.padding = dialogTxtPadding;
                }).setPositive(confirmText, v -> {
            if (callBack != null) {
                callBack.onPositive();
            }
            return true;
        }).setCanceledOnTouchOutside(false);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
            builder.configTitle(params -> {
                params.styleText = Typeface.BOLD;
            });
        }
        builder.show(this.getSupportFragmentManager());
    }


    public void showConfirmDialog(String title,String msg, String cancelText, String confirmText, CommonPop.OnDialogCallBack callBack) {
        new CircleDialog.Builder().setWidth(ComponentConstant.DIALOG_WIDTH_PERCENT)
                .setTitle(title).setTextColor(this.getColor(R.color.black))
                .configTitle(params -> {
                    params.styleText = Typeface.BOLD;
                })
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

    public void showConfirmDialog(String title,String msg, String cancelText,int cancelColor, String confirmText, CommonPop.OnDialogCallBack callBack) {
        new CircleDialog.Builder().setWidth(ComponentConstant.DIALOG_WIDTH_PERCENT)
                .setTitle(title).setTextColor(this.getColor(R.color.black))
                .configTitle(params -> {
                    params.styleText = Typeface.BOLD;
                })
                .setText(msg).setTextColor(this.getColor(R.color.dialog_content))
                .configText(params -> {
                    params.height = DisplayUtils.dp2px(this, ComponentConstant.DIALOG_TEXT_HEIGHT_DP);
//                    params.padding = dialogTxtPadding;
                })
                .configNegative(params -> {
                    params.textColor = this.getColor(cancelColor);
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

    public void showConfirmDialog(String title,String msg, String cancelText,int cancelColor, String confirmText,boolean isBold, CommonPop.OnDialogCallBack callBack) {
        new CircleDialog.Builder().setWidth(ComponentConstant.DIALOG_WIDTH_PERCENT)
                .setTitle(title).setTextColor(this.getColor(R.color.black))
                .configTitle(params -> {
                    params.styleText = Typeface.BOLD;
                })
                .setText(msg).setTextColor(this.getColor(R.color.dialog_content))
                .configText(params -> {
                    params.height = DisplayUtils.dp2px(this, ComponentConstant.DIALOG_TEXT_HEIGHT_DP);
//                    params.padding = dialogTxtPadding;
                })
                .configNegative(params -> {
                    params.textColor = this.getColor(cancelColor);
                })
                .setNegative(cancelText, v -> {
                    if (callBack != null) {
                        callBack.onNegative();
                    }
                    return true;
                }).configPositive(params -> {
                    if (isBold) params.styleText = Typeface.BOLD;
                }).setPositive(confirmText, v -> {
                    if (callBack != null) {
                        callBack.onPositive();
                    }
                    return true;
                }).setCanceledOnTouchOutside(false).show(this.getSupportFragmentManager());
    }

    public void showErrorConfirmDialog(String msg) {
        new CircleDialog.Builder().setWidth(ComponentConstant.DIALOG_WIDTH_PERCENT)
                .setTitle("温馨提示").setTextColor(this.getColor(R.color.black))
                .configTitle(params -> {
                    params.styleText = Typeface.BOLD;
                })
                .setText(msg).setTextColor(this.getColor(R.color.dialog_content))
                .configText(params -> {
                    params.height = DisplayUtils.dp2px(this, ComponentConstant.DIALOG_TEXT_HEIGHT_DP);
//                    params.padding = dialogTxtPadding;
                }).setPositive("确定", v -> true).show(this.getSupportFragmentManager());
    }

    protected CircleDialog.Builder showBottomDialog(String[] options,OnRvItemClickListener listener) {
        CircleDialog.Builder dialogOptions = new CircleDialog.Builder().configDialog(new ConfigDialog() {
            @Override
            public void onConfig(DialogParams params) {
                //增加弹出动画
//                params.animStyle = R.style.dialogWindowAnim;
            }
        })
                .setItems(options, (view, position) -> listener.onItemClick(view,position))
                .configItems(new ConfigItems() {
                    @Override
                    public void onConfig(ItemsParams params) {
                        params.textColor = getColor(R.color.black);
                    }
                })
                .setNegative("取消", null)
                .configNegative(new ConfigButton() {
                    @Override
                    public void onConfig(ButtonParams params) {
                        //取消按钮字体颜色
                        params.textColor = getColor(R.color.black);
                    }
                });
        return dialogOptions;
    }

    protected CircleDialog.Builder showBottomDialog(String[] options,int optionColor,boolean cancelBold,OnRvItemClickListener listener) {
        CircleDialog.Builder dialogOptions = new CircleDialog.Builder().configDialog(new ConfigDialog() {
                    @Override
                    public void onConfig(DialogParams params) {
                        //增加弹出动画
//                params.animStyle = R.style.dialogWindowAnim;
                    }
                })
                .setItems(options, (view, position) -> listener.onItemClick(view,position))
                .configItems(new ConfigItems() {
                    @Override
                    public void onConfig(ItemsParams params) {
                        if (optionColor != 0) {
                            params.textColor = getColor(optionColor);
                        }else {
                            params.textColor = getColor(R.color.black);
                        }
                    }
                })
                .setNegative("取消", null)
                .configNegative(new ConfigButton() {
                    @Override
                    public void onConfig(ButtonParams params) {
                        //取消按钮字体颜色
                        params.textColor = getColor(R.color.black);
                        if (optionColor != 0) {
                            params.textColor = getColor(optionColor);
                        }
                        if (cancelBold) {
                            params.styleText = Typeface.BOLD;
                        }
                    }
                });
        return dialogOptions;
    }

    public void goBack() {
        finish();
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    public void onChangeS(String msg,boolean isBack) {
        if (!TextUtils.isEmpty(msg)) {
            showMessage(msg);
        }
        if (isBack) {
            new Handler().postDelayed(()->{goBack();},1000);
        }
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
//            performTransition();
//        }
    }


//    @Override
//    public void startActivity(Intent intent) {
//        super.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
//    }
}
