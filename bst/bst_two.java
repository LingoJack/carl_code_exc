package bst;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import apple.laf.JRSUIUtils.Tree;

public class bst_two {
    class Node {
        public int val;
        public Node left;
        public Node right;
        public Node next;

        public Node() {
        }

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
     * 填充每个节点的下一个右侧节点指针
     */
    public Node connect(Node root) {
        ArrayDeque<Node> queue = new ArrayDeque<>();
        if (root != null) {
            queue.offer(root);
        } else {
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

    /**
     * 填充每个节点的下一个右侧节点指针II
     * 呃，和上题的代码一模一样
     */
    public Node connect2(Node root) {
        ArrayDeque<Node> queue = new ArrayDeque<>();
        if (root != null) {
            queue.offer(root);
        } else {
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

    public class TreeNode {
        int val; // 节点的值
        TreeNode left; // 左子节点
        TreeNode right; // 右子节点

        // 默认构造函数
        TreeNode() {
        }

        // 带值的构造函数
        TreeNode(int val) {
            this.val = val;
        }

        // 带值、左子节点和右子节点的构造函数
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    /**
     * 二叉树的最小深度
     * 感觉可以使用递归，当前实现的时间复杂度很高，因为存在很多重复计算
     * 等后面讲递归会说优化方法：记忆话递归
     * 当然也还有层序遍历的解法
     */
    public int minDepth(TreeNode root) {
        if (root == null) {
            return 0;
        } else if (root.left == null || root.right == null) {
            return 1 + Math.max(calcMin(root.left), calcMin(root.right));
        }
        return calcMin(root);
    }

    private int calcMin(TreeNode node) {
        if (node == null) {
            return 0;
        } else if (node.left == null || node.right == null) {
            return 1 + Math.max(calcMin(node.left), calcMin(node.right));
        }
        return 1 + Math.min(calcMin(node.left), calcMin(node.right));
    }

    /**
     * 最小深度的层序解法
     */
    public int minDepthWithLevelOrder(TreeNode root) {
        int res = 0;
        ArrayDeque<TreeNode> queue = new ArrayDeque<>();
        if (root != null) {
            queue.offer(root); // 将根节点加入队列
        } else {
            return 0; // 如果根节点为空，返回空结果
        }

        while (!queue.isEmpty()) {
            int size = queue.size(); // 获取当前层的节点数
            // 这里对分层的处理（即，确定每一层的元素个数）很巧妙
            res++;
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll(); // 从队列中取出节点
                if (node != null) {
                    if (node.right == null && node.left == null) {
                        return res;
                    }
                    if (node.left != null)
                        queue.offer(node.left); // 将左子节点加入队列
                    if (node.right != null)
                        queue.offer(node.right); // 将右子节点加入队列Ò
                }
            }
        }

        return res;
    }

    /**
     * 二叉树的最大深度
     * 递归解法，另外还有层序解法
     */
    public int maxDepth(TreeNode root) {
        return calcMax(root);
    }

    private int calcMax(TreeNode node) {
        if (node == null) {
            return 0;
        }
        return 1 + Math.max(calcMax(node.left), calcMax(node.right));
    }

    /**
     * 最大深度的层序解法
     */
    public int maxDepthWithLevelOrder(TreeNode root) {
        int res = 0;
        ArrayDeque<TreeNode> queue = new ArrayDeque<>();
        if (root != null) {
            queue.offer(root); // 将根节点加入队列
        } else {
            return 0; // 如果根节点为空，返回空结果
        }

        while (!queue.isEmpty()) {
            int size = queue.size(); // 获取当前层的节点数
            // 这里对分层的处理（即，确定每一层的元素个数）很巧妙
            res++;
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll(); // 从队列中取出节点
                if (node != null) {
                    if (node.left != null)
                        queue.offer(node.left); // 将左子节点加入队列
                    if (node.right != null)
                        queue.offer(node.right); // 将右子节点加入队列Ò
                }
            }
        }

        return res;
    }

    /**
     * 翻转二叉树
     * 一眼递归解
     */
    public TreeNode invertTree(TreeNode root) {
        invert(root);
        return root;
    }

    private void invert(TreeNode node) {
        if (node == null) {
            return;
        }
        TreeNode t = node.left;
        node.left = node.right;
        node.right = t;
        invert(node.left);
        invert(node.right);
    }

    /**
     * 对称二叉树
     * 使用队列的实现
     */
    public boolean isSymmetric(TreeNode root) {
        Queue<TreeNode> deque = new LinkedList<>();
        deque.offer(root.left);
        deque.offer(root.right);
        while (!deque.isEmpty()) {
            TreeNode leftNode = deque.poll();
            TreeNode rightNode = deque.poll();
            if (leftNode == null && rightNode == null) {
                // 此处相当于true
                continue;
            }
            // 以上三个判断条件合并
            if (leftNode == null || rightNode == null || leftNode.val != rightNode.val) {
                return false;
            }
            // 这里顺序与使用Deque不同
            deque.offer(leftNode.left);
            deque.offer(rightNode.right);
            deque.offer(leftNode.right);
            deque.offer(rightNode.left);
        }
        return true;
    }

    /**
     * 对称二叉树
     * 无论是递归还是非递归解法我都没有想出来...
     * 我的递归还是太不熟练了
     */
    public boolean isSymmetricWithRecursion(TreeNode root) {
        if (root == null) {
            return true;
        }
        return compare(root.left, root.right);
    }

    private boolean compare(TreeNode lt, TreeNode rt) {
        if (lt == null && rt != null) {
            return false;
        } else if (lt != null & rt == null) {
            return false;
        } else if (lt == null & rt == null) {
            return true;
        } else if (lt.val != rt.val) {
            return false;
        }
        return compare(lt.right, rt.left) && compare(lt.left, rt.right);
    }

    private int count = 0;

    /**
     * 完全二叉树的节点个数
     */
    public int countNodes(TreeNode root) {
        clacNodeNum(root);
        return count;
    }

    private void clacNodeNum(TreeNode node) {
        if (node != null) {
            count++;
        } else {
            return;
        }
        clacNodeNum(node.left);
        clacNodeNum(node.right);
    }

    /**
     * 平衡二叉树
     * 也是复用之前的代码和递归丑陋地通过了...
     * 如何高效判断一个二叉树是不是平衡树，可以参考AVL的做法：
     * 即，增加一个节点的属性字段用以存储该节点的高度
     * height = Math.max(left.height, right.height) + 1
     */
    public boolean isBalanced(TreeNode root) {
        return balanced(root);
    }

    private boolean balanced(TreeNode node) {
        if (node == null) {
            return true;
        }

        int diff = maxDepth(node.left) - maxDepth(node.right);
        if (diff > 1 || diff < -1) {
            return false;
        }
        return balanced(node.left) && balanced(node.right);
    }

    /**
     * 平衡二叉树，借助AVL的思想
     * 这里用val字段存储节点的高度
     */
    public boolean isBalancedWithBottomUpRecursion(TreeNode root) {
        return height(root) >= 0;
    }

    public int height(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int leftHeight = height(root.left);
        int rightHeight = height(root.right);
        if (leftHeight == -1 || rightHeight == -1 || Math.abs(leftHeight - rightHeight) > 1) {
            return -1;
        } else {
            return Math.max(leftHeight, rightHeight) + 1;
        }
    }



    /**
     * 二叉树的所有路径
     * 是不是可以直接DFS
     * 似乎不行，我实在是没有太明白递归，还有树的遍历顺序到底如何选择
     * 另外这题也需要回溯
     * 最近的题刷得云里雾里，对了迷迷糊糊对了，错的也是一点思路没
     */
    public List<String> binaryTreePaths(TreeNode root) {
        List<String> res = new ArrayList<>();// 存最终的结果
        if (root == null) {
            return res;
        }
        List<Integer> paths = new ArrayList<>();// 作为结果中的路径
        traversal(root, paths, res);
        return res;
    }

    private void traversal(TreeNode root, List<Integer> paths, List<String> res) {
        paths.add(root.val);// 前序遍历，中
        // 遇到叶子结点
        if (root.left == null && root.right == null) {
            // 输出
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < paths.size() - 1; i++) {
                sb.append(paths.get(i)).append("->");
            }
            sb.append(paths.get(paths.size() - 1));// 记录最后一个节点
            res.add(sb.toString());// 收集一个路径
            return;
        }
        // 递归和回溯是同时进行，所以要放在同一个花括号里
        if (root.left != null) { // 左
            traversal(root.left, paths, res);
            paths.remove(paths.size() - 1);// 回溯
        }
        if (root.right != null) { // 右
            traversal(root.right, paths, res);
            paths.remove(paths.size() - 1);// 回溯
        }
    }

    public List<String> binaryTreePathsWithAwesomeStack(TreeNode root) {
        List<String> result = new ArrayList<>();
        if (root == null)
            return result;
        Stack<Object> stack = new Stack<>();
        // 节点和路径同时入栈
        stack.push(root);
        stack.push(root.val + "");
        while (!stack.isEmpty()) {
            // 节点和路径同时出栈
            String path = (String) stack.pop();
            TreeNode node = (TreeNode) stack.pop();
            // 若找到叶子节点
            if (node.left == null && node.right == null) {
                result.add(path);
            }
            //右子节点不为空
            if (node.right != null) {
                stack.push(node.right);
                stack.push(path + "->" + node.right.val);
            }
            //左子节点不为空
            if (node.left != null) {
                stack.push(node.left);
                stack.push(path + "->" + node.left.val);
            }
        }
        return result;
    }

    /**
     * 左叶子之和
     */
    public int sumOfLeftLeaves(TreeNode root) {
        if (root == null) {
            return 0;
        }
        return sumLeftChild(root);
    }

    private int sumLeftChild(TreeNode node) {
        if (node == null) {
            return 0;
        }

        int sum = 0;

        // 检查是否是左叶子节点
        if (node.left != null && node.left.left == null && node.left.right == null) {
            sum += node.left.val; // 左叶子值
        }

        // 递归处理左右子树
        sum += sumLeftChild(node.left);
        sum += sumLeftChild(node.right);

        return sum;
    }

    /**
     * 找树左下角的值
     * 呃，为数不多的一眼看有思路的题目
     * 层序遍历直到最后一层，拿到的第一个就是要求的结果
     * 一次过了！！！
     */
    public int findBottomLeftValue(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int res = 0;
        ArrayDeque<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
                if (i == 0) {
                    res = node.val;
                }
            }
        }
        return res;
    }

    private int known_max_dept = -1;
    private int value = 0;

    /**
     * 找树左下角的值，递归解法
     */
    public int findBottomLeftValueWithRecursion(TreeNode root) {
        value = root.val;
        findLeftValue(root, 0);
        return value;
    }

    private void findLeftValue(TreeNode root, int deep) {
        if (root == null)
            return;
        if (root.left == null && root.right == null) {
            if (deep > known_max_dept) {
                value = root.val;
                known_max_dept = deep;
            }
        }
        if (root.left != null)
            findLeftValue(root.left, deep + 1);
        if (root.right != null)
            findLeftValue(root.right, deep + 1);
    }
}
