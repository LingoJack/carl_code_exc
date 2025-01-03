package leetcodeHot100;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class hash {

    /**
     * 两数之和
     * 这次二刷注意到了一个重要的点，
     * 就是put操作必须放在if检查之后
     * 否则元素会被重复使用
     */
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            Integer idx = map.get(nums[i]);
            if (idx != null) {
                return new int[] { idx, i };
            }
            // 这一句必须要放在if检查之后，不然会出现元素的重复使用
            map.put(target - nums[i], i);
        }
        return new int[2];
    }

    /**
     * 字母异位词分组
     * 9min ac
     * 但是时间复杂度并不优秀
     * 呃，好吧，时间复杂度不优秀是因为之前用了stream流
     * 改成return new ArrayList<>(map.values());就击败98.66%了
     */
    public List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();
        for (String str : strs) {
            char[] chars = str.toCharArray();
            Arrays.sort(chars);
            String sortedStr = new String(chars);
            List<String> list = map.getOrDefault(sortedStr, new ArrayList<>());
            list.add(str);
            map.put(sortedStr, list);
        }
        return new ArrayList<>(map.values());
    }

    /**
     * 字母异位词分组
     * 链式编程写法
     */
    public List<List<String>> groupAnagramsWithLinkCode(String[] strs) {
        return new ArrayList<>(Arrays.stream(strs)
                .collect(Collectors.groupingBy(s -> Arrays.toString(s.codePoints().sorted().toArray()))).values());
    }

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

}
