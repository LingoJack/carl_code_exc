package backtracking;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
        backtracking4Combine(n, k, 1);
        return result4Combina;
    }

    private void backtracking4Combine(int n, int k, int startIndex) {
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
            backtracking4Combine(n, k, i + 1);
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
        backtracking4CombinationSum(res, list, sum, 1, k, n);
        return res;
    }

    private void backtracking4CombinationSum(List<List<Integer>> res, List<Integer> list, int sum, int start, int k, int n) {
        if (list.size() == k) {
            if(sum == n) res.add(new ArrayList<>(list));
            return;
        }
        for (int i = start; i <= Math.min(9, n - sum); i++) {
            list.add(i);
            sum += i;
            backtracking4CombinationSum(res, list, sum, i + 1, k, n);
            list.removeLast();
            sum -= i;
        }
    }
}
