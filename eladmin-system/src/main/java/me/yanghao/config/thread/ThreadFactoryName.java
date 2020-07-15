package me.yanghao.config.thread;

import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author YangHao
 * @ClassName: ThreadFactoryName
 * @Description: 自定义线程名称
 * @date 2020/7/15 17:37
 * @Version V1.0
 */
@Component
public class ThreadFactoryName implements ThreadFactory {


    private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);

    private final ThreadGroup group;

    private final AtomicInteger threadNumber = new AtomicInteger(1);

    private final String namePrefix;

    public ThreadFactoryName() {
        this("el-pool");
    }

    private ThreadFactoryName(String name) {
        SecurityManager s = System.getSecurityManager();
        group = (s == null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        //此时nameprefix 就是 name + 用这个工厂创建线程池的
        this.namePrefix = name + POOL_NUMBER.getAndIncrement();
    }

    @Override
    public Thread newThread(Runnable r) {
        //此时nameprefix 就是 name + 用这个工厂创建线程池的
        Thread t = new Thread(group,r,namePrefix + "-thread-" + threadNumber.getAndIncrement());

        if (t.isDaemon())
            t.setDaemon(false);

        if (t.getPriority() != Thread.NORM_PRIORITY)
            t.setPriority(Thread.NORM_PRIORITY);

        return t;
    }
}
