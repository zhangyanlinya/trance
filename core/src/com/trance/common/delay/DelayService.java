package com.trance.common.delay;

import com.trance.view.utils.MsgUtil;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;

public class DelayService {


    private static volatile DelayService service;

    private DelayService() {
        start();
    }

    public static DelayService getIntance() {
        if (service == null) {
            synchronized (DelayService.class) {
                if (service == null) {
                    service = new DelayService();
                }
            }
        }
        return service;
    }

    private final BlockingQueue<DelayItem<Runnable>> blockingQueue = new DelayQueue<DelayItem<Runnable>>();

    public volatile boolean running = false;

    /**
     * 线程
     */
    private Thread thread;

    /**
     * 初始化
     */
    public void start() {
        running = true;
        thread = new Thread() {
            public void run() {
                while (running || blockingQueue.size() > 0) {
                    try {
                        DelayItem<Runnable> delay = blockingQueue.take();
                        delay.getTask().run();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.setName("延时处理线程");
        thread.start();
    }

    /**
     * 提交延迟对象
     *
     * @param item
     */
    public void submit(DelayItem<Runnable> item) {
        if(!running){
            start();
        }
        if (blockingQueue.contains(item)) {
            return;
        }
        blockingQueue.offer(item);
    }

    public void flush() {
        for (DelayItem<?> delay : blockingQueue) {
            delay.setExpire(0); // 立即执行
        }
    }

    public boolean isEmpty() {
        return blockingQueue.isEmpty();
    }

    public void shutdown() {
        if (!isEmpty()) {
            flush();
            running = false;
            // 改成同步执行
            for (DelayItem<Runnable> delay : blockingQueue) {
                delay.getTask().run();
            }
            blockingQueue.clear();
        }
    }
}
