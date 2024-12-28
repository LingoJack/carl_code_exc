package leetcodeHot100;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class bst {

    /**
     * 锯齿形打印二叉树
     */
    public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null)
            return result;

        Deque<TreeNode> deque = new LinkedList<>();
        deque.offerLast(root);
        boolean leftToRight = true;

        while (!deque.isEmpty()) {
            int size = deque.size();
            List<Integer> level = new ArrayList<>(size);

            for (int i = 0; i < size; ++i) {
                if (leftToRight) {
                    TreeNode node = deque.pollFirst();
                    level.add(node.val);
                    if (node.left != null)
                        deque.offerLast(node.left);
                    if (node.right != null)
                        deque.offerLast(node.right);
                } else {
                    TreeNode node = deque.pollLast();
                    level.add(node.val);
                    if (node.right != null)
                        deque.offerFirst(node.right);
                    if (node.left != null)
                        deque.offerFirst(node.left);
                }
            }

            result.add(level);
            leftToRight = !leftToRight;
        }

        return result;
    }

    private int res = 0;
    private int count = 0;
    private boolean hasFound = false;

    /**
     * 二叉搜索树中第k小的元素
     */
    public int kthSmallest(TreeNode root, int k) {
        // 考虑是中序遍历
        inorderTraversal(root, k);
        return res;
    }

    private void inorderTraversal(TreeNode node, int k) {
        if (node == null || hasFound) {
            return;
        }
        inorderTraversal(node.left, k);
        count++;
        if (count == k) {
            hasFound = true;
            res = node.val;
        }
        inorderTraversal(node.right, k);
    }

    /**
     * 最大路径和
     */
    public int maxPathSum(TreeNode root) {
        if (root == null) {
            return 0;
        }
        if (root.left == null && root.right == null) {
            return root.val;
        }
        calcPathSumGain(root);
        return maxSum;
    }

    private int maxSum = Integer.MIN_VALUE;

    private int calcPathSumGain(TreeNode node) {
        if (node == null) {
            return 0;
        }
        int leftGain = Math.max(0, calcPathSumGain(node.left));
        int rightGain = Math.max(0, calcPathSumGain(node.right));
        maxSum = Math.max(maxSum, node.val + leftGain + rightGain);
        return node.val + Math.max(leftGain, rightGain);
    }

    /**
     * 二叉树的直径
     * 14min ac
     */
    public int diameterOfBinaryTree(TreeNode root) {
        calcDiameter(root);
        return max_diameter;
    }

    private int max_diameter = Integer.MIN_VALUE;

    private int calcDiameter(TreeNode node) {
        if (node == null) {
            return 0;
        }
        int lt = calcDiameter(node.left);
        int rt = calcDiameter(node.right);
        max_diameter = Math.max(max_diameter, lt + rt + 1);
        return Math.max(lt, rt) + 1;
    }
}
