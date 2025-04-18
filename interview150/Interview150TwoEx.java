package interview150;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import interview150.Interview150TwoEx.LRUCache.CacheEntry;
import leetcodeHot100.link_list;

public class Interview150TwoEx {

    /**
     * 合并两个有序数组
     */
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int idx1 = m - 1;
        int idx2 = n - 1;
        int idx = m + n - 1;
        while (idx1 >= 0 || idx2 >= 0) {
            int num1 = idx1 >= 0 ? nums1[idx1] : Integer.MIN_VALUE;
            int num2 = idx2 >= 0 ? nums2[idx2] : Integer.MIN_VALUE;
            if (num1 > num2) {
                idx1--;
                nums1[idx] = num1;
            } else {
                idx2--;
                nums1[idx] = num2;
            }
            idx--;
        }
    }

    /**
     * 移除元素
     */
    public int removeElement(int[] nums, int val) {
        int fast = 0, slow = 0;
        while (fast < nums.length) {
            if (nums[fast] != val) {
                swap(nums, fast, slow++);
            }
            fast++;
        }
        return slow;
    }

    private void swap(int[] nums, int idx1, int idx2) {
        int t = nums[idx1];
        nums[idx1] = nums[idx2];
        nums[idx2] = t;
    }

    /**
     * 多数元素
     */
    public int majorityElement(int[] nums) {
        int count = 0;
        Integer winner = null;
        for (int num : nums) {
            if (winner == null) {
                winner = num;
            }
            if (winner == num) {
                count++;
            } else {
                count--;
                if (count == 0) {
                    winner = null;
                }
            }
        }
        return winner;
    }

    /**
     * 轮转数组
     */
    public void rotate(int[] nums, int k) {
        // 1 2 3 4 5 6 7 k=3
        // 5 6 7 1 2 3 4
        int len = nums.length;
        k %= len;
        reverse(nums, 0, len - 1);
        reverse(nums, 0, k - 1);
        reverse(nums, k, len - 1);
    }

    private void reverse(int[] nums, int start, int end) {
        if (end <= start) {
            return;
        }
        while (start < end) {
            swap(nums, start, end);
            start++;
            end--;
        }
    }

    /**
     * 买卖股票的最佳时机
     */
    public int maxProfit(int[] prices) {
        // dp[i][j] 到第i天为止，持有或不持有股票的最大收益
        int[][] dp = new int[prices.length][2];
        dp[0][0] = 0;
        dp[0][1] = -prices[0];
        for (int i = 1; i < prices.length; i++) {
            dp[i][0] = Math.max(dp[i - 1][0], dp[i - 1][1] + prices[i]);
            dp[i][1] = Math.max(dp[i - 1][1], -prices[i]);
        }
        return dp[prices.length - 1][0];
    }

    /**
     * 买卖股票的最佳时机
     * 这题和买卖股票的最佳时机II容易弄混
     * 区别是II可以买卖多次
     */
    public int maxProfitAnotherSolution(int[] prices) {
        int minPrice = Integer.MAX_VALUE;
        int maxProfit = 0;
        for (int price : prices) {
            if (minPrice > price) {
                minPrice = price;
            } else {
                maxProfit = Math.max(maxProfit, price - minPrice);
            }
        }
        return maxProfit;
    }

    /**
     * 分发糖果
     */
    public int candy(int[] ratings) {
        int len = ratings.length;
        int[] candy = new int[len];
        Arrays.fill(candy, 1);
        for (int i = 0; i < len - 1; i++) {
            if (ratings[i] < ratings[i + 1] && candy[i + 1] <= candy[i]) {
                candy[i + 1] = candy[i] + 1;
            }
        }
        for (int i = len - 1; i > 0; i--) {
            if (ratings[i - 1] > ratings[i] && candy[i - 1] <= candy[i]) {
                candy[i - 1] = candy[i] + 1;
            }
        }
        return Arrays.stream(candy).sum();
    }

    /**
     * 反转字符串中的单词
     */
    public String reverseWords(String s) {
        List<String> words = new ArrayList<>();
        int len = s.length();
        int fast = 0;
        while (fast < len && s.charAt(fast) == ' ') {
            fast++;
        }
        int slow = fast;
        while (fast < len) {
            if (s.charAt(fast) == ' ') {
                words.add(s.substring(slow, fast));
                do {
                    fast++;
                } while (fast < len && s.charAt(fast) == ' ');
                slow = fast;
                continue;
            }
            fast++;
        }
        if (slow < len && s.charAt(slow) != ' ') {
            words.add(s.substring(slow));
        }
        StringBuilder sb = new StringBuilder();
        for (int i = words.size() - 1; i >= 0; i--) {
            sb.append(words.get(i));
            if (i != 0) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    /**
     * 找出字符串中第一个匹配项的下标
     * 没做出来
     * 这个果然涉及KMP算法（其实也可以不涉及KMP，滑动窗口就可以）
     */
    public int strStr(String haystack, String needle) {
        int hLen = haystack.length(), nLen = needle.length();
        if (nLen == 0) {
            return 0;
        }
        // 构建真前后缀表
        int[] partialMathchTable = preprocess(needle);
        // 根据前后缀表执行相应的逻辑
        int idx = 0, matchIdx = 0;
        while (idx < hLen) {
            while (matchIdx > 0 && haystack.charAt(idx) != needle.charAt(matchIdx)) {
                matchIdx = partialMathchTable[matchIdx - 1];
            }
            if (haystack.charAt(idx) == needle.charAt(matchIdx)) {
                matchIdx++;
            }
            if (matchIdx == nLen) {
                return idx - nLen + 1;
            }
            idx++;
        }
        return -1;
    }

    /**
     * 构建KMP的部分匹配表， Partial Match Table
     * 注意这里的部分匹配表和前后缀相等长度表是不同的，不应该弄混
     * 关于KMP算法的理解：https://www.zhihu.com/question/21923021/answer/281346746
     * 什么是部分匹配？
     * a b a b a b
     * a b a b _ _
     * _ _ a b a b
     * 这个字符串的部分匹配值是4，因为[0, 3]和[2, 5]是一样的
     * 
     * @param needle
     * @return
     */
    private int[] preprocess(String needle) {
        int idx = 1, matchIdx = 0; // idx 是当前处理的索引，matchIdx 是前缀长度
        int[] table = new int[needle.length()];
        while (idx < needle.length()) {
            if (needle.charAt(idx) == needle.charAt(matchIdx)) {
                // 当前字符匹配，增加匹配长度
                matchIdx++;
                table[idx] = matchIdx;
                idx++;
                continue;
            }
            if (matchIdx > 0) {
                // 不匹配时，回退到前一个匹配位置
                matchIdx = table[matchIdx - 1];
            } else {
                // 如果无法回退，说明部分匹配值为0
                table[idx] = 0;
                idx++;
            }
        }
        return table;
    }

    /**
     * 找出字符串中第一个匹配项的下标
     */
    public int strStrWithRollingWindow(String haystack, String needle) {
        int lt = 0;
        int hLen = haystack.length(), nLen = needle.length();
        while (lt <= (hLen - nLen)) {
            int rt = lt + nLen;
            if (haystack.substring(lt, rt).equals(needle)) {
                return lt;
            }
            lt++;
        }
        return -1;
    }

    /**
     * 盛最多水的容器
     */
    public int maxArea(int[] height) {
        int lt = 0, rt = height.length - 1;
        int res = 0;
        while (lt < rt) {
            int h = Math.min(height[lt], height[rt]);
            int w = rt - lt;
            res = Math.max(w * h, res);
            if (height[lt] < height[rt]) {
                lt++;
            } else {
                rt--;
            }
        }
        return res;
    }

    /**
     * 三数之和
     */
    public List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);
        int len = nums.length;
        List<List<Integer>> res = new ArrayList<>();
        for (int first = 0; first < len - 2; first++) {
            if (first > 0 && nums[first] == nums[first - 1]) {
                continue;
            }
            int n1 = nums[first];
            int second = first + 1, third = len - 1;
            while (second < third) {
                int n2 = nums[second], n3 = nums[third];
                int sum = n1 + n2 + n3;
                if (sum == 0) {
                    res.add(List.of(n1, n2, n3));
                    third--;
                    while (third > second && nums[third] == nums[third + 1]) {
                        third--;
                    }
                    second++;
                    while (second < third && nums[second] == nums[second - 1]) {
                        second++;
                    }
                } else if (sum < 0) {
                    second++;
                } else {
                    third--;
                }
            }
        }
        return res;
    }

    public List<List<Integer>> threeSumTimeExceed(int[] nums) {
        Arrays.sort(nums);
        int len = nums.length;
        List<List<Integer>> res = new ArrayList<>();
        for (int first = 0; first < len - 2; first++) {
            if (first > 0 && nums[first] == nums[first - 1]) {
                continue;
            }
            int n1 = nums[first];
            for (int second = first + 1; second < len - 1; second++) {
                if (second > first + 1 && nums[second] == nums[second - 1]) {
                    continue;
                }
                int n2 = nums[second];
                for (int third = second + 1; third < len; third++) {
                    if (third > second + 1 && nums[third] == nums[third - 1]) {
                        continue;
                    }
                    int n3 = nums[third];
                    if (n1 + n2 + n3 == 0) {
                        res.add(List.of(n1, n2, n3));
                    }
                }
            }
        }
        return res;
    }

    /**
     * 长度最小的子数组
     */
    public int minSubArrayLen(int target, int[] nums) {
        int lt = 0, rt = 0;
        int sum = 0;
        int res = nums.length;
        boolean hasFound = false;
        while (rt < nums.length) {
            sum += nums[rt];
            while (sum >= target) {
                hasFound |= true;
                res = Math.min(res, rt - lt + 1);
                sum -= nums[lt];
                lt++;
            }
            rt++;
        }
        return hasFound ? res : 0;
    }

    /**
     * 无重复字符的最长子串
     */
    public int lengthOfLongestSubstring(String s) {
        Set<Character> set = new HashSet<>();
        int lt = 0, rt = 0;
        int len = s.length();
        int maxLen = 0;
        while (rt < len) {
            char rc = s.charAt(rt);
            while (set.contains(rc)) {
                char lc = s.charAt(lt);
                set.remove(lc);
                lt++;
            }
            set.add(rc);
            if (maxLen < (rt - lt + 1)) {
                maxLen = rt - lt + 1;
            }
            rt++;
        }
        return maxLen;
    }

    /**
     * 最小覆盖子串
     */
    public String minWindow(String s, String t) {
        Map<Character, Integer> need = new HashMap<>();
        Map<Character, Integer> have = new HashMap<>();
        for (char c : t.toCharArray()) {
            need.put(c, need.getOrDefault(c, 0) + 1);
        }
        int valid = 0;
        int lt = 0, rt = 0;
        int sLen = s.length();
        int start = -1, minLen = Integer.MAX_VALUE;
        while (rt < sLen) {
            char rc = s.charAt(rt);
            if (need.containsKey(rc)) {
                have.put(rc, have.getOrDefault(rc, 0) + 1);
                if (have.get(rc).equals(need.get(rc))) {
                    valid++;
                }
            }
            while (valid >= need.size()) {
                if (rt - lt + 1 <= minLen) {
                    minLen = rt - lt + 1;
                    start = lt;
                }
                char lc = s.charAt(lt);
                if (need.containsKey(lc)) {
                    if (have.get(lc).equals(need.get(lc))) {
                        valid--;
                    }
                    have.put(lc, have.getOrDefault(lc, 0) - 1);
                }
                lt++;
            }
            rt++;
        }
        return start == -1 ? "" : s.substring(start, start + minLen);
    }

    /**
     * 螺旋矩阵
     */
    public List<Integer> spiralOrder(int[][] matrix) {
        int row = matrix.length, col = matrix[0].length;
        List<Integer> res = new ArrayList<>();
        int rowIdx = 0, colIdx = 0;
        int upBound = 0, downBound = row - 1;
        int leftBound = 0, rightBound = col - 1;
        int count = 0;
        int total = row * col;
        while (count < total) {
            // right
            for (int i = leftBound; count < total && i <= rightBound; i++) {
                res.add(matrix[rowIdx][i]);
                count++;
                colIdx = i;
            }
            upBound++;
            // down
            for (int i = upBound; count < total && i <= downBound; i++) {
                res.add(matrix[i][colIdx]);
                count++;
                rowIdx = i;
            }
            rightBound--;
            // left
            for (int i = rightBound; count < total && i >= leftBound; i--) {
                res.add(matrix[rowIdx][i]);
                count++;
                colIdx = i;
            }
            downBound--;
            // up
            for (int i = downBound; count < total && i >= upBound; i--) {
                res.add(matrix[i][colIdx]);
                count++;
                rowIdx = i;
            }
            leftBound++;
        }
        return res;
    }

    /**
     * 旋转图像
     */
    public void rotate(int[][] nums) {
        int row = nums.length, col = nums[0].length;
        for (int i = 0; 2 * i < row; i++) {
            for (int j = 0; j < col; j++) {
                int t = nums[i][j];
                nums[i][j] = nums[row - 1 - i][j];
                nums[row - 1 - i][j] = t;
            }
        }
        for (int i = 0; i < row; i++) {
            for (int j = 0; j <= i; j++) {
                int t = nums[i][j];
                nums[i][j] = nums[j][i];
                nums[j][i] = t;
            }
        }
    }

    /**
     * 矩阵置零
     */
    public void setZeroes(int[][] matrix) {
        int row = matrix.length, col = matrix[0].length;
        boolean[] zeroRows = new boolean[row];
        boolean[] zeroCols = new boolean[col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (matrix[i][j] == 0) {
                    zeroCols[j] = true;
                    zeroRows[i] = true;
                }
            }
        }
        for (int i = 0; i < row; i++) {
            if (zeroRows[i]) {
                for (int j = 0; j < col; j++) {
                    matrix[i][j] = 0;
                }
            }
        }
        for (int i = 0; i < col; i++) {
            if (zeroCols[i]) {
                for (int j = 0; j < row; j++) {
                    matrix[j][i] = 0;
                }
            }
        }
    }

    /**
     * 赎金信
     */
    public boolean canConstruct(String ransomNote, String magazine) {
        int[] count = new int[26];
        for (char c : ransomNote.toCharArray()) {
            count[c - 'a']++;
        }
        for (char c : magazine.toCharArray()) {
            count[c - 'a']--;
        }
        for (int val : count) {
            if (val > 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 有效的字母异位词
     */
    public boolean isAnagram(String s, String t) {
        int[] count = new int[26];
        for (char c : s.toCharArray()) {
            count[c - 'a']++;
        }
        for (char c : t.toCharArray()) {
            count[c - 'a']--;
        }
        for (int val : count) {
            if (val != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 字母异位词分组
     */
    public List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> stringListMap = new HashMap<>();
        for (String s : strs) {
            char[] chs = s.toCharArray();
            Arrays.sort(chs);
            String sortedString = new String(chs);
            List<String> list = stringListMap.getOrDefault(sortedString, new ArrayList<>());
            list.add(s);
            stringListMap.put(sortedString, list);
        }
        return new ArrayList<>(stringListMap.values());
    }

    /**
     * 两数之和
     */
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(target - nums[i])) {
                return new int[] { map.get(target - nums[i]), i };
            }
            map.put(nums[i], i);
        }
        return null;
    }

    /**
     * 快乐数
     */
    public boolean isHappy(int n) {
        Set<Integer> set = new HashSet<>();
        int sum = 0;
        while (true) {
            int digit = n % 10;
            sum += digit * digit;
            n /= 10;
            if (n == 0) {
                if (set.contains(sum)) {
                    return false;
                } else if (sum == 1) {
                    return true;
                }
                set.add(sum);
                n = sum;
                sum = 0;
            }
        }
    }

    /**
     * 最长连续序列
     * 没做出来，忘记了
     * 只记得从最低的点开始找
     * 实际上是先把所有都放进去Set，然后看看是否包含更低的
     */
    public int longestConsecutive(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int num : nums) {
            set.add(num);
        }
        int res = 0;
        for (int num : set) {
            if (set.contains(num - 1)) {
                continue;
            }
            int count = 0;
            int repl = num;
            while (set.contains(repl)) {
                repl++;
                count++;
            }
            res = Math.max(res, count);
        }
        return res;
    }

    /**
     * 合并区间
     */
    public int[][] merge(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> {
            return a[0] == b[0] ? Integer.compare(a[1], b[1]) : Integer.compare(a[0], b[0]);
        });
        Deque<int[]> stack = new ArrayDeque<>();
        for (int[] interval : intervals) {
            if (!stack.isEmpty()) {
                int[] last = stack.peek();
                if (last[1] >= interval[0]) {
                    stack.pop();
                    stack.push(new int[] { last[0], Math.max(last[1], interval[1]) });
                    continue;
                }
            }
            stack.push(interval);
        }
        int[][] res = new int[stack.size()][2];
        int idx = stack.size() - 1;
        for (int[] interval : stack) {
            res[idx--] = interval;
        }
        return res;
    }

    /**
     * 用最少的箭引爆气球
     */
    public int findMinArrowShots(int[][] points) {
        Arrays.sort(points, (a, b) -> a[0] != b[0] ? Integer.compare(a[0], b[0]) : Integer.compare(a[1], b[1]));
        long edge = Long.MIN_VALUE;
        int count = 0;
        for (int[] point : points) {
            if (edge < point[0]) {
                edge = point[1];
                count++;
            }
            edge = Math.min(edge, point[1]);
        }
        return count;
    }

    /**
     * 有效的括号
     */
    public boolean isValid(String s) {
        Deque<Character> stack = new ArrayDeque<>();
        HashMap<Character, Character> map = new HashMap<>();
        map.put('}', '{');
        map.put(']', '[');
        map.put(')', '(');
        for (char c : s.toCharArray()) {
            if (map.containsKey(c)) {
                if (stack.isEmpty() || !stack.peek().equals(map.get(c))) {
                    return false;
                }
                stack.pop();
                continue;
            }
            stack.push(c);
        }
        return stack.size() == 0;
    }

    /**
     * 最小栈
     */
    class MinStack {

        private Deque<Integer> stack;
        private Deque<Integer> minStack;

        public MinStack() {
            this.stack = new ArrayDeque<>();
            this.minStack = new ArrayDeque<>();
        }

        public void push(int val) {
            stack.push(val);
            if (minStack.isEmpty() || minStack.peek() >= val) {
                minStack.push(val);
            }
        }

        public void pop() {
            int val = stack.pop();
            if (val == minStack.peek()) {
                minStack.pop();
            }
        }

        public int top() {
            return stack.peek();
        }

        public int getMin() {
            return minStack.peek();
        }
    }

    /**
     * LCR120.寻找文件副本
     */
    public int findRepeatDocument(int[] documents) {
        int len = documents.length;
        boolean[] exist = new boolean[len];
        for (int document : documents) {
            if (exist[document]) {
                return document;
            }
            exist[document] = true;
        }
        return -1;
    }

    /**
     * 逆波兰表达式求值
     */
    public int evalRPN(String[] tokens) {
        Deque<Integer> stack = new ArrayDeque<>();
        for (String token : tokens) {
            if (token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/")) {
                int num1 = stack.pop();
                int num2 = stack.pop();
                int num = 0;
                if (token.equals("+") || token.equals("-")) {
                    num = token.equals("+") ? num1 + num2 : num2 - num1;
                } else {
                    num = token.equals("*") ? num1 * num2 : num2 / num1;
                }
                stack.push(num);
                continue;
            }
            stack.push(Integer.parseInt(token));
        }
        return stack.pop();
    }

    /**
     * 环形链表
     */
    public boolean hasCycle(ListNode head) {
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
            if (fast == slow) {
                return true;
            }
        }
        return false;
    }

    /**
     * 两数相加
     */
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode();
        ListNode last = dummy;
        ListNode node1 = l1, node2 = l2;
        int carry = 0;
        while (carry != 0 || node1 != null || node2 != null) {
            int num1 = node1 == null ? 0 : node1.val;
            int num2 = node2 == null ? 0 : node2.val;
            int num = num1 + num2 + carry;
            carry = num / 10;
            num %= 10;
            ListNode node = new ListNode(num);
            last.next = node;
            last = node;
            if (node1 != null) {
                node1 = node1.next;
            }
            if (node2 != null) {
                node2 = node2.next;
            }
        }
        return dummy.next;
    }

    /**
     * 合并两个有序链表
     */
    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        ListNode dummy = new ListNode();
        ListNode last = dummy;
        ListNode node1 = list1, node2 = list2;
        while (node1 != null || node2 != null) {
            int num1 = node1 == null ? Integer.MAX_VALUE : node1.val;
            int num2 = node2 == null ? Integer.MAX_VALUE : node2.val;
            if (num1 < num2) {
                last.next = node1;
                last = node1;
                node1 = node1.next;
            } else {
                last.next = node2;
                last = node2;
                node2 = node2.next;
            }
        }
        return dummy.next;
    }

    /**
     * 随机链表的复制
     */
    public class CopyRandomListClass {

        private class Node {
            int val;
            Node next;
            Node random;

            public Node(int val) {
                this.val = val;
            }
        }

        public Node copyRandomList(Node head) {
            Map<Node, Node> map = new HashMap<>();
            Node node = head;
            while (node != null) {
                Node newNode = new Node(node.val);
                map.put(node, newNode);
                node = node.next;
            }
            node = head;
            while (node != null) {
                Node newNode = map.get(node);
                newNode.next = map.get(node.next);
                newNode.random = map.get(node.random);
                node = node.next;
            }
            return map.get(head);
        }
    }

    /**
     * 反转链表II
     */
    public ListNode reverseBetween(ListNode head, int left, int right) {
        ListNode dummy = new ListNode(0, head);
        int count = 0;
        ListNode node = dummy;
        ListNode last = dummy, prevGroupEnd = dummy;
        ListNode lt = null, rt = null;
        while (count < left) {
            last = node;
            node = node.next;
            count++;
        }
        lt = node;
        while (count < right) {
            node = node.next;
            count++;
        }
        rt = node;
        ListNode prevEnd = last, nextStart = rt.next;
        node = lt;
        last = null;
        while (node != nextStart) {
            ListNode next = node.next;
            node.next = last;
            last = node;
            node = next;
        }
        lt.next = nextStart;
        prevEnd.next = rt;
        return dummy.next;
    }

    /**
     * K个一组反转链表
     */
    public ListNode reverseKGroup(ListNode head, int k) {
        ListNode dummy = new ListNode(0, head);
        ListNode slow = head;
        ListNode prevGroupEnd = dummy;
        while (slow != null) {
            int count = 0;
            ListNode fast = slow;
            while (count < k && fast != null) {
                count++;
                fast = fast.next;
            }
            if (count < k) {
                break;
            }
            ListNode nextGroupStart = fast;
            ListNode[] newHeadAndTail = reverseListNodes(slow, fast);
            ListNode newHead = newHeadAndTail[0], newTail = newHeadAndTail[1];
            newTail.next = nextGroupStart;
            prevGroupEnd.next = newHead;
            prevGroupEnd = newTail;
            slow = nextGroupStart;
        }
        return dummy.next;
    }

    private ListNode[] reverseListNodes(ListNode head, ListNode tail) {
        ListNode node = head;
        ListNode last = null;
        while (node != tail) {
            ListNode next = node.next;
            node.next = last;
            last = node;
            node = next;
        }
        return new ListNode[] { last, head };
    }

    /**
     * 删除链表的倒数第N个结点
     */
    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode dummy = new ListNode(0, head);
        ListNode slow = head, fast = head;
        int len = 0;
        while (fast != null) {
            fast = fast.next;
            len++;
        }
        ListNode last = dummy;
        while (len != n) {
            last = slow;
            slow = slow.next;
            len--;
        }
        last.next = slow.next;
        slow.next = null;
        return dummy.next;
    }

    /**
     * 删除排序链表中的重复元素II
     */
    public ListNode deleteDuplicates(ListNode head) {
        ListNode dummy = new ListNode(0, head);
        ListNode node = head;
        ListNode last = dummy;
        while (node != null) {
            ListNode next = node.next;
            boolean duplicated = false;
            while (next != null && next.val == node.val) {
                node.next = next.next;
                next = node.next;
                duplicated = true;
            }
            if (duplicated) {
                last.next = node.next;
            } else {
                last = node;
            }
            node = node.next;
        }
        return dummy.next;
    }

    /**
     * LRU缓存
     */
    class LRUCache {

        public class CacheEntry {
            private int key;
            private int val;
            private CacheEntry next;
            private CacheEntry prev;

            public CacheEntry() {

            }

            public CacheEntry(int key, int val) {
                this.val = val;
                this.key = key;
            }
        }

        private Map<Integer, CacheEntry> cache;

        private CacheEntry head;

        private CacheEntry tail;

        private int capacity;

        public LRUCache(int capacity) {
            head = new CacheEntry();
            tail = new CacheEntry();
            head.next = tail;
            tail.prev = head;
            cache = new HashMap<>();
            this.capacity = capacity;
        }

        public int get(int key) {
            if (!cache.containsKey(key)) {
                return -1;
            }
            CacheEntry entry = cache.get(key);
            entry.prev.next = entry.next;
            entry.next.prev = entry.prev;
            entry.next = null;
            entry.prev = null;
            entry.prev = head;
            entry.next = head.next;
            head.next.prev = entry;
            head.next = entry;
            return entry.val;
        }

        public void put(int key, int value) {
            if (cache.containsKey(key)) {
                CacheEntry entry = cache.get(key);
                entry.next.prev = entry.prev;
                entry.prev.next = entry.next;
                entry.next = null;
                entry.prev = null;
                entry.prev = head;
                entry.next = head.next;
                head.next.prev = entry;
                head.next = entry;
                entry.val = value;
                return;
            }
            CacheEntry entry = new CacheEntry(key, value);
            entry.prev = this.head;
            entry.next = this.head.next;
            this.head.next.prev = entry;
            this.head.next = entry;
            cache.put(key, entry);
            if (capacity < cache.size()) {
                CacheEntry removedEntry = tail.prev;
                removedEntry.prev.next = tail;
                tail.prev = removedEntry.prev;
                removedEntry.next = null;
                removedEntry.prev = null;
                cache.remove(removedEntry.key);
            }
        }
    }

    /**
     * 二叉树的最大深度
     */
    public int maxDepth(TreeNode root) {
        return height(root);
    }

    private int height(TreeNode node) {
        if (node == null) {
            return 0;
        }
        return Math.max(height(node.left), height(node.right)) + 1;
    }

    /**
     * 相同的树
     */
    public boolean isSameTree(TreeNode p, TreeNode q) {
        if (p == null && q == null) {
            return true;
        }
        if (p != null && q != null && p.val == q.val) {
            return isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
        }
        return false;
    }

    /**
     * 翻转二叉树
     */
    public TreeNode invertTree(TreeNode root) {
        if (root == null) {
            return root;
        }
        TreeNode t = root.left;
        root.left = root.right;
        root.right = t;
        invertTree(root.left);
        invertTree(root.right);
        return root;
    }

    /**
     * 对称二叉树
     */
    public boolean isSymmetric(TreeNode root) {
        if (root == null) {
            return true;
        }
        return isSysmetric(root.left, root.right);
    }

    private boolean isSysmetric(TreeNode lt, TreeNode rt) {
        if (lt == null && rt == null) {
            return true;
        } else if (lt != null && rt != null) {
            if (lt.val != rt.val) {
                return false;
            }
            return isSysmetric(lt.left, rt.right) && isSysmetric(lt.right, rt.left);
        }
        return false;
    }

    /**
     * 从前序和中序遍历构造二叉树
     */
    public TreeNode buildTreeFromInorderAndPreorder(int[] preorder, int[] inorder) {
        preorderIdx = 0;
        Map<Integer, Integer> inorderIdxMap = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) {
            inorderIdxMap.put(inorder[i], i);
        }
        return getRootFromInorderScopeAndPreorderIdx(preorder, inorder, inorderIdxMap, 0, inorder.length - 1);
    }

    private int preorderIdx;

    private TreeNode getRootFromInorderScopeAndPreorderIdx(int[] preorder, int[] inorder,
            Map<Integer, Integer> inorderIdxMap, int start, int end) {
        if (end < start) {
            return null;
        }
        TreeNode root = new TreeNode(preorder[preorderIdx++]);
        Integer inorderIdx = inorderIdxMap.get(root.val);
        root.left = getRootFromInorderScopeAndPreorderIdx(preorder, inorder, inorderIdxMap, start, inorderIdx - 1);
        root.right = getRootFromInorderScopeAndPreorderIdx(preorder, inorder, inorderIdxMap, inorderIdx + 1, end);
        return root;
    }

    /**
     * 从中序和后序遍历构造二叉树
     */
    public TreeNode buildTreeFromInorderAndPostorder(int[] inorder, int[] postorder) {
        postorderIdx = postorder.length - 1;
        Map<Integer, Integer> inorderMap = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) {
            inorderMap.put(inorder[i], i);
        }
        return getRootFromInorderScopeAndPostorder(postorder, inorder, inorderMap, 0, inorder.length - 1);
    }

    private int postorderIdx;

    private TreeNode getRootFromInorderScopeAndPostorder(int[] postorder, int[] inorder,
            Map<Integer, Integer> inorderIdxMap, int start, int end) {
        if (end < start) {
            return null;
        }
        TreeNode root = new TreeNode(postorder[postorderIdx--]);
        int inorderIdx = inorderIdxMap.get(root.val);
        root.right = getRootFromInorderScopeAndPostorder(postorder, inorder, inorderIdxMap, inorderIdx + 1, end);
        root.left = getRootFromInorderScopeAndPostorder(postorder, inorder, inorderIdxMap, start, inorderIdx - 1);
        return root;
    }

    /**
     * 填充每个节点的下一个右侧节点指针
     */
    public class ConnectSolution {
        public Node connect(Node root) {
            Deque<Node> queue = new ArrayDeque<>();
            if (root == null) {
                return null;
            }
            queue.offer(root);
            while (!queue.isEmpty()) {
                int size = queue.size();
                Node last = null;
                for (int i = 0; i < size; i++) {
                    Node node = queue.poll();
                    if (last != null) {
                        last.next = node;
                    }
                    last = node;
                    if (node.left != null) {
                        queue.offer(node.left);
                    }
                    if (node.right != null) {
                        queue.offer(node.right);
                    }
                }
            }
            return root;
        }

        private class Node {
            int val;
            Node left;
            Node right;
            Node next;

            public Node() {

            }

            public Node(int val) {
                this.val = val;
            }

            public Node(int val, Node left, Node right, Node next) {
                this.val = val;
                this.left = left;
                this.right = right;
                this.next = next;
            }
        }
    }

    /**
     * 统计公平数对的数目
     * 这个类似两数之和的解法是超时的
     * 需要注意到，因为不需要返回具体的数对，只需要数对的数量，所以排序不会影响最终结果
     */
    public long countFairPairs(int[] nums, int lower, int upper) {
        Arrays.sort(nums);
        long count = 0;
        int mid = nums.length - 1, rt = nums.length - 1;
        for (int lt = 0; lt < nums.length - 1; lt++) {
            while (mid > lt && nums[mid] + nums[lt] >= lower) {
                mid--;
            }
            while (rt > lt && nums[rt] + nums[lt] > upper) {
                rt--;
            }
            // System.out.printf("lt = %s, mid = %s, rt = %s, delta count = %s\n", lt, mid,
            // rt, rt - mid);
            count += Math.max(rt, lt) - Math.max(mid, lt);
        }
        return count;
    }

    /**
     * 统计公平数对的数目
     * 这个类似两数之和的解法是超时的
     * 需要注意到，因为不需要返回具体的数对，只需要数对的数量，所以排序不会影响最终结果
     */
    public long countFairPairsTimeExceed(int[] nums, int lower, int upper) {
        Map<Integer, Integer> map = new HashMap<>();
        int count = 0;
        for (int num : nums) {
            for (int another : map.keySet()) {
                int sum = another + num;
                if (sum <= upper && sum >= lower) {
                    count += map.get(another);
                }
            }
            map.put(num, map.getOrDefault(num, 0) + 1);
        }
        return count;
    }

    public long countFairPairsAwesomeSolutionBinarySearchArray(int[] nums, int lower, int upper) {
        Arrays.sort(nums);
        long ans = 0;
        for (int j = 0; j < nums.length; j++) {
            int r = lowerBound(nums, j, upper - nums[j] + 1);
            int l = lowerBound(nums, j, lower - nums[j]);
            ans += r - l;
        }
        return ans;
    }

    private int lowerBound(int[] nums, int right, int target) {
        int left = -1;
        while (left + 1 < right) {
            int mid = (left + right) >>> 1;
            if (nums[mid] < target) {
                left = mid;
            } else {
                right = mid;
            }
        }
        return right;
    }

    public long countFairPairsWithBinarySearch(int[] nums, int lower, int upper) {
        List<Integer> list = new ArrayList<>();
        long count = 0;
        for (int num : nums) {
            int left = lowerBound(list, lower - num);
            int right = upperBound(list, upper - num);
            count += right - left;
            int pos = lowerBound(list, num);
            list.add(pos, num);
        }
        return count;
    }

    private int lowerBound(List<Integer> list, int target) {
        int left = 0, right = list.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (list.get(mid) < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return left;
    }

    // 返回第一个 > target 的位置（upper_bound）
    private int upperBound(List<Integer> list, int target) {
        int left = 0, right = list.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (list.get(mid) <= target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return left;
    }

    /**
     * 二叉树展开为链表
     */
    public void flatten(TreeNode root) {
        TreeNode node = root;
        while (node != null) {
            if (node.left != null) {
                TreeNode right = node.right;
                node.right = node.left;
                node.left = null;
                TreeNode tail = node;
                while (tail.right != null) {
                    tail = tail.right;
                }
                tail.right = right;
            }
            node = node.right;
        }
    }

    /**
     * 路径总和
     */
    public boolean hasPathSum(TreeNode root, int targetSum) {
        if (root == null) {
            return false;
        }
        return (targetSum == root.val && root.left == null && root.right == null)
                || hasPathSum(root.left, targetSum - root.val)
                || hasPathSum(root.right, targetSum - root.val);
    }

    /**
     * 求根节点到叶子节点数字之和
     */
    public int sumNumbers(TreeNode root) {
        this.sum = 0;
        dfs(root, 0);
        return this.sum;
    }

    private int sum;

    private void dfs(TreeNode node, int pathSum) {
        if (node.left == null && node.right == null) {
            sum += pathSum + node.val;
            return;
        }
        int val = (pathSum + node.val) * 10;
        if (node.left != null) {
            dfs(node.left, val);
        }
        if (node.right != null) {
            dfs(node.right, val);
        }
    }

    /**
     * 二叉树中的最大路径和
     */
    public int maxPathSum(TreeNode root) {

    }
}
