package leetcodeHot100;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class monotonic_stack {
    /**
     * 接雨水
     */
    public int trap(int[] height) {
        // 栈顶到栈底递增，也就是说，如果是小的，可以直接放入栈顶
        // 大的则需要把比它小的先弹出来
        Deque<Integer> monotonicStack = new ArrayDeque<>();
        int res = 0;
        for (int i = 0; i < height.length; i++) {
            while (!monotonicStack.isEmpty() && height[monotonicStack.peek()] <= height[i]) {
                // 当前的柱子比栈顶的柱子高，而栈顶到栈底的柱子是递增的，所以会形成低洼
                int bottom = monotonicStack.pop();
                Integer leftIndex = monotonicStack.peek();
                if (leftIndex == null) {
                    // 若没有左边界，则形不成积水
                    continue;
                }
                int w = i - leftIndex - 1;
                int h = Math.min(height[leftIndex], height[i]) - height[bottom];
                res += w * h;
            }
            // 如果当前柱子比栈顶的柱子小，则符合的递增栈的性质，直接放入
            monotonicStack.push(i);
        }
        return res;
    }

    /**
     * 每日温度
     * 6min ac
     */
    public int[] dailyTemperatures(int[] temperatures) {
        int[] res = new int[temperatures.length];
        // 栈顶到栈底递增，存储下标
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(0);
        for (int i = 1; i < temperatures.length; i++) {
            while (!stack.isEmpty() && temperatures[i] > temperatures[stack.peek()]) {
                Integer index = stack.pop();
                res[index] = i - index;
            }
            stack.push(i);
        }
        return res;
    }

    /**
     * 柱状图中的最大矩形
     */
    public int largestRectangleArea(int[] heights) {
        int max = Integer.MIN_VALUE;
        int len = heights.length;
        int[] area = new int[len];
        // 栈顶到栈底递减的栈，存储下标，即，栈顶元素的最小的
        Deque<Integer> stack = new ArrayDeque<>();
        // 遍历以i为最开始的柱子的情况
        for (int i = 0; i < len; i++) {
            // 找右边第一根比i低的柱子
            while (!stack.isEmpty() && heights[stack.peek()] > heights[i]) {
                int startIndex = stack.pop();
                int wid = i - startIndex;
                int height = heights[startIndex];
                area[startIndex] += wid * height;
            }
            stack.push(i);
        }
        while (!stack.isEmpty()) {
            int startIndex = stack.pop();
            int wid = len - startIndex;
            area[startIndex] += wid * heights[startIndex];
        }
        for (int i = len - 1; i >= 0; i--) {
            // 找左边第一根比i低的柱子
            while (!stack.isEmpty() && heights[stack.peek()] > heights[i]) {
                int startIndex = stack.pop();
                int wid = startIndex - i - 1;
                int height = heights[startIndex];
                area[startIndex] += wid * height;
                max = Math.max(max, area[startIndex]);
            }
            stack.push(i);
        }
        while (!stack.isEmpty()) {
            int startIndex = stack.pop();
            int wid = startIndex;
            area[startIndex] += wid * heights[startIndex];
            max = Math.max(max, area[startIndex]);
        }
        return max;
    }
}
