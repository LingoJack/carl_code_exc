package leetcodeHot100;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class graph {

    /**
     * 岛屿数量
     * 并查集的关键就是操作并和查
     * 并，就是小树并入大树
     * 查，就是看祖先是否是同一个。
     * 祖先节点存储树的大小（秩），用parent数组记录父子关系
     * DFS和BFS解法请见graph专题
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

    /**
     * 腐烂的橘子
     * 没做出来，这题不适合用DFS做，只适合用BFS做
     * 类似树的层序遍历，需要有一个size来防止每一次腐烂范围扩散
     */
    public int orangesRotting(int[][] grid) {
        int rowNum = grid.length;
        int columnNum = grid[0].length;
        // 用来存放腐烂橘子的坐标
        Queue<int[]> queue = new ArrayDeque<>();
        int freshCount = 0; // 记录新鲜橘子的数量
        // 初始化队列，加入所有腐烂橘子的位置
        for (int row = 0; row < rowNum; row++) {
            for (int col = 0; col < columnNum; col++) {
                if (grid[row][col] == 2) {
                    queue.offer(new int[] { row, col });
                } else if (grid[row][col] == 1) {
                    freshCount++;
                }
            }
        }
        // 如果没有新鲜橘子，直接返回0
        if (freshCount == 0) {
            return 0;
        }
        // 四个方向：上、下、左、右
        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
        int minutes = 0;
        // 广度优先搜索
        while (!queue.isEmpty() && freshCount > 0) {
            // 这里有点像二叉树的层序遍历，用意是记录每一次的腐烂扩散的范围
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int[] current = queue.poll();
                int row = current[0];
                int col = current[1];
                // 扩展到四个方向
                for (int[] dir : directions) {
                    int newRow = row + dir[0];
                    int newCol = col + dir[1];
                    // 判断新位置是否合法且是新鲜橘子
                    if (newRow >= 0 && newRow < rowNum && newCol >= 0 && newCol < columnNum
                            && grid[newRow][newCol] == 1) {
                        grid[newRow][newCol] = 2;
                        freshCount--;
                        queue.offer(new int[] { newRow, newCol });
                    }
                }
            }
            minutes++;
        }
        // 如果仍有新鲜橘子未腐烂，返回-1
        return freshCount == 0 ? minutes : -1;
    }

    /**
     * 课程表
     * 拓扑排序，这里也就是判断是否有环路
     * 入度为0，加计数器
     */
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        int count = 0;
        int[] inDegree = new int[numCourses];
        Map<Integer, List<Integer>> map = new HashMap<>();
        boolean[] used = new boolean[numCourses];
        for (int[] rel : prerequisites) {
            inDegree[rel[1]]++;
            List<Integer> list = map.getOrDefault(rel[0], new ArrayList<>());
            list.add(rel[1]);
            map.put(rel[0], list);
        }

        int course = findOneWithZeroInDegree(inDegree, used);
        while (count != numCourses && course != -1) {
            List<Integer> list = map.get(course);
            if (list != null) {
                for (int to : list) {
                    inDegree[to]--;
                }
            }
            used[course] = true;
            count++;
            course = findOneWithZeroInDegree(inDegree, used);
        }

        return count == numCourses;
    }

    private int findOneWithZeroInDegree(int[] inDegree, boolean[] used) {
        for (int i = 0; i < inDegree.length; i++) {
            if (inDegree[i] == 0 && !used[i]) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 前缀树
     * 30min ac
     */
    class Trie {

        private Map<Character, Node> map;

        private Map<String, Boolean> contain;

        public Trie() {
            this.map = new HashMap<>();
            this.contain = new HashMap<>();
        }

        public void insert(String word) {
            if (word.isEmpty()) {
                return;
            }
            char[] str = word.toCharArray();
            Node head = map.get(str[0]);
            if (head == null) {
                head = new Node(str[0]);
                map.put(str[0], head);
            }
            Node node = head;
            for (int i = 1; i < str.length; i++) {
                char ch = str[i];
                Node nextNode = node.nextMap.get(ch);
                if (nextNode == null) {
                    nextNode = new Node(ch);
                    node.nextMap.put(ch, nextNode);
                }
                node = nextNode;
            }
            contain.put(word, true);
        }

        public boolean search(String word) {
            if (word.equals("")) {
                return true;
            }
            return contain.getOrDefault(word, false);
        }

        public boolean startsWith(String prefix) {
            if (prefix.equals("")) {
                return true;
            }
            char[] str = prefix.toCharArray();
            Node head = map.get(str[0]);
            if (head == null) {
                return false;
            }
            Node node = head;
            for (int i = 1; i < str.length; i++) {
                char ch = str[i];
                Node nextNode = node.nextMap.get(ch);
                if (nextNode == null) {
                    return false;
                }
                node = nextNode;
            }
            return true;
        }

        public class Node {
            char ch;
            Map<Character, Node> nextMap;

            public Node(char ch) {
                this.ch = ch;
                nextMap = new HashMap<>();
            }
        }
    }

    /**
     * 岛屿数量
     * 这是二刷了
     * dfs解法
     */
    public int numIslandsWithDFS(char[][] grid) {
        int row = grid.length;
        int column = grid[0].length;
        boolean[][] visited = new boolean[row][column];
        int count = 0;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                if (!visited[i][j] && grid[i][j] == '1') {
                    count++;
                    dfs(grid, visited, i, j);
                }
            }
        }
        return count;
    }

    private void dfs(char[][] grid, boolean[][] visited, int rowIndex, int columnIndex) {
        if (!(columnIndex >= 0 && columnIndex < grid[0].length) || !(rowIndex >= 0 && rowIndex < grid.length)
                || visited[rowIndex][columnIndex] || grid[rowIndex][columnIndex] == '0') {
            return;
        }
        visited[rowIndex][columnIndex] = true;
        dfs(grid, visited, rowIndex - 1, columnIndex);
        dfs(grid, visited, rowIndex + 1, columnIndex);
        dfs(grid, visited, rowIndex, columnIndex - 1);
        dfs(grid, visited, rowIndex, columnIndex + 1);
    }

    /**
     * 腐烂的橘子
     * 二刷，依旧没做出来
     * 而且思路还是不对
     * 关键是利用BFS，类似树的层序遍历的思路
     * 下面是修改过后的正确的代码
     */
    public int orangesRottingWithBfsAndFloorTraserval(int[][] grid) {
        int freshNum = 0;
        Deque<int[]> queue = new ArrayDeque<>();
        int row = grid.length;
        int column = grid[0].length;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                if (grid[i][j] == 1) {
                    freshNum++;
                } else if (grid[i][j] == 2) {
                    queue.offer(new int[] { i, j });
                }
            }
        }
        if (freshNum == 0) {
            return 0;
        }
        int minute = 0;
        while (!queue.isEmpty() && freshNum > 0) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int[] index = queue.poll();
                int rowIndex = index[0];
                int columnIndex = index[1];
                int diff = spread(grid, queue, rowIndex - 1, columnIndex)
                        + spread(grid, queue, rowIndex + 1, columnIndex)
                        + spread(grid, queue, rowIndex, columnIndex - 1)
                        + spread(grid, queue, rowIndex, columnIndex + 1);
                freshNum -= diff;
            }
            minute++;
        }
        return freshNum == 0 ? minute : -1;
    }

    private int spread(int[][] grid, Deque<int[]> queue, int rowIndex, int columnIndex) {
        int row = grid.length;
        int column = grid[0].length;
        if (rowIndex < 0 || rowIndex >= row || columnIndex < 0 || columnIndex >= column) {
            return 0;
        }
        if (grid[rowIndex][columnIndex] == 1) {
            grid[rowIndex][columnIndex] = 2;
            queue.offer(new int[] { rowIndex, columnIndex });
            return 1;
        }
        return 0;
    }

    /**
     * 课程表
     */
    public boolean canFinishWithIndegreeAndQueueAndHashMap(int numCourses, int[][] prerequisites) {
        int[] inDegree = new int[numCourses];
        Map<Integer, List<Integer>> dependMap = new HashMap<>();
        Deque<Integer> queue = new ArrayDeque<>();
        for (int[] rel : prerequisites) {
            inDegree[rel[0]]++;
            dependMap.computeIfAbsent(rel[1], ArrayList::new).add(rel[0]);
        }
        for (int i = 0; i < numCourses; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }
        while (!queue.isEmpty()) {
            Integer preCourse = queue.poll();
            for (int postCourse : dependMap.getOrDefault(preCourse, new ArrayList<>())) {
                if (--inDegree[postCourse] == 0) {
                    queue.offer(postCourse);
                }
            }
            numCourses--;
        }
        return numCourses == 0;
    }

    /**
     * 课程表，一样是使用入度来计算拓扑，但是使用ArrayList而不是HashMap来记录出入关系
     * 击败66.42%
     */
    public boolean canFinishWithIndegreeAndQueueAndArrayList(int numCourses, int[][] prerequisites) {
        int[] inDegree = new int[numCourses];
        List<List<Integer>> dependMap = new ArrayList<>();
        Deque<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < numCourses; i++) {
            dependMap.add(new ArrayList<>());
        }
        for (int[] rel : prerequisites) {
            inDegree[rel[0]]++;
            dependMap.get(rel[1]).add(rel[0]);
        }
        for (int i = 0; i < numCourses; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }
        while (!queue.isEmpty()) {
            Integer preCourse = queue.poll();
            for (int postCourse : dependMap.get(preCourse)) {
                if (--inDegree[postCourse] == 0) {
                    queue.offer(postCourse);
                }
            }
            numCourses--;
        }
        return numCourses == 0;
    }

    List<List<Integer>> edges;
    // 0 未搜索；1 搜索中；2 搜索过
    int[] visited;
    boolean valid = true;

    public boolean canFinishWithDFS(int numCourses, int[][] prerequisites) {
        edges = new ArrayList<List<Integer>>();
        for (int i = 0; i < numCourses; ++i) {
            edges.add(new ArrayList<Integer>());
        }
        visited = new int[numCourses];
        for (int[] info : prerequisites) {
            edges.get(info[1]).add(info[0]);
        }
        for (int i = 0; i < numCourses && valid; ++i) {
            if (visited[i] == 0) {
                dfs(i);
            }
        }
        return valid;
    }

    public void dfs(int u) {
        visited[u] = 1;
        for (int v : edges.get(u)) {
            if (visited[v] == 0) {
                dfs(v);
                if (!valid) {
                    return;
                }
            } else if (visited[v] == 1) {
                valid = false;
                return;
            }
        }
        visited[u] = 2;
    }

    public class TrieTwoEx {
        private class Node {
            Map<Character, Node> children = new HashMap<>();
            boolean isEndOfWord;

            public Node() {
                isEndOfWord = false;
            }
        }

        private final Node root;

        public TrieTwoEx() {
            root = new Node();
        }

        public void insert(String word) {
            Node node = root;
            for (char c : word.toCharArray()) {
                node.children.putIfAbsent(c, new Node());
                node = node.children.get(c);
            }
            node.isEndOfWord = true;
        }

        public boolean search(String word) {
            Node node = root;
            for (char c : word.toCharArray()) {
                if (!node.children.containsKey(c)) {
                    return false;
                }
                node = node.children.get(c);
            }
            return node.isEndOfWord;
        }

        public boolean startsWith(String prefix) {
            Node node = root;
            for (char c : prefix.toCharArray()) {
                if (!node.children.containsKey(c)) {
                    return false;
                }
                node = node.children.get(c);
            }
            return true;
        }
    }

}
