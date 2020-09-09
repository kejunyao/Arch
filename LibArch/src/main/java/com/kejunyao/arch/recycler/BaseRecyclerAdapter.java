package com.kejunyao.arch.recycler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.kejunyao.arch.util.Utility;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter}基类
 *
 * @author kejunyao
 * @since 2018年04月20日
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter {

    protected List<T> mData;

    public BaseRecyclerAdapter() {
    }

    @Nullable
    public List<T> getData() {
        return mData;
    }

    public void setData(List<T> data) {
        this.mData = data;
    }

    public void setData(int dataIndex, T data, int itemPosition) {
        if (data != null && mData != null && !mData.isEmpty()) {
            mData.set(dataIndex, data);
            notifyItemChanged(itemPosition);
        }
    }

    public void addData(List<T> data, boolean needRefresh) {
        if (data == null || data.isEmpty()) {
            return;
        }
        if (mData == null) {
            mData = new ArrayList<>();
            mData.addAll(data);
            if (needRefresh) {
                notifyDataSetChanged();
            }
        } else {
            int positionStart = mData.size();
            mData.addAll(data);
            if (needRefresh) {
                notifyItemRangeInserted(positionStart, data.size());
            }
        }
    }

    public void addData(List<T> data) {
        if (data == null || data.isEmpty()) {
            return;
        }
        if (mData == null) {
            mData = new ArrayList<>();
        }
        mData.addAll(data);
        notifyDataSetChanged();
    }

    /**
     * 防重复加入
     */
    public void addDataDistinct(List<T> data) {
        if (data == null || data.isEmpty()) {
            return;
        }
        if (mData == null) {
            mData = new ArrayList<>();
            mData.addAll(data);
        } else {
            for (T t : data) {
                if (t == null) {
                    continue;
                }
                if (mData.contains(t)) {
                    continue;
                }
                mData.add(t);
            }
        }
        notifyDataSetChanged();
    }

    public void addData(T data, boolean needRefresh) {
        if (data == null) {
            return;
        }
        if (mData == null) {
            mData = new ArrayList<>();
            mData.add(data);
            if (needRefresh) {
                notifyDataSetChanged();
            }
        } else {
            int positionStart = mData.size();
            mData.add(data);
            if (needRefresh) {
                notifyItemRangeInserted(positionStart, 1);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BaseRecyclerHolder) {
            ((BaseRecyclerHolder) holder).refresh(getItem(position));
        }
    }

    public void remove(int dataIndex, int itemPosition) {
        if (mData == null) {
            return;
        }
        if (dataIndex >= 0 && dataIndex < mData.size()) {
            mData.remove(dataIndex);
            notifyItemRemoved(itemPosition);
        }
    }

    public void remove(@NonNull T data, int itemPosition) {
        if (mData == null || mData.isEmpty()) {
            return;
        }
        boolean isRemoved = mData.remove(data);
        if (isRemoved) {
            notifyItemRemoved(itemPosition);
        }
    }

    public boolean insert(@NonNull T data, int dataIndex, boolean dealOutOfBounds) {
        if (mData == null) {
            return false;
        }
        if (dataIndex >= 0 && dataIndex <= mData.size()) {
            mData.add(dataIndex, data);
            notifyItemInserted(dataIndex);
            return true;
        }
        if (dealOutOfBounds) {
            if (dataIndex >= mData.size()) {
                mData.add(data);
                notifyItemInserted(mData.size() - 1);
                return true;
            }
            if (dataIndex < 0) {
                mData.add(0, data);
                notifyItemInserted(0);
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean insert(@NonNull T data, int dataIndex) {
        return insert(data, dataIndex, false);
    }

    public boolean insert(@NonNull List<T> data, int dataIndex) {
        if (mData == null) {
            return false;
        }
        if (dataIndex >= 0 && dataIndex <= mData.size()) {
            mData.addAll(dataIndex, data);
            notifyItemRangeInserted(dataIndex, data.size());
            return true;
        }
        return false;
    }

    public boolean replace(@NonNull T data, int dataIndex) {
        if (mData == null) {
            return false;
        }
        if (dataIndex >= 0 && dataIndex <= mData.size()) {
            mData.set(dataIndex, data);
            notifyItemChanged(dataIndex);
            return true;
        }
        return false;
    }

    public void clear() {
        if (mData != null) {
            mData.clear();
        }
    }

    @Nullable
    public T getItem(int dataIndex) {
        if (mData != null && dataIndex >= 0 && dataIndex < mData.size()) {
            return mData.get(dataIndex);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return Utility.getSizeSafely(mData);
    }
}
