package com.kejunyao.arch.recycler;


import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

/**
 * {@link RecyclerView.ViewHolder}基类
 *
 * @author kejunyao
 * @since 2018年04月20日
 */
public abstract class BaseClickRecyclerHolder<T> extends BaseRecyclerHolder<T> {

    private int[] mClickIds;

    public BaseClickRecyclerHolder(View itemView) {
        super(itemView);
    }

    private T mData;
    @Override
    public void refresh(T data) {
        mData = data;
    }

    /**
     * 设置itemView（包含itemView自身）的点击事件
     * 设置步骤：
     * 1、清除之前设置的Listener；
     * 2、假如bindClickIds() == null，只给itemView设置点击事件
     * 3、假如bindClickIds() != null，则给其中每一个id对应的View设置点击事件监听。
     * @param l {@link OnItemClickListener<T>}
     */
    public void setOnItemClickListener(final OnItemClickListener<T> l) {
        int itemViewId = itemView.getId();
        for (int i = 0, size = mClickIds == null ? 0 : mClickIds.length; i < size; i++) {
            int id = mClickIds[i];
            if (itemViewId == itemView.getId()) {
                itemView.setOnClickListener(null);
            } else {
                View view = itemView.findViewById(id);
                if (view != null) {
                    view.setOnClickListener(null);
                }
            }
        }
        if (itemViewId == 0) {
            itemView.setOnClickListener(null);
        }
        if (l == null) {
            mClickIds = null;
        } else {
            mClickIds = l.bindClickIds();
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    l.onClick(v.getId(), getAdapterPosition(), mData);
                }
            };
            if (mClickIds == null) {
                itemView.setOnClickListener(listener);
            } else {
                for (int id : mClickIds) {
                    if (id == itemViewId) {
                        itemView.setOnClickListener(listener);
                    } else {
                        View view = itemView.findViewById(id);
                        if (view != null) {
                            view.setOnClickListener(listener);
                        }
                    }
                }
            }
        }
    }

}
