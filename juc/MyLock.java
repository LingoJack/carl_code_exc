package juc;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

/**
 * 手动实现Lock
 */
public class MyLock {

    private static class Node {
        volatile Thread thread;
        volatile Node prev;
        volatile Node next;

        Node(Thread thread) {
            this.thread = thread;
        }
    }

    private final AtomicInteger state = new AtomicInteger(0);
    private volatile Thread owner;
    private final boolean fair;
    private final AtomicReference<Node> head;
    private final AtomicReference<Node> tail;

    public MyLock() {
        this(false);
    }

    public MyLock(boolean fair) {
        this.fair = fair;
        Node dummy = new Node(null);
        this.head = new AtomicReference<>(dummy);
        this.tail = new AtomicReference<>(dummy);
    }

    public void lock() {
        // 处理重入
        if (owner == Thread.currentThread()) {
            state.incrementAndGet();
            return;
        }
        // 非公平锁先尝试直接获取
        if (!fair && state.compareAndSet(0, 1)) {
            owner = Thread.currentThread();
            return;
        }
        // 创建新节点并加入队列
        Node node = new Node(Thread.currentThread());
        Node prev = tail.getAndSet(node);
        prev.next = node;
        node.prev = prev;
        // 等待直到成为队列第一个有效节点
        while (node.prev != head.get()) {
            LockSupport.park();
        }
        // 自旋获取锁（处理可能的竞争）
        while (true) {
            if (state.compareAndSet(0, 1)) {
                owner = Thread.currentThread();
                // 更新头节点并断开链接
                head.set(node);
                node.prev = null;
                return;
            }
            LockSupport.park();
        }
    }

    public void unlock() {
        if (Thread.currentThread() != owner) {
            throw new IllegalMonitorStateException();
        }
        int i = state.get();
        if (i <= 0) {
            throw new IllegalMonitorStateException();
        }
        if (state.decrementAndGet() != 0) {
            return; // 仍有重入计数
        }
        owner = null;
        Node h = head.get();
        if (h.next != null) {
            LockSupport.unpark(h.next.thread);
        }
    }
}