import java.util.Scanner;

/**
 * 数组篇总结:
 * <a href="https://www.programmercarl.com/%E6%95%B0%E7%BB%84%E6%80%BB%E7%BB%93%E7%AF%87.html#%E6%95%B0%E7%BB%84%E7%9A%84%E7%BB%8F%E5%85%B8%E9%A2%98%E7%9B%AE">...</a>
 */
public class array {

    public static void main(String[] args) {
        // [-1,0,3,5,9,12]
        generateMatrix(4);
    }

    /**
     * 二分查找
     * 需要注意的是，关注区间的定义这个不变量来分清边界判断
     * 注意
     * tail = mid - 1;
     * head = mid + 1;
     * 布什，戈门？手撕二分没有撕出来，给我气笑了...
     */
    public int search(int[] nums, int target) {
        int tail = nums.length - 1;
        int head = 0;

        int mid = (tail + head) / 2;
        while (tail >= head) {
            if (nums[mid] > target) {
                tail = mid - 1;
            }
            else if (nums[mid] < target) {
                head = mid + 1;
            }
            else {
                return mid;
            }
            mid = (head + tail) / 2;
        }
        return -1;
    }


    /**
     * 移除元素
     * 著名的双指针思想
     * 大体思路就是：
     * 一个快指针，一个慢指针，快指针去找符合要求的值，然后换给慢指针，然后慢指针往后移动一位
     * 这么一来，当快指针遍历完整个数组，就把符合要求的都移到前面去了，同时慢指针就是符合条件的值的个数
     * " 快排的本质是双指针 "
     */
    public int removeElement(int[] nums, int val) {
        int len = nums.length;
        int rt = 0;
        int lt = 0;

        while (rt < len) {
            if (nums[rt] != val) {
                nums[lt] = nums[rt];
                lt++;
            }
            rt++;
        }
        return lt;
    }

    /**
     * 有序数组的平方
     * 本题解决主要是用双指针从两端向中间靠拢，
     * 比较平方值，选择小的移入结果数组，有点类似二路归并
     * 想得到么，牢弟
     */
    public int[] sortedSquares(int[] nums) {
        int[] res = new int[nums.length];
        int lt = 0;
        int rt = nums.length - 1;
        int index = nums.length - 1;
        while (lt <= rt) {
            nums[lt] = nums[lt] < 0 ? -nums[lt] : nums[lt];
            nums[rt] = nums[rt] < 0 ? -nums[rt] : nums[rt];

            if (nums[lt] >= nums[rt]) {
                res[index] = nums[lt] * nums[lt];
                lt++;
            }
            else {
                res[index] = nums[rt] * nums[rt];
                rt--;
            }

            index--;
        }
        return res;
    }

    /**
     * 长度最小的子数组
     * 找出该数组中满足其总和大于等于 target 的长度最小的子数组，返回其长度
     * 好好想想 双指针 快的是找符合条件的 在本题滑动窗口中代表着什么
     */
    public int minSubArrayLen(int target, int[] nums) {
        int lt = 0;              // 左指针
        int rt = 0;              // 右指针
        int sum = 0;            // 当前窗口的和
        int minLen = Integer.MAX_VALUE; // 记录最小长度

        // 使用滑动窗口扩展右指针
        while (rt < nums.length) {
            sum += nums[rt];    // 增加右指针的元素
            rt++;               // 右指针向右移动

            // 当前窗口和大于等于 target，尝试缩小窗口
            while (sum >= target) {
                minLen = Math.min(minLen, rt - lt); // 更新最小长度
                sum -= nums[lt]; // 缩小窗口，减少左指针的元素
                lt++;            // 左指针向右移动
            }
        }

        // 如果没有找到合适的子数组，则返回0
        return minLen == Integer.MAX_VALUE ? 0 : minLen;
    }


