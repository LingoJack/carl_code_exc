package graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class graph {

    /**
     * 所有可达路径
     * DFS版本
     */
    public List<List<Integer>> allPathsSourceTargetWithDFS(int[][] graph) {
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> path = new ArrayList<>();
        dfs4allPathsSourceTarget(graph, 0, graph.length - 1, path, result);
        return result;
    }

    private void dfs4allPathsSourceTarget(int[][] graph, int node, int target, List<Integer> path,
            List<List<Integer>> result) {
        // 将当前节点加入路径
        path.add(node);
        // 如果当前节点是目标节点，保存当前路径
        if (node == target) {
            result.add(new ArrayList<>(path)); // 复制当前路径
        } else {
            // 遍历当前节点的所有邻接节点
            for (int nextNode : graph[node]) {
                dfs4allPathsSourceTarget(graph, nextNode, target, path, result);
            }
        }
        // 回溯，移除当前节点
        path.remove(path.size() - 1);
    }

    /**
     * 岛屿数量
     * DFS，BFS、并查集的入门题目
     * 核心就是使用known数组标记一个位置是否已经被访问过，
     * 并且在找到第一个未被访问的1时，DFS进行扩散
     * 这里为深搜解法
     */
    public int numIslandsWithDFS(char[][] grid) {
        if (grid == null) {
            return 0;
        }
        int count = 0;
        int rowNum = grid.length;
        int columnNum = grid[0].length;
        boolean[][] known = new boolean[rowNum][columnNum];
        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < columnNum; j++) {
                if (known[i][j] == false && grid[i][j] == '1') {
                    count++;
                    spreadByDFS(grid, known, i, j);
                }
            }
        }
        return count;
    }

    private void spreadByDFS(char[][] grid, boolean[][] known, int row, int column) {
        int rowNum = grid.length;
        int columnNum = grid[0].length;
        if (row < 0 || row >= rowNum || column < 0 || column >= columnNum) {
            return;
        }
        if (known[row][column] == false && grid[row][column] == '1') {
            known[row][column] = true;
            spreadByDFS(grid, known, row + 1, column);
            spreadByDFS(grid, known, row - 1, column);
            spreadByDFS(grid, known, row, column + 1);
            spreadByDFS(grid, known, row, column - 1);
        }
    }

    /**
     * 岛屿数量
     * 这里为广搜解法
     */
    public int numIslandsWithBFS(char[][] grid) {
        if (grid == null) {
            return 0;
        }
        int count = 0;
        int rowNum = grid.length;
        int columnNum = grid[0].length;
        boolean[][] known = new boolean[rowNum][columnNum];
        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < columnNum; j++) {
                if (known[i][j] == false && grid[i][j] == '1') {
                    count++;
                    spreadByBFS(grid, known, i, j);
                }
            }
        }
        return count;
    }

    private void spreadByBFS(char[][] grid, boolean[][] known, int row, int column) {
        int rowNum = grid.length;
        int columnNum = grid[0].length;
        ArrayDeque<Pair> queue = new ArrayDeque<>();
        queue.offer(new Pair(row, column));
        known[row][column] = true;
        while (!queue.isEmpty()) {
            Pair index = queue.poll();
            if (index.row < 0 || index.row >= rowNum || index.column < 0 || index.column >= columnNum) {
                continue;
            }
            if (index.row - 1 >= 0 && known[index.row - 1][index.column] == false
                    && grid[index.row - 1][index.column] == '1') {
                queue.offer(new Pair(index.row - 1, index.column));
                known[index.row - 1][index.column] = true;
            }
            if (index.row + 1 < rowNum && known[index.row + 1][index.column] == false
                    && grid[index.row + 1][index.column] == '1') {
                queue.offer(new Pair(index.row + 1, index.column));
                known[index.row + 1][index.column] = true;
            }
            if (index.column - 1 >= 0 && known[index.row][index.column - 1] == false
                    && grid[index.row][index.column - 1] == '1') {
                queue.offer(new Pair(index.row, index.column - 1));
                known[index.row][index.column - 1] = true;
            }
            if (index.column + 1 < columnNum && known[index.row][index.column + 1] == false
                    && grid[index.row][index.column + 1] == '1') {
                queue.offer(new Pair(index.row, index.column + 1));
                known[index.row][index.column + 1] = true;
            }
        }
    }

    public class Pair {
        int row;
        int column;

        public Pair() {

        }

        public Pair(int row, int column) {
            this.row = row;
            this.column = column;
        }
    }
}
