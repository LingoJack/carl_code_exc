package leetcodeHot100;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class backtrack {
    /**
     * 括号生成
     */
    public List<String> generateParenthesis(int n) {
        List<String> res = new ArrayList<>();
        backtrack4GenerateParenthesis(res, new StringBuilder().append("("), n, 1, 0);
        return res;
    }

    private void backtrack4GenerateParenthesis(List<String> res, StringBuilder sb, int n, int leftParentthesisCount,
            int rightParentthesisCount) {
        if (leftParentthesisCount == n && rightParentthesisCount == n) {
            res.add(sb.toString());
            return;
        }
        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                if (leftParentthesisCount < n) {
                    sb.append("(");
                    backtrack4GenerateParenthesis(res, sb, n, leftParentthesisCount + 1, rightParentthesisCount);
                    sb.setLength(sb.length() - 1);
                }
            } else {
                if (rightParentthesisCount < leftParentthesisCount) {
                    sb.append(")");
                    backtrack4GenerateParenthesis(res, sb, n, leftParentthesisCount, rightParentthesisCount + 1);
                    sb.setLength(sb.length() - 1);
                }
            }
        }
    }

    /**
     * 单词搜索
     */
    public boolean exist(char[][] board, String word) {
        int row = board.length, column = board[0].length;
        boolean[][] visited = new boolean[row][column];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                boolean flag = check(board, visited, i, j, word, 0);
                if (flag) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean check(char[][] board, boolean[][] visited, int i, int j, String s, int k) {
        if (board[i][j] != s.charAt(k)) {
            return false;
        } else if (k == s.length() - 1) {
            return true;
        }
        visited[i][j] = true;
        int[][] directions = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };
        boolean result = false;
        for (int[] dir : directions) {
            int newi = i + dir[0], newj = j + dir[1];
            if (newi >= 0 && newi < board.length && newj >= 0 && newj < board[0].length) {
                if (!visited[newi][newj]) {
                    boolean flag = check(board, visited, newi, newj, s, k + 1);
                    if (flag) {
                        result = true;
                        break;
                    }
                }
            }
        }
        visited[i][j] = false;
        return result;
    }

    /**
     * 分割回文串
     * 待会从12min开始计时
     * 12min30s ac
     * 击败61.6%
     * 使用记忆化递归之后击败99.98%
     */
    public List<List<String>> partition(String s) {
        isPalindromeString = new boolean[s.length()][s.length()];
        List<List<String>> res = new ArrayList<>();
        backtrack4Partition(res, new ArrayList<>(), s, 0);
        return res;
    }

    // 记忆化递归，记录[i, j]的子串是否为回文串
    private boolean[][] isPalindromeString;

    private void backtrack4Partition(List<List<String>> res, List<String> list, String s, int start) {
        if (start == s.length()) {
            res.add(new ArrayList<>(list));
            return;
        }
        for (int i = start; i < s.length(); i++) {
            String subString = s.substring(start, i + 1);
            if (isPalindromeString[start][i] || isPalindromeString(subString)) {
                isPalindromeString[start][i] = true;
                list.add(subString);
                backtrack4Partition(res, list, s, i + 1);
                list.remove(list.size() - 1);
            }
        }
    }

    private boolean isPalindromeString(String s) {
        int lt = 0;
        int rt = s.length() - 1;
        while (rt > lt) {
            if (s.charAt(rt) != s.charAt(lt)) {
                return false;
            }
            rt--;
            lt++;
        }
        return true;
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
