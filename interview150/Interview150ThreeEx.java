package interview150;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Interview150ThreeEx {

    /**
     * 合并两个有序数组
     */
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int idx = nums1.length - 1;
        int idx1 = m - 1, idx2 = n - 1;
        while (idx1 >= 0 || idx2 >= 0) {
            int num1 = idx1 >= 0 ? nums1[idx1] : Integer.MIN_VALUE;
            int num2 = idx2 >= 0 ? nums2[idx2] : Integer.MIN_VALUE;
            if (num1 > num2) {
                nums1[idx] = num1;
                idx1--;
            } else {
                nums1[idx] = num2;
                idx2--;
            }
            idx--;
        }
    }

    /**
     * 移除元素
     */
    public int removeElement(int[] nums, int val) {
        int fast = 0, slow = 0;
        while (fast < nums.length) {
            if (nums[fast] != val) {
                swap(nums, fast, slow);
                slow++;
            }
            fast++;
        }
        return slow;
    }

    private void swap(int[] nums, int idx1, int idx2) {
        int t = nums[idx1];
        nums[idx1] = nums[idx2];
        nums[idx2] = t;
    }

    /**
     * 删除有序数组中的重复项
     * 这不是最优的解法，你没有充分利用题目中“有序”这个特性
     */
    public int removeDuplicates(int[] nums) {
        Set<Integer> set = new HashSet<>();
        int slow = 0, fast = 0;
        while (fast < nums.length) {
            if (!set.contains(nums[fast])) {
                set.add(nums[fast]);
                swap(nums, slow, fast);
                slow++;
            }
            fast++;
        }
        return set.size();
    }

    /**
     * 删除有序数组中的重复项
     * 这个是最优解
     * 这个快慢指针的思路很牛逼，但是很反人类思维
     */
    public int removeDuplicatesAwesomeSolution(int[] nums) {
        // 0 1 2 1 1 2 2 3 3 4
        // _ _ s
        // _ _ _ _ _ f
        int len = nums.length;
        if (len <= 1) {
            return len;
        }
        // slow跟踪下一个不同元素需要被放置到的位置
        // slow - 1就是上一组元素
        // fast跟踪当前扫描的元素
        int slow = 1, fast = 1;
        while (fast < len) {
            if (nums[fast] != nums[slow - 1]) {
                nums[slow] = nums[fast];
                slow++;
            }
            fast++;
        }
        return slow;
    }

    /**
     * 删除有序数组中的重复项II
     * 没做出来
     * 想到了用双指针，但是写不出来
     * 只有一个混沌的想法...
     * 明天要好好看看（time： 2026-6-17 01:03 24h）
     * --- 已阅
     * 这题没做出来，因为「删除有序数组中的重复项」你就不是最优解做的，
     * 换句话说「删除有序数组中的重复项」你都没做明白
     */
    public int removeDuplicatesII(int[] nums) {
        int len = nums.length;
        if (len <= 2) {
            return len;
        }
        // slow跟踪下一个不同组的元素应该被插入到的位置
        // slow - 2代表上一个不同组的元素
        int slow = 2, fast = 2;
        while (fast < len) {
            if (nums[slow - 2] != nums[fast]) {
                nums[slow] = nums[fast];
                slow++;
            }
            fast++;
        }
        return slow;
    }

    /**
     * 多数元素
     */
    public int majorityElement(int[] nums) {
        int count = 0;
        Integer winner = null;
        for (int num : nums) {
            if (winner == null) {
                winner = num;
            }
            if (winner.equals(num)) {
                count++;
            } else {
                count--;
                if (count == 0) {
                    winner = null;
                }
            }
        }
        return winner;
    }

    /**
     * 轮转数组
     */
    public void rotate(int[] nums, int k) {
        int len = nums.length;
        reverse(nums, 0, len - 1);
        reverse(nums, 0, k - 1);
        reverse(nums, k, len - 1);
    }

    private void reverse(int[] nums, int start, int end) {
        int lt = start, rt = end;
        while (lt < rt) {
            swap(nums, lt, rt);
            lt++;
            rt--;
        }
    }

    /**
     * 买卖股票的最佳时机
     */
    public int maxProfit(int[] prices) {
        int minPrice = Integer.MAX_VALUE;
        int maxProft = 0;
        for (int price : prices) {
            if (minPrice > price) {
                minPrice = price;
            } else {
                maxProft = Math.max(maxProft, price - minPrice);
            }
        }
        return maxProft;
    }

    /**
     * 买卖股票的最佳时机II
     */
    public int maxProfitII(int[] prices) {
        int len = prices.length;
        int[][] dp = new int[len][2];
        dp[0][0] = 0;
        dp[0][1] = -prices[0];
        for (int i = 1; i < len; i++) {
            dp[i][0] = Math.max(dp[i - 1][0], dp[i - 1][1] + prices[i]);
            dp[i][1] = Math.max(dp[i - 1][1], dp[i - 1][0] - prices[i]);
        }
        return dp[len - 1][0];
    }

    /**
     * 跳跃游戏
     */
    public boolean canJump(int[] nums) {
        int scope = 0;
        for (int i = 0; i <= scope; i++) {
            scope = Math.max(scope, i + nums[i]);
            if (scope >= nums.length - 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * 跳跃游戏II
     */
    public int jump(int[] nums) {
        int[] scope = new int[nums.length];
        int maxScope = 0;
        for (int i = 0; i < nums.length; i++) {
            maxScope = Math.max(maxScope, nums[i] + i);
            scope[i] = maxScope;
        }
        int step = 0;
        int pos = 0;
        while (pos < nums.length - 1) {
            pos = scope[pos];
            step++;
            if (pos >= nums.length - 1) {
                break;
            }
        }
        return step;
    }

    /**
     * H指数
     */
    public int hIndex(int[] citations) {
        // 3 0 6 1 5
        // 0 1 3 5 6
        // _ _ _ _ i
        // _ _ _ _ c
        int len = citations.length;
        Arrays.sort(citations);
        int idx = len - 1;
        int count = 0;
        while (idx >= 0) {
            if (citations[idx] <= count) {
                break;
            }
            count++;
            idx--;
        }
        return count;
    }

    /**
     * O(1)时间插入、删除和获取随机元素
     * 没做出来
     * 主要是remove的逻辑没有写好
     */
    class RandomizedSet {

        private Map<Integer, Integer> map;

        private List<Integer> list;

        private Random random;

        public RandomizedSet() {
            list = new ArrayList<>();
            map = new HashMap<>();
            random = new Random();
        }

        public boolean insert(int val) {
            if (map.containsKey(val)) {
                return false;
            }
            map.put(val, list.size());
            list.add(val);
            return true;
        }

        public boolean remove(int val) {
            if (!map.containsKey(val)) {
                return false;
            }
            Integer removedIdx = map.get(val);
            list.set(removedIdx, list.get(list.size() - 1));
            map.put(list.get(list.size() - 1), removedIdx);
            map.remove(val);
            list.remove(list.size() - 1);
            return true;
        }

        public int getRandom() {
            return list.get(random.nextInt(list.size()));
        }
    }

    /**
     * 除自身以外的数组乘积
     */
    public int[] productExceptSelf(int[] nums) {
        int len = nums.length;
        int[] prefix = new int[len];
        int[] suffix = new int[len];
        int t = 1;
        for (int i = 0; i < len; i++) {
            t *= nums[i];
            prefix[i] = t;
        }
        t = 1;
        for (int i = len - 1; i >= 0; i--) {
            t *= nums[i];
            suffix[i] = t;
        }
        int[] res = new int[len];
        for (int i = 0; i < len; i++) {
            int a = i - 1 < 0 ? 1 : prefix[i - 1];
            int b = i + 1 >= len ? 1 : suffix[i + 1];
            res[i] = a * b;
        }
        return res;
    }

    /**
     * 加油站
     */
    public int canCompleteCircuit(int[] gas, int[] cost) {
        int len = cost.length;
        int[] diff = new int[len];
        int profit = 0;
        for (int i = 0; i < len; i++) {
            diff[i] = gas[i] - cost[i];
            profit += diff[i];
        }
        if (profit < 0) {
            return -1;
        }
        int offset = 0;
        int sum = 0;
        int max = 0;
        int start = 0;
        for (int i = 0; i < 2 * len - 1; i++) {
            int idx = i % len;
            if (sum < 0) {
                sum = 0;
                start = idx;
            }
            sum += diff[idx];
            if (sum > max) {
                offset = start;
                max = sum;
            }
        }
        return offset;
    }

    /**
     * 加油站
     */
    public int canCompleteCircuitAwesomeSolution(int[] gas, int[] cost) {
        int len = cost.length;
        int[] profit = new int[len];
        int sum = 0;
        for (int i = 0; i < len; i++) {
            profit[i] = gas[i] - cost[i];
            sum += profit[i];
        }
        if (sum < 0) {
            return -1;
        }
        sum = 0;
        int start = 0;
        for (int i = 0; i < len; i++) {
            if (sum < 0) {
                start = i;
                sum = 0;
            }
            sum += profit[i];
        }
        return start;
    }

    /**
     * 分发糖果
     */
    public int candy(int[] ratings) {
        int len = ratings.length;
        int[] candy = new int[len];
        Arrays.fill(candy, 1);
        // 1 3 2 2 1
        // 1 1 1 1 1
        // 1 2 1 1 1
        // 1 2 1 2 1
        for(int i = 1; i < len; i++) {
            if (ratings[i] > ratings[i - 1] && candy[i] < candy[i - 1] + 1) {
                candy[i] = candy[i - 1] + 1;
            }
        }
        for(int i = len - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1] && candy[i] < candy[i + 1] + 1) {
                candy[i] = candy[i + 1] + 1;
            }
        }
        int res = 0;
        for(int c : candy) {
            res += c;
        }
        return res;
    }

    /**
     * 接雨水
     */
    public int trap(int[] height) {
        
    }
}