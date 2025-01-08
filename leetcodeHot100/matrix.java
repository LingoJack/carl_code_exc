package leetcodeHot100;

import java.util.ArrayList;
import java.util.List;

public class matrix {
    /**
     * 螺旋矩阵
     * 28min ac
     * 自己做出来了
     * 核心是visited数组和四个方向状态机的转换
     */
    public List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> res = new ArrayList<>();
        if (matrix == null) {
            return res;
        }
        int row = matrix.length;
        int column = matrix[0].length;
        boolean[][] visited = new boolean[row][column];
        int rowIndex = 0;
        int columnIndex = 0;
        int count = 0;
        // 0 1 2 3
        // 上右下左
        int dir = 1;
        while (count != row * column) {
            if (dir == 1) {
                for(int i = 0; i < column; i++) {
                    if (!visited[rowIndex][i]) {
                        res.add(matrix[rowIndex][i]);
                        visited[rowIndex][i] = true;
                        columnIndex = i;
                        count++;
                    }
                }
                dir = 2;
            } else if (dir == 2) {
                for(int i = 0; i < row; i++) {
                    if (!visited[i][columnIndex]) {
                        res.add(matrix[i][columnIndex]);
                        visited[i][columnIndex] = true;
                        rowIndex = i;
                        count++;
                    }
                }
                dir = 3;
            } else if (dir == 3) {
                for(int i = column - 1; i >= 0; i--) {
                    if (!visited[rowIndex][i]) {
                        res.add(matrix[rowIndex][i]);
                        visited[rowIndex][i] = true;
                        columnIndex = i;
                        count++;
                    }
                }
                dir = 0;
            } else if (dir == 0) {
                for(int i = row - 1; i >= 0; i--) {
                    if (!visited[i][columnIndex]) {
                        res.add(matrix[i][columnIndex]);
                        visited[i][columnIndex] = true;
                        rowIndex = i;
                        count++;
                    }
                }
                dir = 1;
            }
        }
        return res;
    }
}
