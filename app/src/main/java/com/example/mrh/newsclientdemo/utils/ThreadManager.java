package com.example.mrh.newsclientdemo.utils;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程管理
 * Created by MR.H on 2016/6/22 0022.
 */
public class ThreadManager {
    private static ThreadPool threadPool;
    private ThreadManager (){

    }
    public static ThreadPool getThreadPool(){
        if (threadPool == null){
            synchronized (ThreadManager.class){
                if (threadPool == null){
//                    int i = Runtime.getRuntime().availableProcessors();
                    threadPool= new ThreadPool(10, 10, 1L);//这里外部类可以new内部类，虽然构造函数已经私有了
                }
            }
        }
        return threadPool;
    }
    public static class ThreadPool{

        private int corePoolSize;
        private int maximumPoolSize;
        private long keepAliveTime;
        private ThreadPoolExecutor executor;

        private ThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime){
            this.corePoolSize = corePoolSize;
            this.maximumPoolSize = maximumPoolSize;
            this.keepAliveTime = keepAliveTime;
        }
        //开启线程
        public void startThread(Runnable r){
            if (executor == null){
                executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
                        keepAliveTime, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());
            }
            executor.execute(r);
        }
        //关闭线程
        public void cancelThread (Runnable r){
            if (executor != null){
                executor.getQueue().remove(r);
            }
        }
    }
}