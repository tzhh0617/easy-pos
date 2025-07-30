/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jess.arms.base;

import android.view.View;

import com.jakewharton.rxbinding2.view.RxView;
import com.jess.arms.utils.ThirdViewUtil;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.concurrent.TimeUnit;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

/**
 * ================================================
 * 基类 {@link RecyclerView.ViewHolder}
 * <p>
 * Created by JessYan on 2015/11/24.
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * * Version    Date       ModifiedBy                 Content
 * * 1.0      2022/1/27       yuanbo.shu            移除ButterKnife  viewbind版
 * ================================================
 */
public abstract class BaseHolderVB<T, VB extends ViewBinding> extends RecyclerView.ViewHolder  {
    protected final String TAG = this.getClass().getSimpleName();
    protected OnViewClickListener mOnViewClickListener = null;
    protected VB binding;

    public BaseHolderVB(VB binding) {
        super(binding.getRoot());
        this.binding = binding;
        //点击事件
        RxView.clicks(itemView).throttleFirst(1200, TimeUnit.MILLISECONDS)
                        .subscribe(o -> {
                            if (mOnViewClickListener != null) {
                                mOnViewClickListener.onViewClick(itemView, this.getPosition());
                            }
                        });
        //屏幕适配
        if (ThirdViewUtil.isUseAutolayout()) {
            AutoUtils.autoSize(itemView);
        }
        //绑定 ButterKnife
//        ThirdViewUtil.bindTarget(this, itemView);
    }

    /**
     * 设置数据
     *
     * @param data     数据
     * @param position 在 RecyclerView 中的位置
     */
    public abstract void setData(VB binding, T data, int position);

    /**
     * 在 Activity 的 onDestroy 中使用 {@link DefaultAdapter#releaseAllHolder(RecyclerView)} 方法 (super.onDestroy() 之前)
     * {@link BaseHolderVB#onRelease()} 才会被调用, 可以在此方法中释放一些资源
     */
    protected void onRelease() {

    }

    public void setOnItemClickListener(OnViewClickListener listener) {
        this.mOnViewClickListener = listener;
    }

    public VB getBinding() {
        return binding;
    }

    /**
     * item 点击事件
     */
    public interface OnViewClickListener {

        /**
         * item 被点击
         *
         * @param view     被点击的 {@link View}
         * @param position 在 RecyclerView 中的位置
         */
        void onViewClick(View view, int position);
    }
}