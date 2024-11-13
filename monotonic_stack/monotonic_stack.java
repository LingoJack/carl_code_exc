package monotonic_stack;

import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.Stack;

public class monotonic_stack {
    /**
     * 每日温度
     * 初见能想到的只有双指针暴力解 + 剪枝了
     * 单调栈是什么东东，瓦达西不懂思密达
     */
    public int[] dailyTemperaturesBruteForce(int[] temperatures) {
        int[] res = new int[temperatures.length];
        
        if (temperatures.length == 1) {
            return new int[]{0};
        }

        int slow = 0;
        int fast = 0;
        int count = 0;
        // 73,74,75,71,69,72,76,73
        for(int i = 0; i < temperatures.length - 1; i++) {
            slow = i;
            fast = i;
            if (slow > 0 && temperatures[slow] == temperatures[slow - 1]) {
                res[i] = res[i - 1] - 1 > 0 ? res[i - 1] - 1 : 0;
                continue;
            }
            while (fast < temperatures.length && temperatures[fast] <= temperatures[slow]) {
                fast++;
                count++;
                if (fast == temperatures.length) {
                    count = 0;
                }
            }
            res[i] = count;
            count = 0;
        }
        res[temperatures.length - 1] = 0;
        return res;
    }

    /**
     * 每日温度
     * 单调栈解法
     * 维护一个从栈顶到栈底趋势递增的单调栈
     * 栈存储下标
     * 随想录说：什么时候会想到用单调栈？
     * 答曰：通常是一维数组，要寻找任一个元素的右边或者左边第一个比自己大或者小的元素的位置，此时我们就要想到可以用单调栈了
     * 这个和KMP算法用在查一个模式串是否在目标串中出现过一样，可以当成一个API存储记忆
     * 尊嘟很巧妙
     */
    public int[] dailyTemperatures(int[] temperatures) {
        int[] res = new int[temperatures.length];
        
        // 维护的从栈顶到栈底的趋势递增的单调栈
        ArrayDeque<Integer> monotonicStack = new ArrayDeque<>();

        for(int i = 0; i < temperatures.length; i++) {
            // 维护单调栈从栈顶到栈底趋势递增的性质，遇到小的就弹出来，更新res数组
            while (!monotonicStack.isEmpty() && temperatures[i] > temperatures[monotonicStack.peek()]) {
                int index = monotonicStack.pop();
                res[index] = i - index;
            }
            // temperatures[i] <= temperatures[monotonicStack.peek()]
            monotonicStack.push(i);
        }

        return res;
    }

    /**
     * 下一个更大元素 I
     * 属于是刚学完单调栈的趁热打铁了，拿下
     */
    public int[] nextGreaterElement(int[] nums1, int[] nums2) {
        // 记录i位置的下一个比i大的为数的索引
        int[] firstLargerIndexArray = new int[nums2.length];

        // 维护的从栈顶到栈底的趋势递增的单调栈
        ArrayDeque<Integer> monotonicStack = new ArrayDeque<>();
        
        // 倒排索引，键为nums2[i]，值为i，这里用来确定nums1的数在nums2中的位置以便在firstLargerIndexArray取值操作
        HashMap<Integer, Integer> map = new HashMap<>();
        
        for(int i = 0; i < nums2.length; i++) {
            map.put(nums2[i], i);
            // 维护单调栈从栈顶到栈底趋势递增的性质，遇到小的就弹出来，更新firstLargeIndexArray数组
            while (!monotonicStack.isEmpty() && nums2[i] > nums2[monotonicStack.peek()]) {
                int index = monotonicStack.pop();
                firstLargerIndexArray[index] = i;
            }
            monotonicStack.push(i);
        }

        int[] res = new int[nums1.length];
        for(int i = 0; i < nums1.length; i++) {
            int index = firstLargerIndexArray[map.get(nums1[i])];
            res[i] = index == 0 ? -1 : nums2[index];
        }

        return res;
    }

    /**
     * 下一个更大元素II
     * 与上一题不同的是，这是一个循环数组
     * 所以区别也就是最后一个数字，那么我是不是可以考虑在整个数组加一个数字
     * 或者写一个两倍长度的重复的新数组
     * 也是拿下了，这里由于数组就是原数组，所以不用像上一题一样用倒排索引
     */
    public int[] nextGreaterElements(int[] nums) {
        // 1 2 1 
        // 1 2 1 1 2 1
        // 2 -1 2
        int[] circleArray = new int[2 * nums.length];
        for(int i = 0; i < nums.length; i++) {
            circleArray[i] = nums[i];
            circleArray[i + nums.length] = nums[i];
        }

        // 记录i位置的下一个比i大的为数的索引
        int[] firstLargerIndexArray = new int[circleArray.length];
        Arrays.fill(firstLargerIndexArray, -1);

        ArrayDeque<Integer> monotonicStack = new ArrayDeque<>();
        for(int i = 0; i < circleArray.length; i++) {
            // 维护单调栈从栈顶到栈底趋势递增的性质，遇到小的就弹出来，更新firstLargeIndexArray数组
            while (!monotonicStack.isEmpty() && circleArray[i] > circleArray[monotonicStack.peek()]) {
                int index = monotonicStack.pop();
                firstLargerIndexArray[index] = i;
            }
            monotonicStack.push(i);
        }

        int[] res = new int[nums.length];

        for(int i = 0; i < nums.length; i++) {
            res[i] = firstLargerIndexArray[i] == -1 ? -1 : nums[firstLargerIndexArray[i] % nums.length];
        }

        return res;
    }

