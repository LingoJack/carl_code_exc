package leetcodeHot100;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
     * 缺失的第一个正数
     * 原地哈希的思想
     * 本题的核心是注意到：
     * 答案一定在[1, nums.length + 1]的区间内
     */
    public int firstMissingPositive(int[] nums) {
        int len = nums.length;
        byte[] exist = new byte[len + 1];
        for (int num : nums) {
            if (num >= 0 && num <= len) {
                exist[num] = 1;
            }
        }

        for (int i = 1; i <= nums.length; i++) {
            if (exist[i] == 0) {
                return i;
            }
        }

        return len + 1;
    }

    /**
     * 和为K的子数组
     * 思路完全错了，这题必须要用前缀和 + HashMap
     * 前缀和的解法很妙，如果是我自己想，恐怕想不到...
     * 通过计算前缀和，我们可以将问题转化为求解两个前缀和之差等于k的情况
     * 那么对于任意的两个下标i和j（i < j），如果prefixSum[j] - prefixSum[i] = k，
     * 即从第i个位置到第j个位置的元素之和等于k，那么说明从第i+1个位置到第j个位置的连续子数组的和为k。
     * 通过遍历数组，计算每个位置的前缀和，并使用一个哈希表来存储每个前缀和出现的次数。
     * 在遍历的过程中，我们检查是否存在prefixSum[j] - k的前缀和，
     * 如果存在，说明从某个位置到当前位置的连续子数组的和为k，我们将对应的次数累加到结果中。
     */
    public int subarraySum(int[] nums, int k) {
        int count = 0;
        int sum = 0;
        // 使用哈希表记录前缀和及其出现次数
        Map<Integer, Integer> prefixSumMap = new HashMap<>();
        // 初始化前缀和为 0 的情况
        prefixSumMap.put(0, 1);
        for (int num : nums) {
            sum += num;
            if (prefixSumMap.containsKey(sum - k)) {
                count += prefixSumMap.get(sum - k);
            }
            prefixSumMap.put(sum, prefixSumMap.getOrDefault(sum, 0) + 1);
        }
        return count;
    }

    /**
     * 三数之和
     * 20min ac，但是时间复杂度十分差，仅击败5%
     * 正解的核心的去重逻辑其实是排序，然后左右指针，跳过重复的
     */
    public List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);
        Set<List<Integer>> res = new HashSet<>();
        for(int i = 0; i < nums.length - 2; i++) {
            List<List<Integer>> list = findTwoNumSum(nums, i + 1, -nums[i]);
            if (list.isEmpty()) {
                continue;
            }
            int index = i;
            list.forEach(l -> l.add(nums[index]));
            if (!res.contains(list)) {
                res.addAll(list);
            }
        }
        return new ArrayList<>(res);
    }

    private List<List<Integer>> findTwoNumSum(int[] nums, int start, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        List<List<Integer>> res = new ArrayList<>();
        List<Integer> list = null;
        for(int i = start; i < nums.length; i++) {
            Integer index = map.get(target - nums[i]);
            if (index != null) {
                list = new ArrayList<>();
                list.add(nums[i]);
                list.add(nums[index]);
                res.add(list);
            }
            map.put(nums[i], i);
        }
        return res;
    }

    /**
     * 最大子数组和
     */
    public int maxSubArray(int[] nums) {
        int res = nums[0];
        int sum = 0;
        for(int num : nums) {
            if (sum < 0) {
                sum = 0;
            }
            sum += num;
            res = Math.max(res, sum);
        }
        return res;
    }
}
