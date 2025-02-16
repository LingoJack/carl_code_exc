package leetcodeHot100;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class LeetcodeHot00TwoEx {

    /**
     * 全排列
     */
    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        dfs(nums, new boolean[nums.length], res, new ArrayList<>());
        return res;
    }

    private void dfs(int[] nums, boolean[] visited, List<List<Integer>> res, List<Integer> list) {
        if (list.size() == nums.length) {
            res.add(new ArrayList<>(list));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (visited[i]) {
                continue;
            }
            list.add(nums[i]);
            visited[i] = true;
            dfs(nums, visited, res, list);
            list.remove(list.size() - 1);
            visited[i] = false;
        }
    }

    /**
     * 盛最多水的容器
     */
    public int maxArea(int[] height) {
        int len = height.length;
        int lt = 0;
        int rt = len - 1;
        int max = 0;
        while (rt >= lt) {
            int h = Math.min(height[lt], height[rt]);
            int w = rt - lt;
            max = Math.max(max, w * h);
            if (height[lt] < height[rt]) {
                lt++;
            } else {
                rt--;
            }
        }
        return max;
    }

    /**
     * 和为K的子数组
     */
    public int subarraySum(int[] nums, int k) {
        int[] prefix = new int[nums.length];
        prefix[0] = nums[0];
        for (int i = 1; i < nums.length; i++) {
            prefix[i] = prefix[i - 1] + nums[i];
        }
        int count = 0;
        // 1 1 1
        // 1 2 3
        // 3
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, 1);
        for (int i = 0; i < prefix.length; i++) {
            count += map.getOrDefault(prefix[i] - k, 0);
            map.put(prefix[i], map.getOrDefault(prefix[i], 0) + 1);
        }
        return count;
    }

    /**
     * 最小覆盖子串
     */
    public String minWindow(String s, String t) {

    }

    /**
     * 排序链表
     */
    public ListNode sortList(ListNode head) {

    }

    /**
     * 二叉树展开为链表
     */
    public void flatten(TreeNode root) {

    }

    /**
     * 从前序遍历和中序遍历序列构造二叉树
     */
    public TreeNode buildTree(int[] preorder, int[] inorder) {

    }

    /**
     * 组合总和
     */
    public List<List<Integer>> combinationSumTwoEx(int[] candidates, int target) {

    }

    /**
     * 分割回文串
     */
    public List<List<String>> partition(String s) {

    }

    /**
     * 最长递增子序列
     */
    public int lengthOfLIS(int[] nums) {

    }

    /**
     * 复原ip地址
     */
    public List<String> restoreIpAddresses(String s) {

    }

    /**
     * 用最少的箭引爆气球
     */
    public int findMinArrowShots(int[][] points) {

    }

    /**
     * 搜索旋转排序数组
     */
    public int search(int[] nums, int target) {

    }

    /**
     * 寻找两个正序数组的中位数
     */
    public double findMedianSortedArraysTwoEx(int[] nums1, int[] nums2) {

    }

    /**
     * 最长回文子串
     */
    public String longestPalindrome(String s) {

    }

    /**
     * 数据流的中位数
     */
    class MedianFinder {

    }

    /**
     * 分割等和子集
     */
    public boolean canPartition(int[] nums) {

    }

    /**
     * 最长公共子序列
     */
    public int longestCommonSubsequence(String text1, String text2) {

    }

    /**
     * 多数元素
     */
    public int majorityElement(int[] nums) {

    }
}
