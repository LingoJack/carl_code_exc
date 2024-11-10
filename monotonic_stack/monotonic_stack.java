package monotonic_stack;

import java.util.ArrayDeque;

public class monotonic_stack {
    /**
     * 每日温度
     * 初见能想到的只有双指针暴力解 + 剪枝了
     * 单调栈是什么东东，瓦达西不懂思密达
     */
    public int[] dailyTemperatures(int[] temperatures) {
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
}
