import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;

public class linklist {
    public static void main(String[] args) {

    }

    /**
     * 移除链表元素
     * 涉及 链表 操作中一个非常重要的技巧：虚拟头节点
     */
    public ListNode removeElements(ListNode head, int val) {
        ListNode dummyHead = new ListNode();
        dummyHead.next = head;
        ListNode cur = dummyHead;
        while (cur != null) {
            if (cur.next != null && cur.next.val == val) {
                cur.next = cur.next.next;
            }
            else {
                cur = cur.next;
            }
        }
        return dummyHead.next;
    }

    /**
     * 反转链表
     * 这是目前做得最顺利的一题
     * 不过采用的是新建链表的方式，空间复杂度方面是不如双指针法优秀的
     */
    public ListNode reverseList(ListNode head) {
        // 创建一个新的链表
        ListNode cur = head;
        ListNode reverseHead = null;

        // 遍历当前链表，把每个节点都插入到新链表的头部
        while (cur != null) {
            ListNode node = new ListNode(cur.val);
            // 往头部插入
            node.next = reverseHead;
            reverseHead = node;
            cur = cur.next;
        }

        return reverseHead;
    }

    /**
     * 反转链表
     * 双指针实现
     * 还有就是清楚java的拷贝默认是值拷贝这一点
     * 将一个对象引用赋值给另一个变量时，实际上是在进行地址的值拷贝，也就是浅拷贝
     */
    public ListNode reverseListWithDoublePointer(ListNode head) {
        ListNode prev = null;
        ListNode cur = head;
        ListNode temp = null;
        while (cur != null) {
            
            // 因为后面要改变cur的next指针的指向，所以先要用temp保存一下
            temp = cur.next;

            // 反转指向
            cur.next = prev;

            // 更新左指针
            prev = cur;

            // 更新右指针
            cur = temp;
        }
        return prev;
    }

    /**
     * 反转链表
     * 递归实现
     */
    public ListNode reverseListWithRecursion(ListNode head) {
        return reverse(null, head);
    }

    public ListNode reverse(ListNode prev, ListNode cur) {

        if (cur == null) {
            return prev;
        }

        // 暂存
        ListNode tmp = null;
        
        tmp = cur.next;

        // 反转
        cur.next = prev;

        return reverse(cur, tmp);
    }

    /**
     * 两两交换链表中的节点
     * 解决的技巧依旧是虚拟头节点
     * 但是重点是要画图而且理清操作的先后顺序
     * 还有就是清楚java的拷贝默认是值拷贝这一点
     * 将一个对象引用赋值给另一个变量时，实际上是在进行地址的值拷贝，也就是浅拷贝
     */
    public ListNode swapPairs(ListNode head) {
        ListNode dummyHead = new ListNode();
        dummyHead.next = head;

        ListNode firstNode = null;
        ListNode secondNode = null;
        
        ListNode cur = dummyHead;
        while (cur.next != null && cur.next.next != null) {
            
            // 暂存两个节点
            firstNode = cur.next;

            // 这里的secondNode是 1 -> 2(first) -> 3 -> 4(second)
            secondNode = cur.next.next.next;

            // 步骤一
            cur.next = firstNode.next;

            // 步骤二
            firstNode.next.next = firstNode;

            // 步骤三
            firstNode.next = secondNode;
            cur = cur.next.next;
        }

        // 不满足两两配对的情况，不交换
        return dummyHead.next;
    }

    /**
     * 删除链表的倒数第N个节点
     * 这里我是用双指针来标记需要标记删除的倒数第n个节点
     * 链表的一大问题就是操作当前节点必须要找前一个节点才能操作。这就造成了，头结点的尴尬，因为头结点没有前一个节点了。
     * 每次对应头结点的情况都要单独处理，所以使用虚拟头结点的技巧，就可以解决这个问题。
     * 用到的技巧就是虚拟头节点，这样方便处理删除实际头结点的逻辑
     * 需要特殊考虑的就是，当被删除的节点是头节点的时候，需要设置新的头节点
     */
    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode dummyHead = new ListNode();
        dummyHead.next = head;

        // 目前看可以考虑使用双指针
        ListNode show = dummyHead;
        ListNode fast = dummyHead;

