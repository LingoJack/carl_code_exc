package leetcodeHot100;

import java.util.Timer;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class threadpool {
    public static void main(String[] args) {
        try {
            testThreadLocal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 代替Timer
     */
    private static void schecudledPool() {
        try (ScheduledThreadPoolExecutor threadPoolExecutor = new ScheduledThreadPoolExecutor(2)) {
            threadPoolExecutor.schedule(() -> {
                System.out.println("1");
            }, 0, null);
        }
    }

    /**
     * 线程池传参实验
     */
    private static void testThreadLocal() throws InterruptedException {
        InheritableThreadLocal<Integer> local = new InheritableThreadLocal<>();
        try {
            local.set(100);
            new Thread(() -> {
                System.out.println(local.get());
            }).start();
        } finally {
            TimeUnit.SECONDS.sleep(3);
            local.remove();
        }
    }
}
