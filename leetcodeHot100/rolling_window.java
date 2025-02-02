package leetcodeHot100;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.stream.Stream;

public class rolling_window {

    /**
     * 无重复字符的最长子串
     * 7min ac
     */
    public int lengthOfLongestSubstring(String s) {
        if (s == null || s.isEmpty()) {
            return 0;
        }
        int slow = 0;
        int fast = 0;
        int len = s.length();
        int res = 0;
        Map<Character, Boolean> contain = new HashMap<>();
        while (fast < len) {
            while (contain.getOrDefault(s.charAt(fast), false)) {
                contain.put(s.charAt(slow), false);
                slow++;
            }
            contain.put(s.charAt(fast), true);
            res = Math.max(res, fast - slow + 1);
            fast++;
        }
        return res;
    }

    /**
     * 找到字符串中所有字母异位词
     * 思路粗暴，没做出来
     * 核心是用数组作为map去统计每个字符出现的次数
     */
    public List<Integer> findAnagrams(String s, String p) {
        int sLen = s.length(), pLen = p.length();
        if (sLen < pLen) {
            return new ArrayList<Integer>();
        }
        List<Integer> res = new ArrayList<Integer>();
        int[] sCount = new int[26];
        int[] pCount = new int[26];
        for (int i = 0; i < pLen; ++i) {
            ++sCount[s.charAt(i) - 'a'];
            ++pCount[p.charAt(i) - 'a'];
        }

        if (Arrays.equals(sCount, pCount)) {
            res.add(0);
        }

        for (int i = 0; i < sLen - pLen; ++i) {
            --sCount[s.charAt(i) - 'a'];
            ++sCount[s.charAt(i + pLen) - 'a'];
            if (Arrays.equals(sCount, pCount)) {
                res.add(i + 1);
            }
        }
        return res;
    }

    /**
     * 最小覆盖子串
     * 没做出来
     */
    public String minWindow(String s, String t) {
        Map<Character, Integer> tCharCount = new HashMap<>();
        for (char ch : t.toCharArray()) {
            tCharCount.put(ch, tCharCount.getOrDefault(ch, 0) + 1);
        }
        Map<Character, Integer> windowCounts = new HashMap<>();
        int required = tCharCount.size();
        int formed = 0;
        int l = 0, r = 0;
        int start = -1;
        int minLen = Integer.MAX_VALUE;
        while (r < s.length()) {
            char c = s.charAt(r);
            windowCounts.put(c, windowCounts.getOrDefault(c, 0) + 1);
            if (tCharCount.containsKey(c) && windowCounts.get(c).equals(tCharCount.get(c))) {
                formed++;
            }
            while (l <= r && formed == required) {
                c = s.charAt(l);
                if (r - l + 1 < minLen) {
                    start = l;
                    minLen = r - l + 1;
                }
                windowCounts.put(c, windowCounts.get(c) - 1);
                if (tCharCount.containsKey(c) && windowCounts.get(c) < tCharCount.get(c)) {
                    formed--;
                }
                l++;
            }
            r++;
        }
        return start == -1 ? "" : s.substring(start, start + minLen);
    }

    private boolean valid(Map<Character, Integer> tMap, Map<Character, Integer> windowMap) {
        for (Character ch : tMap.keySet()) {
            if (windowMap.getOrDefault(ch, 0) < tMap.get(ch)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 滑动窗口最大值
     * 堆解法
     */
    public int[] maxSlidingWindowWithHeap(int[] nums, int k) {
        int n = nums.length;
        // 值倒序、索引升序
        PriorityQueue<int[]> priorityQueue = new PriorityQueue<int[]>(new Comparator<int[]>() {
            public int compare(int[] pair1, int[] pair2) {
                return pair1[0] != pair2[0] ? pair2[0] - pair1[0] : pair1[1] - pair2[1];
            }
        });
        for (int i = 0; i < k; ++i) {
            priorityQueue.offer(new int[] { nums[i], i });
        }
        int[] ans = new int[n - k + 1];
        ans[0] = priorityQueue.peek()[0];
        for (int i = k; i < n; ++i) {
            // 对于每一个窗口，将窗口的元素放入堆，并且移除不属于当前窗口的元素
            priorityQueue.offer(new int[] { nums[i], i });
            while (priorityQueue.peek()[1] <= i - k) {
                priorityQueue.poll();
            }
            ans[i - k + 1] = priorityQueue.peek()[0];
        }
        return ans;
    }

    /**
     * 滑动窗口最大值
     * 单调队列解法
     */
    public int[] maxSlidingWindow(int[] nums, int k) {
        // 1,3,-1,-3,5,3,6,7
        // 单调递减队列,内部放的是元素索引
        ArrayDeque<Integer> deque = new ArrayDeque<>();
        int len = nums.length;
        int[] res = new int[len - k + 1];
        int index = 0;
        for (int i = 0; i < len; i++) {
            // 在[i - k + 1, i] 中选到最大值
            // 队列头结点需要在[i - k + 1, i]范围内，不符合则要弹出
            while (!deque.isEmpty() && deque.peek() < i - k + 1) {
                deque.poll();
            }
            // 要保证单调性，就要保证每次放进去的数字要比末尾的都大，否则也弹出
            while (!deque.isEmpty() && nums[deque.peekLast()] < nums[i]) {
                deque.pollLast();
            }
            deque.offer(i);
            // 因为单调，当i增长到符合第一个k范围的时候，每滑动一步都将队列头节点放入结果就行了
            if (i >= k - 1) {
                res[index++] = nums[deque.peek()];
            }
        }
        return res;
    }

    /**
     * 无重复的最长子串
     * 13min ac
     */
    public int lengthOfLongestSubstringTwoEx(String s) {
        // abcabcbb
        // s
        //    f
        int max = 0;
        int slow = 0;
        int fast = 0;
        int len = s.length();
        Map<Character, Boolean> contain = new HashMap<>();
        while(fast < len) {
            char fastChar = s.charAt(fast);
            while(contain.getOrDefault(fastChar, false) && slow < len) {
                contain.put(s.charAt(slow), false);
                slow++;
            }
            max = Math.max(max, fast - slow + 1);
            contain.put(fastChar, true);
            fast++;
        }
        return max;
    }
}
