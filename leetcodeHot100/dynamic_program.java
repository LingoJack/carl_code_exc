package leetcodeHot100;

import java.util.ArrayDeque;
import java.util.Deque;

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
        for(boolean match : note) {
            if (match) {
                count++;
                max = Math.max(max, count);
            }
            else {
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
            }
            else {
                if (s.charAt(i - 1) == '(') {
                    dp[i] = (i >= 2 ? dp[i - 2] : 0) + 2;
                }
                else {
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
}
