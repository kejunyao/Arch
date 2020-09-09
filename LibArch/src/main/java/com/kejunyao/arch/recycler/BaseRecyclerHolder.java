package com.kejunyao.arch.recycler;

import android.view.View;

import androidx.annotation.IdRes;
import androidx.recyclerview.widget.RecyclerView;

/**
 * {@link RecyclerView.ViewHolder}基类
 *
 * @author kejunyao
 * @since 2018年04月20日
 */
public abstract class BaseRecyclerHolder<T> extends RecyclerView.ViewHolder {


    public BaseRecyclerHolder(View itemView) {
        super(itemView);
    }

    protected final <V extends View> V findViewById(@IdRes int id) {
        return itemView.findViewById(id);
    }

    public abstract void refresh(T data);
}
