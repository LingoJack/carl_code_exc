package backtracking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import string.string;

/**
 * 把回溯拆分成树形，会容易理解很多
 */
public class backtracking {

    List<List<Integer>> result4Combina = new ArrayList<>();
    LinkedList<Integer> path4Combina = new LinkedList<>();

    /**
     * 组合
     * 这是一道典型的回溯，回溯题是有一定的模板可以帮助思考的
     * [1, n]中k个数的组合
     * 组合不管顺序，所以要从前往后取，每次取都不往前
     * 而且进行剪枝
     */
    public List<List<Integer>> combine(int n, int k) {
        backtrack4Combine(n, k, 1);
        return result4Combina;
    }

    private void backtrack4Combine(int n, int k, int startIndex) {
        // 终止条件
        if (path4Combina.size() == k) {
            // 存放结果
            result4Combina.add(new ArrayList<>(path4Combina));
            // 返回
            return;
        }

        // 本层要处理的集合中的元素
        for (int i = startIndex; i <= n - (k - path4Combina.size()) + 1; i++) {
            // 处理节点
            path4Combina.add(i);
            // 递归
            backtrack4Combine(n, k, i + 1);
            // 回溯，撤销处理结果
            path4Combina.removeLast();
        }
    }

    /**
     * 组合总和III
     * 经过剪枝，击败100%
     */
    public List<List<Integer>> combinationSum3(int k, int n) {
        List<List<Integer>> res = new ArrayList<>();
        List<Integer> list = new ArrayList<>(k);
        int sum = 0;
        backtrack4CombinationSum3(res, list, sum, 1, k, n);
        return res;
    }

    private void backtrack4CombinationSum3(List<List<Integer>> res, List<Integer> list, int sum, int start, int k,
            int n) {
        if (list.size() == k) {
            if (sum == n)
                res.add(new ArrayList<>(list));
            return;
        }
        for (int i = start; i <= Math.min(9, n - sum); i++) {
            list.add(i);
            sum += i;
            backtrack4CombinationSum3(res, list, sum, i + 1, k, n);
            list.remove(list.size() - 1);
            sum -= i;
        }
    }

    /**
     * 电话号码的字母组合
     * 经过反复调整，终于击败100%
     */
    public List<String> letterCombinations(String digits) {
        if (digits.isBlank()) {
            return new ArrayList<>();
        }
        char[] str = digits.toCharArray();
        String[] phoneArray = {
                "", // 0 (无效)
                "", // 1 (无效)
                "abc", // 2
                "def", // 3
                "ghi", // 4
                "jkl", // 5
                "mno", // 6
                "pqrs", // 7
                "tuv", // 8
                "wxyz" // 9
        };

        List<String> res = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        backtrack4LetterCombination(res, sb, 0, str, phoneArray);
        return res;
    }

    private void backtrack4LetterCombination(List<String> res, StringBuilder sb, int start, char[] str,
            String[] phoneArray) {
        if (sb.length() == str.length) {
            res.add(sb.toString());
            return;
        }
        for (int i = start; i < str.length; i++) {
            int index = str[i] - '0'; // 将字符转换为数字
            if (index < 2 || index > 9) { // 跳过无效数字
                continue;
            }
            String charStr = phoneArray[index];
            char[] chars = charStr.toCharArray();
            for (char ch : chars) {
                sb.append(ch);
                backtrack4LetterCombination(res, sb, i + 1, str, phoneArray);
                sb.deleteCharAt(sb.length() - 1);
            }
        }
    }

