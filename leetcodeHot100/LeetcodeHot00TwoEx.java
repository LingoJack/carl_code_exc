package leetcodeHot100;

import java.util.ArrayList;
import java.util.List;
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
        
    }

    /**
     * 和为K的子数组
     */
    public int subarraySum(int[] nums, int k) {
        CompletableFuture
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
