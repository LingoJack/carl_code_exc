package interview150;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Stream;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

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
        maxSum = Integer.MIN_VALUE;
        dfs(root);
        return maxSum;
    }

    private int maxSum;

    private int dfs(TreeNode node) {
        if (node == null) {
            return 0;
        }
        int lt = dfs(node.left);
        int rt = dfs(node.right);
        lt = lt > 0 ? lt : 0;
        rt = rt > 0 ? rt : 0;
        maxSum = Math.max(maxSum, node.val + lt + rt);
        return node.val + Math.max(lt, rt);
    }

    /**
     * 完全二叉树的节点个数
     */
    public int countNodes(TreeNode root) {
        count = 0;
        dfs4CountNodes(root);
        return count;
    }

    private int count;

    private void dfs4CountNodes(TreeNode node) {
        if (node == null) {
            return;
        }
        count++;
        dfs4CountNodes(node.left);
        dfs4CountNodes(node.right);
    }

    /**
     * 二叉树的最近公共祖先
     * 层序遍历的解法
     */
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (isAncestor(p, q)) {
            return p;
        }
        if (isAncestor(q, p)) {
            return q;
        }
        Deque<TreeNode> queue = new ArrayDeque<>();
        if (root == null) {
            return null;
        }
        queue.offer(root);
        TreeNode ancestor = null;
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                if (isAncestor(node, p) && isAncestor(node, q)) {
                    ancestor = node;
                }
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
        }
        return ancestor;
    }

    private boolean isAncestor(TreeNode ancestor, TreeNode node) {
        if (ancestor == null) {
            return false;
        }
        if (ancestor == node) {
            return true;
        }
        return isAncestor(ancestor.left, node) || isAncestor(ancestor.right, node);
    }

    /**
     * 二叉树的最近公共祖先
     * DFS解法，十分巧妙
     */
    public TreeNode lowestCommonAncestorDFS(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null) {
            return null;
        }
        if (root == p || root == q) {
            return root;
        }
        TreeNode lt = lowestCommonAncestorDFS(root.left, p, q);
        TreeNode rt = lowestCommonAncestorDFS(root.right, p, q);
        if (lt == null && rt == null) {
            return null;
        } else if (lt != null && rt != null) {
            return root;
        }
        return lt == null ? rt : lt;
    }

    /**
     * 二叉树的右视图
     */
    public List<Integer> rightSideView(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) {
            return res;
        }
        Deque<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
                if (i == size - 1) {
                    res.add(node.val);
                }
            }
        }
        return res;
    }

    /**
     * 二叉树的层平均值
     */
    public List<Double> averageOfLevels(TreeNode root) {
        List<Double> res = new ArrayList<>();
        if (root == null) {
            return res;
        }
        Deque<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            double sum = 0;
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                sum += node.val;
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
            res.add(sum / size);
        }
        return res;
    }

    /**
     * 二叉树的层序遍历
     */
    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) {
            return res;
        }
        Deque<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                list.add(node.val);
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
            res.add(list);
        }
        return res;
    }

    /**
     * 二叉搜索树的最小绝对差
     */
    public int getMinimumDifference(TreeNode root) {
        minDiff = Integer.MAX_VALUE;
        dfs4GetMinimunDifference(root);
        return minDiff;
    }

    private int minDiff;

    private void dfs4GetMinimunDifference(TreeNode node) {
        if (node == null) {
            return;
        }
        if (node.left != null) {
            TreeNode ltTail = node.left;
            while (ltTail.right != null) {
                ltTail = ltTail.right;
            }
            minDiff = Math.min(minDiff, Math.abs(ltTail.val - node.val));
            dfs4GetMinimunDifference(node.left);
        }
        if (node.right != null) {
            TreeNode rtHead = node.right;
            while (rtHead.left != null) {
                rtHead = rtHead.left;
            }
            minDiff = Math.min(minDiff, Math.abs(rtHead.val - node.val));
            dfs4GetMinimunDifference(node.right);
        }
    }

    /**
     * 二叉搜索树中第K小的元素
     */
    public int kthSmallest(TreeNode root, int k) {
        this.k = k;
        dfs4KthSmallest(root);
        return res;
    }

    private int k;

    private int res;

    private void dfs4KthSmallest(TreeNode node) {
        if (node == null) {
            return;
        }
        dfs4KthSmallest(node.left);
        this.k--;
        if (this.k == 0) {
            this.res = node.val;
            return;
        }
        dfs4KthSmallest(node.right);
    }

    /**
     * 验证二叉搜索树
     * DFS解法
     */
    public boolean isValidBST(TreeNode root) {
        if (root == null) {
            return true;
        }
        TreeNode lt = root.left;
        while (lt != null && lt.right != null) {
            lt = lt.right;
        }
        if (lt != null && lt.val >= root.val) {
            return false;
        }
        TreeNode rt = root.right;
        while (rt != null && rt.left != null) {
            rt = rt.left;
        }
        if (rt != null && rt.val <= root.val) {
            return false;
        }
        return isValidBST(root.left) && isValidBST(root.right);
    }

    /**
     * 验证二叉搜索树
     * BFS解法
     */
    public boolean isValidBSTWithBFS(TreeNode root) {
        if (root == null) {
            return false;
        }
        Deque<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            if (!valid(node)) {
                return false;
            }
            if (node.left != null) {
                queue.offer(node.left);
            }
            if (node.right != null) {
                queue.offer(node.right);
            }
        }
        return true;
    }

    private boolean valid(TreeNode node) {
        if (node == null) {
            return true;
        }
        TreeNode ltTail = node.left, rtHead = node.right;
        while (ltTail != null && ltTail.right != null) {
            ltTail = ltTail.right;
        }
        while (rtHead != null && rtHead.left != null) {
            rtHead = rtHead.left;
        }
        long lt = ltTail == null ? Long.MIN_VALUE : ltTail.val;
        long rt = rtHead == null ? Long.MAX_VALUE : rtHead.val;
        return (lt < node.val) && (node.val < rt);
    }

    /**
     * 岛屿数量
     */
    public int numIslands(char[][] grid) {
        int count = 0;
        int row = grid.length, col = grid[0].length;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (grid[i][j] == '0') {
                    continue;
                }
                count++;
                dfs(grid, i, j);
            }
        }
        return count;
    }

    private void dfs(char[][] grid, int rowIdx, int colIdx) {
        int row = grid.length, col = grid[0].length;
        if (!(rowIdx >= 0 && rowIdx < row && colIdx >= 0 && colIdx < col)) {
            return;
        }
        if (grid[rowIdx][colIdx] != '1') {
            return;
        }
        grid[rowIdx][colIdx] = '0';
        dfs(grid, rowIdx - 1, colIdx);
        dfs(grid, rowIdx + 1, colIdx);
        dfs(grid, rowIdx, colIdx - 1);
        dfs(grid, rowIdx, colIdx + 1);
    }

    /**
     * 课程表
     */
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        int[] inDegrees = new int[numCourses];
        Map<Integer, List<Integer>> succeedCoursesMap = new HashMap<>();
        for (int[] rel : prerequisites) {
            inDegrees[rel[0]]++;
            List<Integer> list = succeedCoursesMap.getOrDefault(rel[1], new ArrayList<>());
            list.add(rel[0]);
            succeedCoursesMap.put(rel[1], list);
        }
        Deque<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < numCourses; i++) {
            if (inDegrees[i] == 0) {
                queue.offer(i);
            }
        }
        int valid = 0;
        while (!queue.isEmpty()) {
            Integer course = queue.poll();
            valid++;
            List<Integer> list = succeedCoursesMap.getOrDefault(course, new ArrayList<>());
            for (Integer succeed : list) {
                inDegrees[succeed]--;
                if (inDegrees[succeed] == 0) {
                    queue.offer(succeed);
                }
            }
        }
        return valid == numCourses;
    }

    /**
     * 实现Trie（前缀树）
     */
    class Trie {

        private class Node {
            char c;
            Map<Character, Node> next;
            boolean isEndOfWord;

            public Node(char c) {
                this.c = c;
                this.next = new HashMap<>();
                this.isEndOfWord = false;
            }
        }

        private Node root;

        Map<String, Boolean> map;

        public Trie() {
            this.root = new Node('0');
            this.map = new HashMap<>();
        }

        public void insert(String word) {
            map.put(word, true);
            int len = word.length();
            Node node = root;
            for (int i = 0; i < len; i++) {
                char c = word.charAt(i);
                if (node.next.containsKey(c)) {
                    node = node.next.get(c);
                    continue;
                }
                Node newNode = new Node(c);
                node.next.put(c, newNode);
                node = node.next.get(c);
            }
            node.isEndOfWord = true;
        }

        public boolean search(String word) {
            return map.containsKey(word);
        }

        public boolean startsWith(String prefix) {
            Node node = root;
            int len = prefix.length();
            for (int i = 0; i < len; i++) {
                char c = prefix.charAt(i);
                if (!node.next.containsKey(c)) {
                    return false;
                }
                node = node.next.get(c);
            }
            return true;
        }
    }

    /**
     * 电话号码的字母组合
     */
    public List<String> letterCombinations(String digits) {
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
        List<String> res = new ArrayList<>();
        dfs(res, map, 0, digits, new StringBuilder());
        return res;
    }

    private void dfs(List<String> res, char[][] map, int idx, String digit, StringBuilder sb) {
        if (idx == digit.length()) {
            if (sb.isEmpty()) {
                return;
            }
            res.add(sb.toString());
            return;
        }
        char[] chs = map[digit.charAt(idx) - '0'];
        for (char c : chs) {
            sb.append(c);
            dfs(res, map, idx + 1, digit, sb);
            sb.setLength(sb.length() - 1);
        }
    }

    /**
     * 组合
     */
    public List<List<Integer>> combine(int n, int k) {
        List<List<Integer>> res = new ArrayList<>();
        dfs(res, new ArrayList<>(), k, 1, n);
        return res;
    }

    private void dfs(List<List<Integer>> res, List<Integer> list, int k, int start, int n) {
        if (list.size() == k) {
            res.add(new ArrayList<>(list));
            return;
        }
        for (int i = start; i <= n; i++) {
            list.add(i);
            dfs(res, list, k, i + 1, n);
            list.remove(list.size() - 1);
        }
    }

    /**
     * 全排列
     */
    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        dfs(res, new ArrayList<>(), nums, new boolean[nums.length]);
        return res;
    }

    private void dfs(List<List<Integer>> res, List<Integer> list, int[] nums, boolean[] used) {
        if (list.size() == nums.length) {
            res.add(new ArrayList<>(list));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (used[i]) {
                continue;
            }
            used[i] = true;
            list.add(nums[i]);
            dfs(res, list, nums, used);
            list.remove(list.size() - 1);
            used[i] = false;
        }
    }

    /**
     * 组合总和
     */
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> res = new ArrayList<>();
        dfs(res, new ArrayList<>(), candidates, target, 0);
        return res;
    }

    private void dfs(List<List<Integer>> res, List<Integer> list, int[] candidates, int target, int start) {
        if (target == 0) {
            res.add(new ArrayList<>(list));
            return;
        }
        if (start == candidates.length) {
            return;
        }
        dfs(res, list, candidates, target, start + 1);
        if (candidates[start] <= target) {
            list.add(candidates[start]);
            dfs(res, list, candidates, target - candidates[start], start);
            list.remove(list.size() - 1);
        }
    }

    /**
     * 括号生成
     */
    public List<String> generateParenthesis(int n) {
        List<String> res = new ArrayList<>();
        dfs(res, new StringBuilder(), n, n);
        return res;
    }

    private void dfs(List<String> res, StringBuilder sb, int leftCount, int rightCount) {
        if (leftCount == 0 && rightCount == 0) {
            res.add(sb.toString());
            return;
        }
        if (leftCount > 0) {
            sb.append('(');
            dfs(res, sb, leftCount - 1, rightCount);
            sb.setLength(sb.length() - 1);
        }
        if (rightCount > leftCount) {
            sb.append(')');
            dfs(res, sb, leftCount, rightCount - 1);
            sb.setLength(sb.length() - 1);
        }
    }

    /**
     * 单词搜索
     */
    public boolean exist(char[][] board, String word) {
        int row = board.length, col = board[0].length;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (dfs(board, word, new boolean[row][col], 0, i, j)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean dfs(char[][] board, String word, boolean[][] used, int matchIdx, int rowIdx, int colIdx) {
        if (matchIdx == word.length()) {
            return true;
        }
        int row = board.length, col = board[0].length;
        if (!(rowIdx >= 0 && rowIdx < row && colIdx >= 0 && colIdx < col)) {
            return false;
        }
        if (used[rowIdx][colIdx]) {
            return false;
        }
        if (board[rowIdx][colIdx] != word.charAt(matchIdx)) {
            return false;
        }
        used[rowIdx][colIdx] = true;
        boolean res = dfs(board, word, used, matchIdx + 1, rowIdx + 1, colIdx)
                || dfs(board, word, used, matchIdx + 1, rowIdx - 1, colIdx)
                || dfs(board, word, used, matchIdx + 1, rowIdx, colIdx + 1)
                || dfs(board, word, used, matchIdx + 1, rowIdx, colIdx - 1);
        used[rowIdx][colIdx] = false;
        return res;
    }

    /**
     * 将有序数组转换为二叉搜索树
     */
    public TreeNode sortedArrayToBST(int[] nums) {
        if (nums == null) {
            return null;
        }
        return dfs(nums, 0, nums.length - 1);
    }

    private TreeNode dfs(int[] nums, int start, int end) {
        if (end < start) {
            return null;
        }
        int mid = (start + end) >> 1;
        TreeNode root = new TreeNode(nums[mid]);
        root.left = dfs(nums, start, mid - 1);
        root.right = dfs(nums, mid + 1, end);
        return root;
    }

    /**
     * 排序链表
     */
    public ListNode sortList(ListNode head) {
        // 由于需要递归调用sortList，故需要设置截止条件
        if (head == null || head.next == null) {
            return head;
        }
        ListNode[] frontAndBackHeads = splitList(head);
        ListNode front = sortList(frontAndBackHeads[0]);
        ListNode back = sortList(frontAndBackHeads[1]);
        ListNode dummy = new ListNode();
        ListNode last = dummy;
        while (front != null || back != null) {
            int frontVal = front == null ? Integer.MAX_VALUE : front.val;
            int backVal = back == null ? Integer.MAX_VALUE : back.val;
            if (frontVal < backVal) {
                last.next = front;
                last = front;
                front = front.next;
            } else {
                last.next = back;
                last = back;
                back = back.next;
            }
        }
        return dummy.next;
    }

    private ListNode[] splitList(ListNode head) {
        // 此处没有处理head为null的判断，故需要在sortList方法里确保head不为null
        ListNode slow = head, fast = head;
        ListNode last = null;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            last = slow;
            slow = slow.next;
        }
        // 1 3 5 7 9
        // s
        // f
        last.next = null;
        return new ListNode[] { head, slow };
    }

    /**
     * 合并K个升序链表
     */
    public ListNode mergeKLists(ListNode[] lists) {
        PriorityQueue<ListNode> priorityQueue = new PriorityQueue<>((a, b) -> Integer.compare(a.val, b.val));
        for (ListNode list : lists) {
            if (list == null) {
                continue;
            }
            priorityQueue.offer(list);
        }
        ListNode dummy = new ListNode();
        ListNode last = dummy;
        while (!priorityQueue.isEmpty()) {
            ListNode node = priorityQueue.poll();
            last.next = node;
            last = node;
            if (node.next != null) {
                priorityQueue.offer(node.next);
            }
        }
        return dummy.next;
    }

    /**
     * 最大子数组和
     */
    public int maxSubArray(int[] nums) {
        int sum = 0;
        int max = nums[0];
        for (int num : nums) {
            if (sum < 0) {
                sum = 0;
            }
            sum += num;
            max = Math.max(max, sum);
        }
        return max;
    }

    /**
     * 搜索插入位置
     */
    public int searchInsert(int[] nums, int target) {
        int lt = 0, rt = nums.length - 1;
        while (lt <= rt) {
            int mid = lt + (rt - lt) / 2;
            if (nums[mid] < target) {
                lt = mid + 1;
            } else if (nums[mid] > target) {
                rt = mid - 1;
            } else {
                rt = mid - 1;
            }
        }
        return lt;
    }

    /**
     * 搜索二维矩阵
     */
    public boolean searchMatrix(int[][] matrix, int target) {
        int row = matrix.length, col = matrix[0].length;
        int i = 0, j = col - 1;
        while (i >= 0 && i < row && j >= 0 && j < col) {
            if (matrix[i][j] > target) {
                j--;
            } else if (matrix[i][j] < target) {
                i++;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * 搜索旋转排序数组
     * 没做出来，现在反倒做不出来了
     * 核心是想到根据rt和mid的关系来
     * 意识到左边侧的最小值一定大于右边最大值
     */
    public int search(int[] nums, int target) {
        int len = nums.length;
        int turnPoint = findTurnPoint(nums);
        int r1 = binarySearch(nums, 0, turnPoint - 1, target);
        int r2 = binarySearch(nums, turnPoint, len - 1, target);
        if (r1 == -1 && r2 == -1) {
            return -1;
        }
        return r1 == -1 ? r2 : r1;
    }

    private int findTurnPoint(int[] nums) {
        int lt = 0, rt = nums.length - 1;
        while (lt <= rt) {
            int mid = (lt + rt) >> 1;
            if (nums[mid] > nums[rt]) {
                lt = mid + 1;
            } else if (nums[mid] < nums[rt]) {
                rt = mid;
            } else {
                return mid;
            }
        }
        return -1;
    }

    private int binarySearch(int[] nums, int start, int end, int target) {
        int lt = start, rt = end;
        while (lt <= rt) {
            int mid = lt + (rt - lt) / 2;
            if (nums[mid] < target) {
                lt = mid + 1;
            } else if (nums[mid] > target) {
                rt = mid - 1;
            } else {
                return mid;
            }
        }
        return -1;
    }

    /**
     * 在排序数组中查找元素的第一个和最后一个位置
     */
    public int[] searchRange(int[] nums, int target) {
        if (!contain(nums, target)) {
            return new int[] { -1, -1 };
        }
        int lt = searchFirstLarger(nums, target);
        int rt = searchFirstLarger(nums, target + 1);
        if (lt == -1) {
            return new int[] { -1, -1 };
        }
        return new int[] { lt, rt - 1 };
    }

    private boolean contain(int[] nums, int target) {
        int lt = 0, rt = nums.length - 1;
        while (lt <= rt) {
            int mid = (rt + lt) / 2;
            if (nums[mid] > target) {
                rt = mid - 1;
            } else if (nums[mid] < target) {
                lt = mid + 1;
            } else {
                return true;
            }
        }
        return false;
    }

    private int searchFirstLarger(int[] nums, int target) {
        int lt = 0, rt = nums.length - 1;
        while (lt <= rt) {
            int mid = (rt + lt) / 2;
            if (nums[mid] > target) {
                rt = mid - 1;
            } else if (nums[mid] < target) {
                lt = mid + 1;
            } else {
                rt = mid - 1;
            }
        }
        return lt;
    }

    /**
     * 寻找旋转排序数组中的最小值
     */
    public int findMin(int[] nums) {
        int lt = 0, rt = nums.length - 1;
        while (lt <= rt) {
            int mid = (lt + rt) >> 1;
            if (nums[mid] < nums[rt]) {
                rt = mid;
            } else if (nums[mid] > nums[rt]) {
                lt = mid + 1;
            } else {
                return nums[mid];
            }
        }
        return -1;
    }

    /**
     * 寻找两个正序数组的中位数
     * 感觉依旧还是没能解决问题
     * 就是到底为什么这么写，这么写的底层逻辑没弄清楚，就不好记忆
     */
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int l1 = nums1.length, l2 = nums2.length;
        if (l1 < l2) {
            return findMedianSortedArrays(nums2, nums1);
        }
        // 保证l2是更小的
        int lt = 0, rt = l2;
        // 0001111
        // 0011
        int total = (l1 + l2) >> 1;
        while (lt < rt) {
            int mid = (lt + rt) >> 1;
            int idx2 = mid - 1;
            int idx1 = total - mid - 1;
            if (nums1[idx1] > nums2[idx2 + 1]) {
                lt = mid + 1;
            } else {
                rt = mid;
            }
        }
        // lt == rt
        int take2 = lt;
        int take1 = total - take2;
        if ((l1 + l2) % 2 == 0) {
            int n1 = take1 == 0 ? Integer.MIN_VALUE : nums1[take1 - 1];
            int n2 = take2 == 0 ? Integer.MIN_VALUE : nums2[take2 - 1];
            int n3 = take1 == nums1.length ? Integer.MAX_VALUE : nums1[take1];
            int n4 = take2 == nums2.length ? Integer.MAX_VALUE : nums2[take2];
            return (Math.max(n1, n2) + Math.min(n3, n4)) / 2.0;
        } else {
            int n1 = take1 == nums1.length ? Integer.MAX_VALUE : nums1[take1];
            int n2 = take2 == nums2.length ? Integer.MAX_VALUE : nums2[take2];
            return Math.min(n1, n2);
        }
    }

    /**
     * 数组中第K个最大元素
     * 快速选择算法会超时
     * 还是得用堆来实现
     */
    public int findKthLargest(int[] nums, int k) {
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>();
        for (int num : nums) {
            if (priorityQueue.size() < k) {
                priorityQueue.offer(num);
                continue;
            }
            if (num > priorityQueue.peek()) {
                priorityQueue.poll();
                priorityQueue.offer(num);
            }
        }
        return priorityQueue.peek();
    }

    private int quickSorter(int[] nums, int start, int end) {
        int slow = start, fast = start;
        while (fast < end) {
            if (nums[fast] < nums[end]) {
                swap(nums, slow, fast);
                slow++;
            }
            fast++;
        }
        swap(nums, slow, end);
        return slow;
    }

    public int findKthLargestTimeExceed(int[] nums, int k) {
        int lt = 0, rt = nums.length - 1;
        k = nums.length - k;
        while (lt <= rt) {
            int idx = quickSorter(nums, lt, rt);
            if (idx + 1 < k) {
                lt = idx + 1;
            } else if (idx + 1 > k) {
                rt = idx - 1;
            } else {
                return nums[idx];
            }
        }
        return nums[lt];
    }

    /**
     * 数据流的中位数
     */
    class MedianFinder {
        
    }
}
