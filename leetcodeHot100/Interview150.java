package leetcodeHot100;

import java.security.InvalidKeyException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import leetcodeHot100.Node;

public class Interview150 {

    /**
     * 二叉搜索树迭代器
     */
    class BSTIterator {

        private int idx;

        private List<Integer> list;

        public BSTIterator(TreeNode root) {
            this.list = new ArrayList<>();
            this.idx = -1;
            inorder(root);
        }

        private class TreeNode {
            TreeNode lt;
            TreeNode rt;
            int val;
        }

        private void inorder(TreeNode node) {
            if (node == null) {
                return;
            }
            inorder(node.lt);
            list.add(node.val);
            inorder(node.rt);
        }

        public int next() {
            idx++;
            return list.get(idx);
        }

        public boolean hasNext() {
            return idx + 1 < list.size();
        }
    }

    /**
     * 只出现一次的数字II
     * 没做出来
     * 将每个数想象成32位的二进制，
     * 对于每一位的二进制的1和0累加起来必然是3N或者3N+1，
     * 为3N代表目标值在这一位没贡献，3N+1代表目标值在这一位有贡献(=1)，
     * 然后将所有有贡献的位|起来就是结果
     */
    public int singleNumber(int[] nums) {
        int ret = 0;
        for (int i = 0; i < 32; i++) {
            int mask = 1 << i;
            int cnt = 0;
            for (int j = 0; j < nums.length; j++) {
                if ((nums[j] & mask) != 0) {
                    cnt++;
                }
            }
            if (cnt % 3 != 0) {
                ret |= mask;
            }
        }
        return ret;
    }

