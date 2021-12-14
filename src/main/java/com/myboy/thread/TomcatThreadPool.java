package com.myboy.thread;

import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TomcatThreadPool {

    private static ThreadPoolExecutor executor;

    public static void init(Properties properties){
        // 创建线程池
        int core = Integer.parseInt(properties.getProperty("thread.pool.core", "100"));
        int max = Integer.parseInt(properties.getProperty("thread.pool.max", "200"));
        int aliveTime = Integer.parseInt(properties.getProperty("thread.pool.alive-time", "60"));
        executor = new ThreadPoolExecutor(
                core,
                max,
                aliveTime,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                new TomcatThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy()
                );
    }

    public static ThreadPoolExecutor getExecutor(){
        if (executor == null){
            throw new NullPointerException("线程池未创建，请先调用 init() 方法");
        }
        return executor;
    }

}
