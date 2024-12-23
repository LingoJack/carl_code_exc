package leetcodeHot100;

public class graph {

    /**
     * 岛屿数量
     */
    public int numIslands(char[][] grid) {
        if (grid == null || grid.length == 0) {
            return 0;
        }
        int height = grid.length;
        int width = grid[0].length;
       
        // 并查集的父节点数组
        int[] parent = new int[height * width];
        // 并查集的大小数组，用于按秩合并
        int[] size = new int[height * width];

        // 初始化并查集
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (grid[i][j] == '1') {
                    int index = i * width + j;
                    parent[index] = index; // 自己是自己的父节点
                    size[index] = 1; // 每个岛屿初始大小为1
                } else {
                    parent[i * width + j] = -1; // 水域没有父节点
                }
            }
        }

        // 定义四个方向（上、下、左、右）
        int[] directions = { -1, 1, 0, 0 }; // 上下左右

        // 合并操作：通过并查集的路径压缩和按秩合并
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (grid[i][j] == '1') {
                    int index = i * width + j;
                    // 检查四个方向是否相邻的格子是陆地并且进行合并
                    for (int d = 0; d < 4; d++) {
                        int ni = i + directions[d];
                        int nj = j + directions[3 - d];
                        if (ni >= 0 && ni < height && nj >= 0 && nj < width && grid[ni][nj] == '1') {
                            int neighborIndex = ni * width + nj;
                            // 合并当前格子和相邻格子的岛屿
                            union(parent, size, index, neighborIndex);
                        }
                    }
                }
            }
        }
        int count = 0;
        // 统计岛屿个数
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (grid[i][j] == '1' && parent[i * width + j] == i * width + j) {
                    count++; // 每次找到一个新的根节点即为一个岛屿
                }
            }
        }
        return count;
    }

    // 查找根节点（路径压缩）
    private int find(int[] parent, int index) {
        if (parent[index] != index) {
            parent[index] = find(parent, parent[index]); // 路径压缩
        }
        return parent[index];
    }

    // 合并两个岛屿
    private void union(int[] parent, int[] size, int index1, int index2) {
        int root1 = find(parent, index1);
        int root2 = find(parent, index2);
        if (root1 != root2) {
            // 按秩合并：小树挂在大树下
            if (size[root1] > size[root2]) {
                parent[root2] = root1;
                size[root1] += size[root2];
            } else {
                parent[root1] = root2;
                size[root2] += size[root1];
            }
        }
    }
}
