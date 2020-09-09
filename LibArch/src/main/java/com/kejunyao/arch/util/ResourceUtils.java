package com.kejunyao.arch.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;

/**
 * 资源工具类
 *
 * @author kejunyao
 * @since 2020年09月09日
 */
public final class ResourceUtils {

    private ResourceUtils() {
    }

    /**
     * 将dp转为px
     * @param context {@link Context}
     * @param dp dp值
     * @return 返回dp转换后的px值
     */
    public static int dp2px(Context context, float dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }

    public static int getColor(Context context, int resId) {
        return context.getResources().getColor(resId);
    }

    public static ColorStateList getColorStateList(Context context, int resId) {
        return context.getResources().getColorStateList(resId);
    }

    public static String getString(Context context, int resId) {
        return context.getString(resId);
    }

    public static String getString(Context context, int resId, Object... formatArgs) {
        return context.getString(resId, formatArgs);
    }

    public static String[] getStringArray(Context context, int resId) {
        return context.getResources().getStringArray(resId);
    }

    public static int getDimenPixelSize(Context context, int resId) {
        return context.getResources().getDimensionPixelSize(resId);
    }

    public static int getInteger(Context context, int resId) {
        return context.getResources().getInteger(resId);
    }

    public static Drawable getDrawable(Context context, int resId) {
        return context.getResources().getDrawable(resId);
    }

    public static String toHex(float present) {
        int d = (int) (255 * present);
        return Integer.toHexString(d).toUpperCase();
    }
}
