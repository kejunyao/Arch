package com.kejunyao.arch.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

/**
 * {@link RecyclerView.ViewHolder}工具类
 */
public final class ViewHolderUtils {

    private ViewHolderUtils() {
    }

    /**
     * inflate{@link RecyclerView.ViewHolder}所需要的工具类
     */
    public static View inflate(ViewGroup parent, int layoutId) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
    }
}
