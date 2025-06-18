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
        
    }
}
