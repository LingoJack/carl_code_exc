package leetcodeHot100;

import java.util.HashMap;
import java.util.Map;

public class hash {
    
    /**
     * 两数之和
     * 这次二刷注意到了一个重要的点，
     * 就是put操作必须放在if检查之后
     * 否则元素会被重复使用
     */
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for(int i = 0; i < nums.length; i++) {
            Integer idx = map.get(nums[i]);
            if (idx != null) {
                return new int[]{idx, i};
            }
            // 这一句必须要放在if检查之后，不然会出现元素的重复使用
            map.put(target - nums[i], i);
        }
        return new int[2];
    }
}
