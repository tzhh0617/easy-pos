package com.freemud.app.easypos.mvp.widget.common;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.freemud.app.easypos.R;
import com.github.ybq.android.spinkit.sprite.SpriteContainer;
import com.github.ybq.android.spinkit.style.Wave;

import androidx.annotation.NonNull;

/**
 * Created by shuyuanbo on 2020/9/16.
 * Description:
 */
public class LoadingDialog extends Dialog {
    private Context mContext;
    private ImageView mIv;
    private TextView mTv;
    private RelativeLayout mRl;
    private SpriteContainer animPic;

    public LoadingDialog(@NonNull Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public void initProperty(int x, int y, int w, int h) {
        Window window = getWindow();//得到对话框的窗口．
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = x;//设置对话框的位置．0为中间
        wl.y = y;
        wl.width = w;
        wl.height = h;
        wl.alpha = 1f;// 设置对话框的透明度,1f不透明
        wl.gravity = Gravity.CENTER;//设置显示在中间
        window.setAttributes(wl);
        window.setBackgroundDrawableResource(R.color.white);
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.loading_default, null);
        mIv = view.findViewById(R.id.view_loading_default_iv);
        mTv = view.findViewById(R.id.view_loading_default_tv);
        mRl = view.findViewById(R.id.view_loading_default_ll);
        animPic = new Wave();
//        animPic = new FadingCircle();
        animPic.setColor(mContext.getColor(R.color.white));
        mIv.setImageDrawable(animPic);
        mRl.setBackgroundColor(mContext.getColor(R.color.blue_d3));
//        mRl.setBackgroundColor(mContext.getColor(R.color.transparent));
        this.setContentView(view);
//        this.setCancelable(false);
        this.setCanceledOnTouchOutside(false);
    }

    @Override
    public void show() {
        super.show();
        initProperty(0, 0, 300, 200);
        if (animPic != null) {
            animPic.start();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (animPic != null) {
            animPic = null;
        }
    }
}
