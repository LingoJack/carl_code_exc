import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class juc {

    /**
     * 顺序打印 
     */
    private static volatile boolean thread2Completed = false;

    public static void main(String[] args) {

        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        new Thread(() -> {
            lock.lock();
            try {
                if (!thread2Completed) {
                    condition.await();
                }
                System.out.println("1");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }, "t1").start();

        new Thread(() -> {
            lock.lock();
            try {
                System.out.println(2);
                thread2Completed = true;
                condition.signal();
            } finally {
                lock.unlock();
            }
        }, "t2").start();
    }
}
