package com.kejunyao.arch.util;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.view.View;

/**
 * Android四大组件{@link android.app.Activity}工具类
 *
 * @author kejunyao
 * @since 2020年09月06日
 */
public final class ActivityUtils {

    private ActivityUtils() {
    }

    /**
     * {@link android.app.Activity}是否已经finish
     * @param activity {@link android.app.Activity}
     * @return true，已finish；false，未finish
     */
    public static boolean isFinishing(Activity activity) {
        return activity == null || activity.isFinishing()
                || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed());
    }

    /**
     * 尝试获取{@link android.app.Activity}
     * @param context {@link Context}
     * @return {@link android.app.Activity}
     */
    public static Activity getActivity(Context context) {
        if (context instanceof Activity) {
            return (Activity) context;
        }
        while (context instanceof ContextWrapper) {
            context = ((ContextWrapper) context).getBaseContext();
            if (context instanceof Activity) {
                return (Activity) context;
            }
        }
        return null;
    }

    /**
     * 假如{@link Context}为{@link android.app.Activity}，则可以使用本方法判断其是否finish
     * @param context {@link Context}
     * @return true，已finish；false，未finish（Context如果不为Activity也是为未finish）
     */
    public static boolean isFinishing(Context context) {
        if (context == null) {
            return true;
        }
        Activity activity = getActivity(context);
        return isFinishing(activity);
    }

    /**
     * 隐藏{@link Activity}底部虚拟菜单
     * @param activity {@link Activity}
     */
    public static void hideVirtualMenu(Activity activity) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            View view = activity.getWindow().getDecorView();
            view.setSystemUiVisibility(View.GONE);
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View decorView = activity.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
}
