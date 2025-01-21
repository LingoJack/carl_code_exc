package leetcodeHot100;

import java.util.Arrays;

import stack_and_queue.stack_and_queue;

public class trick {

    /**
     * 只出现一次的数字
     * 这是异或运算的技巧
     */
    public int singleNumber(int[] nums) {
        int res = 0;
        for (int num : nums) {
            res ^= num;
        }
        return res;
    }

    /**
     * 寻找重复数
     */
    public int findDuplicate(int[] nums) {
        boolean[] existed = new boolean[nums.length];
        for (int num : nums) {
            if (existed[num]) {
                return num;
            }
            existed[num] = true;
        }
        return -1;
    }

    /**
     * 多数元素
     */
    public int majorityElement(int[] nums) {
        int len = nums.length;
        int count = 0;
        Arrays.sort(nums);
        for (int i = 0; i < len; i++) {
            if (i >= 1 && nums[i] == nums[i - 1]) {
                count++;
            } else {
                count = 0;
            }
            if (count >= len / 2) {
                return nums[i];
            }
        }
        return -1;
    }

    /**
     * 多数元素
     * 同归于尽消杀法
     * count理解成占领地的士兵数
     */
    public int majorityElementAwesomeSolution(int[] nums) {
        int winner = nums[0];
        int count = 0;
        for (int num : nums) {
            if (count == 0) {
                winner = num;
            }
            if (winner == num) {
                count++;
            } else {
                count--;
            }
        }
        return winner;
    }

    /**
     * 颜色分类
     * 要求原地排序
     * 呃，一个快排秒了
     */
    public void sortColors(int[] nums) {
        quickSort(nums, 0, nums.length - 1);
    }

    private void quickSort(int[] nums, int start, int end) {
        if (start >= end) {
            return;
        }
        int pivot = partition(nums, start, end);
        quickSort(nums, start, pivot - 1);
        quickSort(nums, pivot + 1, end);
    }

    private int partition(int[] nums, int start, int end) {
        int slow = start;
        int fast = start;
        int pivot = end;
        while (fast < end) {
            if (nums[pivot] > nums[fast]) {
                swap(nums, fast, slow);
                slow++;
            }
            fast++;
        }
        swap(nums, slow, pivot);
        return slow;
    }

    private void swap(int[] nums, int a, int b) {
        int t = nums[a];
        nums[a] = nums[b];
        nums[b] = t;
    }

    /**
     * 下一个排列
     * 没做出来
     */
    public void nextPermutation(int[] nums) {
        // 2 3 1
        // i
        // j
        int i = nums.length - 2;
        // 从后往前找到第一个升序
        while (i >= 0 && nums[i] >= nums[i + 1]) {
            i--;
        }
        if (i >= 0) {
            // 从后往前找到第一个比i大的
            int j = nums.length - 1;
            while (nums[j] <= nums[i]) {
                j--;
            }
            swap(nums, i, j);
        }
        reverse(nums, i + 1);
    }

    private void reverse(int[] nums, int start) {
        int i = start, j = nums.length - 1;
        while (i < j) {
            swap(nums, i, j);
            i++;
            j--;
        }
    }
}