    /**
     * 合并两个有序数组
     */
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int[] t = new int[m];
        for (int i = 0; i < m; i++) {
            t[i] = nums1[i];
        }
        int idx = 0;
        int idx1 = 0, idx2 = 0;
        while (idx1 < m || idx2 < n) {
            int n1 = idx1 < m ? t[idx1] : Integer.MAX_VALUE;
            int n2 = idx2 < n ? nums2[idx2] : Integer.MAX_VALUE;
            if (n1 < n2) {
                nums1[idx] = n1;
                idx1++;
            } else {
                nums1[idx] = n2;
                idx2++;
            }
            idx++;
        }
    }

    /**
     * 合并两个有序数组
     * 逆向双指针
     */
    public void mergeWithReverseDoublePointer(int[] nums1, int m, int[] nums2, int n) {
        int ptr1 = m - 1, ptr2 = n - 1;
        int ptr = m + n - 1;
        while (ptr1 >= 0 || ptr2 >= 0) {
            int num1 = ptr1 >= 0 ? nums1[ptr1] : Integer.MIN_VALUE;
            int num2 = ptr2 >= 0 ? nums2[ptr2] : Integer.MIN_VALUE;
            if (num1 > num2) {
                nums1[ptr--] = num1;
                ptr1--;
            } else {
                nums1[ptr--] = num2;
                ptr2--;
            }
        }
    }

    /**
     * 罗马数字转整数
     * 确实从后往前的顺序会顺利一点
     * 也更快一点
     */
    public int romanToInt(String s) {
        Map<Character, Integer> map = new HashMap<>(8);
        map.put('I', 1);
        map.put('V', 5);
        map.put('X', 10);
        map.put('L', 50);
        map.put('C', 100);
        map.put('D', 500);
        map.put('M', 1000);
        int len = s.length();
        int sum = 0;
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            if (i < len - 1 && map.get(s.charAt(i + 1)) > map.get(c)) {
                int val = map.get(c);
                int last = map.get(s.charAt(i + 1));
                sum += last - val;
                i++;
                continue;
            } else {
                sum += map.get(c);
            }
        }
        return sum;
    }

    /**
     * 整数转罗马数字
     * 这个找对了方法就很简洁，方法不对则很复杂
     */
    public String intToRoman(int num) {
        Map<Integer, String> map = new HashMap<>();
        int[] degree = new int[] { 1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1 };
        map.put(1000, "M");
        map.put(900, "CM");
        map.put(500, "D");
        map.put(400, "CD");
        map.put(100, "C");
        map.put(90, "XC");
        map.put(50, "L");
        map.put(40, "XL");
        map.put(10, "X");
        map.put(9, "IX");
        map.put(5, "V");
        map.put(4, "IV");
        map.put(1, "I");
        StringBuilder sb = new StringBuilder();
        while (num > 0) {
            int idx = 0;
            while (num < degree[idx]) {
                idx++;
            }
            num -= degree[idx];
            sb.append(map.get(degree[idx]));
        }
        return sb.toString();
    }

    /**
     * 删除有序数组中的重复项
     */
    public int removeDuplicates(int[] nums) {
        int slow = 1;
        int fast = 1;
        while (fast < nums.length) {
            if (nums[fast] != nums[fast - 1]) {
                nums[slow] = nums[fast];
                slow++;
            }
            fast++;
        }
        return slow;
    }

    /**
     * 删除有序数组中的重复项II
     */
    public int removeDuplicatesII(int[] nums) {
        int slow = 1;
        int fast = 1;
        int count = 1;
        Integer last = nums[0];
        while (fast < nums.length) {
            if (last == nums[fast]) {
                count++;
            } else {
                last = nums[fast];
                count = 1;
            }
            if (count < 3) {
                swap(nums, slow++, fast);
            }
            fast++;
        }
        return slow;
    }

    private void swap(int[] nums, int a, int b) {
        int t = nums[a];
        nums[a] = nums[b];
        nums[b] = t;
    }

    /**
     * 最后一个单词的长度
     */
    public int lengthOfLastWord(String s) {
        s = s.trim();
        int len = s.length();
        int fast = 0, slow = -1;
        while (fast < len) {
            if (s.charAt(fast) == ' ') {
                slow = fast;
            }
            fast++;
        }
        return fast - slow - 1;
    }

    /**
     * 最长公共前缀
     */
    public String longestCommonPrefix(String[] strs) {
        int idx = 0;
        String template = strs[0];
        while (idx < template.length()) {
            char expect = template.charAt(idx);
            for (int i = 1; i < strs.length; i++) {
                String s = strs[i];
                if (idx >= s.length() || s.charAt(idx) != expect) {
                    return template.substring(0, idx);
                }
            }
            idx++;
        }
        return template;
    }

    /**
     * Z字形变换
     */
    public String convert(String s, int numRows) {
        if (numRows == 1) {
            return s;
        }
        int len = s.length();
        int[] row = new int[len];
        int rowIndex = 1;
        boolean turnUp = true;
        for (int i = 0; i < len; i++) {
            row[i] = rowIndex;
            if (rowIndex == numRows) {
                turnUp = false;
            } else if (rowIndex == 1) {
                turnUp = true;
            }
            if (turnUp) {
                rowIndex++;
            } else {
                rowIndex--;
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= numRows; i++) {
            for (int j = 0; j < len; j++) {
                if (row[j] == i) {
                    sb.append(s.charAt(j));
                }
            }
        }
        return sb.toString();
    }

    /**
     * O(1)时间插入、删除和获取随机元素
     * 这题的核心是想到一个方法利用随机函数生成下表获取一个元素
     * 所以不仅仅需要map，还需要list
     */
    class RandomizedSet {

        List<Integer> list;

        Map<Integer, Integer> map;

        Random random;

        public RandomizedSet() {
            this.random = new Random();
            this.list = new ArrayList<>();
            this.map = new HashMap<>();
        }

        public boolean insert(int val) {
            if (map.containsKey(val)) {
                return false;
            }
            list.add(val);
            map.put(val, list.size() - 1);
            return true;
        }

        public boolean remove(int val) {
            if (!map.containsKey(val)) {
                return false;
            }
            int idx = map.get(val);
            int lastVal = list.get(list.size() - 1);
            list.set(idx, lastVal);
            map.put(lastVal, idx);
            map.remove(val);
            list.remove(list.size() - 1);
            return true;
        }

        public int getRandom() {
            int idx = random.nextInt(list.size());
            return list.get(idx);
        }
    }

    /**
     * 蚂蚁笔试：
     * 计算公式，最终只能过20%
     */
    public static int calcV3(int[] nums, int n) {
        int res = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= i; j++) {
                res += nums[i] / nums[j];
                if (i != j) {
                    res += nums[j] / nums[i];
                }
            }
        }
        return res;
    }

    /**
     * 蚂蚁笔试
     * 字符串比对
     */
    public static void printSByT(String s, String t, int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            char sChar = s.charAt(i);
            char tChar = t.charAt(i);
            if ('A' <= sChar && sChar <= 'Z') {
                sb.append(toUpperCase(tChar));
            } else if ('a' <= sChar && sChar <= 'z') {
                sb.append(toLowerCase(tChar));
            } else if ('0' <= sChar && sChar <= '9') {
                sb.append("" + (int) tChar);
            } else {
                sb.append('_');
            }
        }
        System.out.println(sb.toString());
    }

    private static char toUpperCase(char c) {
        if ('a' <= c && c <= 'z') {
            return (char) (c - 32);
        }
        return c;
    }

    private static char toLowerCase(char c) {
        if ('A' <= c && c <= 'Z') {
            return (char) (c + 32);
        }
        return c;
    }

    /**
     * 存在重复元素II
     */
    public boolean containsNearbyDuplicate(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(nums[i])) {
                int j = map.get(nums[i]);
                if (Math.abs(i - j) <= k && i != j) {
                    return true;
                }
            }
            map.put(nums[i], i);
        }
        return false;
    }

    /**
     * 验证回文串
     */
    public boolean isPalindrome(String s) {
        int len = s.length();
        int lt = 0, rt = len - 1;
        while (lt < len && !isChar(s.charAt(lt))) {
            lt++;
        }
        while (rt >= 0 && !(isChar(s.charAt(rt)))) {
            rt--;
        }
        while (lt < rt) {
            if (!(rt >= 0 && lt < len) || !equalsIngnoreCase(s.charAt(rt), s.charAt(lt))) {
                return false;
            }
            lt++;
            while (lt < len && !isChar(s.charAt(lt))) {
                lt++;
            }
            rt--;
            while (rt >= 0 && !(isChar(s.charAt(rt)))) {
                rt--;
            }
        }
        return true;
    }

    private boolean isChar(char c) {
        return ('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z') || ('0' <= c && c <= '9');
    }

    private boolean equalsIngnoreCase(char c1, char c2) {
        if ('a' <= c1 && c1 <= 'z') {
            return c1 == c2 || (char) (c1 - 32) == c2;
        } else if ('A' <= c1 && c1 <= 'Z') {
            return c1 == c2 || (char) (c1 + 32) == c2;
        }
        return c1 == c2;
    }

    /**
     * 和为K的子数组
     */
    public int subarraySum(int[] nums, int k) {
        int len = nums.length;
        int[] prefix = new int[len];
        int sum = 0;
        for (int i = 0; i < len; i++) {
            sum += nums[i];
            prefix[i] = sum;
        }
        // 1 2 3
        int count = 0;
        Map<Integer, Integer> sumCount = new HashMap<>();
        sumCount.put(0, 1);
        for (int i = 0; i < len; i++) {
            count += sumCount.getOrDefault(prefix[i] - k, 0);
            sumCount.put(prefix[i], sumCount.getOrDefault(prefix[i], 0) + 1);
        }
        return count;
    }

    /**
     * 同构字符串
     */
    public boolean isIsomorphic(String s, String t) {
        Map<Character, List<Integer>> sCountMap = new HashMap<>();
        Map<Character, List<Integer>> tCountMap = new HashMap<>();
        char[] sChs = s.toCharArray();
        char[] tChs = t.toCharArray();
        for (int i = 0; i < sChs.length; i++) {
            char c = sChs[i];
            List<Integer> list = sCountMap.getOrDefault(c, new ArrayList<>());
            list.add(i);
            sCountMap.put(c, list);
        }
        for (int i = 0; i < tChs.length; i++) {
            char c = tChs[i];
            List<Integer> list = tCountMap.getOrDefault(c, new ArrayList<>());
            list.add(i);
            tCountMap.put(c, list);
        }
        int valid = 0;
        int target = Math.max(sCountMap.size(), tCountMap.size());
        int idx = 0;
        Map<Character, Boolean> used = new HashMap<>();
        while (idx < sChs.length && valid < target) {
            List<Integer> list1 = sCountMap.get(sChs[idx]);
            List<Integer> list2 = tCountMap.get(tChs[idx]);
            if (!used.getOrDefault(sChs[idx], false)) {
                if (!listEqual(list1, list2)) {
                    return false;
                }
                valid++;
                used.put(sChs[idx], true);
            }
            idx++;
        }
        return valid == target;
    }

    private boolean listEqual(List<Integer> list1, List<Integer> list2) {
        if (list1.size() != list2.size()) {
            return false;
        }
        int len = list1.size();
        for (int i = 0; i < len; i++) {
            if (!list1.get(i).equals(list2.get(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 单词规律
     */
    public boolean wordPattern(String pattern, String s) {
        // 一个字母对应到一个单词
        String[] words = s.split(" ");
        Map<Character, String> c2WordMap = new HashMap<>();
        Map<String, Character> word2CharMap = new HashMap<>();
        if (pattern.length() != words.length) {
            return false;
        }
        int len = words.length;
        for (int i = 0; i < len; i++) {
            char c = pattern.charAt(i);
            String word = words[i];
            String mappedWord = c2WordMap.get(c);
            Character mappedChar = word2CharMap.get(word);
            if (mappedWord != null && !mappedWord.equals(word)) {
                return false;
            }
            if (mappedChar != null && c != mappedChar) {
                return false;
            }
            c2WordMap.put(c, word);
            word2CharMap.put(word, c);
        }
        return true;
    }

    /**
     * 插入区间
     * 没做出来
     * 暴露出来几个问题，之前的合并区间你的方法论不太合适
     * 而且你的二分又不熟悉了
     */
    public int[][] insert(int[][] intervals, int[] newInterval) {
        List<int[]> result = new ArrayList<>();
        int i = 0;
        while (i < intervals.length && intervals[i][1] < newInterval[0]) {
            result.add(intervals[i]);
            i++;
        }
        // 这部分的逻辑处理很巧妙
        int start = newInterval[0], end = newInterval[1];
        while (i < intervals.length && intervals[i][0] <= end) {
            start = Math.min(start, intervals[i][0]);
            end = Math.max(end, intervals[i][1]);
            i++;
        }
        result.add(new int[] { start, end });
        while (i < intervals.length) {
            result.add(intervals[i]);
            i++;
        }
        return result.toArray(new int[result.size()][]);
    }

    /**
     * 最长递增子序列
     */
    public int lengthOfLIS(int[] nums) {
        // 以第i个字符结尾的最长递增子序列长度
        int[] dp = new int[nums.length];
        int max = 1;
        for (int i = 0; i < nums.length; i++) {
            dp[i] = 1;
            for (int j = 0; j <= i; j++) {
                dp[i] = (nums[i] > nums[j]) ? Math.max(dp[j] + 1, dp[i]) : dp[i];
            }
            if (dp[i] > max) {
                max = dp[i];
            }
        }
        return max;
    }

    /**
     * 最长递增子序列
     * 快速解法
     */
    public int lengthOfLISTwoEx(int[] nums) {
        List<Integer> list = new ArrayList<>();
        for (int num : nums) {
            if (list.isEmpty()) {
                list.add(num);
                continue;
            }
            if (list.get(list.size() - 1) < num) {
                list.add(num);
            } else {
                list.set(findPos(list, num), num);
            }
        }
        return list.size();
    }

    private int findPos(List<Integer> list, int num) {
        int lt = 0;
        int rt = list.size() - 1;
        // 1 3 5
        // 4
        while (lt <= rt) {
            int mid = (lt + rt) >> 1;
            int val = list.get(mid);
            if (val > num) {
                rt = mid - 1;
            } else if (val < num) {
                lt = mid + 1;
            } else {
                return mid;
            }
        }
        return lt;
    }

    /**
     * 下一个最大元素III
     * 这题感觉是hot100: 下一个排列的变种
     */
    public int nextGreaterElement(int n) {
        List<Integer> digits = new ArrayList<>();
        int num = n;
        while (num > 0) {
            digits.add(0, num % 10);
            num /= 10;
        }
        List<Integer> res = new ArrayList<>();
        dfs(digits, new ArrayList<>(), res, new boolean[digits.size()], n);
        return res.isEmpty() ? -1 : res.get(0);
    }

    private void dfs(List<Integer> digits, List<Integer> temp, List<Integer> res, boolean[] used, int originalNum) {
        if (temp.size() == digits.size()) {
            if (largerThan(temp, digits) > 0) { // 只考虑比原数大的情况
                int num = toNumber(temp);
                if (num > originalNum && (res.isEmpty() || num < res.get(0))) {
                    res.clear();
                    res.add(num); // 只存储最小的符合条件的数
                }
            }
            return;
        }
        for (int i = 0; i < digits.size(); i++) {
            if (used[i])
                continue;
            used[i] = true;
            temp.add(digits.get(i));
            dfs(digits, temp, res, used, originalNum);
            temp.remove(temp.size() - 1);
            used[i] = false;
        }
    }

    private int toNumber(List<Integer> numList) {
        long num = 0;
        for (int digit : numList) {
            num = num * 10 + digit;
            if (num > Integer.MAX_VALUE)
                return -1; // 防止溢出
        }
        return (int) num;
    }

    private int largerThan(List<Integer> num1, List<Integer> num2) {
        for (int i = 0; i < num1.size(); i++) {
            if (!num1.get(i).equals(num2.get(i))) {
                return num1.get(i) - num2.get(i);
            }
        }
        return 0;
    }

    /**
     * 下一个排列
     * 也是独立做出来了
     */
    public void nextPermutation(int[] nums) {
        // 1 2 3
        // i
        // 1 3 2
        // 从后往前找第一个降序对
        int len = nums.length;
        int idx = len - 1;
        while (idx >= 1 && nums[idx] <= nums[idx - 1]) {
            idx--;
        }
        if (idx == 0) {
            Arrays.sort(nums);
        } else {
            // 从后往前找到第一个降序对较小值大的
            // 此处可以优化为二分查找
            int idx1 = idx - 1;
            int idx2 = len - 1;
            while (idx2 >= idx && nums[idx2] <= nums[idx1]) {
                idx2--;
            }
            swap(nums, idx1, idx2);
            // 这里也可以直接reverse
            Arrays.sort(nums, idx, len);
        }
    }

    public void nextPermutationBetter(int[] nums) {
        // 1 2 3
        // i
        // 1 3 2
        // 从后往前找第一个降序对
        int len = nums.length;
        int idx = len - 1;
        while (idx >= 1 && nums[idx] <= nums[idx - 1]) {
            idx--;
        }
        if (idx == 0) {
            reverse(nums, 0, len - 1);
        } else {
            // 从后往前找到第一个降序对较小值大的
            int idx1 = idx - 1;
            int rt = len - 1, lt = idx;
            // 1 3 2
            //
            while (lt < rt) {
                int mid = (lt + rt) >> 1;
                if (nums[mid] > nums[idx1]) {
                    lt = mid + 1;
                } else {
                    rt = mid;
                }
            }
            swap(nums, idx1, lt);
            // 这里也可以直接reverse
            reverse(nums, idx, len - 1);
        }
    }

    private void reverse(int[] nums, int start, int end) {
        while (start < end) {
            swap(nums, start, end);
            start++;
            end--;
        }
    }

    /**
     * 比较版本号
     */
    public int compareVersion(String version1, String version2) {
        String[] v1 = version1.split("\\.");
        String[] v2 = version2.split("\\.");
        int idx = 0;
        while (idx < v1.length || idx < v2.length) {
            int s1 = idx < v1.length ? parse(v1[idx]) : 0;
            int s2 = idx < v2.length ? parse(v2[idx]) : 0;
            if (s1 != s2) {
                return s1 > s2 ? 1 : -1;
            }
            idx++;
        }
        return 0;
    }

    private int parse(String s) {
        int res = 0;
        int pow = 1;
        for (int i = s.length() - 1; i >= 0; i--) {
            char c = s.charAt(i);
            if ((c >= '0' && c <= '9')) {
                res += pow * (c - '0');
                pow *= 10;
            }
        }
        return res;
    }

    private static final String[] DIGITS = { "零", "一", "二", "三", "四", "五", "六", "七", "八", "九" };

    private static final String[] UNITS = { "", "万", "亿", "兆" };

    /**
     * 阿拉伯数字 -> 中文数字
     * 要意识到万、亿的特殊性
     * 需要注意这里需要一个零标识位
     * 核心就是几点：万亿兆为一组，用零到九处理一组以内的个十百千，注意needZero和节间零的处理
     */
    public static String numberToChinese(long num) {
        if (num == 0) {
            return DIGITS[0];
        }
        String numStr = Long.toString(num);
        int len = numStr.length();
        List<Integer> sections = new ArrayList<>();
        for (int i = len; i > 0; i -= 4) {
            int start = Math.max(i - 4, 0);
            String sectionStr = numStr.substring(start, i);
            sections.add(Integer.parseInt(sectionStr));
        }
        Collections.reverse(sections);
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < sections.size(); i++) {
            int sectionNum = sections.get(i);
            String converted = convertSection(sectionNum);
            if (!converted.isEmpty()) {
                int unitIndex = sections.size() - 1 - i;
                result.append(converted).append(UNITS[unitIndex]);
                // 处理节间零
                if (i < sections.size() - 1 && sections.get(i + 1) > 0 && sections.get(i + 1) < 1000) {
                    result.append("零");
                }
            }
        }
        return result.toString();
    }

    private static String convertSection(int num) {
        if (num == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        boolean needZero = false;
        // 千位
        int qian = num / 1000;
        num %= 1000;
        if (qian > 0) {
            sb.append(DIGITS[qian]).append("千");
            needZero = false;
        }
        // 百位
        int bai = num / 100;
        num %= 100;
        if (bai > 0) {
            if (needZero) {
                sb.append("零");
            }
            sb.append(DIGITS[bai]).append("百");
            needZero = false;
        } else {
            needZero |= sb.length() > 0;
        }
        // 十位
        int shi = num / 10;
        num %= 10;
        if (shi > 0) {
            if (needZero) {
                sb.append("零");
            }
            if (shi == 1 && sb.length() == 0) {
                sb.append("十");
            } else {
                sb.append(DIGITS[shi]).append("十");
            }
            needZero = false;
        } else {
            needZero |= sb.length() > 0;
        }
        // 个位
        if (num > 0) {
            if (needZero) {
                sb.append("零");
            }
            sb.append(DIGITS[num]);
        }
        return sb.toString();
    }

    /**
     * 无重复字符的最长子串
     */
    public int lengthOfLongestSubstring(String s) {
        Map<Character, Boolean> exist = new HashMap<>();
        int lt = 0, rt = 0;
        int len = s.length();
        int max = 0;
        while (rt < len) {
            char rc = s.charAt(rt);
            while (exist.getOrDefault(rc, false)) {
                char lc = s.charAt(lt);
                exist.put(lc, false);
                lt++;
            }
            exist.put(rc, true);
            if (max < rt - lt + 1) {
                max = rt - lt + 1;
            }
            rt++;
        }
        return max;
    }

    /**
     * 两个有序数组的中位数
     */
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        // 1 3
        // 2
        int l1 = nums1.length, l2 = nums2.length;
        if (l1 < l2) {
            return findMedianSortedArrays(nums2, nums1);
        }
        // l1 >= l2
        int total = l1 + l2;
        // 011 0011
        int frontNum = total / 2;
        int lt = 0;
        int rt = l2;
        while (lt < rt) {
            int mid = (lt + rt) / 2;
            int idx2 = mid - 1;
            int idx1 = frontNum - mid - 1;
            if (nums2[idx2 + 1] < nums1[idx1]) {
                lt = mid + 1;
            } else {
                rt = mid;
            }
        }
        if (total % 2 == 1) {
            int idx2 = lt - 1 + 1;
            int idx1 = frontNum - lt - 1;
            int num2 = idx2 >= l2 ? Integer.MAX_VALUE : nums2[idx2];
            int num1 = idx1 + 1 >= l1 ? Integer.MAX_VALUE : nums1[idx1 + 1];
            return num1 > num2 ? num2 : num1;
        } else {
            int idx2 = lt - 1;
            int idx1 = frontNum - lt - 1;
            int num2 = idx2 < 0 ? Integer.MIN_VALUE : nums2[idx2];
            int num1 = idx1 < 0 ? Integer.MIN_VALUE : nums1[idx1];
            int num3 = Math.max(num2, num1);
            int num4 = idx2 + 1 >= l2 ? Integer.MAX_VALUE : nums2[idx2 + 1];
            int num5 = idx1 + 1 >= l1 ? Integer.MAX_VALUE : nums1[idx1 + 1];
            int num6 = Math.min(num4, num5);
            return (num3 + num6) / 2.0;
        }
    }

    /**
     * 最小覆盖子串
     */
    public String minWindow(String s, String t) {
        int sLen = s.length();
        int tLen = t.length();
        int offset = -1, minLen = Integer.MAX_VALUE;
        Map<Character, Integer> need = new HashMap<>();
        Map<Character, Integer> have = new HashMap<>();
        for (int i = 0; i < tLen; i++) {
            char c = t.charAt(i);
            need.put(c, need.getOrDefault(c, 0) + 1);
        }
        int fast = 0, slow = 0;
        int valid = 0;
        while (fast < sLen) {
            char fc = s.charAt(fast);
            if (need.containsKey(fc)) {
                have.put(fc, have.getOrDefault(fc, 0) + 1);
                if (have.get(fc).equals(need.get(fc))) {
                    valid++;
                }
            }
            while (valid >= need.size()) {
                int len = fast - slow + 1;
                if (len < minLen) {
                    minLen = len;
                    offset = slow;
                }
                char sc = s.charAt(slow);
                if (need.containsKey(sc)) {
                    if (have.get(sc).equals(need.get(sc))) {
                        valid--;
                    }
                    have.put(sc, have.get(sc) - 1);
                }
                slow++;
            }
            fast++;
        }
        return offset == -1 ? "" : s.substring(offset, offset + minLen);
    }

    /**
     * 两数之和II - 输入有序数组
     */
    public int[] twoSum(int[] numbers, int target) {
        int lt = 0, rt = numbers.length - 1;
        while (lt < rt) {
            int sum = numbers[lt] + numbers[rt];
            if (sum > target) {
                rt--;
            } else if (sum < target) {
                lt++;
            } else {
                return new int[] { lt + 1, rt + 1 };
            }
        }
        return new int[] { -1, -1 };
    }

    /**
     * 判断子序列
     */
    public boolean isSubsequence(String s, String t) {
        int sLen = s.length();
        if (sLen == 0) {
            return true;
        }
        int matchIdx = -1;
        for (char c : t.toCharArray()) {
            if (s.charAt(matchIdx + 1) == c) {
                matchIdx++;
                if (matchIdx == sLen - 1) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 基本计算器
     * 没做出来
     */
    public int calculate(String s) {
        Deque<Integer> stack = new ArrayDeque<>();
        int operand = 0, res = 0;
        int sign = 1; // 表示当前数值的正负
        for (char c : s.toCharArray()) {
            if (Character.isDigit(c)) {
                // 如果是数字，则构建完整的数字
                operand = 10 * operand + (c - '0');
            } else if (c == '+') {
                // 遇到+，将当前数字加入结果，并重置符号为+
                res += sign * operand;
                sign = 1;
                operand = 0;
            } else if (c == '-') {
                // 遇到-，将当前数字加入结果，并重置符号为-
                res += sign * operand;
                sign = -1;
                operand = 0;
            } else if (c == '(') {
                // 将目前为止的结果和符号压入栈，然后重置result和sign
                stack.push(res);
                stack.push(sign);
                res = 0;
                sign = 1;
            } else if (c == ')') {
                // 结束子表达式，先将当前子表达式的结果加入总结果
                res += sign * operand;
                // 弹出之前保存的符号并应用
                res *= stack.pop();
                // 弹出之前保存的结果并加上
                res += stack.pop();
                operand = 0;
            }
        }
        return res + (sign * operand); // 加上最后一个数字
    }

    /**
     * 简化路径
     * 这题主要是被split有可能产生""给信息差了
     */
    public String simplifyPath(String path) {
        Deque<String> stack = new ArrayDeque<>();
        String[] paths = path.split("/");
        for (String dir : paths) {
            if (dir.equals(".") || dir.equals("")) {
                continue;
            }
            if (dir.equals("..")) {
                if (!stack.isEmpty()) {
                    stack.pop();
                }
            } else {
                stack.push(dir);
            }
        }
        StringBuilder sb = new StringBuilder();
        if (stack.isEmpty()) {
            return "/";
        }
        while (!stack.isEmpty()) {
            sb.insert(0, "/" + stack.pop());
        }
        return sb.toString();
    }

    /**
     * CSIG一面：ip二进制转十进制
     * 核心是想到这其实是个移位置（base256）的操作
     * 或者用boolean[32]模拟也行
     */
    public int convertIpToIntBestSolution(String ip) {
        String[] segaments = ip.split("\\.");
        int base = 1;
        int res = 0;
        for (int i = segaments.length - 1; i >= 0; i--) {
            res += base * Integer.parseInt(segaments[i]);
            base *= 256;
        }
        return res;
    }

    public int convertIpToIntWithBooleanArray(String ip) {
        boolean[] digits = new boolean[32];
        String[] segaments = ip.split("\\.");
        for (int i = 0; i < segaments.length; i++) {
            int base = 128;
            int num = Integer.parseInt(segaments[i]);
            for (int j = i * 8; j < (i + 1) * 8; j++) {
                if (num >= base) {
                    digits[j] = num >= base;
                    num -= base;
                }
                base /= 2;
            }
        }
        int res = 0, base = 1;
        for (int i = 31; i >= 0; i--) {
            res += digits[i] ? base : 0;
            base *= 2;
        }
        return res;
    }

    /**
     * CSIG一面：判断一个串是否是另一个串的出入栈的结果
     * 核心是维护一个s串的当前匹配位置，用一个栈把t串放入匹配
     */
    public boolean isStackOperationResult(String s, String t) {
        int sLen = s.length(), tLen = t.length();
        if (sLen != tLen) {
            return false;
        }
        int matchIdx = 0;
        Deque<Character> stack = new ArrayDeque<>();
        for (int i = 0; i < tLen; i++) {
            char c = t.charAt(i);
            stack.push(c);
            while (s.charAt(matchIdx) == stack.peek()) {
                stack.pop();
                matchIdx++;
            }
        }
        return matchIdx == sLen;
    }

    /**
     * 生命游戏
     */
    public void gameOfLife(int[][] board) {
        int row = board.length;
        int col = board[0].length;
        int[][] alive = new int[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (valid(board, i - 1, j)) {
                    alive[i][j]++;
                }
                if (valid(board, i - 1, j + 1)) {
                    alive[i][j]++;
                }
                if (valid(board, i - 1, j - 1)) {
                    alive[i][j]++;
                }
                if (valid(board, i, j + 1)) {
                    alive[i][j]++;
                }
                if (valid(board, i, j - 1)) {
                    alive[i][j]++;
                }
                if (valid(board, i + 1, j)) {
                    alive[i][j]++;
                }
                if (valid(board, i + 1, j + 1)) {
                    alive[i][j]++;
                }
                if (valid(board, i + 1, j - 1)) {
                    alive[i][j]++;
                }
            }
        }
        // 1 -> 2 ~ 3
        // 0 -> 3
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (board[i][j] == 1) {
                    if (alive[i][j] < 2 || alive[i][j] > 3) {
                        board[i][j] = 0;
                    }
                } else {
                    if (alive[i][j] == 3) {
                        board[i][j] = 1;
                    }
                }
            }
        }
    }

    private boolean valid(int[][] board, int rowIdx, int colIdx) {
        int row = board.length;
        int col = board[0].length;
        return (rowIdx >= 0 && rowIdx < row) && (colIdx >= 0 && colIdx < col) && (board[rowIdx][colIdx] == 1);
    }

    /**
     * 根据二叉树的中序遍历和先序遍历直接输出后序遍历
     */
    public int[] getPostTravsal(int[] preorder, int[] inorder) {
        int len = preorder.length;
        int[] postorder = new int[len];
        // 1
        // 2 3
        // 4 5 6 7
        // in: 4 2 5 1 6 3 7
        // pre:1 2 4 5 3 6 7
        // post: 4 5 2 6 7 3 1
        Map<Integer, Integer> inorderIdxMap = new HashMap<>();
        for (int i = 0; i < len; i++) {
            inorderIdxMap.put(inorder[i], i);
        }
        getRoot(preorder, inorderIdxMap, postorder, 0, len - 1);
        return postorder;
    }

    private int preorderIdx = 0;

    private int postorderIdx = 0;

    private void getRoot(int[] preorder, Map<Integer, Integer> inorderIdxMap, int[] postorder, int start, int end) {
        if (preorderIdx == preorder.length || end < start) {
            return;
        }
        int root = preorder[preorderIdx++];
        int rootIdx = inorderIdxMap.get(root);
        getRoot(preorder, inorderIdxMap, postorder, start, rootIdx - 1);
        getRoot(preorder, inorderIdxMap, postorder, rootIdx + 1, end);
        postorder[postorderIdx++] = root;
    }

    /**
     * 小于n的最大数
     * 给定一个数n,如23121;
     * 给定一组数字A如{2,4,9}求由A中元素组成的、小于n的最大数,
     * 如小于23121的最大数为22999
     */
    public int maxNumLessThanN(int n, int[] nums) {
        boolean hasLess = false;
        if (nums[0] > 0) {
            int[] newNums = new int[nums.length + 1];
            for (int i = 1; i < newNums.length; i++) {
                newNums[i] = nums[i - 1];
            }
            nums = newNums;
        }
        int res = 0;
        List<Integer> list = new ArrayList<>();
        String num = String.valueOf(n);
        for (int i = 0; i < num.length(); i++) {
            if (hasLess) {
                list.add(nums[nums.length - 1]);
                continue;
            }
            int digit = num.charAt(i) - '0';
            int idx = binarySearch(nums, digit);
            // nums[idx] >= digit
            if (nums[idx] > digit) {
                list.add(nums[idx - 1]);
                hasLess = true;
            } else {
                list.add(nums[idx]);
                if (idx == 0 && digit > 0) {
                    hasLess = true;
                }
            }
        }
        int base = 1;
        for (int i = list.size() - 1; i >= 0; i--) {
            res += base * list.get(i);
            base *= 10;
        }
        return res;
    }

    private int binarySearch(int[] nums, int target) {
        // 0 1 4 5 9
        // 0
        int lt = 0, rt = nums.length - 1;
        while (lt < rt) {
            int mid = (lt + rt) / 2;
            if (nums[mid] > target) {
                rt = mid - 1;
            } else if (nums[mid] < target) {
                lt = mid + 1;
            } else {
                return mid;
            }
        }
        return lt;
    }

    /**
     * 旋转链表
     */
    public ListNode rotateRight(ListNode head, int k) {
        if (head == null) {
            return head;
        }
        int len = 0;
        ListNode node = head;
        while (node != null) {
            node = node.next;
            len++;
        }
        k %= len;
        if (k == 0) {
            return head;
        }
        head = reverse(head)[0];
        ListNode dummy = new ListNode(0, head);
        node = head;
        int count = 0;
        ListNode last = dummy;
        while (count < k) {
            count++;
            last = node;
            node = node.next;
        }
        last.next = null;
        ListNode[] frontPart = reverse(head);
        ListNode[] backPart = reverse(node);
        frontPart[1].next = backPart[0];
        return frontPart[0];
    }

    private ListNode[] reverse(ListNode head) {
        ListNode last = null;
        ListNode node = head;
        while (node != null) {
            ListNode next = node.next;
            node.next = last;
            last = node;
            node = next;
        }
        return new ListNode[] { last, head };
    }

    /**
     * 分割链表
     */
    public ListNode partition(ListNode head, int x) {
        ListNode dummy = new ListNode(0, head);
        ListNode fast = head;
        ListNode slow = dummy;
        ListNode last = dummy;
        while (fast != null) {
            ListNode fastNext = fast.next;
            if (fast.val < x) {
                last.next = fast.next;
                ListNode slowNext = slow.next;
                slow.next = fast;
                fast.next = slowNext;
                slow = fast;
            }
            fast = fastNext;
            last = dummy.next;
            while (last.next != fast) {
                last = last.next;
            }
        }
        return dummy.next;
    }

    /**
     * 字符串转换整数（atoi）
     */
    public int myAtoi(String s) {
        int len = s.length();
        long res = 0;
        int sign = 1;
        boolean digitMatched = false, signMatched = false;
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            if (c == ' ') {
                if (digitMatched || signMatched) {
                    return (int) res * sign;
                }
                continue;
            }
            if (c == '+' || c == '-') {
                if (digitMatched || signMatched) {
                    return (int) res * sign;
                }
                signMatched = true;
                sign = c == '-' ? -1 : 1;
            } else if (Character.isDigit(c)) {
                digitMatched = true;
                res = res * 10 + (c - '0');
                if (res * sign >= Integer.MAX_VALUE || res * sign <= Integer.MIN_VALUE) {
                    return sign == 1 ? Integer.MAX_VALUE : Integer.MIN_VALUE;
                }
            } else {
                return (int) res * sign;
            }
        }
        return (int) res * sign;
    }

    /**
     * 相同的树
     */
    public boolean isSameTree(TreeNode n1, TreeNode n2) {
        if (n1 == null && n2 == null) {
            return true;
        } else if (n1 != null && n2 != null && n1.val == n2.val) {
            return isSameTree(n1.left, n2.left) && isSameTree(n1.right, n2.right);
        } else {
            return false;
        }
    }

    /**
     * 判断完全二叉树
     * 完全二叉树定义：除了最后一层外，其他层的节点都是满的，并且最后一层的节点都靠左排列。
     */
    public boolean isCompleteBinaryTree(TreeNode root) {
        if (root == null) {
            return true; // 空树被认为是完全二叉树
        }
        Deque<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);
        // 标志位，表示是否遇到了空节点
        boolean hasNull = false;
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            // 如果已经遇到过空节点，但当前节点不为空，则不是完全二叉树
            if (hasNull && node != null) {
                return false;
            }
            // 如果当前节点为空，设置标志位
            if (node == null) {
                hasNull = true;
            } else {
                // 否则将左右子节点加入队列（即使为空也加入）
                queue.offer(node.left);
                queue.offer(node.right);
            }
        }
        return true;
    }

    /**
     * 整型无序双向链表转BST
     * 不能建立树结构的，不能先排序的原地修改方式，双向链表的prev和next分别就指向左右子树
     * 方法就是模拟二叉树的插入过程？
     */
    public DListNode buildBSTByDLinkList(DListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        DListNode node = head.next;
        head.next.prev = null;
        head.next = null;
        while (node != null) {
            DListNode next = node.next;
            insert(head, node);
            node = next;
        }
        return head;
    }

    private void insert(DListNode root, DListNode node) {
        if (node.next != null) {
            node.next.prev = null;
        }
        node.next = null;
        DListNode insertPos = root;
        DListNode last = root;
        while (insertPos != null) {
            last = insertPos;
            if (insertPos.val > node.val) {
                insertPos = insertPos.prev;
            } else {
                insertPos = insertPos.next;
            }
        }
        if (last.val > node.val) {
            last.prev = node;
        } else {
            last.next = node;
        }
    }

    private Node lastNode;

    /**
     * 将二叉搜索树转化为排序的双向链表（排序的循环双向链表）
     */
    public Node treeToDoublyList(Node root) {
        if (root == null) {
            return null;
        }
        Node dummy = new Node();
        lastNode = dummy;
        dfs(root);
        Node head = dummy.right;
        Node tail = lastNode;
        head.left = tail;
        tail.right = head;
        return dummy.right;
    }

    private void dfs(Node node) {
        if (node == null) {
            return;
        }
        Node left = node.left;
        Node right = node.right;
        dfs(left);
        lastNode.right = node;
        node.left = lastNode;
        lastNode = node;
        dfs(right);
    }

    /**
     * 单词搜索 II
     * 没做出来
     * 核心是想到前缀树Trie（hot100），然后dfs扩散开去
     * Trie需要自己实现
     */
    public List<String> findWords(char[][] board, String[] words) {
        int row = board.length;
        int col = board[0].length;
        List<String> res = new ArrayList<>();
        Map<String, Boolean> contained = new HashMap<>();
        Trie trie = new Trie();
        for (String word : words) {
            trie.insert(word);
        }
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                char c = board[i][j];
                if (trie.root.nextMap.containsKey(c)) {
                    // 开始扩散
                    dfs(res, new boolean[row][col], contained, trie, board, i, j, trie.root);
                }
            }
        }
        return res;
    }

    private void dfs(List<String> res, boolean[][] used, Map<String, Boolean> contained, Trie trie, char[][] board,
            int rowIdx, int colIdx,
            Trie.TrieNode last) {
        int row = board.length;
        int col = board[0].length;
        if (!((rowIdx >= 0 && rowIdx < row) && (colIdx >= 0 && colIdx < col)) || used[rowIdx][colIdx]) {
            return;
        }
        char ch = board[rowIdx][colIdx];
        if (!last.nextMap.containsKey(ch)) {
            return;
        }
        Trie.TrieNode node = last.nextMap.get(ch);
        used[rowIdx][colIdx] = true;
        if (node.isEnd && !contained.getOrDefault(node.word, false)) {
            res.add(node.word);
            contained.put(node.word, true);
        }
        dfs(res, used, contained, trie, board, rowIdx + 1, colIdx, node);
        dfs(res, used, contained, trie, board, rowIdx - 1, colIdx, node);
        dfs(res, used, contained, trie, board, rowIdx, colIdx + 1, node);
        dfs(res, used, contained, trie, board, rowIdx, colIdx - 1, node);
        used[rowIdx][colIdx] = false;
    }

    public class Trie {

        public class TrieNode {
            char ch;
            Map<Character, TrieNode> nextMap;
            boolean isEnd;
            String word;

            public TrieNode(char c) {
                this.ch = c;
                this.nextMap = new HashMap<>();
                this.isEnd = false;
            }
        }

        private TrieNode root;

        private Map<String, Integer> map;

        public Trie() {
            this.root = new TrieNode('.');
            this.map = new HashMap<>();
        }

        public void insert(String word) {
            if (map.containsKey(word)) {
                map.put(word, map.get(word) + 1);
                return;
            }
            map.put(word, 1);
            int len = word.length();
            TrieNode node = root;
            for (int i = 0; i < len; i++) {
                char c = word.charAt(i);
                if (!node.nextMap.containsKey(c)) {
                    TrieNode newNode = new TrieNode(c);
                    node.nextMap.put(c, newNode);
                }
                node = node.nextMap.get(c);
            }
            node.isEnd = true;
            node.word = word;
            return;
        }

        public boolean search(String word) {
            return map.containsKey(word) && map.get(word) > 0;
        }

        public boolean startsWith(String prefix) {
            int len = prefix.length();
            TrieNode node = root;
            for (int i = 0; i < len; i++) {
                char c = prefix.charAt(i);
                if (!node.nextMap.containsKey(c)) {
                    return false;
                }
                node = node.nextMap.get(c);
            }
            return true;
        }
    }

    /**
     * 电梯调度
     */
    public class Elevator {

        private volatile int curFloor;

        private final int maxFloor;

        private final int minFloor;

        private volatile PriorityQueue<Integer> upQueue;

        private volatile PriorityQueue<Integer> downQueue;

        private boolean towardUp;

        private Lock lock;

        private Thread engine;

        private Condition noSelected;

        public Elevator(int maxFloor, int minFloor) {
            this.curFloor = minFloor;
            this.maxFloor = maxFloor;
            this.minFloor = minFloor;
            this.towardUp = true;
            this.lock = new ReentrantLock();
            this.noSelected = lock.newCondition();
            this.upQueue = new PriorityQueue<>((a, b) -> Integer.compare(a, b));
            this.downQueue = new PriorityQueue<>((a, b) -> Integer.compare(b, a));
            this.engine = new Thread(() -> {
                try {
                    work();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            this.engine.start();
        }

        public void selectFloor(int floor) {
            lock.lock();
            try {
                if (!(floor >= minFloor && floor <= maxFloor)) {
                    return;
                }
                if (curFloor < floor) {
                    upQueue.offer(floor);
                } else if (curFloor > floor) {
                    downQueue.offer(floor);
                } else {
                    arrival(floor);
                }
                if (!upQueue.isEmpty() || !downQueue.isEmpty()) {
                    noSelected.signal();
                }
            } finally {
                lock.unlock();
            }
        }

        private void work() throws InterruptedException {
            while (true) {
                lock.lock();
                try {
                    while (upQueue.isEmpty() && downQueue.isEmpty()) {
                        // await是等待并释放锁，sleep是持有等待
                        noSelected.await();
                    }
                    while (towardUp && !upQueue.isEmpty()) {
                        int floor = upQueue.poll();
                        arrival(floor);
                    }
                    towardUp = false;
                    while (!towardUp && !downQueue.isEmpty()) {
                        int floor = downQueue.poll();
                        arrival(floor);
                    }
                    towardUp = true;
                } finally {
                    lock.unlock();
                }
            }
        }

        private void arrival(int floor) {
            this.curFloor = floor;
            System.out.println("arrival floor: " + floor);
        }
    }

    /**
     * 设计线程池
     * 设计一个支持任务队列和并发控制的线程池，给出关键数据结构、接口定义和使用示例
     */
    public class MyThreadPool {

        private final int corePoolSize;

        private final int maxSize;

        private final int timeout;

        private final TimeUnit timeUnit;

        public final BlockingQueue<Runnable> blockingQueue;

        private final RejectHandle rejectHandle;

        private List<Thread> coreList = new ArrayList<>();

        private List<Thread> supportList = new ArrayList<>();

        // 实际还应该有一个线程工厂的参数，此处不作实现
        public MyThreadPool(int corePoolSize, int maxSize, int timeout, TimeUnit timeUnit,
                BlockingQueue<Runnable> blockingQueue, RejectHandle rejectHandle) {
            this.corePoolSize = corePoolSize;
            this.maxSize = maxSize;
            this.timeout = timeout;
            this.timeUnit = timeUnit;
            this.blockingQueue = blockingQueue;
            this.rejectHandle = rejectHandle;
        }

        public void execute(Runnable command) {
            if (coreList.size() < corePoolSize) {
                Thread thread = new CoreThread(command);
                thread.start();
                return;
            }
            if (blockingQueue.offer(command)) {
                return;
            }
            if (coreList.size() + supportList.size() < maxSize) {
                Thread thread = new SupportThread(command);
                thread.start();
                return;
            }
            if (!blockingQueue.offer(command)) {
                rejectHandle.reject(command, this);
            }
        }

        private class CoreThread extends Thread {

            private final Runnable firstTask;

            public CoreThread(Runnable firstTask) {
                this.firstTask = firstTask;
            }

            @Override
            public void run() {
                firstTask.run();
                while (true) {
                    try {
                        Runnable command = blockingQueue.take();
                        command.run();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        private class SupportThread extends Thread {
            private final Runnable firstTask;

            public SupportThread(Runnable firstTask) {
                this.firstTask = firstTask;
            }

            @Override
            public void run() {
                firstTask.run();
                while (true) {
                    try {
                        // 十分巧妙，这样子就让支持线程在空闲时间过后自动销毁了
                        Runnable command = blockingQueue.poll(timeout, timeUnit);
                        if (command == null) {
                            break;
                        }
                        command.run();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    public interface RejectHandle {
        public void reject(Runnable rejectCommand, MyThreadPool threadPool);
    }

    /**
     * IPO
     * 直接回溯是超时的
     * 看起来得翻译成dp试试，但是我后来尝试翻译成dp就发现，是错的
     */
    public int findMaximizedCapitalWithTimeExceed(int k, int w, int[] profits, int[] capital) {
        int money = w;
        int maxTranscationNum = k;
        dfs(0, maxTranscationNum, profits, capital, 0, money);
        return max;
    }

    private int max = 0;

    private void dfs(int transcationNum, int maxTranscationNum, int[] profits, int[] capital, int idx, int money) {
        if (transcationNum == maxTranscationNum || idx == profits.length) {
            max = Math.max(max, money);
            return;
        }
        if (money >= capital[idx]) {
            dfs(transcationNum + 1, maxTranscationNum, profits, capital, idx + 1, money + profits[idx]);
        }
        dfs(transcationNum, maxTranscationNum, profits, capital, idx + 1, money);
    }

    /**
     * IPO
     * 没做出来，这是正确的解法
     * 这个做法十分巧妙
     * 排序预处理 + 大顶堆（类似层序遍历的思想）
     */
    public int findMaximizedCapital(int maxTranscationNum, int money, int[] profits, int[] capital) {
        int projectNum = profits.length;
        int projectIdx = 0;
        int[][] capitalAndProfits = new int[projectNum][2];
        for (int i = 0; i < projectNum; ++i) {
            capitalAndProfits[i][0] = capital[i];
            capitalAndProfits[i][1] = profits[i];
        }
        Arrays.sort(capitalAndProfits, (a, b) -> a[0] - b[0]);
        // 大顶堆
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>((x, y) -> y - x);
        for (int i = 0; i < maxTranscationNum; ++i) {
            while (projectIdx < projectNum && capitalAndProfits[projectIdx][0] <= money) {
                priorityQueue.add(capitalAndProfits[projectIdx][1]);
                projectIdx++;
            }
            if (!priorityQueue.isEmpty()) {
                money += priorityQueue.poll();
            } else {
                break;
            }
        }
        return money;
    }

    /**
     * 被围绕的区域
     * 这个解法虽然能做，但是不是最优的思路
     * 可以考虑从边缘出发
     */
    public void solve(char[][] board) {
        int row = board.length;
        int col = board[0].length;
        boolean[][] visited = new boolean[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (board[i][j] == 'X' || visited[i][j]) {
                    continue;
                }
                List<int[]> list = new ArrayList<>();
                if (dfs(board, list, i, j, visited)) {
                    for (int[] idx : list) {
                        board[idx[0]][idx[1]] = 'X';
                    }
                }
            }
        }
        // ["O","O","O","O","X","X"],
        // ["O","O","O","O","O","O"],
        // ["O","X","O","X","O","O"],
        // ["O","X","O","O","X","O"],
        // ["O","X","O","X","O","O"],
        // ["O","X","O","O","O","O"]
    }

    private boolean dfs(char[][] board, List<int[]> list, int i, int j, boolean[][] visited) {
        int row = board.length;
        int col = board[0].length;
        if (!(i >= 0 && i < row && j >= 0 && j < col)) {
            return false;
        }
        if (board[i][j] == 'X' || visited[i][j]) {
            return true;
        }
        list.add(new int[] { i, j });
        visited[i][j] = true;
        boolean down = dfs(board, list, i + 1, j, visited);
        boolean up = dfs(board, list, i - 1, j, visited);
        boolean left = dfs(board, list, i, j - 1, visited);
        boolean right = dfs(board, list, i, j + 1, visited);
        return down && up && left && right;
    }

    public void solveBetterSolution(char[][] board) {
        int row = board.length;
        int col = board[0].length;
        for (int i = 0; i < row; i++) {
            dfs(board, i, 0);
            dfs(board, i, col - 1);
        }
        for (int i = 0; i < col; i++) {
            dfs(board, 0, i);
            dfs(board, row - 1, i);
        }
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (board[i][j] == 'A') {
                    board[i][j] = 'O';
                } else if (board[i][j] == 'O') {
                    board[i][j] = 'X';
                }
            }
        }
        // ["O","O","O","O","X","X"],
        // ["O","O","O","O","O","O"],
        // ["O","X","O","X","O","O"],
        // ["O","X","O","O","X","O"],
        // ["O","X","O","X","O","O"],
        // ["O","X","O","O","O","O"]
    }

    private void dfs(char[][] board, int i, int j) {
        int row = board.length;
        int col = board[0].length;
        if (!(i >= 0 && i < row && j >= 0 && j < col)) {
            return;
        }
        if (board[i][j] != 'O') {
            return;
        }
        board[i][j] = 'A';
        dfs(board, i + 1, j);
        dfs(board, i - 1, j);
        dfs(board, i, j - 1);
        dfs(board, i, j + 1);
    }

    class GraphNode {
        public int val;
        public List<GraphNode> neighbors;

        public GraphNode() {
            this.val = 0;
            neighbors = new ArrayList<GraphNode>();
        }

        public GraphNode(int val) {
            this.val = val;
            neighbors = new ArrayList<GraphNode>();
        }

        public GraphNode(int val, ArrayList<GraphNode> neighbors) {
            this.val = val;
            this.neighbors = neighbors;
        }
    }

    /**
     * 克隆图
     * BFS写法
     * 思路是先完成map的初始化，再去完成克隆节点的邻接节点的处理
     */
    public GraphNode cloneGraph(GraphNode node) {
        Map<GraphNode, GraphNode> map = new HashMap<>();
        Set<GraphNode> set = new HashSet<>();
        Deque<GraphNode> queue = new ArrayDeque<>();
        if (node == null) {
            return null;
        }
        // 完成map的初始化
        queue.offer(node);
        while (!queue.isEmpty()) {
            GraphNode originNode = queue.poll();
            if (!set.contains(originNode)) {
                for (GraphNode nextNode : originNode.neighbors) {
                    queue.offer(nextNode);
                }
                GraphNode newNode = new GraphNode(originNode.val);
                map.put(originNode, newNode);
                set.add(originNode);
            }
        }
        set.clear();
        queue.offer(node);
        while (!queue.isEmpty()) {
            GraphNode originNode = queue.poll();
            if (!set.contains(originNode)) {
                List<GraphNode> list = new ArrayList<>();
                for (GraphNode nextNode : originNode.neighbors) {
                    list.add(map.get(nextNode));
                    queue.offer(nextNode);
                }
                map.get(originNode).neighbors = list;
                set.add(originNode);
            }
        }
        return map.get(node);
    }

    /**
     * 克隆图
     * BFS解法
     * 这个写法更简洁，再一次遍历中处理克隆节点和克隆节点的邻接节点的处理
     */
    public GraphNode cloneGraphBFS(GraphNode node) {
        Map<GraphNode, GraphNode> map = new HashMap<>();
        Deque<GraphNode> queue = new ArrayDeque<>();
        if (node == null) {
            return null;
        }
        queue.offer(node);
        while (!queue.isEmpty()) {
            GraphNode originNode = queue.poll();
            map.putIfAbsent(node, new GraphNode(node.val));
            for (GraphNode nextNode : originNode.neighbors) {
                if (!map.containsKey(nextNode)) {
                    map.put(nextNode, new GraphNode(nextNode.val));
                    queue.offer(nextNode);
                }
                map.get(originNode).neighbors.add(map.get(nextNode));
            }
        }
        return map.get(node);
    }

    /**
     * 克隆图
     * DFS写法
     */
    public GraphNode cloneGraphDFS(GraphNode node) {
        Map<GraphNode, GraphNode> map = new HashMap<>();
        return dfs(map, node);
    }

    private GraphNode dfs(Map<GraphNode, GraphNode> map, GraphNode node) {
        if (node == null) {
            return null;
        }
        if (map.containsKey(node)) {
            return map.get(node);
        }
        GraphNode cloneNode = new GraphNode(node.val);
        map.put(node, cloneNode);
        for (GraphNode neighbor : node.neighbors) {
            cloneNode.neighbors.add(dfs(map, neighbor));
        }
        return cloneNode;
    }

    /**
     * 二叉树的锯齿形层序遍历
     */
    public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) {
            return res;
        }
        Deque<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);
        boolean reverse = false;
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
            if (reverse) {
                reverse(list);
            }
            res.add(list);
            reverse = !reverse;
        }
        return res;
    }

    private void reverse(List<Integer> list) {
        int lt = 0, rt = list.size() - 1;
        while (lt < rt) {
            int t = list.get(rt);
            list.set(rt, list.get(lt));
            list.set(lt, t);
            lt++;
            rt--;
        }
    }

    /**
     * 除法求值
     */
    public double[] calcEquation(List<List<String>> equations, double[] values, List<List<String>> queries) {
        Map<String, Map<String, Double>> map = new HashMap<>();
        for (int i = 0; i < values.length; i++) {
            double val = values[i];
            List<String> equation = equations.get(i);
            String start = equation.get(0);
            String end = equation.get(1);
            Map<String, Double> startToEndDIstanceMap = map.getOrDefault(start, new HashMap<>());
            startToEndDIstanceMap.put(end, val);
            map.put(start, startToEndDIstanceMap);
            Map<String, Double> endToStartDistanceMap = map.getOrDefault(end, new HashMap<>());
            endToStartDistanceMap.put(start, 1 / val);
            map.put(end, endToStartDistanceMap);
        }
        double[] res = new double[queries.size()];
        for (int i = 0; i < queries.size(); i++) {
            List<String> list = queries.get(i);
            String start = list.get(0);
            String end = list.get(1);
            res[i] = dfs(map, start, end, new HashSet<>());
        }
        return res;
    }

    private double dfs(Map<String, Map<String, Double>> map, String start, String end, Set<String> visited) {
        if ((!map.containsKey(start)) || visited.contains(start)) {
            return -1.0;
        }
        Map<String, Double> endToDistanceMap = map.get(start);
        if (endToDistanceMap.containsKey(end)) {
            return endToDistanceMap.get(end);
        }
        visited.add(start);
        for (String mid : endToDistanceMap.keySet()) {
            double distance = dfs(map, mid, end, visited);
            if (distance != -1.0) {
                return distance * endToDistanceMap.get(mid);
            }
        }
        visited.remove(start);
        return -1.0;
    }

    /**
     * 课程表II
     */
    public int[] findOrder(int numCourses, int[][] prerequisites) {
        int[] res = new int[numCourses];
        Map<Integer, List<Integer>> precourseMap = new HashMap<>();
        int[] inDegree = new int[numCourses];
        for (int[] rel : prerequisites) {
            inDegree[rel[0]]++;
            List<Integer> list = precourseMap.getOrDefault(rel[1], new ArrayList<>());
            list.add(rel[0]);
            precourseMap.put(rel[1], list);
        }
        Deque<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < numCourses; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }
        int idx = 0;
        while (!queue.isEmpty()) {
            int course = queue.poll();
            res[idx++] = course;
            for (int nextCourse : precourseMap.getOrDefault(course, new ArrayList<>())) {
                inDegree[nextCourse]--;
                if (inDegree[nextCourse] == 0) {
                    queue.offer(nextCourse);
                }
            }
        }
        return idx == numCourses ? res : new int[0];
    }

    /**
     * 蛇梯棋
     * 没做出来
     */
    public int snakesAndLadders(int[][] board) {
        int row = board.length;
        int col = board[0].length;
        int[] line = new int[row * col + 1];
        boolean asc = true;
        int idx = 1;
        for (int i = row - 1; i >= 0; i--) {
            if (asc) {
                for (int j = 0; j < col; j++) {
                    line[idx++] = board[i][j];
                }
                asc = false;
            } else {
                for (int j = col - 1; j >= 0; j--) {
                    line[idx++] = board[i][j];
                }
                asc = true;
            }
        }
        // BFS 初始化
        Deque<Integer> queue = new ArrayDeque<>();
        Set<Integer> visited = new HashSet<>();
        queue.offer(1); // 起点
        visited.add(1);
        int steps = 0;
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int position = queue.poll();
                if (position == row * col) {
                    return steps;
                }
                // 模拟掷骰子，尝试移动 1 到 6 步
                for (int increment = 1; increment <= 6; increment++) {
                    int next = position + increment;
                    // 超过棋盘范围，跳过
                    if (next > row * col) {
                        continue;
                    }
                    // 如果有梯子或蛇，跳转到目标位置
                    if (line[next] != -1) {
                        next = line[next];
                    }
                    // 如果该位置未访问过，加入队列
                    if (!visited.contains(next)) {
                        queue.offer(next);
                        visited.add(next);
                    }
                }
            }
            steps++; // 每一轮掷骰子算一步
        }
        return -1;
    }

    /**
     * 文本左右对齐
     * 没做出来，主要是单词间空格的均匀分配和最后一行考虑不周
     * 而且不知道String有方便的repeat API
     * 参考了答案的空格分配思路以后也是做出来了
     * 空格分配的思路这里就是取余，然后一个一个多加一格
     */
    public List<String> fullJustify(String[] words, int maxWidth) {
        StringBuilder sb = new StringBuilder();
        Deque<String> wordQueue = new ArrayDeque<>();
        Deque<String> spaceQueue = new ArrayDeque<>();
        List<String> res = new ArrayList<>();
        int wordLength = 0;
        for (String word : words) {
            if (wordLength + wordQueue.size() + word.length() > maxWidth) {
                int gapCount = wordQueue.size() == 1 ? 1 : wordQueue.size() - 1;
                String spaceUnit = " ".repeat((maxWidth - wordLength) / gapCount);
                int extraSpaceCount = (maxWidth - wordLength) % gapCount;
                sb.setLength(0);
                for (int i = 0; i < gapCount; i++) {
                    sb.append(spaceUnit);
                    if (i < extraSpaceCount) {
                        sb.append(' ');
                    }
                    spaceQueue.offer(sb.toString());
                    sb.setLength(0);
                }
                while (!wordQueue.isEmpty()) {
                    sb.append(wordQueue.poll()).append(spaceQueue.isEmpty() ? "" : spaceQueue.poll());
                }
                res.add(sb.toString());
                sb.setLength(0);
                wordLength = 0;
            }
            wordLength += word.length();
            wordQueue.offer(word);
        }
        sb.setLength(0);
        while (!wordQueue.isEmpty()) {
            String word = wordQueue.poll();
            sb.append(word).append(wordQueue.isEmpty() ? "" : " ");
        }
        while (sb.length() < maxWidth) {
            sb.append(' ');
        }
        res.add(sb.toString());
        return res;
    }

    /**
     * 文本左右对齐
     * 非常优雅的写法
     */
    public List<String> fullJustifyCorrectAns(String[] words, int maxWidth) {
        List<String> result = new ArrayList<>();
        List<String> currentLine = new ArrayList<>();
        int currentLength = 0;
        for (String word : words) {
            // 检查当前单词是否可以加入当前行
            if (currentLength + word.length() + currentLine.size() > maxWidth) {
                // 当前行已满，进行对齐处理
                if (currentLine.size() == 1) {
                    // 特殊情况：当前行只有一个单词，左对齐
                    String line = currentLine.get(0);
                    line += " ".repeat(maxWidth - line.length());
                    result.add(line);
                } else {
                    // 计算空格总数和每个间隙的空格数
                    int totalSpaces = maxWidth - currentLength;
                    int spaceCount = currentLine.size() - 1;
                    int spacePerGap = totalSpaces / spaceCount;
                    int extraSpaces = totalSpaces % spaceCount;
                    // 构造对齐后的行
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < currentLine.size(); i++) {
                        sb.append(currentLine.get(i));
                        if (i < currentLine.size() - 1) {
                            sb.append(" ".repeat(spacePerGap + (i < extraSpaces ? 1 : 0)));
                        }
                    }
                    result.add(sb.toString());
                }
                // 重置当前行
                currentLine.clear();
                currentLength = 0;
            }
            // 将当前单词加入当前行
            currentLine.add(word);
            currentLength += word.length();
        }
        // 处理最后一行（左对齐）
        StringBuilder lastLine = new StringBuilder();
        for (int i = 0; i < currentLine.size(); i++) {
            lastLine.append(currentLine.get(i));
            if (i < currentLine.size() - 1) {
                lastLine.append(" ");
            }
        }
        lastLine.append(" ".repeat(maxWidth - lastLine.length()));
        result.add(lastLine.toString());
        return result;
    }

    /**
     * 串联所有单词的子串
     * 超时了，或许可以改变思路为活动窗口，窗口一次移动一个wordLen
     * 经过我的修改，没有超时，击败68%
     */
    public List<Integer> findSubstring(String s, String[] words) {
        List<Integer> res = new ArrayList<>();
        Map<String, Integer> need = new HashMap<>();
        for (String word : words) {
            need.put(word, need.getOrDefault(word, 0) + 1);
        }
        int wordCount = words.length;
        int wordLen = words[0].length();
        int totalLen = wordLen * wordCount;
        for (int i = 0; i < wordLen; i++) {
            int valid = 0;
            Map<String, Integer> have = new HashMap<>();
            for (int start = i, wordStart = start; wordStart + wordLen - 1 < Math.min(
                    start + totalLen, s.length()); wordStart += wordLen) {
                int wordEnd = wordStart + wordLen - 1;
                String word = s.substring(wordStart, wordEnd + 1);
                have.put(word, have.getOrDefault(word, 0) + 1);
                if (need.containsKey(word) && have.get(word).equals(need.get(word))) {
                    valid++;
                }
            }
            if (valid == need.size()) {
                res.add(i);
            }
            for (int start = i + wordLen; start + totalLen - 1 < s.length(); start += wordLen) {
                String firstWord = s.substring(start - wordLen, start);
                String lastWord = s.substring(start + totalLen - wordLen, start + totalLen);
                if (need.containsKey(firstWord)) {
                    if (have.get(firstWord).equals(need.get(firstWord))) {
                        valid--;
                    }
                    have.put(firstWord, have.getOrDefault(firstWord, 0) - 1);
                }
                if (need.containsKey(lastWord)) {
                    have.put(lastWord, have.getOrDefault(lastWord, 0) + 1);
                    if (have.get(lastWord).equals(need.get(lastWord))) {
                        valid++;
                    }
                }
                if (valid == need.size()) {
                    res.add(start);
                }
            }
        }
        return res;
    }

    public List<Integer> findSubstringTimeExceed(String s, String[] words) {
        List<Integer> res = new ArrayList<>();
        Map<String, Integer> need = new HashMap<>();
        for (String word : words) {
            need.put(word, need.getOrDefault(word, 0) + 1);
        }
        int wordCount = words.length;
        int wordLen = words[0].length();
        int totalLen = wordLen * wordCount;
        Map<String, Integer> have = new HashMap<>();
        for (int start = 0; start + totalLen - 1 < s.length(); start++) {
            int valid = 0;
            for (int wordStart = start; wordStart + wordLen - 1 < start + totalLen; wordStart += wordLen) {
                int wordEnd = wordStart + wordLen - 1;
                String word = s.substring(wordStart, wordEnd + 1);
                if (!need.containsKey(word)) {
                    break;
                }
                have.put(word, have.getOrDefault(word, 0) + 1);
                if (have.get(word).equals(need.get(word))) {
                    valid++;
                }
            }
            have.clear();
            if (valid == need.size()) {
                res.add(start);
            }
        }
        return res;
    }

    /**
     * 有效的数独
     */
    public boolean isValidSudoku(char[][] board) {
        int row = board.length, col = board[0].length;
        boolean[][] groupExist = new boolean[9][10];
        boolean[][] rowExist = new boolean[9][10];
        boolean[][] colExist = new boolean[9][10];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (board[i][j] == '.') {
                    continue;
                }
                int num = board[i][j] - '0';
                int groupIdx = getGroupIdx(i, j);
                if (rowExist[i][num] || colExist[j][num] || groupExist[groupIdx][num]) {
                    return false;
                }
                rowExist[i][num] = true;
                colExist[j][num] = true;
                groupExist[groupIdx][num] = true;
            }
        }
        return true;
    }

    private int getGroupIdx(int rowIdx, int colIdx) {
        return (rowIdx / 3) * 3 + (colIdx / 3);
    }

    /**
     * 汇总区间
     */
    public List<String> summaryRanges(int[] nums) {
        StringBuilder sb = new StringBuilder();
        List<String> res = new ArrayList<>();
        Integer expected = null;
        boolean continued = false;
        for (int num : nums) {
            if (expected == null) {
                expected = num + 1;
                sb.append(num);
            } else if (expected == num) {
                expected++;
                if (!continued) {
                    sb.append("->");
                }
                continued = true;
            } else {
                if (continued) {
                    sb.append(expected - 1);
                }
                if (!sb.isEmpty()) {
                    res.add(sb.toString());
                    sb.setLength(0);
                }
                sb.append(num);
                expected = num + 1;
                continued = false;
            }
        }
        if (!sb.isEmpty()) {
            if (continued) {
                sb.append(expected - 1);
            }
            res.add(sb.toString());
            sb.setLength(0);
        }
        return res;
    }

    /**
     * 最小基因变化
     * 我终于自己做出来了
     * 核心就是想到计算两个字符串的差值，startGene每次只能到diff == 1的bankGene上，就这样走下去
     * dfs可解
     */
    public int minMutation(String startGene, String endGene, String[] bank) {
        return dfs(startGene, endGene, bank, new boolean[bank.length], 0);
    }

    private int dfs(String source, String target, String[] bank, boolean[] used, int count) {
        if (source.equals(target)) {
            return count;
        }
        boolean hasFound = false;
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < bank.length; i++) {
            if (used[i]) {
                continue;
            }
            int diff = diff(source, bank[i]);
            if (diff == 1) {
                used[i] = true;
                int res = dfs(bank[i], target, bank, used, count + 1);
                if (res != -1) {
                    min = Math.min(min, res);
                    hasFound = true;
                }
                used[i] = false;
            }
        }
        return hasFound ? min : -1;
    }

    private int diff(String s1, String s2) {
        if (s1.length() != s2.length()) {
            return -1;
        }
        int len = s1.length();
        int count = 0;
        for (int i = 0; i < len; i++) {
            if (s1.charAt(i) != s2.charAt(i)) {
                count++;
            }
        }
        return count;
    }

    /**
     * 单词接龙
     * 和最小基因变化几乎是一样的代码
     * 只是这里明确了建图，BFS解法
     * 无论是这里的BFS还是下面的DFS，判断两个节点有连接的依据都是两个单词差异为一个字符
     * 但是这么做的效率很低，可以考虑针对单词建图
     */
    public int ladderLength(String beginWord, String endWord, List<String> wordList) {
        Map<String, List<String>> map = new HashMap<>();
        int len = wordList.size();
        for (int i = 0; i < len; i++) {
            String word = wordList.get(i);
            if (diff(beginWord, word) == 1) {
                List<String> list = map.getOrDefault(beginWord, new ArrayList<>());
                list.add(word);
                map.put(beginWord, list);
            }
        }
        for (int i = 0; i < len; i++) {
            String word1 = wordList.get(i);
            List<String> list = map.getOrDefault(word1, new ArrayList<>());
            for (int j = 0; j < len; j++) {
                if (i == j) {
                    continue;
                }
                String word2 = wordList.get(j);
                if (diff(word1, word2) == 1) {
                    list.add(word2);
                }
            }
            map.put(word1, list);
        }
        Deque<String> queue = new ArrayDeque<>();
        queue.offer(beginWord);
        Set<String> set = new HashSet<>();
        int count = 1;
        while (!queue.isEmpty()) {
            count++;
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                String wordFrom = queue.poll();
                List<String> list = map.getOrDefault(wordFrom, new ArrayList<>());
                for (String wordTo : list) {
                    if (wordTo.equals(endWord)) {
                        return count;
                    }
                    if (!set.contains(wordTo)) {
                        queue.offer(wordTo);
                        set.add(wordTo);
                    }
                }
            }
        }
        return 0;
    }

    public int ladderLengthBFSFasterMaybe(String beginWord, String endWord, List<String> wordList) {
        Map<String, List<Integer>> map = new HashMap<>();
        wordList.add(0, beginWord);
        int len = wordList.size();
        for (int i = 0; i < len; i++) {
            String word1 = wordList.get(i);
            List<Integer> list = map.getOrDefault(word1, new ArrayList<>());
            for (int j = 0; j < len; j++) {
                if (i == j) {
                    continue;
                }
                String word2 = wordList.get(j);
                if (diff(word1, word2) == 1) {
                    list.add(j);
                }
            }
            map.put(word1, list);
        }
        Deque<Integer> queue = new ArrayDeque<>();
        queue.offer(0);
        boolean[] used = new boolean[len];
        int count = 1;
        while (!queue.isEmpty()) {
            count++;
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                String wordFrom = wordList.get(queue.poll());
                List<Integer> list = map.getOrDefault(wordFrom, new ArrayList<>());
                for (Integer wordToIdx : list) {
                    String wordTo = wordList.get(wordToIdx);
                    if (wordTo.equals(endWord)) {
                        return count;
                    }
                    if (!used[wordToIdx]) {
                        queue.offer(wordToIdx);
                        used[wordToIdx] = true;
                    }
                }
            }
        }
        return 0;
    }

    public int ladderLengthDFSTimeExceed(String beginWord, String endWord, List<String> wordList) {
        Map<String, List<Integer>> map = new HashMap<>();
        wordList.add(0, beginWord);
        int len = wordList.size();
        for (int i = 0; i < len; i++) {
            String word1 = wordList.get(i);
            List<Integer> list = map.getOrDefault(word1, new ArrayList<>());
            for (int j = 0; j < len; j++) {
                if (i == j) {
                    continue;
                }
                String word2 = wordList.get(j);
                if (diff(word1, word2) == 1) {
                    list.add(j);
                }
            }
            map.put(word1, list);
        }
        return dfs(beginWord, endWord, map, wordList, new boolean[len], 1);
    }

    private int dfs(String start, String end, Map<String, List<Integer>> map, List<String> wordList, boolean[] used,
            int count) {
        if (start.equals(end)) {
            return count;
        }
        int min = Integer.MAX_VALUE;
        boolean hasFound = false;
        List<Integer> words = map.getOrDefault(start, new ArrayList<>());
        for (Integer idx : words) {
            String word = wordList.get(idx);
            if (used[idx]) {
                continue;
            }
            used[idx] = true;
            int res = dfs(word, end, map, wordList, used, count + 1);
            if (res != 0) {
                hasFound = true;
                min = Math.min(min, res);
                System.out.println(String.format("%s -> %s, min = %s, hasFound = %s", word, end, min, hasFound));
            }
            used[idx] = false;
        }
        return hasFound ? min : 0;
    }

    /**
     * 单词接龙，但是根据单词和虚拟模板节点来建图
     * 如hit可以和模板*it，h*t，hi*虚拟节点建立连接，具体逻辑看addEdge方法
     */
    public int ladderLengthBetterSolution(String beginWord, String endWord, List<String> wordList) {
        if (beginWord.equals(endWord)) {
            return 1;
        }
        Map<String, List<String>> map = new HashMap<>();
        for (String word : wordList) {
            interconnectWithTemplate(word, map);
        }
        interconnectWithTemplate(beginWord, map);
        Map<String, Integer> step = new HashMap<>();
        Queue<String> queue = new LinkedList<>();
        queue.offer(beginWord);
        step.put(beginWord, 0);
        while (!queue.isEmpty()) {
            String word = queue.poll();
            if (word.equals(endWord)) {
                return step.get(word) / 2 + 1;
            }
            for (String template : map.get(word)) {
                if (!step.containsKey(template)) {
                    step.put(template, step.get(word) + 1);
                    queue.offer(template);
                }
            }
        }
        return 0;
    }

    private void interconnectWithTemplate(String word, Map<String, List<String>> map) {
        if (!map.containsKey(word)) {
            map.put(word, new ArrayList<>());
        }
        char[] wordChars = word.toCharArray();
        for (int i = 0; i < wordChars.length; i++) {
            char replacedChar = wordChars[i];
            wordChars[i] = '*';
            String template = new String(wordChars);
            map.get(word).add(template);
            if (!map.containsKey(template)) {
                map.put(template, new ArrayList<>());
            }
            map.get(template).add(word);
            wordChars[i] = replacedChar;
        }
    }

    /**
     * 添加与搜索单词 - 数据结构设计
     * 没做出来(基本做出来了)
     * dfs的写法可以更简洁一点
     */
    class WordDictionary {
        public class Node {
            char ch;
            Map<Character, Node> nextChars;
            boolean isEndOfWord;

            public Node() {
                this.nextChars = new HashMap<>();
                this.isEndOfWord = false;
            }

            public Node(char ch) {
                this.ch = ch;
                this.nextChars = new HashMap<>();
                this.isEndOfWord = false;
            }

            public Node(char ch, boolean isEndOfWord) {
                this.ch = ch;
                this.nextChars = new HashMap<>();
                this.isEndOfWord = isEndOfWord;
            }
        }

        private Node root;

        public WordDictionary() {
            this.root = new Node();
            root.nextChars.put('*', new Node());
        }

        public void addWord(String word) {
            Node node = root;
            int len = word.length();
            for (int i = 0; i < len; i++) {
                char ch = word.charAt(i);
                if (node.nextChars.containsKey(ch)) {
                    node = node.nextChars.get(ch);
                    continue;
                }
                Node newNode = new Node(ch);
                node.nextChars.put(ch, newNode);
                node = newNode;
            }
            node.isEndOfWord = true;
        }

        public boolean search(String word) {
            return search(word, 0, root);
        }

        private boolean search(String word, int start, Node startNode) {
            int len = word.length();
            Node node = startNode;
            for (int i = start; i < len; i++) {
                char c = word.charAt(i);
                if (c == '.') {
                    for (char ch = 'a'; ch <= 'z'; ch++) {
                        if (!node.nextChars.containsKey(ch)) {
                            continue;
                        }
                        if (search(word, i + 1, node.nextChars.get(ch))) {
                            return true;
                        }
                    }
                    return false;
                }
                if (!node.nextChars.containsKey(c)) {
                    return false;
                }
                node = node.nextChars.get(c);
            }
            return node.isEndOfWord;
        }
    }

    /**
     * 添加与搜索单词 - 数据结构设计
     * 简洁版 DFS
     */
    class WordDictionaryConciseDFS {
        public class Node {
            char ch;
            Map<Character, Node> nextChars;
            boolean isEndOfWord;

            public Node() {
                this.nextChars = new HashMap<>();
                this.isEndOfWord = false;
            }

            public Node(char ch) {
                this.ch = ch;
                this.nextChars = new HashMap<>();
                this.isEndOfWord = false;
            }

            public Node(char ch, boolean isEndOfWord) {
                this.ch = ch;
                this.nextChars = new HashMap<>();
                this.isEndOfWord = isEndOfWord;
            }
        }

        private Node root;

        public WordDictionaryConciseDFS() {
            this.root = new Node();
            root.nextChars.put('*', new Node());
        }

        public void addWord(String word) {
            Node node = root;
            int len = word.length();
            for (int i = 0; i < len; i++) {
                char ch = word.charAt(i);
                if (node.nextChars.containsKey(ch)) {
                    node = node.nextChars.get(ch);
                    continue;
                }
                Node newNode = new Node(ch);
                node.nextChars.put(ch, newNode);
                node = newNode;
            }
            node.isEndOfWord = true;
        }

        public boolean search(String word) {
            return dfs(word, 0, root);
        }

        private boolean dfs(String word, int index, Node node) {
            if (index == word.length()) {
                return node.isEndOfWord;
            }
            char ch = word.charAt(index);
            if (ch == '.') {
                for (Node child : node.nextChars.values()) {
                    if (dfs(word, index + 1, child)) {
                        return true;
                    }
                }
                return false;
            } else {
                if (!node.nextChars.containsKey(ch)) {
                    return false;
                }
                return dfs(word, index + 1, node.nextChars.get(ch));
            }
        }
    }

    /**
     * N皇后 II
     * 直接把N皇后那题返回的list的size返回就可以了
     */
    public int totalNQueens(int n) {
        return solveNQueens(n).size();
    }

    private List<List<String>> solveNQueens(int n) {
        boolean[][] grid = new boolean[n][n];
        List<List<String>> res = new ArrayList<>();
        dfs(res, grid, 0);
        return res;
    }

    private void dfs(List<List<String>> res, boolean[][] grid, int rowIdx) {
        int row = grid.length, col = grid[0].length;
        if (rowIdx == row) {
            StringBuilder sb = new StringBuilder();
            List<String> list = new ArrayList<>();
            for (boolean[] line : grid) {
                sb.setLength(0);
                for (boolean isQueen : line) {
                    sb.append(isQueen ? 'Q' : '.');
                }
                list.add(sb.toString());
            }
            res.add(list);
            return;
        }
        for (int colIdx = 0; colIdx < col; colIdx++) {
            boolean shouldContinue = false;
            for (int checkRow = rowIdx, checkCol = colIdx; valid(row, col, checkRow, checkCol)
                    && !shouldContinue; checkRow--) {
                if (grid[checkRow][checkCol]) {
                    shouldContinue = true;
                }
            }
            for (int checkRow = rowIdx, checkCol = colIdx; valid(row, col, checkRow,
                    checkCol) && !shouldContinue; checkRow--, checkCol--) {
                if (grid[checkRow][checkCol]) {
                    shouldContinue = true;
                }
            }
            for (int checkRow = rowIdx, checkCol = colIdx; valid(row, col, checkRow,
                    checkCol) && !shouldContinue; checkRow--, checkCol++) {
                if (grid[checkRow][checkCol]) {
                    shouldContinue = true;
                }
            }
            if (shouldContinue) {
                continue;
            }
            grid[rowIdx][colIdx] = true;
            dfs(res, grid, rowIdx + 1);
            grid[rowIdx][colIdx] = false;
        }
    }

    private boolean valid(int row, int col, int rowIdx, int colIdx) {
        return (rowIdx >= 0 && rowIdx < row) && (colIdx >= 0 && colIdx < col);
    }

    /**
     * H指数
     * 做出来了
     * 但是也提醒了我二分的模板必须要尽快弄熟悉
     */
    public int hIndex(int[] citations) {
        int sum = 0;
        int max = citations[0];
        Arrays.sort(citations);
        for (int citation : citations) {
            sum += citation;
            max = Math.max(max, citation);
        }
        while (max >= 0) {
            if (max * max <= sum && max <= citations.length) {
                int score = max;
                if (citations.length - findInsertPosInAscOrderByBinarySearch(citations, score) >= max) {
                    return max;
                }
            }
            max--;
        }
        return 0;
    }

    /**
     * 这里是二分的框架之一
     * 找到第一个比target大的
     */
    private int findInsertPosInAscOrderByBinarySearch(int[] nums, int target) {
        // 1 2 3 6 8
        // 5
        int lt = 0, rt = nums.length - 1;
        while (lt < rt) {
            int mid = (lt + rt) >> 1;
            if (nums[mid] < target) {
                lt = mid + 1;
            } else {
                rt = mid;
            }
        }
        return lt;
    }

    /**
     * 在排序数组中查找元素的第一个和最后一个位置
     * 这是一道拿来训练二分模板的十分好的题
     * 二分一般有几个场景：
     * 对于升序序列：
     * 确定target是否存在
     * 寻找target的左侧边界
     * 寻找target的右侧边界
     */
    public int[] searchRange(int[] nums, int target) {
        int ltBound = leftBoundAsc(nums, target);
        int rtBound = rightBoundAsc(nums, target);
        int[] res = new int[] { ((ltBound >= 0 && ltBound < nums.length) && nums[ltBound] == target ? ltBound : -1),
                ((rtBound >= 0 && rtBound < nums.length) && nums[rtBound] == target ? rtBound : -1) };
        return res;
    }

    /**
     * 二分模板
     * 寻找target
     * 左闭右闭，不存在则返回-1
     * 二分模板系列以及滑动窗口系列，可以参考：
     * https://labuladong.online/algo/essential-technique/binary-search-framework/
     * https://labuladong.online/algo/essential-technique/sliding-window-framework/
     * while内部判断的条件是区间内是否包含数据
     * 采用不同的闭区间、开区间、左闭右开区间的判断条件写法是不同的：
     * lt <= rt、lt + 1 < rt、lt < rt
     */
    private int findTargetAsc(int[] nums, int target) {
        int lt = 0, rt = nums.length - 1;
        while (lt <= rt) {
            int mid = (lt + rt) >> 1;
            if (nums[mid] > target) {
                rt = mid - 1;
            } else if (nums[mid] < target) {
                lt = mid + 1;
            } else if (nums[mid] == target) {
                return mid;
            }
        }
        return -1;
    }

    /**
     * 二分模板
     * 寻找target的左边界
     * while循环结束条件取决了是左闭右闭（<=），还是左闭右开（<）
     * 左闭右闭，返回的结果不一定是有效的索引
     * 如[1, 1], target = 2
     * 返回 idx = 2
     * 即，若target不存在于数组中，则返回的idx为插入target后target应在的索引
     * 对于lt和rt的定义可以从红蓝染色法的角度去理解
     * 另外，其实rightBound也可以通过target的转换来实现
     */
    private int leftBoundAsc(int[] nums, int target) {
        int lt = 0, rt = nums.length - 1;
        while (lt <= rt) {
            int mid = (lt + rt) >> 1;
            if (nums[mid] > target) {
                rt = mid - 1;
            } else if (nums[mid] < target) {
                lt = mid + 1;
            } else if (nums[mid] == target) {
                // 相等时，因为要找最左边界，所以尽量收缩右边界
                rt = mid - 1;
            }
        }
        // 返回左指针
        return lt;
    }

    /**
     * 二分模板
     * 寻找右边界
     * 左闭右闭
     * 返回的不一定是有效的索引
     * 如[1], target = 0
     * 返回 idx = -1
     * 即，若target不存在于数组中，则返回的idx + 1为插入target后target应在的索引
     */
    private int rightBoundAsc(int[] nums, int target) {
        int lt = 0, rt = nums.length - 1;
        while (lt <= rt) {
            int mid = (lt + rt) >> 1;
            if (nums[mid] > target) {
                rt = mid - 1;
            } else if (nums[mid] < target) {
                lt = mid + 1;
            } else {
                // 相等时，因为要找最右边界，所以尽量收缩左边界
                lt = mid + 1;
            }
        }
        // 返回右指针
        return rt;
    }

    /**
     * 二分模板
     * 寻找最左边界
     * 降序数组，左闭右闭
     */
    private int leftBoundDesc(int[] nums, int target) {
        int lt = 0, rt = nums.length - 1;
        while (lt <= rt) {
            int mid = (lt + rt) >> 1;
            if (nums[mid] > target) {
                lt = mid + 1;
            } else if (nums[mid] < target) {
                rt = mid - 1;
            } else if (nums[mid] == target) {
                // 相等时，因为要找最左边界，所以尽量收缩右边界
                rt = mid - 1;
            }
        }
        // 返回左指针
        return lt;
    }

    /**
     * 二分模板
     * 寻找最右边界
     * 降序数组，左闭右闭
     */
    private int rightBoundDesc(int[] nums, int target) {
        int lt = 0, rt = nums.length - 1;
        while (lt <= rt) {
            int mid = (lt + rt) >> 1;
            if (nums[mid] > target) {
                lt = mid + 1;
            } else if (nums[mid] < target) {
                rt = mid - 1;
            } else if (nums[mid] == target) {
                // 相等时，因为要找最右边界，所以尽量收缩左边界
                lt = mid + 1;
            }
        }
        // 返回右指针
        return rt;
    }

    /**
     * 二分模板
     * 左闭右开
     * 寻找左边界
     * 这里的记忆逻辑是
     * 由于右边是开区间，所以缩减右边界的逻辑是rt = mid就可以排除mid
     * 由于左边是闭区间，所以缩减左边界的逻辑是lt = mid + 1才可以避开mid
     */
    public int leftBoundLCloseROpen(int[] nums, int target) {
        int lt = 0, rt = nums.length;
        while (lt < rt) {
            int mid = (lt + rt) >> 1;
            if (nums[mid] < target) {
                lt = mid + 1;
            } else if (nums[mid] > target) {
                rt = mid;
            } else if (nums[mid] == target) {
                // 由于寻找最左边界，所以相等时优先缩减右边界
                rt = mid;
            }
        }
        return lt;
    }

    /**
     * 二分模板
     * 左闭右开
     * 寻找右边界
     * 这里的记忆逻辑是
     * 由于右边是开区间，所以缩减右边界的逻辑是rt = mid就可以排除mid
     * 由于左边是闭区间，所以缩减左边界的逻辑是lt = mid + 1才可以避开mid
     */
    public int rightBoundLCloseROpen(int[] nums, int target) {
        int lt = 0, rt = nums.length;
        while (lt < rt) {
            int mid = (lt + rt) >> 1;
            if (nums[mid] > target) {
                rt = mid;
            } else if (nums[mid] < target) {
                lt = mid + 1;
            } else if (nums[mid] == target) {
                // 由于寻找最右边界，所以相等时优先缩减左边界
                lt = mid + 1;
            }
        }
        // 注意这里返回的是lt - 1
        return lt - 1;
    }

    /**
     * 建立四叉树
     */
    public class Leetcode427Solution {
        class Node {
            public boolean val;
            public boolean isLeaf;
            public Node topLeft;
            public Node topRight;
            public Node bottomLeft;
            public Node bottomRight;

            public Node() {
                this.val = false;
                this.isLeaf = false;
                this.topLeft = null;
                this.topRight = null;
                this.bottomLeft = null;
                this.bottomRight = null;
            }

            public Node(boolean val, boolean isLeaf) {
                this.val = val;
                this.isLeaf = isLeaf;
                this.topLeft = null;
                this.topRight = null;
                this.bottomLeft = null;
                this.bottomRight = null;
            }

            public Node(boolean val, boolean isLeaf, Node topLeft, Node topRight, Node bottomLeft, Node bottomRight) {
                this.val = val;
                this.isLeaf = isLeaf;
                this.topLeft = topLeft;
                this.topRight = topRight;
                this.bottomLeft = bottomLeft;
                this.bottomRight = bottomRight;
            }
        }

        public Node construct(int[][] grid) {
            int n = grid.length;
            return dfs(grid, 0, 0, n);
        }

        private Node dfs(int[][] grid, int rowIdx, int colIdx, int len) {
            if (len == 0) {
                return null;
            }
            for (int i = 0; i < len; i++) {
                for (int j = 0; j < len; j++) {
                    int x = rowIdx + i;
                    int y = colIdx + j;
                    if (grid[x][y] != grid[rowIdx][colIdx]) {
                        int half = len >> 1;
                        Node node = new Node(grid[rowIdx][colIdx] == 1, false);
                        node.topLeft = dfs(grid, rowIdx, colIdx, half);
                        node.topRight = dfs(grid, rowIdx, colIdx + half, half);
                        node.bottomLeft = dfs(grid, rowIdx + half, colIdx, half);
                        node.bottomRight = dfs(grid, rowIdx + half, colIdx + half, half);
                        return node;
                    }
                }
            }
            Node node = new Node(grid[rowIdx][colIdx] == 1, true);
            return node;
        }
    }

    /**
     * 环形子数组最大和
     * Kadane算法是什么？
     * 没做出来，这题的解法很多，也都很巧妙
     * 关键是认识到，这个最大子数组要么是中间，要么是两侧，如果是两侧，就等价于sum - 最小子数组和
     * 或者是转换为长度2n的数组，然后使用单调队列去找一个长度不超过n的最大子数组
     */
    public int maxSubarraySumCircular(int[] nums) {
        int len = nums.length;
        int sum = 0;
        int maxSum = nums[0];
        int minSum = nums[0];
        int curMinSum = 0;
        int curMaxSum = 0;
        for (int i = 0; i < len; i++) {
            sum += nums[i];
            if (curMaxSum < 0) {
                curMaxSum = 0;
            }
            curMaxSum += nums[i];
            maxSum = Math.max(maxSum, curMaxSum);
            if (curMinSum > 0) {
                curMinSum = 0;
            }
            curMinSum += nums[i];
            minSum = Math.min(minSum, curMinSum);
        }
        // 这里因为sum - minSum可能为0，但是要求必须要取一个元素，所以需要做校验
        return sum == minSum ? maxSum : Math.max(maxSum, sum - minSum);
    }

    /**
     * 环形子数组最大和
     * 单调队列解法，十分巧妙，值得学习
     * 和滑动窗口的最大值那题异曲同工
     */
    public int maxSubarraySumCircularWithMonotonicQueue(int[] nums) {
        int len = nums.length;
        Deque<int[]> queue = new ArrayDeque<int[]>();
        int prefixSum = nums[0], res = nums[0];
        queue.offerLast(new int[] { 0, prefixSum });
        for (int i = 1; i < 2 * len; i++) {
            while (!queue.isEmpty() && queue.peekFirst()[0] < i - len) {
                queue.pollFirst();
            }
            prefixSum += nums[i % len];
            res = Math.max(res, prefixSum - queue.peekFirst()[1]);
            // 只允许放入更小的prefixSum
            while (!queue.isEmpty() && queue.peekLast()[1] >= prefixSum) {
                queue.pollLast();
            }
            queue.offerLast(new int[] { i, prefixSum });
        }
        return res;
    }

    /**
     * 寻找峰值
     * 没做出来
     * 需要用到红蓝染色法
     * 红蓝染色的核心是循环不变量
     * 满足题目要求的为蓝色，在rt
     * 不满足的为红色，在lt
     * 红蓝染色的一个核心是：
     * 二分区间[lt, rt]内的是没有染色的，外的是染色的，所以这里才是mid + 1, mid - 1
     */
    public int findPeakElement(int[] nums) {
        // lt左边的位于极大值点左边，rt右边的位于极大值及其右边
        int lt = 0, rt = nums.length - 2;
        while (lt <= rt) {
            int mid = (lt + rt) >> 1;
            if (nums[mid] < nums[mid + 1]) {
                lt = mid + 1;
            } else if (nums[mid] > nums[mid + 1]) {
                rt = mid - 1;
            } else {
                rt = mid - 1;
            }
        }
        return rt + 1;
    }

    /**
     * 寻找峰值
     * 开区间的写法
     */
    public int findPeakElementV2(int[] nums) {
        // lt左边的位于极大值点左边，rt右边的位于极大值及其右边
        int lt = -1, rt = nums.length - 1;
        while (lt + 1 < rt) {
            int mid = (lt + rt) >> 1;
            if (nums[mid] < nums[mid + 1]) {
                lt = mid;
            } else if (nums[mid] > nums[mid + 1]) {
                rt = mid;
            } else {
                rt = mid;
            }
        }
        return rt;
    }

    /**
     * 查找和最小的K对数字
     * 超时了，后面想到了极端情况，分析可以得知只看nums1, nums2的前k个元素就可以得出答案
     */
    public List<List<Integer>> kSmallestPairs(int[] nums1, int[] nums2, int k) {
        List<List<Integer>> res = new ArrayList<>();
        PriorityQueue<int[]> priorityQueue = new PriorityQueue<>(
                (a, b) -> Integer.compare(nums1[b[0]] + nums2[b[1]], nums1[a[0]] + nums2[a[1]]));
        for (int i = 0; i < Math.min(nums1.length, k); i++) {
            // 进行适当的剪枝
            if (priorityQueue.size() == k
                    && (nums1[priorityQueue.peek()[0]] + nums2[priorityQueue.peek()[1]]) < nums1[i] + nums2[0]) {
                break;
            }
            for (int j = 0; j < Math.min(nums2.length, k); j++) {
                int num = nums1[i] + nums2[j];
                int[] pair = priorityQueue.peek();
                int curMax = pair == null ? Integer.MIN_VALUE : nums1[pair[0]] + nums2[pair[1]];
                if ((priorityQueue.size() < k)) {
                    priorityQueue.offer(new int[] { i, j });
                } else if (curMax > num) {
                    priorityQueue.poll();
                    priorityQueue.offer(new int[] { i, j });
                } else if (curMax < num) {
                    break;
                }
            }
        }
        for (int[] pair : priorityQueue) {
            List<Integer> list = new ArrayList<>();
            list.add(nums1[pair[0]]);
            list.add(nums2[pair[1]]);
            res.add(list);
        }
        return res;
    }

    /**
     * 查找和最小的K对数字
     * 这是更好的解法
     * 思路十分巧妙，而且不好想
     * 核心就是以nums1作为参考
     * nums1[i], nums2[0]一定是nums1[i], nums2[j]中最小的
     * 所以先把所有nums1[i], nums2[0]放入最小堆中，然后每次取一次堆顶的元素出来然后再把
     * 对应的nums1[i]，下一个nums2的元素放进去
     * 类似多路归并
     */
    public List<List<Integer>> kSmallestPairsBetterSolution(int[] nums1, int[] nums2, int k) {
        PriorityQueue<int[]> priorityQueue = new PriorityQueue<>(k, (o1, o2) -> {
            return nums1[o1[0]] + nums2[o1[1]] - nums1[o2[0]] - nums2[o2[1]];
        });
        List<List<Integer>> res = new ArrayList<>();
        int l1 = nums1.length;
        int l2 = nums2.length;
        for (int i = 0; i < Math.min(l1, k); i++) {
            priorityQueue.offer(new int[] { i, 0 });
        }
        while (res.size() < k && !priorityQueue.isEmpty()) {
            int[] pair = priorityQueue.poll();
            List<Integer> list = new ArrayList<>();
            list.add(nums1[pair[0]]);
            list.add(nums2[pair[1]]);
            res.add(list);
            if (pair[1] + 1 < l2) {
                priorityQueue.offer(new int[] { pair[0], pair[1] + 1 });
            }
        }
        return res;
    }

    /**
     * 情侣牵手
     * 应该使用并查集去做
     */
    public int minSwapsCouples(int[] row) {
        int len = row.length;
        int coupleNum = len >> 1;
        UnionFindSet unionFindSet = new UnionFindSet(coupleNum);
        for (int i = 0; i < len; i += 2) {
            unionFindSet.union(row[i] / 2, row[i + 1] / 2);
        }
        return coupleNum - unionFindSet.getConnectedComponentNum();
    }

    public class UnionFindSet {

        private int connectedComponentNum;

        private int[] parent;

        public UnionFindSet(int connectedComponentNum) {
            this.connectedComponentNum = connectedComponentNum;
            parent = new int[connectedComponentNum];
            for (int i = 0; i < connectedComponentNum; i++) {
                parent[i] = i;
            }
        }

        public int getConnectedComponentNum() {
            return connectedComponentNum;
        }

        private int findRoot(int node) {
            while (node != parent[node]) {
                parent[node] = parent[parent[node]];
                node = parent[node];
            }
            return node;
        }

        public void union(int node1, int node2) {
            int root1 = findRoot(node1);
            int root2 = findRoot(node2);
            if (root1 == root2) {
                return;
            }
            parent[root1] = root2;
            connectedComponentNum--;
        }
    }

    /**
     * 二进制求和
     */
    public String addBinary(String a, String b) {
        int l1 = a.length();
        int l2 = b.length();
    }
}
