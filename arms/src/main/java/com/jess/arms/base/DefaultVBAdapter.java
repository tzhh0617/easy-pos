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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

/**
 * created by syb
 * viewbind 模式基类adapter
 * ================================================
 */
public abstract class DefaultVBAdapter<T, VB extends ViewBinding> extends RecyclerView.Adapter<BaseHolderVB<T, VB>> {
    protected List<T> mInfos;
    protected OnRecyclerViewItemClickListener mOnItemClickListener = null;
    protected OnItemLongClickListener mItemLongClickListener = null;
    protected OnChildViewClickListener mChildViewListener = null;

    public static int HEAD_ITEM_TYPE = 0;
    public static int CONTENT_ITEM_TYPE = 1;
    protected int mHeadCount = 0;

    public DefaultVBAdapter(List<T> infos) {
        super();
        this.mInfos = infos;
    }

    public interface OnChildViewClickListener<T> {
        void onClick(View view, int viewType, T data, int position);
    }

    public interface OnItemLongClickListener<T> {
        void onLongClick(RecyclerView.ViewHolder vh);
    }

    /**
     * 遍历所有 {@link BaseHolder}, 释放他们需要释放的资源
     *
     * @param recyclerView {@link RecyclerView}
     */
    public static void releaseAllHolder(RecyclerView recyclerView) {
        if (recyclerView == null) {
            return;
        }
        for (int i = recyclerView.getChildCount() - 1; i >= 0; i--) {
            final View view = recyclerView.getChildAt(i);
            RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(view);
            if (viewHolder instanceof BaseHolder) {
                ((BaseHolder) viewHolder).onRelease();
            }
        }
    }

    /**
     * 创建 {@link BaseHolder}
     *
     * @param parent   父容器
     * @param viewType 布局类型
     * @return {@link BaseHolder}
     */
    @NotNull
    @Override
    public BaseHolderVB<T, VB> onCreateViewHolder(ViewGroup parent, final int viewType) {

//        View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutId(viewType), parent, false);
        BaseHolderVB<T, VB> mHolder = getHolder(LayoutInflater.from(parent.getContext()), parent, viewType);
        //设置Item点击事件
        if (mOnItemClickListener != null && mInfos.size() > 0) {
            mHolder.setOnItemClickListener((view1, position) -> {
                //noinspection unchecked
                mOnItemClickListener.onItemClick(view1, viewType, mInfos.get(position), position);
            });
        }
        if (mItemLongClickListener != null && mInfos.size() > 0) {
            mHolder.itemView.setOnLongClickListener(view -> {
                mItemLongClickListener.onLongClick(mHolder);
                return true;
            });
        }

        return mHolder;
    }

    public void setData(List<T> mInfos) {
        this.mInfos = mInfos;
        notifyDataSetChanged();
    }


    /**
     * 绑定数据
     *
     * @param holder   {@link BaseHolder}
     * @param position 在 RecyclerView 中的位置
     */
    @Override
    public void onBindViewHolder(BaseHolderVB<T, VB> holder, int position) {
        if (getItemViewType(position) == CONTENT_ITEM_TYPE) {
            holder.setData(holder.getBinding(), mInfos.get(position - mHeadCount), position - mHeadCount);
            return;
        }
        holder.setData(holder.getBinding(), mInfos.size() > 0 ? mInfos.get(position) : null, position);
    }

    /**
     * 返回数据总个数
     *
     * @return 数据总个数
     */
    @Override
    public int getItemCount() {
        return mInfos.size() + mHeadCount;
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeadCount != 0 && position < mHeadCount) {
            return HEAD_ITEM_TYPE;
        }
        return CONTENT_ITEM_TYPE;
    }

    /**
     * 返回数据集合
     *
     * @return 数据集合
     */
    public List<T> getInfos() {
        return mInfos;
    }

    /**
     * 获得 RecyclerView 中某个 position 上的 item 数据
     *
     * @param position 在 RecyclerView 中的位置
     * @return 数据
     */
    public T getItem(int position) {
        return mInfos == null ? null : mInfos.get(position);
    }

    /**
     * 让子类实现用以提供 {@link BaseHolder}
     *
     * @param viewType 布局类型
     * @return {@link BaseHolder}
     */
    public abstract BaseHolderVB<T, VB> getHolder(LayoutInflater layoutInflater, ViewGroup parent, int viewType);

    /**
     * 提供用于 item 布局的 {@code layoutId}
     *
     * @param viewType 布局类型
     * @return 布局 id
     */
//    public abstract int getLayoutId(int viewType);

    /**
     * 设置 item 点击事件
     *
     * @param listener
     */
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setChildViewListener(OnChildViewClickListener mChildViewListener) {
        this.mChildViewListener = mChildViewListener;
    }

    /**
     * item 点击事件
     *
     * @param <T>
     */
    public interface OnRecyclerViewItemClickListener<T> {

        /**
         * item 被点击
         *
         * @param view     被点击的 {@link View}
         * @param viewType 布局类型
         * @param data     数据
         * @param position 在 RecyclerView 中的位置
         */
        void onItemClick(View view, int viewType, T data, int position);
    }

    public void setItemLongClickListener(OnItemLongClickListener mItemLongClickListener) {
        this.mItemLongClickListener = mItemLongClickListener;
    }
}
