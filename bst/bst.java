package bst;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class bst {
    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode() {
        }

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    private List<Integer> list = new ArrayList<>();

    /**
     * 前序遍历：递归
     */
    public List<Integer> preorderTraversal(TreeNode root) {
        traversalPre(root);
        return list;
    }

    private void traversalPre(TreeNode node) {
        // 确定基准条件
        if (node == null) {
            return;
        }
        list.add(node.val);
        traversalPre(node.left);
        traversalPre(node.right);
    }

    public List<Integer> postorderTraversal(TreeNode root) {
        traversalPost(root);
        return list;
    }

    /**
     * 后序遍历：递归
     */
    private void traversalPost(TreeNode node) {
        // 确定基准条件
        if (node == null) {
            return;
        }
        traversalPost(node.left);
        traversalPost(node.right);
        list.add(node.val);
    }

    /**
     * 中序遍历：递归
     */
    public List<Integer> inorderTraversal(TreeNode root) {
        traversalIn(root);
        return list;
    }

    private void traversalIn(TreeNode node) {
        // 确定基准条件
        if (node == null) {
            return;
        }
        traversalIn(node.left);
        list.add(node.val);
        traversalIn(node.right);
    }

    /**
     * 前序遍历：栈
     */
    public List<Integer> preorderTraversal1(TreeNode root) {
        List<Integer> res = new ArrayList<>();

        if (root == null) {
            return res;
        }

        ArrayDeque<TreeNode> stack = new ArrayDeque<>();
        stack.push(root);
        TreeNode t = null;
        while (!stack.isEmpty()) {
            t = stack.pop();
            res.add(t.val);
            if (t.right != null) {
                stack.push(t.right);
            }
            if (t.left != null) {
                stack.push(t.left);
            }
        }

        return res;
    }

    /**
     * 后序遍历：栈 + 反转
     */
    public List<Integer> postorderTraversal1(TreeNode root) {
        List<Integer> res = new ArrayList<>();

        if (root == null) {
            return res;
        }

        ArrayDeque<TreeNode> stack = new ArrayDeque<>();
        stack.push(root);
        TreeNode t = null;
        while (!stack.isEmpty()) {
            t = stack.pop();
            res.add(t.val);
            if (t.left != null) {
                stack.push(t.left);
            }
            if (t.right != null) {
                stack.push(t.right);
            }
        }
        Collections.reverse(res);
        return res;
    }

    /**
     * 中序遍历：记录一个左子节点是否有被放入过栈，然后对于栈中的每一个节点，先将其最左一系的节点中为入过栈的都入栈
     * 然后记录值，然后弹出节点，若弹出的节点有右子节点，则右子节点入栈
     */
    public List<Integer> inorderTraversal1(TreeNode root) {
        List<Integer> res = new ArrayList<>();

        if (root == null) {
            return res;
        }

        ArrayDeque<TreeNode> stack = new ArrayDeque<>();
        Map<TreeNode, Boolean> map = new HashMap<>();
        TreeNode t = null;
        stack.push(root);
        map.put(t, true);

        while (!stack.isEmpty()) {
            t = stack.peek();
            // 把当前节点的左子节点全部入栈
            while (t.left != null && !map.getOrDefault(t.left, false)) {
                t = t.left;
                stack.push(t);
                map.put(t, true);
            }
            t = stack.pop();
            res.add(t.val);
            if (t.right != null) {
                stack.push(t.right);
            }
        }

        return res;
    }

    /**
     * 中序遍历：迭代
     * 相比上面我自己的实现，这是一个更高效的方法
     * 但是我想不到，感觉用迭代实现中序遍历蛮麻烦的
     */
    public List<Integer> inorderTraversal2(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        Stack<TreeNode> stack = new Stack<>();
        TreeNode cur = root;
        while (cur != null || !stack.isEmpty()) {
            if (cur != null) {
                stack.push(cur);
                cur = cur.left;
            } 
            else {
                cur = stack.pop();
                result.add(cur.val);
                cur = cur.right;
            }
        }
        return result;
    }

}
