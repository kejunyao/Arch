package com.kejunyao.arch.thread;

import android.os.Handler;
import android.os.Looper;

/**
 * 线程工具类
 *
 * @author kejunyao
 * @since 2018年06月07日.
 */
public final class ThreadUtils {

    private static Handler sMainThreadHandler = new Handler(Looper.getMainLooper());

    private ThreadUtils() {
    }

    /**
     * 在UI线程中执行
     * @param command {@link Runnable}
     */
    public static void runOnUiThread(final Runnable command) {
        sMainThreadHandler.post(command);
    }

    /**
     * 在UI线程中执行
     * @param command {@link Runnable}
     */
    public static void runOnUiThread(final Runnable command, long delayMillis) {
        sMainThreadHandler.postDelayed(command, delayMillis);
    }

    /**
     * 移除UI线程中的Callback
     * @param r {@link Runnable}
     */
    public static void removeUiCallbacks(Runnable r) {
        if (r == null) {
            return;
        }
        sMainThreadHandler.removeCallbacks(r);
    }

    public static boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    public static void checkMain() {
        if (Looper.getMainLooper().getThread() != Thread.currentThread()) {
            throw new IllegalStateException("Method call should happen from the main thread.");
        }
    }
}
