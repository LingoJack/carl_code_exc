package interview150;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        for(int i = 0; i < row; i++) {
            for(int j = 0; j < col; j++) {
                if (matrix[i][j] == 0) {
                    zeroCols[j] = true;
                    zeroRows[i] = true;
                }
            }
        }
        for(int i = 0; i < row; i++) {
            if (zeroRows[i]) {
                for(int j = 0; j < col; j++) {
                    matrix[i][j] = 0;
                }
            }
        }
        for(int i = 0; i < col; i++) {
            if (zeroCols[i]) {
                for(int j = 0; j < row; j++) {
                    matrix[j][i] = 0;
                }
            }
        }
    }
}
