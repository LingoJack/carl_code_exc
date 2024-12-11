package backtracking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
        for(int end = start; end < start + 3 && end < s.length(); end++) {
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
}
