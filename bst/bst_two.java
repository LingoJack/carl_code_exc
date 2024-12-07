package bst;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

import apple.laf.JRSUIUtils.Tree;

/**
 * 需要清楚，二叉树的几种不同的遍历方式
 * DFS、BFS、
 * 前序、中序、后序、
 * 层序
 * 的写法
 */
public class bst_two {
    class Node {
        public int val;
        public Node left;
        public Node right;
        public Node next;

        public Node() {
        }

        public Node(int _val) {
            val = _val;
        }

        public Node(int _val, Node _left, Node _right, Node _next) {
            val = _val;
            left = _left;
            right = _right;
            next = _next;
        }
    };

    /**
     * 填充每个节点的下一个右侧节点指针
     */
    public Node connect(Node root) {
        ArrayDeque<Node> queue = new ArrayDeque<>();
        if (root != null) {
            queue.offer(root);
        } else {
            return root;
        }

        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                Node node = queue.poll();
                if (node != null) {
                    if (node.left != null) {
                        queue.offer(node.left);
                    }
                    if (node.right != null) {
                        queue.add(node.right);
                    }
                    Node next = queue.peek();
                    // 这里需要判断一下，本层的最后一个节点应该指向null，而不是下一层的第一个节点
                    node.next = i == size - 1 ? null : next;
                }
            }
        }

        return root;
    }

    /**
     * 填充每个节点的下一个右侧节点指针II
     * 呃，和上题的代码一模一样
     */
    public Node connect2(Node root) {
        ArrayDeque<Node> queue = new ArrayDeque<>();
        if (root != null) {
            queue.offer(root);
        } else {
            return root;
        }

        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                Node node = queue.poll();
                if (node != null) {
                    if (node.left != null) {
                        queue.offer(node.left);
                    }
                    if (node.right != null) {
                        queue.add(node.right);
                    }
                    Node next = queue.peek();
                    // 这里需要判断一下，本层的最后一个节点应该指向null，而不是下一层的第一个节点
                    node.next = i == size - 1 ? null : next;
                }
            }
        }

        return root;
    }

    public class TreeNode {
        int val; // 节点的值
        TreeNode left; // 左子节点
        TreeNode right; // 右子节点

        // 默认构造函数
        TreeNode() {
        }

        // 带值的构造函数
        TreeNode(int val) {
            this.val = val;
        }

        // 带值、左子节点和右子节点的构造函数
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    /**
     * 二叉树的最小深度
     * 感觉可以使用递归，当前实现的时间复杂度很高，因为存在很多重复计算
     * 等后面讲递归会说优化方法：记忆话递归
     * 当然也还有层序遍历的解法
     */
    public int minDepth(TreeNode root) {
        if (root == null) {
            return 0;
        } else if (root.left == null || root.right == null) {
            return 1 + Math.max(calcMin(root.left), calcMin(root.right));
        }
        return calcMin(root);
    }

    private int calcMin(TreeNode node) {
        if (node == null) {
            return 0;
        } else if (node.left == null || node.right == null) {
            return 1 + Math.max(calcMin(node.left), calcMin(node.right));
        }
        return 1 + Math.min(calcMin(node.left), calcMin(node.right));
    }

    /**
     * 最小深度的层序解法
     */
    public int minDepthWithLevelOrder(TreeNode root) {
        int res = 0;
        ArrayDeque<TreeNode> queue = new ArrayDeque<>();
        if (root != null) {
            queue.offer(root); // 将根节点加入队列
        } else {
            return 0; // 如果根节点为空，返回空结果
        }

        while (!queue.isEmpty()) {
            int size = queue.size(); // 获取当前层的节点数
            // 这里对分层的处理（即，确定每一层的元素个数）很巧妙
            res++;
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll(); // 从队列中取出节点
                if (node != null) {
                    if (node.right == null && node.left == null) {
                        return res;
                    }
                    if (node.left != null)
                        queue.offer(node.left); // 将左子节点加入队列
                    if (node.right != null)
                        queue.offer(node.right); // 将右子节点加入队列Ò
                }
            }
        }

        return res;
    }

    /**
     * 二叉树的最大深度
     * 递归解法，另外还有层序解法
     */
    public int maxDepth(TreeNode root) {
        return calcMax(root);
    }

    private int calcMax(TreeNode node) {
        if (node == null) {
            return 0;
        }
        return 1 + Math.max(calcMax(node.left), calcMax(node.right));
    }

    /**
     * 最大深度的层序解法
     */
    public int maxDepthWithLevelOrder(TreeNode root) {
        int res = 0;
        ArrayDeque<TreeNode> queue = new ArrayDeque<>();
        if (root != null) {
            queue.offer(root); // 将根节点加入队列
        } else {
            return 0; // 如果根节点为空，返回空结果
        }

        while (!queue.isEmpty()) {
            int size = queue.size(); // 获取当前层的节点数
            // 这里对分层的处理（即，确定每一层的元素个数）很巧妙
            res++;
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll(); // 从队列中取出节点
                if (node != null) {
                    if (node.left != null)
                        queue.offer(node.left); // 将左子节点加入队列
                    if (node.right != null)
                        queue.offer(node.right); // 将右子节点加入队列Ò
                }
            }
        }

        return res;
    }

    /**
     * 翻转二叉树
     * 一眼递归解
     */
    public TreeNode invertTree(TreeNode root) {
        invert(root);
        return root;
    }

    private void invert(TreeNode node) {
        if (node == null) {
            return;
        }
        TreeNode t = node.left;
        node.left = node.right;
        node.right = t;
        invert(node.left);
        invert(node.right);
    }

    /**
     * 对称二叉树
     * 使用队列的实现
     */
    public boolean isSymmetric(TreeNode root) {
        Queue<TreeNode> deque = new LinkedList<>();
        deque.offer(root.left);
        deque.offer(root.right);
        while (!deque.isEmpty()) {
            TreeNode leftNode = deque.poll();
            TreeNode rightNode = deque.poll();
            if (leftNode == null && rightNode == null) {
                // 此处相当于true
                continue;
            }
            // 以上三个判断条件合并
            if (leftNode == null || rightNode == null || leftNode.val != rightNode.val) {
                return false;
            }
            // 这里顺序与使用Deque不同
            deque.offer(leftNode.left);
            deque.offer(rightNode.right);
            deque.offer(leftNode.right);
            deque.offer(rightNode.left);
        }
        return true;
    }

    /**
     * 对称二叉树
     * 无论是递归还是非递归解法我都没有想出来...
     * 我的递归还是太不熟练了
     */
    public boolean isSymmetricWithRecursion(TreeNode root) {
        if (root == null) {
            return true;
        }
        return compare(root.left, root.right);
    }

    private boolean compare(TreeNode lt, TreeNode rt) {
        if (lt == null && rt != null) {
            return false;
        } else if (lt != null & rt == null) {
            return false;
        } else if (lt == null & rt == null) {
            return true;
        } else if (lt.val != rt.val) {
            return false;
        }
        return compare(lt.right, rt.left) && compare(lt.left, rt.right);
    }

    private int count = 0;

    /**
     * 完全二叉树的节点个数
     */
    public int countNodes(TreeNode root) {
        clacNodeNum(root);
        return count;
    }

    private void clacNodeNum(TreeNode node) {
        if (node != null) {
            count++;
        } else {
            return;
        }
        clacNodeNum(node.left);
        clacNodeNum(node.right);
    }

    /**
     * 平衡二叉树
     * 也是复用之前的代码和递归丑陋地通过了...
     * 如何高效判断一个二叉树是不是平衡树，可以参考AVL的做法：
     * 即，增加一个节点的属性字段用以存储该节点的高度
     * height = Math.max(left.height, right.height) + 1
     */
    public boolean isBalanced(TreeNode root) {
        return balanced(root);
    }

    private boolean balanced(TreeNode node) {
        if (node == null) {
            return true;
        }

        int diff = maxDepth(node.left) - maxDepth(node.right);
        if (diff > 1 || diff < -1) {
            return false;
        }
        return balanced(node.left) && balanced(node.right);
    }

    /**
     * 平衡二叉树，借助AVL的思想
     * 这里用val字段存储节点的高度
     */
    public boolean isBalancedWithBottomUpRecursion(TreeNode root) {
        return height(root) >= 0;
    }

    public int height(TreeNode root) {
        if (root == null) {
            return 0;
        }
        // 后序遍历
        int leftHeight = height(root.left);
        int rightHeight = height(root.right);
        if (leftHeight == -1 || rightHeight == -1 || Math.abs(leftHeight - rightHeight) > 1) {
            return -1;
        } else {
            return Math.max(leftHeight, rightHeight) + 1;
        }
    }

    /**
     * 二叉树的所有路径
     * 是不是可以直接DFS
     * 似乎不行，我实在是没有太明白递归，还有树的遍历顺序到底如何选择
     * 另外这题也需要回溯
     * 最近的题刷得云里雾里，对了迷迷糊糊对了，错的也是一点思路没
     */
    public List<String> binaryTreePaths(TreeNode root) {
        List<String> res = new ArrayList<>();// 存最终的结果
        if (root == null) {
            return res;
        }
        List<Integer> paths = new ArrayList<>();// 作为结果中的路径
        traversal(root, paths, res);
        return res;
    }

    private void traversal(TreeNode root, List<Integer> paths, List<String> res) {
        paths.add(root.val);// 前序遍历，中
        // 遇到叶子结点
        if (root.left == null && root.right == null) {
            // 输出
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < paths.size() - 1; i++) {
                sb.append(paths.get(i)).append("->");
            }
            sb.append(paths.get(paths.size() - 1));// 记录最后一个节点
            res.add(sb.toString());// 收集一个路径
            return;
        }
        // 递归和回溯是同时进行，所以要放在同一个花括号里
        if (root.left != null) { // 左
            traversal(root.left, paths, res);
            paths.remove(paths.size() - 1);// 回溯
        }
        if (root.right != null) { // 右
            traversal(root.right, paths, res);
            paths.remove(paths.size() - 1);// 回溯
        }
    }

    public List<String> binaryTreePathsWithAwesomeStack(TreeNode root) {
        List<String> result = new ArrayList<>();
        if (root == null)
            return result;
        Stack<Object> stack = new Stack<>();
        // 节点和路径同时入栈
        stack.push(root);
        stack.push(root.val + "");
        while (!stack.isEmpty()) {
            // 节点和路径同时出栈
            String path = (String) stack.pop();
            TreeNode node = (TreeNode) stack.pop();
            // 若找到叶子节点
            if (node.left == null && node.right == null) {
                result.add(path);
            }
            // 右子节点不为空
            if (node.right != null) {
                stack.push(node.right);
                stack.push(path + "->" + node.right.val);
            }
            // 左子节点不为空
            if (node.left != null) {
                stack.push(node.left);
                stack.push(path + "->" + node.left.val);
            }
        }
        return result;
    }

    /**
     * 左叶子之和
     */
    public int sumOfLeftLeaves(TreeNode root) {
        if (root == null) {
            return 0;
        }
        return sumLeftChild(root);
    }

    private int sumLeftChild(TreeNode node) {
        if (node == null) {
            return 0;
        }

        int sum = 0;

        // 检查是否是左叶子节点
        if (node.left != null && node.left.left == null && node.left.right == null) {
            sum += node.left.val; // 左叶子值
        }

        // 递归处理左右子树
        sum += sumLeftChild(node.left);
        sum += sumLeftChild(node.right);

        return sum;
    }

    /**
     * 找树左下角的值
     * 呃，为数不多的一眼看有思路的题目
     * 层序遍历直到最后一层，拿到的第一个就是要求的结果
     * 一次过了！！！
     */
    public int findBottomLeftValue(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int res = 0;
        ArrayDeque<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
                if (i == 0) {
                    res = node.val;
                }
            }
        }
        return res;
    }

    private int known_max_dept = -1;
    private int value = 0;

    /**
     * 找树左下角的值，递归解法
     * 这个递归写的也蛮巧妙的
     */
    public int findBottomLeftValueWithRecursion(TreeNode root) {
        value = root.val;
        findLeftValue(root, 0);
        return value;
    }

    private void findLeftValue(TreeNode root, int deep) {
        if (root == null)
            return;
        if (root.left == null && root.right == null) {
            if (deep > known_max_dept) {
                value = root.val;
                known_max_dept = deep;
            }
        }
        if (root.left != null)
            findLeftValue(root.left, deep + 1);
        if (root.right != null)
            findLeftValue(root.right, deep + 1);
    }

    private int targetSum;

    /**
     * 路径总和
     * 成功自己做出来了，击败100%
     */
    public boolean hasPathSum(TreeNode root, int targetSum) {
        this.targetSum = targetSum;
        return searchPathSum(root, 0);
    }

    private boolean searchPathSum(TreeNode node, int sum) {
        if (node == null) {
            return false;
        }
        sum += node.val;
        if (node.left == null && node.right == null) {
            return sum == this.targetSum;
        }
        return searchPathSum(node.left, sum) || searchPathSum(node.right, sum);
    }

    private List<List<Integer>> pathLists = new ArrayList<>();

    /**
     * 路径总和 II
     * 这个涉及回溯，感觉又不是那么好做
     * 什么时候回溯，如何回溯，你真的懂了吗？
     */
    public List<List<Integer>> pathSum2(TreeNode root, int targetSum) {
        this.targetSum = targetSum;
        searchPathSum2(root, 0, new ArrayList<>());
        return this.pathLists;
    }

    private void searchPathSum2(TreeNode node, int sum, List<Integer> list) {
        if (node == null) {
            return;
        }
        sum += node.val;
        list.add(node.val);
        if (node.left == null && node.right == null) {
            if (sum == targetSum) {
                pathLists.add(new ArrayList<>(list));
            }
            return;
        }

        if (node.left != null) {
            searchPathSum2(node.left, sum, list);
            list.remove(list.size() - 1);
        }

        if (node.right != null) {
            searchPathSum2(node.right, sum, list);
            list.remove(list.size() - 1);
        }
    }

    private Map<Integer, Integer> inOrderIndexMap;
    private int[] postorder;
    private int postIndex;

    /**
     * 从中序与后序遍历序列构造二叉树
     * 可以假设树中没有重复的元素
     * 完全没做出来，不愧是medium难度的递归
     * 这个递归很巧妙，也不好想
     */
    public TreeNode buildTree(int[] inorder, int[] postorder) {
        this.postorder = postorder;
        this.postIndex = postorder.length - 1;
        this.inOrderIndexMap = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) {
            inOrderIndexMap.put(inorder[i], i);
        }
        return buildTreeRecursive(0, inorder.length - 1);
    }

    /**
     * 递归函数
     * 用以返回当前子树的根节点
     * 
     * @param inStart 中序数组的开始下标
     * @param inEnd   中序数组的结束下标
     * @return
     */
    private TreeNode buildTreeRecursive(int inStart, int inEnd) {
        // 如果中序范围无效，返回 null
        if (inStart > inEnd) {
            return null;
        }
        // 当前子树的根节点值
        int rootValue = postorder[postIndex--];
        TreeNode root = new TreeNode(rootValue);
        // 根节点在中序遍历中的位置
        int rootIndex = inOrderIndexMap.get(rootValue);
        // 先递归构造右子树，再构造左子树
        root.right = buildTreeRecursive(rootIndex + 1, inEnd);
        root.left = buildTreeRecursive(inStart, rootIndex - 1);
        return root;
    }

    /**
     * 最大二叉树
     * 虽然是一次过了，但是击败10%
     * 呃，击败10%好像是因为我用了map
     * 我不用map了，直接返回下标，再提交，击败97.9%
     * 哈哈哈哈哈
     */
    public TreeNode constructMaximumBinaryTree(int[] nums) {
        if (nums == null) {
            return null;
        }
        return construct(nums, 0, nums.length - 1);
    }

    private TreeNode construct(int[] nums, int start, int end) {
        int index = findMax(nums, start, end);
        if (index == -1) {
            return null;
        }
        TreeNode root = new TreeNode(nums[index]);
        root.left = construct(nums, start, index - 1);
        root.right = construct(nums, index + 1, end);
        return root;
    }

    private int findMax(int[] nums, int start, int end) {
        int index = -1;
        int max = Integer.MIN_VALUE;
        for (int i = start; i <= end; i++) {
            if (nums[i] > max) {
                index = i;
                max = nums[i];
            }
        }
        return index;
    }

    /**
     * 合并二叉树
     * 过了，击败100%
     */
    public TreeNode mergeTrees(TreeNode root1, TreeNode root2) {
        return merge(root1, root2);
    }

    private TreeNode merge(TreeNode node1, TreeNode node2) {
        TreeNode node = new TreeNode();
        if (node1 == null && node2 != null) {
            node = node2;
        } else if (node2 == null && node1 != null) {
            node = node1;
        } else if (node1 != null && node2 != null) {
            node.val = node1.val + node2.val;
            node.left = merge(node1.left, node2.left);
            node.right = merge(node1.right, node2.right);
        } else {
            node = null;
        }
        return node;
    }

    /**
     * 二叉搜索树中的搜索
     * 秒了，击败100%
     */
    public TreeNode searchBST(TreeNode root, int val) {
        if (root == null) {
            return null;
        }
        if (root.val == val) {
            return root;
        }
        TreeNode res = searchBST(root.left, val);
        res = res == null ? searchBST(root.right, val) : res;
        return res;
    }

    /**
     * 二叉搜索树中的搜索
     * 利用二叉搜索树的性质
     * 递归写法
     */
    public TreeNode searchBSTWithCompareRecursion(TreeNode root, int val) {
        if (root == null) {
            return null;
        }
        if (root.val == val) {
            return root;
        } else if (root.val > val) {
            return searchBST(root.left, val);
        } else {
            return searchBST(root.right, val);
        }
    }

    /**
     * 二叉搜索树中的搜索
     * 利用二叉搜索树的性质
     * 迭代写法
     */
    public TreeNode searchBSTWithCompare(TreeNode root, int val) {
        TreeNode node = root;
        while (node != null) {
            if (node.val == val) {
                return node;
            } else if (node.val > val) {
                node = node.left;
            } else {
                node = node.right;
            }
        }
        return node;
    }

    /**
     * 验证二叉搜索树
     * 呃，是不是可以记录左子树的最大值，右子树的最小值
     * 终于通过了，击败100%，这是纯自己做出来的二叉树的Medium
     * 关键是想到，以一个节点为根的二叉树是合法的，当且仅当根节点的值大于左子树的最大值，小于右子树的最小值
     */
    public boolean isValidBST(TreeNode root) {
        return isValid(root);
    }

    private boolean isValid(TreeNode node) {
        if (node == null) {
            return true;
        }
        TreeNode cur = node.left;
        Integer lMax = null;
        while (cur != null) {
            lMax = cur.val;
            cur = cur.right;
        }

        cur = node.right;
        Integer rMin = null;
        while (cur != null) {
            rMin = cur.val;
            cur = cur.left;
        }

        if (rMin != null && node.val >= rMin) {
            return false;
        }
        if (lMax != null && node.val <= lMax) {
            return false;
        }
        return isValid(node.left) && isValid(node.right);
    }

    /**
     * 二叉搜索树的最小绝对差
     * 上题做出来了，这题就不难了，因为可以递归，每个节点算一下它与其右子树最小值和左子树最大值的差值，取其中小者
     * 秒了，但是1ms只击败了41.29%
     */
    public int getMinimumDifference(TreeNode root) {
        return minDiff(root);
    }

    private int minDiff(TreeNode node) {
        if (node == null) {
            return Integer.MAX_VALUE;
        }
        TreeNode cur = node.left;
        Integer lMax = null;
        while (cur != null) {
            lMax = cur.val;
            cur = cur.right;
        }

        cur = node.right;
        Integer rMin = null;
        while (cur != null) {
            rMin = cur.val;
            cur = cur.left;
        }

        int diff = Math.min(lMax == null ? Integer.MAX_VALUE : node.val - lMax,
                rMin == null ? Integer.MAX_VALUE : rMin - node.val);
        int subMin = Math.min(minDiff(node.left), minDiff(node.right));

        return Math.min(diff, subMin);
    }

    TreeNode pre;
    int res = Integer.MAX_VALUE;

    /**
     * 二叉搜索树的最小绝对差
     * 因为这是一个BST，所以中序遍历顺序就得到一个升序数列
     * 然后用pre来记录中序遍历的上一个节点，比较两者的值，取最小者
     */
    public int getMinimumDifferenceWithPrePointerAndInorderTraversal(TreeNode root) {
        if (root == null) { // 如果树为空，直接返回0
            return 0;
        }
        traversal(root); // 开始进行中序遍历
        return res; // 返回计算得到的最小差值
    }

    private void traversal(TreeNode root) {
        if (root == null) { // 递归终止条件：节点为空
            return;
        }
        // 左子树
        traversal(root.left); // 递归遍历左子树

        // 当前节点（中序遍历的中间位置）
        if (pre != null) { // 如果存在前一个节点（中序遍历中至少第二次访问节点）
            // 计算当前节点值与前一个节点值的差，并更新最小差值
            res = Math.min(res, root.val - pre.val);
        }
        pre = root; // 更新前一个节点为当前节点

        // 右子树
        traversal(root.right); // 递归遍历右子树
    }

    private int modeCount = 0; // 当前连续相等的数的数量
    private int threshold = 0; // 成为众数的阈值
    private Integer lastVal = null; // 记录上一个数，因为BST中序遍历相等的数总是连一起
    private List<Integer> modeList = new ArrayList<>(); // 存储结果，最后转为数组

    /**
     * 二叉搜索树中的众数
     * 微调过了，击败49.94%
     */
    public int[] findMode(TreeNode root) {
        // 依旧是中序遍历，这样相同的数都会在一块
        traversalToFindMode(root);
        return modeList.stream().mapToInt(Integer::intValue).toArray();
    }

    private void traversalToFindMode(TreeNode node) {
        if (node == null) {
            return;
        }
        traversalToFindMode(node.left);
        if (lastVal == null || lastVal.equals(node.val)) {
            modeCount++;
            if (modeCount == threshold) {
                threshold = modeCount;
                modeList.add(node.val);
            } else if (modeCount > threshold) {
                threshold = modeCount;
                modeList.clear();
                modeList.add(node.val);
            }
        } else {
            modeCount = 1;
            if (modeCount == threshold) {
                threshold = modeCount;
                modeList.add(node.val);
            }
        }
        lastVal = node.val;
        traversalToFindMode(node.right);
    }

    private int targetCount = 2;
    private TreeNode p;
    private TreeNode q;
    private boolean updated = false;

    /**
     * 二叉树的最近公共祖先
     * 层序遍历+对遍历的每个节点再进行先序遍历校验
     * 如果一开始直接采用后序遍历的话，应该也可以，而且会更快
     * 一次过，但击败5.22%
     */
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || p == null || q == null) {
            return null;
        }
        this.p = p;
        this.q = q;
        ArrayDeque<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);
        TreeNode res = null;
        while (!queue.isEmpty()) {
            int size = queue.size();
            this.updated = false;
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                this.targetCount = 2;
                if (traversalToFindTarget(node)) {
                    res = node;
                    this.updated = true;
                }
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
            if (!updated) {
                break;
            }
        }
        return res;
    }

    private boolean traversalToFindTarget(TreeNode node) {
        if (node == null) {
            return false;
        }
        if (node.val == p.val || node.val == q.val) {
            targetCount--;
            if (targetCount == 0) {
                return true;
            }
        }
        return traversalToFindTarget(node.left) || traversalToFindTarget(node.right);
    }

    /**
     * 二叉树的最近公共祖先
     * 递归解法
     * 这个递归也蛮巧妙的
     */
    public TreeNode lowestCommonAncestorWithRecursion(TreeNode root, TreeNode p, TreeNode q) {
        if (p == null || q == null) {
            return null;
        }
        this.p = p;
        this.q = q;
        return findLowestCommanAncestorNode(root);
    }

    private TreeNode findLowestCommanAncestorNode(TreeNode node) {
        if (node == null || node == this.q || node == this.p) {
            return node;
        }
        // 后序遍历
        TreeNode left = findLowestCommanAncestorNode(node.left);
        TreeNode right = findLowestCommanAncestorNode(node.right);

        // 处理中间节点，如果中间节点的左右节点都含有目标节点，那么它就是最近公共祖先
        if (left != null && right != null) {
            return node;
        }

        // 否则返回包含目标节点的一侧的节点
        return left == null ? right : left;
    }

    /**
     * 二叉搜索树的最近公共祖先
     * 用上一题的代码可以击败100%过
     * 当然用我现在的新实现也可以击败100%过
     * 就是依次去找每个目标节点，用list记录走过的路径
     * 然后得到两个list，从末尾开始，最先遇到的两个list中都有的节点就是最近公共祖先
     * 还有一个思路就是第一个遇到满足假设p.val < q.val
     * p.val < node.val < q.val的就是最近公共祖先
     */
    public TreeNode lowestCommonAncestorBST(TreeNode root, TreeNode p, TreeNode q) {
        // 如果采用后序遍历的话是否可以更快找到
        if (root == null || root == q || root == p) {
            return root;
        }
        List<TreeNode> list4P = findNode(root, p);
        List<TreeNode> list4Q = findNode(root, q);
        while (!list4P.isEmpty()) {
            TreeNode node = list4P.getLast();
            if (list4Q.contains(node)) {
                return node;
            } else {
                list4P.removeLast();
            }
        }
        return null;
    }

    private List<TreeNode> findNode(TreeNode root, TreeNode target) {
        ArrayList<TreeNode> list = new ArrayList<>();
        TreeNode node = root;
        while (node != null) {
            list.add(node);
            if (node == target) {
                break;
            } else if (node.val > target.val) {
                node = node.left;
            } else {
                node = node.right;
            }
        }
        return list;
    }

    /**
     * 二叉搜索树中的插入操作
     * 一次过，击败100%
     */
    public TreeNode insertIntoBST(TreeNode root, int val) {
        if (root == null) {
            return new TreeNode(val);
        }
        TreeNode node = root;
        TreeNode pre = root;
        while (node != null) {
            pre = node;
            if (val > node.val) {
                node = node.right;
            } else if (val < node.val) {
                node = node.left;
            }
        }
        node = new TreeNode(val);
        if (val > pre.val) {
            pre.right = node;
        } else {
            pre.left = node;
        }
        return root;
    }

    /**
     * 删除二叉搜索树中的节点
     * 迭代做法，mad，写得我烦死了
     * 最后还差一点，不想再改了，遂叫GPT改
     * 我真是个five
     */
    public TreeNode deleteNode(TreeNode root, int key) {
        // 找到要删除的节点
        TreeNode node = root;
        TreeNode pre = null;
        while (node != null) {
            if (key == node.val) {
                break;
            } else if (key > node.val) {
                pre = node;
                node = node.right;
            } else {
                pre = node;
                node = node.left;
            }
        }
        if (node == null) {
            return root;
        }

        // 要删除的节点没有左右子树
        if (node.left == null && node.right == null) {
            if (pre == null) {
                return null;
            }
            if (pre.val > node.val) {
                pre.left = null;
            } else {
                pre.right = null;
            }
        } else {
            TreeNode substitute = (node.right != null) ? getMinRightSon(node) : getMaxLeftSon(node);
            node.val = substitute.val; // 用替代节点值覆盖当前节点
            if (node.right != null) {
                node.right = deleteNode(node.right, substitute.val); // 删除替代节点
            } else {
                node.left = deleteNode(node.left, substitute.val); // 删除替代节点
            }
        }
        return root;
    }

    private TreeNode getMinRightSon(TreeNode root) {
        TreeNode node = root.right;
        while (node != null && node.left != null) {
            node = node.left;
        }
        return node;
    }

    private TreeNode getMaxLeftSon(TreeNode root) {
        TreeNode node = root.left;
        while (node != null && node.right != null) {
            node = node.right;
        }
        return node;
    }

    /**
     * 删除二叉搜索树中的节点
     * 巧妙地用了递归
     */
    public TreeNode deleteNodeWithRecursion(TreeNode root, int key) {
        if (root == null) {
            return null;
        }

        if (key < root.val) {
            // 递归在左子树中删除节点
            root.left = deleteNode(root.left, key);
        } else if (key > root.val) {
            // 递归在右子树中删除节点
            root.right = deleteNode(root.right, key);
        } else {
            // 找到目标节点
            if (root.left == null && root.right == null) {
                // 目标节点是叶子节点，直接删除
                return null;
            } else if (root.right == null) {
                // 目标节点只有左子树，返回左子树作为新的子树
                return root.left;
            } else if (root.left == null) {
                // 目标节点只有右子树，返回右子树作为新的子树
                return root.right;
            } else {
                // 目标节点有左右子树，用右子树的最小节点替代
                TreeNode minNode = getMin(root.right);
                root.val = minNode.val; // 替换当前节点值
                // 删除右子树中的最小节点
                root.right = deleteNode(root.right, minNode.val);
            }
        }
        return root;
    }

    /**
     * 获取以 root 为根的树中最小节点
     */
    private TreeNode getMin(TreeNode root) {
        while (root.left != null) {
            root = root.left;
        }
        return root;
    }

    /**
     * 修剪二叉搜索树
     */
    public TreeNode trimBST(TreeNode root, int low, int high) {

    }
}
