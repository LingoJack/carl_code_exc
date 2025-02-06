package leetcodeHot100;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class leetcodeExercise {
    /**
     * 划分字母区间
     * 25min ac
     * 但是击败5%
     * 不如第一次做的时候想出来的思路
     * 第一次做的思路：
     * 记录每一个字母最后出现的位置，跟踪串内字母的最右边界
     * 跟踪并更新当前串长度
     */
    public List<Integer> partitionLabels(String s) {
        Map<Character, Integer> lastOccurIndexMap = new HashMap<>();
        Map<Character, Integer> firstOccurIndexMap = new HashMap<>();
        int len = s.length();
        for (int i = 0; i < len; i++) {
            char ch = s.charAt(i);
            lastOccurIndexMap.put(ch, i);
            firstOccurIndexMap.putIfAbsent(ch, i);
        }
        Character firstCh = s.charAt(0);
        Character lastCh = s.charAt(0);
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            char ch = s.charAt(i);
            if (firstCh == null) {
                firstCh = ch;
            }
            if (lastCh == null) {
                lastCh = ch;
            }
            if (i == lastOccurIndexMap.get(lastCh)) {
                res.add(i - firstOccurIndexMap.get(firstCh) + 1);
                firstCh = null;
                lastCh = null;
                continue;
            }
            if (lastOccurIndexMap.get(lastCh) < lastOccurIndexMap.get(ch)) {
                // baba
                lastCh = ch;
            }
        }
        return res;
    }

    /**
     * 二叉树的层序遍历
     */
    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        Deque<TreeNode> queue = new ArrayDeque<>();
        if (root != null) {
            queue.offer(root);
        }
        while (!queue.isEmpty()) {
            int size = queue.size();
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
                list.add(node.val);
            }
            res.add(list);
        }
        return res;
    }

    /**
     * 不同路径
     */
    public int uniquePaths(int m, int n) {
        int[][] dp = new int[m][n];
        for (int i = 0; i < m; i++) {
            dp[i][0] = 1;
        }
        for (int i = 0; i < n; i++) {
            dp[0][i] = 1;
        }
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
            }
        }
        return dp[m - 1][n - 1];
    }

    /**
     * 二叉树的最大深度
     * 思路是层序遍历
     */
    public int maxDepth(TreeNode root) {
        Deque<TreeNode> queue = new ArrayDeque<>();
        if (root != null) {
            queue.offer(root);
        }
        int height = 0;
        while (!queue.isEmpty()) {
            int size = queue.size();
            height++;
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                if (node.left != null)
                    queue.offer(node.left);
                if (node.right != null)
                    queue.offer(node.right);
            }
        }
        return height;
    }

    /**
     * 翻转二叉树
     */
    public TreeNode invertTree(TreeNode root) {
        invert(root);
        return root;
    }

    private void invert(TreeNode node) {
        if (node == null)
            return;
        TreeNode ltSon = node.left;
        node.left = node.right;
        node.right = ltSon;
        invert(node.left);
        invert(node.right);
    }

    /**
     * 对称二叉树
     */
    public boolean isSymmetric(TreeNode root) {
        if (root == null) {
            return true;
        }
        return check(root.left, root.right);
    }

    private boolean check(TreeNode lt, TreeNode rt) {
        if (lt == null && rt == null) {
            return true;
        }
        if (lt != null && rt != null) {
            if (lt.val != rt.val) {
                return false;
            }
            return check(lt.left, rt.right) && check(lt.right, rt.left);
        }
        return false;
    }

    /**
     * 买卖股票的最佳时机
     * 这里需要限制交易次数
     * 不限制的做法：dp[i][1] = Math.max(dp[i - 1][0] - prices[i], dp[i - 1][1]);
     * 限制的做法： dp[i][1] = Math.max(-prices[i], dp[i - 1][1]);
     */
    public int maxProfit(int[] prices) {
        // 0 未持有 1 持有
        int[][] dp = new int[prices.length][2];
        dp[0][0] = 0;
        dp[0][1] = -prices[0];
        for (int i = 1; i < prices.length; i++) {
            dp[i][0] = Math.max(dp[i - 1][1] + prices[i], dp[i - 1][0]);
            dp[i][1] = Math.max(-prices[i], dp[i - 1][1]);
        }
        return Math.max(dp[prices.length - 1][1], dp[prices.length - 1][0]);
    }

    /**
     * 跳跃游戏
     * 核心是记录范围的变化
     */
    public boolean canJump(int[] nums) {
        // 3,2,1,0,4
        // 2,3,1,1,4
        int scope = 0;
        for (int i = 0; i <= scope && scope < nums.length; i++) {
            scope = Math.max(scope, i + nums[i]);
        }
        return scope >= nums.length - 1;
    }

    /**
     * 跳跃游戏II
     * dp做出来了，但是时间复杂度太高
     */
    public int jump(int[] nums) {
        // 跳到i位置所需的最少跳跃次数
        int[] dp = new int[nums.length];
        Arrays.fill(dp, nums.length);
        dp[0] = 0;
        for (int i = 0; i < nums.length; i++) {
            for (int j = 0; j <= i; j++) {
                if (j + nums[j] >= i) {
                    dp[i] = Math.min(dp[i], dp[j] + 1);
                }
            }
        }
        return dp[nums.length - 1];
    }

    /**
     * 全排列
     * 这次没做出来。。。
     * 第一次做的时候可是一次过并且击败99%的
     * 必须要每天做题保持敏感度和思维
     */
    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        dfs4Permute(nums, res, new ArrayList<>(), new boolean[21]);
        return res;
    }

    private void dfs4Permute(int[] nums, List<List<Integer>> res, List<Integer> list, boolean[] used) {
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
            dfs4Permute(nums, res, list, used);
            list.remove(list.size() - 1);
            used[10 + nums[i]] = false;
        }
    }

    /**
     * 子集
     */
    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        dfs4Subsets(res, new ArrayList<>(), 0, nums);
        return res;
    }

    private void dfs4Subsets(List<List<Integer>> res, List<Integer> list, int index, int[] nums) {
        if (index == nums.length) {
            res.add(new ArrayList<>(list));
            return;
        }
        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                dfs4Subsets(res, list, index + 1, nums);
            } else {
                list.add(nums[index]);
                dfs4Subsets(res, list, index + 1, nums);
                list.remove(list.size() - 1);
            }
        }
    }

    /**
     * 电话号码的字母组合
     */
    public List<String> letterCombinations(String digits) {
        List<String> res = new ArrayList<>();
        if (digits.isEmpty()) {
            return res;
        }
        char[][] map = new char[][] {
                {},
                {},
                { 'a', 'b', 'c' },
                { 'd', 'e', 'f' },
                { 'g', 'h', 'i' },
                { 'j', 'k', 'l' },
                { 'm', 'n', 'o' },
                { 'p', 'q', 'r', 's' },
                { 't', 'u', 'v' },
                { 'w', 'x', 'y', 'z' }
        };
        dfs4LetterCombainations(digits, map, res, new StringBuilder(), 0);
        return res;
    }

    private void dfs4LetterCombainations(String digits, char[][] map, List<String> res, StringBuilder sb, int index) {
        if (index == digits.length()) {
            res.add(sb.toString());
            return;
        }
        char[] chs = map[digits.charAt(index) - '0'];
        for (int i = 0; i < chs.length; i++) {
            sb.append(chs[i]);
            dfs4LetterCombainations(digits, map, res, sb, index + 1);
            sb.setLength(sb.length() - 1);
        }
    }

    /**
     * 组合总和
     */
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        Arrays.sort(candidates);
        List<List<Integer>> res = new ArrayList<>();
        dfs4CombainationSum(candidates, target, res, new ArrayList<>(), 0);
        return res;
    }

    private void dfs4CombainationSum(int[] candidates, int target, List<List<Integer>> res, List<Integer> list,
            int index) {
        if (target == 0) {
            res.add(new ArrayList<>(list));
            return;
        }
        if (target < 0 || index >= candidates.length) {
            return;
        }
        for (int i = index; i < candidates.length; i++) {
            if (target >= candidates[i]) {
                list.add(candidates[i]);
                dfs4CombainationSum(candidates, target - candidates[i], res, list, i);
                list.remove(list.size() - 1);
            }
        }
    }

    /**
     * 两数之和
     */
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        int[] res = new int[2];
        for (int i = 0; i < nums.length; i++) {
            if (map.get(nums[i]) != null) {
                res[0] = i;
                res[1] = map.get(nums[i]);
            }
            map.put(target - nums[i], i);
        }
        return res;
    }

    /**
     * 字母异位词分组
     */
    public List<List<String>> groupAnagrams(String[] strs) {
        // 字母映射到一个列表
        Map<String, List<String>> map = new HashMap<>();
        for (String s : strs) {
            char[] chs = s.toCharArray();
            Arrays.sort(chs);
            String sorted = new String(chs);
            List<String> list = map.getOrDefault(sorted, new ArrayList<>());
            list.add(s);
            map.put(sorted, list);
        }
        return new ArrayList<>(map.values());
    }

    /**
     * 最长连续序列
     * 这次的做法比第一次的做法好，击败90%
     */
    public int longestConsecutive(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        Arrays.sort(nums);
        int count = 1;
        int res = 1;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] == nums[i - 1])
                continue;
            if (nums[i] == nums[i - 1] + 1) {
                count++;
                res = Math.max(res, count);
            } else {
                count = 1;
            }
        }
        return res;
    }

    /**
     * 移动零
     */
    public void moveZeroes(int[] nums) {
        int slow = 0;
        int fast = 0;
        while (fast < nums.length) {
            if (nums[fast] != 0) {
                int t = nums[fast];
                nums[fast] = nums[slow];
                nums[slow] = t;
                slow++;
            }
            fast++;
        }
    }

    /**
     * 盛最多水的容器
     * 没做出来，但其实思路很简单，就是一开始左右指针，选择较矮的柱子移动
     */
    public int maxArea(int[] height) {
        int lt = 0;
        int rt = height.length - 1;
        int max = (rt - lt) * Math.min(height[rt], height[lt]);
        while (lt < rt) {
            if (height[rt] > height[lt]) {
                lt++;
            } else {
                rt--;
            }
            max = Math.max(max, (rt - lt) * Math.min(height[rt], height[lt]));
        }
        return max;
    }

    /**
     * 三数之和
     * 思路是对的，但是实现有些问题，最终还是做出来了
     */
    public List<List<Integer>> threeSum(int[] nums) {
        // -1,0,1,2,-1,-4
        // -1 -1 0 1 2
        Arrays.sort(nums);
        List<List<Integer>> res = new ArrayList<>();
        int lt = 0, mid = 1, rt = nums.length - 1;
        while (lt <= nums.length - 2) {
            if (lt >= 1 && nums[lt] == nums[lt - 1]) {
                lt++;
                continue;
            }
            rt = nums.length - 1;
            mid = lt + 1;
            int target = -nums[lt];
            while (rt > mid) {
                if (nums[rt] + nums[mid] > target) {
                    rt--;
                    // 跳过重复的
                    while (rt > mid && nums[rt] == nums[rt + 1]) {
                        rt--;
                    }
                } else if (nums[rt] + nums[mid] < target) {
                    mid++;
                    while (mid < rt && nums[mid] == nums[mid - 1]) {
                        mid++;
                    }
                } else {
                    res.add(List.of(nums[lt], nums[mid], nums[rt]));
                    mid++;
                    while (mid < rt && nums[mid] == nums[mid - 1]) {
                        mid++;
                    }
                }
            }
            lt++;
        }
        return res;
    }

    /**
     * 接雨水
     */
    public int trap(int[] height) {
        int res = 0;
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 0; i < height.length; i++) {
            while (!stack.isEmpty() && height[stack.peek()] < height[i]) {
                int lowIndex = stack.pop();
                int h = stack.peek() != null ? (Math.min(height[stack.peek()], height[i])) - height[lowIndex] : 0;
                int w = stack.peek() != null ? i - stack.peek() - 1 : 0;
                res += w * h;
            }
            stack.push(i);
        }
        return res;
    }

    /**
     * 无重复字符子串
     */
    public int lengthOfLongestSubstring(String s) {
        if (s == null || s.isEmpty()) {
            return 0;
        }
        int max = 0;
        int slow = 0;
        Map<Character, Boolean> map = new HashMap<>();
        int len = s.length();
        for (int fast = 0; fast < len; fast++) {
            while (map.getOrDefault(s.charAt(fast), false)) {
                map.put(s.charAt(slow), false);
                slow++;
            }
            map.put(s.charAt(fast), true);
            max = Math.max(max, fast - slow + 1);
        }
        return max;
    }

    /**
     * 找到字符串中的所有字母异位词
     * 做出来了，但这不是最好的方法
     */
    public List<Integer> findAnagrams(String s, String p) {
        int count = 0;
        int[] exist = new int[26];
        int[] used = new int[26];
        char[] pChars = p.toCharArray();
        for (char ch : pChars) {
            exist[ch - 'a']++;
        }

        List<Integer> res = new ArrayList<>();
        // cbaeb 5 2
        // abc
        for (int i = 0; i <= s.length() - p.length(); i++) {
            int cur = i;
            while (cur <= p.length() - 1 + i) {
                if (exist[s.charAt(cur) - 'a'] > 0 && used[s.charAt(cur) - 'a'] < exist[s.charAt(cur) - 'a']) {
                    used[s.charAt(cur) - 'a']++;
                    count++;
                }
                cur++;
            }
            if (count == p.length()) {
                res.add(i);
            }
            Arrays.fill(used, 0);
            count = 0;
        }
        return res;
    }

    /**
     * 和为K的子数组
     * 前缀和
     * 二刷了，没做出来
     */
    public int subarraySum(int[] nums, int k) {
        if (nums == null) {
            return 0;
        }
        int[] prefixSum = new int[nums.length];
        prefixSum[0] = nums[0];
        for (int i = 1; i < nums.length; i++) {
            prefixSum[i] = nums[i] + prefixSum[i - 1];
        }
        int count = 0;
        // 记录前缀和出现的次数
        Map<Integer, Integer> map = new HashMap<>();
        // 初始化前缀和为0的情况，这个非常重要
        map.put(0, 1);
        for (int i = 0; i < nums.length; i++) {
            if (map.get(prefixSum[i] - k) != null) {
                count += map.getOrDefault(prefixSum[i] - k, 0);
            }
            map.put(prefixSum[i], map.getOrDefault(prefixSum[i], 0) + 1);
        }
        return count;
    }

    /**
     * 滑动窗口的最大值
     * 四刷了，终于艰难地自己写了出来
     */
    public int[] maxSlidingWindow(int[] nums, int k) {
        // 1,3,-1,-3,5,3,6,7
        // i
        // 3 -1 -3

        // 1,3,1,2,0,5
        // i
        // 3 2 0
        // 存放下标
        Deque<Integer> queue = new ArrayDeque<>();
        int[] res = new int[nums.length - k + 1];
        for (int i = 0; i < nums.length; i++) {
            while (!queue.isEmpty() && nums[queue.peekLast()] < nums[i]) {
                queue.pollLast();
            }
            queue.offerLast(i);
            if (i >= k - 1) {
                // 对于窗口之外的元素，应该移除
                while (queue.peekFirst() < i - k + 1) {
                    queue.pollFirst();
                }
                res[i - k + 1] = nums[queue.peekFirst()];
            }
        }
        return res;
    }

    /**
     * 最小覆盖子串
     * 三刷，没做出来
     * 有快慢指针的思路
     * 但是实现有问题
     */
    public String minWindow(String s, String t) {
        Map<Character, Integer> need = new HashMap<>();
        Map<Character, Integer> have = new HashMap<>();
        int sLen = s.length();
        int tLen = t.length();
        for (int i = 0; i < tLen; i++) {
            need.put(t.charAt(i), need.getOrDefault(t.charAt(i), 0) + 1);
        }
        int slow = 0;
        int fast = 0;
        int count = 0;
        int start = 0;
        int minLen = Integer.MAX_VALUE;
        while (fast < sLen) {
            if (count < need.size()) {
                char fastChar = s.charAt(fast);
                if (need.containsKey(fastChar)) {
                    have.put(fastChar, have.getOrDefault(fastChar, 0) + 1);
                    if (have.get(fastChar).equals(need.get(fastChar))) {
                        count++;
                    }
                }
                fast++;
            }
            while (count == need.size()) {
                if (fast - slow < minLen) {
                    minLen = fast - slow;
                    start = slow;
                }
                char slowChar = s.charAt(slow);
                if (need.containsKey(slowChar)) {
                    if (have.get(slowChar).equals(need.get(slowChar))) {
                        count--;
                    }
                    have.put(slowChar, have.getOrDefault(slowChar, 0) - 1);
                }
                slow++;
            }
        }
        return minLen == Integer.MAX_VALUE ? "" : s.substring(start, start + minLen);
    }

    /**
     * 最大子数组和
     * 没做出来
     * 主要是步骤1和2的顺序不能错，而且初始化要对
     */
    public int maxSubArray(int[] nums) {
        int len = nums.length;
        int sum = 0;
        int max = nums[0];
        for (int i = 0; i < len; i++) {
            // 1
            if (sum < 0) {
                sum = 0;
            }
            // 2
            sum += nums[i];
            max = Math.max(sum, max);
        }
        return max;
    }

    /**
     * 合并区间
     */
    public int[][] merge(int[][] intervals) {
        PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> {
            return a[0] != b[0] ? a[0] - b[0] : a[1] - b[1];
        });
        for (int[] interval : intervals) {
            heap.offer(interval);
        }
        Deque<int[]> stack = new ArrayDeque<>();
        while (!heap.isEmpty()) {
            if (stack.isEmpty()) {
                stack.push(heap.poll());
            } else {
                int[] last = stack.peek();
                int[] cur = heap.poll();
                if (last[1] >= cur[0]) {
                    stack.pop();
                    stack.push(new int[] { last[0], Math.max(last[1], cur[1]) });
                } else {
                    stack.push(cur);
                }
            }
        }
        int size = stack.size();
        int[][] res = new int[size][2];
        for (int i = 0; i < size; i++) {
            res[i] = stack.pop();
        }
        return res;
    }

    /**
     * 轮转数组
     */
    public void rotate(int[] nums, int k) {
        // 1,2,3,4,5,6,7
        // 7 6 5 4 3 2 1
        // 5,6,7,1,2,3,4
        k %= nums.length;
        reverse(nums, 0, nums.length - 1);
        reverse(nums, 0, k - 1);
        reverse(nums, k, nums.length - 1);
    }

    private void reverse(int[] nums, int start, int end) {
        if (start < 0 || end >= nums.length) {
            return;
        }
        int lt = start;
        int rt = end;
        while (lt < rt) {
            int t = nums[rt];
            nums[rt] = nums[lt];
            nums[lt] = t;
            lt++;
            rt--;
        }
    }

    /**
     * 除自身以外的数组的乘积
     * 前缀积和后缀积
     */
    public int[] productExceptSelf(int[] nums) {
        int len = nums.length;
        int[] ans = new int[len];
        int[] prefix = new int[len];
        int[] suffix = new int[len];
        int prod = 1;
        for (int i = 0; i < len; i++) {
            prod *= nums[i];
            prefix[i] = prod;
        }
        prod = 1;
        for (int i = len - 1; i >= 0; i--) {
            prod *= nums[i];
            suffix[i] = prod;
        }
        for (int i = 0; i < len; i++) {
            int prefixProd = i - 1 >= 0 ? prefix[i - 1] : 1;
            int suffixProd = i + 1 < len ? suffix[i + 1] : 1;
            ans[i] = prefixProd * suffixProd;
        }
        return ans;
    }

    /**
     * 缺失的第一个正数
     * 核心是想到长度和正数
     */
    public int firstMissingPositive(int[] nums) {
        int len = nums.length;
        boolean[] exist = new boolean[len + 1];
        for (int num : nums) {
            if (num > 0 && num <= len) {
                exist[num] = true;
            }
        }
        int count = 0;
        for (int i = 1; i < len + 1 && exist[i]; i++) {
            count++;
        }
        return count + 1;
    }

    /**
     * 矩阵置零
     */
    public void setZeroes(int[][] matrix) {
        int row = matrix.length;
        int col = matrix[0].length;
        boolean[] rowRecord = new boolean[row];
        boolean[] colRecord = new boolean[col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (matrix[i][j] == 0) {
                    rowRecord[i] = true;
                    colRecord[j] = true;
                }
            }
        }
        for (int i = 0; i < row; i++) {
            if (rowRecord[i]) {
                for (int j = 0; j < col; j++) {
                    matrix[i][j] = 0;
                }
            }
        }
        for (int j = 0; j < col; j++) {
            if (colRecord[j]) {
                for (int i = 0; i < row; i++) {
                    matrix[i][j] = 0;
                }
            }
        }
    }

    /**
     * 螺旋矩阵
     */
    public List<Integer> spiralOrder(int[][] matrix) {
        int row = matrix.length;
        int col = matrix[0].length;
        List<Integer> list = new ArrayList<>();
        boolean[][] visited = new boolean[row][col];
        int rowIndex = 0;
        int colIndex = 0;
        int count = 0;
        // 上右下左
        int dir = 2;
        while (count < row * col) {
            if (dir == 1) {
                for (int i = rowIndex; i >= 0; i--) {
                    if (!visited[i][colIndex]) {
                        list.add(matrix[i][colIndex]);
                        rowIndex = i;
                        visited[i][colIndex] = true;
                        count++;
                    }
                }
                dir = 2;
            } else if (dir == 2) {
                for (int i = 0; i < col; i++) {
                    if (!visited[rowIndex][i]) {
                        list.add(matrix[rowIndex][i]);
                        colIndex = i;
                        count++;
                        visited[rowIndex][i] = true;
                    }
                }
                dir = 3;
            } else if (dir == 3) {
                for (int i = 0; i < row; i++) {
                    if (!visited[i][colIndex]) {
                        list.add(matrix[i][colIndex]);
                        rowIndex = i;
                        count++;
                        visited[i][colIndex] = true;
                    }
                }
                dir = 4;
            } else if (dir == 4) {
                for (int i = colIndex; i >= 0; i--) {
                    if (!visited[rowIndex][i]) {
                        list.add(matrix[rowIndex][i]);
                        colIndex = i;
                        count++;
                        visited[rowIndex][i] = true;
                    }
                }
                dir = 1;
            }
        }
        return list;
    }

    /**
     * 旋转图像
     */
    public void rotate(int[][] matrix) {
        int row = matrix.length;
        int col = matrix[0].length;
        // 水平翻转
        for (int j = 0; j < col; j++) {
            for (int i = 0; i < row / 2; i++) {
                int t = matrix[i][j];
                matrix[i][j] = matrix[row - i - 1][j];
                matrix[row - i - 1][j] = t;
            }
        }
        // 沿主对角线翻转
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < i; j++) {
                int t = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = t;
            }
        }
    }

    /**
     * 搜索二维矩阵II
     * 做出来了，但是时间复杂度不够优秀
     * 题解的做法是如果从右上角看，左边的比其小，下边的比其大，是个BST去搜索
     */
    public boolean searchMatrix(int[][] matrix, int target) {
        int colNum = matrix[0].length;
        for (int[] row : matrix) {
            if (row[0] <= target && row[colNum - 1] >= target) {
                int lt = 0;
                int rt = colNum - 1;
                while (rt >= lt) {
                    int mid = (lt + rt) / 2;
                    if (row[mid] > target) {
                        rt = mid - 1;
                    } else if (row[mid] < target) {
                        lt = mid + 1;
                    } else {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 相交链表
     */
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        ListNode nodeA = headA;
        ListNode nodeB = headB;
        int lenA = 0;
        while (nodeA != null) {
            nodeA = nodeA.next;
            lenA++;
        }
        int lenB = 0;
        while (nodeB != null) {
            nodeB = nodeB.next;
            lenB++;
        }
        nodeA = headA;
        nodeB = headB;
        if (lenA > lenB) {
            for (int i = 0; i < lenA - lenB; i++) {
                nodeA = nodeA.next;
            }
        } else {
            for (int i = 0; i < lenB - lenA; i++) {
                nodeB = nodeB.next;
            }
        }
        while (nodeA != null && nodeB != null && nodeA != nodeB) {
            nodeA = nodeA.next;
            nodeB = nodeB.next;
        }
        return nodeA == null ? null : nodeA;
    }

    /**
     * 反转链表
     */
    public ListNode reverseList(ListNode head) {
        ListNode node = head;
        ListNode last = null;
        while (node != null) {
            ListNode next = node.next;
            node.next = last;
            last = node;
            node = next;
        }
        return last;
    }

    /**
     * 回文链表
     */
    public boolean isPalindrome(ListNode head) {
        ListNode fast = head;
        ListNode slow = head;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }
        ListNode node = slow;
        ListNode last = null;
        while (node != null) {
            ListNode next = node.next;
            node.next = last;
            last = node;
            node = next;
        }
        fast = last;
        slow = head;
        while (fast != null && slow != null) {
            if (fast.val != slow.val) {
                return false;
            }
            fast = fast.next;
            slow = slow.next;
        }
        return true;
    }

    /**
     * 环形链表
     */
    public boolean hasCycle(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
            if (slow == fast) {
                return true;
            }
        }
        return false;
    }

    /**
     * 环形链表II
     */
    public ListNode detectCycle(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
            if (slow == fast) {
                ListNode node1 = slow;
                ListNode node2 = head;
                while (node1 != node2) {
                    node1 = node1.next;
                    node2 = node2.next;
                }
                return node1;
            }
        }
        return null;
    }

    /**
     * 合并两个有序链表
     */
    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        ListNode n1 = list1;
        ListNode n2 = list2;
        ListNode dummy = new ListNode();
        ListNode node = dummy;
        while (n1 != null && n2 != null) {
            if (n1.val < n2.val) {
                node.next = n1;
                n1 = n1.next;
            } else {
                node.next = n2;
                n2 = n2.next;
            }
            node = node.next;
        }
        node.next = n1 == null ? n2 : n1;
        return dummy.next;
    }

    /**
     * 两数相加
     */
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode();
        ListNode node = dummy;
        ListNode n1 = l1;
        ListNode n2 = l2;
        int carryout = 0;
        while (n1 != null || n2 != null || carryout == 1) {
            int val = (n1 == null ? 0 : n1.val) + (n2 == null ? 0 : n2.val) + carryout;
            carryout = 0;
            if (val >= 10) {
                carryout++;
                val %= 10;
            }
            node.next = new ListNode(val);
            node = node.next;
            if (n1 != null) {
                n1 = n1.next;
            }
            if (n2 != null) {
                n2 = n2.next;
            }
        }
        return dummy.next;
    }

    /**
     * 删除链表的倒数第N个结点
     */
    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode dummy = new ListNode();
        dummy.next = head;
        ListNode last = null;
        ListNode fast = head;
        ListNode slow = head;
        for (int i = 0; i < n; i++) {
            fast = fast.next;
        }
        while (fast != null) {
            last = slow;
            slow = slow.next;
            fast = fast.next;
        }
        if (slow != head) {
            last.next = slow.next;
        } else {
            dummy.next = slow.next;
        }
        return dummy.next;
    }

    /**
     * 两两交换链表中的节点
     */
    public ListNode swapPairs(ListNode head) {
        ListNode dummy = new ListNode(0, head);
        ListNode slow = head;
        ListNode fast = head;
        ListNode nextGroupStart = head;
        ListNode prevGroupEnd = dummy;
        while (nextGroupStart != null && nextGroupStart.next != null) {
            fast = nextGroupStart.next;
            slow = prevGroupEnd.next;
            nextGroupStart = fast.next;
            prevGroupEnd.next = fast;
            fast.next = slow;
            slow.next = nextGroupStart;
            prevGroupEnd = slow;
        }
        return dummy.next;
    }

    /**
     * K个一组翻转链表
     */
    public ListNode reverseKGroup(ListNode head, int k) {
        ListNode dummy = new ListNode(0, head);
        ListNode nextGoupStart = head;
        ListNode prevGroupEnd = dummy;
        while (true) {
            ListNode fast = nextGoupStart;
            ListNode slow = nextGoupStart;
            int count = 0;
            while (count < k && fast != null) {
                fast = fast.next;
                count++;
            }
            if (count < k) {
                break;
            }
            ListNode node = slow;
            ListNode last = null;
            while (node != fast) {
                ListNode next = node.next;
                node.next = last;
                last = node;
                node = next;
            }
            prevGroupEnd.next = last;
            prevGroupEnd = slow;
            nextGoupStart = fast;
            slow.next = nextGoupStart;
        }
        return dummy.next;
    }

    /**
     * 随机链表的复制
     */
    public Node copyRandomList(Node head) {
        Node node = head;
        Node last = null;
        Map<Node, Node> map = new HashMap<>();
        while (node != null) {
            Node newNode = new Node(node.val);
            if (last != null) {
                last.next = newNode;
            }
            last = newNode;
            map.put(node, newNode);
            node = node.next;
        }
        node = head;
        while (node != null) {
            map.get(node).random = map.get(node.random);
            node = node.next;
        }
        return map.get(head);
    }

    class Node {
        int val;
        Node next;
        Node random;

        public Node(int val) {
            this.val = val;
            this.next = null;
            this.random = null;
        }
    }

    /**
     * 排序链表
     * 没做出来，大部分是对的
     * 就是while (fast.next != null && fast.next.next != null) {这里没对
     * 我错误写成了while (fast != null && fast.next != null)
     * 这里寻找中间节点的逻辑是要注意的
     */
    public ListNode sortList(ListNode head) {
        // 4 2 1 3
        // f
        // s
        return spilt(head);
    }

    private ListNode spilt(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode slow = head, fast = head;
        while (fast.next != null && fast.next.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }
        ListNode secondHead = slow.next;
        slow.next = null;
        ListNode ltPart = spilt(head);
        ListNode rtPart = spilt(secondHead);
        ListNode dummy = new ListNode();
        ListNode last = dummy;
        while (ltPart != null && rtPart != null) {
            if (ltPart.val < rtPart.val) {
                last.next = ltPart;
                last = ltPart;
                ltPart = ltPart.next;
            } else {
                last.next = rtPart;
                last = rtPart;
                rtPart = rtPart.next;
            }
        }
        last.next = rtPart == null ? ltPart : rtPart;
        return dummy.next;
    }

    /**
     * 合并k个升序链表
     */
    public ListNode mergeKLists(ListNode[] lists) {
        PriorityQueue<ListNode> heap = new PriorityQueue<>((a, b) -> a.val - b.val);
        for (ListNode node : lists) {
            if (node != null) {
                heap.offer(node);
            }
        }
        ListNode dummy = new ListNode();
        ListNode last = dummy;
        while (!heap.isEmpty()) {
            ListNode node = heap.poll();
            if (node != null && node.next != null) {
                heap.offer(node.next);
            }
            last.next = node;
            last = node;
        }
        return dummy.next;
    }

    /**
     * LRU缓存
     */
    public class LRUCache {

        private Map<Integer, Node> map = new HashMap<>();

        private Node head;

        private int capacity;

        private Node tail;

        public class Node {
            int key;
            int val;
            Node next;
            Node prev;

            public Node(int key, int val) {
                this.key = key;
                this.val = val;
            }

            public Node() {

            }
        }

        public LRUCache(int capacity) {
            this.capacity = capacity;
            this.head = new Node();
            this.tail = new Node();
            this.head.next = this.tail;
            this.tail.prev = this.head;
        }

        public int get(int key) {
            Node node = map.get(key);
            if (node != null) {
                node.prev.next = node.next;
                node.next.prev = node.prev;
                node.next = this.head.next;
                this.head.next.prev = node;
                node.prev = this.head;
                this.head.next = node;
                return node.val;
            }
            return -1;
        }

        public void put(int key, int value) {
            Node node = map.get(key);
            if (node == null) {
                node = new Node(key, value);
                map.put(key, node);
                node.next = this.head.next;
                this.head.next.prev = node;
                this.head.next = node;
                node.prev = this.head;
                if (map.entrySet().size() > capacity) {
                    Node removeNode = this.tail.prev;
                    this.tail.prev = removeNode.prev;
                    removeNode.prev.next = this.tail;
                    map.remove(removeNode.key);
                }
            } else {
                if (value == node.val) {
                    node.prev.next = node.next;
                    node.next.prev = node.prev;
                    node.next = this.head.next;
                    this.head.next.prev = node;
                    node.prev = this.head;
                    this.head.next = node;
                } else {
                    node.val = value;
                    node.prev.next = node.next;
                    node.next.prev = node.prev;
                    node.next = this.head.next;
                    this.head.next.prev = node;
                    node.prev = this.head;
                    this.head.next = node;
                }
            }
        }
    }

    /**
     * 二叉树的中序遍历
     */
    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        inorder(list, root);
        return list;
    }

    private void inorder(List<Integer> list, TreeNode node) {
        if (node == null) {
            return;
        }
        inorder(list, node.left);
        list.add(node.val);
        inorder(list, node.right);
    }

    /**
     * 二叉树的最大深度
     */
    public int maxDepthTwoEx(TreeNode root) {
        return maxGain(root);
    }

    private int maxGain(TreeNode node) {
        if (node == null) {
            return 0;
        }
        return Math.max(maxGain(node.left), maxGain(node.right)) + 1;
    }

    /**
     * 翻转二叉树
     */
    public TreeNode invertTreeTwoEx(TreeNode root) {
        invertTwoEx(root);
        return root;
    }

    private void invertTwoEx(TreeNode node) {
        if (node == null) {
            return;
        }
        TreeNode t = node.left;
        node.left = node.right;
        node.right = t;
        invertTwoEx(node.left);
        invertTwoEx(node.right);
    }

    /**
     * 对称二叉树
     */
    public boolean isSymmetricTwoEx(TreeNode root) {
        if (root == null) {
            return true;
        }
        return checkSymmetric(root.left, root.right);
    }

    private boolean checkSymmetric(TreeNode lt, TreeNode rt) {
        if (lt != null && rt != null) {
            if (lt.val != rt.val) {
                return false;
            }
            return checkSymmetric(lt.left, rt.right) && checkSymmetric(lt.right, rt.left);
        }
        return lt == null && rt == null;
    }

    /**
     * 二叉树的直径
     * 写出来了，但是原来的做法时间复杂度太高
     * 所以这里改成了正确的快的做法
     * 其实思路是一样的
     * 只是快的做法是在计算高度的时候拿到左右子树的高度就同时更新最大直径
     * 两种做法同样都是在树的高度上做文章
     */
    public int diameterOfBinaryTree(TreeNode root) {
        maxDiameterGain(root);
        return maxDiameter;
    }

    private int maxDiameter = 0;

    private int maxDiameterGain(TreeNode node) {
        if (node == null) {
            return 0;
        }
        int lt = maxDiameterGain(node.left);
        int rt = maxDiameterGain(node.right);
        int height = Math.max(lt, rt) + 1;
        maxDiameter = Math.max(maxDiameter, lt + rt);
        return height;
    }

    /**
     * 二叉树的层序遍历
     */
    public List<List<Integer>> levelOrderTwoEx(TreeNode root) {
        Deque<TreeNode> queue = new ArrayDeque<>();
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) {
            return res;
        }
        queue.offer(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                list.add(node.val);
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
            res.add(list);
        }
        return res;
    }

    /**
     * 将有序数组转为二叉树
     */
    public TreeNode sortedArrayToBST(int[] nums) {

    }
}