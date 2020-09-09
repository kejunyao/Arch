package com.kejunyao.arch.util;

import android.app.Activity;
import android.graphics.Paint;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import com.google.android.material.appbar.AppBarLayout;

/**
 * UI工具类
 *
 * @author kejunyao
 * @since 2020年09月06日
 */
public final class UIUtils {

    private UIUtils() {
    }

    public static boolean isVisible(View view) {
        return view != null && view.getVisibility() == View.VISIBLE;
    }

    public static void removeRule(RelativeLayout.LayoutParams lp, int verb) {
        if (lp == null) {
            return;
        }
        lp.addRule(verb, 0);
    }

    public static void addRule(RelativeLayout.LayoutParams lp, int verb) {
        if (lp == null) {
            return;
        }
        lp.addRule(verb);
    }

    public static void setViewWidth(View view, int width) {
        if (view == null) {
            return;
        }
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp != null && lp.width != width) {
            lp.width = width;
            view.requestLayout();
        }
    }

    public static void setPaddingLeft(View view, int padding) {
        if (view == null) {
            return;
        }
        if (view.getPaddingLeft() != padding) {
            view.setPadding(padding, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
        }
    }

    public static void setPaddingLeftAndRight(View view, int paddingLeft, int paddingRight) {
        if (view == null) {
            return;
        }
        if (view.getPaddingLeft() != paddingLeft || view.getPaddingRight() != paddingRight) {
            view.setPadding(paddingLeft, view.getPaddingTop(), paddingRight, view.getPaddingBottom());
        }
    }

    public static void setPaddingTop(View view, int padding) {
        if (view == null) {
            return;
        }
        if (view.getPaddingTop() != padding) {
            view.setPadding(view.getPaddingLeft(), padding, view.getPaddingRight(), view.getPaddingBottom());
        }
    }

    public static void setPaddingBottom(View view, int padding) {
        if (view == null) {
            return;
        }
        if (view.getPaddingBottom() != padding) {
            view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), padding);
        }
    }

    public static void setPaddingRight(View view, int padding) {
        if (view == null) {
            return;
        }
        if (view.getPaddingRight() != padding) {
            view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), padding, view.getPaddingBottom());
        }
    }

    public static void setMarginLeft(View view, int marginLeft) {
        if (view == null) {
            return;
        }
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) lp;
            if (mlp.leftMargin != marginLeft) {
                mlp.leftMargin = marginLeft;
                view.requestLayout();
            }
        }
    }

    public static void setMarginRight(View view, int marginRight) {
        if (view == null) {
            return;
        }
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) lp;
            if (mlp.rightMargin != marginRight) {
                mlp.rightMargin = marginRight;
                view.requestLayout();
            }
        }
    }

    public static void setMarginTop(View view, int marginTop) {
        if (view == null) {
            return;
        }
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) lp;
            if (mlp.topMargin != marginTop) {
                mlp.topMargin = marginTop;
                view.requestLayout();
            }
        }
    }

    public static void setMarginBottom(View view, int marginBottom) {
        if (view == null) {
            return;
        }
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) lp;
            if (mlp.bottomMargin != marginBottom) {
                mlp.bottomMargin = marginBottom;
                view.requestLayout();
            }
        }
    }

    public static void setViewSize(@Nullable View view, int width, int height) {
        if (view == null) {
            return;
        }
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        boolean change = false;
        if (lp != null) {
            if (lp.width != width) {
                lp.width = width;
                change = true;
            }
            if (lp.height != height) {
                lp.height = height;
                change = true;
            }
        }
        if (change) {
            view.requestLayout();
        }
    }

    public static void setFakeBoldText(TextView textView, boolean fakeBoldText) {
        if (textView != null) {
            Paint paint = textView.getPaint();
            if (paint != null) {
                paint.setFakeBoldText(fakeBoldText);
            }
        }
    }

    public static void disabledChangeAnimations(RecyclerView recycler) {
        if (recycler == null) {
            return;
        }
        RecyclerView.ItemAnimator animator = recycler.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            animator.setAddDuration(0);
            animator.setMoveDuration(0);
            animator.setRemoveDuration(0);
            animator.setChangeDuration(0);
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
    }

    public static void setCanDrag(AppBarLayout layout, final boolean canDrag) {
        ViewGroup.LayoutParams lps = layout.getLayoutParams();
        if (lps instanceof CoordinatorLayout.LayoutParams) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) lps;
            CoordinatorLayout.Behavior behavior = layoutParams.getBehavior();
            if (behavior instanceof AppBarLayout.Behavior) {
                AppBarLayout.Behavior b = (AppBarLayout.Behavior) behavior;
                b.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
                    @Override
                    public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                        return canDrag;
                    }
                });
            }
        }
    }

    public static void setCanScroll(AppBarLayout layout, boolean canScroll) {
        if (layout.getChildCount() <= 0) {
            return;
        }
        View view = layout.getChildAt(0);
        ViewGroup.LayoutParams lps = view.getLayoutParams();
        if (lps instanceof AppBarLayout.LayoutParams) {
            AppBarLayout.LayoutParams apl = (AppBarLayout.LayoutParams) lps;
            if (canScroll) {
                apl.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
                view.setLayoutParams(apl);
            } else {
                apl.setScrollFlags(0);
            }
        }
    }

    /**
     * 设置9.0全屏
     * @param activity {@link Activity}
     */
    public static void fullScreenWithP(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            activity.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            );
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            activity.getWindow().setAttributes(lp);
        }
    }

}
