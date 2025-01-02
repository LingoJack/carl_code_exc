package leetcodeHot100;

import java.util.ArrayDeque;
import java.util.Deque;

public class monotonic_stack {
    /**
     * 接雨水
     */
    public int trap(int[] height) {
        // 栈顶到栈底递增，也就是说，如果是小的，可以直接放入栈顶
        // 大的则需要把比它小的先弹出来
        Deque<Integer> monotonicStack = new ArrayDeque<>();
        int res = 0;
        for(int i = 0; i < height.length; i++) {
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
}
