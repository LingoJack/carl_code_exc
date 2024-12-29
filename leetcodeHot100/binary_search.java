package leetcodeHot100;

import java.util.Map;

public class binary_search {

    /**
     * 搜索插入位置
     * 6min ac
     * 二分查找的模板要熟练
     */
    public int searchInsert(int[] nums, int target) {
        int lt = 0;
        int rt = nums.length - 1;
        int mid = 0;
        while (rt >= lt) {
            mid = (lt + rt) / 2;
            if (nums[mid] > target) {
                rt = mid - 1;
            } else if (nums[mid] < target) {
                lt = mid + 1;
            } else {
                return mid;
            }
        }
        return lt;
    }

    /**
     * 搜索二维矩阵
     * 就是行维度和列维度的两趟二分查找
     * 击败100%
     */
    public boolean searchMatrix(int[][] matrix, int target) {
        int rowNum = matrix.length;
        int rowIndex = 0;
        int up = rowNum - 1;
        int down = 0;
        while (up >= down) {
            int mid = (up + down) / 2;
            if (matrix[mid][0] > target) {
                up = mid - 1;
            } else if (matrix[mid][0] < target) {
                down = mid + 1;
            } else {
                return true;
            }
        }
        rowIndex = up == -1 ? 0 : up;

        int columnNum = matrix[0].length;
        int lt = 0;
        int rt = columnNum - 1;
        while (lt <= rt) {
            int mid = (lt + rt) / 2;
            if (matrix[rowIndex][mid] < target) {
                lt = mid + 1;
            } else if (matrix[rowIndex][mid] > target) {
                rt = mid - 1;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * 在排列数组中查找元素的第一个和最后一个位置
     * 7min30s ac
     */
    public int[] searchRange(int[] nums, int target) {
        int lt = 0;
        int rt = nums.length - 1;
        int mid = -1;
        while (lt <= rt) {
            mid = (lt + rt) / 2;
            if (nums[mid] > target) {
                rt = mid - 1;
            } else if (nums[mid] < target) {
                lt = mid + 1;
            } else {
                break;
            }
        }
        if (mid == -1 || nums[mid] != target) {
            return new int[] { -1, -1 };
        }
        int lower = mid;
        while (lower >= 0 && nums[lower] == target) {
            lower--;
        }
        lower = Math.max(++lower, 0);

        int upper = mid;
        while (upper <= nums.length - 1 && nums[upper] == target) {
            upper++;
        }
        upper = Math.min(--upper, nums.length - 1);
        return new int[] { lower, upper };
    }

    /**
     * 寻找旋转排序数组中的最小值
     * 没做出来，思路错了
     */
    public int findMin(int[] nums) {
        int lt = 0;
        int rt = nums.length - 1;
        // 如果数组已经被完全排序或只有一个元素
        if (nums[lt] < nums[rt]) {
            return nums[lt];
        }

        while (lt < rt) {
            int mid = lt + (rt - lt) / 2;
            if (nums[mid] > nums[rt]) {
                // 最小值在右边
                lt = mid + 1;
            } else {
                // 最小值在左边，包括mid本身也可能是最小值
                rt = mid;
            }
        }
        // 循环结束时，lt == rt，指向最小值
        return nums[lt];
    }
}
