package leetcodeHot100;

import java.security.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;

public class link_list {

    /**
     * K个一组反转链表
     * hot100的hard，没做出来
     */
    public ListNode reverseKGroup(ListNode head, int k) {
        ListNode dummyHead = new ListNode(0);
        dummyHead.next = head;
        ListNode prev = dummyHead;

        while (head != null) {
            ListNode tail = prev;
            // 查看剩余部分长度是否大于等于 k
            for (int i = 0; i < k; ++i) {
                tail = tail.next;
                if (tail == null) {
                    return dummyHead.next;
                }
            }
            // 存下下一组的第一个，以变反转之后链接上去
            ListNode next = tail.next;
            ListNode[] reverse = reverse(head, tail);
            head = reverse[0];
            tail = reverse[1];
            // 把子链表重新接回原链表
            prev.next = head;
            tail.next = next;
            prev = tail;
            head = tail.next;
        }

        return dummyHead.next;
    }

    /**
     * 从head到tail反转节点
     * 要从改变指针的指向的角度去理解
     * 这里要是不懂，还是要自己画一下
     * 
     * @param head
     * @param tail
     * @return
     */
    private ListNode[] reverse(ListNode head, ListNode tail) {
        ListNode prev = tail.next;

        // 借助node从head开始反转到tail的每个节点
        ListNode node = head;
        while (prev != tail) {
            // 暂时记录node要到的下一个节点
            ListNode next = node.next;

            // 反转node的指针方向
            node.next = prev;
            prev = node;
            node = next;
        }
        return new ListNode[] { tail, head };
    }

    /**
     * LRU
     * 没做出来，连方向都错了
     * 双向链表（需要自己实现） + HashMap
     * 使用过的就放到链表头部，要淘汰直接淘汰最后的就是最久没有用过的
     * 最难想到的是虚拟头尾节点
     */
    public class LRUCache {
        public class Node {
            private Integer key;
            private Integer val;
            private Node prev;
            private Node next;

            public Node() {

            }

            public Node(Integer key, Integer val) {
                this.key = key;
                this.val = val;
            }
        }

        private Map<Integer, Node> cache = new HashMap<>();
        private int capacity;
        private int size;
        private Node head;
        private Node tail;

        public LRUCache(int capacity) {
            this.capacity = capacity;
            this.head = new Node();
            this.tail = new Node();
            head.next = tail;
            tail.prev = head;
            this.size = 0;
        }

        public int get(int key) {
            if (cache.containsKey(key)) {
                Node node = cache.get(key);
                moveToHead(node);
                return cache.get(key).val;
            }
            return -1;
        }

        public void put(int key, int value) {
            if (cache.containsKey(key)) {
                Node node = cache.get(key);
                node.val = value;
                moveToHead(node);
                return;
            }
            Node newNode = new Node(key, value);
            cache.put(key, newNode);
            addAtHead(newNode);
            ensureCapacityValid();
        }

        private void ensureCapacityValid() {
            while (size > capacity) {
                System.out.println(size + " " + capacity);
                removeFromTail();
            }
        }

        private void addAtHead(Node node) {
            Node oldHead = head.next;
            node.next = oldHead;
            node.prev = head;
            head.next = node;
            oldHead.prev = node;
            size++;
            cache.put(node.key, node);
            ensureCapacityValid();
        }

        private void removeFromTail() {
            Node oldTail = tail.prev;
            removeNode(oldTail);
        }

        private void removeNode(Node node) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
            size--;
            cache.remove(node.key);
        }

        private void moveToHead(Node node) {
            removeNode(node);
            addAtHead(node);
        }
    }
}
