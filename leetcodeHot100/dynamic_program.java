package leetcodeHot100;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.PriorityQueue;

public class dynamic_program {

    /**
     * 最小路径和
     * 9min30s ac
     * 其实写完代码花了4min30s，剩下的时间在查初始化的Bug...
     */
    public int minPathSum(int[][] grid) {
        if (grid == null) {
            return 0;
        }
        int row = grid.length;
        int col = grid[0].length;
        int[][] dp = new int[row][col];
        for (int i = 0; i < col; i++) {
            dp[0][i] = i > 0 ? dp[0][i - 1] + grid[0][i] : grid[0][i];
        }
        for (int i = 1; i < row; i++) {
            dp[i][0] = dp[i - 1][0] + grid[i][0];
        }
        for (int i = 1; i < row; i++) {
            for (int j = 1; j < col; j++) {
                dp[i][j] = Math.min(dp[i][j - 1], dp[i - 1][j]) + grid[i][j];
            }
        }
        return dp[row - 1][col - 1];
    }

    /**
     * 最长公共子序列
     * 7min ac
     */
    public int longestCommonSubsequence(String text1, String text2) {
        int len1 = text1.length();
        int len2 = text2.length();

        // 以i - 1的text1和j - 1 结尾的text2的最长公共子序列
        int[][] dp = new int[len1 + 1][len2 + 1];
        for (int i = 1; i < len1 + 1; i++) {
            for (int j = 1; j < len2 + 1; j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        return dp[len1][len2];
    }

    /**
     * 最长有效括号
     * 这是最好理解的思路：
     * 如果直接用栈，似乎不好处理连续的判定
     * 可以用一个数组，匹配的括号对设置值，
     * 然后就遍历这个数组，求连续有值的个数
     */
    public int longestValidParentheses(String s) {
        Deque<Integer> stack = new ArrayDeque<>();
        int len = s.length();
        boolean[] note = new boolean[len];
        for (int i = 0; i < len; i++) {
            char ch = s.charAt(i);
            if (ch == '(') {
                stack.push(i);
            } else if (ch == ')') {
                Integer index = stack.peek();
                if (index == null) {
                    continue;
                }
                Character lastCharacter = s.charAt(index);
                if (lastCharacter != null && lastCharacter == '(') {
                    stack.pop();
                    note[index] = true;
                    note[i] = true;
                }
            }
        }
        int count = 0;
        int max = 0;
        for (boolean match : note) {
            if (match) {
                count++;
                max = Math.max(max, count);
            } else {
                count = 0;
            }
        }
        return max;
    }

    /**
     * 最长有效括号
     * DP解法
     * 核心是想清楚dp的定义，什么是以第i个字符结尾的有效括号的最大长度
     * 例如：（））就是 0， （（）就说2
     * 然后去想递推公式
     */
    public int longestValidParenthesesWithDP(String s) {
        int max = 0;
        int len = s.length();
        // dp[i] 表示以第i个字符结尾的有效括号的最大长度
        int[] dp = new int[len];
        for (int i = 1; i < len; i++) {
            char token = s.charAt(i);
            if (token == '(') {
                continue;
            } else {
                if (s.charAt(i - 1) == '(') {
                    dp[i] = (i >= 2 ? dp[i - 2] : 0) + 2;
                } else {
                    // (())
                    // 0123
                    int index2Match = i - dp[i - 1] - 1;
                    if (index2Match >= 0 && s.charAt(index2Match) == '(') {
                        dp[i] = dp[i - 1] + 2 + (index2Match >= 1 ? dp[index2Match - 1] : 0);
                    }
                }
            }
            max = Math.max(max, dp[i]);
        }
        return max;
    }

    private boolean ok = false;

    /**
     * 分割等和子集
     * 回溯解法，但是超时了
     */
    public boolean canPartition(int[] nums) {
        int total = 0;
        for (int num : nums) {
            total += num;
        }
        if (total % 2 == 1) {
            return false;
        }
        findTargetSum(nums, total / 2, 0);
        return ok;
    }

    private void findTargetSum(int[] nums, int target, int start) {
        if (start == nums.length || ok) {
            return;
        }
        if (nums[start] == target) {
            ok = true;
        }
        findTargetSum(nums, target, start + 1);
        findTargetSum(nums, target - nums[start], start + 1);
    }

    /**
     * 分割等和子集
     * 没做出来,dp忘记太多了
     * 很不熟练
     */
    public boolean canPartitionWithDp(int[] nums) {
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        if (sum % 2 != 0) {
            return false;
        }
        int target = sum / 2;
        // dp[i][j]，考虑下标i的数为止，是否存在和为j的子集
        boolean[][] dp = new boolean[nums.length][target + 1];
        for (int i = 0; i < nums.length; i++) {
            dp[i][0] = true;
        }
        if (nums[0] < target + 1) {
            dp[0][nums[0]] = true;
        }
        for (int i = 1; i < nums.length; i++) {
            for (int j = 1; j < target + 1; j++) {
                dp[i][j] = dp[i - 1][j] || (j - nums[i] >= 0 ? dp[i - 1][j - nums[i]] : false);
            }
        }
        return dp[nums.length - 1][target];
    }

    /**
     * 打家劫舍
     * 13min ac
     */
    public int rob(int[] nums) {
        if (nums == null) {
            return 0;
        }
        if (nums.length == 1) {
            return nums[0];
        }
        // dp[i]为考虑到nums[i]（0没打劫 1打劫了）为止能打劫到的最大价值
        int[][] dp = new int[nums.length][2];
        dp[0][0] = 0;
        dp[0][1] = nums[0];
        for (int i = 1; i < nums.length; i++) {
            dp[i][0] = Math.max(dp[i - 1][1], dp[i - 1][0]);
            dp[i][1] = dp[i - 1][0] + nums[i];
        }
        return Math.max(dp[nums.length - 1][0], dp[nums.length - 1][1]);
    }

    /**
     * 最长递增子序列
     */
    public int lengthOfLIS(int[] nums) {
        int[] dp = new int[nums.length];
        Arrays.fill(dp, 1);
        int res = 1;
        for (int i = 1; i < nums.length; i++) {
            for (int j = i - 1; j >= 0; j--) {
                if (nums[i] > nums[j] && dp[i] <= 1 + dp[j]) {
                    dp[i] = dp[j] + 1;
                }
                res = Math.max(res, dp[i]);
            }
        }
        return res;
    }

    /**
     * 最长回文子串
     */
    public String longestPalindromeWithBacktracking(String s) {
        int[] index = new int[2];
        find(s, 0, s.length() - 1, new boolean[s.length()][s.length()], index);
        return s.substring(index[0], index[1] + 1);
    }

    private int max = 0;

    private int find(String s, int head, int tail, boolean[][] isPalindrome, int[] index) {
        if (tail == head) {
            return 1;
        }
        if (isPalindrome(s, head, tail, isPalindrome)) {
            int len = tail - head + 1;
            if (len > max) {
                max = len;
                index[0] = head;
                index[1] = tail;
            }
            return len;
        } else {
            return Math.max(find(s, head + 1, tail, isPalindrome, index), find(s, head, tail - 1, isPalindrome, index));
        }
    }

    private boolean isPalindrome(String s, int head, int tail, boolean[][] isPalindrome) {
        if (isPalindrome[head][tail]) {
            return true;
        }
        int lt = head;
        int rt = tail;
        while (rt > lt) {
            if (s.charAt(rt) != s.charAt(lt)) {
                return false;
            }
            rt--;
            lt++;
        }
        isPalindrome[head][tail] = true;
        return true;
    }

    /**
     * 最长回文子串
     * 没做出来
     */
    public String longestPalindrome(String s) {
        int len = s.length();
        if (len < 2) {
            return s;
        }
        int maxLen = 1;
        int start = 0;
        boolean[][] dp = new boolean[len][len];
        for (int i = 0; i < len; i++) {
            dp[i][i] = true;
        }
        for (int j = 1; j < len; j++) {
            for (int i = j - 1; i >= 0; i--) {
                if (s.charAt(i) != s.charAt(j)) {
                    dp[i][j] = false;
                } else {
                    dp[i][j] = j - i == 1 ? true : dp[i + 1][j - 1];
                }
                if (dp[i][j] && j - i + 1 > maxLen) {
                    maxLen = j - i + 1;
                    start = i;
                }
            }
        }
        return s.substring(start, start + maxLen);
    }

    // rand5 如何实现 rand7
    // rand5 = 0...4
    // rand7 = 0...6
    // rand7 = rand5 + 0...2 = rand5 + rand5 % 3
    // rand5 % 3 = 0 1 2 0 1
    // 其实就是要产生0...6，那么可以考虑 %7
    // 5 * rand5 = 0 5 10 15 20
    // (rand5 + 5 * rand5) % 7

    /**
     * 最长有小括号
     * 二刷，8min ac
     */
    public int longestValidParenthesesReview(String s) {
        int len = s.length();
        // 以下标i结尾的最长有效括号长度
        int[] dp = new int[len];
        int max = 0;
        for (int i = 1; i < len; i++) {
            char ch = s.charAt(i);
            if (ch == '(') {
                dp[i] = 0;
            } else {
                // )
                char last = s.charAt(i - 1);
                if (last == '(') {
                    dp[i] = (i >= 2 ? dp[i - 2] : 0) + 2;
                } else {
                    // (())
                    // 0123
                    int start = i - dp[i - 1] - 1;
                    if (start >= 0) {
                        char front = s.charAt(start);
                        if (front == '(') {
                            dp[i] = dp[i - 1] + 2 + (start >= 1 ? dp[start - 1] : 0);
                        }
                    }
                }
            }
            max = Math.max(max, dp[i]);
        }
        return max;
    }

    /**
     * 最长回文子串
     * 二刷，没做出来
     * 关键是要理解，外层循环是长度
     */
    public String longestPalindromeReview(String s) {
        // babad
        int len = s.length();
        // dp[i][j] ：s.substring(i, j + 1)是否为回文串
        boolean[][] dp = new boolean[len + 1][len + 1];
        for (int i = 0; i <= len; i++) {
            dp[i][i] = true;
        }
        int max = 1;
        int index = 0;
        for (int subLen = 2; subLen <= subLen; subLen++) {
            for (int start = 0; start <= subLen - subLen; start++) {
                int end = start + subLen - 1;
                if (subLen == 2) {
                    dp[start][end] = s.charAt(start) == s.charAt(end);
                } else {
                    dp[start][end] = s.charAt(start) == s.charAt(end) && dp[start + 1][end - 1];
                }
                if (dp[start][end] && subLen > max) {
                    max = subLen;
                    index = start;
                }
            }
        }
        return s.substring(index, index + max);
    }


    
}
