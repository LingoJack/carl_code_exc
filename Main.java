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

    /**
     * 美团笔试第二批
     */
    public static String decode(String s, String t) {
        StringBuilder sb = new StringBuilder(t);
        int sLen = s.length(), tLen = t.length();
        for (int i = 0; i < sLen; i++) {
            char sc = s.charAt(i);
            if (sc == 'Z') {
                continue;
            } else if (sc == 'R') {
                if (i + 1 < sLen && s.charAt(i + 1) == 'Z') {
                    continue;
                }
                sb.reverse();
            } else {
                if (i + 1 < sLen && s.charAt(i + 1) == 'Z') {
                    continue;
                }
                sb.append(sc);
            }
        }
        return sb.toString();
    }

    public static int calc(int r1, int r2, int l1, int l2) {
        int res = 0;
        for (int i = l1; i <= r1; i++) {
            for (int j = l2; j <= r2; j++) {
                res += func(i, j);
            }
        }
        return res;
    }

    public static int calcV2(int r1, int r2, int l1, int l2) {
        int res = 0;
        for (int j = l2; j <= r2; j++) {
            for (int time = 1; time * j <= r1; time++) {
                if (time * j >= l1) {
                    res++;
                }
            }
        }
        return res;
    }

    private static int func(int i, int j) {
        return i % j == 0 ? 1 : 0;
    }

    public static int[][] connect(int[] nums, int k) {
        int[][] matrix = new int[nums.length][nums.length];
        for (int i = 0; i < nums.length; i++) {
            for (int j = 0; j < nums.length; j++) {
                matrix[i][j] = getSolutionNum(nums[i], nums[j], k);
            }
        }
        return matrix;
    }

    private static int getSolutionNum(int ai, int aj, int k) {
        boolean[] map = new boolean[k + 1];
        int num = 0;
        for (int i = 1; ai * i <= k; i++) {
            map[k - ai * i] = true;
        }
        for (int j = 1; aj * j <= k; j++) {
            if (map[aj * j]) {
                num++;
            }
        }
        return num;
    }

    /**
     * 淘天笔试
     */
    public static int[][] process(char[][] grid) {
        row = grid.length;
        col = grid[0].length;
        int[][] res = new int[row][col];
        boolean[] exist = new boolean[26];
        boolean[][] visited = new boolean[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (res[i][j] != 0) {
                    continue;
                }
                List<int[]> list = new ArrayList<>();
                list.add(new int[] { i, j });
                dfs(grid, list, visited, 0, exist);
                for (int[] idx : list) {
                    int x = idx[0], y = idx[1];
                    res[x][y] = count;
                }
                count = 0;
                Arrays.fill(exist, false);
                for (boolean[] line : visited) {
                    Arrays.fill(line, false);
                }
            }
        }
        return res;
    }

    private static int count = 0;

    private static int row;

    private static int col;

    private static int[][] dir = new int[][] { { -1, 0 }, { 0, 1 }, { 1, 0 }, { 0, -1 } };

    private static void dfs(char[][] grid, List<int[]> list, boolean[][] visited,
            int idx, boolean[] exist) {
        if (idx == list.size() || count == 26) {
            return;
        }
        int[] index = list.get(idx);
        int i = index[0], j = index[1];
        for (int d = 0; d < 4; d++) {
            int row = i + dir[d][0];
            int col = j + dir[d][1];
            if (!valid(row, col) || visited[row][col]) {
                continue;
            }
            visited[row][col] = true;
            if (grid[row][col] == grid[i][j]) {
                list.add(new int[] { row, col });
            } else {
                if (!exist[grid[row][col] - 'a']) {
                    count++;
                    exist[grid[row][col] - 'a'] = true;
                }
            }
        }
        dfs(grid, list, visited, idx + 1, exist);
    }

    private static boolean valid(int i, int j) {
        return (i >= 0 && i < row) && (j >= 0 && j < col);
    }

    private static int calcSum(int[] nums) {
        int sum = 0;
        for (int len = 1; len <= nums.length; len++) {
            int[] exist = new int[len + 1];
            int start = 0;
            int end = start + len - 1;
            for (int i = start; i <= end; i++) {
                if (nums[i] > len)
                    continue;
                exist[nums[i]]++;
            }
            for (int i = 0; i < exist.length; i++) {
                if (exist[i] == 0) {
                    sum += i;
                    break;
                }
            }
            for (start = 1; start <= nums.length - len; start++) {
                end = start + len - 1;
                if (nums[start - 1] <= len) {
                    exist[nums[start - 1]]--;
                }
                if (nums[end] <= len) {
                    exist[nums[end]]++;
                }
                for (int i = 0; i < exist.length; i++) {
                    if (exist[i] == 0) {
                        sum += i;
                        break;
                    }
                }
            }
        }
        return sum;
    }

    private static int getMex(int[] nums, int start, int end) {
        int len = end - start + 1;
        boolean[] exist = new boolean[len + 1];
        for (int i = start; i <= end; i++) {
            if (nums[i] > len)
                continue;
            exist[nums[i]] = true;
        }
        for (int i = 0; i < exist.length; i++) {
            if (!exist[i]) {
                return i;
            }
        }
        return exist.length;
    }

    /**
     * 蚂蚁笔试
     * 曼哈顿树
     */
    public void ansV1() {
        Scanner sc = new Scanner(System.in);
        int nodeNum = sc.nextInt();
        int queryNum = sc.nextInt();
        Map<Integer, TreeNode> map = new HashMap<>();
        map.put(1, new TreeNode(1, 0, 0));
        for (int i = 0; i < nodeNum - 1; i++) {
            int parent = sc.nextInt();
            int son = sc.nextInt();
            TreeNode parentNode = map.get(parent);
            TreeNode node = new TreeNode(son);
            if (parentNode.lt == null) {
                node.x = parentNode.x - 1;
                node.y = parentNode.y - 1;
                parentNode.lt = node;
            } else {
                node.x = parentNode.x + 1;
                node.y = parentNode.y - 1;
                parentNode.rt = node;
            }
            map.put(son, node);
        }
        for (int i = 0; i < queryNum; i++) {
            int node1Id = sc.nextInt();
            int node2Id = sc.nextInt();
            TreeNode node1 = map.get(node1Id);
            TreeNode node2 = map.get(node2Id);
            int xVal = Math.abs(node1.x - node2.x);
            int yVal = Math.abs(node1.y - node2.y);
            // int xVal = node1.x > node2.x ? node1.x - node2.x : node2.x - node1.x;
            // int yVal = node1.y > node2.y ? node1.y - node2.y : node2.y - node1.y;
            System.out.println(xVal + yVal);
        }
    }

    public class TreeNode {
        int id;
        int x;
        int y;
        TreeNode lt;
        TreeNode rt;

        public TreeNode(int id, int x, int y) {
            this.id = id;
            this.x = x;
            this.y = y;
        }

        public TreeNode(int id) {
            this.id = id;
        }
    }

    private int[][] transcation;

    /**
     * 网易互娱笔试：股神小易
     */
    public double maxProfit(double[][] prices, double money) {
        int ticketNum = prices[0].length;
        int dayNum = prices.length;
        // 卖出、买入
        int[][] record = new int[dayNum][2];
        for (int i = 0; i < dayNum; i++) {
            Arrays.fill(record[i], -1);
        }
        double res = money;
        int lastSelectedTicket = -1;
        for (int day = 0; day < dayNum; day++) {
            double[] curPrices = prices[day];
            double[] tomorrowPrices = day + 1 < dayNum ? prices[day + 1] : new double[ticketNum];
            int selectedTicket = -1;
            double maxProfit = 0;
            for (int ticket = 0; ticket < ticketNum; ticket++) {
                double profit = (res / curPrices[ticket]) * (tomorrowPrices[ticket] - curPrices[ticket]);
                if (profit > maxProfit) {
                    selectedTicket = ticket;
                    maxProfit = profit;
                }
            }
            record[day][0] = lastSelectedTicket;
            record[day][1] = selectedTicket;
            lastSelectedTicket = selectedTicket;
            res += maxProfit;
        }
        transcation = record;
        return res;
    }

    /**
     * 网易互娱笔试：星际探险
     * Ax + By + Cz + Dw = N
     * 0 ~ 2500
     * 返回满足条件的最小字典序四元组（x, y, z, w），且x，y，z，w互不相同，若无解，返回-1，-1，-1，-1
     */
    public int[] getSolution(int A, int B, int C, int D, int N) {
        // 预处理Cz + Dw的所有可能值
        Map<Integer, int[]> map = new HashMap<>();
        for (int z = 0; z <= 2500; z++) {
            for (int w = 0; w <= 2500; w++) {
                if (w == z) {
                    continue;
                }
                int value = C * z + D * w;
                if (!map.containsKey(value)) {
                    map.put(value, new int[] { z, w });
                }
            }
        }
        // 遍历x和y
        for (int x = 0; x <= 2500; x++) {
            for (int y = 0; y <= 2500; y++) {
                if (y == x) {
                    continue; // 确保y != x
                }
                // 计算剩余的值：Cz + Dw = N - Ax - By
                int remaining = N - (A * x + B * y);
                if (remaining < 0) {
                    continue; // 如果剩余值为负，跳过
                }
                // 查找是否存在满足条件的z和w
                if (map.containsKey(remaining)) {
                    int[] zw = map.get(remaining);
                    int z = zw[0], w = zw[1];
                    if (z != x && z != y && w != x && w != y) {
                        // 找到解，直接返回
                        return new int[] { x, y, z, w };
                    }
                }
            }
        }
        // 无解
        return new int[] { -1, -1, -1, -1 };
    }

    private void main() {
        Main main = new Main();
        double[][] prices1 = new double[][] {
                { 3, 1, 5, 7, 4 },
                { 2, 4, 6, 4, 7 },
                { 1, 3, 2, 2, 4 },
                { 10, 4, 8, 6, 9 },
                { 3, 8, 10, 5, 6 }
        };
        double[][] prices2 = new double[][] {
                { 3, 1, 5, 7, 4 },
                { 2, 4, 6, 4, 7 },
                { 8, 3, 9, 3, 5 },
                { 10, 4, 8, 6, 9 },
                { 3, 8, 10, 5, 6 }
        };
        double money = 100;
        // 保留四位小数
        System.out.printf("%.4f\n", main.maxProfit(prices2, money));
        for (int[] record : main.transcation) {
            System.out.println(record[0] + " " + record[1]);
        }
    }
}
