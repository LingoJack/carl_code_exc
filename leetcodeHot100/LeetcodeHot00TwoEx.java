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
     * 又没做出来
     * 实现有问题，这已经是第三次刷了
     * 三次都没有做出来，都是有思路但是一写就bug多
     * 根本原因是我没有掌握滑动窗口的模板写法
     * 滑动窗口的模板写法应该是外层循环写右边界的移动（增大窗口），内层循环写左边界的移动（缩小窗口）
     * 
     * <pre>
    * int left = 0, right = 0;
    * // 外层循环增大窗口
    * while (right < nums.size()) {
    *     window.addLast(nums[right]);
    *     right++;
    *     // 内层循环缩小窗口
    *     while (window needs shrink) {
    *         window.removeFirst(nums[left]);
    *         left++;
    *     }
    * }
     * </pre>
     */
    public String minWindow(String s, String t) {
        Map<Character, Integer> need = new HashMap<>();
        Map<Character, Integer> have = new HashMap<>();
        int count = 0;
        for (char c : t.toCharArray()) {
            need.putIfAbsent(c, 0);
            need.put(c, need.get(c) + 1);
        }
        int fast = 0;
        int slow = 0;
        int minLen = Integer.MAX_VALUE;
        int start = 0;
        while (fast < s.length()) {
            char fastCh = s.charAt(fast);
            if (need.containsKey(fastCh)) {
                have.putIfAbsent(fastCh, 0);
                have.put(fastCh, have.get(fastCh) + 1);
                if (have.get(fastCh).equals(need.get(fastCh))) {
                    count++;
                }
            }
            fast++;
            while (slow < s.length() && count == need.size()) {
                char slowCh = s.charAt(slow);
                if (fast - slow + 1 < minLen) {
                    minLen = fast - slow + 1;
                    start = slow;
                }
                if (need.containsKey(slowCh)) {
                    have.put(slowCh, have.get(slowCh) - 1);
                    if (have.get(slowCh) < need.get(slowCh)) {
                        count--;
                    }
                }
                slow++;
            }
        }
        return minLen == Integer.MAX_VALUE ? "" : s.substring(start, Math.min(start + minLen, s.length()));
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

    /**
     * LFU
     */
    class LFUCache {

        public LFUCache(int capacity) {

        }

        public int get(int key) {

        }

        public void put(int key, int value) {

        }
    }

    /**
     * 找出数组的最大公约数（lc1979）
     */
    public int findGCD(int[] nums) {

    }
}
