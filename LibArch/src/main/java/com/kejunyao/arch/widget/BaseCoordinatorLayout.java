package com.kejunyao.arch.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

/**
 * CoordinatorLayout基类
 *
 * @author kejunyao
 * @since 2019年11月15日
 */
public abstract class BaseCoordinatorLayout extends CoordinatorLayout {

    public BaseCoordinatorLayout(@NonNull Context context) {
        this(context, null);
    }

    public BaseCoordinatorLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseCoordinatorLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public BaseCoordinatorLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
