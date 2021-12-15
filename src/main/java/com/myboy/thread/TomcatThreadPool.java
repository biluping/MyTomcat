package com.myboy.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TomcatThreadPool {

    private static Logger logger = LoggerFactory.getLogger(TomcatThreadPool.class);
    private static ThreadPoolExecutor executor;

    public static void init(Properties properties) {
        // 创建线程池
        int core = Integer.parseInt(properties.getProperty("thread.pool.core", "100"));
        int max = Integer.parseInt(properties.getProperty("thread.pool.max", "200"));
        int aliveTime = Integer.parseInt(properties.getProperty("thread.pool.alive-time", "60"));
        int blockSize = Integer.parseInt(properties.getProperty("thread.pool.block-queue.size", "100"));
        executor = new ThreadPoolExecutor(
                core,
                max,
                aliveTime,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(blockSize),
                new TomcatThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );

        if (logger.isDebugEnabled()) {
            logger.debug("线程池创建成功\n" +
                    "核心线程数：{}\n" +
                    "最大线程数：{}\n" +
                    "阻塞队列容量：{}\n" +
                    "空闲线程存活时间：{}秒", core, max, blockSize, aliveTime);
        }
    }

    public static ThreadPoolExecutor getExecutor() {
        if (executor == null) {
            throw new NullPointerException("线程池未创建，请先调用 init() 方法");
        }
        return executor;
    }

}
