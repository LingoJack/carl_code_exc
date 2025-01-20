package leetcodeHot100;

import java.util.Arrays;

public class trick {

    /**
     * 只出现一次的数字
     * 这是异或运算的技巧
     */
    public int singleNumber(int[] nums) {
        int res = 0;
        for(int num : nums) {
            res ^= num;
        }
        return res;
    }

    /**
     * 寻找重复数
     */
    public int findDuplicate(int[] nums) {
        boolean[] existed = new boolean[nums.length];
        for(int num : nums) {
            if (existed[num]) {
                return num;
            }
            existed[num] = true;
        }
        return -1;
    }

    /**
     * 多数元素
     */
    public int majorityElement(int[] nums) {
        int len = nums.length;
        int count = 0;
        Arrays.sort(nums);
        for(int i = 0; i < len; i++) {
            if(i >= 1 && nums[i] == nums[i - 1]) {
                count++;
            }
            else {
                count = 0;
            }
            if(count >= len / 2) {
                return nums[i];
            }
        }
        return -1;
    }

    /**
     * 多数元素
     * 同归于尽消杀法
     * count理解成占领地的士兵数
     */
    public int majorityElementAwesomeSolution(int[] nums) {
        int winner = nums[0];
        int count = 0;
        for(int num : nums) {
            if(count == 0) {
                winner = num;
            }
            if(winner == num) {
                count++;
            }
            else {
                count--;
            }
        }
        return winner;
    }
}
