package lingshen_question_list;

import java.util.HashMap;
import java.util.Map;

public class rolling_window {

    /**
     * 和相同的二元子数组
     * 这题没做出来，不应该的
     * 你有点废物了
     * 这是非滑动窗口的做法
     */
    public int numSubarraysWithSum(int[] nums, int goal) {
        Map<Integer, Integer> map = new HashMap<>();
        int sum = 0;
        map.put(0, 1);
        int count = 0;
        for (int num : nums) {
            sum += num;
            count += map.getOrDefault(sum - goal, 0);
            map.put(sum, map.getOrDefault(sum, 0) + 1);
        }
        return count;
    }

    /**
     * 和相同的二元子数组
     * 滑动窗口解法
     */
    public int numSubarraysWithSumWithRollingWindow(int[] nums, int goal) {
        
    }
}
