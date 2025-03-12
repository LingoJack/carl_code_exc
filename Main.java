import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import leetcodeHot100.stack;

public class Main {

    /**
     * 蚂蚁笔试
     */
    public static void main(String[] args) {
        int num = 6;
        int[][] pos = new int[][] {
                { 0, 0 },
                { 0, 1 },
                { 0, 3 },
                { 1, 0 },
                { 2, 0 },
                { 3, 0 }
        };
        Map<Integer, List<Integer>> rowIndexMap = new HashMap<>();
        Map<Integer, List<Integer>> colIndexMap = new HashMap<>();
        for (int i = 0; i < num; i++) {
            int[] index = pos[i];
            rowIndexMap.putIfAbsent(index[0], new ArrayList<>());
            colIndexMap.putIfAbsent(index[1], new ArrayList<>());
            rowIndexMap.get(index[0]).add(i);
            colIndexMap.get(index[1]).add(i);
        }
        rowIndexMap.values().forEach(l -> Collections.sort(l, (a, b) -> Integer.compare(pos[a][1], pos[b][1])));
        colIndexMap.values().forEach(l -> Collections.sort(l, (a, b) -> Integer.compare(pos[a][0], pos[b][0])));
        for (int i = 0; i < num; i++) {
            System.out.println(getNum(pos, i, rowIndexMap, colIndexMap));
        }
    }

    public static int getNum(int[][] pos, int i, Map<Integer, List<Integer>> rowIndexMap,
            Map<Integer, List<Integer>> colIndexMap) {
        int rowIndex = pos[i][0];
        int colIndex = pos[i][1];
        int count = 0;
        List<Integer> rowList = rowIndexMap.get(rowIndex);
        int colPos = rowList.indexOf(i);
        if (colPos - 1 > 0) {
            count++;
        }
        if (colPos + 1 < rowList.size() - 1) {
            count++;
        }
        List<Integer> colList = colIndexMap.get(colIndex);
        int rowPos = colList.indexOf(i);
        if (rowPos - 1 > 0) {
            count++;
        }
        if (rowPos + 1 < colList.size() - 1) {
            count++;
        }
        return count;
    }

    public static String decode(String s) {
        int p = 0;
        int len = s.length();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            if (c >= '0' && c <= '9') {
                if (p == 0) {
                    p = c - '0';
                } else {
                    p = 10 * p + (c - '0');
                }
            } else {
                // 将字符串左移p位
                leftRemoveP(sb, p);
                p = 0;
                if (c == 'R') {
                    reverse(sb);
                } else {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    private static void leftRemoveP(StringBuilder sb, int p) {
        // emD p=2
        // Dme
        reverse(sb, 0, sb.length() - 1);
        reverse(sb, 0, sb.length() - p - 1);
        reverse(sb, sb.length() - p, sb.length() - 1);
    }

    private static void reverse(StringBuilder sb, int start, int end) {
        while (start < end) {
            char c = sb.charAt(start);
            sb.setCharAt(start, sb.charAt(end));
            sb.setCharAt(end, c);
            start++;
            end--;
        }
    }

    private static void reverse(StringBuilder sb) {
        reverse(sb, 0, sb.length() - 1);
    }

    /**
     * 腾讯云一面：
     * 全排列
     */
    public List<List<Integer>> all(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        dfs(res, new ArrayList<>(), nums, new boolean[nums.length]);
        return res;
    }

    private void dfs(List<List<Integer>> res, List<Integer> list, int[] nums, boolean[] used) {
        if (list.size() == nums.length) {
            res.add(new ArrayList<>(list));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (used[i]) {
                continue;
            }
            used[i] = true;
            list.add(nums[i]);
            dfs(res, list, nums, used);
            list.remove(list.size() - 1);
            used[i] = false;
        }
    }

    /**
     * 字节Tiktok二面：
     * 一个字符串，可以从最前面或者最后面添加字符，使之成回文串，返回最小的回文串
     */
    public String shortestPalindrome(String s) {
        boolean[][] dp = new boolean[s.length()][s.length()];
        for (int i = 0; i < s.length(); i++) {
            dp[i][i] = true;
        }
        int maxLen = 1;
        int minStartPoint = 0;
        for (int len = 2; len <= s.length(); len++) {
            // 0 1 2 3
            for (int start = 0; start < s.length() - len + 1; start++) {
                int end = start + len - 1;
                if (len == 2) {
                    dp[start][end] = s.charAt(start) == s.charAt(end);
                } else {
                    dp[start][end] = s.charAt(start) == s.charAt(end) && dp[start + 1][end - 1];
                }
                if (dp[start][end] && len > maxLen) {
                    maxLen = len;
                    minStartPoint = start;
                }
            }
        }
        StringBuilder sb = new StringBuilder(s);
        if (minStartPoint == 0) {
            for (int i = maxLen; i < s.length(); i++) {
                sb.insert(0, s.charAt(i));
            }
        } else if (minStartPoint + maxLen - 1 == s.length() - 1) {
            for (int i = minStartPoint - 1; i >= 0; i--) {
                sb.append(s.charAt(i));
            }
        }
        return sb.toString();
    }
}
