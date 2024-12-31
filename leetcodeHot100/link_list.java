package leetcodeHot100;

import java.security.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;

import monotonic_stack.monotonic_stack;

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

    /**
     * 合并 K 个升序链表
     * 15min 初始化的时候有问题，差一点ac
     * 但是效率低
     * 思路就是归并排序
     */
    public ListNode mergeKLists(ListNode[] lists) {
        if (lists == null || lists.length == 0) {
            return null;
        }
        // 直接使用输入的lists数组，无需额外创建nodeList数组
        ListNode dummyHead = new ListNode(0);
        ListNode node = dummyHead;
        while (true) {
            ListNode minNode = null;
            int idx = -1;
            boolean updated = false;
            for(int i = 0; i < lists.length; i++) {
                if (lists[i] != null && (minNode == null || lists[i].val < minNode.val)) {
                    minNode = lists[i];
                    idx = i;
                    updated = true;
                }
            }
            if (!updated) {
                break;
            }
            // 更新当前链表头到下一个节点
            lists[idx] = lists[idx].next;
            node.next = minNode;
            node = node.next;
        }
        return dummyHead.next;
    }

    /**
     * 合并 K 个升序链表
     * 思路和上面是一样的，区别就是这里使用优先队列来获取min
     * 而之前的解法是遍历一次来获取min
     */
    public ListNode mergeKListsWithPriorityQueue(ListNode[] lists) {
        if (lists == null || lists.length == 0) {
            return null;
        }
        // 直接使用输入的lists数组，无需额外创建nodeList数组
        ListNode dummyHead = new ListNode(0);
        ListNode node = dummyHead;
        
        // 优先队列，这里可以简单记忆比较器：(a, b) -> a - b 这边a，b前后出现的顺序都是一样的，所以是升序，反之就是降序了 
        PriorityQueue<ListNode> priorityQueue = new PriorityQueue<>((a, b) -> Integer.compare(a.val, b.val));
        for(int i = 0; i < lists.length; i++) {
            if(lists[i] != null) priorityQueue.offer(lists[i]);
        }

        while (true) {
            ListNode minNode = priorityQueue.poll();
            if (minNode == null) {
                break;
            }
            node.next = minNode;
            node = node.next;
            if(minNode.next != null) priorityQueue.offer(minNode.next);
        }
        return dummyHead.next;
    }

    /**
     * 环形链表
     * 思路是快慢指针
     */
    public boolean hasCycle(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;
        while (fast != null && slow != null) {
            if (fast.next != null) {
                fast = fast.next.next;
            }
            else {
                return false;
            }
           
            slow = slow.next;
            if (fast == slow) {
                return true;
            }
        }
        return false;
    }

    /**
     * 环形链表II
     * 4min ac，但是时间复杂度非常糟糕
     */
    public ListNode detectCycle(ListNode head) {
        Map<ListNode, Boolean> visited = new HashMap<>();
        ListNode node = head;
        while (node != null) {
            Boolean hasVisited = visited.getOrDefault(node, false);
            if (hasVisited) {
                return node;
            }
            visited.put(node, true);
            node = node.next;
        }
        return null;
    }

    /**
     * 环形链表II
     * 这个快慢指针的解法很巧妙，
     * 要检测环，就是快慢指针相遇之后，说明快指针比慢指针多走了一个环的距离
     * 那么此时让头节点和快指针一步步前进，它们遇到的地方就是环的入口
     * 要证明这一点，可以画图理解一下
     */
    public ListNode detectCycleWithSlowFastPointer(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;
        while (slow != null && fast != null) {
            if (fast.next != null) {
                fast = fast.next.next;
                if (fast == slow) {
                    return slow;
                }
            }
            else {
                return null;
            }
            slow = slow.next;
        }
        return null;
    }

    /**
     * 两数相加
     * 一次过，击败100%
     */
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        int carryout = 0;
        ListNode n1 = l1;
        ListNode n2 = l2;
        ListNode node = null;
        ListNode last = null;
        ListNode head = null;
        while (n1 != null || n2 != null || carryout == 1) {
            int partSum = (n1 == null ? 0 : n1.val) + (n2 == null ? 0 : n2.val) + carryout;
            carryout = 0;
            if (partSum > 9) {
                partSum %= 10;
                carryout = 1;
            }
            node = new ListNode(partSum);
            if (last == null) {
                head = node;
            }
            else {
                last.next = node;
            }
            last = node;
            node = node.next;
            if (n1 != null) n1 = n1.next;
            if (n2 != null) n2 = n2.next;
        }
        return head;
    }
}
