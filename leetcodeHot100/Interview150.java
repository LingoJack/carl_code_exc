package leetcodeHot100;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

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
     * 曼哈顿树
     */
    public void ansV1() {
        Scanner sc = new Scanner(System.in);
        int nodeNum = sc.nextInt();
        int queryNum = sc.nextInt();
        Map<Integer, TreeNode> map = new HashMap<>();
        map.put(1, new TreeNode(1, 0, 0));
        for (int i = 0; i < nodeNum - 1; i++) {
            int parent = sc.nextInt();
            int son = sc.nextInt();
            TreeNode parentNode = map.get(parent);
            TreeNode node = new TreeNode(son);
            if (parentNode.lt == null) {
                node.x = parentNode.x - 1;
                node.y = parentNode.y - 1;
                parentNode.lt = node;
            } else {
                node.x = parentNode.x + 1;
                node.y = parentNode.y - 1;
                parentNode.rt = node;
            }
            map.put(son, node);
        }
        for (int i = 0; i < queryNum; i++) {
            int node1Id = sc.nextInt();
            int node2Id = sc.nextInt();
            TreeNode node1 = map.get(node1Id);
            TreeNode node2 = map.get(node2Id);
            int xVal = Math.abs(node1.x - node2.x);
            int yVal = Math.abs(node1.y - node2.y);
            // int xVal = node1.x > node2.x ? node1.x - node2.x : node2.x - node1.x;
            // int yVal = node1.y > node2.y ? node1.y - node2.y : node2.y - node1.y;
            System.out.println(xVal + yVal);
        }
    }

    public class TreeNode {
        int id;
        int x;
        int y;
        TreeNode lt;
        TreeNode rt;

        public TreeNode(int id, int x, int y) {
            this.id = id;
            this.x = x;
            this.y = y;
        }

        public TreeNode(int id) {
            this.id = id;
        }
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

    private int largerThan(List<Integer> num1, List<Integer> num2) {
        for (int i = 0; i < num1.size(); i++) {
            if (!num1.get(i).equals(num2.get(i))) {
                return num1.get(i) - num2.get(i);
            }
        }
        return 0;
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

    public static String toChinese(long number) {
        String[] chineseNumber = { "零", "一", "二", "三", "四", "五", "六", "七", "八", "九" };
        String[] units = { "", "十", "百", "千", "万", "十万", "百万", "千万", "亿" };
        if (number == 0) {
            return chineseNumber[0];
        }
        StringBuilder result = new StringBuilder();
        int unitPos = 0;
        boolean needZero = false;
        while (number != 0) {
            int digit = (int) (number % 10);
            if (digit == 0) {
                if (!needZero) {
                    needZero = true;
                }
            } else {
                if (needZero) {
                    result.insert(0, chineseNumber[0]);
                    needZero = false;
                }
                result.insert(0, units[unitPos]);
                result.insert(0, chineseNumber[digit]);
            }
            unitPos++;
            number /= 10;
        }
        return result.toString();
    }

    
}
