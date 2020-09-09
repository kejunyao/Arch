package com.kejunyao.arch.thread;

import android.os.Process;

import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public final class ThreadPoolUtils {

    private static final int MAX_THREAD_COUNT = 3;

    private ThreadPoolUtils() {
    }

    private static ThreadPoolExecutor sExecutorMultiple;
    private static ThreadPoolExecutor multiple() {
        if (sExecutorMultiple == null || sExecutorMultiple.isShutdown()) {
            sExecutorMultiple = ThreadExecutors.newFixedThreadPool(MAX_THREAD_COUNT, "ThreadPoolUtils_Executor_Multiple");
        }
        return sExecutorMultiple;
    }

    private static ThreadPoolExecutor sExecutorSingle;
    private static ThreadPoolExecutor single() {
        if (sExecutorSingle == null || sExecutorSingle.isShutdown()) {
            sExecutorSingle = ThreadExecutors.newFixedThreadPool(1, "ThreadPoolUtils_Executor_Single");
        }
        return sExecutorSingle;
    }

    /**
     * 多线程处理
     * @param command {@link Runnable}
     */
    public static void runOnMultiple(final Runnable command) {
        if (ThreadUtils.isMainThread()) {
            multiple(command);
        } else {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    multiple(command);
                }
            });
        }
    }

    /**
     * 多线程处理延迟处理
     */
    public static void runOnMultiple(final Runnable command, long delayMillis) {
        ThreadUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                multiple(command);
            }
        }, delayMillis);
    }

    public static Future<?> submitOnMultiple(Runnable runnable) {
        return multiple().submit(runnable);
    }

    /**
     * 非UI单线程处理
     * @param command {@link Runnable}
     */
    public static void runOnSingle(final Runnable command) {
        if (ThreadUtils.isMainThread()) {
            single(command);
        } else {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    single(command);
                }
            });
        }
    }

    public static void runOnSingle(final Runnable command, long delayMillis) {
        ThreadUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                single(command);
            }
        }, delayMillis);
    }

    /**
     * 多线程处理, UI线程返回结果
     * @param processor {@link Processor}
     * @param <T>
     */
    public static <T> void runOnMultiple(final Processor<T> processor) {
        if (ThreadUtils.isMainThread()) {
            multiple(processor);
        } else {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    multiple(processor);
                }
            });
        }
    }

    public static <T> void runOnMultiple(final Processor<T> processor, long delayMillis) {
        ThreadUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                multiple(processor);
            }
        }, delayMillis);
    }

    /**
     * 非UI单线程处理, UI线程返回结果
     * @param processor {@link Processor}
     * @param <T>
     */
    public static <T> void runOnSingle(final Processor<T> processor) {
        if (ThreadUtils.isMainThread()) {
            single(processor);
        } else {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    single(processor);
                }
            });
        }
    }

    public static <T> void runOnSingle(final Processor<T> processor, long delayMillis) {
        ThreadUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                single(processor);
            }
        }, delayMillis);
    }

    private static void single(final Runnable r) {
        single().execute(new Runnable() {
            @Override
            public void run() {
                Process.setThreadPriority(Process.THREAD_PRIORITY_FOREGROUND);
                try {
                    r.run();
                } finally {
                    return;
                }
            }
        });
    }

    private static <T> void single(final Processor<T> processor) {
        single().execute(new Runnable() {
            @Override
            public void run() {
                Process.setThreadPriority(Process.THREAD_PRIORITY_FOREGROUND);
                T result = null;
                try {
                    result = processor.onProcess();
                } finally {
                    final T r = result;
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            processor.onResult(r);
                        }
                    });
                }
            }
        });
    }

    private static void multiple(final Runnable r) {
        multiple().execute(new Runnable() {
            @Override
            public void run() {
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                try {
                    r.run();
                } finally {
                    return;
                }
            }
        });
    }

    private static  <T>  void multiple(final Processor<T> processor) {
        multiple().execute(new Runnable() {
            @Override
            public void run() {
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                T result = null;
                try {
                    result = processor.onProcess();
                } finally {
                    final T r = result;
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            processor.onResult(r);
                        }
                    });
                }
            }
        });
    }
}
