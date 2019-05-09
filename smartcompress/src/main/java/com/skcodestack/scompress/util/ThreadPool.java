package com.skcodestack.scompress.util;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池 工具
 */
public class ThreadPool {

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));
    private final ThreadPoolExecutor poolExecutor;

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "SmartCompress #" + mCount.getAndIncrement());
        }
    };

    /**
     * 获取单列
     * @return  ThreadPool
     */
    public static ThreadPool getInstance() {
        return Singleton.INSTANCE;
    }

    public ThreadPool() {
        poolExecutor = new ThreadPoolExecutor(0, CORE_POOL_SIZE, 3000, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>(), sThreadFactory);
    }

    public void execute(Runnable runnable) {
        if (poolExecutor != null) {
            poolExecutor.execute(runnable);
        }
    }

    /**
     * 立刻停止所有的任务
     */
    public void shutdownNow() {
        if (poolExecutor != null) {
            poolExecutor.shutdownNow();
        }
    }

    /**
     * 停止所有的任务
     */
    public void shutdown() {
        if (poolExecutor != null) {
            poolExecutor.shutdown();
        }
    }

    private static class Singleton {
        public static final ThreadPool INSTANCE = new ThreadPool();
    }
}
