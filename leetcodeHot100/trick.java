package leetcodeHot100;

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
}
