package bst;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.w3c.dom.Node;

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
     * 中序遍历：
     * 记录一个左子节点是否有被放入过栈，
     * 然后对于栈中的每一个节点，先将其最左一系的节点中为入过栈的都入栈
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
            } else {
                cur = stack.pop();
                result.add(cur.val);
                cur = cur.right;
            }
        }
        return result;
    }

    /**
     * 统一风格的迭代三序号遍历，
     * 这里以先序遍历为例
     */
    public List<Integer> preorderTraversalStandard(TreeNode root) {
        List<Integer> res = new ArrayList<>(); // 存储遍历结果的列表
        ArrayDeque<TreeNode> stack = new ArrayDeque<>(); // 使用栈来模拟递归

        // 如果根节点不为空，初始化栈，将根节点压入栈中
        if (root != null) {
            stack.push(root);
        }

        while (!stack.isEmpty()) { // 当栈不为空时继续遍历
            TreeNode node = stack.peek(); // 获取栈顶节点

            if (node != null) {
                // 如果栈顶节点不为空，弹出栈顶节点
                stack.pop();

                // 先压入右子节点，再压入左子节点，这样左子节点会先被访问（因为栈是后进先出）
                if (node.right != null) {
                    stack.push(node.right); // 将右子节点压入栈中
                }
                if (node.left != null) {
                    stack.push(node.left); // 将左子节点压入栈中
                }

                // 将当前节点再次压入栈中（为了在访问其左右子节点后再访问该节点）
                stack.push(node);
                // 压入一个 `null` 作为标记，表示该节点的左右子节点已经访问完，可以处理该节点了
                stack.push(null);
            } else {
                // 如果栈顶是 `null`，弹出 `null` 标记
                stack.pop();

                // 获取栈顶节点
                node = stack.peek();
                stack.pop();

                // 将节点值添加到结果列表中
                res.add(node.val);
            }
        }
        return res;
    }

    /**
     * 统一风格的迭代三序号遍历，
     * 这里以中序遍历为例
     */
    public List<Integer> inorderTraversalStandard(TreeNode root) {
        List<Integer> res = new ArrayList<>(); // 存储遍历结果的列表
        ArrayDeque<TreeNode> stack = new ArrayDeque<>(); // 使用栈来模拟递归

        // 如果根节点不为空，初始化栈，将根节点压入栈中
        if (root != null) {
            stack.push(root);
        }

        while (!stack.isEmpty()) { // 当栈不为空时继续遍历
            TreeNode node = stack.peek(); // 获取栈顶节点

            if (node != null) {
                // 如果栈顶节点不为空，弹出栈顶节点
                stack.pop();

                // 先压入右子节点，再压入左子节点，这样左子节点会先被访问（因为栈是后进先出）
                if (node.right != null) {
                    stack.push(node.right); // 将右子节点压入栈中
                }

                stack.push(node);
                // 压入一个 `null` 作为标记
                stack.push(null);

                if (node.left != null) {
                    stack.push(node.left); // 将左子节点压入栈中
                }
            } else {
                // 如果栈顶是 `null`，弹出 `null` 标记
                stack.pop();

                // 获取栈顶节点
                node = stack.peek();
                stack.pop();

                // 将节点值添加到结果列表中
                res.add(node.val);
            }
        }
        return res;
    }

    /**
     * 二叉树的层序遍历
     * 妙啊，在写这题之前我虽然会写BFS
     * 但是没有思考过怎么分层
     */
    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        ArrayDeque<TreeNode> queue = new ArrayDeque<>();
        if (root != null) {
            queue.offer(root); // 将根节点加入队列
        } else {
            return res; // 如果根节点为空，返回空结果
        }

        while (!queue.isEmpty()) {
            int size = queue.size(); // 获取当前层的节点数
            List<Integer> list = new ArrayList<>(); // 当前层的节点值

            // 这里对分层的处理（即，确定每一层的元素个数）很巧妙
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll(); // 从队列中取出节点
                if (node != null) {
                    list.add(node.val); // 将节点值添加到当前层的 list 中
                    if (node.left != null)
                        queue.offer(node.left); // 将左子节点加入队列
                    if (node.right != null)
                        queue.offer(node.right); // 将右子节点加入队列
                }
            }

            // 如果当前层的 list 非空，将其添加到结果中
            if (!list.isEmpty()) {
                res.add(list);
            }
        }

        return res;
    }

    /**
     * 二叉树的层次遍历II
     * 和上一题的唯一区别是这里要自低向上的层序
     * 所以可以用栈来暂存每个list
     * 后面再从栈中单独pop出来加到res里
     */
    public List<List<Integer>> levelOrderBottom(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        ArrayDeque<TreeNode> queue = new ArrayDeque<>();
        ArrayDeque<List<Integer>> stack = new ArrayDeque<>();
        if (root != null) {
            queue.offer(root); // 将根节点加入队列
        } else {
            return res; // 如果根节点为空，返回空结果
        }

        while (!queue.isEmpty()) {
            int size = queue.size(); // 获取当前层的节点数
            List<Integer> list = new ArrayList<>(); // 当前层的节点值

            // 这里对分层的处理（即，确定每一层的元素个数）很巧妙
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll(); // 从队列中取出节点
                if (node != null) {
                    list.add(node.val); // 将节点值添加到当前层的 list 中
                    if (node.left != null)
                        queue.offer(node.left); // 将左子节点加入队列
                    if (node.right != null)
                        queue.offer(node.right); // 将右子节点加入队列
                }
            }

            // 如果当前层的 list 非空，将其添加到结果中
            if (!list.isEmpty()) {
                stack.push(list);
            }
        }

        while (!stack.isEmpty()) {
            res.add(stack.pop());
        }

        return res;
    }

    /**
     * 二叉树的右视图
     * 最高层最右边的，相当于在层序遍历的基础上挑选出每一层最后一个节点的值
     */
    public List<Integer> rightSideView(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        ArrayDeque<TreeNode> queue = new ArrayDeque<>();
        if (root != null) {
            queue.offer(root); // 将根节点加入队列
        } else {
            return res; // 如果根节点为空，返回空结果
        }

        while (!queue.isEmpty()) {
            int size = queue.size(); // 获取当前层的节点数
            List<Integer> list = new ArrayList<>(); // 当前层的节点值

            // 这里对分层的处理（即，确定每一层的元素个数）很巧妙
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll(); // 从队列中取出节点
                if (node != null) {
                    list.add(node.val); // 将节点值添加到当前层的 list 中
                    if (node.left != null)
                        queue.offer(node.left); // 将左子节点加入队列
                    if (node.right != null)
                        queue.offer(node.right); // 将右子节点加入队列
                    if (i == size - 1) {
                        res.add(node.val);
                    }
                }
            }
        }

        return res;
    }

    /**
     * 二叉树的层平均值
     * 层序遍历的基础上统计每一层的均值
     */
    public List<Double> averageOfLevels(TreeNode root) {
        List<Double> res = new ArrayList<>();
        ArrayDeque<TreeNode> queue = new ArrayDeque<>();
        if (root != null) {
            queue.offer(root); // 将根节点加入队列
        } else {
            return res; // 如果根节点为空，返回空结果
        }

        while (!queue.isEmpty()) {
            int size = queue.size(); // 获取当前层的节点数
            List<Integer> list = new ArrayList<>(); // 当前层的节点值
            double sum = 0;
            // 这里对分层的处理（即，确定每一层的元素个数）很巧妙
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll(); // 从队列中取出节点
                if (node != null) {
                    list.add(node.val); // 将节点值添加到当前层的 list 中
                    if (node.left != null)
                        queue.offer(node.left); // 将左子节点加入队列
                    if (node.right != null)
                        queue.offer(node.right); // 将右子节点加入队列
                    sum += node.val;
                }
            }
            res.add(sum / size);
        }

        return res;
    }

    class Node {
        public int val;
        public List<Node> children;
    
        public Node() {}
    
        public Node(int _val) {
            val = _val;
        }
    
        public Node(int _val, List<Node> _children) {
            val = _val;
            children = _children;
        }
    };

    /**
     * N 叉树的层序遍历
     */
    public List<List<Integer>> levelOrder(Node root) {
        List<List<Integer>> res = new ArrayList<>();
        ArrayDeque<Node> queue = new ArrayDeque<>();
        if (root != null) {
            queue.offer(root); // 将根节点加入队列
        } else {
            return res; // 如果根节点为空，返回空结果
        }

        while (!queue.isEmpty()) {
            int size = queue.size(); // 获取当前层的节点数
            List<Integer> list = new ArrayList<>(); // 当前层的节点值

            // 这里对分层的处理（即，确定每一层的元素个数）很巧妙
            for (int i = 0; i < size; i++) {
                Node node = queue.poll(); // 从队列中取出节点
                if (node != null) {
                    queue.addAll(node.children);
                    list.add(node.val);
                }
            }

            // 如果当前层的 list 非空，将其添加到结果中
            if (!list.isEmpty()) {
                res.add(list);
            }
        }

        return res;
    }

    /**
     * 在每个树行中找最大值
     */
    public List<Integer> largestValues(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        ArrayDeque<TreeNode> queue = new ArrayDeque<>();
        if (root != null) {
            queue.offer(root); // 将根节点加入队列
        } else {
            return res; // 如果根节点为空，返回空结果
        }

        while (!queue.isEmpty()) {
            int size = queue.size(); // 获取当前层的节点数
            int max = Integer.MIN_VALUE;
            // 这里对分层的处理（即，确定每一层的元素个数）很巧妙
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll(); // 从队列中取出节点
                if (node != null) {
                    if (node.left != null)
                        queue.offer(node.left); // 将左子节点加入队列
                    if (node.right != null)
                        queue.offer(node.right); // 将右子节点加入队列
                    max = Math.max(max, node.val);
                }
            }
            res.add(max);
        }

        return res;
    }
}
