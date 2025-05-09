package leetcodeHot100;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LeetcodeHot100FourEx {

    /**
     * 两数之和
     */
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(target - nums[i])) {
                return new int[] { map.get(target - nums[i]), i };
            }
            map.put(nums[i], i);
        }
        return null;
    }

    /**
     * 字母异位词分组
     */
    public List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();
        for (String s : strs) {
            char[] chs = s.toCharArray();
            Arrays.sort(chs);
            String sortedString = new String(chs);
            List<String> list = map.getOrDefault(sortedString, new ArrayList<>());
            list.add(s);
            map.put(sortedString, list);
        }
        return map.values().stream().collect(Collectors.toList());
    }

    public List<List<String>> groupAnagramsV2(String[] strs) {
        return Arrays
                .stream(strs)
                .collect(Collectors
                        .groupingBy(s -> s
                                .chars()
                                .sorted()
                                .collect(StringBuilder::new, (sb, c) -> sb.append((char) c), StringBuilder::append)
                                .toString()))
                .values()
                .stream()
                .collect(Collectors.toList());
    }

    /**
     * 最长连续子序列
     */
    public int longestConsecutive(int[] nums) {
        Set<Integer> set = new HashSet<>();
        int max = 0;
        for (int num : nums) {
            set.add(num);
        }
        // 注意这里是set的遍历，不是nums
        for (int num : set) {
            if (set.contains(num - 1)) {
                continue;
            }
            int count = 0;
            while (set.contains(num)) {
                num++;
                count++;
            }
            max = Math.max(max, count);
        }
        return max;
    }

    public int longestConsecutiveV2(int[] nums) {
        Set<Integer> set = new HashSet<>();
        int[] max = new int[1];
        Arrays.stream(nums).forEach(num -> set.add(num));
        set.stream().forEach(num -> {
            if (set.contains(num - 1)) {
                return;
            }
            int count = 0;
            while (set.contains(num)) {
                num++;
                count++;
            }
            max[0] = Math.max(max[0], count);
        });
        return max[0];
    }
}
