import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;

import interview150.Interview150TwoEx;
import leetcodeHot100.link_list;

public class Test {

    /**
     * 蚂蚁笔试
     */
    public void testForGetNum() {
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
        Test test = new Test();
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
        System.out.printf("%.4f\n", test.maxProfit(prices2, money));
        for (int[] record : test.transcation) {
            System.out.println(record[0] + " " + record[1]);
        }
    }

    /**
     * 灵犀互娱：数组元素平方后重排序
     */
    public ArrayList<Integer> sortedSquares(ArrayList<Integer> nums) {
        ArrayList<Integer> list = new ArrayList<>();
        int len = nums.size();
        if (len == 1) {
            list.add(nums.get(0) * nums.get(0));
            return list;
        }
        if (nums.get(len - 1) < 0 || nums.get(0) >= 0) {
            for (int i = 0; i < len; i++) {
                int num = nums.get(i);
                list.add(num * num);
            }
            return list;
        }
        // -4 -1 0 3 7
        // s
        // f
        int slow = 0, fast = 1;
        while (fast < len) {
            if (nums.get(fast) >= 0 && nums.get(slow) < 0) {
                break;
            } else if (nums.get(slow) >= 0) {
                break;
            }
            fast++;
            slow++;
        }
        while (fast < len || slow >= 0) {
            int fastNum = fast < len ? nums.get(fast) * nums.get(fast) : Integer.MAX_VALUE;
            int slowNum = slow >= 0 ? nums.get(slow) * nums.get(slow) : Integer.MAX_VALUE;
            if (fastNum > slowNum) {
                list.add(slowNum);
                slow--;
            } else {
                list.add(fastNum);
                fast++;
            }
        }
        return list;
    }

    /**
     * 灵犀互娱：最小因式分解
     * 给一个正整数num，求最小正整数，且该最小正整数的各数位之积等于num
     */
    public int test(int a) {
        return minFactorsConsturctNum(a);
    }

    public int minFactorsConsturctNum(int num) {
        if (num == 1) {
            return 1;
        }
        List<List<Integer>> factorsList = new ArrayList<>();
        dfs(factorsList, new ArrayList<>(), num);
        if (factorsList.isEmpty()) {
            return 0;
        }
        List<Integer> minList = null;
        long minSize = Long.MAX_VALUE;
        for (List<Integer> list : factorsList) {
            if (minSize > list.size()) {
                minSize = list.size();
                minList = list;
            }
        }
        Collections.sort(minList);
        long res = 0;
        for (int i = 0; i < minSize; i++) {
            res = res * 10 + minList.get(i);
        }
        return (res >= Integer.MAX_VALUE) ? -1 : (int) res;
    }

    private void dfs(List<List<Integer>> factorsList, List<Integer> factors, int num) {
        if (num == 1) {
            factorsList.add(new ArrayList<>(factors));
            return;
        }
        for (int i = 2; i <= 9; i++) {
            if (num % i == 0) {
                factors.add(i);
                dfs(factorsList, factors, num / i);
                factors.remove(factors.size() - 1);
            }
        }
    }

