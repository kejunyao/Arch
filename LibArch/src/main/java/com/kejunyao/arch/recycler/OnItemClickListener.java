package com.kejunyao.arch.recycler;

import androidx.annotation.IdRes;
import androidx.recyclerview.widget.RecyclerView;

/**
 * ViewHolder中各个View被点击的回调Listener
 *
 * @author kejunyao
 * @since 2021年02月10日
 */
public interface OnItemClickListener<T> {
    /**
     * 绑定被点击View的id
     * @return 返回值为null时，{@link OnItemClickListener#onClick(int, Object)}回调当前{@link RecyclerView.ViewHolder}对应的ItemView
     */
    int[] bindClickIds();

    /**
     * 点击事件回调
     */
    void onClick(@IdRes int id, int position, T data);
}
