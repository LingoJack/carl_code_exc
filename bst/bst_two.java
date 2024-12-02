package bst;

import java.util.ArrayDeque;

public class bst_two {
    class Node {
        public int val;
        public Node left;
        public Node right;
        public Node next;
    
        public Node() {}
        
        public Node(int _val) {
            val = _val;
        }
    
        public Node(int _val, Node _left, Node _right, Node _next) {
            val = _val;
            left = _left;
            right = _right;
            next = _next;
        }
    };

    /**
     *  填充每个节点的下一个右侧节点指针
     */
    public Node connect(Node root) {
        ArrayDeque<Node> queue = new ArrayDeque<>();
        if (root != null) {
            queue.offer(root);
        }
        else {
            return root;
        }

        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                Node node = queue.poll();
                if (node != null) {
                    if (node.left != null) {
                        queue.offer(node.left);
                    }
                    if (node.right != null) {
                        queue.add(node.right);
                    }
                    Node next = queue.peek();
                    // 这里需要判断一下，本层的最后一个节点应该指向null，而不是下一层的第一个节点
                    node.next = i == size - 1 ? null : next;
                }
            }
        }

        return root;
    }
}
