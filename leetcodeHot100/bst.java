package leetcodeHot100;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

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
        return maxDiameter;
    }

    private int maxDiameter = Integer.MIN_VALUE;

    private int calcDiameter(TreeNode node) {
        if (node == null) {
            return 0;
        }
        int lt = calcDiameter(node.left);
        int rt = calcDiameter(node.right);
        maxDiameter = Math.max(maxDiameter, lt + rt + 1);
        return Math.max(lt, rt) + 1;
    }

    /**
     * 从前序与中序遍历序列构造二叉树
     * 没做出来，和题目“从后序和中序遍历序列构造二叉树”一样
     */
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        findInorderIndexMap = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) {
            findInorderIndexMap.put(inorder[i], i);
        }
        this.preorder = preorder;
        return buildTreeHelper(0, preorder.length - 1);
    }

    private int preorderIndex = 0;
    private int[] preorder;
    private Map<Integer, Integer> findInorderIndexMap;

    private TreeNode buildTreeHelper(int start, int end) {
        if (end < start) {
            return null;
        }
        TreeNode root = new TreeNode(preorder[preorderIndex++]);
        int rootInorderIndex = findInorderIndexMap.get(root.val);
        root.left = buildTreeHelper(start, rootInorderIndex - 1);
        root.right = buildTreeHelper(rootInorderIndex + 1, end);
        return root;
    }

    /**
     * B+树的叶子节点和非叶子节点
     */
    public class BPlusTreeNode {
        boolean isLeaf;
        int[] vals;
        BPlusTreeNode[] ptrs;
        int count;
    }

    public class BPlusTreeIndexNode extends BPlusTreeNode {

    }

    public class BPlusTreeLeafNode extends BPlusTreeNode {
        BPlusTreeLeafNode prev;
        BPlusTreeLeafNode next;
    }

    /**
     * 路径总和III
     * 这题和题目“和为K的子数组”类似，都需要类似前缀和的概念
     */
    private int pathCount = 0;
    private Map<Integer, Integer> prefixSumCount;

    public int pathSum(TreeNode root, int targetSum) {
        prefixSumCount = new HashMap<>();
        prefixSumCount.put(0, 1); // base case: one way to reach sum 0 (no elements)
        calcPathSum(root, 0, targetSum);
        return pathCount;
    }

    private void calcPathSum(TreeNode node, int currentSum, int targetSum) {
        if (node == null) {
            return;
        }
        currentSum += node.val;
        pathCount += prefixSumCount.getOrDefault(currentSum - targetSum, 0);
        prefixSumCount.put(currentSum, prefixSumCount.getOrDefault(currentSum, 0) + 1);
        calcPathSum(node.left, currentSum, targetSum);
        calcPathSum(node.right, currentSum, targetSum);
        // 回溯，如果没有选这个
        prefixSumCount.put(currentSum, prefixSumCount.get(currentSum) - 1);
    }

    /**
     * 二叉树展开为链表
     * 5min ac
     * 但是时间复杂度太高
     */
    public void flatten(TreeNode root) {
        List<TreeNode> list = new ArrayList<>();
        preorder(root, list);
        TreeNode last = new TreeNode(0);
        for (TreeNode node : list) {
            last.right = node;
            last.left = null;
            last = node;
        }
    }

    private void preorder(TreeNode node, List<TreeNode> list) {
        if (node == null) {
            return;
        }
        list.add(node);
        preorder(node.left, list);
        preorder(node.right, list);
    }

    /**
     * 二叉树展开为链表
     * 思路我想到了，但是当时想着递归实现，没有实现出来，这个很巧
     * 思路：根节点的右子树连接到根的左子树的最右节点成为其右子树，根的左子树成为根的右子树，根的左子树设置null
     */
    public void flattenWithAwesomeSolution(TreeNode root) {
        while (root != null) {
            TreeNode move = root.left;
            while (move != null && move.right != null) {
                move = move.right;
            }
            // 此时move为左子树的最右节点
            if (move != null) {
                move.right = root.right;
                root.right = root.left;
                root.left = null;
            }
            root = root.right;
        }
    }

    /**
     * 二叉树的最近公共祖先
     * 10min ac
     * 击败99.78%
     */
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        // p 包含 q， 或 q 包含 p
        // p 与 q 不存在包含与被包含关系
        // 最近公共祖先考虑的应该是广度优先遍历
        if (contain(p, q)) {
            return p;
        }
        else if (contain(q, p)) {
            return q;
        }
        else {
            Deque<TreeNode> queue = new ArrayDeque<>();
            queue.offer(root);
            TreeNode res = null;
            while (!queue.isEmpty()) {
                TreeNode cur = queue.poll();
                if (contain(cur, p) && contain(cur, q)) {
                    res = cur;
                }
                if (cur.left != null) {
                    queue.offer(cur.left);
                }
                if (cur.right != null) {
                    queue.offer(cur.right);
                }
            }
            return res;
        }
    }

    private boolean contain(TreeNode startNode, TreeNode targetNode) {
        if (startNode == targetNode) {
            return true;
        } else if (startNode == null) {
            return false;
        }
        return contain(startNode.left, targetNode) || contain(startNode.right, targetNode);
    }

}
