package interview150;

import java.util.Arrays;
import java.util.HashSet;
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
     * 删除有序数组中的重复项II
     * 没做出来
     * 想到了用双指针，但是写不出来
     * 只有一个混沌的想法...
     */
    public int removeDuplicatesII(int[] nums) {
        int n = nums.length;
        if (n <= 2) {
            return n;
        }
        int slow = 2, fast = 2;
        while (fast < n) {
            if (nums[slow - 2] != nums[fast]) {
                nums[slow] = nums[fast];
                ++slow;
            }
            ++fast;
        }
        return slow;
    }
}
