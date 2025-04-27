package lingshen_question_list;

public class rolling_window {

    /**
     * 和相同的二元子数组
     */
    public int numSubarraysWithSum(int[] nums, int goal) {
        // 1 0 1 0 1
        // 1 1 2 2 3
        // l
        // r
        int len = nums.length;
        int[] prefix = new int[len];
        prefix[0] = nums[0];
        for (int i = 1; i < len; i++) {
            prefix[i] = prefix[i - 1] + nums[i];
        }
        int rt = len - 1;
        int count = 0;
        while (rt >= 0) {
            int lt = 0;
            while (lt <= rt && prefix[rt] - prefix[lt] >= goal) {
                if (prefix[rt] - prefix[lt] == goal) {
                    count++;
                }
                lt++;
            }
            rt--;
        }
        return count;
    }
}
