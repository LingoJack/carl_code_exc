package leetcodeHot100;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
}
