package greedy;

import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.tree.TreeNode;

public class greedy {
    /**
     * 分发饼干
     * g是需求值数组，s是供给数组
     * 贪心是类全凭感觉的玄学题
     * 感觉就是一个匹配度的问题，匹配度最高，浪费的就最小了
     */
    public int findContentChildren(int[] g, int[] s) {
        int childNum = g.length;
        Arrays.sort(g);
        Arrays.sort(s);

        int count = 0;
        int index = 0;
        for (int i = 0; i < childNum; i++) {
            // 用尽可能小的饼干去匹配胃口小的孩子
            while (index < s.length && g[i] > s[index]) index++;
            // g[i] <= s[index]
            if (index < s.length) {
                count++;
                index++;
            }
            else {
                break;
            }
        }

        return count;
    }

    /**
     * 摆动序列
     * 我想不出来，但是看了题解之后大悟
     * 似乎统计极值点就好了。。。
     * 好家伙，极值点也搞不定左右两边
     * i am a noob
     * 但是看到一个惊为天人的题解
     */
    public int wiggleMaxLength(int[] nums) {
        int down = 1, up = 1;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] > nums[i - 1])
                up = down + 1;
            else if (nums[i] < nums[i - 1])
                down = up + 1;
        }
        return nums.length == 0 ? 0 : Math.max(down, up);
    }

    /**
     * 摆动序列
     * 这个解法算是比较好理解的
     * 但是我也没有想到
     * 至于最后一种DP解法，就不实现了
     * 说一下DP解法的思路：
     * 很容易可以发现，对于我们当前考虑的这个数，要么是作为山峰（即 nums[i] > nums[i-1]），要么是作为山谷（即 nums[i] < nums[i - 1]）。
     * 设 dp 状态dp[i][0]，表示考虑前 i 个数，第 i 个数作为山峰的摆动子序列的最长长度
     * 设 dp 状态dp[i][1]，表示考虑前 i 个数，第 i 个数作为山谷的摆动子序列的最长长度
     * 则转移方程为：
     * dp[i][0] = max(dp[i][0], dp[j][1] + 1)，其中0 < j < i且nums[j] < nums[i]，表示将 nums[i]接到前面某个山谷后面，作为山峰。
     * dp[i][1] = max(dp[i][1], dp[j][0] + 1)，其中0 < j < i且nums[j] > nums[i]，表示将 nums[i]接到前面某个山峰后面，作为山谷。
     * 初始状态：
     * 由于一个数可以接到前面的某个数后面，也可以以自身为子序列的起点，所以初始状态为：dp[0][0] = dp[0][1] = 1。
     */
    public int wiggleMaxLengthAnotherSolution(int[] nums) {
        if (nums.length <= 1) {
            return nums.length;
        }
        //当前差值
        int curDiff = 0;
        //上一个差值
        int preDiff = 0;
        int count = 1;
        for (int i = 1; i < nums.length; i++) {
            //得到当前差值
            curDiff = nums[i] - nums[i - 1];
            //如果当前差值和上一个差值为一正一负
            //等于0的情况表示初始时的preDiff
            if ((curDiff > 0 && preDiff <= 0) || (curDiff < 0 && preDiff >= 0)) {
                count++;
                preDiff = curDiff;
            }
        }
        return count;
    }

    /**
     * 最大子序和
     * 没想出来，看题解自己实现了
     * 关键是想到：
     * 当连续和为负数的时候，其后面无论怎么加，都不会比从下一个数重新计算连续和大
     */
    public int maxSubArray(int[] nums) {
        int res = Integer.MIN_VALUE;
        int sum = 0;
        int lt = 0;
        int rt = 0;
        while (lt < nums.length) {
            while (rt < nums.length && sum >= 0) {
                sum += nums[rt];
                res = Math.max(res, sum);
                rt++;
            }
            lt = rt;
            sum = 0;
        }
        return res;
    }

    /**
     * 买卖股票的最佳时机 II
     * 没想出来，后来发现是极值点问题
     * 极小值买入，极大值卖出，preices附加末尾0
     * 或者是预知明天能赚就买入，直到预知要亏的时候再卖出
     */
    public int maxProfit(int[] prices) {
        int res = 0;
        for(int i = 0; i < prices.length - 1; i++) {
            int t = prices[i + 1] - prices[i];
            res += t > 0 ? t : 0;
        }
        return res;
    }

    /**
     * 跳跃游戏
     * 经过思考与七次试错，我成功做出来了
     * 思路是：
     * fast指定当前可以达到的最远范围
     * slow指定当前的位置
     * slow一直尝试向前走并且更新最远可达位置
     * 当前位置是不可能超过最远可达位置的，如果超过了说明不合理，返回false
     * 如果最远可达位置触及到了最后一个元素，那么就可以直接返回true了
     */
    public boolean canJump(int[] nums) {
        // 3 2 1 0 4
        int slow = 0;
        int fast = 0;
        while (true) {
            fast = Math.max(fast, slow + nums[slow]);
            if (fast >= nums.length - 1) {
                return true;
            }
            slow++;
            if (slow > fast) {
                return false;
            }
        }
    }

    /**
     * 跳跃游戏 II
     * 经过思考与十二次试错，我终于做出来了
     * 这有点动归的思想
     */
    public int jump(int[] nums) {
        int curMostFar = 0;
        int cur = 0;
        int count = 0;

        if (nums.length == 1) {
            return 0;
        }

        // 从位置0到位置i的最远范围中的最大值
        int[] tilMostFar = new int[nums.length];
        tilMostFar[0] = nums[0];
        
        for(int i = 1; i < nums.length; i++) {
            // 当前位置i的最远可达范围
            curMostFar = i + nums[i];
            tilMostFar[i] = Math.max(tilMostFar[i - 1], curMostFar);
        }

        while (true) {
            // 贪心，让fast每一次都跨度最大
            cur = tilMostFar[cur];
            count++;
            if (cur >= nums.length - 1) {
                return count;
            }
        }
    }

    /**
     * K次取反后最大化的数组和
     * 有负数优先取反负数，没有负数优先取反小的
     * 可以考虑先对数组排个序？
     * 呃，这样的话每次取反排序可能会变，要不维护一个小顶堆？
     * 我对Java的Stream API还是不太熟悉。
     */
    public int largestSumAfterKNegations(int[] nums, int k) {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>(IntStream.of(nums).boxed().collect(Collectors.toList()));
        for(int i = 0; i < k; i++) {
           int val = minHeap.poll();
           minHeap.offer(-val);
        }
        return minHeap.stream().mapToInt(Integer::intValue).sum();
    }

    /**
     * 加油站
     * 这是完全自己做出来了的
     * 核心是认识到了油耗diff = gas-cost
     * 然后就是计算diff数组的最大子序列和
     * 为什么diff不用做成循环数组考虑最大子序和
     * 因为在一开始判断totalDiff >= 0了
     * 所以可以知道油量一定是够的，只需要找到一个合适的起点即可
     */
    public int canCompleteCircuit(int[] gas, int[] cost) {
        int len = gas.length;
        int[] diff = new int[len];
        int totalDiff = 0;
        for(int i = 0; i < len; i++) {
            diff[i] = gas[i] - cost[i];
            totalDiff += diff[i];
        }

        if (totalDiff < 0) {
            return -1;
        }
        // 1 2 3 4 5
        // 3 4 5 1 2
        // -2 -2 -2 3 3

        // 2 3 4
        // 3 4 3
        // -1 -1 1

        // 5 1 2 3 4
        // 4 4 1 5 1
        // 1 -3 1 -2 3

        // 寻找diff数组里最大子序列和
        // 关键是不要在子序和为负的时候加上一个数
        int subSum = 0;
        int slow = 0;
        int fast = 0;
        while (slow < len && fast < len) {
            subSum += diff[fast];
            fast++;
            if (subSum < 0) {
                slow = fast;
                subSum = 0;
            }
        }
        return slow;
    }

    /**
     * 分发糖果
     * 这题是碰到的第四道hard了，似乎是的
     * 做出来了，还是计算分数的diff数组
     * diff[i] = ratings[i] - ratings[i - 1] diff[i] > 0给i孩加一个，小于0给i - 1孩子加一个
     * 一直重复这个过程直到没有改变为止
     * 但是运行效率太高了
     */
    public int candyTroubleTimeEffective(int[] ratings) {
            
        int len = ratings.length;
        int candyNum = len;
        
        int[] candys = new int[len];
        Arrays.fill(candys, 1);

        int[] diff = new int[len];

        // 1 0 2 2 4 3 1
        //  -1 2 0 2 -1 -2
        // 
        // 1 1 1 1 1 1 1
        // 2 1 2 1 3 2 1

        // 1 2 2
        //   1 0
        //   

        // 1 3 2 2 1
        //   2 -1 0 -1
        // 1 2 1 2 1
        // 1 3 1 2 1

        // 1 2 3 1 3 2 1

        // 1 2 87 87 87 2 1
        //   + +  =  =  - -
        // 1 2 3  1  2  1 1

        for(int i = 1; i < len; i++) {
            diff[i] = ratings[i] - ratings[i - 1];
            if (diff[i] > 0) {
                if(candys[i - 1] >= candys[i]) {
                    candys[i]++;
                    candyNum++;
                }
            }
            else if (diff[i] < 0) {
                if(candys[i - 1] <= candys[i]){
                    candys[i - 1]++;
                    candyNum++;
                }
            }
        }

        boolean change = true;
        while (change) {
            change = false;
            for(int i = 1; i < len; i++) {
                if (diff[i] > 0) {
                    if(candys[i - 1] >= candys[i]) {
                        candys[i]++;
                        candyNum++;
                        change = true;
                    }
                }
                else if (diff[i] < 0) {
                    if(candys[i - 1] <= candys[i]){
                        candys[i - 1]++;
                        candyNum++;
                        change = true;
                    }
                }
            }
        }
        
        return candyNum;
    }

    /**
     * 分发糖果
     * 确定左孩子大于右孩子的情况，一定要从后往前遍历
     * 这个贪心解法的关键是以正确的顺序贪心考虑左右孩子的大小处理
     * 因为 rating[5]与rating[4]的比较 要利用上 rating[5]与rating[6]的比较结果，所以 要从后向前遍历
     * 同理，确定右孩子大于左孩子的情况，一定要从前往后遍历
     */
    public int candy(int[] ratings) {

        // 1 0 2
        // 2 1 1

        int len = ratings.length;
        int[] candys = new int[len];
        Arrays.fill(candys, 1);

        // 从前往后确定右孩子大于左孩子的情况
        for(int i = 1; i < len; i++) {
            if (ratings[i] > ratings[i - 1]) {
                candys[i] = candys[i - 1] + 1;
            }
        }

        // 从后往前确定左孩子大于右孩子的情况
        for(int i = len - 2; i >= 0; i--) {
           if(ratings[i + 1] < ratings[i]) {
                candys[i] = Math.max(candys[i + 1] + 1, candys[i]);
            }
        }

        int candyNum = 0;
        for(int candy : candys) {
            candyNum += candy;
        }
        
        return candyNum;
    }

    /**
     * 柠檬水找零
     * 核心就是意识到：
     * 5是很宝贵的，无论是找10，20零都需要5，找20优先消耗10
     * 然后围绕5的数量做文章
     */
    public boolean lemonadeChange(int[] bills) {
        // 5 5 10 10 20

        // 10 -> 5
        // 20 -> 5 5 5
        // 20 -> 10 5

        int count5 = 0;
        int count10 = 0;
    
        for(int i = 0; i < bills.length; i++) {
           if (bills[i] == 5) {
                count5++;
           }
           else if (bills[i] == 10) {
                if (count5 > 0) {
                    count5--;
                    count10++;
                }
                else {
                    return false;
                }
           }
           else {
                if (count10 > 0 && count5 > 0) {
                    count10--;
                    count5--;
                }
                else if (count5 >= 3) {
                    count5 -= 3;
                }
                else {
                    return false;
                }
           }
        }
        
        return true;
    }

    /**
     * 根据身高重建队列
     * 似乎想不到什么好的思路
     * 呃，我似乎想到了
     * 就是先按身高从大到小排序，如果身高相同就按照第二个数字从小到大排
     * 然后直接从头到尾根据第二个数字，插入到指定位置即可
     */
    public int[][] reconstructQueue(int[][] people) {
        // 先按身高降序排序，如果身高相同，则按ki升序排序
        Arrays.sort(people, (e1, e2) -> {
            if (e1[0] != e2[0]) {
                return e2[0] - e1[0]; // 降序排序
            }
            return e1[1] - e2[1]; // 如果身高相同，按照ki升序排序
        });

        List<int[]> result = new ArrayList<>();

        // 遍历排序后的数组，将每个人插入到指定位置
        for (int[] person : people) {
            result.add(person[1] > result.size() ? result.size() : person[1], person); 
        }

        return result.toArray(new int[result.size()][]);
    }

    /**
     * 用最少数量的箭引爆气球
     * 规定时间内没做出来
     * 这个解法的思路很巧妙，可是是怎么想到的呢？
     * 核心是先排序
     */
    public int findMinArrowShots(int[][] points) {
        if (points.length == 0) return 0;

        // 按照每个区间的右端点进行排序
        Arrays.sort(points, (a, b) -> Integer.compare(a[1], b[1]));
    
        int arrows = 1;
        int arrowPos = points[0][1]; // 当前箭射的位置设为第一个区间的右端点
    
        for (int i = 1; i < points.length; i++) {
            // 如果当前区间的左端点在当前箭的射击范围外，则需要一个新的箭
            if (points[i][0] > arrowPos) {
                arrows++;
                arrowPos = points[i][1]; // 更新射箭位置为当前区间的右端点
            }
        }
    
        return arrows;
    }

    /**
     * 无重叠区间
     * 关于compare(int o1, int o2)接口可以这么记忆：o1 => min, o2 => max
     * o1 - o2 ==> min - max ==> 从小到大
     * o2 - o1 ==> max - min ==> 从大到小
     * 通过了但是执行用时仅击败了23.47%
     * 呃，但是我把comment1处稍微剔除了第二个参数的排序之后就好起来了，击败79.02%
     * 核心是先排序
     */
    public int eraseOverlapIntervals(int[][] intervals) {
        if (intervals.length <= 1) {
            return 0;
        }

        int count = 0;

        // [1,2] [2,3] [3,4]
        // 
        Arrays.sort(intervals, (e1, e2) -> {
            // comment 1: return e1[0] - e2[0] == 0 ? e1[1] - e2[1] : e1[0] - e2[0];
            return e1[0] - e2[0];
        });

        // ,,,,,,,,,,,
        // [-73,-26] [-65,-11] [-63,2] [-62,-49] [-52,31] [-40,-26] [-31,49] [30,47] [58,95] [66,98] [82,97] [95,99]
        // 

        // 上一个非重叠区间的有边界
        int prevMax = intervals[0][1];

        for(int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] < prevMax) {
                count++;
                prevMax = Math.min(prevMax, intervals[i][1]);
            }
            else {
                prevMax = intervals[i][1];
            }
        }

        return count;
    }

    /**
     * 划分字母区间
     * 完全自己想出来了，击败99.12%
     * 核心思路是用一个数组记录字母出现的最后一个位置
     * 然后遍历
     */
    public List<Integer> partitionLabels(String s) {
        List<Integer> res = new ArrayList<>();
        char[] str = s.toCharArray();
        // 每个单词最后出现的位置
        int[] last = new int[26];
        Arrays.fill(last, -1);
        // ababcbacadefegdehijhklij
        for(int i = 0; i < str.length; i++) {
            last[str[i] - 97] = i;
        }
        // 当前串内的字母出现的最后边界
        int edge = 0;
        // 记录串长度
        int count = 0;
        for(int i = 0; i < str.length; i++) {
            // 更新串内字母的最后边界
            edge = Math.max(edge, last[str[i] - 97]);
            count++;
            if (i == edge) {
                res.add(count);
                count = 0;
            }
        }
        return res;
    }

    /**
     * 合并区间
     * 一次过
     * 击败88.6%
     * 核心依旧是先排序，然后用一个栈来存储临时的结果
     * 为什么想到了用栈，因为排序是升序的，每一个要比较的都是上一个，
     * 这与栈的LIFO的性质匹配
     */
    public int[][] merge(int[][] intervals) {
        // [1,3],[2,6],[8,10],[15,18]
        // [1,3] [2,6] [8,10] [15,18]
        
        Arrays.sort(intervals, (min, max) -> {
            return min[0] - max[0];
        });

        ArrayDeque<int[]> stack = new ArrayDeque<>();
        stack.push(intervals[0]);
        int[] prev;
        int[] cur;
 
        for(int i = 1; i < intervals.length; i++) {
            cur = intervals[i];
            prev = stack.pop();
            if (cur[0] >= prev[0] && cur[0] <= prev[1]) {
                prev[1] = Math.max(cur[1], prev[1]);
                stack.push(prev);
            }
            else {
                stack.push(prev);
                stack.push(cur);
            }
        }

        int[][] res = new int[stack.size()][];

        int index = stack.size() - 1;
        while (!stack.isEmpty()) {
            res[index] = stack.pop();
            index--;
        }

        return res;
    }

    /**
     * 单调递增的数字
     * 没想出来
     * 看了题解，本题的核心是意识到一点
     * 这里以98为例子
     * 98的最大位数递增数可以这么计算str[i - 1]>str[i]那么就是(str[i - 1] - 1)9，即89
     * 而79，7 < 9 那么就是str[i - 1](str[i])，即79
     * 根据这个思路，后一位决定前一位
     * 所以需要的是从后往前的遍历
     * 正确解决这道题的关键是：
     * 1. 从左到右找到第一个不满足单调递增的数字
     * 2. 调整这个位置的数字减 1，并将其右侧所有的数字变为 9。
     * 3. 需要从右向左回溯，确保前一位也满足单调递增性。
     */
    public int monotoneIncreasingDigits(int n) {
        char[] num = Integer.toString(n).toCharArray(); // 将数字转为字符数组方便操作
        int len = num.length;
        int marker = len; // 用于标记需要变成9的位置

        // 3 3 2
        //   i  

        // 从右向左遍历，找到第一个不满足单调递增的数字
        for (int i = len - 1; i > 0; i--) {
            if (num[i - 1] > num[i]) {
                num[i - 1]--;  // 当前位减 1
                marker = i;    // 标记需要变成9的位置
            }
        }

        // 将标记位置及其右侧所有的数字变为9
        for (int i = marker; i < len; i++) {
            num[i] = '9';
        }

        return Integer.parseInt(new String(num)); // 转换回整数
    }

    /**
     * 监控二叉树
     * 遇到的第五个hard
     * 没做出来，因为做这个之前跳过了二叉树模块，熟练度上也吃了点亏
     */
    class Solution {
        private int count = 0;
    
        public int minCameraCover(TreeNode root) {
            if (dfs(root) == -1) {
                count++;
            }
            return count;
        }
    
        // 返回值:
        // 0: 当前节点被覆盖
        // 1: 当前节点未被覆盖，但不需要放置摄像头
        // -1: 当前节点未被覆盖，需要放置摄像头
        private int dfs(TreeNode node) {
            if (node == null) return 0; // 空节点视为已覆盖
    
            int left = dfs(node.left);
            int right = dfs(node.right);
    
            // 如果左右子节点至少有一个未被覆盖，则当前节点需要放置摄像头
            if (left == -1 || right == -1) {
                count++;
                return 1;
            }
    
            // 如果左右子节点至少有一个放置了摄像头，则当前节点已被覆盖
            if (left == 1 || right == 1) {
                return 0;
            }
    
            // 当前节点未被覆盖，但不需要放置摄像头
            return -1;
        }
    }
    
    /**
     * Definition for a binary tree node.
     */
    public class TreeNode {
        int val;          // 节点的值
        TreeNode left;    // 左子节点
        TreeNode right;   // 右子节点

        // 无参构造方法
        public TreeNode() {}

        // 单参数构造方法
        public TreeNode(int val) {
            this.val = val;
        }

        // 全参数构造方法
        public TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }
}


