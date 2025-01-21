package leetcodeHot100;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

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
     * 寻找两个正序数组的中位数
     * 5min ac
     * 但是时间复杂度很高
     */
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>(Integer::compareTo);
        for(int num : nums1) {
            priorityQueue.offer(num);
        }
        for(int num : nums2) {
            priorityQueue.offer(num);
        }
        int len = priorityQueue.size();
        if (len % 2 == 0) {
            int count = 0;
            // len 4
            // 1 2 3 4
            while (count != (len - 2) / 2) {
                priorityQueue.poll();
                count++;
            }
            return (priorityQueue.poll() + priorityQueue.poll()) / 2.0;
        }
        else {
            int count = 0;
            // len 3
            // 1 2 3
            while (count != (len - 1) / 2) {
                priorityQueue.poll();
                count++;
            }
            return priorityQueue.poll();
        }
    }

    /**
     * 寻找两个正序数组的中位数
     * 题解的做法
     */
    public double findMedianSortedArraysWithBinarySearch(int[] nums1, int[] nums2) {
        int length1 = nums1.length, length2 = nums2.length;
        int totalLength = length1 + length2;
        if (totalLength % 2 == 1) {
            int midIndex = totalLength / 2;
            double median = getKthElement(nums1, nums2, midIndex + 1);
            return median;
        } else {
            int midIndex1 = totalLength / 2 - 1, midIndex2 = totalLength / 2;
            double median = (getKthElement(nums1, nums2, midIndex1 + 1) + getKthElement(nums1, nums2, midIndex2 + 1)) / 2.0;
            return median;
        }
    }

    public int getKthElement(int[] nums1, int[] nums2, int k) {
        /* 主要思路：要找到第 k (k>1) 小的元素，那么就取 pivot1 = nums1[k/2-1] 和 pivot2 = nums2[k/2-1] 进行比较
         * 这里的 "/" 表示整除
         * nums1 中小于等于 pivot1 的元素有 nums1[0 .. k/2-2] 共计 k/2-1 个
         * nums2 中小于等于 pivot2 的元素有 nums2[0 .. k/2-2] 共计 k/2-1 个
         * 取 pivot = min(pivot1, pivot2)，两个数组中小于等于 pivot 的元素共计不会超过 (k/2-1) + (k/2-1) <= k-2 个
         * 这样 pivot 本身最大也只能是第 k-1 小的元素
         * 如果 pivot = pivot1，那么 nums1[0 .. k/2-1] 都不可能是第 k 小的元素。把这些元素全部 "删除"，剩下的作为新的 nums1 数组
         * 如果 pivot = pivot2，那么 nums2[0 .. k/2-1] 都不可能是第 k 小的元素。把这些元素全部 "删除"，剩下的作为新的 nums2 数组
         * 由于我们 "删除" 了一些元素（这些元素都比第 k 小的元素要小），因此需要修改 k 的值，减去删除的数的个数
         */

        int length1 = nums1.length, length2 = nums2.length;
        int index1 = 0, index2 = 0;
        int kthElement = 0;

        while (true) {
            // 边界情况
            if (index1 == length1) {
                return nums2[index2 + k - 1];
            }
            if (index2 == length2) {
                return nums1[index1 + k - 1];
            }
            if (k == 1) {
                return Math.min(nums1[index1], nums2[index2]);
            }
            
            // 正常情况
            int half = k / 2;
            int newIndex1 = Math.min(index1 + half, length1) - 1;
            int newIndex2 = Math.min(index2 + half, length2) - 1;
            int pivot1 = nums1[newIndex1], pivot2 = nums2[newIndex2];
            if (pivot1 <= pivot2) {
                k -= (newIndex1 - index1 + 1);
                index1 = newIndex1 + 1;
            } else {
                k -= (newIndex2 - index2 + 1);
                index2 = newIndex2 + 1;
            }
        }
    }

    /**
     * 搜索旋转排序的数组
     * 20min ac 击败100%
     */
    public int search(int[] nums, int target) {
        int lt = 0;
        int rt = nums.length - 1;
        while(rt > lt) {
            int mid = (lt + rt) / 2;
            if(nums[mid] > nums[lt]){
                lt = mid;
            }
            else if(nums[mid] < nums[lt]) {
                rt = mid;
            }
            else {
                break;
            }
        }
        int s1 = bs(nums, 0, lt, target);
        int s2 = bs(nums, lt + 1, nums.length - 1, target);
        if(s1 == -1 && s2 == -1) {
            return -1;
        }
        return s1 == -1 ? s2 : s1;
    }

    private int bs(int[] nums, int start, int end, int target) {
        int lt = start;
        int rt = end;
        while(rt >= lt) {
            int mid = (lt + rt) / 2;
            if(nums[mid] == target) {
                return mid;
            }
            else if(nums[mid] > target) {
                rt = mid - 1;
            }
            else if(nums[mid] < target) {
                lt = mid + 1;
            }
        }
        return -1;
    }
}
