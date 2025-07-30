package com.freemud.app.easypos.common.base;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.freemud.app.easypos.R;
import com.freemud.app.easypos.mvp.model.constant.ComponentConstant;
import com.freemud.app.easypos.mvp.utils.DisplayUtils;
import com.freemud.app.easypos.mvp.widget.common.CommonPop;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.mvp.IPresenter;
import com.jess.arms.utils.ArmsUtils;
import com.mylhyl.circledialog.CircleDialog;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

/*******************************************************************************************
 *
 * @author: yuanbo.shu
 * @date： 2022/1/28 0:06
 * @version 1.0
 * @description:
 * Version    Date       ModifiedBy                 Content
 * 1.0      2022/1/28       yuanbo.shu                             
 *******************************************************************************************
 */
public abstract class MyBaseFragment<VB extends ViewBinding, P extends IPresenter> extends BaseFragment<P> {
    public static final int REFRESH_TYPE_DEFAULT = 0;
    public static final int REFRESH_TYPE_SYNC = 1;
    public static final int REFRESH_TYPE_LOAD_MORE = 2;

    protected VB mBinding;
    private boolean isShowing = true;
    protected int refreshType = 0;    //0默认  1刷新 2更多
    protected int mCommonPage = 1;
    protected int[] dialogTxtPadding;
    private boolean isFirstRun = true;

    @Override
    public View initView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = getContentView(inflater, container, savedInstanceState);
        return mBinding.getRoot();
    }

    protected abstract VB getContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    public void showLoading() {
        if (getActivity() instanceof MyBaseActivity) {
            ((MyBaseActivity) getActivity()).showLoading();
        }
    }

    public void hideLoading() {
        if (getActivity() instanceof MyBaseActivity) {
            ((MyBaseActivity) getActivity()).hideLoading();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialogTxtPadding = new int[]{DisplayUtils.dp2px(getContext(), 35),
                0, DisplayUtils.dp2px(getContext(), 35), 0};
    }

    @Override
    public void onResume() {
        super.onResume();
        isShowing = isVisible();
        if (isFirstRun) {
            isFirstRun = false;
        }else {
            onRealResume();
        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        isShowing = !hidden;
        onVisibleChange(isVisbleEx());
        if (getCurrentChildFragment() != null) {
            getCurrentChildFragment().onHiddenChanged(hidden);
        }
    }

    protected void onVisibleChange(boolean isVisible) {
    }

    public void onRealResume() {
    }

    /**
     * 如果有子类 获取当前的子类fragment
     *
     * @return
     */
    protected MyBaseFragment getCurrentChildFragment() {
        return null;
    }

    public boolean isVisbleEx() {
        if (isShowing && getUserVisibleHint() && isVisible()) {
            if (getParentFragment() == null) {
                return true;
            }
            if (getParentFragment() instanceof MyBaseFragment) {
                return ((MyBaseFragment<?, ?>) getParentFragment()).isVisbleEx();
            } else {
                return getParentFragment().isVisible();
            }
        }
        return false;
    }

    @Override
    public void onStop() {
        super.onStop();
        isShowing = false;
    }

    public String getTitle() {
        return "Alapos";
    }

    public int getBackgroundColor() {
        return R.color.white;
    }

    public int getHeadRightIconId() {
        return 0;
    }

    public void onRightIconClick() {

    }

    /**
     * 子类统一结束带刷新列表的处理方法
     */
    public void refreshComplete() {
        if (getRefreshLayout() == null) return;
        if (refreshType == REFRESH_TYPE_LOAD_MORE) {
            getRefreshLayout().finishLoadMore();
            refreshType = REFRESH_TYPE_DEFAULT;
        } else if (refreshType == REFRESH_TYPE_SYNC) {
            getRefreshLayout().finishRefresh();
            refreshType = REFRESH_TYPE_DEFAULT;
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

    /**
     * 蓝色左 红色右 通用对话框
     *
     * @param msg
     * @param cancelTxt  左按钮文字 默认取消
     * @param confirmTxt 右按钮文字 默认关闭
     * @param callBack
     */
    public void showCancelDialog(String msg, String cancelTxt, String confirmTxt, CommonPop.OnDialogCallBack callBack) {
        if (TextUtils.isEmpty(msg)) {
            msg = "点击关闭，则不对已编辑的信息进行保存";
        }
        new CircleDialog.Builder().setWidth(ComponentConstant.DIALOG_WIDTH_PERCENT)
                .setText(msg).setTextColor(getContext().getColor(R.color.dialog_content))
                .configText(params -> {
                    params.height = DisplayUtils.dp2px(getContext(), ComponentConstant.DIALOG_TEXT_HEIGHT_DP);
                    params.padding = dialogTxtPadding;
                })
                .setNegative(TextUtils.isEmpty(cancelTxt) ? "取消" : cancelTxt, v -> {
                    callBack.onNegative();
                    return true;
                }).setPositive(TextUtils.isEmpty(confirmTxt) ? "关闭" : confirmTxt, v -> {
            callBack.onPositive();
            return true;
        }).configNegative(params -> {
            params.textColor = getContext().getColor(R.color.dialog_cancel);
        }).configPositive(params -> {
            params.textColor = getContext().getColor(R.color.dialog_confirm);
        }).show(getActivity().getSupportFragmentManager());
    }

    public void showConfirmDialog(String msg) {
        new CircleDialog.Builder().setWidth(ComponentConstant.DIALOG_WIDTH_PERCENT)
                .setText(msg).setTextColor(getContext().getColor(R.color.dialog_content))
                .configText(params -> {
                    params.height = DisplayUtils.dp2px(getContext(), ComponentConstant.DIALOG_TEXT_HEIGHT_DP);
                    params.padding = dialogTxtPadding;
                }).setPositive("确定", v -> {
            return true;
        }).show(getActivity().getSupportFragmentManager());
    }

    public void showConfirmDialog(String title,String msg, String cancelText,int cancelColor, String confirmText,boolean isBold, CommonPop.OnDialogCallBack callBack) {
        CircleDialog.Builder builder =
        new CircleDialog.Builder().setWidth(ComponentConstant.DIALOG_WIDTH_PERCENT)
                .setTextColor(getContext().getColor(R.color.black))
                .setText(msg).setTextColor(getContext().getColor(R.color.dialog_content))
                .configText(params -> {
                    params.height = DisplayUtils.dp2px(getContext(), ComponentConstant.DIALOG_TEXT_HEIGHT_DP);
//                    params.padding = dialogTxtPadding;
                })
                .configNegative(params -> {
                    params.textColor = getContext().getColor(cancelColor);
                })
                .configPositive(params -> {
                    if (isBold) params.styleText = Typeface.BOLD;
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
                });
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
            builder.configTitle(params -> {
                params.styleText = Typeface.BOLD;
            });
        }
        builder.show(getActivity().getSupportFragmentManager());
    }

    public void showMessage(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        ArmsUtils.snackbarTextWithLong(msg);
//        ArmsUtils.makeText(getActivity(), msg);
    }
}
