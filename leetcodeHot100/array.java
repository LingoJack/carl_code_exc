package leetcodeHot100;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class array {

    /**
     * 数组中的第K个最大元素
     * 有几个问题：
     * 你**的就是个唐氏，我****
     * 快排和堆排都不会写
     */
    public int findKthLargest(int[] nums, int k) {
        int pivot = 0;
        int start = 0;
        int end = nums.length - 1;
        k = nums.length - k - 1;
        while (true) {
            pivot = end;
            int index = partition(nums, start, end, pivot);
            if (index == k) {
                return nums[index];
            } else if (index > k) {
                end = index - 1;
            } else if (index < k) {
                start = index + 1;
            }
        }
    }

    private int partition(int[] nums, int start, int end, int pivot) {
        if (start == end) {
            return start;
        }
        int slow = start;
        int fast = start;
        while (fast < end) {
            if (nums[fast] < nums[pivot]) {
                swap(nums, slow, fast);
                slow = slow < fast ? slow + 1 : slow;
            }
            fast++;
        }
        swap(nums, slow, pivot);
        return slow;
    }

    private void swap(int[] nums, int idx1, int idx2) {
        int tmp = nums[idx1];
        nums[idx1] = nums[idx2];
        nums[idx2] = tmp;
    }

    /**
     * 最长连续序列
     * 15min做出来一个实现，但是超时了
     */
    public int longestConsecutiveWithTimeout(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        Map<Integer, Boolean> map = new HashMap<>();
        int max = 1;
        for (int num : nums) {
            map.put(num, true);
            int t = num - 1;
            int count = 1;
            while (map.getOrDefault(t, false)) {
                count++;
                t--;
            }
            t = num + 1;
            while (map.getOrDefault(t, false)) {
                t++;
                count++;
            }
            max = Math.max(max, count);
        }
        return max;
    }

    /**
     * 最长连续序列
     * 核心：
     * 去重
     * 只从序列的第一个数开始查找
     */
    public int longestConsecutive(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }

        Set<Integer> numSet = new HashSet<>();
        for (int num : nums) {
            numSet.add(num);
        }

        int res = 0;
        for (int num : numSet) {
            // 只从序列的起点开始查找
            if (!numSet.contains(num - 1)) {
                int currentNum = num;
                int currentStreak = 1;

                while (numSet.contains(currentNum + 1)) {
                    currentNum += 1;
                    currentStreak += 1;
                }

                res = Math.max(res, currentStreak);
            }
        }
        return res;
    }

    /**
     * 贪吃的小Q
     * 二分查找的变种
     */
    public int minQ(int day, int chocalateNum) {
        int dayOneEatNum = 1;
        // 使用二分查找优化搜索范围
        int left = 1, right = chocalateNum;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (canFinishInDays(mid, chocalateNum, day)) {
                dayOneEatNum = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return dayOneEatNum;
    }

    private boolean canFinishInDays(int dayOneEatNum, int chocalateNum, int day) {
        int remainingChocolates = chocalateNum;
        int eatNum = dayOneEatNum;
        for (int i = 0; i < day && remainingChocolates >= 0; i++) {
            remainingChocolates -= eatNum;
            eatNum = (int) Math.ceil((double) eatNum / 2);
        }
        return remainingChocolates >= 0;
    }

    /**
     * 缺失的第一个正数
     * 原地哈希的思想
     * 本题的核心是注意到：
     * 答案一定在[1, nums.length + 1]的区间内
     */
    public int firstMissingPositive(int[] nums) {
        int len = nums.length;
        byte[] exist = new byte[len + 1]; 
        for(int num : nums) {
            if (num >= 0 && num <= len) {
                exist[num] = 1;
            }
        }

        for(int i = 1; i <= nums.length; i++) {
            if (exist[i] == 0) {
                return i;
            }
        }

        return len + 1;
    }
}
