class MyLinkedList {

    ListNode head;

    public MyLinkedList() {

    }

    public int get(int index) {
        int count = 0;
        // 找到index节点
        ListNode cur = head;
        while (cur != null) {
            if (count == index) {
                break;
            }
            count++;
            cur = cur.next;
        }
        // cur == null 或者 找到了需要的节点
        return cur == null ? -1 : cur.val;
    }

    public void addAtHead(int val) {
        ListNode newNode = new ListNode(val);
        newNode.next = head;
        head = newNode;
    }

    public void addAtTail(int val) {

        if (head == null) {
            head = new ListNode(val);
            return;
        }

        ListNode cur = head;
        while (cur.next != null) {
            cur = cur.next;
        }
        cur.next = new ListNode(val);
    }

    public void addAtIndex(int index, int val) {

        if (index < 0) {
            addAtHead(val);
            return;
        }

        int count = 0;

        // 找到index的前一个节点
        ListNode dummyHead = new ListNode();
        dummyHead.next = head;

        ListNode cur = dummyHead;
        while (cur != null) {
            if (index == count) {
                break;
            }
            count++;
            cur = cur.next;
        }

        // cur == null 或者 找到了index的前一个节点
        if (cur == null) {
            return;
        }

        // 将新的节点的next指向该节点的next的节点
        ListNode node = new ListNode(val);
        node.next = cur.next == null ? null : cur.next;

        if (index == 0){
            head = node;
        }

        // 将该节点的next指向新的节点
        cur.next = node;
    }

    public void deleteAtIndex(int index) {

        ListNode dummyHead = new ListNode();
        dummyHead.next = head;

        int count = 0;
        ListNode cur = dummyHead;

        // 找到需要删除的节点的前一个节点
        while (cur != null) {
            if (count == index) {
                break;
            }
            count++;
            cur = cur.next;
        }

        // 将该节点的next指向其next的next，如果不为null
        // cur == null 或者 找到了要删除节点的前一个节点
        if (cur == null) {
            return;
        }

        cur.next = cur.next == null ? null : cur.next.next;
        if (index == 0) {
            head = dummyHead.next;
        }

    }

    public static void main(String[] args) {
        MyLinkedList linkedList = new MyLinkedList();
        linkedList.addAtIndex(0, 10);
        linkedList.addAtIndex(0, 20);
        linkedList.addAtIndex(1, 30);

        ListNode cur = linkedList.head;
        while (cur != null) {
            System.out.print(cur.val + " ");
            cur = cur.next;
        }
    }
}

/**
 * Your MyLinkedList object will be instantiated and called as such:
 * MyLinkedList obj = new MyLinkedList();
 * int param_1 = obj.get(index);
 * obj.addAtHead(val);
 * obj.addAtTail(val);
 * obj.addAtIndex(index,val);
 * obj.deleteAtIndex(index);
 */
