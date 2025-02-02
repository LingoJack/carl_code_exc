package leetcodeHot100;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class leetcodeExercise {
    /**
     * 划分字母区间
     * 25min ac
     * 但是击败5%
     * 不如第一次做的时候想出来的思路
     * 第一次做的思路：
     * 记录每一个字母最后出现的位置，跟踪串内字母的最右边界
     * 跟踪并更新当前串长度
     */
    public List<Integer> partitionLabels(String s) {
        Map<Character, Integer> lastOccurIndexMap = new HashMap<>();
        Map<Character, Integer> firstOccurIndexMap = new HashMap<>();
        int len = s.length();
        for (int i = 0; i < len; i++) {
            char ch = s.charAt(i);
            lastOccurIndexMap.put(ch, i);
            firstOccurIndexMap.putIfAbsent(ch, i);
        }
        Character firstCh = s.charAt(0);
        Character lastCh = s.charAt(0);
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            char ch = s.charAt(i);
            if (firstCh == null) {
                firstCh = ch;
            }
            if (lastCh == null) {
                lastCh = ch;
            }
            if (i == lastOccurIndexMap.get(lastCh)) {
                res.add(i - firstOccurIndexMap.get(firstCh) + 1);
                firstCh = null;
                lastCh = null;
                continue;
            }
            if (lastOccurIndexMap.get(lastCh) < lastOccurIndexMap.get(ch)) {
                // baba
                lastCh = ch;
            }
        }
        return res;
    }

    /**
     * 二叉树的层序遍历
     */
    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        Deque<TreeNode> queue = new ArrayDeque<>();
        if (root != null) {
            queue.offer(root);
        }
        while (!queue.isEmpty()) {
            int size = queue.size();
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
                list.add(node.val);
            }
            res.add(list);
        }
        return res;
    }

    /**
     * 不同路径
     */
    public int uniquePaths(int m, int n) {
        int[][] dp = new int[m][n];
        for (int i = 0; i < m; i++) {
            dp[i][0] = 1;
        }
        for (int i = 0; i < n; i++) {
            dp[0][i] = 1;
        }
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
            }
        }
        return dp[m - 1][n - 1];
    }

    /**
     * 二叉树的最大深度
     * 思路是层序遍历
     */
    public int maxDepth(TreeNode root) {
        Deque<TreeNode> queue = new ArrayDeque<>();
        if (root != null) {
            queue.offer(root);
        }
        int height = 0;
        while (!queue.isEmpty()) {
            int size = queue.size();
            height++;
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                if (node.left != null)
                    queue.offer(node.left);
                if (node.right != null)
                    queue.offer(node.right);
            }
        }
        return height;
    }

    /**
     * 翻转二叉树
     */
    public TreeNode invertTree(TreeNode root) {
        invert(root);
        return root;
    }

    private void invert(TreeNode node) {
        if (node == null)
            return;
        TreeNode ltSon = node.left;
        node.left = node.right;
        node.right = ltSon;
        invert(node.left);
        invert(node.right);
    }

    /**
     * 对称二叉树
     */
    public boolean isSymmetric(TreeNode root) {
        return check(root);
    }

    private boolean check(TreeNode lt, TreeNode rt) {
        if(lt == null && rt == null) {
            return true;
        }
        if(lt != null && rt != null) {
            
        }
        return false;
    }
}