    /**
     * 游戏中有多少弱角色
     */
    public int numOfWeakRoles(int[][] properties) {
        int roleNum = properties.length;
        Arrays.sort(properties, (a, b) -> {
            return a[0] == b[0] ? Integer.compare(a[1], b[1]) : Integer.compare(a[0], b[0]);
        });
        int count = 0;
        // [1,5],[10,4],[4,3],[2,3],[6,7]
        // [1,5] [2,3] [4,3] [6,7] [10,4]
        boolean[] isWeak = new boolean[roleNum];
        for (int i = 0; i < roleNum; i++) {
            int[] curRole = properties[i];
            for (int j = i + 1; j < roleNum; j++) {
                int[] nextRole = properties[j];
                if (curRole[0] < nextRole[0] && curRole[1] < nextRole[1] && !isWeak[i]) {
                    isWeak[i] = true;
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * 多多的推箱子游戏
     */
    public boolean pushGame(String operations, int[] startPos) {
        int len = operations.length();
        int x = startPos[0], y = startPos[1];
        for (int i = 0; i < len; i++) {
            char op = operations.charAt(i);
            if (op == 'W') {
                y++;
            } else if (op == 'A') {
                x--;
            } else if (op == 'S') {
                y--;
            } else if (op == 'D') {
                x++;
            }
        }
        return x == 0 && y == 0;
    }

    /**
     * 多多买彩票
     * 关键是发现这个规律：
     * 大于100的数字都满足存在子串位数和为3的倍数
     */
    public int getLuckyNumCount(long start, long end) {
        int count = 0;
        for (long i = start; i <= end; i++) {
            if (isLuckyNum(i)) {
                count++;
            }
        }
        return count;
    }

    private boolean isLuckyNum(long num) {
        int len = 0;
        long repl = num;
        while (repl > 0) {
            repl /= 10;
            len++;
        }
        long[] prefixSum = new long[len];
        prefixSum[0] = num % 10;
        num /= 10;
        for (int i = 1; i < len - 1; i++) {
            prefixSum[i] = prefixSum[i - 1] + (num % 10);
            num /= 10;
        }
        for (int i = 0; i < prefixSum.length; i++) {
            if (prefixSum[i] % 3 == 0) {
                return true;
            }
            for (int j = i + 1; j < prefixSum.length; j++) {
                if ((prefixSum[j] - prefixSum[i]) % 3 == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 多多的排队
     */
    public long canSeePeopleNum(long[] heights) {
        if (heights == null || heights.length == 0) {
            return 0;
        }
        // 10 7 4 8 2 1
        long res = 0;
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 0; i < heights.length; i++) {
            while (!stack.isEmpty() && heights[stack.peek()] <= heights[i]) {
                long j = stack.pop();
                res += (i - j);
            }
            stack.push(i);
        }
        while (!stack.isEmpty()) {
            long j = stack.pop();
            res += (heights.length - 1 - j);
        }
        return res;
    }

    /**
     * 求和谐数对
     * |x - y| = |x| - |y|
     * x 绝对值必须 大于等于 0
     */
    public List<int[]> getPair(int[] nums) {
        // 1 1 4 5 0 4
        // 0 1 1 4 4 5
        // _ _ _ _ _ x
        // y
        Map<Integer, List<Integer>> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            List<Integer> list = map.getOrDefault(nums[i], new ArrayList<>());
            list.add(i);
            map.put(nums[i], list);
        }
        Arrays.sort(nums);
        int lt = 0, rt = nums.length - 1;
        List<int[]> res = new ArrayList<>();
        while (rt >= 1) {
            lt = 0;
            int x = nums[rt];
            while (lt < rt) {
                int y = nums[lt];
                int ltPart = x - y;
                int rtPart = Math.abs(x) - Math.abs(y);
                if (ltPart == rtPart) {
                    List<Integer> idxXList = map.get(x);
                    List<Integer> idxYList = map.get(y);
                    for (int idxX = 0; idxX < idxXList.size(); idxX++) {
                        int indexX = idxXList.get(idxX);
                        for (int idxY = 0; idxY < idxYList.size(); idxY++) {
                            int indexY = idxYList.get(idxY);
                            if (indexX < indexY) {
                                res.add(new int[] { x, y });
                            }
                        }
                    }
                }
                lt++;
                while (lt > 0 && nums[lt] == nums[lt - 1]) {
                    lt++;
                }
            }
            rt--;
            while (rt < nums.length - 1 && nums[rt] == nums[rt + 1]) {
                rt--;
            }
        }
        return res;
    }

    /**
     * 蚂蚁
     * 
     * @param nums
     * @return
     */
    public int countPairs(int[] nums) {
        // 正数集合
        List<Integer> positives = new ArrayList<>();
        // 负数集合
        List<Integer> negatives = new ArrayList<>();
        int count = 0;
        for (int num : nums) {
            if (num >= 0) {
                // 在正数集合中找到第一个不小于 num 的位置
                int pos = binarySearch(positives, num);
                // 答案加上该位置后面的数字个数
                count += positives.size() - pos;
                // 将 num 插入到正数集合中
                positives.add(pos, num);
            } else {
                // 在负数集合中找到第一个小于 num 的位置
                int pos = binarySearchNegatives(negatives, num);
                // 答案加上该位置前面的数字个数
                count += pos;
                // 将 num 插入到负数集合中
                negatives.add(pos, num);
            }
        }
        return count;
    }

    // 二分查找：找到第一个不小于 target 的位置
    private int binarySearch(List<Integer> list, int target) {
        int left = 0, right = list.size();
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (list.get(mid) < target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }

    // 二分查找：找到第一个小于 target 的位置
    private int binarySearchNegatives(List<Integer> list, int target) {
        int left = 0, right = list.size();
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (list.get(mid) >= target) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }

    /**
     * 字节面试题
     * 具有相同字符种类的两个字符串的最长子串组合
     * 比如s1 = "adcdfe", s2 = "acbfefe"
     * 输出：
     * ("acb", "abc"), ("fefe", "fe")
     */
    public List<List<String>> longestSubstringCombinationWithSameCharacter(String s1, String s2) {
        List<List<String>> res = new ArrayList<>();
        int l1 = s1.length();
        boolean[] exist1 = new boolean[26];
        boolean[] exist2 = new boolean[26];
        int count1 = 0, count2 = 0;
        for (char c : s1.toCharArray()) {
            if (exist1[c - 'a']) {
                continue;
            }
            exist1[c - 'a'] = true;
            count1++;
        }
        for (char c : s2.toCharArray()) {
            if (exist2[c - 'a']) {
                continue;
            }
            exist2[c - 'a'] = true;
            count2++;
        }
        // 要求s1的字符种类必须不少于s2
        if (count2 > count1) {
            return longestSubstringCombinationWithSameCharacter(s2, s1);
        }
        boolean[] exist1ButNotExist2 = new boolean[26];
        for (char c = 'a'; c <= 'z'; c++) {
            exist1ButNotExist2[c - 'a'] = exist1[c - 'a'] && !exist2[c - 'a'];
        }
        List<String> candidates = new ArrayList<>();
        int start = 0;
        for (int end = 0; end <= l1; end++) {
            if (end == l1 || exist1ButNotExist2[s1.charAt(end) - 'a']) {
                candidates.add(s1.substring(start, end));
                start = end + 1;
            }
        }
        if (candidates.isEmpty()) {
            res.add(List.of(s1, s2));
            return res;
        }
        int maxLen = 0;
        for (String candidate : candidates) {
            List<String> list = findLongestSubstringContainingSameCharacterWithPattern(s2, candidate);
            if (list.isEmpty()) {
                continue;
            }
            int len = list.get(0).length() + candidate.length();
            if (len > maxLen) {
                maxLen = len;
                res.clear();
            }
            if (len >= maxLen) {
                for (String substring : list) {
                    res.add(List.of(substring, candidate));
                }
            }
        }
        return res;
    }

    private List<String> findLongestSubstringContainingSameCharacterWithPattern(String s, String pattern) {
        boolean[] exist = new boolean[26];
        int count = 0;
        for (char c : pattern.toCharArray()) {
            if (exist[c - 'a']) {
                continue;
            }
            exist[c - 'a'] = true;
            count++;
        }
        int valid = 0;
        int start = 0;
        int maxLen = 0;
        List<String> list = new ArrayList<>();
        boolean[] have = new boolean[26];
        for (int end = 0; end < s.length(); end++) {
            char c = s.charAt(end);
            int len = end - 1 - start + 1;
            if (!exist[c - 'a']) {
                if (valid < count) {
                    valid = 0;
                    Arrays.fill(have, false);
                    start = end + 1;
                    continue;
                }
                if (len > maxLen) {
                    maxLen = len;
                    list.clear();
                }
                if (len >= maxLen) {
                    list.add(s.substring(start, end));
                }
                valid = 0;
                Arrays.fill(have, false);
                start = end + 1;
            }
            if (!have[c - 'a']) {
                valid++;
            }
            have[c - 'a'] = true;
        }
        if (start != s.length()) {
            int len = s.length() - 1 - start + 1;
            if (len > maxLen) {
                maxLen = len;
                list.clear();
            }
            if (len >= maxLen) {
                list.add(s.substring(start, s.length()));
                start = s.length() + 1;
            }
        }
        return list;
    }

    /**
     * 拼多多一面
     * 一副扑克牌随机抽六张牌，判断是不是顺子（数字连在一起的，比如A23456），大小王可以当任意牌用
     */
    public boolean isContinue(int[] nums) {
        boolean[] exist = new boolean[15]; // 0 - 14
        for (int num : nums) {
            if (exist[num]) {
                return false;
            }
            exist[num] = true;
        }
        int any = 0;
        if (exist[13]) {
            any++;
        }
        if (exist[14]) {
            any++;
        }
        int count = 0;
        for (int num : nums) {
            if (exist[num - 1]) {
                continue;
            }
            // !exist[num - 1] 可能是起点
            while (exist[num] || any > 0) {
                if (!exist[num]) {
                    any--;
                }
                num++;
                count++;
                if (count == 6) {
                    return true;
                }
            }
            if (count < 6) {
                return false;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        int[] nums = new int[] { 1, 1, 1, 1, 1, 10 };
        int upperBound = 4;
        System.out.println(new Test().cowGirlGem(upperBound, nums));
    }

    /**
     * 牛妹的宝石
     * nums，长度为len，上界为upperBound，对于[1, upperBound]中的数，nums中至少会出现一次
     * 0 < upperBound <= len
     * 每次可以选择加上或减去nums[i]，求问能最小的绝对值
     */
    public int cowGirlGemBF(int upperBound, int[] nums) {
        Set<Integer> set = new HashSet<>();
        set.add(0);
        for (int i = 0; i < nums.length; i++) {
            Set<Integer> newSet = new HashSet<>();
            for (int sum : set) {
                newSet.add(sum + nums[i]);
                newSet.add(sum - nums[i]);
            }
            set = newSet;
        }
        int res = Integer.MAX_VALUE;
        for (int sum : set) {
            res = Math.min(Math.abs(sum), res);
        }
        return res;
    }

    /**
     * 字节Tiktok一面
     * 将数组分割成和相等的子数组，问可分割的子数组的个数最大是多少
     */
    public int valid(int[] nums) {
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        for (int partNum = nums.length; partNum >= 2; partNum--) {
            if (sum % partNum != 0) {
                continue;
            }
            int target = sum / partNum;
            if (dfs(nums, target, 0, 0, partNum)) {
                return partNum;
            }
        }
        return 1;
    }

    private boolean dfs(int[] nums, int target, int start, int count, int needPartNum) {
        if (start == nums.length) {
            return count == needPartNum;
        }
        for (int i = start; i < nums.length; i++) {
            if (check(nums, start, i, target)) {
                return dfs(nums, target, i + 1, count + 1, needPartNum);
            }
        }
        return false;
    }

    private boolean check(int[] nums, int start, int end, int target) {
        int sum = 0;
        for (int i = start; i <= end; i++) {
            sum += nums[i];
        }
        return sum == target;
    }

    /**
     * 牛妹的宝石
     * nums，长度为len，上界为upperBound，对于[1, upperBound]中的数，nums中至少会出现一次
     * 0 < upperBound <= len
     * 每次可以选择加上或减去nums[i]，求问能最小的绝对值
     * 加以转化就是对于一个正整数数组，将内部的数分为两组，求这两组数的最小差值（大于等于0）
     * 换句话说，我们可以计算数组的总和total，然后求出和最接近total/2的一组数，由于是整数，我们可以从total/2开始去寻找和为target的子数组
     * 这里就会用到经典的背包问题
     * 这个思路是我自己想出来的，十分不错
     */
    public int cowGirlGem(int upperBound, int[] nums) {
        int total = 0;
        for (int num : nums) {
            total += num;
        }
        int target = total / 2;
        boolean[] dp = new boolean[target + 1];
        dp[0] = true;
        for (int num : nums) {
            for (int j = target; j >= num; j--) {
                if (dp[j - num]) {
                    dp[j] = true;
                }
            }
        }
        int maxSum = 0;
        for (int j = target; j >= 0; j--) {
            if (dp[j]) {
                maxSum = j;
                break;
            }
        }
        return total - 2 * maxSum;
    }

    /**
     * 背包问题
     * 01背包，数组中是否存在和为target的子序列
     */
    private boolean containsSum(int[] nums, int target) {
        int len = nums.length;
        boolean[][] dp = new boolean[len + 1][target + 1];
        for (int i = 0; i < len + 1; i++) {
            dp[i][0] = true;
        }
        for (int i = 1; i < len + 1; i++) {
            for (int j = 0; j <= target; j++) {
                if (j >= nums[i - 1]) {
                    dp[i][j] = dp[i - 1][j - nums[i - 1]];
                } else {
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }
        return dp[len][target];
    }

    /**
     * 最接近目标值的子序列和
     * 尝试使用滑动窗口去做，lt，rt都从最左边开始，先排序一下
     * 使用滑动窗口是做不了这题的，因为就算是排序之后，滑窗要求必须相邻的才能组合
     * 而这个子序列是不相邻的也可以的
     * 比如：
     * 2 4 6 8 20
     * target = 10
     * 就是找不到答案
     * 这边的核心思路就是：
     * 分成左右两个部分，然后分别去求左右两部分的所有子序列，去做一个组合
     */
    public int minAbsDifference(int[] nums, int goal) {
        int len = nums.length;
        List<Integer> left = new ArrayList<>();
        List<Integer> right = new ArrayList<>();
        for (int i = 0; i < len / 2; i++) {
            left.add(nums[i]);
        }
        for (int i = len / 2; i < len; i++) {
            right.add(nums[i]);
        }
        List<Integer> ltSumList = generateSubsetSums(left);
        List<Integer> rtSumList = generateSubsetSums(right);
        Collections.sort(rtSumList);
        int res = Integer.MAX_VALUE;
        for (int ltSum : ltSumList) {
            int targetRtSum = goal - ltSum;
            // Collections.binarySearch对于不存在的，会返回该插入位置的负数值
            int idx = Collections.binarySearch(rtSumList, targetRtSum);
            if (idx >= 0) {
                // binarySearch返回的是非负数，说明是存在的
                return 0;
            } else {
                // 得到真正的该插入的位置
                // binarySearch返回的是 -(insertPoint)-1 = -(insertPoint + 1)
                idx = -idx - 1;
                if (idx > 0) {
                    res = Math.min(res, Math.abs(ltSum + rtSumList.get(idx - 1) - goal));
                }
                if (idx < rtSumList.size()) {
                    res = Math.min(res, Math.abs(ltSum + rtSumList.get(idx) - goal));
                }
            }
            // 检测是否能提前返回
            if (res == 0) {
                break;
            }
        }
        return res;
    }

    private List<Integer> generateSubsetSums(List<Integer> left) {
        ArrayList<Integer> sums = new ArrayList<>();
        sums.add(0);
        for (int num : left) {
            int size = sums.size();
            for (int i = 0; i < size; i++) {
                sums.add(sums.get(i) + num);
            }
        }
        return sums;
    }

    /**
     * WXG二面
     * 没做出来，这里需要逆向思维
     * 戳气球
     * 这里核心是想到应该把移除气球的过程倒过来
     * 想成增加气球
     */
    public int maxCoins(int[] nums) {
        int len = nums.length;
        // 预处理之后的数组
        int[] preprocess = new int[len + 2];
        for (int i = 1; i <= len; i++) {
            preprocess[i] = nums[i - 1];
        }
        preprocess[0] = preprocess[len + 1] = 1;
        // (i, j)​​在开区间 (i, j) 内戳气球能获得的最大硬币数​​
        int[][] record = new int[len + 2][len + 2];
        for (int i = 0; i <= len + 1; i++) {
            Arrays.fill(record[i], -1);
        }
        return getMaxCoins(preprocess, record, 0, len + 1);
    }

    public int getMaxCoins(int[] preprocess, int[][] record, int left, int right) {
        if (left >= right - 1) {
            return 0;
        }
        if (record[left][right] != -1) {
            return record[left][right];
        }
        for (int i = left + 1; i < right; i++) {
            int sum = preprocess[left] * preprocess[i] * preprocess[right];
            sum += getMaxCoins(preprocess, record, left, i) + getMaxCoins(preprocess, record, i, right);
            record[left][right] = Math.max(record[left][right], sum);
        }
        return record[left][right];
    }

    /**
     * 戳气球
     * 动态规划解法
     */
    public int maxCoinsWithDp(int[] nums) {
        int len = nums.length;
        // dp[i][j]为戳破开区间(i,j)里所有气球后所能获得的最大硬币数量
        int[][] dp = new int[len + 2][len + 2];
        int[] preprocess = new int[len + 2];
        preprocess[0] = preprocess[len + 1] = 1;
        for (int i = 1; i <= len; i++) {
            preprocess[i] = nums[i - 1];
        }
        for (int interval = 2; interval <= len + 1; interval++) {
            // (start, end)
            // 枚举起点
            for (int start = 0; start + interval <= len + 1; start++) {
                int end = start + interval;
                // 枚举最后一个被戳破的气球 k
                for (int k = start + 1; k < end; k++) {
                    int sum = preprocess[start] * preprocess[k] * preprocess[end];
                    sum += dp[start][k] + dp[k][end];
                    dp[start][end] = Math.max(dp[start][end], sum);
                }
            }
        }
        return dp[0][len + 1];
    }
}