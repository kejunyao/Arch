package com.kejunyao.arch.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * LinearLayout基类
 *
 * @author kejunyao
 * @since 2018年05月09日
 */
public abstract class BaseLinearLayout extends LinearLayout {

    public BaseLinearLayout(@NonNull Context context) {
        this(context, null);
    }

    public BaseLinearLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseLinearLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public BaseLinearLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this(context, attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs) {
        inflate(getContext(), layoutId(), this);
        findViews();
        setAttrs(attrs);
    }

    protected abstract int layoutId();

    protected abstract void findViews();

    protected abstract void setAttrs(AttributeSet attrs);

}