    /**
     * 组合总和
     * 这其实也是一个背包问题，而且是完全背包
     * 啊这，我居然自己写出来了，太好了
     * 这题关键是每个数可以被重复放入，所以我用了if (sum > target) return;增加返回的时机而防止爆栈
     * 击败89.55%，这是还没有经过剪枝优化的
     * 剪枝优化之后，击败99.97%
     */
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> res = new ArrayList<>();
        List<Integer> list = new ArrayList<>();
        int sum = 0;
        backtrack4CombinationSum(res, list, sum, target, candidates, 0);
        return res;
    }

    private void backtrack4CombinationSum(List<List<Integer>> res, List<Integer> list, int sum, int target,
            int[] candidates, int start) {
        if (sum == target) {
            res.add(new ArrayList<>(list));
            return;
        } else if (sum > target) {
            return;
        }
        for (int i = start; i < candidates.length; i++) {
            // 剪枝处理
            if (sum + candidates[i] > target) {
                continue;
            }
            sum += candidates[i];
            list.add(candidates[i]);
            backtrack4CombinationSum(res, list, sum, target, candidates, i);
            sum -= candidates[i];
            list.remove(list.size() - 1);
        }
    }

    /**
     * 组合总和II
     * 这题和组合总和的区别在于元素只能在每个组合中使用一次，可能有重复的元素
     * 但是组合的集合要去重，呃，那是不是可以先排序
     * 到底如何去重
     * 呃，我自己想到了用一个hasIn数组，另外在题解的稍微提示下做出来了，击败69.6%
     * 其实也可以不用hasIn数组，因为要想到start和i的大小关系来去除同一层的相等元素
     * if (i > start && candidates[i] == candidates[i - 1]) continue;
     */
    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        List<List<Integer>> res = new ArrayList<>();
        List<Integer> list = new ArrayList<>();
        boolean[] hasIn = new boolean[candidates.length];
        int sum = 0;
        Arrays.sort(candidates);
        backtrack4CombinationSum2(res, list, sum, target, candidates, 0, hasIn);
        return res;
    }

    private void backtrack4CombinationSum2(List<List<Integer>> res, List<Integer> list, int sum, int target,
            int[] candidates, int start, boolean[] hasIn) {
        if (sum == target) {
            res.add(new ArrayList<>(list));
            return;
        }
        for (int i = start; i < candidates.length; i++) {
            // 剪枝处理
            if (sum + candidates[i] > target) {
                continue;
            }
            if (i > 0 && candidates[i] == candidates[i - 1] && hasIn[i - 1] == false) {
                continue;
            }
            hasIn[i] = true;
            sum += candidates[i];
            list.add(candidates[i]);
            backtrack4CombinationSum2(res, list, sum, target, candidates, i + 1, hasIn);
            sum -= candidates[i];
            hasIn[i] = false;
            list.remove(list.size() - 1);
        }
    }

    /**
     * 分割回文串
     * 知道要回溯，但是没做出来
     * 我可能只会套模板，但是没有真正能够将回溯自由发挥
     * 脑子里其实想清楚了树形图，但是没有实现出来，也可能是代码实现的能力有待提高
     */
    public List<List<String>> partition(String s) {
        List<List<String>> res = new ArrayList<>();
        if (s == null || s.length() == 0) {
            return res;
        }
        backtrack4Partition(res, new ArrayList<>(), s, 0);
        return res;
    }

    private void backtrack4Partition(List<List<String>> res, List<String> path, String s, int start) {
        if (start == s.length()) { // 如果起始位置等于字符串长度，说明找到了一种分割方式
            res.add(new ArrayList<>(path));
            return;
        }
        // 从起始位置开始尝试分割
        for (int end = start; end < s.length(); end++) {
            // 如果从 start 到 end 是回文，则进行下一步递归
            // 这里顺便进行了剪枝
            if (isPalindrome(s, start, end)) {
                path.add(s.substring(start, end + 1)); // 加入当前回文子串
                backtrack4Partition(res, path, s, end + 1); // 递归处理剩余部分
                path.remove(path.size() - 1); // 回溯，移除当前子串
            }
        }
    }

    private boolean isPalindrome(String s, int start, int end) {
        // 双指针判断回文串了
        while (start < end) {
            if (s.charAt(start) != s.charAt(end)) {
                return false;
            }
            start++;
            end--;
        }
        return true;
    }

    /**
     * 复原IP地址
     * 还是要注意终止条件应该怎么写
     * 另外有了上一题切割回文串的经验，这里已经对如何写切割的回溯处理逻辑有了方法论
     */
    public List<String> restoreIpAddresses(String s) {
        List<String> res = new ArrayList<>();
        backtrack4RestoreIp(res, new StringBuilder(), s, 0, 0);
        return res;
    }

    private void backtrack4RestoreIp(List<String> res, StringBuilder sb, String s, int start, int num) {
        if (num == 4) {
            if (start == s.length()) {
                res.add(sb.substring(0, sb.length() - 1));
            }
            return;
        }
        for (int end = start; end < start + 3 && end < s.length(); end++) {
            String segment = s.substring(start, end + 1);
            if (isValidIpSection(segment)) {
                int len = sb.length();
                sb.append(segment).append(".");
                backtrack4RestoreIp(res, sb, s, end + 1, num + 1);
                sb.delete(len, sb.length());
            }
        }
    }

    private boolean isValidIpSection(String s, int start, int end) {
        if (end - start >= 3) {
            return false;
        }
        String segment = s.substring(start, end + 1);
        if (segment.startsWith("0") && segment.length() > 1) { // 检查前导 0
            return false;
        }
        int val = Integer.parseInt(segment);
        return val >= 0 && val <= 255;
    }

    private boolean isValidIpSection(String segment) {
        int len = segment.length();
        if (len > 3) {
            return false;
        }
        if (segment.startsWith("0") && len > 1) { // 检查前导 0
            return false;
        }
        int val = Integer.parseInt(segment);
        return val >= 0 && val <= 255;
    }

    /**
     * 子集
     * 第二次提交过了，击败100%
     */
    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        backtracking4Subsets(res, new ArrayList<>(), nums, 0);
        return res;
    }

    private void backtracking4Subsets(List<List<Integer>> res, List<Integer> list, int[] nums, int index) {
        if (index == nums.length) {
            res.add(new ArrayList<>(list));
            return;
        }
        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                list.add(nums[index]);
                backtracking4Subsets(res, list, nums, index + 1);
                list.remove(list.size() - 1);
            } else {
                backtracking4Subsets(res, list, nums, index + 1);
            }
        }
    }

    /**
     * 子集II
     * 难处依旧是去重
     * 过了，但是只击败了4.18%
     * 果然按照上一题的思路不好改，得按随想录的上一题思路，那么就好改了
     * 上一个如果和当前的一样，且上一个没有被选入，那么当前的也不应该被选入
     */
    public List<List<Integer>> subsetsWithDup(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        Arrays.sort(nums); // 排序以便去重
        backtracking4SubsetsWithDup(res, new ArrayList<>(), nums, 0);
        return res;
    }

    private void backtracking4SubsetsWithDup(List<List<Integer>> res, List<Integer> tempList, int[] nums, int start) {
        res.add(new ArrayList<>(tempList)); // 每次都加入当前子集

        for (int i = start; i < nums.length; i++) {
            // 如果当前数与前一个数相同，且前一个数没有被选择，则跳过
            if (i > start && nums[i] == nums[i - 1])
                continue;

            tempList.add(nums[i]); // 选择当前数
            backtracking4SubsetsWithDup(res, tempList, nums, i + 1); // 递归
            tempList.remove(tempList.size() - 1); // 撤销选择
        }
    }

    /**
     * 递增子序列
     * 本题的难点在不能排序而且要去重，所以选用布尔数组used
     * 需要注意的是，uesd在每一层递归应该有一个新的初始化
     */
    public List<List<Integer>> findSubsequences(int[] nums) {
        if (nums == null || nums.length < 2) {
            return new ArrayList<>();
        }
        List<List<Integer>> res = new ArrayList<>();
        backtrack4FindSubsequences(res, new ArrayList<>(), nums, 0);
        return res;
    }

    private void backtrack4FindSubsequences(List<List<Integer>> res, List<Integer> list, int[] nums, int start) {
        if (list.size() >= 2) {
            res.add(new ArrayList<>(list));
        }
        boolean[] used = new boolean[201];
        for (int i = start; i < nums.length; i++) {
            if (!list.isEmpty() && list.get(list.size() - 1) > nums[i] || used[100 + nums[i]]) {
                continue;
            }
            used[100 + nums[i]] = true;
            list.add(nums[i]);
            backtrack4FindSubsequences(res, list, nums, i + 1);
            list.remove(list.size() - 1);
        }
    }

    /**
     * 全排列
     * 一次过，击败96.37%
     */
    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        backtrack4Permute(res, new ArrayList<>(), nums, new boolean[21]);
        return res;
    }

    private void backtrack4Permute(List<List<Integer>> res, List<Integer> list, int[] nums, boolean[] used) {
        if (list.size() == nums.length) {
            res.add(new ArrayList<>(list));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (used[10 + nums[i]]) {
                continue;
            }
            used[10 + nums[i]] = true;
            list.add(nums[i]);
            backtrack4Permute(res, list, nums, used);
            list.remove(list.size() - 1);
            used[10 + nums[i]] = false;
        }
    }

    /**
     * 全排列 II
     * 稍微修改了一下，两次过
     * 击败99.97%
     */
    public List<List<Integer>> permuteUnique(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        Arrays.sort(nums);
        backtrack4PermuteUnique(res, new ArrayList<>(), nums, new boolean[nums.length]);
        return res;
    }

    private void backtrack4PermuteUnique(List<List<Integer>> res, List<Integer> list, int[] nums, boolean[] used) {
        if (list.size() == nums.length) {
            res.add(new ArrayList<>(list));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (used[i] || i > 0 && nums[i] == nums[i - 1] && !used[i - 1]) {
                continue;
            }
            used[i] = true;
            list.add(nums[i]);
            backtrack4PermuteUnique(res, list, nums, used);
            list.remove(list.size() - 1);
            used[i] = false;
        }
    }

    /**
     * 重新安排行程
     * 包括这道，接下来连着三题都是hard
     * 没做出来，首先是传统的DFS会超时
     * 然后是对如何处理字典序的问题没有处理好，我最开始没想排序
     * 这个题解用到了优先队列（堆），很巧妙
     * 还有就是DFS到从后往前构建路径的逻辑顺序要想清楚
     * quote：
     * 递归函数什么时候需要返回值？什么时候不需要返回值？这里总结如下三点：
     * 1.如果需要搜索整棵二叉树且不用处理递归返回值，递归函数就不要返回值。（这种情况就是本文下半部分介绍的113.路径总和ii）
     * 2.如果需要搜索整棵二叉树且需要处理递归返回值，递归函数就需要返回值。 （这种情况我们在236. 二叉树的最近公共祖先 (opens new
     * window)中介绍）
     * 3.如果要搜索其中一条符合条件的路径，那么递归一定需要返回值，因为遇到符合条件的路径了就要及时返回。（本题的情况）
     */
    public List<String> findItineraryAwesomeSolution(List<List<String>> tickets) {
        // 构建邻接表并排序
        Map<String, PriorityQueue<String>> graph = new HashMap<>();
        for (List<String> ticket : tickets) {
            graph.computeIfAbsent(ticket.get(0), k -> new PriorityQueue<>()).offer(ticket.get(1));
        }

        List<String> res = new LinkedList<>();
        dfs("JFK", graph, res);
        return res;
    }

    private void dfs(String airport, Map<String, PriorityQueue<String>> graph, List<String> res) {
        PriorityQueue<String> destinations = graph.get(airport);
        while (destinations != null && !destinations.isEmpty()) {
            String next = destinations.poll();
            dfs(next, graph, res);
        }
        // 从后往前构建路径
        res.add(0, airport);
    }

    /**
     * 重新安排行程
     * 推荐学习这个随想录的解法，经过我的修改之后击败100%
     */
    public List<String> findItinerary(List<List<String>> tickets) {
        List<String> result = new ArrayList<>();
        Map<String, List<String>> ticketMap = new HashMap<>();
        // 遍历tickets，存入ticketMap中
        for (List<String> ticket : tickets) {
            addNew(ticket.get(0), ticket.get(1), ticketMap);
        }
        backtrack4FindItinerary(result, tickets, ticketMap, "JFK");
        return result;
    }

    private boolean backtrack4FindItinerary(List<String> result, List<List<String>> tickets,
            Map<String, List<String>> ticketMap, String currentLocation) {
        result.add(currentLocation);
        // 机票全部用完，找到最小字符路径
        if (result.size() == tickets.size() + 1) {
            return true;
        }
        // 当前位置的终点列表
        List<String> targetLocations = ticketMap.get(currentLocation);
        // 没有从当前位置出发的机票了，说明这条路走不通
        if (targetLocations != null && !targetLocations.isEmpty()) {
            // 终点列表中遍历到的终点
            String targetLocation;
            // 遍历从当前位置出发的机票
            for (int i = 0; i < targetLocations.size(); i++) {
                // 去重，否则在最后一个测试用例中遇到循环时会无限递归
                if (i > 0 && targetLocations.get(i).equals(targetLocations.get(i - 1)))
                    continue;
                targetLocation = targetLocations.get(i);
                // 删除终点列表中当前的终点
                targetLocations.remove(i);
                // 递归
                if (backtrack4FindItinerary(result, tickets, ticketMap, targetLocation)) {
                    return true;
                }
                // 路线走不通，将机票重新加回去
                targetLocations.add(i, targetLocation);
                result.remove(result.size() - 1);
            }
        }
        return false;
    }

    /**
     * 在map中按照字典顺序添加新元素
     */
    private void addNew(String start, String destination, Map<String, List<String>> ticketMap) {
        List<String> destinationListSortedByDict = ticketMap.getOrDefault(start, new ArrayList<>());
        if (!destinationListSortedByDict.isEmpty()) {
            for (int i = 0; i < destinationListSortedByDict.size(); i++) {
                if (destination.compareTo(destinationListSortedByDict.get(i)) < 0) {
                    destinationListSortedByDict.add(i, destination);
                    return;
                }
            }
            destinationListSortedByDict.add(destinationListSortedByDict.size(), destination);
        } else {
            destinationListSortedByDict.add(destination);
            ticketMap.put(start, destinationListSortedByDict);
        }
    }

    /**
     * N皇后 hard
     * n个queen，n * n的棋盘
     * N皇后！！！这道hard题，我一次过！！！虽然只击败40.6%
     */
    public List<List<String>> solveNQueens(int n) {
        List<List<String>> res = new ArrayList<>();
        char[][] cheer = new char[n][n];
        for (char[] chars : cheer) {
            Arrays.fill(chars, '.');
        }
        backtrack4NQueens(res, cheer, n, 0);
        return res;
    }

    private void backtrack4NQueens(List<List<String>> res, char[][] cheer, int n, int row) {
        if (row == n) {
            List<String> list = new ArrayList<>();
            for (char[] chars : cheer) {
                list.add(new String(chars));
            }
            res.add(list);
            return;
        }
        for (int i = 0; i < cheer[row].length; i++) {
            if (isValid(cheer, row, i, false)) {
                cheer[row][i] = 'Q';
                backtrack4NQueens(res, cheer, n, row + 1);
                cheer[row][i] = '.';
            }
        }
    }

    private boolean isValid(char[][] cheer, int row, int column, boolean useRecursion) {
        // 经过实测，这里使用迭代进行校验比使用递归校验更有效率
        if (useRecursion) {
            return isValidPlcaementOnColumn(cheer, row, column) && isValidPlcaementOnLeftIncline(cheer, row, column)
                    && isValidPlcaementOnRightIncline(cheer, row, column);
        } else {
            // 检查列
            for (int i = 0; i < row; ++i) { // 相当于剪枝
                if (cheer[i][column] == 'Q') {
                    return false;
                }
            }
            // 检查45度对角线
            for (int i = row - 1, j = column - 1; i >= 0 && j >= 0; i--, j--) {
                if (cheer[i][j] == 'Q') {
                    return false;
                }
            }
            // 检查135度对角线
            for (int i = row - 1, j = column + 1; i >= 0 && j <= cheer[0].length - 1; i--, j++) {
                if (cheer[i][j] == 'Q') {
                    return false;
                }
            }
            return true;
        }
    }

    private boolean isValidPlcaementOnColumn(char[][] cheer, int row, int column) {
        if (row < 0 || column < 0 || row >= cheer[0].length || column >= cheer[0].length) {
            return true;
        }
        return cheer[row][column] != 'Q' && isValidPlcaementOnColumn(cheer, row - 1, column);
    }

    private boolean isValidPlcaementOnLeftIncline(char[][] cheer, int row, int column) {
        if (row < 0 || column < 0 || row >= cheer[0].length || column >= cheer[0].length) {
            return true;
        }
        return cheer[row][column] != 'Q' && isValidPlcaementOnLeftIncline(cheer, row - 1, column - 1);
    }

    private boolean isValidPlcaementOnRightIncline(char[][] cheer, int row, int column) {
        if (row < 0 || column < 0 || row >= cheer[0].length || column >= cheer[0].length) {
            return true;
        }
        return cheer[row][column] != 'Q' && isValidPlcaementOnRightIncline(cheer, row - 1, column + 1);
    }

    /**
     * 解数独
     * hard
     * 没做出来
     */
    public void solveSudoku(char[][] board) {
        if (board == null || board.length == 0) {
            return;
        }
        backtrack4SudoKu(board, 0, 0);
    }

    private boolean backtrack4SudoKu(char[][] board, int row, int col) {
        if (row == 9) {
            return true; // 到达第9行，说明已经填满整个棋盘
        }

        // 如果当前列到了末尾，切换到下一行
        if (col == 9) {
            return backtrack4SudoKu(board, row + 1, 0);
        }

        // 如果当前格子已经有数字，跳过
        if (board[row][col] != '.') {
            return backtrack4SudoKu(board, row, col + 1);
        }

        // 尝试填充每一个可能的数字
        for (char ch : getPossibleNums(row, col, board)) {
            board[row][col] = ch; // 做选择
            if (backtrack4SudoKu(board, row, col + 1)) { // 递归下一步
                return true;
            }
            board[row][col] = '.'; // 撤销选择
        }

        return false; // 如果没有一个数字能成功填充，返回 false
    }

    private List<Character> getPossibleNums(int row, int col, char[][] board) {
        boolean[] numsUsed = new boolean[10]; // 1-9 的使用状态

        // 检查当前行
        for (int i = 0; i < 9; i++) {
            if (board[row][i] != '.') {
                numsUsed[board[row][i] - '0'] = true;
            }
        }

        // 检查当前列
        for (int i = 0; i < 9; i++) {
            if (board[i][col] != '.') {
                numsUsed[board[i][col] - '0'] = true;
            }
        }

        // 检查当前 3x3 小方块
        int boxStartRow = (row / 3) * 3;
        int boxStartCol = (col / 3) * 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                char ch = board[boxStartRow + i][boxStartCol + j];
                if (ch != '.') {
                    numsUsed[ch - '0'] = true;
                }
            }
        }

        // 收集未被使用的数字
        List<Character> list = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            if (!numsUsed[i]) {
                list.add((char) ('0' + i));
            }
        }

        return list;
    }

    /**
     * 解数独
     * 这是随想录的解法，虽然耗时比较多，但是比较好理解
     * 暂时先不去理解位运算的优化策略了
     * 本题的核心是写一个函数来判断某位置放入某个数字后该数独是否合法
     * 其余就是正常回溯
     */
    public void solveSudokuSuixianglu(char[][] board) {
        solveSudokuHelper(board);
    }

    private boolean solveSudokuHelper(char[][] board) {
        // 「一个for循环遍历棋盘的行，一个for循环遍历棋盘的列，
        // 一行一列确定下来之后，递归遍历这个位置放9个数字的可能性！」
        for (int i = 0; i < 9; i++) { // 遍历行
            for (int j = 0; j < 9; j++) { // 遍历列
                if (board[i][j] != '.') { // 跳过原始数字
                    continue;
                }
                for (char k = '1'; k <= '9'; k++) { // (i, j) 这个位置放k是否合适
                    if (isValidSudoku(i, j, k, board)) {
                        board[i][j] = k;
                        if (solveSudokuHelper(board)) { // 如果找到合适一组立刻返回
                            return true;
                        }
                        board[i][j] = '.';
                    }
                }
                // 9个数都试完了，都不行，那么就返回false
                return false;
                // 因为如果一行一列确定下来了，这里尝试了9个数都不行，说明这个棋盘找不到解决数独问题的解！
                // 那么会直接返回， 「这也就是为什么没有终止条件也不会永远填不满棋盘而无限递归下去！」
            }
        }
        // 遍历完没有返回false，说明找到了合适棋盘位置了
        return true;
    }

    /**
     * 判断棋盘是否合法有如下三个维度:
     * 同行是否重复
     * 同列是否重复
     * 9宫格里是否重复
     */
    private boolean isValidSudoku(int row, int col, char val, char[][] board) {
        // 同行是否重复
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == val) {
                return false;
            }
        }
        // 同列是否重复
        for (int j = 0; j < 9; j++) {
            if (board[j][col] == val) {
                return false;
            }
        }
        // 9宫格里是否重复
        int startRow = (row / 3) * 3;
        int startCol = (col / 3) * 3;
        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                if (board[i][j] == val) {
                    return false;
                }
            }
        }
        return true;
    }

}
