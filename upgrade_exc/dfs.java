package upgrade_exc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * dfs.java, dfs_record.java, dp.java是同一系列题目的不同写法
 * 都参照灵茶山艾府的dp题单：https://leetcode.cn/discuss/post/3581838/fen-xiang-gun-ti-dan-dong-tai-gui-hua-ru-007o/
 */
public class dfs {

    /**
     * 爬楼梯
     * 对于动态规划问题，可以先从回溯的角度去思考
     * 比如这个爬楼梯，有多少种方法
     * 可以考虑从前往后思考，因为最后一个受到的约束最小
     * 比如这个是爬楼梯的自顶向下的写法
     * 这个是存粹的DFS，没有做记忆化处理
     */
    public int climbStairs(int n) {
        return dfs4ClimbStairs(n, n);
    }

    private int dfs4ClimbStairs(int n, int floor) {
        if (floor == 1) {
            return 1;
        } else if (floor == 2) {
            return 2;
        }
        return dfs4ClimbStairs(n, floor - 1) + dfs4ClimbStairs(n, floor - 2);
    }

    /**
     * 使用最小的花费爬楼梯
     */
    public int minCostClimbingStairs(int[] cost) {
        return dfs4MinCostClimbingStairs(cost, cost.length);
    }

    private int dfs4MinCostClimbingStairs(int[] cost, int floor) {
        if (floor == 0 || floor == 1) {
            return 0;
        }
        return Math.min(
                dfs4MinCostClimbingStairs(cost, floor - 1) + cost[floor - 1],
                dfs4MinCostClimbingStairs(cost, floor - 2) + cost[floor - 2]);
    }

    /**
     * 组合总和IV
     */
    public int combinationSum4(int[] nums, int target) {
        return dfs4CombinationSum4(nums, target);
    }

    private int dfs4CombinationSum4(int[] nums, int target) {
        if (target == 0) {
            return 1;
        }
        if (target < 0) {
            return 0;
        }
        int count = 0;
        for (int num : nums) {
            count += dfs4CombinationSum4(nums, target - num);
        }
        return count;
    }

    /**
     * 统计构造好字符串的方案数
     */
    public int countGoodStrings(int low, int high, int zero, int one) {
        this.count = 0;
        dfs4CountGoodStrings(low, high, zero, one, 0);
        return this.count;
    }

    private int count;

    private void dfs4CountGoodStrings(int low, int high, int zero, int one, int len) {
        if (len > high) {
            return;
        }
        if (len >= low) {
            count++;
        }
        dfs4CountGoodStrings(low, high, zero, one, len + one);
        dfs4CountGoodStrings(low, high, zero, one, len + zero);
    }

    /**
     * 统计打字方案数
     */
    public int countTexts(String pressedKeys) {
        return dfs4CountTexts(pressedKeys, 0);
    }

    private int dfs4CountTexts(String pressedKeys, int idx) {
        int len = pressedKeys.length();
        if (idx == len) {
            return 1;
        }
        int count = 1;
        int repl = idx;
        while (repl >= 1 && pressedKeys.charAt(repl) == pressedKeys.charAt(repl - 1)) {
            repl--;
            count++;
        }
        return dfs4CountTexts(pressedKeys, idx + 1) * count;
    }
}
