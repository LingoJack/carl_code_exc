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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class Leetcodehot100ThreeEx {

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
        Map<String, List<String>> res = new HashMap<>();
        for (String s : strs) {
            char[] chs = s.toCharArray();
            Arrays.sort(chs);
            String sorted = new String(chs);
            res.putIfAbsent(sorted, new ArrayList<>());
            res.get(sorted).add(s);
        }
        return new ArrayList<>(res.values());
    }

    /**
     * 最长连续序列
     */
    public int longestConsecutive(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int num : nums) {
            set.add(num);
        }
        int res = 0;
        int count = 1;
        for (int num : set) {
            // 如果不是序列中最小的，就不往后查找
            if (set.contains(num - 1)) {
                continue;
            }
            while (set.contains(num + 1)) {
                num++;
                count++;
            }
            res = Math.max(res, count);
            count = 1;
        }
        return res;
    }

    /**
     * 移动零
     */
    public void moveZeroes(int[] nums) {
        int fast = 0;
        int slow = 0;
        int len = nums.length;
        while (fast < len) {
            if (nums[fast] != 0) {
                swap(nums, fast, slow);
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
     * 删除链表中的重复元素II
     */
    public ListNode deleteDuplicates(ListNode head) {
        // 1 2 3 3 4 4 5
        // _ 1 2 3 3 4 4 5 5
        // 1 1
        // n
        // 1 2 2
        // n
        ListNode dummy = new ListNode(0);
        ListNode last = dummy;
        ListNode node = head;
        while (node != null) {
            if (node.next == null || node.next.val != node.val) {
                ListNode newNode = new ListNode(node.val);
                last.next = newNode;
                last = newNode;
            } else {
                while (node != null && ((node.next != null && node.val == node.next.val) || node.next == null)) {
                    node = node.next;
                }
            }
            if (node != null) {
                node = node.next;
            }
        }
        return dummy.next;
    }

    /**
     * 最大交换
     * ac 本题的关键是利用后缀最大值表和倒排索引
     */
    public int maximumSwap(int num) {
        List<Integer> list = new ArrayList<>();
        while (num > 0) {
            list.add(0, num % 10);
            num /= 10;
        }
        // 2 7 3 6
        // s
        // f
        int max = Integer.MIN_VALUE;
        int[] suffixMax = new int[list.size()];
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            map.put(list.get(i), i);
            suffixMax[list.size() - 1 - i] = Math.max(i == 0 ? Integer.MIN_VALUE : suffixMax[list.size() - i],
                    list.get(list.size() - 1 - i));
        }
        for (int i = 0; i < list.size(); i++) {
            max = suffixMax[i];
            if (!list.get(i).equals(max)) {
                if (map.get(max) > i) {
                    swap(list, i, map.get(max));
                    break;
                }
            }
        }
        int sum = 0;
        int power = 0;
        int index = list.size() - 1;
        while (index >= 0) {
            sum += list.get(index--) * Math.pow(10, power++);
        }
        return sum;
    }

    private void swap(List<Integer> list, int idx1, int idx2) {
        int t = list.get(idx1);
        list.set(idx1, list.get(idx2));
        list.set(idx2, t);
    }

    /**
     * 盛最多水的容器
     */
    public int maxArea(int[] height) {
        int len = height.length;
        int lt = 0;
        int rt = len - 1;
        int max = 0;
        while (rt > lt) {
            int h = Math.min(height[lt], height[rt]);
            int w = rt - lt;
            max = Math.max(max, w * h);
            if (height[rt] < height[lt]) {
                rt--;
            } else {
                lt++;
            }
        }
        return max;
    }

    /**
     * 三数之和
     */
    public List<List<Integer>> threeSum(int[] nums) {
        // -1,0,1,2,-1,-4
        // -4 -1 -1 0 1 2
        // i l r
        Arrays.sort(nums);
        int len = nums.length;
        List<List<Integer>> res = new ArrayList<>();
        for (int i = 0; i <= len - 2; i++) {
            if (i >= 1 && nums[i] == nums[i - 1]) {
                continue;
            }
            int target = -nums[i];
            int lt = i + 1;
            int rt = len - 1;
            while (lt < rt) {
                int sum = nums[lt] + nums[rt];
                if (sum > target) {
                    rt--;
                    while (lt < rt && nums[rt] == nums[rt + 1]) {
                        rt--;
                    }
                } else if (sum < target) {
                    lt++;
                    while (lt < rt && nums[lt] == nums[lt - 1]) {
                        lt++;
                    }
                } else {
                    res.add(List.of(nums[i], nums[lt], nums[rt]));
                    lt++;
                    while (lt < rt && nums[lt] == nums[lt - 1]) {
                        lt++;
                    }
                }
            }
        }
        return res;
    }

    /**
     * 接雨水
     */
    public int trap(int[] height) {
        int area = 0;
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 0; i < height.length; i++) {
            while (!stack.isEmpty() && height[stack.peek()] < height[i]) {
                int low = height[stack.pop()];
                int h = stack.isEmpty() ? 0 : Math.min(height[i], height[stack.peek()]) - low;
                int w = stack.isEmpty() ? 0 : i - stack.peek() - 1;
                area += w * h;
            }
            stack.push(i);
        }
        return area;
    }

    /**
     * 无重复字符的最长子串
     * 之前都对了，这次反倒没做出来
     * 因为我看了滑动窗口的框架反倒错了???
     * 不过稍微改一下又对了，还是按照模板来写
     */
    public int lengthOfLongestSubstring(String s) {
        char[] str = s.toCharArray();
        int lt = 0;
        int rt = 0;
        int max = 0;
        int len = s.length();
        Map<Character, Boolean> exist = new HashMap<>();
        while (rt < len) {
            exist.put(str[rt], true);
            max = Math.max(max, rt - lt + 1);
            rt++;
            while (rt < len && exist.getOrDefault(str[rt], false)) {
                exist.put(str[lt++], false);
            }
        }
        return max;
    }

    /**
     * 找到字符串中所有字母异位词
     * 滑动窗口我很不熟
     * 没做出来
     * 操，但好像是我把这题和最小覆盖子串那题记混了
     * 不过都是用滑动窗口来做的，之前我的做法十分的混乱
     * 而实际上是有方法论的
     * 扩大后的效果 -> 判断是否加入结果集 -> rt++ -> 缩小后的效果 -> lt++
     */
    public List<Integer> findAnagrams(String s, String p) {
        Map<Character, Integer> need = new HashMap<>();
        Map<Character, Integer> contain = new HashMap<>();
        for (char c : p.toCharArray()) {
            need.put(c, need.getOrDefault(c, 0) + 1);
        }
        int valid = 0;
        int lt = 0, rt = 0;
        List<Integer> list = new ArrayList<>();
        while (rt < s.length()) {
            // 扩大
            char rc = s.charAt(rt);
            if (need.containsKey(rc)) {
                contain.put(rc, contain.getOrDefault(rc, 0) + 1);
                if (contain.get(rc).equals(need.get(rc))) {
                    valid++;
                }
                if (valid == need.size()) {
                    list.add(lt);
                }
            }
            rt++;
            // 缩小
            while (rt - lt + 1 > p.length()) {
                char lc = s.charAt(lt);
                if (need.containsKey(lc)) {
                    if (contain.get(lc).equals(need.get(lc))) {
                        valid--;
                    }
                    contain.put(lc, contain.get(lc) - 1);
                }
                lt++;
            }
        }
        return list;
    }

    /**
     * 
     */
}
