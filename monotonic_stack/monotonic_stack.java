package monotonic_stack;

import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;

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

}
