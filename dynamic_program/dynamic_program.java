package dynamic_program;

import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import doublepointer.doublepointer;
import stack_and_queue.stack_and_queue;
import string.string;

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
        for (int i = 2; i <= n; i++) {
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
        for (int i = 3; i < n + 1; i++) {
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

        for (int i = 2; i < step.length; i++) {
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

        for (int i = 0; i < m; i++) {
            dp[i][0] = 1;
        }

        for (int j = 0; j < n; j++) {
            dp[0][j] = 1;
        }

        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
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
        for (int i = 0; i < m; i++) {
            if (obstacleGrid[i][0] == 1) {
                hasBlock = true;
            }
            dp[i][0] = hasBlock ? 0 : 1;
        }

        hasBlock = false;
        for (int j = 0; j < n; j++) {
            if (obstacleGrid[0][j] == 1) {
                hasBlock = true;
            }
            dp[0][j] = hasBlock ? 0 : 1;
        }

        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
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
        } else if (n == 2) {
            return 1;
        }

        // 拆与不拆的最大值
        // dp[i] = Math.max(dp[i - j] * j, (i - j) * j)
        dp[2] = 1;

        for (int i = 3; i < n + 1; i++) {
            // i = 4 ,j = 2
            for (int j = 0; j < i; j++) {
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
        // 初始化 dp 数组
        int[] dp = new int[n + 1];
        // 初始化0个节点和1个节点的情况
        dp[0] = 1;
        dp[1] = 1;
        for (int i = 2; i <= n; i++) {
            for (int j = 1; j <= i; j++) {
                // 对于第i个节点，需要考虑1作为根节点直到i作为根节点的情况，所以需要累加
                // 一共i个节点，对于根节点j时,左子树的节点个数为j-1，右子树的节点个数为i-j
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
        // 如果只有0，或者1个节点，则可能的子树情况为1种
        if (n == 0 || n == 1) {
            return 1;
        }

        if (map.containsKey(n)) {
            return map.get(n);
        }

        int count = 0;
        for (int i = 1; i <= n; i++) {
            // 当用i这个节点当做根节点时

            // 左边有多少种子树
            int leftNum = numTrees(i - 1);

            // 右边有多少种子树
            int rightNum = numTrees(n - i);

            // 乘起来就是当前节点的子树个数
            count += leftNum * rightNum;
        }

        map.put(n, count);

        return count;
    }

    /**
     * 01背包理论基础
     * 背包问题的一大用处是可以找部分和为target
     */
    public static int zeroOnePackageProblemBase(int num, int space, int[] value, int[] cost) {
        // 只考虑i + 1个物品，容量为j时的能装的最大价值
        int[][] dp = new int[cost.length][space + 1];

        for (int i = 0; i < cost.length; i++) {
            dp[i][0] = 0;
        }

        for (int j = 0; j <= space; j++) {
            dp[0][j] = j >= cost[0] ? value[0] : 0;
        }

        // 放或者不放
        // dp[i][j] = Math.max(dp[i - 1][j - cost[i]] + value[i], dp[i - 1][j])
        for (int i = 1; i < cost.length; i++) {
            for (int j = 0; j < space + 1; j++) {
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

        return dp[space]; // 返回容量为 space 时能获得的最大价值
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

        for (int i = 0; i < len; i++) {
            dp[i][0] = false;
        }

        for (int j = 1; j < target + 1; j++) {
            dp[0][j] = nums[0] == j;
        }

        // dp[i][j] = dp[i - 1][j] || dp[i - 1][j - nums[i]]
        for (int i = 1; i < len; i++) {
            for (int j = 1; j < target + 1; j++) {
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
        for (int j = 0; j < target + 1; j++) {
            dp[j] = j == nums[0];
        }

        // 这里i必须从1开始，不然会覆盖初始化的结果
        // dp[i][j] = dp[i - 1][j] || dp[i - 1][j - nums[i]]
        for (int i = 1; i < len; i++) {
            for (int j = target; j >= 1; j--) {
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

        // 初始化，dp[i][j]为可以放0-i物品，背包容量为j的情况下背包中的最大价值
        int[][] dp = new int[stones.length][target + 1];

        // dp[i][0]默认初始化为0
        // dp[0][j]取决于stones[0]
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
        for (int num : nums) {
            sum += num;
        }

        int x = (target + sum);

        if (x < 0 || x % 2 == 1) {
            return 0;
        }

        x /= 2;

        // 只考虑[0, i]的物品的情况下，装满容量为j的背包的方法数
        int[][] dp = new int[nums.length][x + 1];

        // 无非放入、不放入
        // dp[i][j] = dp[i - 1][j] + dp[i - 1][j - nums[i]]

        int numZero = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == 0)
                numZero++;
            dp[i][0] = (int) Math.pow(2.0, numZero);
        }

        for (int j = 1; j < x + 1; j++) {
            dp[0][j] = j == nums[0] ? 1 : 0;
        }

        for (int i = 1; i < nums.length; i++) {
            for (int j = 1; j < x + 1; j++) {
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
        int[] weight = { 1, 3, 4 };
        int[] value = { 15, 20, 30 };
        int bagWeight = 4;
        int[] dp = new int[bagWeight + 1];
        // 遍历物品
        for (int i = 0; i < weight.length; i++) {
            // 遍历背包容量
            for (int j = weight[i]; j <= bagWeight; j++) {
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

        for (i = 0; i < coins.length; i++) {
            dp[i][0] = 1;
        }

        for (j = 1; j < amount + 1; j++) {
            dp[0][j] = j % coins[0] == 0 ? 1 : 0;
        }

        // 放入或不放入
        for (i = 1; i < coins.length; i++) {
            for (j = 0; j < amount + 1; j++) {
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

        for (i = 1; i < target + 1; i++) {
            // 计算dp[i]以各个数结尾的排列数，加上去
            for (int j = 0; j < nums.length; j++) {
                if (i >= nums[j]) {
                    dp[i] += dp[i - nums[j]];
                }
            }
        }

        return dp[target];
    }

    private void printArray(int[] array) {
        for (int e : array) {
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

        for (i = 1; i < n + 1; i++) {
            for (j = 0; j <= m; j++) {
                if (i >= j) {
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

        for (j = 1; j < amount + 1; j++) {
            for (i = 0; i < len; i++) {
                if (j >= coins[i]) {
                    dp[j] = Math.min(dp[j], dp[j - coins[i]] + 1);
                }
            }
        }

        return dp[amount] == amount + 1 ? -1 : dp[amount];
    }

    /**
     * 完全平方数
     * mua的，我做出来了
     * 转化为完全背包问题
     * 只是我完全背包问题还不够熟练
     */
    public int numSquares(int n) {
        // 找到比n小的最大的完全平方数
        List<Integer> squareNum = new ArrayList<>();

        int num = 1;
        while (num * num <= n) {
            squareNum.add(num);
            num++;
        }

        // 出来 num * num > n
        int size = squareNum.size();

        // 考虑前i个数的和为j最小完全平方数个数
        int[][] dp = new int[size][n + 1];

        int i = 0, j = 0;

        for (i = 0; i < size; i++) {
            dp[i][0] = 0;
        }

        for (j = 1; j <= n; j++) {
            dp[0][j] = j;
        }

        for (i = 1; i < size; i++) {
            for (j = 1; j <= n; j++) {
                dp[i][j] = dp[i - 1][j];
                int t = squareNum.get(i);
                t *= t;
                if (j - t >= 0) {
                    dp[i][j] = Math.min(dp[i][j], dp[i][j - t] + 1);
                }
            }
        }

        return dp[size - 1][n];
    }

    /**
     * 完全平方数
     * 一位背包版本
     */
    public int numSquaresWithOneDimension(int n) {
        int[] dp = new int[n + 1];
        Arrays.fill(dp, n);
        dp[0] = 0;
        for (int i = 1; i * i <= n; i++) {
            for (int j = i * i; j <= n; j++) {
                dp[j] = Math.min(dp[j], dp[j - i * i] + 1);
            }
        }
        return dp[n];
    }

    /**
     * 单词拆分
     * 没做出来
     * 这题的dp解法很巧妙，不好想
     * 首先就是dp数组的含义：长度为i的子串可否被字典中的词组成
     * 然后就是递推，当前下标j和长度i之间的词若在字典中，则dp[j]为true
     */
    public boolean wordBreak(String s, List<String> wordDict) {
        boolean[] dp = new boolean[s.length() + 1];
        dp[0] = true;
        for (int i = 1; i <= s.length(); i++) {
            for (String word : wordDict) {
                int len = word.length();
                if (i >= len && dp[i - len] && word.equals(s.substring(i - len, i))) {
                    dp[i] = true;
                    break;
                }
            }
        }
        return dp[s.length()];
    }

    /**
     * 打家劫舍
     * 一次过了，击败100%
     */
    public int rob(int[] nums) {
        // 2 7 9 3 1
        // 1
        // 1
        // 一开始一定会在1或者2号中偷一个
        int len = nums.length;

        if (len == 1) {
            return nums[0];
        } else if (len == 2) {
            return Math.max(nums[0], nums[1]);
        }

        // 只考虑前i个房屋的时的偷窃最大值
        int[] dp = new int[len];
        int i = 0;

        dp[0] = nums[0];
        dp[1] = Math.max(nums[0], nums[1]);

        for (i = 2; i < len; i++) {
            dp[i] = Math.max(dp[i - 1], dp[i - 2] + nums[i]);
        }

        return dp[len - 1];
    }

    /**
     * 打家劫舍II
     * 在上一题的基础上首尾连接了
     * 没做出来，但是随想录的题解很巧妙：
     * 环状排列
     * 意味着第一个房子和最后一个房子中，只能选择一个偷窃，因此可以把此环状排列房间问题约化为两个单排排列房间子问题
     * 就是考虑两个情况
     * 1. 不考虑最后一个元素按照上题思路求解
     * 2. 不考虑第一个元素，按照上题思路求解
     * 然后取上两者中较大者
     * 按照随想录题解的解法实现了，击败100%
     */
    public int rob2(int[] nums) {
        int len = nums.length;
        if (len == 1) {
            return nums[0];
        } else if (len == 2) {
            return Math.max(nums[0], nums[1]);
        }

        // 2 3 2
        // 2 7 9 3 1
        // 1 2 7 9 3
        int t1 = robber(nums, 0, nums.length - 2);
        int t2 = robber(nums, 1, nums.length - 1);
        System.out.println("t1 = " + t1);
        System.out.println("t2 = " + t2);
        return Math.max(t1, t2);
    }

    /**
     * 抢劫器(bushi)
     */
    private int robber(int[] nums, int begin, int end) {
        // 2 7 9 3 1
        // 1
        // 1
        // 一开始一定会在1或者2号中偷一个
        int len = end - begin + 1;

        if (len == 1) {
            return nums[begin];
        } else if (len == 2) {
            return Math.max(nums[begin], nums[begin + 1]);
        }

        // 只考虑前i个房屋的时的偷窃最大值
        int[] dp = new int[len];
        int i = 0;

        dp[0] = nums[begin];
        dp[1] = Math.max(nums[begin], nums[begin + 1]);

        for (i = 2; i < len; i++) {
            dp[i] = Math.max(dp[i - 1], dp[i - 2] + nums[begin + i]);
        }

        return dp[len - 1];
    }

    /**
     * 某节点被选中时的最大权值和
     * 当一个节点o被选中时，
     * f(o) = g(l) + g(r) + o.val
     */
    Map<TreeNode, Integer> f = new HashMap<TreeNode, Integer>();

    /**
     * 某节点未被选中时的最大权值和
     * 当一个节点o未被选中时，
     * g(o) = max(f(l), g(l)) + max(f(r), g(r))
     */
    Map<TreeNode, Integer> g = new HashMap<TreeNode, Integer>();

    /**
     * 打家劫舍 III
     * 还是没有想出来，感觉是我跳过了二叉树的问题
     * 树形DP
     * 这题首先是递推公式没那么好想，其次是还要对树的遍历够熟练
     */
    public int rob3(TreeNode root) {
        dfs(root);
        return Math.max(f.getOrDefault(root, 0), g.getOrDefault(root, 0));
    }

    public void dfs(TreeNode node) {
        if (node == null) {
            return;
        }

        // 从这里可以看出来是后序遍历
        dfs(node.left);
        dfs(node.right);
        f.put(node, node.val + g.getOrDefault(node.left, 0) + g.getOrDefault(node.right, 0));
        g.put(node, Math.max(f.getOrDefault(node.left, 0), g.getOrDefault(node.left, 0))
                + Math.max(f.getOrDefault(node.right, 0), g.getOrDefault(node.right, 0)));
    }

    public class TreeNode {
        int val; // 节点值
        TreeNode left; // 左子节点
        TreeNode right; // 右子节点

        // 默认构造函数
        TreeNode() {
        }

        // 带一个参数的构造函数，用于设置节点值
        TreeNode(int val) {
            this.val = val;
        }

        // 带三个参数的构造函数，用于设置节点值及左右子节点
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    /**
     * 买卖股票的最佳时机
     */
    public int maxProfit(int[] prices) {
        int len = prices.length;

        // 到第i天持有1与不持有0股票的最大利润
        int[][] dp = new int[len][2];

        dp[0][1] = -prices[0];
        dp[0][0] = 0;

        // 二维DP，还有一点就是不能在买入前卖出
        for (int i = 1; i < len; i++) {
            // 本题限制了交易次数是1，所以是和-prices[i]比较，如果不限制交易次数，这里比较的应该为dp[i - 1][0] - prices[i]
            dp[i][1] = Math.max(-prices[i], dp[i - 1][1]);
            dp[i][0] = Math.max(dp[i - 1][1] + prices[i], dp[i - 1][0]);
        }

        return dp[len - 1][0];
    }

    /**
     * 买卖股票的最佳时机II
     * 其实上一题我把这一题的答案做出来了...
     */
    public int maxProfit2(int[] prices) {
        int len = prices.length;

        // 到第i天持有1与不持有0股票的最大利润
        int[][] dp = new int[len][2];

        dp[0][1] = -prices[0];
        dp[0][0] = 0;

        // 二维DP，还有一点就是不能在买入前卖出
        for (int i = 1; i < len; i++) {
            dp[i][1] = Math.max(dp[i - 1][0] - prices[i], dp[i - 1][1]);
            dp[i][0] = Math.max(dp[i - 1][1] + prices[i], dp[i - 1][0]);
        }

        return dp[len - 1][0];
    }

    /**
     * 买卖股票的最佳时机III
     * 股票系列的hard题...
     * 与上题的区别就是，限制了交易的次数
     * 上次已经知道了限一次的和不限次的，那么现在限两次的要怎么办
     * 这边没想出来，但是看了灵茶山艾府的视频，应该是要加一个维读来标识交易次数
     * 我的建议是DP先刷到这里，去刷回溯 + 二叉树吧
     */
    public int maxProfit3(int[] prices) {
        if (prices == null || prices.length < 2)
            return 0;

        int len = prices.length;
        int k = 2; // 最多可以完成的交易次数

        // dp[i][j][l] 表示到第i天为止，是否持有股票(j=0表示不持有, j=1表示持有)，并且已经完成了l次交易的最大利润
        int[][][] dp = new int[len][2][k + 1];

        // 初始化第0天的状态
        for (int j = 0; j <= k; j++) { // 对于所有可能的交易次数
            dp[0][0][j] = 0; // 第0天不持有股票的最大利润为0
            dp[0][1][j] = -prices[0]; // 第0天持有股票的最大利润为-prices[0]
        }

        for (int i = 1; i < len; i++) {
            for (int j = 0; j <= k; j++) { // 遍历交易次数
                if (j > 0) { // 如果交易次数大于0
                    dp[i][1][j] = Math.max(dp[i - 1][0][j - 1] - prices[i], dp[i - 1][1][j]); // 今天买入/继续持有
                    dp[i][0][j] = Math.max(dp[i - 1][1][j] + prices[i], dp[i - 1][0][j]); // 今天卖出/继续不持有
                } else { // 如果交易次数为0，则只能保持不持有状态
                    dp[i][0][j] = dp[i - 1][0][j];
                    dp[i][1][j] = Math.max(-prices[i], dp[i - 1][1][j]);
                }
            }
        }

        // 返回最后一天不持有股票的所有可能交易次数下的最大值
        int maxProfit = 0;
        for (int j = 0; j <= k; j++) {
            maxProfit = Math.max(maxProfit, dp[len - 1][0][j]);
        }
        return maxProfit;
    }

    /**
     * 买卖股票的最佳时机III
     * hard
     * 评论区还有一个逆天解法
     */
    public int maxProfit3WithAwesomeSolution(int[] prices) {
        // 初始化buy和sell数组，分别为第j次交易持有/不持有股票的最大利润
        int[] buy = new int[3];
        int[] sell = new int[3];

        Arrays.fill(buy, Integer.MIN_VALUE); // 将buy数组的值初始化为Integer.MIN_VALUE
        sell[0] = 0; // 第0次交易后不持有股票的利润为0

        for (int price : prices) {
            for (int j = 1; j < 3; j++) {
                // 更新第j次买入后的最大利润
                if (j == 1) {
                    buy[j] = Math.max(buy[j], -price); // 第一次买入时，直接减去价格
                } else {
                    buy[j] = Math.max(buy[j], sell[j - 1] - price); // 后续买入基于上一次卖出的最大利润
                }
                // 更新第j次卖出后的最大利润
                sell[j] = Math.max(sell[j], buy[j] + price);
            }
        }

        // 返回最多进行两次交易后的最大利润
        return sell[2];
    }

    /**
     * 买卖股票的最佳时机IV
     * 限制了k次交易次数
     */
    public int maxProfit(int k, int[] prices) {
        if (prices == null || prices.length < 2)
            return 0;

        int len = prices.length;

        // dp[i][j][l] 表示到第i天为止，是否持有股票(j=0表示不持有, j=1表示持有)，并且已经完成了l次交易的最大利润
        int[][][] dp = new int[len][2][k + 1];

        // 初始化第0天的状态
        for (int j = 0; j <= k; j++) {
            // 第0天不持有股票的最大利润为0
            dp[0][0][j] = 0;
            // 第0天持有股票的最大利润为-prices[0]
            dp[0][1][j] = -prices[0];
        }

        for (int i = 1; i < len; i++) {
            for (int j = 0; j <= k; j++) {
                if (j > 0) {
                    // 今天买入/继续持有
                    dp[i][1][j] = Math.max(dp[i - 1][0][j - 1] - prices[i], dp[i - 1][1][j]);
                    // 今天卖出/继续不持有
                    dp[i][0][j] = Math.max(dp[i - 1][1][j] + prices[i], dp[i - 1][0][j]);
                } else {
                    // 如果交易次数为0，则只能保持不持有状态
                    dp[i][0][j] = dp[i - 1][0][j];
                    dp[i][1][j] = Math.max(-prices[i], dp[i - 1][1][j]);
                }
            }
        }
        // 返回最后一天不持有股票的所有可能交易次数下的最大值
        int maxProfit = 0;
        for (int j = 0; j <= k; j++) {
            maxProfit = Math.max(maxProfit, dp[len - 1][0][j]);
        }
        return maxProfit;
    }

    /**
     * 最佳买卖股票时机含冷冻期
     * 相当II加了一天冷冻期
     * 稍微修改一下，过了，主要是意识到冷冻期的递归公式
     * 还有在i == 1 时候的公式判断，即要么是第1天买了股票，要么是第二天买了股票
     */
    public int maxProfitWithFreezingPeriod(int[] prices) {
        int len = prices.length;

        // 到第i天持有1与不持有0股票的最大利润
        int[][] dp = new int[len][2];

        dp[0][1] = -prices[0];
        dp[0][0] = 0;

        for (int i = 1; i < len; i++) {
            dp[i][1] = i >= 2 ? Math.max(dp[i - 2][0] - prices[i], dp[i - 1][1])
                    : Math.max(dp[i - 1][1], dp[i - 1][0] - prices[i]);
            dp[i][0] = Math.max(dp[i - 1][1] + prices[i], dp[i - 1][0]);
        }

        return dp[len - 1][0];
    }

    /**
     * 买卖股票的最佳时机含手续费
     * 呃，也是微调一下递归公式就通过了
     */
    public int maxProfitWithFees(int[] prices, int fee) {
        int len = prices.length;

        // 到第i天持有1与不持有0股票的最大利润
        int[][] dp = new int[len][2];

        dp[0][1] = -prices[0];
        dp[0][0] = 0;

        for (int i = 1; i < len; i++) {
            dp[i][1] = Math.max(dp[i - 1][0] - prices[i], dp[i - 1][1]);
            dp[i][0] = Math.max(dp[i - 1][1] + prices[i] - fee, dp[i - 1][0]);
        }

        return dp[len - 1][0];
    }

    /**
     * 最长递增子序列
     * 过了，但是时间复杂度并不好
     */
    public int lengthOfLIS(int[] nums) {
        // 以nums[i]结尾的最大递增子序列长度
        int[] dp = new int[nums.length];
        dp[0] = 1;
        int max = dp[0];
        for (int i = 1; i < nums.length; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[i] > nums[j]) {
                    dp[i] = Math.max(dp[j] + 1, dp[i]);
                }
            }
            max = Math.max(max, dp[i]);
        }
        return max;
    }

    /**
     * 最长连续递增序列
     */
    public int findLengthOfLCIS(int[] nums) {
        // 以nums[i]结尾的最大递增子序列长度
        int[] dp = new int[nums.length];
        dp[0] = 1;
        Arrays.fill(dp, 1);
        int max = 1;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] > nums[i - 1]) {
                dp[i] = dp[i - 1] + 1;
            }
            max = Math.max(max, dp[i]);
        }
        return max;
    }

    /**
     * 最长重复子数组
     * 没有一点思路，但是看了答案之后...
     * 恍然大悟！！！
     * 对于这样的公共部分，一般要二维数组
     * 我的二维DP能力真是太弱了
     */
    public int findLength(int[] nums1, int[] nums2) {
        // 呃，首先是不是得找到公共子序列
        // 1 2 3 2 1
        // 3 2 1 4 7
        // 没有一点思路
        int result = 0;
        int[][] dp = new int[nums1.length + 1][nums2.length + 1];

        for (int i = 1; i < nums1.length + 1; i++) {
            for (int j = 1; j < nums2.length + 1; j++) {
                if (nums1[i - 1] == nums2[j - 1]) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                    result = Math.max(result, dp[i][j]);
                }
            }
        }

        return result;
    }

    /**
     * 最长公共子序列
     * 没做出来，难点是想出dp数组的定义
     * 有了dp数组的定义以后，然后就是根据i和j字符的相等与否来判断到底最长公共子序列应该为哪一个
     * （在不相等时做dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1])赋值处理，是本题与上一题最大的变化点），
     * 或者是+1
     */
    public int longestCommonSubsequence(String text1, String text2) {
        // dp标识前缀的最长公共子序列长度
        int[][] dp = new int[text1.length() + 1][text2.length() + 1]; // 先对dp数组做初始化操作
        for (int i = 1; i <= text1.length(); i++) {
            char c1 = text1.charAt(i - 1);
            for (int j = 1; j <= text2.length(); j++) {
                char c2 = text2.charAt(j - 1);
                if (c1 == c2) { // 开始列出状态转移方程
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        return dp[text1.length()][text2.length()];
    }

    /**
     * 不相交的线
     * 看样子也是二维数组的题目
     * 关键是确定DP数组的定义，然后根据如何相交的判断列出递推公式开始动归
     * wc 其实本质就是最长公共子序列
     */
    public int maxUncrossedLines(int[] nums1, int[] nums2) {
        // dp标识前缀的最长公共子序列长度
        int[][] dp = new int[nums1.length + 1][nums2.length + 1]; // 先对dp数组做初始化操作
        for (int i = 1; i <= nums1.length; i++) {
            int n1 = nums1[i - 1];
            for (int j = 1; j <= nums2.length; j++) {
                int n2 = nums2[j - 1];
                if (n1 == n2) { // 开始列出状态转移方程
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        return dp[nums1.length][nums2.length];
    }

    /**
     * 最大子序和
     * 9min oc
     * 核心是dp数组的定义，以i结尾的子数组的最大子序和
     * 其实需要的只是前一个变量，这样的话就可以节省空间复杂度，省去分配空间的时间复杂度了
     */
    public int maxSubArray(int[] nums) {
        int len = nums.length;
        // 考虑0到i元素且以i元素结尾的最大子序和
        int[] dp = new int[len];
        dp[0] = nums[0];
        int max = dp[0];
        for (int i = 1; i < len; i++) {
            if (dp[i - 1] <= 0) {
                dp[i] = nums[i];
            } else {
                dp[i] = dp[i - 1] + nums[i];
            }
            max = Math.max(max, dp[i]);
        }
        return max;
    }

    /**
     * 判断子序列
     * 这个回溯超时了
     */
    public boolean isSubsequenceTimeout(String s, String t) {
        if (s.equals("")) {
            return true;
        }
        return isSubsequence(t, s, new StringBuilder(), 0);
    }

    private boolean isSubsequence(String s, String t, StringBuilder sb, int index) {
        if (index == s.length()) {
            return false;
        }
        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                if (isSubsequence(s, t, sb, index + 1)) {
                    return true;
                }
            } else {
                sb.append(s.charAt(index));
                if (sb.toString().equals(t) || isSubsequence(s, t, sb, index + 1)) {
                    return true;
                }
                sb.setLength(sb.length() - 1);
            }
        }
        return false;
    }

    /**
     * 判断子序列
     * 6min ac
     * 思路是逐个扫t，遇到一个s的字符就点亮，同时游标+1
     * 本题是编辑距离的入门题，所以了解DP解法十分有必要
     */
    public boolean isSubsequence(String s, String t) {
        if (s.equals("")) {
            return true;
        } else if (t.equals("")) {
            return false;
        }
        int index = 0;
        int len = t.length();
        for (int i = 0; i < len; i++) {
            if (t.charAt(i) == s.charAt(index)) {
                index++;
                if (index == s.length()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断子序列
     * DP解法
     */
    public boolean isSubsequenceWithDP(String s, String t) {
        int sLen = s.length();
        int tLen = t.length();
        // 以下标i-1为结尾的字符串s，和以下标j-1为结尾的字符串t，相同子序列的长度为dp[i][j]
        int[][] dp = new int[sLen + 1][tLen + 1];
        for (int i = 1; i <= sLen; i++) {
            for (int j = 1; j <= tLen; j++) {
                if (s.charAt(i - 1) == t.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = dp[i][j - 1];
                }
            }
        }
        return dp[sLen][tLen] == sLen;
    }

    /**
     * 不同的子序列
     * 思路是对的，但是没用DP，我这里用的递归的思路，也没有成功实现
     * 就是，统计t中每个字符在s中出现的索引，组成一个列表
     * 然后从t的第一个字符开始，递归遍历后面的字符的索引大于当前的位置的数量加起来
     * 说的有点抽象
     * 呃，结果超时了
     * 如果记忆化递归呢？
     */
    public int numDistinct(String s, String t) {
        // 记录每个字符在s中的索引位置
        Map<Character, List<Integer>> charIndexsMap = new HashMap<>();
        Map<Character, Boolean> needCharMap = new HashMap<>();
        char[] tChars = t.toCharArray();

        // 标记t中需要的字符
        for (char ch : tChars) {
            needCharMap.put(ch, true);
        }

        // 将s中需要的字符的索引位置加入map
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (needCharMap.get(ch) == null || needCharMap.get(ch)) {
                List<Integer> list = charIndexsMap.getOrDefault(ch, new ArrayList<>());
                list.add(i);
                charIndexsMap.put(ch, list);
            }
        }

        // 从t的第一个字符开始，递归处理
        char firstCh = tChars[0];
        List<Integer> firstCharIndexes = charIndexsMap.get(firstCh);
        int result = 0;
        if (firstCharIndexes != null) {
            // 对第一个字符的每个位置进行递归处理
            for (Integer firstCharIndex : firstCharIndexes) {
                result += calcRec(charIndexsMap, t, 1, firstCharIndex);
            }
        }

        return result;
    }

    // 递归计算，index是t中当前字符的位置，pivot是s中当前匹配字符的位置
    private int calcRec(Map<Character, List<Integer>> charIndexsMap, String t, int index, int pivot) {
        // 如果t的所有字符都已经匹配完成
        if (index == t.length()) {
            return 1;
        }

        // 获取当前字符在t中的所有匹配位置
        char ch = t.charAt(index);
        List<Integer> indexs = charIndexsMap.get(ch);
        int count = 0;

        // 在s中找比pivot大（即在pivot位置之后）的字符位置
        for (Integer idx : indexs) {
            if (idx > pivot) {
                // 递归调用，寻找t中下一个字符的匹配
                count += calcRec(charIndexsMap, t, index + 1, idx);
            }
        }

        return count;
    }

    /**
     * 不同的子序列
     * 本题DP的难点在于：
     * DP数组的定义：以i-1为结尾的s子序列中出现以j-1为结尾的t的个数为dp[i][j]
     * 初始化要想清楚
     * 状态转移方程也要想清楚，区分点就是s[i - 1]是否要纳入匹配，即选择与不选择
     */
    public int numDistinctWithDp(String s, String t) {
        // 以i-1为结尾的s子序列中出现以j-1为结尾的t的个数为dp[i][j]
        int[][] dp = new int[s.length() + 1][t.length() + 1];

        // 初始化
        for (int i = 0; i < s.length() + 1; i++) {
            dp[i][0] = 1;
        }

        for (int i = 1; i < s.length() + 1; i++) {
            for (int j = 1; j < t.length() + 1; j++) {
                if (s.charAt(i - 1) == t.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + dp[i - 1][j];
                } else {
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }

        return dp[s.length()][t.length()];
    }

    /**
     * 两个字符串的删除操作
     * 这题和直接求最大公共子序列好像是一个套路
     */
    public int minDistance(String word1, String word2) {
        int len1 = word1.length();
        int len2 = word2.length();

        // 以 i - 1元素结尾和j - 1元素结尾的最大子序列长度
        int[][] dp = new int[len1 + 1][len2 + 1];

        for (int i = 1; i < len1 + 1; i++) {
            for (int j = 1; j < len2 + 1; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }

        return len1 + len2 - 2 * dp[len1][len2];
    }

    /**
     * 编辑距离
     * 30min ac
     * 核心是dp数组的定义以及增删改的递归公式推导
     */
    public int minEditDistance(String word1, String word2) {
        int len1 = word1.length();
        int len2 = word2.length();
        // 以下标i-1为结尾的字符串word1，和以下标j-1为结尾的字符串word2，最近编辑距离为dp[i][j]
        int[][] dp = new int[len1 + 1][len2 + 1];
        for (int i = 0; i < len2 + 1; i++) {
            dp[0][i] = i;
        }
        for (int i = 1; i < len1 + 1; i++) {
            dp[i][0] = i;
        }

        for (int i = 1; i < len1 + 1; i++) {
            for (int j = 1; j < len2 + 1; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = min(dp[i - 1][j] + 1, dp[i - 1][j - 1] + 1, dp[i][j - 1] + 1);
                }
            }
        }
        return dp[len1][len2];
    }

    private int min(int a, int b, int c) {
        return a >= b ? Math.min(b, c) : Math.min(a, c);
    }

    /**
     * 回文子串
     */
    public int countSubstrings(String s) {

    }
}
