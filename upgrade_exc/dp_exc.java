package upgrade_exc;

import java.util.Arrays;

/**
 * dfs.java, dfs_record.java, dp.java是同一系列题目的不同写法
 * 都参照灵茶山艾府的dp题单：https://leetcode.cn/discuss/post/3581838/fen-xiang-gun-ti-dan-dong-tai-gui-hua-ru-007o/
 */
public class dp_exc {

    /**
     * 爬楼梯
     * 动态规划
     */
    public int climbStairs(int n) {
        if (n <= 2) {
            return n == 1 ? 1 : 2;
        }
        int[] dp = new int[n + 1];
        dp[1] = 1;
        dp[2] = 2;
        for (int i = 3; i < n + 1; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        return dp[n];
    }

    /**
     * 使用最小的花费爬楼梯
     */
    public int minCostClimbingStairs(int[] cost) {
        int[] dp = new int[cost.length + 1];
        dp[0] = 0;
        dp[1] = 0;
        for (int i = 2; i < cost.length + 1; i++) {
            dp[i] = Math.min(dp[i - 1] + cost[i - 1], dp[i - 2] + cost[i - 2]);
        }
        return dp[cost.length];
    }

    /**
     * 组合总和IV
     */
    public int combinationSum4(int[] nums, int target) {
        int[] dp = new int[target + 1];
        dp[0] = 1;
        for (int i = 0; i < target + 1; i++) {
            for (int j = 0; j < nums.length; j++) {
                dp[i] += i >= nums[j] ? dp[i - nums[j]] : 0;
            }
        }
        return dp[target];
    }

    /**
     * 统计构造好字符串的方案数
     */
    public int countGoodStrings(int low, int high, int zero, int one) {
        // dp[i]为长度为i的字符串的个数
        int[] dp = new int[high + 1];
        dp[0] = 1;
        int mod = 1_000_000_007;
        int count = 0;
        for (int i = 1; i < high + 1; i++) {
            dp[i] = (((i >= zero ? dp[i - zero] : 0) % mod) + ((i >= one ? dp[i - one] : 0) % mod)) % mod;
            if (i >= low) {
                count = (count + dp[i]) % mod;
            }
        }
        return count;
    }

    /**
     * 统计打字方案数
     */
    public int countTexts(String pressedKeys) {
        int len = pressedKeys.length();
        // dp[i] 长度为i的字符串的打字方案数
        int[] dp = new int[len + 1];
        dp[0] = 1;
        for (int i = 1; i <= len; i++) {
            char digit = pressedKeys.charAt(i - 1);
            int limit = (digit == '7' || digit == '9') ? 4 : 3;
            dp[i] = 0;
            for (int delta = 0; delta < limit && i - delta >= 1; delta++) {
                if (pressedKeys.charAt(i - delta - 1) != digit) {
                    break;
                }
                dp[i] = (dp[i] + dp[i - delta - 1]) % 1_000_000_007;
            }
        }
        return dp[len];
    }

    /**
     * 解决智力问题
     */
    public long mostPoints(int[][] questions) {
        int len = questions.length;
        long[] dp = new long[len + 1];
        for (int i = len - 1; i >= 0; --i) {
            int next = i + questions[i][1] + 1;
            long take = questions[i][0] + (next < len ? dp[next] : 0);
            long skip = dp[i + 1];
            dp[i] = Math.max(take, skip);
        }
        return dp[0];
    }
}