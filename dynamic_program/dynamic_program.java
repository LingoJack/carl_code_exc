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
     * 背包问题的一大用处是可以找部分和为target
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

    public static void main1(String[] args) {
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

    /**
     * 目标和
     * 这题的关键还是想着怎么转成背包问题
     * 既然这些nums里面取正数的数之和为x，取负数的数之和为-y
     * 假设存在等于target的，那么就是 x - y = target
     * x + y = nums数组之和 即 x + y = sum
     * 得到 x = (target + sum) / 2
     * 也就是从nums数组里面挑选和为x的组合数
     * 好难，除了想到转换的方法
     * 初始化也不好想
     */
    public int findTargetSumWays(int[] nums, int target) {
        int sum = 0;
        for(int num : nums) {
            sum += num;
        }

        int x = (target + sum);

        if (x < 0 || x % 2 == 1){
            return 0;
        }

        x /= 2;

        // 只考虑[0, i]的物品的情况下，装满容量为j的背包的方法数
        int[][] dp = new int[nums.length][x + 1];

        // 无非放入、不放入
        // dp[i][j] = dp[i - 1][j] + dp[i - 1][j - nums[i]]

        int numZero = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == 0) numZero++;
            dp[i][0] = (int) Math.pow(2.0, numZero);
        }

        for(int j = 1; j < x + 1; j++) {
            dp[0][j] = j == nums[0] ? 1 : 0;
        }

        for(int i = 1; i < nums.length; i++) {
            for(int j = 1; j < x + 1;j++){
                dp[i][j] = j >= nums[i] ? dp[i - 1][j] + dp[i - 1][j - nums[i]] : dp[i - 1][j];
            }
        }
        
        return dp[nums.length - 1][x];
    }

    /**
     * 一和零
     * DP问题的关键是把变量纳入dp数组，有时候需要一定的转化
     * 为什么这里题目要强调是二进制的字符串，说明不是0就是1这一点对于解题是有帮助的
     * 最大子集的大小
     * 最后还是没做出来，好难受
     * 这就是一个典型的01背包！ 只不过物品的重量有了两个维度而已...
     */
    public int findMaxForm(String[] strs, int m, int n) {
        // dp[i][j]表示i个0和j个1时的最大子集
        int[][] dp = new int[m + 1][n + 1];
        int oneNum, zeroNum;
        for (String str : strs) {
            oneNum = 0;
            zeroNum = 0;
            for (char ch : str.toCharArray()) {
                if (ch == '0') {
                    zeroNum++;
                } else {
                    oneNum++;
                }
            }
            // 倒序遍历，一维数组避免重复计算
            for (int i = m; i >= zeroNum; i--) {
                for (int j = n; j >= oneNum; j--) {
                    dp[i][j] = Math.max(dp[i][j], dp[i - zeroNum][j - oneNum] + 1);
                }
            }
        }
        return dp[m][n];
    }

    /**
     * 完全背包问题示例
     * 携带研究材料
     * n 材料种类
     * v 背包容量
     */
    public int maximizeResearchValue(int n, int v, int[][] materials) {
        int[] weight = {1, 3, 4};
        int[] value = {15, 20, 30};
        int bagWeight = 4;
        int[] dp = new int[bagWeight + 1];
        // 遍历物品
        for (int i = 0; i < weight.length; i++){ 
            // 遍历背包容量
            for (int j = weight[i]; j <= bagWeight; j++){ 
                dp[j] = Math.max(dp[j], dp[j - weight[i]] + value[i]);
            }
        }
        return dp[bagWeight];
    }

    /**
     * 零钱兑换II
     * 完全背包问题，需要注意的是初始化
     * 5555，这是时隔了这么久我第一道自己做出来的Medium难度的DP
     */
    public int change(int amount, int[] coins) {

        // 考虑前i枚硬币时金额总值为j的组合数
        int[][] dp = new int[coins.length][amount + 1];
        
        int i = 0, j = 0;

        for(i = 0; i < coins.length; i++) {
            dp[i][0] = 1;
        }

        for(j = 1; j < amount + 1; j++) {
            dp[0][j] = j % coins[0] == 0 ? 1 : 0;
        }

        // 放入或不放入
        for(i = 1; i < coins.length; i++) {
            for(j = 0; j < amount + 1; j++) {
                dp[i][j] = j - coins[i] >= 0 ? dp[i - 1][j] + dp[i][j - coins[i]] : dp[i - 1][j];
            }
        }

        return dp[coins.length - 1][amount];
    }

    /**
     * 组合总和 Ⅳ
     * 这里是求排列
     * 码的，这里属于是被代码随想录的解释带偏了
     * 随想录想强行想弄一个统一的理论来解释dp的排列组合解法，结果就是只有它自己觉得好理解了联系起来了合理了
     * 傻逼
     * 还是要结合力扣官方题解和评论区和随想录三方一起看，偏听则暗
     */
    public int combinationSum4(int[] nums, int target) {
        // 和为i的排列数
        int[] dp = new int[target + 1];
        int i = 0;
        
        // 对应以nums[i]结尾的排列
        // dp[i] += dp[i - nums[i]];
        
        dp[0] = 1;

        for(i = 1; i < target + 1; i++) {
            // 计算dp[i]以各个数结尾的排列数，加上去
            for(int j = 0; j < nums.length; j++) {
                if(i >= nums[j]) {
                    dp[i] += dp[i - nums[j]];
                }   
            }
        }

        return dp[target];
    }

    private void printArray(int[] array) {
        for(int e : array) {
            System.out.print(e + " ");
        }
        System.out.println();
    }

    /**
     * 爬楼梯（进阶版）
     * 一次过
     */
    public static int climbStairsUpgrade(int m, int n) {
        // 爬上j阶楼梯的方法数
        int[] dp = new int[n + 1];

        int i = 0;
        int j = 0;

        dp[0] = 1;

        for(i = 1; i < n + 1; i++) {
            for(j = 0; j <= m; j++) {
                if(i >= j) {
                    dp[i] += dp[i - j];
                }
            }
        }

        return dp[n];
    }

    public static void main2(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int m = sc.nextInt();
        System.out.println(climbStairsUpgrade(m, n));
    }

    /**
     * 零钱兑换
     * 卡在初始化赋值
     */
    public int coinChange(int[] coins, int amount) {
        int len = coins.length;

        // 和为j的最小硬币数
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1);
        int i = 0, j = 0;

        dp[0] = 0;

        for(j = 1; j < amount + 1; j++) {
            for(i = 0; i < len; i++) {
                if (j >= coins[i]) {
                    dp[j] = Math.min(dp[j], dp[j - coins[i]] + 1);
                }
            }
        }
        
        return dp[amount] == amount + 1 ? -1 : dp[amount];
    }

    /**
     * 完全平方数
     */
    public int numSquares(int n) {
        
    }
}
