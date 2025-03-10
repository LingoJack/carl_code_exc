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
                for (int i = 0; i < column; i++) {
                    if (!visited[rowIndex][i]) {
                        res.add(matrix[rowIndex][i]);
                        visited[rowIndex][i] = true;
                        columnIndex = i;
                        count++;
                    }
                }
                dir = 2;
            } else if (dir == 2) {
                for (int i = 0; i < row; i++) {
                    if (!visited[i][columnIndex]) {
                        res.add(matrix[i][columnIndex]);
                        visited[i][columnIndex] = true;
                        rowIndex = i;
                        count++;
                    }
                }
                dir = 3;
            } else if (dir == 3) {
                for (int i = column - 1; i >= 0; i--) {
                    if (!visited[rowIndex][i]) {
                        res.add(matrix[rowIndex][i]);
                        visited[rowIndex][i] = true;
                        columnIndex = i;
                        count++;
                    }
                }
                dir = 0;
            } else if (dir == 0) {
                for (int i = row - 1; i >= 0; i--) {
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
    public void setZeroesAwesomeSpace(int[][] matrix) {
        Deque<int[]> queue = new ArrayDeque<>();
        int row = matrix.length;
        int col = matrix[0].length;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (matrix[i][j] == 0) {
                    queue.offer(new int[] { i, j });
                }
            }
        }
        while (!queue.isEmpty()) {
            int[] index = queue.poll();
            for (int i = 0; i < row; i++) {
                matrix[i][index[1]] = 0;
            }
            for (int i = 0; i < col; i++) {
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
        for (int i = 0; i < col; i++) {
            if (matrix[0][i] == 0) {
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
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (matrix[i][j] == 0) {
                    rowConatinZero[i] = true;
                    colContainZero[j] = true;
                }
            }
        }
        if (row0ContainZero) {
            for (int i = 0; i < col; i++) {
                matrix[0][i] = 0;
            }
        }
        if (col0ContainZero) {
            for (int i = 0; i < row; i++) {
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
     * 没做出来
     * 思路是：
     * 先水平翻转，然后再沿主对角线翻转
     */
    public void rotate(int[][] matrix) {
        int n = matrix.length;
        // 水平翻转
        for (int i = 0; i < n / 2; ++i) {
            for (int j = 0; j < n; ++j) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[n - i - 1][j];
                matrix[n - i - 1][j] = temp;
            }
        }
        // 主对角线翻转
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < i; ++j) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = temp;
            }
        }
    }

    /**
     * 搜索二维矩阵II
     * 做出来了，但不是最优的方法
     */
    public boolean searchMatrix(int[][] matrix, int target) {
        int row = matrix.length;
        int col = matrix[0].length;
        Deque<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < row; i++) {
            if (matrix[i][0] <= target && matrix[i][col - 1] >= target) {
                queue.offer(i);
            } else if (matrix[i][0] > target) {
                break;
            }
        }
        while (!queue.isEmpty()) {
            int possibleRow = queue.poll();
            int lt = 0, rt = col - 1;
            while (lt <= rt) {
                int mid = (lt + rt) / 2;
                int val = matrix[possibleRow][mid];
                if (val < target) {
                    lt = mid + 1;
                } else if (val > target) {
                    rt = mid - 1;
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 搜索二维矩阵II
     * Z字形搜索
     * 很巧妙地利用了这个矩阵的性质
     */
    public boolean searchMatrixWithZLikeSearch(int[][] matrix, int target) {
        int row = matrix.length, col = matrix[0].length;
        int x = 0, y = col - 1;
        while (x < row && y >= 0) {
            if (matrix[x][y] == target) {
                return true;
            }
            if (matrix[x][y] > target) {
                --y;
            } else {
                ++x;
            }
        }
        return false;
    }
}
