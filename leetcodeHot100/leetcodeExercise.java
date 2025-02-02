package leetcodeHot100;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
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
        if (root == null) {
            return true;
        }
        return check(root.left, root.right);
    }

    private boolean check(TreeNode lt, TreeNode rt) {
        if (lt == null && rt == null) {
            return true;
        }
        if (lt != null && rt != null) {
            if (lt.val != rt.val) {
                return false;
            }
            return check(lt.left, rt.right) && check(lt.right, rt.left);
        }
        return false;
    }

    /**
     * 买卖股票的最佳时机
     * 这里需要限制交易次数
     * 不限制的做法：dp[i][1] = Math.max(dp[i - 1][0] - prices[i], dp[i - 1][1]);
     * 限制的做法： dp[i][1] = Math.max(-prices[i], dp[i - 1][1]);
     */
    public int maxProfit(int[] prices) {
        // 0 未持有 1 持有
        int[][] dp = new int[prices.length][2];
        dp[0][0] = 0;
        dp[0][1] = -prices[0];
        for (int i = 1; i < prices.length; i++) {
            dp[i][0] = Math.max(dp[i - 1][1] + prices[i], dp[i - 1][0]);
            dp[i][1] = Math.max(-prices[i], dp[i - 1][1]);
        }
        return Math.max(dp[prices.length - 1][1], dp[prices.length - 1][0]);
    }

    /**
     * 跳跃游戏
     * 核心是记录范围的变化
     */
    public boolean canJump(int[] nums) {
        // 3,2,1,0,4
        // 2,3,1,1,4
        int scope = 0;
        for (int i = 0; i <= scope && scope < nums.length; i++) {
            scope = Math.max(scope, i + nums[i]);
        }
        return scope >= nums.length - 1;
    }

    /**
     * 跳跃游戏II
     * dp做出来了，但是时间复杂度太高
     */
    public int jump(int[] nums) {
        // 跳到i位置所需的最少跳跃次数
        int[] dp = new int[nums.length];
        Arrays.fill(dp, nums.length);
        dp[0] = 0;
        for (int i = 0; i < nums.length; i++) {
            for (int j = 0; j <= i; j++) {
                if (j + nums[j] >= i) {
                    dp[i] = Math.min(dp[i], dp[j] + 1);
                }
            }
        }
        return dp[nums.length - 1];
    }

    /**
     * 全排列
     * 这次没做出来。。。
     * 第一次做的时候可是一次过并且击败99%的
     * 必须要每天做题保持敏感度和思维
     */
    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        dfs4Permute(nums, res, new ArrayList<>(), new boolean[21]);
        return res;
    }

    private void dfs4Permute(int[] nums, List<List<Integer>> res, List<Integer> list, boolean[] used) {
        if (list.size() == nums.length) {
            res.add(new ArrayList<>(list));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (used[10 + nums[i]]) {
                continue;
            }
            used[10 + nums[i]] = true;
            list.add(nums[i]);
            dfs4Permute(nums, res, list, used);
            list.remove(list.size() - 1);
            used[10 + nums[i]] = false;
        }
    }

    /**
     * 子集
     */
    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        dfs4Subsets(res, new ArrayList<>(), 0, nums);
        return res;
    }

    private void dfs4Subsets(List<List<Integer>> res, List<Integer> list, int index, int[] nums) {
        if (index == nums.length) {
            res.add(new ArrayList<>(list));
            return;
        }
        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                dfs4Subsets(res, list, index + 1, nums);
            } else {
                list.add(nums[index]);
                dfs4Subsets(res, list, index + 1, nums);
                list.remove(list.size() - 1);
            }
        }
    }

    /**
     * 电话号码的字母组合
     */
    public List<String> letterCombinations(String digits) {
        List<String> res = new ArrayList<>();
        if (digits.isEmpty()) {
            return res;
        }
        char[][] map = new char[][] {
                {},
                {},
                { 'a', 'b', 'c' },
                { 'd', 'e', 'f' },
                { 'g', 'h', 'i' },
                { 'j', 'k', 'l' },
                { 'm', 'n', 'o' },
                { 'p', 'q', 'r', 's' },
                { 't', 'u', 'v' },
                { 'w', 'x', 'y', 'z' }
        };
        dfs4LetterCombainations(digits, map, res, new StringBuilder(), 0);
        return res;
    }

    private void dfs4LetterCombainations(String digits, char[][] map, List<String> res, StringBuilder sb, int index) {
        if (index == digits.length()) {
            res.add(sb.toString());
            return;
        }
        char[] chs = map[digits.charAt(index) - '0'];
        for (int i = 0; i < chs.length; i++) {
            sb.append(chs[i]);
            dfs4LetterCombainations(digits, map, res, sb, index + 1);
            sb.setLength(sb.length() - 1);
        }
    }

    /**
     * 组合总和
     */
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        Arrays.sort(candidates);
        List<List<Integer>> res = new ArrayList<>();
        dfs4CombainationSum(candidates, target, res, new ArrayList<>(), 0);
        return res;
    }

    private void dfs4CombainationSum(int[] candidates, int target, List<List<Integer>> res, List<Integer> list,
            int index) {
        if (target == 0) {
            res.add(new ArrayList<>(list));
            return;
        }
        if (target < 0 || index >= candidates.length) {
            return;
        }
        for (int i = index; i < candidates.length; i++) {
            if (target >= candidates[i]) {
                list.add(candidates[i]);
                dfs4CombainationSum(candidates, target - candidates[i], res, list, i);
                list.remove(list.size() - 1);
            }
        }
    }
}
