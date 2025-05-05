package upgrade_exc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * dfs.java, dfs_record.java, dp.java是同一系列题目的不同写法
 * 都参照灵茶山艾府的dp题单：https://leetcode.cn/discuss/post/3581838/fen-xiang-gun-ti-dan-dong-tai-gui-hua-ru-007o/
 */
public class dfs_record_exc {

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
        int[] record = new int[pressedKeys.length() + 1];
        Arrays.fill(record, -1);
        return dfs4CountTexts(pressedKeys, 0, record);
    }

    private int dfs4CountTexts(String pressedKeys, int idx, int[] record) {
        int len = pressedKeys.length();
        if (idx == len) {
            return 1;
        }
        if (record[idx] >= 0) {
            return record[idx];
        }
        char digit = pressedKeys.charAt(idx);
        int limit = (digit == '7' || digit == '9') ? 4 : 3;
        int count = 0;
        for (int delta = 0; delta < limit && idx + delta < len; delta++) {
            if (pressedKeys.charAt(idx + delta) == digit) {
                count = (count + dfs4CountTexts(pressedKeys, idx + delta + 1, record)) % 1_000_000_007;
            } else {
                break;
            }
        }
        record[idx] = count;
        return count;
    }

    /**
     * 打家劫舍
     */
    public int rob(int[] nums) {
        int[] record = new int[nums.length + 1];
        Arrays.fill(record, -1);
        return dfs4Rob(nums, 0, record);
    }

    // 这个是一个典型的记忆化存储的错误，根源就是入参有idx，sum两个变量，这里record却只以idx为key
    private int dfs4RobError(int[] nums, int idx, int sum, int[] record) {
        if (idx >= nums.length) {
            return sum;
        }
        if (record[idx] >= 0) {
            return record[idx];
        }
        int res = Math.max(dfs4RobError(nums, idx + 1, sum, record),
                dfs4RobError(nums, idx + 2, sum + nums[idx], record));
        record[idx] = res;
        return res;
    }

    private int dfs4Rob(int[] nums, int idx, int[] record) {
        if (idx >= nums.length) {
            return 0;
        }
        if (record[idx] >= 0) {
            return record[idx];
        }
        int res = Math.max(dfs4Rob(nums, idx + 1, record), dfs4Rob(nums, idx + 2, record) + nums[idx]);
        record[idx] = res;
        return res;
    }

    /**
     * 打家劫舍II
     * 核心是意识到问题可以拆解为
     * 首尾两间房不能同时抢
     */
    public int robII(int[] nums) {
        int[] record1 = new int[nums.length + 1];
        int[] record2 = new int[nums.length + 1];
        Arrays.fill(record1, -1);
        Arrays.fill(record2, -1);
        return Math.max(dfs4RobII(nums, 0, nums.length - 2, record1), dfs4RobII(nums, 1, nums.length - 1, record2));
    }

    private int dfs4RobII(int[] nums, int start, int end, int[] record) {
        if (start >= nums.length) {
            return 0;
        }
        if (record[start] >= 0) {
            return record[start];
        }
        int res = Math.max(dfs4RobII(nums, start + 1, end, record),
                dfs4RobII(nums, start + 2, end, record) + nums[start]);
        record[start] = res;
        return res;
    }

    /**
     * 统计放置房子的方式数
     */
    public int countHousePlacements(int n) {
        int[] record = new int[n + 1];
        Arrays.fill(record, -1);
        int tempRes = dfs(n, 0, record);
        long res = (long) tempRes * tempRes % 1_000_000_007;
        return (int) res;
    }

    private int dfs(int blockNum, int start, int[] record) {
        if (start >= blockNum) {
            return 1;
        }
        if (record[start] >= 0) {
            return record[start];
        }
        int res = (dfs(blockNum, start + 2, record) + dfs(blockNum, start + 1, record)) % 1_000_000_007;
        record[start] = res;
        return res;
    }

    /**
     * 删除并获得点数
     * 妙啊，这题可以转换成为打家劫舍题
     */
    public int deleteAndEarn(int[] nums) {
        Map<Integer, Integer> map = new HashMap<>();
        int max = 0;
        for (int num : nums) {
            map.put(num, map.getOrDefault(num, 0) + num);
            max = Math.max(max, num);
        }
        int[] sums = new int[max + 1];
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            sums[entry.getKey()] = entry.getValue();
        }
        return rob(sums);
    }

    /**
     * 施咒的最大总伤害
     */
    public long maximumTotalDamage(int[] powers) {
        Map<Integer, Long> sumMap = new HashMap<>();
        for (int power : powers) {
            sumMap.put(power, sumMap.getOrDefault(power, 0L) + power);
        }
        int[] sortedPowers = sumMap.keySet().stream()
                .sorted()
                .mapToInt(Integer::intValue)
                .toArray();
        int len = sortedPowers.length;
        if (len < 2) {
            return len == 0 ? 0 : sumMap.get(sortedPowers[0]);
        }
        long[] dp = new long[len];
        dp[0] = sumMap.get(sortedPowers[0]);
        dp[1] = sortedPowers[1] - sortedPowers[0] < 3 ? Math.max(dp[0], sumMap.get(sortedPowers[1]))
                : dp[0] + sumMap.get(sortedPowers[1]);
        if (len >= 3) {
            long option1 = dp[0] + (sortedPowers[2] - sortedPowers[0] >= 3 ? sumMap.get(sortedPowers[2]) : 0);
            long option2 = dp[1] + (sortedPowers[2] - sortedPowers[1] >= 3 ? sumMap.get(sortedPowers[2]) : 0);
            dp[2] = Math.max(Math.max(option1, option2), sumMap.get(sortedPowers[2]));
        }
        for (int i = 3; i < len; i++) {
            int lastSelectablePower = i - 1;
            while (lastSelectablePower >= 0 && sortedPowers[i] - sortedPowers[lastSelectablePower] < 3) {
                lastSelectablePower--;
            }
            long selectCurrent = (lastSelectablePower >= 0 ? dp[lastSelectablePower] : 0) + sumMap.get(sortedPowers[i]);
            dp[i] = Math.max(selectCurrent, dp[i - 1]);
        }
        return dp[len - 1];
    }
}
