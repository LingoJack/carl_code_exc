package leetcodeHot100;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
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

    /**
     * 矩阵置零
     * 7min ac 击败51%
     * 但是空间复杂度太高，不符合题目要求
     */
    public void setZeroes(int[][] matrix) {
        Deque<int[]> queue = new ArrayDeque<>();
        int row = matrix.length;
        int col = matrix[0].length;
        for(int i = 0; i < row; i++) {
            for(int j = 0; j < col; j++) {
                if(matrix[i][j] == 0) {
                    queue.offer(new int[]{i, j});
                }
            }
        }
        while(!queue.isEmpty()) {
            int[] index = queue.poll();
            for(int i = 0; i < row; i++) {
                matrix[i][index[1]] = 0;
            }
            for(int i = 0; i < col; i++) {
                matrix[index[0]][i] = 0;
            }
        }
    }

    /**
     * 矩阵置零
     * 优化空间复杂度
     */
    public void setZeroes(int[][] matrix) {
        int row = matrix.length;
        int col = matrix[0].length;
        boolean row0ContainZero = false;
        boolean col0ContainZero = false;
        boolean[] rowConatinZero = new boolean[row];
        boolean[] colContainZero = new boolean[col];
        for(int i = 0; i < col; i++) {
            if(matrix[0][i] == 0) {
                row0ContainZero = true;
                break;
            }
        }
        for (int i = 0; i < row; i++) {
            if (matrix[i][0] == 0) {
                col0ContainZero = true;
                break;
            }
        }
        for(int i = 0; i < row; i++) {
            for(int j = 0; j < col; j++) {
                if(matrix[i][j] == 0) {
                    rowConatinZero[i] = true;
                    colContainZero[j] = true;
                }
            }
        }
        if(row0ContainZero) {
            for (int i = 0; i < col; i++) {
                matrix[0][i] = 0;
            }
        }
        if(col0ContainZero) {
            for(int i = 0; i < row; i++) {
                matrix[i][0] = 0;
            }
        }
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (rowConatinZero[i] || colContainZero[j]) {
                    matrix[i][j] = 0;
                }
            }
        }
    }

    /**
     * 旋转图像
     */
}
