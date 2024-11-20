package dynamic_program;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import doublepointer.doublepointer;
import stack_and_queue.stack_and_queue;

/**
 * 1. 确定dp数组（dp table）以及下标的含义
 * 2. 确定递推公式
 * 3. dp数组如何初始化
 * 4. 确定遍历顺序
 * 5. 举例推导dp数组
 */
 public class dynamic_program {

    /**
     * 斐波那契数
     * 注意下标是否有意义
     */
    public int fib(int n) {
        int res = 0;
        int[] fn = new int[n + 1];

        if (n < 2) {
            return n;
        }

        fn[0] = 0;
        fn[1] = 1;
        for(int i = 2; i <= n; i++) {
            fn[i] = fn[i - 1] + fn[i - 2];
        }
        res = fn[n];
        return res;
    }

    /**
     * 爬楼梯
     * 注意下标是否有意义
     */
    public int climbStairs(int n) {
        if (n < 3) {
            return n;
        }

        int[] step = new int[n + 1];
        step[1] = 1;
        step[2] = 2;

        // step[n] = step[n - 1] + step[n - 2]
        for(int i = 3; i < n + 1; i++) {
            step[i] = step[i - 1] + step[i - 2];
        }

        return step[n];
    }

    /**
     * 使用最小花费爬楼梯
     * 关键是要明确dp数组的定义
     */
    public int minCostClimbingStairs(int[] cost) {
        // 到达i位置的最低花费
        int[] step = new int[cost.length + 1];

        if (cost.length < 2) {
            return cost.length == 0 ? 0 : cost[0];
        }

        // step[i] = Math.min(step[i - 1], step[i - 2] + cost[i]);
        step[0] = 0;
        step[1] = 0;

        for(int i = 2; i < step.length; i++) {
            step[i] = Math.min(step[i - 1] + cost[i - 1], step[i - 2] + cost[i - 2]);
        }

        return step[cost.length];
    }

    /**
     * 不同路径
     * 关键是确定dp数组的初始化，第一行和第一列都初始化为1
     */
    public int uniquePaths(int m, int n) {
        // 到(i,j)的路径数
        int[][] dp = new int[m][n];

        // dp[i][j] = dp[i - 1][j] + dp[i][j - 1];

        for(int i = 0; i < m; i++) {
            dp[i][0] = 1;
        }

        for(int j = 0; j < n; j++) {
            dp[0][j] = 1;
        }

        for(int i = 1; i < m; i++) {
            for(int j = 1; j < n; j++) {
                dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
            }
        }

        return dp[m - 1][n - 1];
    }

    /**
     * 不同路径 II
     * 关键是与遇到障碍的处理（保持0），以及初始化时遇到障碍的处理（如果遇到0，那后面也是0）
     */
    public int uniquePathsWithObstacles(int[][] obstacleGrid) {
        int m = obstacleGrid.length;
        int n = obstacleGrid[0].length;

        // 到(i,j)的路径数
        int[][] dp = new int[m][n];

        // dp[i][j] = dp[i - 1][j] + dp[i][j - 1];

        boolean hasBlock = false;
        for(int i = 0; i < m; i++) {
            if(obstacleGrid[i][0] == 1) {
                hasBlock = true;
            }
            dp[i][0] = hasBlock ? 0 : 1;
        }

        hasBlock = false;
        for(int j = 0; j < n; j++) {
            if(obstacleGrid[0][j] == 1) {
                hasBlock = true;
            }
            dp[0][j] = hasBlock ? 0 : 1;
        }

        for(int i = 1; i < m; i++) {
            for(int j = 1; j < n; j++) {
                dp[i][j] = obstacleGrid[i][j] == 1 ? 0 : dp[i - 1][j] + dp[i][j - 1];
            }
        }

        return dp[m - 1][n - 1];
    }

    /**
     * 整数拆分
     * 核心是想出这题的递推公式
     */
    public int integerBreak(int n) {
        // 可拆分的整数的最大乘积
        int[] dp = new int[n + 1];

        if (n == 3) {
            return 2;
        }
        else if (n == 2) {
            return 1;
        }

        // 拆与不拆的最大值
        // dp[i] = Math.max(dp[i - j] * j, (i - j) * j)
        dp[2] = 1;
        
        for(int i = 3; i < n + 1; i++) {
            // i = 4 ,j = 2
            for(int j = 0; j < i; j++) {
                dp[i] = Math.max(dp[i], Math.max(dp[i - j] * j, (i - j) * j));
            }
        }

        return dp[n];
    }

    /**
     * 不同的二叉搜索树
     * 动归解法
     * 没做出来，关键是没有想出来递推公式
     */
    public int numTreesWithDP(int n) {
        //初始化 dp 数组
        int[] dp = new int[n + 1];
        //初始化0个节点和1个节点的情况
        dp[0] = 1;
        dp[1] = 1;
        for (int i = 2; i <= n; i++) {
            for (int j = 1; j <= i; j++) {
                //对于第i个节点，需要考虑1作为根节点直到i作为根节点的情况，所以需要累加
                //一共i个节点，对于根节点j时,左子树的节点个数为j-1，右子树的节点个数为i-j
                dp[i] += dp[j - 1] * dp[i - j];
            }
        }
        return dp[n];
    }

    Map<Integer, Integer> map = new HashMap<>();

    /**
     * 不同的二叉搜索树
     * 记忆化递归
     * 这个感觉更合理，更容易想到
     * 就是对于一个数组，每个元素都有可能被当成头节点
     */
    public int numTrees(int n) {
        //如果只有0，或者1个节点，则可能的子树情况为1种
        if (n == 0 || n == 1){
            return 1;
        }

        if (map.containsKey(n)){
            return map.get(n);
        }

        int count = 0;
        for (int i = 1; i <= n; i++) {
            //当用i这个节点当做根节点时

            //左边有多少种子树
            int leftNum = numTrees(i-1);

            //右边有多少种子树
            int rightNum = numTrees(n-i);

            //乘起来就是当前节点的子树个数
            count+=leftNum*rightNum;
        }

        map.put(n,count);

        return count;
    }

    /**
     * 01背包理论基础
     */
    public static int zeroOnePackageProblemBase(int num, int space, int[] value, int[] cost) {
        // 只考虑i + 1个物品，容量为j时的能装的最大价值
        int[][] dp = new int[cost.length][space + 1];

        for(int i = 0; i < cost.length; i++) {
            dp[i][0] = 0;
        }

        for(int j = 0; j <= space; j++) {
            dp[0][j] = j >= cost[0] ? value[0] : 0;
        }

        // 放或者不放
        // dp[i][j] = Math.max(dp[i - 1][j - cost[i]] + value[i], dp[i - 1][j])
        for(int i = 1; i < cost.length; i++) {
            for(int j = 0; j < space + 1; j++) {
                dp[i][j] = j - cost[i] >= 0 ? Math.max(dp[i - 1][j - cost[i]] + value[i], dp[i - 1][j]) : dp[i - 1][j];
            }
        }

        return dp[cost.length - 1][space];        
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int num = sc.nextInt();
        int space = sc.nextInt();
        // 输入并拆分为字符串数组
        String[] valueStr = sc.nextLine().trim().split("\\s+"); // 去掉首尾空格并按一个或多个空格分割
        String[] costStr = sc.nextLine().trim().split("\\s+");

        
        int[] cost = Arrays.stream(costStr)
                           .filter(s -> !s.isEmpty())
                           .mapToInt(Integer::parseInt)
                           .toArray();

                           // 将字符串数组转换为整数数组，过滤空值
        int[] value = Arrays.stream(valueStr)
                            .filter(s -> !s.isEmpty()) // 过滤空字符串
                            .mapToInt(Integer::parseInt)
                            .toArray();
        System.out.println(zeroOnePackageProblemBase(num, space, value, cost));
    }

    /**
     * 01背包理论基础（滚动数组）
     * 0-1背包问题滚动数组遍历空间时为什么需要从后往前。
     * 因为一维数组本质是在二维数组的基础上将i-1层赋值给i层，j的是有i-1层j-1推来的，
     * 如果从前往后就会使后面 的j用的不是i-1层的j-1而是i层的j-1
     */
    public static int zeroOnePackageProblemBaseRolling(int num, int space, int[] value, int[] cost) {
        // dp[j] 表示容量为 j 时能获得的最大价值
        int[] dp = new int[space + 1];

        // 初始化 dp 数组，dp[0] = 0，因为当背包容量为 0 时，最大价值为 0
        for (int j = 0; j <= space; j++) {
            dp[j] = 0;
        }

        // 从第 1 个物品开始逐个处理
        for (int i = 0; i < num; i++) {
            // 从后向前遍历更新 dp 数组，确保每个状态只被更新一次
            for (int j = space; j >= cost[i]; j--) {
                dp[j] = Math.max(dp[j], dp[j - cost[i]] + value[i]);
            }
        }

        return dp[space];  // 返回容量为 space 时能获得的最大价值
    }

    /**
     * 分割等和子集
     * 就像背包问题里的物品无非取或不取两种状态
     * 这里的子集对于每个元素而言也是加入与不加入两种状态
     */
    public boolean canPartition(int[] nums) {
        // 首先计算数组总和
        int totalSum = 0;
        for (int num : nums) {
            totalSum += num;
        }

        // 如果总和为奇数，不可能分割成两部分相等
        if (totalSum % 2 != 0) {
            return false;
        }

        // 目标是找到一部分的和为 totalSum / 2
        int target = totalSum / 2;

        int len = nums.length;
        
        // 只考虑前i个数能否和为j+1
        boolean[][] dp = new boolean[len][target + 1];
        
        for(int i = 0; i < len; i++) {
            dp[i][0] = false;
        }

        for(int j = 1; j < target + 1; j++) {
            dp[0][j] = nums[0] == j;
        }
        
        // dp[i][j] = dp[i - 1][j] || dp[i - 1][j - nums[i]]
        for(int i = 1; i < len; i++) {
            for(int j = 1; j < target + 1; j++) {
                dp[i][j] = (j - nums[i] >= 0) ? dp[i - 1][j] || dp[i - 1][j - nums[i]] : dp[i - 1][j];
            }
        }

        return dp[len - 1][target];
    }

    /**
     * 分割等和子集
     * 就像背包问题里的物品无非取或不取两种状态
     * 这里的子集对于每个元素而言也是加入与不加入两种状态
     * 这题如果用深度优先+剪枝会很快
     */
    public boolean canPartitionRolling(int[] nums) {
        // 首先计算数组总和
        int totalSum = 0;
        for (int num : nums) {
            totalSum += num;
        }

        // 如果总和为奇数，不可能分割成两部分相等
        if (totalSum % 2 != 0) {
            return false;
        }

        // 目标是找到一部分的和为 totalSum / 2
        int target = totalSum / 2;

        int len = nums.length;
        
        // 只考虑前i个数能否和为j+1
        boolean[] dp = new boolean[target + 1];
        
        // 这里初始化就是考虑原来二维数组的时候i=0的情况
        for(int j = 0; j < target + 1; j++) {
            dp[j] = j == nums[0];
        }
        
        // 这里i必须从1开始，不然会覆盖初始化的结果
        // dp[i][j] = dp[i - 1][j] || dp[i - 1][j - nums[i]]
        for(int i = 1; i < len; i++) {
            for(int j = target; j >= 1; j--) {
                dp[j] = (j - nums[i] >= 0) ? dp[j] || dp[j - nums[i]] : dp[j];
            }
        }

        return dp[target];
    }

    /**
     * 最后一块石头的重量II
     * 关键是转化为0-1背包问题
     */
    public int lastStoneWeightII(int[] stones) {
        int sum = 0;
        for (int s : stones) {
            sum += s;
        }

        int target = sum / 2;
        
        //初始化，dp[i][j]为可以放0-i物品，背包容量为j的情况下背包中的最大价值
        int[][] dp = new int[stones.length][target + 1];

        //dp[i][0]默认初始化为0
        //dp[0][j]取决于stones[0]
        for (int j = stones[0]; j <= target; j++) {
            dp[0][j] = stones[0];
        }

        for (int i = 1; i < stones.length; i++) {
            for (int j = 1; j <= target; j++) {
                if (j >= stones[i]) {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][j - stones[i]] + stones[i]);
                } else {
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }
        return (sum - dp[stones.length - 1][target]) - dp[stones.length - 1][target];
    }
}