    /**
     * 接雨水
     * 未见其题，先闻其名，害怕。。。
     * 这是我遇到的第二个hard
     * 思路是导数、极大值点
     * 这个思路不好做，因为不是只考虑相邻的就可以的
     * 改了思路，但是超出时间限制了
     * 这个题的核心就是意识到：
     * 如果打算按列来计算雨水
     * 求某个列的储水量就是找到该列左右两边的最大值
     */
    public int trapTimeExceedError(int[] height) {
        int res = 0;
        // 大到小0是峰
        // 0 1  0  2  1  0 1 3  2  1 2  1
        //   1 -1  2 -1 -1 1 2 -1 -1 1 -1
        //   f     f         f       f
        //   x     x         x       x         
        //      1     1  2 1       1     

        // 极大值点索引
        ArrayList<Integer> queue = new ArrayList<>();

        int[] dy = new int[height.length + 1];
        
        for(int i = 1; i < height.length; i++) {
            dy[i] = height[i] - height[i - 1];
            if (i > 1 && i < height.length && dy[i] <= 0 && dy[i - 1] > 0) {
                queue.add(i - 1);
            }
        }

        for(int i = 0; i < height.length; i++) {
            int lt = 0;
            int rt = height.length - 1;
            for(Integer index : queue) {
                if (index >= i) {
                    rt = height[rt] < height[index] ? index : rt;   
                }
                else if (index <= i) {
                    lt = height[lt] < height[index] ? index : lt;
                }
            }
            int low = Math.min(height[lt], height[rt]);
            res += Math.max(0, low - height[i]);
        }

        return res;
    }

    /**
     * 接雨水
     * 动归解法
     * 记录每个位置左边和右边最高的数的索引
     * 这个题的核心就是意识到：
     * 如果打算按列来计算雨水
     * 求某个列的储水量就是找到该列左右两边的最大值
     */
    public int trapWithDynamicProgram(int[] height) {
        if (height.length == 0) {
            return 0;
        }
    
        int res = 0;
        int[] leftMax = new int[height.length];
        int[] rightMax = new int[height.length];
    
        // 初始化 leftMax 和 rightMax 数组
        leftMax[0] = height[0];
        for (int i = 1; i < height.length; i++) {
            // i位置左边最大的一定在i-1位置左边最大的或者i本身之中产生
            leftMax[i] = Math.max(leftMax[i - 1], height[i]);
        }
    
        rightMax[height.length - 1] = height[height.length - 1];
        for (int i = height.length - 2; i >= 0; i--) {
            // i位置右边最大的一定在i+1位置右边最大的或者i本身之中产生
            rightMax[i] = Math.max(rightMax[i + 1], height[i]);
        }
    
        // 计算接雨水总量
        for (int i = 1; i < height.length - 1; i++) {
            int waterLevel = Math.min(leftMax[i], rightMax[i]);
            res += Math.max(0, waterLevel - height[i]);
        }
    
        return res;
    }
    
    /**
     * 接雨水
     * 单调栈的思路是按行计算雨水的
     */
    public int trap(int[] height) {
        int res = 0;
        Deque<Integer> monotonicStack = new ArrayDeque<>();
        
        // 对于每一根柱子
        for (int i = 0; i < height.length; i++) {
            // 若栈里有柱子，且当前的柱子比栈顶的柱子高
            while (!monotonicStack.isEmpty() && height[i] > height[monotonicStack.peek()]) {
                
                // 弹出并获取栈顶柱子的高度
                int bottom = monotonicStack.pop(); // 低洼处
                
                // 如果栈为空，说明没有左边界，跳出
                if (monotonicStack.isEmpty()) {
                    break;
                }
                
                int left = monotonicStack.peek(); // 左边界
                int width = i - left - 1; // 左右边界之间的宽度
                int heightDifference = Math.min(height[left], height[i]) - height[bottom];
                
                res += width * heightDifference;
            }
            
            // 直到栈顶柱子比栈里比当前柱子高，或栈里没有柱子
            monotonicStack.push(i);
        }
        
        return res;
    }

    /**
     * 柱状图中最大的矩形
     * 遇到的第三个hard
     * 2 1 5 6 2 3
     * 矩形的高一定在数组里
     * 宽一定在[1, len]里
     * 若矩形以2为高，那么宽就是从2开始连续大于等于2的矩形的个数，
     * 也就是要去找右边第一个高度小于2的矩形距离2开头的矩形的距离
     * 这个可以采用单调栈（栈顶到栈低递减）来实现
     * 我的思路是对了，只是实现错了
     * 对单调栈的理解还应该继续加深
     * 应该以heights[stack.peek()]为主视角考虑大小关系
     * 对于数组的拷贝，Java有一个可以用的API：System.arraycopy
     */
    public int largestRectangleArea(int[] heights) {
        int maxArea = 0;
        int len = heights.length;

        // i位置左右第一个小于i位置数的数的索引
        int[] rightFirstSmaller = new int[len];
        int[] leftFirstSmaller = new int[len];

        Arrays.fill(rightFirstSmaller, len);
        Arrays.fill(leftFirstSmaller, -1);

        Deque<Integer> stack = new ArrayDeque<>();

        for(int i = 0; i < len; i++) {
            while (!stack.isEmpty() && heights[stack.peek()] > heights[i]) {
                int index = stack.pop();
                rightFirstSmaller[index] = i;
            }
            stack.push(i);
        }

        stack.clear();

        for(int i = len - 1;i >= 0; i--) {
            while (!stack.isEmpty() && heights[stack.peek()] > heights[i]) {
                int index = stack.pop();
                leftFirstSmaller[index] = i;
            }
            stack.push(i);
        }

        for(int i = 0; i < len; i++) {
            int height = heights[i];
            int lw = leftFirstSmaller[i];
            int rw = rightFirstSmaller[i];
            int width = rw - lw - 1;  // 计算宽度
            maxArea = Math.max(maxArea, width * height);
        }

        return maxArea;
    }


}
