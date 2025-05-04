package upgrade_exc;

import java.util.Arrays;

/**
 * dfs.java, dfs_record.java, dp.java是同一系列题目的不同写法
 * 都参照灵茶山艾府的dp题单：https://leetcode.cn/discuss/post/3581838/fen-xiang-gun-ti-dan-dong-tai-gui-hua-ru-007o/
 */
public class dfs_record {

    /**
     * 爬楼梯
     * 依旧是DFS，但是加入了记忆化处理
     * 你可能会有疑惑，既然是DFS自顶向下，那么在返回上面的结果的时候，下面的结果应该都算完了
     * 这个记忆化处理又是在哪里用的呢
     * 这个你可以画一个DFS的树形调用图就知道了
     */
    public int climbStairs(int n) {
        int[] record = new int[n + 1];
        Arrays.fill(record, -1);
        return dfs4ClimbStairs(n, n, record);
    }

    private int dfs4ClimbStairs(int n, int floor, int[] record) {
        // 先检查是否在之前的结果中出现过
        if (record[floor] >= 0) {
            return record[floor];
        }
        if (floor == 1 || floor == 2) {
            return floor == 1 ? 1 : 2;
        }
        int res = dfs4ClimbStairs(n, floor - 1, record) + dfs4ClimbStairs(n, floor - 2, record);
        record[floor] = res;
        return res;
    }

    /**
     * 使用最小的花费爬楼梯
     */
    public int minCostClimbingStairs(int[] cost) {
        int[] record = new int[cost.length + 1];
        Arrays.fill(record, -1);
        return dfs4MinCostClimbingStairs(cost, cost.length, record);
    }

    private int dfs4MinCostClimbingStairs(int[] cost, int floor, int[] record) {
        if (record[floor] >= 0) {
            return record[floor];
        }
        if (floor == 0 || floor == 1) {
            return 0;
        }
        int res = Math.min(dfs4MinCostClimbingStairs(cost, floor - 1, record) + cost[floor - 1],
                dfs4MinCostClimbingStairs(cost, floor - 2, record) + cost[floor - 2]);
        record[floor] = res;
        return res;
    }

    /**
     * 组合总和IV
     */
    public int combinationSum4(int[] nums, int target) {
        int[] record = new int[target + 1];
        Arrays.fill(record, -1);
        return dfs4CombinationSum4(nums, target, record);
    }

    private int dfs4CombinationSum4(int[] nums, int target, int[] record) {
        if (record[target] >= 0) {
            return record[target];
        }
        if (target == 0) {
            return 1;
        }
        if (target < 0) {
            return 0;
        }
        int count = 0;
        for (int num : nums) {
            count += dfs4CombinationSum4(nums, target - num, record);
        }
        record[target] = count;
        return count;
    }

    /**
     * 统计构造好字符串的方案数
     */
    public int countGoodStrings(int low, int high, int zero, int one) {
        int[] record = new int[high + 1];
        Arrays.fill(record, -1);
        return dfs4CountGoodStrings(low, high, zero, one, 0, record);
    }

    private int dfs4CountGoodStrings(int low, int high, int zero, int one, int len, int[] record) {
        int count = 0;
        if (len > high) {
            return 0;
        }
        if (record[len] >= 0) {
            return record[len];
        }
        if (len >= low) {
            count++;
        }
        count += dfs4CountGoodStrings(low, high, zero, one, len + one, record);
        count += dfs4CountGoodStrings(low, high, zero, one, len + zero, record);
        count %= 1_000_000_007;
        record[len] = count;
        return count;
    }

    /**
     * 统计打字方案数
     */
    public int countTexts(String pressedKeys) {

    }
}
