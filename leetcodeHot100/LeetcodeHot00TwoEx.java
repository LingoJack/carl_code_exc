package leetcodeHot100;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;
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
     * 试错了比较多次，且被提示才做出来
     * 被认为是：没做出来
     * 核心是count还有ip地址段的判断：先判断长度，再判断前缀0，再判断值合理
     */
    public List<String> restoreIpAddresses(String s) {
        List<String> res = new ArrayList<>();
        dfs(s, res, new StringBuilder(), 0, 0);
        return res;
    }

    private void dfs(String s, List<String> res, StringBuilder sb, int start, int count) {
        if (count == 4) {
            if (start == s.length()) {
                res.add(sb.substring(0, sb.length() - 1).toString());
            }
            return;
        }
        for (int i = start; i < s.length(); i++) {
            String segement = s.substring(start, i + 1);
            if (valid(segement)) {
                int size = sb.length();
                sb.append(segement).append(".");
                dfs(s, res, sb, i + 1, count + 1);
                sb.setLength(size);
            }
        }
    }

    private boolean valid(String s) {
        if (s.length() > 3 || s.startsWith("0") && s.length() > 1) {
            return false;
        }
        int segement = Integer.valueOf(s);
        if (segement < 0 || segement > 255) {
            return false;
        }
        return true;
    }

    /**
     * 用最少的箭引爆气球
     * 依旧是重试了很多次才做出来
     * 核心：Integer.compare，还有count和rightBound的初始值的设定
     */
    public int findMinArrowShots(int[][] points) {
        Arrays.sort(points, (a, b) -> {
            return Integer.compare(a[0], b[0]) == 0 ? Integer.compare(a[1], b[1]) : Integer.compare(a[0], b[0]);
        });
        // [1,6][2,8][7,12][10,16]
        int count = 1;
        long rightBound = points[0][1];
        for (int i = 1; i < points.length; i++) {
            int[] point = points[i];
            if (rightBound >= point[0]) {
                rightBound = Math.min(point[1], rightBound);
            } else {
                rightBound = point[1];
                count++;
            }
        }
        return count;
    }

    /**
     * 搜索旋转排序数组
     */
    public int search(int[] nums, int target) {
        // 4,5,6,7,0,1,2
        // 找到低谷
        int end = findLowest(nums);
        int ans1 = binarySearch(nums, 0, end, target);
        int ans2 = binarySearch(nums, end + 1, nums.length - 1, target);
        return ans1 == -1 ? ans2 : ans1;
    }

    private int findLowest(int[] nums) {
        int lt = 0;
        int rt = nums.length - 1;
        while (rt > lt) {
            int mid = (lt + rt) / 2;
            if (nums[lt] < nums[mid]) {
                lt = mid;
            } else {
                rt = mid;
            }
        }
        return lt;
    }

    private int binarySearch(int[] nums, int start, int end, int target) {
        int lt = start;
        int rt = end;
        while (lt <= rt) {
            int mid = (lt + rt) / 2;
            if (nums[mid] > target) {
                rt = mid - 1;
            } else if (nums[mid] < target) {
                lt = mid + 1;
            } else {
                return mid;
            }
        }
        return -1;
    }

    /**
     * 寻找两个正序数组的中位数
     * 没做出来
     * 有思路但是没能实现
     */
    public double findMedianSortedArraysTwoEx(int[] nums1, int[] nums2) {
        int l1 = nums1.length;
        int l2 = nums2.length;
        if (l1 > l2) {
            return findMedianSortedArraysTwoEx(nums2, nums1);
        }
        // l1 <= l2
        // 1 3 5 8 9
        // 2 4 7
        int take1Left = 0, take1Right = l1;
        // 前半部分（奇数则取较大部分）的数量: 0000111、000111
        int take = (l1 + l2 + 1) / 2;
        while (take1Right > take1Left) {
            int take1 = (take1Left + take1Right) / 2;
            int take2 = take - take1;
            // 判断依据是nums1取的最后一个元素的下一个元素与nums2取的最后一个元素的大小关系
            if (nums1[(take1 - 1) + 1] > nums2[take2 - 1]) {
                // 最多就取到这了
                take1Right = take1;
            } else if (nums1[(take1 - 1) + 1] <= nums2[take2 - 1]) {
                // 取少了
                take1Left = take1 + 1;
            }
        }

        return -1;
    }

    /**
     * 最长回文子串
     */
    public String longestPalindrome(String s) {
        int size = s.length();
        boolean[][] dp = new boolean[size][size];
        int offset = 0;
        int maxLen = 0;
        for (int len = 1; len < size + 1; len++) {
            for (int start = 0; start < size - len + 1; start++) {
                int end = start + len - 1;
                if (len <= 2) {
                    dp[start][end] = s.charAt(start) == s.charAt(end);
                } else {
                    dp[start][end] = s.charAt(start) == s.charAt(end) && dp[start + 1][end - 1];
                }
                if (dp[start][end] && maxLen < len) {
                    offset = start;
                    maxLen = len;
                }
            }
        }
        return s.substring(offset, offset + maxLen);
    }

    /**
     * 数据流的中位数
     */
    class MedianFinder {
        private PriorityQueue<Integer> minPriorityQueue;
        private PriorityQueue<Integer> maxPriorityQueue;

        public MedianFinder() {
            this.minPriorityQueue = new PriorityQueue<>((a, b) -> Integer.compare(a, b));
            this.maxPriorityQueue = new PriorityQueue<>((a, b) -> Integer.compare(b, a));
        }

        public void addNum(int num) {
            // 2 3 4
            if (maxPriorityQueue.isEmpty() || maxPriorityQueue.peek() >= num) {
                maxPriorityQueue.offer(num);
                if (maxPriorityQueue.size() > minPriorityQueue.size() + 1) {
                    minPriorityQueue.offer(maxPriorityQueue.poll());
                }
            } else {
                minPriorityQueue.offer(num);
                if (minPriorityQueue.size() > maxPriorityQueue.size()) {
                    maxPriorityQueue.offer(minPriorityQueue.poll());
                }
            }
        }

        public double findMedian() {
            if ((maxPriorityQueue.size() + minPriorityQueue.size()) % 2 == 0) {
                return (minPriorityQueue.peek() + maxPriorityQueue.peek()) / 2.0;
            } else {
                return maxPriorityQueue.peek();
            }
        }
    }

    /**
     * 分割等和子集
     * 没做出来
     * 最开始居然想着用回溯做
     */
    public boolean canPartition(int[] nums) {
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        if (sum % 2 == 1) {
            return false;
        }
        int target = sum / 2;
        boolean[][] dp = new boolean[nums.length][target + 1];
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] <= target) {
                dp[i][nums[i]] = true;
            }
            dp[i][0] = true;
        }
        for (int index = 1; index < nums.length; index++) {
            for (int curSum = 0; curSum < target + 1; curSum++) {
                dp[index][curSum] = dp[index - 1][curSum]
                        || (nums[index] <= curSum && dp[index - 1][curSum - nums[index]]);
                if (dp[index][target]) {
                    return true;
                }
            }
        }
        return dp[nums.length - 1][target];
    }

    private boolean dfs(int[] nums, int target, int start) {
        if (target <= 0 || start == nums.length) {
            return target == 0;
        }
        // 放入与不放入元素start
        return dfs(nums, target, start + 1) || dfs(nums, target - nums[start], start + 1);
    }

    /**
     * 最长公共子序列
     */
    public int longestCommonSubsequence(String text1, String text2) {
        int[][] dp = new int[text1.length() + 1][text2.length() + 1];
        dp[0][0] = 0;
        for (int i = 1; i < text1.length() + 1; i++) {
            for (int j = 1; j < text2.length() + 1; j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        return dp[text1.length()][text2.length()];
    }

    /**
     * 多数元素
     */
    public int majorityElement(int[] nums) {
        int count = 0;
        int winner = 0;
        for (int num : nums) {
            if (count == 0) {
                winner = num;
            }
            if (winner == num) {
                count++;
            } else {
                count--;
            }
        }
        return winner;
    }

    /**
     * 找出数组的最大公约数（lc1979）
     */
    public int findGCD(int[] nums) {
        // 2 5 6 9 10
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int num : nums) {
            min = Math.min(min, num);
            max = Math.max(max, num);
        }
        // 辗转相除法
        // 2 5
        return gcd(min, max);
    }

    /**
     * 辗转相除法的模板
     * 记忆方法：
     * 上一个余数做被除数，被除数对上个余数取余作为新余数，知道被除数为0
     */
    private int gcd(int num1, int num2) {
        return num2 == 0 ? num1 : gcd(num2, num1 % num2);
    }

    private void swap(int[] nums, int a, int b) {
        int t = nums[a];
        nums[a] = nums[b];
        nums[b] = t;
    }

    /**
     * 最小公倍数
     * 利用公式，a > 0，b > 0 两数字的最小公倍数lcm和最大公约数gcd满足：
     * lcm = (a * b) / gcd
     */
    public long findLCM(long a, long b) {
        return (long) Math.abs(a * b) / getGCD(a, b);
    }

    private long getGCD(long a, long b) {
        return b == 0 ? a : getGCD(b, a % b);
    }

    /**
     * 最小公倍数等于K的子数组
     */
    public int subarrayLCM(int[] nums, int k) {
        int n = nums.length;
        int count = 0;
        // [i,j]的最小公倍数为dp[i][j]
        long[][] dp = new long[n][n];
        for (int len = 1; len < n + 1; len++) {
            for (int start = 0; start < n - len + 1; start++) {
                int end = start + len - 1;
                if (len == 1) {
                    dp[start][end] = nums[start];
                } else {
                    // 应对溢出的测试用例
                    dp[start][end] = dp[start][end - 1] > k ? (k + 1) : findLCM(dp[start][end - 1], nums[end]);
                }
                if (dp[start][end] == k) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * 字符串的最小公倍数先按照“字符串最小公倍数”的长度，
     * 用他们自身来各自构造两个字符串的相等，
     * 如果构造的字符串相等，
     * 那么构造出来的两个字符串就是他们的“字符串最小公倍数”，如果不相等则就直接输出-1
     */
    public String findGCDString(String s1, String s2) {
        int l1 = s1.length(), l2 = s2.length();
        long lcm = findLCM(l1, l2);
        String t1, t2;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lcm / l1; i++) {
            sb.append(s1);
        }
        t1 = sb.toString();
        sb.setLength(0);
        for (int i = 0; i < lcm / l2; i++) {
            sb.append(s2);
        }
        t2 = sb.toString();
        sb.setLength(0);
        return t1.equals(t2) ? t1 : "-1";
    }

    /**
     * 任务调度器
     * 没做出来 预留空置位是这题的核心，而且要意识到具体的任务名不重要
     */
    public int leastInterval(char[] tasks, int n) {
        int[] taskCounts = new int[26];
        for (char c : tasks) {
            taskCounts[c - 'A']++;
        }
        int minute = 0;
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>((a, b) -> Integer.compare(b, a));
        for (int taskCount : taskCounts) {
            if (taskCount != 0) {
                priorityQueue.offer(taskCount);
            }
        }
        while (!priorityQueue.isEmpty()) {
            List<Integer> list = new ArrayList<>();
            int time = 0;
            for (int i = 0; i < n + 1; i++) {
                if (!priorityQueue.isEmpty()) {
                    list.add(priorityQueue.poll());
                    time++;
                }
            }
            for (Integer freq : list) {
                freq--;
                if (freq > 0) {
                    priorityQueue.offer(freq);
                }
            }
            minute += !priorityQueue.isEmpty() ? (n + 1) : time;
        }
        return minute;
    }

    /**
     * LFU
     * 看了灵神的思路才做出来
     * 核心：
     * 每个频率一个双向链表
     * 维护一个最小频率
     * 一个key到Node的缓存
     */
    class LFUCache {

        public class Node {
            private int key;
            private int val;
            private int freq;

            public Node() {
                this.freq = 1;
            }

            public Node(int key, int val) {
                this.key = key;
                this.val = val;
                this.freq = 1;
            }
        }

        private Map<Integer, Node> cache;

        private Map<Integer, LinkedList<Node>> freqListMap;

        private int capacity;

        private int minFreq;

        public LFUCache(int capacity) {
            this.capacity = capacity;
            this.cache = new HashMap<>();
            this.freqListMap = new HashMap<>();
        }

        public int get(int key) {
            Node node = cache.get(key);
            if (node == null) {
                return -1;
            }
            LinkedList<Node> freqList = freqListMap.get(node.freq);
            freqList.remove(node);
            if (freqListMap.get(minFreq).isEmpty()) {
                minFreq++;
            }
            node.freq++;
            freqListMap.putIfAbsent(node.freq, new LinkedList<>());
            freqListMap.get(node.freq).addFirst(node);
            return node.val;
        }

        public void put(int key, int value) {
            Node node = cache.get(key);
            if (node != null) {
                get(key);
                node.val = value;
                return;
            }
            // node == null
            if (cache.size() >= capacity) {
                LinkedList<Node> minFreqList = freqListMap.get(minFreq);
                Node removedNode = minFreqList.removeLast();
                cache.remove(removedNode.key);
            }
            node = new Node(key, value);
            minFreq = 1;
            cache.put(key, node);
            freqListMap.putIfAbsent(node.freq, new LinkedList<>());
            LinkedList<Node> freqList = freqListMap.get(node.freq);
            freqList.addFirst(node);
        }
    }

    /**
     * 树的最长路径（二叉树的直径）
     */
    public int diameterOfBinaryTree(TreeNode root) {
        dfs(root);
        return maxDiameter;
    }

    private int maxDiameter = 0;

    private int dfs(TreeNode node) {
        if (node == null) {
            return 0;
        }
        int lt = dfs(node.left);
        int rt = dfs(node.right);
        maxDiameter = Math.max(maxDiameter, lt + rt);
        return Math.max(lt, rt) + 1;
    }

    public static void main(String[] args) {
        LeetcodeHot00TwoEx l = new LeetcodeHot00TwoEx();
        MyQueue myQueue = l.new MyQueue();
        myQueue.offer(1);
        myQueue.offer(2);
        myQueue.offer(3);
        myQueue.offer(4);
        myQueue.offer(5);
        for (int i = 0; i < 5; i++) {
            System.out.println(myQueue.poll());
        }
    }

    /**
     * 反转英语句子
     */
    public String reverseWords(String input) {
        String[] statements = input.split(",");
        StringBuilder sb = new StringBuilder();
        for (String statement : statements) {
            sb.append(reverseStatement(statement)).append(",");
        }
        return sb.substring(0, sb.length() - 1).toString();
    }

    private String reverseStatement(String statement) {
        if (statement.isBlank()) {
            return null;
        }
        Deque<String> stack = new ArrayDeque<>();
        int start = 0;
        while (start < statement.length()) {
            while (statement.charAt(start) == ' ') {
                start++;
            }
            int len = 1;
            while (start + len - 1 < statement.length() && statement.charAt(start + len - 1) != ' ') {
                len++;
            }
            stack.push(statement.substring(start, Math.min(start + len, statement.length())));
            start = start + len;
        }
        StringBuilder sb = new StringBuilder();
        while (!stack.isEmpty()) {
            sb.append(stack.pop()).append(" ");
        }
        return sb.substring(0, sb.length() - 1).toString();
    }

    /**
     * 用两个栈实现队列
     */
    public class MyQueue {
        Deque<Integer> stack;
        Deque<Integer> tempStack;

        public MyQueue() {
            stack = new ArrayDeque<>();
            tempStack = new ArrayDeque<>();
        }

        public void offer(int x) {
            while (!stack.isEmpty()) {
                tempStack.push(stack.pop());
            }
            stack.push(x);
            while (!tempStack.isEmpty()) {
                stack.push(tempStack.pop());
            }
        }

        public int peek() {
            return stack.peek();
        }

        public int poll() {
            return stack.pop();
        }

        public boolean empty() {
            return stack.isEmpty();
        }
    }

    /**
     * 四数之和
     */
    public List<List<Integer>> fourSum(int[] nums, int target) {
        Arrays.sort(nums); // 先对数组进行排序
        List<List<Integer>> res = new ArrayList<>();
        int len = nums.length;
        for (int first = 0; first < len - 3; first++) {
            if (first > 0 && nums[first] == nums[first - 1]) {
                continue; // 跳过重复元素
            }
            for (int second = first + 1; second < len - 2; second++) {
                if (second > first + 1 && nums[second] == nums[second - 1]) {
                    continue; // 跳过重复元素
                }
                int third = second + 1, fourth = len - 1;
                long sumToFind = (long) target - (long) nums[first] - (long) nums[second]; // 注意溢出
                while (third < fourth) {
                    if (nums[third] + nums[fourth] < sumToFind) {
                        third++;
                    } else if (nums[third] + nums[fourth] > sumToFind) {
                        fourth--;
                    } else {
                        res.add(Arrays.asList(nums[first], nums[second], nums[third], nums[fourth]));
                        while (third < fourth && nums[third] == nums[++third])
                            ; // 跳过重复元素
                        while (third < fourth && nums[fourth] == nums[--fourth])
                            ; // 跳过重复元素
                    }
                }
            }
        }
        return res;
    }

    /**
     * 使字符串达到最小字典序的最少交换次数
     * 感觉是快排？其实就是对每个字母排序
     * 那么就是快排找到应该插入的位置，如果一样就不自增计数
     * 腾讯wxg二面
     */
    public int minSwapTimeExceed(String s) {
        char[] chars = s.toCharArray();
        int len = chars.length;
        int count = 0;
        for (int i = 0; i < len; i++) {
            int minIndex = i;
            for (int j = i + 1; j < len; j++) {
                if (chars[j] < chars[minIndex]) {
                    minIndex = j;
                }
            }
            if (minIndex != i) {
                swap(chars, i, minIndex);
                count++;
            }
        }
        return count;
    }

    /**
     * 使字符串达到最小字典序的最少交换次数
     * wxg 微信支付二面
     * 可以明确的是，最少交换次数一定小于等于逆序对数
     */
    public int minSwap(String s) {
        char[] chars = s.toCharArray();
        int n = chars.length;
        char[] sorted = chars.clone();
        Arrays.sort(sorted);
        // 预处理每个字符在排序后的位置队列
        Map<Character, Queue<Integer>> charPositions = new HashMap<>();
        for (int i = 0; i < n; i++) {
            char c = sorted[i];
            charPositions.putIfAbsent(c, new LinkedList<>());
            charPositions.get(c).add(i);
        }
        // 构建映射数组，记录原字符串每个字符应该映射到排序后的哪个位置
        int[] map = new int[n];
        for (int i = 0; i < n; i++) {
            char c = chars[i];
            map[i] = charPositions.get(c).poll();
        }
        // 计算环的数量，从而计算最少交换次数
        boolean[] visited = new boolean[n];
        int swaps = 0;
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                int cycleSize = 0;
                int j = i;
                while (!visited[j]) {
                    visited[j] = true;
                    j = map[j];
                    cycleSize++;
                }
                swaps += (cycleSize - 1);
            }
        }
        return swaps;
    }

    private void swap(char[] chars, int i, int j) {
        char temp = chars[i];
        chars[i] = chars[j];
        chars[j] = temp;
    }

    /**
     * 重排链表
     */
    public void reorderList(ListNode head) {
        // 1 2 3 4
        // s
        // f
        ListNode slow = head;
        ListNode fast = head;
        while (fast.next != null && fast.next.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }
        ListNode second = slow.next;
        slow.next = null;
        ListNode last = null;
        ListNode temp = second;
        while (temp != null) {
            ListNode next = temp.next;
            temp.next = last;
            last = temp;
            temp = next;
        }
        ListNode node2 = last;
        ListNode node1 = head;
        ListNode dummy = new ListNode();
        ListNode prev = dummy;
        int count = 0;
        while (node1 != null && node2 != null) {
            if (count % 2 == 0) {
                prev.next = node1;
                prev = node1;
                node1 = node1.next;
            } else {
                prev.next = node2;
                prev = node2;
                node2 = node2.next;
            }
            count++;
        }
        prev.next = node1 == null ? node2 : node1;
        head = dummy.next;
    }

    private ListNode[] spiltTwo(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;
        while (fast.next != null && fast.next.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }
        ListNode second = slow.next;
        slow.next = null;
        return new ListNode[] { head, second };
    }

    private ListNode reverse(ListNode head) {
        ListNode last = null;
        ListNode node = head;
        while (node != null) {
            ListNode next = node.next;
            node.next = last;
            last = node;
            node = next;
        }
        return last;
    }

    /**
     * 不同路径II
     */
    public int uniquePathsWithObstacles(int[][] obstacleGrid) {
        int row = obstacleGrid.length, col = obstacleGrid[0].length;
        int[][] dp = new int[row][col];
        boolean hasObstacle = false;
        for (int i = 0; i < col; i++) {
            hasObstacle = hasObstacle || (obstacleGrid[0][i] == 1);
            if (!hasObstacle) {
                dp[0][i] = 1;
            } else {
                dp[0][i] = 0;
            }
        }
        hasObstacle = false;
        for (int i = 0; i < row; i++) {
            hasObstacle = hasObstacle || (obstacleGrid[i][0] == 1);
            if (!hasObstacle) {
                dp[i][0] = 1;
            } else {
                dp[i][0] = 0;
            }
        }
        for (int i = 1; i < row; i++) {
            for (int j = 1; j < col; j++) {
                if (obstacleGrid[i][j] == 1) {
                    dp[i][j] = 0;
                    continue;
                }
                dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
            }
        }
        return dp[row - 1][col - 1];
    }

    /**
     * 方阵求对角线元素最大质数
     */
    public int diagonalPrime(int[][] nums) {
        // 1 2 3
        // 4 5 6
        // 7 8 9
        int len = nums.length;
        int max = 0;
        for (int i = 0; i < len; i++) {
            if (isPrime(nums[i][i])) {
                max = Math.max(max, nums[i][i]);
            }
            if (isPrime(nums[i][len - 1 - i])) {
                max = Math.max(max, nums[i][len - 1 - i]);
            }
        }
        return max;
    }

    private boolean isPrime(int num) {
        for (int i = 2; i * i <= num; i++) {
            if (num % i == 0) {
                return false;
            }
        }
        return num != 1;
    }

    /**
     * 删除链表的倒数第N个节点
     */
    public ListNode removeNthFromEnd(ListNode head, int n) {
        // 2 3 5 4
        // f
        // s
        // n = 2
        ListNode fast = head;
        int count = 0;
        while (fast != null) {
            fast = fast.next;
            count++;
        }
        ListNode dummy = new ListNode(0, head);
        ListNode last = dummy;
        ListNode slow = head;
        while (count > n && slow != null) {
            last = slow;
            slow = slow.next;
            count--;
        }
        if (slow != null) {
            last.next = slow.next;
            slow.next = null;
        }
        return dummy.next;
    }

    public class ListNode {
        int val;
        ListNode next;

        public ListNode() {

        }

        public ListNode(int val) {
            this.val = val;
        }

        public ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    /**
     * 买卖股票的最佳时机
     * 只能交易一次
     */
    public int maxProfit(int[] prices) {
        int len = prices.length;
        // 0 未持有 1 持有
        int[][] dp = new int[len][2];
        dp[0][1] = -prices[0];
        dp[0][0] = 0;
        for (int i = 1; i < len; i++) {
            dp[i][0] = Math.max(dp[i - 1][1] + prices[i], dp[i - 1][0]);
            dp[i][1] = Math.max(-prices[i], dp[i - 1][1]);
        }
        return dp[len - 1][0];
    }

    /**
     * 买卖股票的最佳时机II
     * 不限制交易次数
     */
    public int maxProfitII(int[] prices) {
        int len = prices.length;
        // 0 未持有 1 持有
        int[][] dp = new int[len][2];
        dp[0][1] = -prices[0];
        dp[0][0] = 0;
        for (int i = 1; i < len; i++) {
            dp[i][0] = Math.max(dp[i - 1][1] + prices[i], dp[i - 1][0]);
            dp[i][1] = Math.max(dp[i - 1][0] - prices[i], dp[i - 1][1]);
        }
        return dp[len - 1][0];
    }

    /**
     * 买卖股票的最佳时机III
     * 没做出来
     */
    public int maxProfitIII(int[] prices) {
        int len = prices.length;
        // i,j,k 标识 j 交易次数，k持有状态
        int[][][] dp = new int[len][3][2];
        for(int tradeCount = 0; tradeCount < 3; tradeCount++) {
            
        }
        for (int index = 1; index < len; index++) {
            for (int tradeCount = 0; index < 3; tradeCount++) {
                if (tradeCount > 0) {
                    dp[index][tradeCount][0] = Math.max(dp[index - 1][tradeCount][0], dp[index - 1][tradeCount - 1][1] + prices[index]);
                    dp[index][tradeCount][1] = Math.max(dp[index - 1][tradeCount][0] - prices[index], dp[index - 1][tradeCount][1]);
                } else {
                    // 交易次数 == 0
                    dp[index][0][0] = Math.max(dp[index - 1][0][1], dp[index - 1][0][0] - prices[index]);
                }
            }
        }
        return dp[len - 1][2][0];
    }
}
