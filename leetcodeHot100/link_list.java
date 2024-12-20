package leetcodeHot100;

public class link_list {

    /**
     * K个一组反转链表
     * hot100的hard，没做出来
     */
    public ListNode reverseKGroup(ListNode head, int k) {
        // 创建一个虚拟头节点，以便处理边界情况
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode prevGroupEnd = dummy; // 上一个反转组的尾部
        ListNode current = head;
        while (current != null) {
            ListNode groupStart = current;
            // 检查是否有足够的节点进行反转
            int count = 0;
            // 计算是否有足够的k个节点
            while (current != null && count < k) {
                current = current.next;
                count++;
            }
            // 如果剩余的节点少于k，则不进行反转
            if (count == k) {
                // 反转当前k个节点
                ListNode prev = null;
                ListNode next = null;
                ListNode groupEnd = groupStart;
                for (int i = 0; i < k; i++) {
                    // TODO 这个过程必须想清楚
                    next = groupStart.next;
                    groupStart.next = prev;
                    prev = groupStart;
                    groupStart = next;
                }
                // 连接反转后的节点
                prevGroupEnd.next = prev;
                // 连接反转组的尾部到下一个节点
                groupEnd.next = current;
                // 更新prevGroupEnd到当前反转组的尾部
                prevGroupEnd = groupEnd;
            }
        }

        return dummy.next;
    }
}