    /**
     * 螺旋矩阵II
     * 给你一个正整数 n ，生成一个包含 1 到 n2 所有元素，且元素按顺时针顺序螺旋排列的 n x n 正方形矩阵 matrix 。
     * 自己目前对代码掌控力还是太弱了...
     */
    public static int[][] generateMatrix(int n) {
        // 初始化四个边界
        int leftBoundary = 0, rightBoundary = n - 1;
        int topBoundary = 0, bottomBoundary = n - 1;

        // 当前要填入的数字和目标数字
        int currentNumber = 1;
        int targetNumber = n * n;

        // 结果矩阵
        int[][] matrix = new int[n][n];

        // 当 currentNumber 小于等于目标数字时继续循环
        while (currentNumber <= targetNumber) {

            // 从左到右填充顶行
            for (int column = leftBoundary; column <= rightBoundary; column++) {
                matrix[topBoundary][column] = currentNumber++;
            }
            // 填完顶行后，缩小上边界，顶行已经填完了
            topBoundary++;

            // 从上到下填充右列
            for (int row = topBoundary; row <= bottomBoundary; row++) {
                matrix[row][rightBoundary] = currentNumber++;
            }
            // 填完右列后，缩小右边界，右列已经填完了
            rightBoundary--;

            // 从右到左填充底行（确保当前行依然存在）
            if (topBoundary <= bottomBoundary) {
                for (int column = rightBoundary; column >= leftBoundary; column--) {
                    matrix[bottomBoundary][column] = currentNumber++;
                }
                // 填完底行后，缩小下边界，底行已经填完了
                bottomBoundary--;
            }

            // 从下到上填充左列（确保当前列依然存在）
            if (leftBoundary <= rightBoundary) {
                for (int row = bottomBoundary; row >= topBoundary; row--) {
                    matrix[row][leftBoundary] = currentNumber++;
                }
                // 填完左列后，缩小左边界，左列已经填完了
                leftBoundary++;
            }
        }

        // 返回生成的螺旋矩阵
        return matrix;
    }

    /**
     * ACM模式
     * 区间和
     * 居然一次过做对了，不得了
     */
    public static void subIntervalSum() {

        Scanner sc = new Scanner(System.in);

        int len = 0;
        len = sc.nextInt();

        int[] array = new int[len];
        int[] sumArray = new int[len];


        for (int i = 0; i < len; i++) {
            array[i] = sc.nextInt();
            if (i == 0) {
                sumArray[0] = array[0];
            }
            else {
                sumArray[i] = sumArray[i - 1] + array[i];
            }
        }

        int left = 0;
        int right = 0;

        do {
            left = sc.nextInt();
            right = sc.nextInt();

            System.out.println(sumArray[right] - sumArray[left] + array[left]);
        } while (sc.hasNext());

        sc.close();
    }

    /**
     * ACM模式
     * 开发商购买土地
     * 差不多是 二维的区间和
     * 第一次在提示下想到的是前缀和，但是没有编码实现...
     */
    public static void buyLand() {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        int sum = 0;
        int[][] vec = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                vec[i][j] = scanner.nextInt();
                sum += vec[i][j];
            }
        }

        // 统计横向
        int[] horizontal = new int[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                horizontal[i] += vec[i][j];
            }
        }

        // 统计纵向
        int[] vertical = new int[m];
        for (int j = 0; j < m; j++) {
            for (int i = 0; i < n; i++) {
                vertical[j] += vec[i][j];
            }
        }

        int result = Integer.MAX_VALUE;
        int horizontalCut = 0;
        for (int i = 0; i < n; i++) {
            horizontalCut += horizontal[i];
            result = Math.min(result, Math.abs(sum - 2 * horizontalCut));
        }

        int verticalCut = 0;
        for (int j = 0; j < m; j++) {
            verticalCut += vertical[j];
            result = Math.min(result, Math.abs(sum - 2 * verticalCut));
        }

        System.out.println(result);
        scanner.close();
    }
}
