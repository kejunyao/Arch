package com.kejunyao.arch.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadExecutors {

    private static class MyThreadFactory implements ThreadFactory {

        private static final AtomicInteger mPoolNumber = new AtomicInteger(1);
        private final ThreadGroup mGroup;
        private final AtomicInteger mThreadNumber = new AtomicInteger(1);
        private final String mNamePrefix;
        private final int mPriority;

        public MyThreadFactory(String name, int priority) {
            SecurityManager s = System.getSecurityManager();
            this.mGroup = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            this.mNamePrefix = "Pool-" + mPoolNumber.getAndIncrement() + "-" + name + "-";
            this.mPriority = priority;
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(mGroup, r, mNamePrefix + mThreadNumber.getAndIncrement(), 0);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != mPriority)
                t.setPriority(mPriority);
            return t;
        }
    }

    // Cached Thread Pool
    public static ThreadPoolExecutor newLowPriorityCachedThreadPool(int maximumPoolSize,
                                                                    int maximumQueueCapacity,
                                                                    String name) {
        return newCachedThreadPool(maximumPoolSize,
                maximumQueueCapacity, Thread.MIN_PRIORITY + 1, name);
    }

    public static ThreadPoolExecutor newCachedThreadPool(int maximumPoolSize,
                                                         int maximumQueueCapacity,
                                                         int priority,
                                                         String name) {
        long keepAliveTime = 60l;
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(maximumQueueCapacity);
        ThreadFactory factory = new MyThreadFactory(name, priority);
        RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardOldestPolicy();

        ThreadPoolExecutor executor = new ThreadPoolExecutor(maximumPoolSize, maximumPoolSize, keepAliveTime,
                unit, workQueue, factory, handler);
        executor.allowCoreThreadTimeOut(true);
        return executor;
    }

    // Fixed Thread Pool
    public static ThreadPoolExecutor newFixedThreadPool(int maximumPoolSize, String name) {
        return newFixedThreadPool(maximumPoolSize, maximumPoolSize, Thread.NORM_PRIORITY, name);
    }

    public static ThreadPoolExecutor newFixedThreadPool(int corePoolSize,
                                                        int maximumPoolSize,
                                                        int priority,
                                                        String name) {
        long keepAliveTime = 60L;
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();
        ThreadFactory factory = new MyThreadFactory(name, priority);
        RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy();

        return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime,
                unit, workQueue, factory, handler);
    }

}
