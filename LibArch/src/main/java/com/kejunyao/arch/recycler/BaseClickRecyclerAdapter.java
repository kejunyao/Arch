package com.kejunyao.arch.recycler;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 含有Item点击事件监听的{@link RecyclerView.Adapter}基类
 *
 * @author kejunyao
 * @since 2018年04月20日
 */
public abstract class BaseClickRecyclerAdapter<T> extends BaseRecyclerAdapter<T> {

    @NonNull
    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = createRecyclerViewHolder(parent, viewType);
        if (mOnItemClickListener != null && holder instanceof BaseClickRecyclerHolder) {
            ((BaseClickRecyclerHolder) holder).setOnItemClickListener(mOnItemClickListener);
        }
        return holder;
    }

    private OnItemClickListener<T> mOnItemClickListener;
    public void setOnItemClickListener(final OnItemClickListener<T> l) {
        mOnItemClickListener = l;
    }

    @NonNull
    protected abstract RecyclerView.ViewHolder createRecyclerViewHolder(@NonNull ViewGroup parent, int viewType);
}
