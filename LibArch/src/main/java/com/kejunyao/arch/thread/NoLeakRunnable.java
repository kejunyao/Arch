package com.kejunyao.arch.thread;

import java.lang.ref.WeakReference;

/**
 * 弱引用{@link Runnable}，防止内存泄漏
 *
 * @author kejunyao
 * @since 2018年07月31日
 */
public abstract class NoLeakRunnable<T> implements Runnable {

    private final WeakReference<T> ref;

    public NoLeakRunnable(T t) {
        if (t == null) {
            ref = null;
        } else {
            ref = new WeakReference<>(t);
        }
    }

    public T get() {
        return ref == null ? null : ref.get();
    }
}
