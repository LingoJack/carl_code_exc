package leetcodeHot100;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
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

    /**
     * 移动零
     */
    public void moveZeroes(int[] nums) {
        int slow = 0, fast = 0;
        while (fast < nums.length) {
            if (nums[fast] != 0) {
                swap(nums, slow, fast);
                slow++;
            }
            fast++;
        }
    }

    private void swap(int[] nums, int a, int b) {
        int t = nums[a];
        nums[a] = nums[b];
        nums[b] = t;
    }

    /**
     * 盛最多水的容器
     */
    public int maxArea(int[] height) {
        int lt = 0, rt = height.length - 1;
        int max = 0;
        while (lt < rt) {
            int wid = rt - lt;
            int hei = Math.min(height[lt], height[rt]);
            max = Math.max(max, wid * hei);
            if (height[lt] < height[rt]) {
                lt++;
            } else {
                rt--;
            }
        }
        return max;
    }

    /**
     * 三数之和
     */
    public List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);
        int len = nums.length;
        List<List<Integer>> res = new ArrayList<>();
        int first = 0;
        while (first < len) {
            int target = -nums[first];
            int second = first + 1;
            int third = len - 1;
            while (second < third) {
                int sum = nums[second] + nums[third];
                if (sum < target) {
                    second++;
                    while (second < len - 1 && second > first && nums[second] == nums[second - 1]) {
                        second++;
                    }
                } else if (sum > target) {
                    third--;
                    while (third > 1 && third < len - 1 && nums[third] == nums[third + 1]) {
                        third--;
                    }
                } else {
                    res.add(List.of(nums[first], nums[second], nums[third]));
                    second++;
                    while (second < len - 1 && second > first && nums[second] == nums[second - 1]) {
                        second++;
                    }
                }
            }
            first++;
            while (first < len && first > 0 && nums[first] == nums[first - 1]) {
                first++;
            }
        }
        return res;
    }

    /**
     * 接雨水
     * 单调栈解法
     */
    public int trap(int[] height) {
        Deque<Integer> stack = new ArrayDeque<>();
        int res = 0;
        for (int i = 0; i < height.length; i++) {
            while (!stack.isEmpty() && height[stack.peek()] < height[i]) {
                int j = stack.pop();
                if (stack.isEmpty()) {
                    break;
                }
                int k = stack.peek();
                int wid = i - k - 1;
                int hei = Math.min(height[i], height[k]) - height[j];
                res += wid * hei;
            }
            stack.push(i);
        }
        return res;
    }
}
