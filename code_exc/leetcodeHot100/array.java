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
     * 后面还有更简单的方法，是第三次做的时候自己想出来的
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
        for (int i = 0; i < nums.length - 2; i++) {
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
        for (int i = start; i < nums.length; i++) {
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
        for (int num : nums) {
            if (sum < 0) {
                sum = 0;
            }
            sum += num;
            res = Math.max(res, sum);
        }
        return res;
    }

    /**
     * 轮转数组
     */
    public void rotate(int[] nums, int k) {
        // 1 2 3 4 5 6 7
        // k = 3
        // 5 6 7 1 2 3 4
        int[] res = new int[k];
        for (int i = 0; i < nums.length; i++) {
            int newIndex = (i + k) % nums.length;
            res[newIndex] = nums[i];
        }
        for (int i = 0; i < nums.length; i++) {
            nums[i] = res[i];
        }
    }

    /**
     * 轮转数组
     * 巧妙的反转解法
     */
    public void rotateWithAnotherSolution(int[] nums, int k) {
        k %= nums.length;
        reverseArray(nums, 0, nums.length - 1);
        reverseArray(nums, 0, k - 1);
        reverseArray(nums, k, nums.length - 1);
    }

    private void reverseArray(int[] nums, int start, int end) {
        int lt = start;
        int rt = end;
        while (rt > lt) {
            swapInArray(nums, lt, rt);
            rt--;
            lt++;
        }
    }

    private void swapInArray(int[] nums, int a, int b) {
        int t = nums[a];
        nums[a] = nums[b];
        nums[b] = t;
    }

    /**
     * 除自身以为数组的乘积
     * 没做出来，需要用到前缀积、后缀积
     */
    public int[] productExceptSelf(int[] nums) {
        int[] prefixProd = new int[nums.length];
        int[] suffixProd = new int[nums.length];
        Arrays.fill(prefixProd, 1);
        Arrays.fill(suffixProd, 1);
        for (int i = 0; i < nums.length; i++) {
            prefixProd[i] = i >= 1 ? prefixProd[i - 1] * nums[i] : nums[i];
        }
        for (int i = nums.length - 1; i >= 0; i--) {
            suffixProd[i] = (i + 1) < nums.length ? suffixProd[i + 1] * nums[i] : nums[i];
        }
        int[] res = new int[nums.length];
        for (int i = 0; i < nums.length; i++) {
            res[i] = ((i - 1 >= 0) ? prefixProd[i - 1] : 1) * ((i + 1 < nums.length) ? suffixProd[i + 1] : 1);
        }
        return res;
    }

    /**
     * 合并区间
     * 做出来了，核心是先排序
     * 然后考虑相邻的数组之间的合并关系
     * 可以用一个Stack存储合并的中间结果
     */
    public int[][] merge(int[][] intervals) {
        // [1,3],[2,6],[8,10],[15,18]
        Arrays.sort(intervals, (a, b) -> {
            if (a[0] != b[0]) {
                return a[0] - b[0];
            } else {
                return a[1] - b[1];
            }
        });
        Deque<int[]> stack = new ArrayDeque<>();
        stack.push(intervals[0]);
        for (int i = 1; i < intervals.length; i++) {
            int[] curInterval = intervals[i];
            int[] lastInterval = stack.peek();
            if (curInterval[0] <= lastInterval[1]) {
                int start = Math.min(curInterval[0], lastInterval[0]);
                int end = Math.max(curInterval[1], lastInterval[1]);
                stack.pop();
                stack.push(new int[] { start, end });
                continue;
            }
            stack.push(curInterval);
        }
        int size = stack.size();
        int[][] res = new int[size][2];
        for (int i = size - 1; i >= 0; i--) {
            res[i] = stack.pop();
        }
        return res;
    }
}
