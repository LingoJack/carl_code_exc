package leetcodeHot100;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
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
     * 单词搜索II
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

    class Node {
        public int val;
        public List<Node> neighbors;

        public Node() {
            this.val = 0;
            neighbors = new ArrayList<Node>();
        }

        public Node(int val) {
            this.val = val;
            neighbors = new ArrayList<Node>();
        }

        public Node(int val, ArrayList<Node> neighbors) {
            this.val = val;
            this.neighbors = neighbors;
        }
    }

    /**
     * 克隆图
     */
    public Node cloneGraph(Node node) {
        Map<Node, Node> map = new HashMap<>();
        Set<Node> set = new HashSet<>();
        Deque<Node> queue = new ArrayDeque<>();
        if (node == null) {
            return null;
        }
        // 完成map的初始化
        queue.offer(node);
        while (!queue.isEmpty()) {
            Node originNode = queue.poll();
            if (!set.contains(originNode)) {
                for (Node nextNode : originNode.neighbors) {
                    queue.offer(nextNode);
                }
                Node newNode = new Node(originNode.val);
                map.put(originNode, newNode);
                set.add(originNode);
            }
        }
        set.clear();
        queue.offer(node);
        while (!queue.isEmpty()) {
            Node originNode = queue.poll();
            if (!set.contains(originNode)) {
                List<Node> list = new ArrayList<>();
                for (Node nextNode : originNode.neighbors) {
                    list.add(map.get(nextNode));
                    queue.offer(nextNode);
                }
                map.get(originNode).neighbors = list;
                set.add(originNode);
            }
        }
        return map.get(node);
    }

    public Node cloneGraphBFS(Node node) {
        Map<Node, Node> map = new HashMap<>();
        Deque<Node> queue = new ArrayDeque<>();
        if (node == null) {
            return null;
        }
        queue.offer(node);
        while (!queue.isEmpty()) {
            Node originNode = queue.poll();
            map.putIfAbsent(node, new Node(node.val));
            for (Node nextNode : originNode.neighbors) {
                if (!map.containsKey(nextNode)) {
                    map.put(nextNode, new Node(nextNode.val));
                    queue.offer(nextNode);
                }
                map.get(originNode).neighbors.add(map.get(nextNode));
            }
        }
        return map.get(node);
    }

}
