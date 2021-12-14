package com.myboy.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class TomcatThreadFactory implements ThreadFactory {

    private final AtomicInteger threadNum = new AtomicInteger(1);

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r, "tomcat-thread-" + threadNum.getAndIncrement());
    }
}
