package leetcodeHot100;

import java.util.ArrayList;
import java.util.List;

public class backtrack {
    /**
     * 括号生成
     */
    public List<String> generateParenthesis(int n) {
        List<String> res = new ArrayList<>();
        backtrack4GenerateParenthesis(res, new StringBuilder().append("("), n, 1, 0);
        return res;
    }

    private void backtrack4GenerateParenthesis(List<String> res, StringBuilder sb, int n, int leftParentthesisCount, int rightParentthesisCount) {
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
        
    }
}
