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
        if (head == null || head.next == null) {
            return head;
        }
        // 3 1 2 5 4
        ListNode[] spilt = spilt(head);
        ListNode ltPart = sortList(spilt[0]);
        ListNode rtPart = sortList(spilt[1]);
        ListNode dummy = new ListNode();
        ListNode last = dummy;
        while (ltPart != null && rtPart != null) {
            if (ltPart.val < rtPart.val) {
                last.next = ltPart;
                last = ltPart;
                ltPart = ltPart.next;
            } else {
                last.next = rtPart;
                last = rtPart;
                rtPart = rtPart.next;
            }
        }
        last.next = rtPart == null ? ltPart : rtPart;
        return dummy.next;
    }

    private ListNode[] spilt(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;
        // 3 1 2 5 4
        // s
        // f
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }
        ListNode next = slow.next;
        slow.next = null;
        return new ListNode[] { head, next };
    }

    /**
     * 二叉树展开为链表
     */
    public void flatten(TreeNode root) {
        TreeNode node = root;
        while (node != null) {
            if (node.left != null) {
                TreeNode rt = node.right;
                node.right = node.left;
                node.left = null;
                TreeNode findRight = node.right;
                while (findRight != null && findRight.right != null) {
                    findRight = findRight.right;
                }
                findRight.right = rt;
            }
            node = node.right;
        }
    }

    /**
     * 从前序遍历和中序遍历序列构造二叉树
     */
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        // 递归得构造左右子树，限定构造的范围
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) {
            map.put(inorder[i], i);
        }
        return getRoot(preorder, inorder, map, 0, inorder.length - 1);
    }

    private int preorderIndex = 0;

    private TreeNode getRoot(int[] preorder, int[] inorder, Map<Integer, Integer> map, int start, int end) {
        if (end < start) {
            return null;
        }
        TreeNode root = new TreeNode(preorder[preorderIndex++]);
        int inorderIndex = map.get(root.val);
        root.left = getRoot(preorder, inorder, map, start, inorderIndex - 1);
        root.right = getRoot(preorder, inorder, map, inorderIndex + 1, end);
        return root;
    }

    /**
     * 组合总和
     * 没做出来
     * 你没有理解做回溯题的方法论，要画树状图去理解做
     * 不要那么想当然，你可以去看你以前的解法，这个dfs内部的for循环是可以不要的
     */
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> res = new ArrayList<>();
        dfs(candidates, res, new ArrayList<>(), target, 0);
        return res;
    }

    private void dfs(int[] candidates, List<List<Integer>> res, List<Integer> list, int target, int start) {
        if (target == 0) {
            res.add(new ArrayList<>(list));
            return;
        }
        if (start == candidates.length) {
            return;
        }
        dfs(candidates, res, list, target, start + 1);
        if (target >= candidates[start]) {
            list.add(candidates[start]);
            dfs(candidates, res, list, target - candidates[start], start);
            list.remove(list.size() - 1);
        }
    }

    /**
     * 分割回文串
     */
    public List<List<String>> partition(String s) {
        List<List<String>> res = new ArrayList<>();
        dfs(s, res, new ArrayList<>(), 0);
        return res;
    }

    private void dfs(String s, List<List<String>> res, List<String> list, int start) {
        if (start == s.length()) {
            res.add(new ArrayList<>(list));
            return;
        }
        for (int i = start; i < s.length(); i++) {
            if (valid(s, start, i)) {
                list.add(s.substring(start, i + 1));
                dfs(s, res, list, i + 1);
                list.remove(list.size() - 1);
            }
        }
    }

    private boolean valid(String s, int start, int end) {
        int lt = start;
        int rt = end;
        while (rt > lt) {
            if (s.charAt(rt) != s.charAt(lt)) {
                return false;
            }
            rt--;
            lt++;
        }
        return true;
    }

    /**
     * 最长递增子序列
     * 要求不准用dp，而用O（n）时间复杂度的算法
     */
    public int lengthOfLIS(int[] nums) {
        // 3 1 2 5 4
        List<Integer> list = new ArrayList<>();
        for (int num : nums) {
            if (list.isEmpty() || list.get(list.size() - 1) < num) {
                list.add(num);
            } else {
                list.set(binarySearch(list, num), num);
            }
        }
        return list.size();
    }

    private int binarySearch(List<Integer> list, int target) {
        // 1 2 5
        int lt = 0;
        int rt = list.size() - 1;
        while (rt >= lt) {
            int mid = (lt + rt) / 2;
            if (list.get(mid) > target) {
                rt = mid - 1;
            } else if (list.get(mid) < target) {
                lt = mid + 1;
            } else {
                return mid;
            }
        }
        return lt;
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
