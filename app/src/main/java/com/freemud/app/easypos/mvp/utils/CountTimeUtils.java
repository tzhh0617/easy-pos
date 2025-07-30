package com.freemud.app.easypos.mvp.utils;

import android.os.CountDownTimer;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * Created by shuyuanbo on 2021/7/14.
 * Description:
 */
public class CountTimeUtils extends CountDownTimer {

    private WeakReference<TextView> mTextView;
    //设置的事件
    private OnTimeFinishListener mOnTimeFinishListener;


    public interface OnTimeFinishListener {
        void onTimeFinish();
    }

    public void setmOnTimeFinishListener(OnTimeFinishListener mOnTimeFinishListener) {
        this.mOnTimeFinishListener = mOnTimeFinishListener;
    }

    public CountTimeUtils(TextView textView, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.mTextView = new WeakReference<>(textView);
    }

    @Override
    public void onTick(long l) {
        //用弱引用 先判空 避免崩溃
        if (mTextView.get() == null) {
            doCancel();
            return;
        }
        mTextView.get().setClickable(false); //设置不可点击
//        mTextView.get().setEnabled(false);
        mTextView.get().setText((int) (l / 1000) + "s后重新获取"); //设置倒计时时间
    }

    @Override
    public void onFinish() {
        //用软引用 先判空 避免崩溃
        if (mTextView.get() == null) {
            doCancel();
            return;
        }
        mTextView.get().setText("重发验证码");
        if (mOnTimeFinishListener != null) {
            mOnTimeFinishListener.onTimeFinish();
        }
        mTextView.get().setClickable(true);//重新获得点击
//        mTextView.get().setEnabled(true);
    }

    public void doCancel() {
        if (this != null) {
            this.cancel();
        }
    }
}
