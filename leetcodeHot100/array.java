package leetcodeHot100;

import java.util.Arrays;

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
            }
            else if (index > k) {
                end = index - 1;
            }
            else if (index < k) {
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
}
