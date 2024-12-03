package bst;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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
}