        int countFast = 0;
        int countShow = 0;

        // n = 1
        // d 1 
        // s
        //   f
        //   h
        
        // show应该比fast滞后n个位置
        while (fast.next != null) {
            // fast遍历链表，保持show在fast后n个位置
            if (countFast - countShow >= n) {
                show = show.next;
                countShow++;
            }
            countFast++;
            fast = fast.next;
        }

        // fast遍历到尾部，移除show即可
        show.next = show.next.next;

        // 处理被移除的是头节点情况
        if (show == dummyHead) {
            head = head.next;
        }
    
        return head;
    }

    /**
     * 链表相交
     * 本题的难度主要在题目描述不说人话
     * 需要注意的点是相等不止是值相等，next指针也得相等
     * 求出两个链表长度的差值，然后尾部对齐，比较cur，不等于则同时往后移动cur
     */
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        int lenA = 0;
        int lenB = 0;

        ListNode curA = headA;
        ListNode curB = headB;

        // 确定A表长度
        while (curA != null) {
            lenA++;
            curA = curA.next;
        }

        // 确定B表长度
        while (curB != null) {
            lenB++;
            curB = curB.next;
        }

        ListNode shortOne = null;
        ListNode longOne = null;

        // 比较长度得到较短的表
        if (lenA > lenB) {
            shortOne = headB;
            longOne = headA;
        }
        else {
            shortOne = headA;
            longOne = headB;
        }
        
        // 4 1 8 4 5 len=5
        // 5 0 1 8 4 5 len=6
        // 移动使两链表尾部对齐
        int diff = (lenA - lenB) < 0 ? (lenB - lenA) : (lenA - lenB);
        for(int i=0;i<diff;i++){
            longOne = longOne.next;
        }

        // 比较，若相等则直接返回，否则两链表的cur指针同时右移，直到为null
        while (shortOne != null && longOne != null) {
            if (shortOne == longOne) {
                return shortOne;
            }
            shortOne = shortOne.next;
            longOne = longOne.next;
        }

        return null;
    }

    /**
     * 环形链表 II
     * 感觉又是题目描述的问题
     * 首先想到的是 HashMap 暴力解
     * 虽然是一下就通过了，但是执行用时并不优秀
     */
    public ListNode detectCycle(ListNode head) {
        
        ListNode cur = head;

        Map<ListNode, Integer> map = new HashMap<>();
        
        // 如果是环形链表，这里就会一直循环
        while (cur != null) {
            Integer count = map.get(cur);
            count = count == null ? 0 : count;
            if (count == 1) {
                return cur;
            }
            count++;
            map.put(cur, count);
            cur = cur.next;
        }

        return null;
    }

    /**
     * 环形链表 II
     * 快慢指针解法
     * 这个思路不容易想到，而且涉及一些数学知识和相对运动观点
     */
    public ListNode detectCycleWithShowFastPointer(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) {// 有环
                ListNode index1 = fast;
                ListNode index2 = head;
                // 两个指针，从头结点和相遇结点，各走一步，直到相遇，相遇点即为环入口
                while (index1 != index2) {
                    index1 = index1.next;
                    index2 = index2.next;
                }
                return index1;
            }
        }
        return null;
    }

    /**
     * K个一组反转链表
     */
    public ListNode reverseKGroup(ListNode head, int k) {
        // 创建一个新的链表来存储反转后的节点
        ListNode newHead = null;
        ListNode countNode = head;
        ArrayDeque<ListNode> stack = new ArrayDeque<>();
        int count = 0;
        ListNode prev = null;
        ListNode cur = null;
        while (countNode != null) {
            if (count < k) {
                count++;
                stack.push(countNode);
                countNode = countNode.next;
            }
            else if (count == k) {
                cur = stack.pop();
                // 说明到达了该交换到地方
                while (!stack.isEmpty()) {
                    if (count == 8) {
                        if (prev == null) {
                            newHead = cur;
                        }
                        prev = cur;
                        count--;
                        continue;
                    }
                    if (prev == null) {
                        break;
                    }
                    prev.next = cur;
                    prev = cur;
                }
            }
        }
        return newHead == null ? head : newHead;
    }
}

