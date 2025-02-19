package leetcodeHot100;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
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
     * LFU
     * 没做出来
     */
    class LFUCache {

        private static class Node {
            int key, value, freq = 1; // 新书只读了一次
            Node prev, next;

            Node(int key, int value) {
                this.key = key;
                this.value = value;
            }
        }

        private final int capacity;
        private final Map<Integer, Node> keyToNode = new HashMap<>();
        private final Map<Integer, Node> freqToDummy = new HashMap<>();
        private int minFreq;

        public LFUCache(int capacity) {
            this.capacity = capacity;
        }

        public int get(int key) {
            Node node = getNode(key);
            return node != null ? node.value : -1;
        }

        public void put(int key, int value) {
            Node node = getNode(key);
            if (node != null) { // 有这本书
                node.value = value; // 更新 value
                return;
            }
            if (keyToNode.size() == capacity) { // 书太多了
                Node dummy = freqToDummy.get(minFreq);
                Node backNode = dummy.prev; // 最左边那摞书的最下面的书
                keyToNode.remove(backNode.key);
                remove(backNode); // 移除
                if (dummy.prev == dummy) { // 这摞书是空的
                    freqToDummy.remove(minFreq); // 移除空链表
                }
            }
            node = new Node(key, value); // 新书
            keyToNode.put(key, node);
            pushFront(1, node); // 放在「看过 1 次」的最上面
            minFreq = 1;
        }

        private Node getNode(int key) {
            if (!keyToNode.containsKey(key)) { // 没有这本书
                return null;
            }
            Node node = keyToNode.get(key); // 有这本书
            remove(node); // 把这本书抽出来
            Node dummy = freqToDummy.get(node.freq);
            if (dummy.prev == dummy) { // 抽出来后，这摞书是空的
                freqToDummy.remove(node.freq); // 移除空链表
                if (minFreq == node.freq) {
                    minFreq++;
                }
            }
            pushFront(++node.freq, node); // 放在右边这摞书的最上面
            return node;
        }

        // 创建一个新的双向链表
        private Node newList() {
            Node dummy = new Node(0, 0); // 哨兵节点
            dummy.prev = dummy;
            dummy.next = dummy;
            return dummy;
        }

        // 在链表头添加一个节点（把一本书放在最上面）
        private void pushFront(int freq, Node x) {
            Node dummy = freqToDummy.computeIfAbsent(freq, k -> newList());
            x.prev = dummy;
            x.next = dummy.next;
            x.prev.next = x;
            x.next.prev = x;
        }

        // 删除一个节点（抽出一本书）
        private void remove(Node x) {
            x.prev.next = x.next;
            x.next.prev = x.prev;
        }
    }

    /**
     * 找出数组的最大公约数（lc1979）
     */
    public int findGCD(int[] nums) {
        
    }

    /**
     * 最小公倍数
     */
    public int findLCM(int[] nums) {

    }

    /**
     * 字符串的最小公倍数
     */
    public String findGCDString(String s1, String s2) {

    }
}
