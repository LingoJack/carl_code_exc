package leetcodeHot100;

import java.security.KeyStore.Entry;
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
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

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
     * 这次没做出来，核心是used数组
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
     * 没做出来，但其实思路很简单，就是一开始左右指针，选择较矮的柱子移动（才有可能使得res增大）
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
     * 二刷了，没做出来，核心是前缀和的初始化map.put(0, 1);
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
        return getRoot(nums, 0, nums.length - 1);
    }

    private TreeNode getRoot(int[] nums, int start, int end) {
        if (end < start) {
            return null;
        }
        int mid = (start + end) / 2;
        TreeNode node = new TreeNode(nums[mid]);
        node.left = getRoot(nums, start, mid - 1);
        node.right = getRoot(nums, mid + 1, end);
        return node;
    }

    /**
     * 验证二叉搜索树
     * 这里是中序遍历去做的，但是时间复杂度不优秀
     */
    public boolean isValidBST(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        inorderTraversal(list, root);
        Integer last = null;
        for (int num : list) {
            if (last != null && num <= last) {
                return false;
            }
            last = num;
        }
        return true;
    }

    private void inorderTraversal(List<Integer> list, TreeNode node) {
        if (node == null) {
            return;
        }
        inorderTraversal(list, node.left);
        list.add(node.val);
        inorderTraversal(list, node.right);
    }

    public boolean isValidBSTTwoEx(TreeNode root) {
        return validate(root);
    }

    private boolean validate(TreeNode node) {
        if (node == null) {
            return true;
        }
        if (node.left != null) {
            TreeNode lt = node.left;
            while (lt != null && lt.right != null) {
                lt = lt.right;
            }
            if (node.val <= lt.val) {
                return false;
            }
        }
        if (node.right != null) {
            TreeNode rt = node.right;
            while (rt != null && rt.left != null) {
                rt = rt.left;
            }
            if (node.val >= rt.val) {
                return false;
            }
        }
        return validate(node.left) && validate(node.right);
    }

    /**
     * 二叉搜索树中第K小的元素
     */
    public int kthSmallest(TreeNode root, int k) {
        inorderFind(root, k);
        return val;
    }

    private int count = 0;

    private int val = 0;

    private void inorderFind(TreeNode node, int k) {
        if (node == null || count >= k) {
            return;
        }
        inorderFind(node.left, k);
        count++;
        if (count == k) {
            this.val = node.val;
        }
        inorderFind(node.right, k);
    }

    /**
     * 二叉树的右视图
     */
    public List<Integer> rightSideView(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        if (root == null) {
            return list;
        }
        Deque<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                if (i == size - 1) {
                    list.add(node.val);
                }
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
        }
        return list;
    }

    /**
     * 二叉树展开为链表
     * 递归解法没做出来
     * 迭代解法一下做出来了
     * 就是一直向右遍历，遇到有左子树的就处理
     */
    public void flatten(TreeNode root) {
        if (root == null) {
            return;
        }
        TreeNode node = root;
        while (node != null) {
            if (node.left != null) {
                TreeNode ltTail = node.left;
                while (ltTail != null && ltTail.right != null) {
                    ltTail = ltTail.right;
                }
                ltTail.right = node.right;
                node.right = node.left;
                node.left = null;
            }
            node = node.right;
        }
    }

    /**
     * 从前序遍历和中序遍历序列构造二叉树
     * 没做出来，这是第二次没做出来了
     * 核心是限定中序遍历的子树的构造范围
     */
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        // 3 9 20 15 7
        // 9 3 15 20 7
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) {
            map.put(inorder[i], i);
        }
        return getRoot(map, preorder, inorder, 0, inorder.length - 1);
    }

    private int preorderIndex = 0;

    private TreeNode getRoot(Map<Integer, Integer> map, int[] preorder, int[] inorder, int start, int end) {
        if (end < start) {
            return null;
        }
        int index = map.get(preorder[preorderIndex]);
        TreeNode node = new TreeNode(preorder[preorderIndex]);
        preorderIndex++;
        node.left = getRoot(map, preorder, inorder, start, index - 1);
        node.right = getRoot(map, preorder, inorder, index + 1, end);
        return node;
    }

    /**
     * 路经总和III
     * 暴力解做出来了
     * 但其实更好的做法是用前缀和
     * 没有做出最优解
     */
    public int pathSum(TreeNode root, long targetSum) {
        Deque<TreeNode> queue = new ArrayDeque<>();
        if (root == null) {
            return 0;
        }
        queue.offer(root);
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            dfs(node, targetSum);
            if (node.left != null) {
                queue.offer(node.left);
            }
            if (node.right != null) {
                queue.offer(node.right);
            }
        }
        return pathCount;
    }

    public int pathSumThreeEx(TreeNode root, long targetSum) {
        preorder(root, targetSum);
        return pathCount;
    }

    private void preorder(TreeNode node, long target) {
        if (node == null) {
            return;
        }
        dfs(node, target);
        preorder(node.left, target);
        preorder(node.right, target);
    }

    private int pathCount = 0;

    private void dfs(TreeNode node, long targetSum) {
        if (node == null) {
            return;
        }
        if (targetSum - node.val == 0) {
            pathCount++;
        }
        dfs(node.left, targetSum - node.val);
        dfs(node.right, targetSum - node.val);
    }

    /**
     * 二叉树的最近公共祖先
     */
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        // 如果没有这一个前置判断，在Leetcode的运行时间会大大增加
        if (haveSon(p, q)) {
            return p;
        } else if (haveSon(q, p)) {
            return q;
        }
        Deque<TreeNode> queue = new ArrayDeque<>();
        if (root == null) {
            return null;
        }
        queue.offer(root);
        TreeNode res = null;
        while (!queue.isEmpty()) {
            int size = queue.size();
            boolean updated = false;
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                if (haveSon(node, p) && haveSon(node, q)) {
                    res = node;
                    updated = true;
                }
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
            if (!updated && res != null) {
                break;
            }
        }
        return res;
    }

    private boolean haveSon(TreeNode node, TreeNode target) {
        if (node == null) {
            return false;
        }
        if (node == target) {
            return true;
        }
        return haveSon(node.left, target) || haveSon(node.right, target);
    }

    /**
     * 二叉树中的最大路径和
     * 计算每个节点作为路径转点的最大路劲和
     * 然后遍历这里是后序遍历的
     * 击败100%
     */
    public int maxPathSum(TreeNode root) {
        maxPathSumGain(root);
        return max;
    }

    private int max = Integer.MIN_VALUE;

    private int length;

    private int maxPathSumGain(TreeNode node) {
        if (node == null) {
            return 0;
        }
        int lt = maxPathSumGain(node.left);
        int rt = maxPathSumGain(node.right);
        max = Math.max(max, (lt < 0 ? 0 : lt) + (rt < 0 ? 0 : rt) + node.val);
        return Math.max((lt < 0 ? 0 : lt), (rt < 0 ? 0 : rt)) + node.val;
    }

    /**
     * 岛屿数量
     */
    public int numIslands(char[][] grid) {
        int rowNum = grid.length;
        int colNum = grid[0].length;
        boolean[][] visited = new boolean[rowNum][colNum];
        int count = 0;
        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < colNum; j++) {
                if (grid[i][j] == '1' && !visited[i][j]) {
                    count++;
                    dfs(grid, visited, i, j);
                }
            }
        }
        return count;
    }

    private void dfs(char[][] grid, boolean[][] visited, int row, int col) {
        int rowNum = grid.length;
        int colNum = grid[0].length;
        if (row >= rowNum || row < 0 || col >= colNum || col < 0) {
            return;
        }
        if (!visited[row][col] && grid[row][col] == '1') {
            visited[row][col] = true;
            dfs(grid, visited, row + 1, col);
            dfs(grid, visited, row - 1, col);
            dfs(grid, visited, row, col + 1);
            dfs(grid, visited, row, col - 1);
        }
    }

    /**
     * 腐烂的橘子
     */
    public int orangesRotting(int[][] grid) {
        Deque<int[]> queue = new ArrayDeque<>();
        if (grid == null) {
            return -1;
        }
        int row = grid.length;
        int col = grid[0].length;
        int count = 0;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (grid[i][j] == 2) {
                    queue.offer(new int[] { i, j });
                }
                if (grid[i][j] == 1) {
                    count++;
                }
            }
        }
        if (count == 0) {
            return 0;
        }
        int minute = -1;
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int[] index = queue.poll();
                int rowIndex = index[0], colIndex = index[1];
                if (rowIndex - 1 >= 0 && rowIndex < row && colIndex >= 0 && colIndex < col
                        && grid[rowIndex - 1][colIndex] == 1) {
                    count--;
                    grid[rowIndex - 1][colIndex] = 2;
                    queue.offer(new int[] { rowIndex - 1, colIndex });
                }
                if (rowIndex >= 0 && rowIndex + 1 < row && colIndex >= 0 && colIndex < col
                        && grid[rowIndex + 1][colIndex] == 1) {
                    count--;
                    grid[rowIndex + 1][colIndex] = 2;
                    queue.offer(new int[] { rowIndex + 1, colIndex });
                }
                if (rowIndex >= 0 && rowIndex < row && colIndex - 1 >= 0 && colIndex < col
                        && grid[rowIndex][colIndex - 1] == 1) {
                    count--;
                    grid[rowIndex][colIndex - 1] = 2;
                    queue.offer(new int[] { rowIndex, colIndex - 1 });
                }
                if (rowIndex >= 0 && rowIndex < row && colIndex >= 0 && colIndex + 1 < col
                        && grid[rowIndex][colIndex + 1] == 1) {
                    count--;
                    grid[rowIndex][colIndex + 1] = 2;
                    queue.offer(new int[] { rowIndex, colIndex + 1 });
                }
            }
            minute++;
        }
        return count == 0 ? minute : -1;
    }

    /**
     * 课程表
     * 这是递归写法
     */
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        // 0 未搜索 1 已搜索 2 搜索中
        int[] visited = new int[numCourses];
        Map<Integer, List<Integer>> map = new HashMap<>();
        for (int[] prerequisity : prerequisites) {
            map.putIfAbsent(prerequisity[0], new ArrayList<>());
            map.get(prerequisity[0]).add(prerequisity[1]);
        }
        for (Integer course : map.keySet()) {
            if (!dfs(map, course, visited)) {
                return false;
            }
        }
        return true;
    }

    private boolean dfs(Map<Integer, List<Integer>> map, int course, int[] visited) {
        if (visited[course] == 2) {
            return false;
        }
        if (visited[course] == 0) {
            visited[course] = 2;
            for (Integer preCourse : map.getOrDefault(course, new ArrayList<>())) {
                if (!dfs(map, preCourse, visited)) {
                    return false;
                }
            }
        }
        visited[course] = 1;
        return true;
    }

    // 出入度写法
    private boolean canFinishTwoEx(int numCourses, int[][] prerequisites) {
        Map<Integer, List<Integer>> map = new HashMap<>();
        int[] inDegree = new int[numCourses];
        for (int[] prerequisity : prerequisites) {
            map.putIfAbsent(prerequisity[1], new ArrayList<>());
            map.get(prerequisity[1]).add(prerequisity[0]);
            inDegree[prerequisity[0]]++;
        }
        int count = 0;
        Deque<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < numCourses; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }
        while (!queue.isEmpty()) {
            int course = queue.poll();
            count++;
            for (Integer preCourse : map.getOrDefault(course, new ArrayList<>())) {
                inDegree[preCourse]--;
                if (inDegree[preCourse] == 0) {
                    queue.offer(preCourse);
                }
            }
        }
        return count == numCourses;
    }

    /**
     * 实现Trie（前缀树）
     */
    class Trie {
        public class Node {
            char ch;
            Map<Character, Node> next = new HashMap<>();
            boolean isEndOfWord = false;

            public Node() {

            }

            public Node(char ch) {
                this.ch = ch;
            }
        }

        private Node root = new Node();

        public Trie() {

        }

        public void insert(String word) {
            int len = word.length();
            Node node = root;
            for (int i = 0; i < len; i++) {
                char ch = word.charAt(i);
                Node nextNode = node.next.getOrDefault(ch, new Node(ch));
                node.next.putIfAbsent(ch, nextNode);
                node = nextNode;
            }
            node.isEndOfWord = true;
        }

        public boolean search(String word) {
            int len = word.length();
            Node node = root;
            for (int i = 0; i < len; i++) {
                char ch = word.charAt(i);
                Node nextNode = node.next.get(ch);
                if (nextNode == null) {
                    return false;
                }
                node = nextNode;
            }
            return node.isEndOfWord;
        }

        public boolean startsWith(String prefix) {
            int len = prefix.length();
            Node node = root;
            for (int i = 0; i < len; i++) {
                char ch = prefix.charAt(i);
                Node nextNode = node.next.get(ch);
                if (nextNode == null) {
                    return false;
                }
                node = nextNode;
            }
            return true;
        }
    }

    /**
     * 全排列
     */
    public List<List<Integer>> permuteTwoEx(int[] nums) {
        boolean[] used = new boolean[nums.length];
        List<List<Integer>> res = new ArrayList<>();
        dfs(nums, used, res, new ArrayList<>());
        return res;
    }

    private void dfs(int[] nums, boolean[] used, List<List<Integer>> res, List<Integer> list) {
        if (list.size() == nums.length) {
            res.add(new ArrayList<>(list));
        }
        for (int i = 0; i < nums.length; i++) {
            if (!used[i]) {
                list.add(nums[i]);
                used[i] = true;
                dfs(nums, used, res, list);
                list.remove(list.size() - 1);
                used[i] = false;
            }
        }
    }

    /**
     * 子集
     */
    public List<List<Integer>> subsetsTwoEx(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        dfs(nums, res, new ArrayList<>(), 0);
        return res;
    }

    private void dfs(int[] nums, List<List<Integer>> res, List<Integer> list, int index) {
        if (index >= nums.length) {
            res.add(new ArrayList<>(list));
            return;
        }
        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                dfs(nums, res, list, index + 1);
            } else {
                list.add(nums[index]);
                dfs(nums, res, list, index + 1);
                list.remove(list.size() - 1);
            }
        }
    }

    /**
     * 电话号码的字母组合
     */
    public List<String> letterCombinationsTwoEx(String digits) {
        List<String> list = new ArrayList<>();
        if (digits.isBlank()) {
            return list;
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
        dfs(digits, map, list, new StringBuilder(), 0);
        return list;
    }

    private void dfs(String digits, char[][] map, List<String> res, StringBuilder sb, int index) {
        if (index >= digits.length()) {
            res.add(sb.toString());
            return;
        }
        char digit = digits.charAt(index);
        for (char ch : map[digit - '0']) {
            sb.append(ch);
            dfs(digits, map, res, sb, index + 1);
            sb.setLength(sb.length() - 1);
        }
    }

    /**
     * 组合总和
     * 没做出来
     * 之前做出来了，而且发现了之前的做法里排序的预处理不是必要的
     */
    public List<List<Integer>> combinationSumTwoEx(int[] candidates, int target) {
        List<List<Integer>> res = new ArrayList<>();
        dfs(candidates, target, res, new ArrayList<>(), 0);
        return res;
    }

    private void dfs(int[] candidates, int target, List<List<Integer>> res, List<Integer> list, int index) {
        if (index >= candidates.length || target < 0) {
            return;
        }
        if (target == 0) {
            res.add(new ArrayList<>(list));
            return;
        }
        for (int i = index; i < candidates.length; i++) {
            if (target >= candidates[i]) {
                list.add(candidates[i]);
                dfs(candidates, target - candidates[i], res, list, i);
                list.remove(list.size() - 1);
            }
        }
    }

    /**
     * 括号生成
     */
    public List<String> generateParenthesis(int n) {
        List<String> list = new ArrayList<>();
        dfs(list, n, 0, 0, new StringBuilder());
        return list;
    }

    private void dfs(List<String> res, int n, int leftCount, int rightCount, StringBuilder sb) {
        if (leftCount < rightCount || leftCount > n || rightCount > n) {
            return;
        }
        if (leftCount == n && rightCount == n) {
            res.add(sb.toString());
            return;
        }
        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                sb.append('(');
                dfs(res, n, leftCount + 1, rightCount, sb);
                sb.setLength(sb.length() - 1);
            } else {
                sb.append(')');
                dfs(res, n, leftCount, rightCount + 1, sb);
                sb.setLength(sb.length() - 1);
            }
        }
    }

    /**
     * 单词搜索
     */
    public boolean exist(char[][] board, String word) {
        if (word == null) {
            return false;
        }
        int row = board.length;
        int col = board[0].length;
        char ch = word.charAt(0);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (board[i][j] == ch) {
                    if (dfs(board, word, i, j, 0, new boolean[row][col])) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean dfs(char[][] board, String word, int i, int j, int index, boolean[][] visited) {
        int row = board.length;
        int col = board[0].length;
        if (index == word.length()) {
            return true;
        }
        if (i < 0 || i >= row || j < 0 || j >= col) {
            return false;
        }
        if (board[i][j] == word.charAt(index) && !visited[i][j]) {
            index++;
            visited[i][j] = true;
            boolean res = dfs(board, word, i - 1, j, index, visited)
                    || dfs(board, word, i + 1, j, index, visited)
                    || dfs(board, word, i, j - 1, index, visited)
                    || dfs(board, word, i, j + 1, index, visited);
            visited[i][j] = false;
            return res;
        } else {
            return false;
        }
    }

    /**
     * 分割回文串
     * 没做出来
     */
    public List<List<String>> partition(String s) {
        List<List<String>> res = new ArrayList<>();
        dfs(s, res, new ArrayList<>(), 0);
        return res;
    }

    private void dfs(String s, List<List<String>> res, List<String> list, int start) {
        if (start == s.length()) {
            res.add(new ArrayList<>(list));
            return;
        }
        for (int i = start; i < s.length(); i++) {
            if (valid(s, start, i)) {
                list.add(s.substring(start, i + 1));
                dfs(s, res, list, i + 1);
                list.remove(list.size() - 1);
            }
        }
    }

    private boolean valid(String s, int start, int end) {
        int lt = start;
        int rt = end;
        while (rt > lt) {
            if (s.charAt(rt) != s.charAt(lt)) {
                return false;
            }
            rt--;
            lt++;
        }
        return true;
    }

    public static void main(String[] args) {
        leetcodeExercise l = new leetcodeExercise();
        System.out.println(l.multiply("999999", "999999"));
    }

    /**
     * 快排
     */
    public void quickSort(int[] nums) {
        quickSort(nums, 0, nums.length - 1);
    }

    private void quickSort(int[] nums, int start, int end) {
        if (end <= start) {
            return;
        }
        int pivot = partition(nums, start, end);
        quickSort(nums, start, pivot - 1);
        quickSort(nums, pivot + 1, end);
    }

    private int partition(int[] nums, int start, int end) {
        int pivot = end;
        int slow = start;
        int fast = start;
        while (fast < end) {
            if (nums[fast] <= nums[pivot]) {
                swap(nums, slow, fast);
                slow++;
            }
            fast++;
        }
        swap(nums, slow, pivot);
        return slow;
    }

    private void swap(int[] nums, int a, int b) {
        int t = nums[a];
        nums[a] = nums[b];
        nums[b] = t;
    }

    /**
     * 归并排序
     */
    public void mergeSort(int[] nums) {
        spilt(nums, 0, nums.length - 1);
    }

    private void spilt(int[] nums, int start, int end) {
        if (end == start) {
            return;
        }
        int mid = (start + end) / 2;
        spilt(nums, start, mid);
        spilt(nums, mid + 1, end);
        merge(nums, start, mid, end);
    }

    private void merge(int[] nums, int start, int mid, int end) {
        int lt = start;
        int rt = mid + 1;
        int[] t = new int[end - start + 1];
        int index = 0;
        while (lt < mid + 1 && rt < end + 1) {
            if (nums[lt] < nums[rt]) {
                t[index] = nums[lt];
                lt++;
            } else {
                t[index] = nums[rt];
                rt++;
            }
            index++;
        }
        int rest = lt == mid + 1 ? rt : lt;
        for (int i = index; i < t.length; i++) {
            t[i] = nums[rest++];
        }
        index = 0;
        for (int i = start; i <= end; i++, index++) {
            nums[i] = t[index];
        }
    }

    /**
     * N皇后
     */
    public List<List<String>> solveNQueens(int n) {
        char[][] board = new char[n][n];
        for (char[] array : board) {
            Arrays.fill(array, '.');
        }
        List<List<String>> res = new ArrayList<>();
        dfs(res, board, 0);
        return res;
    }

    private void dfs(List<List<String>> res, char[][] board, int rowIndex) {
        int row = board.length;
        if (rowIndex == row) {
            List<String> list = new ArrayList<>();
            StringBuilder sb = new StringBuilder();
            for (char[] line : board) {
                sb.setLength(0);
                for (char pos : line) {
                    sb.append(pos);
                }
                list.add(sb.toString());
            }
            res.add(list);
            return;
        }
        for (int i = 0; i < row; i++) {
            if (valid(board, rowIndex, i)) {
                board[rowIndex][i] = 'Q';
                dfs(res, board, rowIndex + 1);
                board[rowIndex][i] = '.';
            }
        }
    }

    private boolean valid(char[][] board, int row, int col) {
        int n = board.length;
        // 检查列
        int rowIndex = row;
        int colIndex = col;
        while (rowIndex >= 0) {
            if (board[rowIndex][col] == 'Q') {
                return false;
            }
            rowIndex--;
        }
        // 检查主对角线
        rowIndex = row;
        colIndex = col;
        while (rowIndex >= 0 && colIndex >= 0) {
            if (board[rowIndex][colIndex] == 'Q') {
                return false;
            }
            rowIndex--;
            colIndex--;
        }
        // 检查副对角线
        rowIndex = row;
        colIndex = col;
        while (rowIndex >= 0 && colIndex >= 0 && colIndex < n) {
            if (board[rowIndex][colIndex] == 'Q') {
                return false;
            }
            rowIndex--;
            colIndex++;
        }
        return true;
    }

    /**
     * 腾讯面试题：优先级的括号匹配
     */
    public boolean priorityMatch(String s) {
        Deque<Character> stack = new ArrayDeque<>();
        Map<Character, Integer> priority = new HashMap<>();
        priority.put('(', 1);
        priority.put('[', 2);
        priority.put('{', 3);
        Map<Character, Character> match = new HashMap<>();
        match.put(')', '(');
        match.put(']', '[');
        match.put('}', '{');
        if (s == null || s.isEmpty()) {
            return true;
        }
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch == '(' || ch == '[' || ch == '{') {
                if (!stack.isEmpty()) {
                    if (priority.get(stack.peek()) < priority.get(ch)) {
                        return false;
                    }
                }
                stack.push(s.charAt(i));
            } else {
                if (stack.peek() == match.get(ch)) {
                    stack.pop();
                } else {
                    return false;
                }
            }
        }
        return stack.isEmpty();
    }

    /**
     * 腾讯面试题：24点游戏
     */
    public boolean judgePoint24(int[] cards) {
        List<Double> list = new ArrayList<>();
        for (double card : cards) {
            list.add(card);
        }
        return valid(list);
    }

    private boolean valid(List<Double> cards) {
        if (cards.size() == 1) {
            return Math.abs(cards.get(0) - 24) < 1e-6;
        }
        int size = cards.size();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                List<Double> list = new ArrayList<>();
                if (i == j) {
                    continue;
                }
                double num1 = cards.get(i);
                double num2 = cards.get(j);
                for (int k = 0; k < size; k++) {
                    if (k == i || k == j) {
                        continue;
                    }
                    list.add(cards.get(k));
                }
                for (int k = 0; k < 4; k++) {
                    if (k == 0) {
                        list.add(num1 + num2);
                    } else if (k == 1) {
                        list.add(num1 - num2);
                    } else if (k == 2) {
                        list.add(num1 * num2);
                    } else if (k == 3) {
                        if (num2 == 0) {
                            continue;
                        }
                        list.add(num1 / num2);
                    }
                    if (valid(list)) {
                        return true;
                    }
                    list.remove(list.size() - 1);
                }
            }
        }
        return false;
    }

    /**
     * 无重复字符的最长子串
     */
    public int lengthOfLongestSubstringTwoEx(String s) {
        Map<Character, Boolean> contain = new HashMap<>();
        int count = 0;
        int slow = 0;
        int fast = 0;
        while (fast < s.length()) {
            while (contain.getOrDefault(s.charAt(fast), false)) {
                contain.put(s.charAt(slow), false);
                slow++;
            }
            count = Math.max(count, fast - slow + 1);
            contain.put(s.charAt(fast), true);
            fast++;
        }
        return count;
    }

    /**
     * 腾讯面试题：俄罗斯套娃信封问题
     * 思路对了，但是超时
     * 但其实题解更巧妙
     * 题解也是先排序，不同的是宽度相同时按高度降序，然后寻找最长递增子序列
     * 超时的核心原因是：在寻找最长递增子序列LIS的时间复杂度太高，之前做LIS的时候就没注意时间复杂度
     */
    public int maxEnvelopesWithTimeExceed(int[][] envelopes) {
        if (envelopes == null) {
            return 0;
        }
        Arrays.sort(envelopes, (a, b) -> {
            return a[0] == b[0] ? a[1] - b[1] : a[0] - b[0];
        });
        int envelopeNum = envelopes.length;
        int max = 1;
        // 第i封信的状态 0放入 1不放入
        int[][] dp = new int[envelopeNum][2];
        dp[0][0] = 0;
        for (int i = 0; i < envelopeNum; i++) {
            dp[i][1] = 1;
        }
        for (int i = 1; i < envelopeNum; i++) {
            int[] cur = envelopes[i];
            dp[i][0] = Math.max(dp[i - 1][0], dp[i - 1][1]);
            for (int j = i - 1; j >= 0; j--) {
                if (dp[i][1] < dp[j][1] + 1 && cur[0] > envelopes[j][0] && cur[1] > envelopes[j][1]) {
                    dp[i][1] = dp[j][1] + 1;
                }
            }
            max = Math.max(dp[i][0], dp[i][1]);
        }
        return max;
    }

    public int maxEnvelopesWithTimeExceedTwo(int[][] envelopes) {
        if (envelopes == null) {
            return 0;
        }
        Arrays.sort(envelopes, (a, b) -> {
            return a[0] == b[0] ? b[1] - a[1] : a[0] - b[0];
        });
        int envelopeNum = envelopes.length;
        int max = 1;
        // 以第i封信结尾的最长递增子序列长度
        int[] dp = new int[envelopeNum];
        Arrays.fill(dp, 1);
        for (int i = 1; i < envelopeNum; i++) {
            for (int j = i; j >= 0; j--) {
                if (envelopes[i][0] > envelopes[j][0] && envelopes[i][1] > envelopes[j][1]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            max = Math.max(max, dp[i]);
        }
        return max;
    }

    /**
     * 最长递增子序列
     * 没做出来
     * 贪心+二分，这次不用dp了，dp解法可以看上面
     */
    public int lengthOfLIS(int[] nums) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            if (list.isEmpty()) {
                list.add(nums[i]);
            } else if (list.get(list.size() - 1) < nums[i]) {
                list.add(nums[i]);
            } else {
                int index = getLocation(list, nums[i]);
                list.set(index, nums[i]);
            }
        }
        return list.size();
    }

    private int getLocation(List<Integer> list, int val) {
        int lt = 0;
        int rt = list.size() - 1;
        while (rt > lt) {
            int mid = (lt + rt) / 2;
            if (list.get(mid) > val) {
                rt = mid - 1;
            } else if (list.get(mid) < val) {
                lt = mid + 1;
            } else {
                return mid;
            }
        }
        return lt;
    }

    /**
     * 搜索插入位置
     */
    public int searchInsert(int[] nums, int target) {
        int lt = 0;
        int rt = nums.length - 1;
        while (rt >= lt) {
            int mid = (rt + lt) / 2;
            if (nums[mid] > target) {
                rt = mid - 1;
            } else if (nums[mid] < target) {
                lt = mid + 1;
            } else {
                return mid;
            }
        }
        return lt;
    }

    /**
     * 比较版本号
     */
    public int compareVersion(String version1, String version2) {
        String[] version1Str = version1.split("\\.");
        String[] version2Str = version2.split("\\.");
        int len1 = version1Str.length;
        int len2 = version2Str.length;
        for (int i = 0; i < Math.min(len1, len2); i++) {
            String num1 = version1Str[i];
            String num2 = version2Str[i];
            if (Integer.valueOf(num1) > Integer.valueOf(num2)) {
                return 1;
            } else if (Integer.valueOf(num1) < Integer.valueOf(num2)) {
                return -1;
            }
        }
        if (len1 > len2) {
            int index = len2;
            while (index < len1) {
                if (Integer.valueOf(version1Str[index]) != 0) {
                    return 1;
                }
                index++;
            }
        } else if (len2 > len1) {
            int index = len1;
            while (index < len2) {
                if (Integer.valueOf(version2Str[index]) != 0) {
                    return -1;
                }
                index++;
            }
        }
        return 0;
    }

    /**
     * 复原ip地址
     * 二刷反而没做出来，这题的一个坑点是if (s.startsWith("0") && len > 1) {
     */
    public List<String> restoreIpAddresses(String s) {
        List<String> list = new ArrayList<>();
        dfs(list, new StringBuilder(), s, 0, 0);
        return list;
    }

    private void dfs(List<String> res, StringBuilder sb, String s, int start, int count) {
        if (count == 4) {
            if (start == s.length()) {
                res.add(sb.substring(0, sb.length() - 1));
            }
            return;
        }
        for (int end = start; end < s.length() && end < start + 3; end++) {
            String segement = s.substring(start, end + 1);
            if (isValidIp(segement)) {
                int len = sb.length();
                sb.append(segement).append('.');
                dfs(res, sb, s, end + 1, count + 1);
                sb.setLength(len);
            }
        }
    }

    private boolean isValidIp(String s) {
        int len = s.length();
        if (len > 3) {
            return false;
        }
        if (s.startsWith("0") && len > 1) {
            return false;
        }
        int segment = Integer.parseInt(s);
        return segment >= 0 && segment <= 255;
    }

    /**
     * 阻塞队列
     */
    public class MyBlockingQueue {
        class Node {
            int val;
            Node next;
            Node prev;

            public Node(int val) {
                this.val = val;
            }
        }

        int capacity = 0;
        Node head;
        Node tail;
        int size;
        ReentrantLock lock;
        Condition notEmpty;
        Condition notFull;

        public MyBlockingQueue(int capacity) {
            this.capacity = capacity;
            this.head = new Node(0);
            this.tail = new Node(0);
            head.next = tail;
            tail.prev = head;
            this.size = 0;
            this.lock = new ReentrantLock();
            this.notEmpty = lock.newCondition();
            this.notFull = lock.newCondition();
        }

        public boolean offer(int val) {
            lock.lock();
            try {
                while (size >= capacity) {
                    notFull.await();
                }
                Node last = tail.prev;
                Node node = new Node(val);
                last.next = node;
                node.prev = last;
                node.next = tail;
                tail.prev = node;
                size++;
                notEmpty.signal();
                return true;
            } catch (Exception e) {
                return false;
            } finally {
                lock.unlock();
            }
        }

        public int poll() {
            lock.lock();
            try {
                while (size <= 0) {
                    notEmpty.await();
                }
                Node node = head.next;
                head.next = node.next;
                node.next.prev = head;
                node.next = null;
                node.prev = null;
                notFull.signal();
                return node.val;
            } catch (Exception e) {
                throw new RuntimeException();
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * 原地去重
     */
    public int removeDuplicates(int[] nums) {
        if (nums == null || nums.length == 1) {
            return nums == null ? 0 : 1;
        }
        Arrays.sort(nums);
        int slow = 1;
        int fast = 1;
        int last = nums[0];
        while (true) {
            while (fast < nums.length && nums[fast] == last) {
                fast++;
            }
            if (fast != nums.length) {
                break;
            }
            last = nums[fast];
            swap(nums, fast, slow);
            slow++;
            fast++;
        }
        return slow;
    }

    /**
     * 手撕HashMap
     */
    class MyHashMap {
        class Entry {
            int key;
            int val;
            Entry next;
            Entry prev;

            public Entry(int key, int val) {
                this.key = key;
                this.val = val;
            }
        }

        class Bucket {
            Entry head;
            Entry tail;

            public Bucket() {
                this.head = new Entry(0, 0);
                this.tail = new Entry(0, 0);
                head.next = tail;
                tail.prev = head;
            }
        }

        private Bucket[] buckets;

        private int size;

        private int hash(int key) {
            return key % size;
        }

        public MyHashMap(int size) {
            this.size = size;
            this.buckets = new Bucket[size];
            for (int i = 0; i < size; i++) {
                buckets[i] = new Bucket();
            }
        }

        public void put(int key, int val) {
            int hash = hash(key);
            Bucket bucket = buckets[hash];
            Entry entry = bucket.head;
            while (entry != bucket.tail && entry.key != key) {
                entry = entry.next;
            }
            if (entry == bucket.tail) {
                // entry == bucket.tail
                Entry newEntry = new Entry(key, val);
                Entry oldHead = bucket.head.next;
                newEntry.next = oldHead;
                oldHead.prev = newEntry;
                newEntry.prev = bucket.head;
                bucket.head.next = newEntry;
            } else {
                // entry.key = key
                if (entry.val != val) {
                    entry.val = val;
                }
            }
        }

        public Integer get(int key) {
            int hash = hash(key);
            Bucket bucket = buckets[hash];
            Entry entry = bucket.head;
            while (entry != bucket.tail && entry.key != key) {
                entry = entry.next;
            }
            return entry == bucket.tail ? null : entry.val;
        }
    }

    /**
     * 用最少数量的箭引爆气球
     * 没做出来，后面才想起来怎么做这题才写出来
     */
    public int findMinArrowShots(int[][] points) {
        // [10,16],[2,8],[1,6],[7,12]
        // [1,6] [2,8] [7,12] [10,16]
        // [1,12]
        Arrays.sort(points, (a, b) -> {
            return Integer.compare(a[0], b[0]) == 0 ? Integer.compare(a[1], b[1]) : Integer.compare(a[0], b[0]);
        });
        int count = 1;
        long rt = points[0][1];
        for (int i = 1; i < points.length; i++) {
            int[] cur = points[i];
            if (cur[0] <= rt) {
                rt = Math.min(cur[1], rt);
            } else {
                rt = cur[1];
                count++;
            }
        }
        return count;
    }

    /**
     * 最长递增子序列
     * 贪心+二分又没做出来
     * 核心思想是：
     * 维护一个list，只允许比队尾更大的元素插入进去
     * 否则就说明当前元素比队尾元素要小，那么我们二分查找这个最小元素应该插入的位置，替换它
     * 这样之所以是对的，是因为替换不会使list的size减小，只有可能会使list的size增大，到最后
     * 返回list.size()就是最终答案
     */
    public int lengthOfLISTwoEx(int[] nums) {
        // 10,9,2,5,3,7,101,18
        List<Integer> list = new ArrayList<>();
        for (int num : nums) {
            if (list.isEmpty()) {
                list.add(num);
            } else if (list.get(list.size() - 1) < num) {
                list.add(num);
            } else {
                // !list.isEmpty() && list.get(list.size() - 1) >= num
                int index = binarySearch(list, num);
                list.set(index, num);
            }
        }
        return list.size();
    }

    private int binarySearch(List<Integer> list, int num) {
        // 1 4 6 7 5
        int lt = 0, rt = list.size() - 1;
        while (rt >= lt) {
            int mid = (rt + lt) >> 1;
            if (list.get(mid) > num) {
                rt = mid - 1;
            } else if (list.get(mid) < num) {
                lt = mid + 1;
            } else {
                return mid;
            }
        }
        return lt;
    }

    /**
     * 搜索二维矩阵
     * 把右上角的节点视作二叉树
     */
    public boolean searchMatrixTwoEx(int[][] matrix, int target) {
        int row = matrix.length;
        int col = matrix[0].length;
        int i = 0, j = col - 1;
        while (i >= 0 && i < row && j >= 0 && j < col) {
            if (matrix[i][j] > target) {
                j--;
            } else if (matrix[i][j] < target) {
                i++;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * 在排序数组中查找元素的第一个和最后一个出现的位置
     * 先用二分法找到位置，然后往前找到头一个，往后找到末一个
     */
    public int[] searchRange(int[] nums, int target) {
        int[] res = new int[] { -1, -1 };
        if (nums == null || nums.length == 0) {
            return res;
        }
        // 5,7,7,8,8,10
        int lt = 0;
        int rt = nums.length - 1;
        while (rt >= lt) {
            int mid = (lt + rt) >> 1;
            if (target < nums[mid]) {
                rt = mid - 1;
            } else if (target > nums[mid]) {
                lt = mid + 1;
            } else {
                lt = mid;
                break;
            }
        }
        if (lt >= nums.length || nums[lt] != target) {
            return res;
        }
        int last = lt, first = lt;
        while (last < nums.length && nums[last] == nums[lt]) {
            last++;
        }
        while (first >= 0 && nums[first] == nums[lt]) {
            first--;
        }
        res[0] = first + 1;
        res[1] = last - 1;
        return res;
    }

    /**
     * 搜索旋转排序数组
     * 思路是一样的，但是没做出来
     * 最关键的错是错在找转折点那里
     * 这个找转折点的方法很巧妙
     * 转折点把左右两端都分为升序的数组，是利用了这个性质让左右指针往转折点靠
     */
    public int search(int[] nums, int target) {
        // 4,6,7,0,1,2, t=0
        // 先找到谷底元素
        int leftPartTail = findTurnPoint(nums);
        int index1 = binarySeach(nums, 0, leftPartTail, target);
        if (index1 == -1) {
            int index2 = binarySeach(nums, leftPartTail + 1, nums.length - 1, target);
            return index2;
        }
        return index1;
    }

    private int binarySeach(int[] nums, int start, int end, int target) {
        int lt = start;
        int rt = end;
        while (rt >= lt) {
            int mid = (lt + rt) >> 1;
            if (nums[mid] > target) {
                rt = mid - 1;
            } else if (nums[mid] < target) {
                lt = mid + 1;
            } else {
                return mid;
            }
        }
        return -1;
    }

    private int findTurnPoint(int[] nums) {
        // 4,6,7,0,1,2
        // 3 5 1
        int lt = 0;
        int rt = nums.length - 1;
        while (rt >= lt) {
            int mid = (lt + rt) / 2;
            if (nums[mid] > nums[lt]) {
                // 左部分是有序的，则左起点往左拉
                lt = mid;
            } else if (nums[mid] < nums[lt]) {
                // 右部分是有序的，则右终点往右拉
                rt = mid;
            } else {
                // nums[mid] == nums[lt]，说明到达了交界点
                break;
            }
        }
        return lt;
    }

    /**
     * 前K个高频元素
     */
    public int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> frequence = new HashMap<>();
        for (int num : nums) {
            frequence.put(num, frequence.getOrDefault(num, 0) + 1);
        }
        Heap heap = new Heap();
        for (Map.Entry<Integer, Integer> entry : frequence.entrySet()) {
            heap.offer(entry.getKey(), entry.getValue());
        }
        int[] res = new int[k];
        for (int i = 0; i < k; i++) {
            res[i] = heap.poll()[0];
        }
        return res;
    }

    public class Heap {

        List<Node> list;

        public class Node {
            int key;
            int val;

            public Node(int key, int val) {
                this.key = key;
                this.val = val;
            }
        }

        public Heap() {
            this.list = new ArrayList<>();
        }

        public void offer(int key, int val) {
            Node node = new Node(key, val);
            list.add(node);
            upload(list.size() - 1);
        }

        private void upload(int index) {
            // 0 1 2 3 4 5
            int parent = (index - 1) / 2;
            if (parent >= 0) {
                if (list.get(parent).val < list.get(index).val) {
                    swap(parent, index);
                    upload(parent);
                }
            }
        }

        public int[] poll() {
            swap(0, list.size() - 1);
            Node node = list.remove(list.size() - 1);
            heapify(0);
            return new int[] { node.key, node.val };
        }

        private void heapify(int index) {
            int lt = index * 2 + 1;
            int rt = index * 2 + 2;
            int largest = index;
            int size = list.size();
            if (lt < size && list.get(largest).val < list.get(lt).val) {
                largest = lt;
            }
            if (rt < size && list.get(largest).val < list.get(rt).val) {
                largest = rt;
            }
            if (largest != index) {
                swap(index, largest);
                heapify(largest);
            }
        }

        private void swap(int index1, int index2) {
            Node n1 = list.get(index1);
            Node n2 = list.get(index2);
            int val = n1.val, key = n1.key;
            n1.val = n2.val;
            n1.key = n2.key;
            n2.key = key;
            n2.val = val;
        }
    }

    /**
     * 搜索旋转排序数组中的最小值
     */
    public int findMin(int[] nums) {
        // 3 4 5 1 2
        // l r
        // 3 4 5 1 2
        int lt = 0;
        int rt = nums.length - 1;
        if (nums[lt] < nums[rt] || nums.length == 1) {
            return nums[lt];
        }
        while (rt >= lt) {
            int mid = (lt + rt) / 2;
            if (nums[mid] > nums[lt]) {
                // 左部分是有序的，则左起点往左拉
                lt = mid;
            } else if (nums[mid] < nums[lt]) {
                // 右部分是有序的，则右终点往右拉
                rt = mid;
            } else {
                // nums[mid] == nums[lt]，说明到达了交界点
                break;
            }
        }
        return nums[lt + 1];
    }

    /**
     * 大数乘法
     * 第一次做成功ac，就是要注意最后要处理前导0
     */
    public String multiply(String num1, String num2) {
        int len1 = num1.length();
        int len2 = num2.length();
        // 100 100 10000
        // 1237
        // 456
        Deque<String> partSum = new ArrayDeque<>();
        StringBuilder sb = new StringBuilder();
        for (int i = len1 - 1; i >= 0; i--) {
            sb.setLength(0);
            int digit1 = num1.charAt(i) - '0';
            int carryout = 0;
            for (int j = len2 - 1; j >= 0; j--) {
                int digit2 = num2.charAt(j) - '0';
                int sum = digit1 * digit2 + carryout;
                carryout = sum / 10;
                sum %= 10;
                sb.append(sum);
            }
            if (carryout != 0) {
                sb.append(carryout);
            }
            // 处理位的权重个十百千万
            for (int count = 0; count < (len1 - 1) - i; count++) {
                sb.insert(0, 0);
            }
            partSum.offer(sb.reverse().toString());
        }
        // 模拟加法
        while (partSum.size() != 1) {
            String part1 = partSum.poll();
            String part2 = partSum.poll();
            if (part2 == null) {
                break;
            }
            partSum.offer(add(part1, part2));
        }
        String result = partSum.poll();
        // 处理前导零
        if (result == null)
            return "0";
        int start = 0;
        while (start < result.length() && result.charAt(start) == '0') {
            start++;
        }
        return start == result.length() ? "0" : result.substring(start);
    }

    public String add(String num1, String num2) {
        int l1 = num1.length();
        int l2 = num2.length();
        StringBuilder sb = new StringBuilder();
        // 1 2 3 4
        // 0 5 6 7
        int carryout = 0;
        int i = 0;
        while (carryout != 0 || i < l1 || i < l2) {
            int digit1 = (l1 - 1 - i < 0) ? 0 : num1.charAt(l1 - 1 - i) - '0';
            int digit2 = (l2 - 1 - i < 0) ? 0 : num2.charAt(l2 - 1 - i) - '0';
            i++;
            int sum = digit1 + digit2 + carryout;
            carryout = sum / 10;
            sum %= 10;
            sb.append(sum);
        }
        return sb.reverse().toString();
    }

    /**
     * 大数除法
     */
    public static String divide(String bigNumber, int divisor) {
        boolean isNegative = false;
        if (bigNumber.charAt(0) == '-') {
            isNegative = true;
            bigNumber = bigNumber.substring(1);
        }
        StringBuilder integerRes = new StringBuilder();
        // 114 / 51
        int remainder = 0;
        int index = 0;
        while (index < bigNumber.length()) {
            int digit = bigNumber.charAt(index) - '0';
            // 当前被除数
            int currentDividend = remainder * 10 + digit;
            // 商
            int quotient = currentDividend / divisor;
            // 余数
            remainder = currentDividend % divisor;
            if (integerRes.length() > 0 || quotient > 0) { // 避免前导零
                integerRes.append(quotient);
            }
            index++;
        }
        if (integerRes.length() == 0) {
            integerRes.append('0');
        }
        StringBuilder decimalRes = new StringBuilder(".00");
        if (remainder > 0) {
            decimalRes = new StringBuilder(".");
            for (int i = 0; i < 2; i++) { // 保留两位小数
                remainder *= 10;
                int nextDigit = remainder / divisor;
                remainder %= divisor;
                decimalRes.append(nextDigit);
            }
        }
        // 合并结果
        String result = integerRes.toString() + decimalRes.toString();
        // 如果是负数，则添加负号
        if (isNegative) {
            result = "-" + result;
        }
        return result;
    }

    /**
     * 寻找两个正序数组的中位数
     * 时间复杂度并不优秀，O(m + n)的复杂度
     */
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        // 1 3 5 8 9
        // 2 4 7
        int l1 = nums1.length;
        int l2 = nums2.length;
        int p1 = 0, p2 = 0;
        int count = 0;
        int last = 0;
        int target = (l1 + l2) % 2 == 0 ? (l1 + l2) / 2 : (l1 + l2 + 1) / 2;
        while (p1 < l1 || p2 < l2) {
            int num1 = p1 < l1 ? nums1[p1] : Integer.MAX_VALUE;
            int num2 = p2 < l2 ? nums2[p2] : Integer.MAX_VALUE;
            if (num1 < num2) {
                last = nums1[p1];
                p1++;
            } else {
                last = nums2[p2];
                p2++;
            }
            count++;
            if (count == target) {
                if ((l1 + l2) % 2 == 0) {
                    num1 = p1 < l1 ? nums1[p1] : Integer.MAX_VALUE;
                    num2 = p2 < l2 ? nums2[p2] : Integer.MAX_VALUE;
                    return (last + Math.min(num1, num2)) / 2.0;
                } else {
                    return last;
                }
            }
        }
        return -1;
    }

    /**
     * 寻找两个正序数组的中位数
     * 做不出来，这个真是神仙解法
     * 没做出来
     * O(log(min(n, m)))
     */
    public double findMedianSortedArraysTwoEx(int[] nums1, int[] nums2) {
        // 2 4 7
        // 1 3 5 8 9
        int l1 = nums1.length, l2 = nums2.length;
        if (l1 > l2) {
            return findMedianSortedArrays(nums2, nums1);
        }
        // nums1元素个数一定小于等于nums2
        int count = (l1 + l2 + 1) / 2;
        // 从nums1取的元素的个数：[lt, rt];
        int lt = 0, rt = l1;
        while (lt < rt) {
            int take1 = (lt + rt) / 2;
            int take2 = count - take1 - 1;
            if (nums1[take1] >= nums2[take2]) {
                rt = take1;
            } else {
                lt = take1 + 1;
            }
        }
        int count1 = lt, count2 = count - lt;
        if ((l1 + l2) % 2 == 1) {
            int leftVal = Math.max(
                    (count1 == 0 ? Integer.MIN_VALUE : nums1[count1 - 1]),
                    (count2 == 0 ? Integer.MIN_VALUE : nums2[count2 - 1]));
            return leftVal;
        } else {
            int leftVal = Math.max(
                    (count1 == 0 ? Integer.MIN_VALUE : nums1[count1 - 1]),
                    (count2 == 0 ? Integer.MIN_VALUE : nums2[count2 - 1]));
            int rightVal = Math.min(
                    (count1 == l1 ? Integer.MAX_VALUE : nums1[count1]),
                    (count2 == l2 ? Integer.MAX_VALUE : nums2[count2]));
            return (leftVal + rightVal) / 2.0;
        }
    }

    /**
     * 最小路径和
     */
    public int minPathSum(int[][] grid) {
        int row = grid.length;
        int col = grid[0].length;
        int[][] dp = new int[row][col];
        dp[0][0] = grid[0][0];
        for (int i = 1; i < row; i++) {
            dp[i][0] = dp[i - 1][0] + grid[i][0];
        }
        for (int i = 1; i < col; i++) {
            dp[0][i] = dp[0][i - 1] + grid[0][i];
        }
        for (int i = 1; i < row; i++) {
            for (int j = 1; j < col; j++) {
                dp[i][j] = Math.min(dp[i - 1][j], dp[i][j - 1]) + grid[i][j];
            }
        }
        return dp[row - 1][col - 1];
    }

    /**
     * 最长回文子串
     * 没做出来，核心是没有想清楚遍历顺序，要根据长度来遍历，这样才能保证递推
     */
    public String longestPalindrome(String s) {
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
        return s.substring(minStartPoint, minStartPoint + maxLen);
    }

    /**
     * 反转链表II
     */
    public ListNode reverseBetween(ListNode head, int left, int right) {
        ListNode dummy = new ListNode(0, head);
        int index = 1;
        ListNode last = dummy;
        ListNode node = head;
        while (node != null && index < left) {
            last = node;
            node = node.next;
            index++;
        }
        ListNode prevGourpStart = last;
        last = null;
        ListNode curGroupStart = node;
        // inedx == left
        while (node != null && index < right) {
            ListNode next = node.next;
            if (last != null) {
                node.next = last;
            }
            last = node;
            node = next;
            index++;
        }
        // index == right
        ListNode nextGourpStart = node.next;
        node.next = last;
        prevGourpStart.next = node;
        curGroupStart.next = nextGourpStart;
        return dummy.next;
    }

    /**
     * 字节面试题：n次幂
     * 核心是分治，难点是n为负数时return (1 / x) * myPow(1 / x, -(n + 1));来应对Integer.MIN_VALUE
     */
    public double myPow(double x, int n) {
        if (n >= 0) {
            if (n == 0) {
                return 1;
            }
            return n % 2 == 0 ? myPow(x * x, n / 2) : x * myPow(x * x, (n - 1) / 2);
        } else {
            return (1 / x) * myPow(1 / x, -(n + 1));
        }
    }

    /**
     * 有效的括号
     */
    public boolean isValid(String s) {
        Map<Character, Character> map = new HashMap<>();
        map.put(')', '(');
        map.put('}', '{');
        map.put(']', '[');
        Deque<Character> stack = new ArrayDeque<>();
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch == '}' || ch == ')' || ch == ']') {
                if (stack.isEmpty() || !stack.peek().equals(map.get(ch))) {
                    return false;
                }
                stack.pop();
            } else {
                stack.push(ch);
            }
        }
        return stack.isEmpty();
    }

    /**
     * 最小栈
     */
    class MinStack {

        Deque<Integer> stack;

        Deque<Integer> minStack;

        public MinStack() {
            stack = new ArrayDeque<>();
            minStack = new ArrayDeque<>();
        }

        public void push(int val) {
            stack.push(val);
            if (minStack.isEmpty() || minStack.peek() >= val) {
                minStack.push(val);
            }
        }

        public void pop() {
            int val = stack.pop();
            if (!minStack.isEmpty() && val == minStack.peek()) {
                minStack.pop();
            }
        }

        public int top() {
            // 获取但不删除
            return stack.peek();
        }

        public int getMin() {
            // 获取但不删除
            return minStack.peek();
        }
    }

    /**
     * 字符串解码
     */
    public String decodeString(String s) {
        StringBuilder res = new StringBuilder();
        Deque<Character> stack = new ArrayDeque<>();
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch == ']') {
                res.setLength(0);
                while (!stack.isEmpty() && stack.peek() != '[') {
                    res.append(stack.pop());
                }
                stack.pop();
                String part = res.reverse().toString();
                res.setLength(0);
                while (!stack.isEmpty() && stack.peek() >= '0' && stack.peek() <= '9') {
                    res.append(stack.pop());
                }
                int time = Integer.parseInt(res.reverse().toString());
                res.setLength(0);
                for (int count = 0; count < time; count++) {
                    res.append(part);
                }
                for (int j = 0; j < res.length(); j++) {
                    stack.push(res.charAt(j));
                }
            } else if (ch == '[') {
                if (!(stack.peek() >= '0' && stack.peek() <= '9')) {
                    stack.push('1');
                }
                stack.push(ch);
            } else {
                stack.push(ch);
            }
        }
        res.setLength(0);
        while (!stack.isEmpty()) {
            res.append(stack.pop());
        }
        return res.reverse().toString();
    }

    /**
     * 每日温度
     */
    public int[] dailyTemperatures(int[] temperatures) {
        int[] res = new int[temperatures.length];
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 0; i < temperatures.length; i++) {
            while (!stack.isEmpty() && temperatures[stack.peek()] < temperatures[i]) {
                int index = stack.pop();
                res[index] = i - index;
            }
            stack.push(i);
        }
        return res;
    }

    /**
     * 柱状图中最大的矩形
     */
    public int largestRectangleArea(int[] heights) {
        Deque<Integer> stack = new ArrayDeque<>();
        int len = heights.length;
        int[] area = new int[len];
        for (int i = 0; i < len; i++) {
            while (!stack.isEmpty() && heights[stack.peek()] > heights[i]) {
                int j = stack.pop();
                int w = i - j;
                area[j] = w * heights[j];
            }
            stack.push(i);
        }
        while (!stack.isEmpty()) {
            int j = stack.pop();
            int w = len - j;
            area[j] = w * heights[j];
        }
        int res = Integer.MIN_VALUE;
        for (int i = len - 1; i >= 0; i--) {
            while (!stack.isEmpty() && heights[stack.peek()] > heights[i]) {
                int j = stack.pop();
                int w = j - i - 1;
                area[j] += heights[j] * w;
                res = Math.max(res, area[j]);
            }
            stack.push(i);
        }
        while (!stack.isEmpty()) {
            int j = stack.pop();
            int w = j;
            area[j] += heights[j] * w;
            res = Math.max(res, area[j]);
        }
        return res;
    }

    /**
     * 数组中第K大的元素
     */
    public int findKthLargest(int[] nums, int k) {
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>((a, b) -> a - b);
        for (int i = 0; i < nums.length; i++) {
            int num = nums[i];
            if (priorityQueue.size() < k) {
                priorityQueue.offer(num);
            } else {
                if (priorityQueue.peek() < num) {
                    priorityQueue.poll();
                    priorityQueue.offer(num);
                }
            }
        }
        return priorityQueue.poll();
    }

    /**
     * 数据流的中位数
     * 没做出来，addNum的思路是关键：
     * 对于一个有序的数据流
     * 用一个大顶堆记录前半部分
     * 小顶堆记录后半部分
     * 且始终保持大顶堆的数量在小顶堆数量和小顶堆数量+1之间
     * 以大顶堆堆顶作为当前中位数的参考标准，
     * 如果当前要插入的数num比大顶堆堆顶数top小，说明它属于有序数据流的前半部分，应该放入大顶堆
     * 反之放入小顶堆
     * 无论是放入大顶堆还是小顶堆后，都应该检查两堆的数量，使之符合大顶堆数量等于小顶堆或者大顶堆比小顶堆多一个元素
     */
    class MedianFinder {

        private PriorityQueue<Integer> minHeap;

        private PriorityQueue<Integer> maxHeap;

        public MedianFinder() {
            minHeap = new PriorityQueue<>((a, b) -> a - b);
            maxHeap = new PriorityQueue<>((a, b) -> b - a);
        }

        public void addNum(int num) {
            // 2 3 4
            // max: 2
            // min: 3 4
            if (!maxHeap.isEmpty() && maxHeap.peek() <= num) {
                minHeap.offer(num);
                if (maxHeap.size() < minHeap.size()) {
                    maxHeap.offer(minHeap.poll());
                }
            } else {
                maxHeap.offer(num);
                if (maxHeap.size() > minHeap.size() + 1) {
                    minHeap.offer(maxHeap.poll());
                }
            }
        }

        public double findMedian() {
            if ((minHeap.size() + maxHeap.size()) % 2 == 0) {
                return (minHeap.peek() + maxHeap.peek()) / 2.0;
            } else {
                return maxHeap.peek();
            }
        }
    }

    /**
     * 买卖股票的最佳时机
     */
    public int maxProfitTwoEx(int[] prices) {
        // dp[i][0]为第i天未持有股票的利润，dp[i][1]为持有股票的利润
        int[][] dp = new int[prices.length][2];
        dp[0][0] = 0;
        dp[0][1] = -prices[0];
        for (int i = 1; i < prices.length; i++) {
            dp[i][1] = Math.max(-prices[i], dp[i - 1][1]);
            dp[i][0] = Math.max(dp[i - 1][1] + prices[i], dp[i - 1][0]);
        }
        return Math.max(dp[prices.length - 1][0], dp[prices.length - 1][1]);
    }

    /**
     * 跳跃游戏
     */
    public boolean canJumpTwoEx(int[] nums) {
        int max = 0;
        for (int i = 0; i <= max; i++) {
            max = Math.max(max, nums[i] + i);
            if (max >= nums.length - 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * 跳跃游戏II
     */
    public int jumpTwoEx(int[] nums) {
        int[] dp = new int[nums.length];
        Arrays.fill(dp, nums.length);
        dp[0] = 0;
        for (int i = 0; i < nums.length; i++) {
            for (int j = 0; j <= nums[i] && i + j < nums.length; j++) {
                dp[j + i] = Math.min(dp[i] + 1, dp[j + i]);
            }
        }
        return dp[nums.length - 1];
    }

    /**
     * 划分字母区间
     */
    public List<Integer> partitionLabelsTwoEx(String s) {
        int[] last = new int[26];
        for (int i = 0; i < s.length(); i++) {
            last[s.charAt(i) - 'a'] = i;
        }
        List<Integer> list = new ArrayList<>();
        int groupStart = 0;
        int groupEnd = last[s.charAt(0) - 'a'];
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            groupEnd = Math.max(groupEnd, last[ch - 'a']);
            if (i == groupEnd) {
                list.add(groupEnd - groupStart + 1);
                groupStart = i + 1;
            }
        }
        return list;
    }

    /**
     * 爬楼梯
     */
    public int climbStairs(int n) {
        if (n < 2) {
            return 1;
        }
        int[] dp = new int[n + 1];
        dp[0] = 1;
        dp[1] = 1;
        dp[2] = 2;
        for (int i = 3; i < n + 1; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        return dp[n];
    }

    /**
     * 杨辉三角
     */
    public List<List<Integer>> generate(int numRows) {
        // 1
        // 1 1
        // 1 2 1
        List<List<Integer>> res = new ArrayList<>();
        res.add(List.of(1));
        for (int i = 1; i < numRows; i++) {
            List<Integer> list = new ArrayList<>();
            List<Integer> last = res.get(res.size() - 1);
            for (int j = 0; j < i + 1; j++) {
                list.add((j - 1 >= 0 ? last.get(j - 1) : 0) + (j < i ? last.get(j) : 0));
            }
            res.add(list);
        }
        return res;
    }

    /**
     * 打家劫舍
     */
    public int rob(int[] nums) {
        if (nums.length < 2) {
            return nums[0];
        }
        // dp[i] 为 只考虑到前i家为止，可以偷盗的最大金额
        int[] dp = new int[nums.length];
        dp[0] = nums[0];
        dp[1] = Math.max(nums[0], nums[1]);
        for (int i = 2; i < nums.length; i++) {
            dp[i] = Math.max(dp[i - 1], dp[i - 2] + nums[i]);
        }
        return dp[nums.length - 1];
    }

    /**
     * 完全平方数
     */
    public int numSquares(int n) {
        // 考虑到i为止的完全平方数和最少数量
        int[] dp = new int[n + 1];
        Arrays.fill(dp, 10000);
        dp[0] = 0;
        dp[1] = 1;
        for (int i = 2; i < n + 1; i++) {
            for (int j = 1; j * j <= i; j++) {
                dp[i] = Math.min(dp[i - j * j] + 1, dp[i]);
            }
        }
        return dp[n];
    }

    /**
     * 零钱兑换
     */
    public int coinChange(int[] coins, int amount) {
        // 考虑到i为止的完全平方数和最少数量
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1);
        dp[0] = 0;
        for (int i = 1; i < amount + 1; i++) {
            for (int j = 0; j < coins.length; j++) {
                if (i >= coins[j]) {
                    dp[i] = Math.min(dp[i - coins[j]] + 1, dp[i]);
                }
            }
        }
        return dp[amount] == amount + 1 ? -1 : dp[amount];
    }

    /**
     * 单词拆分
     * 超时了
     */
    public boolean wordBreakTimeExceed(String s, List<String> wordDict) {
        return dfs(s, 0, wordDict);
    }

    private boolean dfs(String s, int start, List<String> dict) {
        if (start == s.length()) {
            return true;
        }
        for (String word : dict) {
            if (s.substring(start, Math.min(start + word.length(), s.length())).equals(word)) {
                if (dfs(s, Math.min(start + word.length(), s.length()), dict)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 单词拆分
     */
    public boolean wordBreak(String s, List<String> wordDict) {
        // 考虑到前i个字符为止能否被拆分
        boolean[] dp = new boolean[s.length() + 1];
        dp[0] = true;
        for (int i = 1; i < s.length() + 1; i++) {
            // leetcode
            // 4
            for (String word : wordDict) {
                if (i - word.length() >= 0) {
                    dp[i] = dp[i - word.length()] && s.substring(i - word.length(), i).equals(word) || dp[i];
                    if (dp[i]) {
                        break;
                    }
                }
            }
        }
        return dp[s.length()];
    }

    /**
     * 乘积最大子数组
     * 二刷，做出来了
     */
    public int maxProduct(int[] nums) {
        // 考虑到第i项为止的最大最小乘积
        int[] maxProd = new int[nums.length];
        int[] minProd = new int[nums.length];
        maxProd[0] = nums[0];
        minProd[0] = nums[0];
        int max = nums[0];
        for (int i = 1; i < nums.length; i++) {
            maxProd[i] = Math.max(Math.max(maxProd[i - 1] * nums[i], minProd[i - 1] * nums[i]), nums[i]);
            minProd[i] = Math.min(Math.min(maxProd[i - 1] * nums[i], minProd[i - 1] * nums[i]), nums[i]);
            max = Math.max(max, maxProd[i]);
        }
        return max;
    }

    /**
     * 分割等和子集
     * 从这题可以看出来你基本上忘记了01背包和完全背包问题的区别
     * 没做出来
     * 如果要转为一维数组，
     * 第二层的循环我们需要从后往前遍历，
     * 因为如果我们从小到大更新 dp 值，那么在计算 dp[j] 值的时候，
     * dp[j−nums[i]] 已经是被更新过的状态，不再是上一行的 dp 值。
     */
    public boolean canPartition(int[] nums) {
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        if (sum % 2 == 1) {
            return false;
        }
        int target = sum / 2;
        // 考虑到第i位为止是否存在和为j的子集
        boolean[][] dp = new boolean[nums.length][target + 1];
        for (int i = 0; i < nums.length; i++) {
            dp[i][0] = true;
            if (target >= nums[i]) {
                dp[i][nums[i]] = true;
            }
        }
        for (int i = 1; i < nums.length; i++) {
            for (int j = 1; j < target + 1; j++) {
                dp[i][j] = dp[i - 1][j] || (j >= nums[i] && dp[i - 1][j - nums[i]]);
            }
        }
        return dp[nums.length - 1][target];
    }

    /**
     * 最长有效括号
     * 这是DP的思路，还有另外一种思路是boolean数组+栈匹配，把有效匹配的括号设置true，然后统计最长的连续的true的个数
     * 当然还是DP更高效
     */
    public int longestValidParentheses(String s) {
        if (s == null || s.isEmpty()) {
            return 0;
        }
        // 以字符i结尾的最长有效括号的长度
        int[] dp = new int[s.length()];
        dp[0] = 0;
        int max = 0;
        for (int i = 1; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch == ')') {
                if (s.charAt(i - 1) == '(') {
                    // ()()
                    dp[i] = 2 + (i >= 2 ? dp[i - 2] : 0);
                } else {
                    // (())
                    dp[i] = i - dp[i - 1] - 1 >= 0 && s.charAt(i - dp[i - 1] - 1) == '('
                            ? 2 + dp[i - 1] + (i - dp[i - 1] - 2 >= 0 ? dp[i - dp[i - 1] - 2] : 0)
                            : 0;
                }
            }
            max = Math.max(max, dp[i]);
        }
        return max;
    }

    /**
     * 不同路径
     */
    public int uniquePathsTwoEx(int m, int n) {
        int[][] dp = new int[m][n];
        for (int i = 0; i < n; i++) {
            dp[0][i] = 1;
        }
        for (int i = 0; i < m; i++) {
            dp[i][0] = 1;
        }
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
            }
        }
        return dp[m - 1][n - 1];
    }

    /**
     * 最长公共子序列
     * 没做出来，dp数组的定义是对的，但是初始化有问题
     * 可以这么初始化dp数组来简化初始化：
     * int[][] dp = new int[l1 + 1][l2 + 1];
     */
    public int longestCommonSubsequence(String text1, String text2) {
        int l1 = text1.length(), l2 = text2.length();
        // 考虑到字符串1的i和字符串2的j为止的最长公共子序列长度
        int[][] dp = new int[l1 + 1][l2 + 1];
        for (int i = 1; i < l1 + 1; i++) {
            for (int j = 1; j < l2 + 1; j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        return dp[l1][l2];
    }

    /**
     * 编辑距离
     */
    public int minDistance(String word1, String word2) {
        // word1转换word2
        // 仅考虑到word1的第i位和word2的第j位为止的编辑距离
        int l1 = word1.length();
        int l2 = word2.length();
        int[][] dp = new int[l1 + 1][l2 + 1];
        for (int i = 0; i < l1 + 1; i++) {
            dp[i][0] = i;
        }
        for (int i = 0; i < l2 + 1; i++) {
            dp[0][i] = i;
        }
        for (int i = 1; i < l1 + 1; i++) {
            for (int j = 1; j < l2 + 1; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // 删除，增加，替换
                    dp[i][j] = Math.min(dp[i - 1][j], Math.min(dp[i][j - 1], dp[i - 1][j - 1])) + 1;
                }
            }
        }
        return dp[l1][l2];
    }

    /**
     * 只出现一次的数字
     */
    public int singleNumber(int[] nums) {
        int res = 0;
        for (int num : nums) {
            res ^= num;
        }
        return res;
    }

    /**
     * 多数元素
     * 没做出来，主要是这个占领高地的算法忘记了
     */
    public int majorityElement(int[] nums) {
        // 占领高地算法
        int winner = nums[0];
        int count = 0;
        for (int i = 0; i < nums.length; i++) {
            if (count == 0) {
                winner = nums[i];
            }
            if (winner != nums[i]) {
                count--;
            } else {
                count++;
            }
        }
        return winner;
    }

    /**
     * 颜色分类
     */
    public void sortColors(int[] nums) {
        quickSort(nums);
    }

    /**
     * 下一个排列
     * 没做出来
     * 从数组倒着查找，找到nums[i] 比nums[i+1]小的时候（遇到第一个升序），
     * 就将nums[i]跟nums[i+1]到nums[nums.length - 1]当中找到一个最小的比nums[i]大的元素交换。
     * 交换后，再把nums[i+1]到nums[nums.length-1]排序
     */
    public void nextPermutation(int[] nums) {
        int i = nums.length - 2;
        while (i >= 0 && nums[i] >= nums[i + 1]) {
            i--;
        }
        if (i >= 0) {
            int j = nums.length - 1;
            while (nums[j] <= nums[i]) {
                j--;
            }
            swap(nums, i, j);
        }
        reverse(nums, i + 1);
    }

    private void reverse(int[] nums, int start) {
        int i = start, j = nums.length - 1;
        while (i < j) {
            swap(nums, i, j);
            i++;
            j--;
        }
    }

    /**
     * 寻找重复数
     */
    public int findDuplicate(int[] nums) {
        Arrays.sort(nums);
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] == nums[i - 1]) {
                return nums[i];
            }
        }
        return -1;
    }

    /**
     * 就像曾经高考一样
     * 8:00 - 8:30 起床
     * 8：50 - 10:35 面经
     * 11：00 - 12：00 写两道算法题
     * 
     * 13：30 - 2：00 面经
     * 2：30 - 5：00 面经
     * 
     * 7：00 - 23：00 算法题
     */
}