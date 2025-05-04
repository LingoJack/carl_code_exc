package upgrade_exc;

import java.util.Arrays;

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
}
