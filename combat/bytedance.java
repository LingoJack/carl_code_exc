package combat;

import java.util.List;

public class bytedance {

    public class ListNode {
        private int val;
        private ListNode next;

        public ListNode(int val, ListNode next) {
            this.next = next;
            this.val = val;
        }

        public ListNode(int val) {
            this.val = val;
        }

        public ListNode() {
            this.val = 0;
        }
    }

    /**
     * 排序链表，要求时空间复杂度尽可能优
     * 2025/1/14 字节一面
     * Leetcode hot100 原题
     * 下面是自顶向下的递归解法
     */
    public ListNode sortList(ListNode head) {
        // 如果链表为空或只有一个节点，则直接返回
        if (head == null || head.next == null) {
            return head;
        }
        // 使用快慢指针找到链表的中间点
        ListNode slow = head, fast = head.next;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        // 分割链表为两部分
        ListNode rightHead = slow.next;
        slow.next = null;
        // 递归地对左右两部分进行排序
        ListNode leftSorted = sortList(head);
        ListNode rightSorted = sortList(rightHead);
        // 合并两个已排序的链表
        return merge(leftSorted, rightSorted);
    }

    private ListNode merge(ListNode l1, ListNode l2) {
        // 创建一个虚拟头节点，简化边界条件处理
        ListNode dummy = new ListNode(0);
        ListNode cur = dummy;
        // 比较两个链表的节点值，按顺序链接到新的链表上
        while (l1 != null && l2 != null) {
            if (l1.val < l2.val) {
                cur.next = l1;
                l1 = l1.next;
            } else {
                cur.next = l2;
                l2 = l2.next;
            }
            cur = cur.next;
        }
        // 将剩余的节点连接到结果链表上
        cur.next = (l1 != null) ? l1 : l2;
        // 返回合并后的新链表的头部（跳过虚拟头节点）
        return dummy.next;
    }

    /**
     * 排序链表
     * 自底向上的迭代解法
     */
    public ListNode sortListWithDown2Top(ListNode head) {
        // 如果链表为空或只有一个节点，则直接返回
        if (head == null || head.next == null) {
            return head;
        }
        // 获取链表长度
        int length = getListLength(head);
        // 创建一个哨兵节点以简化边界条件的处理
        ListNode dummyHead = new ListNode(0, head);
        // 逐步增加步长，每次都是前一次的两倍，直到覆盖整个链表
        for (int step = 1; step < length; step *= 2) {
            ListNode prev = dummyHead, curr = dummyHead.next;
            // 每次循环中，将链表按步长分割成子链表，并合并它们
            while (curr != null) {
                ListNode head1 = curr;
                ListNode head2 = split(curr, step);
                curr = split(head2, step); // 更新 curr 为下一个待处理的起始节点

                // 合并两个子链表，并更新 prev 指向合并后的最后一个节点
                ListNode[] merged = mergeTwoLists(head1, head2);
                prev.next = merged[0];
                prev = merged[1];
            }
        }
        return dummyHead.next;
    }

    private int getListLength(ListNode head) {
        int length = 0;
        while (head != null) {
            length++;
            head = head.next;
        }
        return length;
    }

    private ListNode split(ListNode head, int size) {
        // 分割出一段长度为 size 的链表，并返回剩余链表的头节点
        for (int i = 1; head != null && i < size; i++) {
            head = head.next;
        }
        if (head == null) {
            return null;
        }
        ListNode secondPart = head.next;
        head.next = null;
        return secondPart;
    }

    private ListNode[] mergeTwoLists(ListNode list1, ListNode list2) {
        ListNode dummy = new ListNode(), tail = dummy;
        // 合并两个有序链表
        while (list1 != null && list2 != null) {
            if (list1.val < list2.val) {
                tail.next = list1;
                list1 = list1.next;
            } else {
                tail.next = list2;
                list2 = list2.next;
            }
            tail = tail.next;
        }
        // 连接剩余部分
        tail.next = list1 != null ? list1 : list2;
        // 找到新链表的尾部
        while (tail.next != null) {
            tail = tail.next;
        }
        return new ListNode[]{dummy.next, tail};
    }

}
