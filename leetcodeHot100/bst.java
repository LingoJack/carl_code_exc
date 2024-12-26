package leetcodeHot100;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class bst {

    /**
     * 锯齿形打印二叉树
     */
    public List<List<Integer>> zigzagLevelOrder(Node root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null)
            return result;

        Deque<Node> deque = new LinkedList<>();
        deque.offerLast(root);
        boolean leftToRight = true;

        while (!deque.isEmpty()) {
            int size = deque.size();
            List<Integer> level = new ArrayList<>(size);

            for (int i = 0; i < size; ++i) {
                if (leftToRight) {
                    Node node = deque.pollFirst();
                    level.add(node.val);
                    if (node.left != null)
                        deque.offerLast(node.left);
                    if (node.right != null)
                        deque.offerLast(node.right);
                } else {
                    Node node = deque.pollLast();
                    level.add(node.val);
                    if (node.right != null)
                        deque.offerFirst(node.right);
                    if (node.left != null)
                        deque.offerFirst(node.left);
                }
            }

            result.add(level);
            leftToRight = !leftToRight; // 切换方向
        }

        return result;
    }
}
