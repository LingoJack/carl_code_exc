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
}
