package leetcodeHot100;

public class dynamic_program {

    /**
     * 最小路径和
     * 9min30s ac
     * 其实写完代码花了4min30s，剩下的时间在查初始化的Bug...
     */
    public int minPathSum(int[][] grid) {
        if (grid == null) {
            return 0;
        }
        int row = grid.length;
        int col = grid[0].length;
        int[][] dp = new int[row][col];
        for (int i = 0; i < col; i++) {
            dp[0][i] = i > 0 ? dp[0][i - 1] + grid[0][i] : grid[0][i];
        }
        for(int i = 1; i < row; i++) {
            dp[i][0] = dp[i - 1][0] + grid[i][0];
        }
        for(int i = 1; i < row; i++) {
            for(int j = 1; j < col; j++) {
                dp[i][j] = Math.min(dp[i][j - 1], dp[i - 1][j]) + grid[i][j];
            }
        }
        return dp[row - 1][col - 1];
    }
}
