package binary_search;

import java.util.Arrays;

public class binary_search {
    /**
     * H指数
     * 做出来了
     * 但是也提醒了我二分的模板必须要尽快弄熟悉
     */
    public int hIndex(int[] citations) {
        int sum = 0;
        int max = citations[0];
        Arrays.sort(citations);
        for (int citation : citations) {
            sum += citation;
            max = Math.max(max, citation);
        }
        while (max >= 0) {
            if (max * max <= sum && max <= citations.length) {
                int score = max;
                if (citations.length - findInsertPosInAscOrderByBinarySearch(citations, score) >= max) {
                    return max;
                }
            }
            max--;
        }
        return 0;
    }

    /**
     * 这里是二分的框架之一
     * 找到第一个比target大的
     */
    private int findInsertPosInAscOrderByBinarySearch(int[] nums, int target) {
        // 1 2 3 6 8
        // 5
        int lt = 0, rt = nums.length - 1;
        while (lt < rt) {
            int mid = (lt + rt) >> 1;
            if (nums[mid] < target) {
                lt = mid + 1;
            } else {
                rt = mid;
            }
        }
        return lt;
    }

    /**
     * 在排序数组中查找元素的第一个和最后一个位置
     * 这是一道拿来训练二分模板的十分好的题
     * 二分一般有几个场景：
     * 对于升序序列：
     * 确定target是否存在
     * 寻找target的左侧边界
     * 寻找target的右侧边界
     */
    public int[] searchRange(int[] nums, int target) {
        int ltBound = leftBoundAsc(nums, target);
        int rtBound = rightBoundAsc(nums, target);
        int[] res = new int[] { ((ltBound >= 0 && ltBound < nums.length) && nums[ltBound] == target ? ltBound : -1),
                ((rtBound >= 0 && rtBound < nums.length) && nums[rtBound] == target ? rtBound : -1) };
        return res;
    }

    /**
     * 二分模板
     * 寻找target
     * 左闭右闭，不存在则返回-1
     * 二分模板系列以及滑动窗口系列，可以参考：
     * https://labuladong.online/algo/essential-technique/binary-search-framework/
     * https://labuladong.online/algo/essential-technique/sliding-window-framework/
     * while内部判断的条件是区间内是否包含数据
     * 采用不同的闭区间、开区间、左闭右开区间的判断条件写法是不同的：
     * lt <= rt、lt + 1 < rt、lt < rt
     */
    private int findTargetAsc(int[] nums, int target) {
        int lt = 0, rt = nums.length - 1;
        while (lt <= rt) {
            int mid = (lt + rt) >> 1;
            if (nums[mid] > target) {
                rt = mid - 1;
            } else if (nums[mid] < target) {
                lt = mid + 1;
            } else if (nums[mid] == target) {
                return mid;
            }
        }
        return -1;
    }

    /**
     * 二分模板
     * 寻找target的左边界
     * while循环结束条件取决了是左闭右闭（<=），还是左闭右开（<）
     * 左闭右闭，返回的结果不一定是有效的索引
     * 如[1, 1], target = 2
     * 返回 idx = 2
     * 即，若target不存在于数组中，则返回的idx为插入target后target应在的索引
     * 对于lt和rt的定义可以从红蓝染色法的角度去理解
     * 另外，其实rightBound也可以通过target的转换来实现
     */
    private int leftBoundAsc(int[] nums, int target) {
        int lt = 0, rt = nums.length - 1;
        while (lt <= rt) {
            int mid = (lt + rt) >> 1;
            if (nums[mid] > target) {
                rt = mid - 1;
            } else if (nums[mid] < target) {
                lt = mid + 1;
            } else if (nums[mid] == target) {
                // 相等时，因为要找最左边界，所以尽量收缩右边界
                rt = mid - 1;
            }
        }
        // 返回左指针
        return lt;
    }

    /**
     * 二分模板
     * 寻找右边界
     * 左闭右闭
     * 返回的不一定是有效的索引
     * 如[1], target = 0
     * 返回 idx = -1
     * 即，若target不存在于数组中，则返回的idx + 1为插入target后target应在的索引
     */
    private int rightBoundAsc(int[] nums, int target) {
        int lt = 0, rt = nums.length - 1;
        while (lt <= rt) {
            int mid = (lt + rt) >> 1;
            if (nums[mid] > target) {
                rt = mid - 1;
            } else if (nums[mid] < target) {
                lt = mid + 1;
            } else {
                // 相等时，因为要找最右边界，所以尽量收缩左边界
                lt = mid + 1;
            }
        }
        // 返回右指针
        return rt;
    }

    /**
     * 二分模板
     * 寻找最左边界
     * 降序数组，左闭右闭
     */
    private int leftBoundDesc(int[] nums, int target) {
        int lt = 0, rt = nums.length - 1;
        while (lt <= rt) {
            int mid = (lt + rt) >> 1;
            if (nums[mid] > target) {
                lt = mid + 1;
            } else if (nums[mid] < target) {
                rt = mid - 1;
            } else if (nums[mid] == target) {
                // 相等时，因为要找最左边界，所以尽量收缩右边界
                rt = mid - 1;
            }
        }
        // 返回左指针
        return lt;
    }

    /**
     * 二分模板
     * 寻找最右边界
     * 降序数组，左闭右闭
     */
    private int rightBoundDesc(int[] nums, int target) {
        int lt = 0, rt = nums.length - 1;
        while (lt <= rt) {
            int mid = (lt + rt) >> 1;
            if (nums[mid] > target) {
                lt = mid + 1;
            } else if (nums[mid] < target) {
                rt = mid - 1;
            } else if (nums[mid] == target) {
                // 相等时，因为要找最右边界，所以尽量收缩左边界
                lt = mid + 1;
            }
        }
        // 返回右指针
        return rt;
    }

    /**
     * 二分模板
     * 左闭右开
     * 寻找左边界
     * 这里的记忆逻辑是
     * 由于右边是开区间，所以缩减右边界的逻辑是rt = mid就可以排除mid
     * 由于左边是闭区间，所以缩减左边界的逻辑是lt = mid + 1才可以避开mid
     */
    public int leftBoundLCloseROpen(int[] nums, int target) {
        int lt = 0, rt = nums.length;
        while (lt < rt) {
            int mid = (lt + rt) >> 1;
            if (nums[mid] < target) {
                lt = mid + 1;
            } else if (nums[mid] > target) {
                rt = mid;
            } else if (nums[mid] == target) {
                // 由于寻找最左边界，所以相等时优先缩减右边界
                rt = mid;
            }
        }
        return lt;
    }

    /**
     * 二分模板
     * 左闭右开
     * 寻找右边界
     * 这里的记忆逻辑是
     * 由于右边是开区间，所以缩减右边界的逻辑是rt = mid就可以排除mid
     * 由于左边是闭区间，所以缩减左边界的逻辑是lt = mid + 1才可以避开mid
     */
    public int rightBoundLCloseROpen(int[] nums, int target) {
        int lt = 0, rt = nums.length;
        while (lt < rt) {
            int mid = (lt + rt) >> 1;
            if (nums[mid] > target) {
                rt = mid;
            } else if (nums[mid] < target) {
                lt = mid + 1;
            } else if (nums[mid] == target) {
                // 由于寻找最右边界，所以相等时优先缩减左边界
                lt = mid + 1;
            }
        }
        // 注意这里返回的是lt - 1
        return lt - 1;
    }

    

}
