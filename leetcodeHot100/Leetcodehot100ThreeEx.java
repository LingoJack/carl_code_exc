package leetcodeHot100;

import java.lang.foreign.GroupLayout;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class Leetcodehot100ThreeEx {

    /**
     * 两数之和
     */
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(target - nums[i])) {
                return new int[] { map.get(target - nums[i]), i };
            }
            map.put(nums[i], i);
        }
        return null;
    }

    /**
     * 字母异位词分组
     */
    public List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> res = new HashMap<>();
        for (String s : strs) {
            char[] chs = s.toCharArray();
            Arrays.sort(chs);
            String sorted = new String(chs);
            res.putIfAbsent(sorted, new ArrayList<>());
            res.get(sorted).add(s);
        }
        return new ArrayList<>(res.values());
    }

    /**
     * 最长连续序列
     */
    public int longestConsecutive(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int num : nums) {
            set.add(num);
        }
        int res = 0;
        int count = 1;
        for (int num : set) {
            // 如果不是序列中最小的，就不往后查找
            if (set.contains(num - 1)) {
                continue;
            }
            while (set.contains(num + 1)) {
                num++;
                count++;
            }
            res = Math.max(res, count);
            count = 1;
        }
        return res;
    }

    /**
     * 移动零
     */
    public void moveZeroes(int[] nums) {
        int fast = 0;
        int slow = 0;
        int len = nums.length;
        while (fast < len) {
            if (nums[fast] != 0) {
                swap(nums, fast, slow);
                slow++;
            }
            fast++;
        }
    }

    private void swap(int[] nums, int a, int b) {
        int t = nums[a];
        nums[a] = nums[b];
        nums[b] = t;
    }

    /**
     * 删除链表中的重复元素II
     */
    public ListNode deleteDuplicates(ListNode head) {
        // 1 2 3 3 4 4 5
        // _ 1 2 3 3 4 4 5 5
        // 1 1
        // n
        // 1 2 2
        // n
        ListNode dummy = new ListNode(0);
        ListNode last = dummy;
        ListNode node = head;
        while (node != null) {
            if (node.next == null || node.next.val != node.val) {
                ListNode newNode = new ListNode(node.val);
                last.next = newNode;
                last = newNode;
            } else {
                while (node != null && ((node.next != null && node.val == node.next.val) || node.next == null)) {
                    node = node.next;
                }
            }
            if (node != null) {
                node = node.next;
            }
        }
        return dummy.next;
    }

    /**
     * 最大交换
     * ac 本题的关键是利用后缀最大值表和倒排索引
     */
    public int maximumSwap(int num) {
        List<Integer> list = new ArrayList<>();
        while (num > 0) {
            list.add(0, num % 10);
            num /= 10;
        }
        // 2 7 3 6
        // s
        // f
        int max = Integer.MIN_VALUE;
        int[] suffixMax = new int[list.size()];
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            map.put(list.get(i), i);
            suffixMax[list.size() - 1 - i] = Math.max(i == 0 ? Integer.MIN_VALUE : suffixMax[list.size() - i],
                    list.get(list.size() - 1 - i));
        }
        for (int i = 0; i < list.size(); i++) {
            max = suffixMax[i];
            if (!list.get(i).equals(max)) {
                if (map.get(max) > i) {
                    swap(list, i, map.get(max));
                    break;
                }
            }
        }
        int sum = 0;
        int power = 0;
        int index = list.size() - 1;
        while (index >= 0) {
            sum += list.get(index--) * Math.pow(10, power++);
        }
        return sum;
    }

    private void swap(List<Integer> list, int idx1, int idx2) {
        int t = list.get(idx1);
        list.set(idx1, list.get(idx2));
        list.set(idx2, t);
    }

    /**
     * 盛最多水的容器
     */
    public int maxArea(int[] height) {
        int len = height.length;
        int lt = 0;
        int rt = len - 1;
        int max = 0;
        while (rt > lt) {
            int h = Math.min(height[lt], height[rt]);
            int w = rt - lt;
            max = Math.max(max, w * h);
            if (height[rt] < height[lt]) {
                rt--;
            } else {
                lt++;
            }
        }
        return max;
    }

    /**
     * 三数之和
     */
    public List<List<Integer>> threeSum(int[] nums) {
        // -1,0,1,2,-1,-4
        // -4 -1 -1 0 1 2
        // i l r
        Arrays.sort(nums);
        int len = nums.length;
        List<List<Integer>> res = new ArrayList<>();
        for (int i = 0; i <= len - 2; i++) {
            if (i >= 1 && nums[i] == nums[i - 1]) {
                continue;
            }
            int target = -nums[i];
            int lt = i + 1;
            int rt = len - 1;
            while (lt < rt) {
                int sum = nums[lt] + nums[rt];
                if (sum > target) {
                    rt--;
                    while (lt < rt && nums[rt] == nums[rt + 1]) {
                        rt--;
                    }
                } else if (sum < target) {
                    lt++;
                    while (lt < rt && nums[lt] == nums[lt - 1]) {
                        lt++;
                    }
                } else {
                    res.add(List.of(nums[i], nums[lt], nums[rt]));
                    lt++;
                    while (lt < rt && nums[lt] == nums[lt - 1]) {
                        lt++;
                    }
                }
            }
        }
        return res;
    }

    /**
     * 接雨水
     */
    public int trap(int[] height) {
        int area = 0;
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 0; i < height.length; i++) {
            while (!stack.isEmpty() && height[stack.peek()] < height[i]) {
                int low = height[stack.pop()];
                int h = stack.isEmpty() ? 0 : Math.min(height[i], height[stack.peek()]) - low;
                int w = stack.isEmpty() ? 0 : i - stack.peek() - 1;
                area += w * h;
            }
            stack.push(i);
        }
        return area;
    }

    /**
     * 无重复字符的最长子串
     * 之前都对了，这次反倒没做出来
     * 因为我看了滑动窗口的框架反倒错了???
     * 不过稍微改一下又对了，还是按照模板来写
     */
    public int lengthOfLongestSubstring(String s) {
        char[] str = s.toCharArray();
        int lt = 0;
        int rt = 0;
        int max = 0;
        int len = s.length();
        Map<Character, Boolean> exist = new HashMap<>();
        while (rt < len) {
            exist.put(str[rt], true);
            max = Math.max(max, rt - lt + 1);
            rt++;
            while (rt < len && exist.getOrDefault(str[rt], false)) {
                exist.put(str[lt++], false);
            }
        }
        return max;
    }

    /**
     * 找到字符串中所有字母异位词
     * 滑动窗口我很不熟
     * 没做出来
     * 操，但好像是我把这题和最小覆盖子串那题记混了
     * 不过都是用滑动窗口来做的，之前我的做法十分的混乱
     * 而实际上是有方法论的（其中rt和lt更新的逻辑顺序，需要根据实际情况而定）
     * 扩大后的效果 -> 判断是否加入结果集 -> rt++ -> 缩小后的效果 -> lt++
     */
    public List<Integer> findAnagrams(String s, String p) {
        Map<Character, Integer> need = new HashMap<>();
        Map<Character, Integer> contain = new HashMap<>();
        for (char c : p.toCharArray()) {
            need.put(c, need.getOrDefault(c, 0) + 1);
        }
        int valid = 0;
        int lt = 0, rt = 0;
        List<Integer> list = new ArrayList<>();
        while (rt < s.length()) {
            // 扩大
            char rc = s.charAt(rt);
            if (need.containsKey(rc)) {
                contain.put(rc, contain.getOrDefault(rc, 0) + 1);
                if (contain.get(rc).equals(need.get(rc))) {
                    valid++;
                }
                if (valid == need.size()) {
                    list.add(lt);
                }
            }
            rt++;
            // 缩小
            while (rt - lt + 1 > p.length()) {
                char lc = s.charAt(lt);
                if (need.containsKey(lc)) {
                    if (contain.get(lc).equals(need.get(lc))) {
                        valid--;
                    }
                    contain.put(lc, contain.get(lc) - 1);
                }
                lt++;
            }
        }
        return list;
    }

    /**
     * 滑动窗口的最大值
     */
    public int[] maxSlidingWindow(int[] nums, int k) {
        // 1,3,-1,-3,5,3,6,7
        // 3 -1 -3 5
        Deque<Integer> queue = new ArrayDeque<>();
        int len = nums.length;
        int[] res = new int[len - k + 1];
        for (int i = 0; i < len; i++) {
            while (!queue.isEmpty() && nums[queue.peekLast()] < nums[i]) {
                queue.pollLast();
            }
            queue.offerLast(i);
            if (i >= k - 1) {
                while (queue.peekFirst() < i - k + 1) {
                    queue.pollFirst();
                }
                res[i - k + 1] = nums[queue.peekFirst()];
            }
        }
        return res;
    }

    /**
     * 最小覆盖子串
     */
    public String minWindow(String s, String t) {
        Map<Character, Integer> need = new HashMap<>();
        Map<Character, Integer> contain = new HashMap<>();
        for (char c : t.toCharArray()) {
            need.putIfAbsent(c, 0);
            need.put(c, need.get(c) + 1);
        }
        int lt = 0;
        int rt = 0;
        int valid = 0;
        int minLen = s.length() + 1;
        int start = -1;
        while (rt < s.length()) {
            char rc = s.charAt(rt);
            if (need.containsKey(rc)) {
                contain.put(rc, contain.getOrDefault(rc, 0) + 1);
                if (need.get(rc).equals(contain.get(rc))) {
                    valid++;
                }
            }
            while (valid >= need.size()) {
                if (minLen >= rt - lt + 1) {
                    minLen = rt - lt + 1;
                    start = lt;
                }
                char lc = s.charAt(lt);
                if (need.containsKey(lc)) {
                    if (need.get(lc).equals(contain.get(lc))) {
                        valid--;
                    }
                    contain.put(lc, contain.get(lc) - 1);
                }
                lt++;
            }
            rt++;
        }
        return start == -1 ? "" : s.substring(start, Math.min(s.length(), start + minLen));
    }

    /**
     * 最大子数组和
     */
    public int maxSubArray(int[] nums) {
        int sum = 0;
        int res = nums[0];
        // -2,1,-3,4,-1,2,1,-5,4
        for (int i = 0; i < nums.length; i++) {
            if (sum < 0) {
                sum = 0;
            }
            sum += nums[i];
            res = Math.max(res, sum);
        }
        return res;
    }

    /**
     * 合并区间
     */
    public int[][] merge(int[][] intervals) {
        // [1,3],[2,6],[8,10],[15,18]
        Arrays.sort(intervals, (a, b) -> {
            return Integer.compare(a[0], b[0]) == 0 ? Integer.compare(a[1], b[1]) : Integer.compare(a[0], b[0]);
        });
        Deque<int[]> stack = new ArrayDeque<>();
        for (int[] interval : intervals) {
            if (stack.isEmpty()) {
                stack.push(interval);
                continue;
            }
            int[] last = stack.peek();
            if (interval[0] <= last[1]) {
                stack.pop();
                stack.push(new int[] { last[0], Math.max(interval[1], last[1]) });
            } else {
                stack.push(interval);
            }
        }
        int[][] res = new int[stack.size()][2];
        int size = stack.size();
        for (int i = 0; i < size; i++) {
            res[i] = stack.pop();
        }
        return res;
    }

    /**
     * 轮转数组
     */
    public void rotate(int[] nums, int k) {
        k %= nums.length;
        // 1,2,3,4,5,6,7，k=3
        // 5,6,7,1,2,3,4
        reverse(nums, 0, nums.length - 1);
        reverse(nums, 0, k - 1);
        reverse(nums, k, nums.length - 1);
    }

    private void reverse(int[] nums, int start, int end) {
        int lt = start;
        int rt = end;
        while (rt > lt) {
            swap(nums, lt, rt);
            lt++;
            rt--;
        }
    }

    /**
     * 除自身以外数组的乘积
     */
    public int[] productExceptSelf(int[] nums) {
        int len = nums.length;
        int[] res = new int[len];
        Arrays.fill(res, 1);
        int prefixProd = 1;
        for (int i = 1; i < len; i++) {
            prefixProd *= nums[i - 1];
            res[i] = prefixProd;
        }
        int suffixProd = 1;
        for (int i = len - 2; i >= 0; i--) {
            suffixProd *= nums[i + 1];
            res[i] *= suffixProd;
        }
        return res;
    }

    /**
     * 缺失的第一个正数
     */
    public int firstMissingPositive(int[] nums) {
        boolean[] exist = new boolean[nums.length + 2];
        for (int num : nums) {
            if (num > 0 && num <= nums.length) {
                exist[num] = true;
            }
        }
        for (int i = 1; i < nums.length + 2; i++) {
            if (!exist[i]) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 矩阵置零
     */
    public void setZeroes(int[][] matrix) {
        int row = matrix.length;
        int col = matrix[0].length;
        boolean[] rowSetZero = new boolean[row];
        boolean[] colSetZero = new boolean[col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (matrix[i][j] == 0) {
                    rowSetZero[i] = true;
                    colSetZero[j] = true;
                }
            }
        }
        for (int i = 0; i < row; i++) {
            if (rowSetZero[i]) {
                for (int j = 0; j < col; j++) {
                    matrix[i][j] = 0;
                }
            }
        }
        for (int j = 0; j < col; j++) {
            if (colSetZero[j]) {
                for (int i = 0; i < row; i++) {
                    matrix[i][j] = 0;
                }
            }
        }
    }

    /**
     * 螺旋矩阵
     * 差点没做出来
     * 第一次采用简化的做法写出来了，
     * 要注意的是list.size() < row * col要在四个方向的for上都作为条件
     */
    public List<Integer> spiralOrder(int[][] matrix) {
        int row = matrix.length;
        int col = matrix[0].length;
        int rowIndex = 0;
        int colIndex = 0;
        int colUpperBound = col - 1;
        int rowUpperBound = row - 1;
        int rowLowerBound = 0;
        int colLowerBound = 0;
        List<Integer> list = new ArrayList<>();
        while (list.size() < row * col) {
            for (int i = colLowerBound; i <= colUpperBound && list.size() < row * col; i++) {
                list.add(matrix[rowIndex][i]);
                colIndex = i;
            }
            rowLowerBound++;
            for (int i = rowLowerBound; i <= rowUpperBound && list.size() < row * col; i++) {
                list.add(matrix[i][colIndex]);
                rowIndex = i;
            }
            colUpperBound--;
            for (int i = colUpperBound; i >= colLowerBound && list.size() < row * col; i--) {
                list.add(matrix[rowIndex][i]);
                colIndex = i;
            }
            rowUpperBound--;
            for (int i = rowUpperBound; i >= rowLowerBound && list.size() < row * col; i--) {
                list.add(matrix[i][colIndex]);
                rowIndex = i;
            }
            colLowerBound++;
        }
        return list;
    }

    /**
     * 旋转图像
     */
    public void rotate(int[][] matrix) {
        int row = matrix.length;
        int col = matrix[0].length;
        // 先上下翻转
        for (int j = 0; j < col; j++) {
            for (int i = 0; i < row / 2; i++) {
                int t = matrix[i][j];
                matrix[i][j] = matrix[row - 1 - i][j];
                matrix[row - 1 - i][j] = t;
            }
        }
        for (int i = 0; i < row; i++) {
            for (int j = 0; j <= i; j++) {
                int t = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = t;
            }
        }
    }

    /**
     * 搜索二维矩阵II
     */
    public boolean searchMatrixII(int[][] matrix, int target) {
        int row = matrix.length;
        int col = matrix[0].length;
        int i = 0, j = col - 1;
        while (i >= 0 && i < row && j >= 0 && j < col) {
            int num = matrix[i][j];
            if (num < target) {
                i++;
            } else if (num > target) {
                j--;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * 相交链表
     */
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        int l1 = 0;
        int l2 = 0;
        ListNode aNode = headA;
        ListNode bNode = headB;
        while (aNode != null) {
            aNode = aNode.next;
            l1++;
        }
        while (bNode != null) {
            bNode = bNode.next;
            l2++;
        }
        aNode = headA;
        bNode = headB;
        if (l2 > l1) {
            int diff = l2 - l1;
            for (int i = 0; i < diff; i++) {
                bNode = bNode.next;
            }
            while (bNode != null && aNode != null) {
                if (aNode == bNode) {
                    return aNode;
                }
                bNode = bNode.next;
                aNode = aNode.next;
            }
        } else {
            int diff = l1 - l2;
            for (int i = 0; i < diff; i++) {
                aNode = aNode.next;
            }
            while (bNode != null && aNode != null) {
                if (aNode == bNode) {
                    return aNode;
                }
                bNode = bNode.next;
                aNode = aNode.next;
            }
        }
        return null;
    }

    /**
     * 反转链表
     */
    public ListNode reverseList(ListNode head) {
        ListNode node = head;
        ListNode last = null;
        while (node != null) {
            ListNode next = node.next;
            node.next = last;
            last = node;
            node = next;
        }
        return last;
    }

    /**
     * 回文链表
     * 做出来了，但还不是最优的解法，之前的做法更快，就是反转链表
     */
    public boolean isPalindrome(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;
        // 1 2 2 1
        // s
        // f
        Deque<Integer> stack = new ArrayDeque<>();
        while (fast != null && fast.next != null) {
            stack.push(slow.val);
            fast = fast.next.next;
            slow = slow.next;
        }
        ListNode node = fast == null ? slow : slow.next;
        while (node != null) {
            if (!stack.isEmpty() && node.val != stack.pop()) {
                return false;
            }
            node = node.next;
        }
        return stack.isEmpty() && node == null;
    }

    /**
     * 环形链表
     */
    public boolean hasCycle(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
            if (fast == slow) {
                return true;
            }
        }
        return false;
    }

    /**
     * 环形链表II
     */
    public ListNode detectCycle(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
            if (fast == slow) {
                ListNode node = head;
                while (node != slow) {
                    node = node.next;
                    slow = slow.next;
                }
                return slow;
            }
        }
        return null;
    }

    /**
     * 合并两个有序链表
     */
    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        ListNode node1 = list1, node2 = list2;
        ListNode dummy = new ListNode();
        ListNode last = dummy;
        while (node1 != null && node2 != null) {
            if (node1.val < node2.val) {
                last.next = node1;
                last = last.next;
                node1 = node1.next;
            } else {
                last.next = node2;
                last = last.next;
                node2 = node2.next;
            }
        }
        last.next = node1 == null ? node2 : node1;
        return dummy.next;
    }

    /**
     * 两数相加
     */
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode n1 = l1;
        ListNode n2 = l2;
        int carry = 0;
        ListNode dummy = new ListNode();
        ListNode last = dummy;
        while (n1 != null || n2 != null || carry != 0) {
            int sum = (n1 == null ? 0 : n1.val) + (n2 == null ? 0 : n2.val) + carry;
            carry = sum / 10;
            sum = sum % 10;
            ListNode node = new ListNode(sum);
            last.next = node;
            last = node;
            if (n1 != null) {
                n1 = n1.next;
            }
            if (n2 != null) {
                n2 = n2.next;
            }
        }
        return dummy.next;
    }

    /**
     * 删除链表的倒数第N个结点
     */
    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode dummy = new ListNode(0, head);
        ListNode slow = head, fast = head;
        int count = 0;
        while (fast != null) {
            fast = fast.next;
            count++;
        }
        ListNode last = dummy;
        while (count > n) {
            count--;
            last = slow;
            slow = slow.next;
        }
        last.next = slow.next;
        slow.next = null;
        return dummy.next;
    }

    /**
     * 两两交换链表中的节点
     */
    public ListNode swapPairs(ListNode head) {
        ListNode dummy = new ListNode(0, head);
        ListNode groupStart = head;
        ListNode prevGroupEnd = dummy;
        while (groupStart != null) {
            ListNode fast = groupStart;
            ListNode slow = groupStart;
            int count = 2;
            while (count > 0 && fast != null) {
                fast = fast.next;
                count--;
            }
            if (count > 0) {
                break;
            }
            ListNode nextGroupStart = fast;
            // 1 2 3 4
            // s
            // f
            ListNode last = null;
            ListNode node = slow;
            while (node != nextGroupStart) {
                ListNode next = node.next;
                node.next = last;
                last = node;
                node = next;
            }
            groupStart.next = nextGroupStart;
            prevGroupEnd.next = last;
            prevGroupEnd = groupStart;
            groupStart = nextGroupStart;
        }
        return dummy.next;
    }

    /**
     * K个一组反转链表
     */
    public ListNode reverseKGroup(ListNode head, int k) {
        ListNode dummy = new ListNode(0, head);
        ListNode groupStart = head;
        ListNode prevGroupEnd = dummy;
        while (groupStart != null) {
            ListNode fast = groupStart;
            ListNode slow = groupStart;
            int count = k;
            while (count > 0 && fast != null) {
                fast = fast.next;
                count--;
            }
            if (count > 0) {
                break;
            }
            ListNode nextGroupStart = fast;
            // 1 2 3 4
            // s
            // f
            ListNode last = null;
            ListNode node = slow;
            while (node != nextGroupStart) {
                ListNode next = node.next;
                node.next = last;
                last = node;
                node = next;
            }
            groupStart.next = nextGroupStart;
            prevGroupEnd.next = last;
            prevGroupEnd = groupStart;
            groupStart = nextGroupStart;
        }
        return dummy.next;
    }

    class Node {
        int val;
        Node next;
        Node random;

        public Node(int val) {
            this.val = val;
            this.next = null;
            this.random = null;
        }
    }

    /**
     * 随机链表的复制
     */
    public Node copyRandomList(Node head) {
        Map<Node, Node> map = new HashMap<>();
        Node node = head;
        Node dummy = new Node(0);
        Node last = dummy;
        while (node != null) {
            Node newNode = new Node(node.val);
            last.next = newNode;
            last = newNode;
            map.put(node, newNode);
            node = node.next;
        }
        node = head;
        while (node != null) {
            Node newNode = map.get(node);
            newNode.random = map.get(node.random);
            node = node.next;
        }
        return dummy.next;
    }

    /**
     * 排序链表
     */
    public ListNode sortList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode[] twoPartHeads = spilt(head);
        ListNode ltPart = sortList(twoPartHeads[0]);
        ListNode rtPart = sortList(twoPartHeads[1]);
        ListNode dummy = new ListNode();
        ListNode last = dummy;
        while (ltPart != null && rtPart != null) {
            if (ltPart.val < rtPart.val) {
                last.next = ltPart;
                last = ltPart;
                ltPart = ltPart.next;
            } else {
                last.next = rtPart;
                last = rtPart;
                rtPart = rtPart.next;
            }
        }
        last.next = rtPart == null ? ltPart : rtPart;
        return dummy.next;
    }

    private ListNode[] spilt(ListNode head) {
        // 1 4 4 1
        // s
        // f
        // 1 2 3
        // s
        // f
        ListNode slow = head, fast = head;
        while (fast.next != null && fast.next.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }
        ListNode nextHead = slow.next;
        slow.next = null;
        return new ListNode[] { head, nextHead };
    }

    /**
     * 合并K个升序链表
     */
    public ListNode mergeKLists(ListNode[] lists) {
        MyPriorityQueue priorityQueue = new MyPriorityQueue();
        for (ListNode node : lists) {
            if (node != null) {
                priorityQueue.offer(node);
            }
        }
        ListNode dummy = new ListNode();
        ListNode last = dummy;
        while (!priorityQueue.isEmpty()) {
            ListNode node = priorityQueue.poll();
            if (node.next != null) {
                priorityQueue.offer(node.next);
            }
            last.next = node;
            last = last.next;
        }
        return dummy.next;
    }

    public class MyPriorityQueue {
        List<ListNode> list = new ArrayList<>();

        public void offer(ListNode node) {
            if (node == null) {
                return;
            }
            list.add(node);
            upCheck(list.size() - 1);
        }

        private void upCheck(int index) {
            // 0 1 2 3 4
            int parent = (index - 1) / 2;
            if (list.get(parent).val > list.get(index).val) {
                swap(parent, index);
                upCheck(parent);
            }
        }

        private void downCheck(int index) {
            int lt = 2 * index + 1;
            int rt = 2 * index + 2;
            int min = index;
            if (lt < list.size() && list.get(min).val > list.get(lt).val) {
                min = lt;
            }
            if (rt < list.size() && list.get(min).val > list.get(rt).val) {
                min = rt;
            }
            if (min != index) {
                swap(min, index);
                downCheck(min);
            }
        }

        public ListNode poll() {
            swap(0, list.size() - 1);
            ListNode res = list.remove(list.size() - 1);
            downCheck(0);
            return res;
        }

        private void swap(int idx1, int idx2) {
            ListNode t = list.get(idx1);
            list.set(idx1, list.get(idx2));
            list.set(idx2, t);
        }

        public boolean isEmpty() {
            return list.isEmpty();
        }
    }

    /**
     * LRU缓存
     */
    public class LRUCache {

        public class Node {
            int key;
            int val;
            Node prev;
            Node next;

            public Node(int key, int val) {
                this.key = key;
                this.val = val;
                this.prev = null;
                this.next = null;
            }

            public Node() {

            }
        }

        private Node head;

        private Node tail;

        private Map<Integer, Node> map;

        private int capacity;

        public LRUCache(int capacity) {
            this.capacity = capacity;
            this.head = new Node();
            this.tail = new Node();
            this.head.next = this.tail;
            this.tail.prev = this.head;
            this.map = new HashMap<>();
        }

        public int get(int key) {
            if (!map.containsKey(key)) {
                return -1;
            }
            Node node = map.get(key);
            node.prev.next = node.next;
            node.next.prev = node.prev;
            node.next = head.next;
            head.next.prev = node;
            head.next = node;
            node.prev = head;
            return node.val;
        }

        public void put(int key, int val) {
            Node node = map.get(key);
            if (node == null) {
                if (map.size() >= capacity) {
                    Node removedNode = tail.prev;
                    tail.prev = removedNode.prev;
                    removedNode.prev.next = tail;
                    removedNode.prev = null;
                    removedNode.next = null;
                    map.remove(removedNode.key);
                }
                node = new Node(key, val);
                node.prev = head;
                node.next = head.next;
                head.next.prev = node;
                head.next = node;
                map.put(key, node);
            } else {
                if (node.val != val) {
                    node.val = val;
                }
                get(key);
            }
        }
    }

    /**
     * 二叉树的中序遍历
     */
    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        inorder(list, root);
        return list;
    }

    private void inorder(List<Integer> list, TreeNode node) {
        if (node == null) {
            return;
        }
        inorder(list, node.left);
        list.add(node.val);
        inorder(list, node.right);
    }

    /**
     * 二叉树的最大深度
     */
    public int maxDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        return Math.max(maxDepth(root.left), maxDepth(root.right)) + 1;
    }

    /**
     * 翻转二叉树
     */
    public TreeNode invertTree(TreeNode root) {
        invert(root);
        return root;
    }

    private void invert(TreeNode node) {
        if (node == null) {
            return;
        }
        TreeNode rt = node.right;
        node.right = node.left;
        node.left = rt;
        invert(node.right);
        invert(node.left);
    }

    /**
     * 对称二叉树
     */
    public boolean isSymmetric(TreeNode root) {
        if (root == null) {
            return true;
        }
        return check(root.left, root.right);
    }

    private boolean check(TreeNode lt, TreeNode rt) {
        if (rt == null || lt == null) {
            return rt == null && lt == null;
        }
        if (rt.val != lt.val) {
            return false;
        }
        return check(lt.left, rt.right) && check(lt.right, rt.left);
    }

    /**
     * 二叉树的直径
     */
    public int diameterOfBinaryTree(TreeNode root) {
        height(root);
        return maxPath;
    }

    private int maxPath = Integer.MIN_VALUE;

    private int height(TreeNode node) {
        if (node == null) {
            return 0;
        }
        int lt = height(node.left);
        int rt = height(node.right);
        maxPath = Math.max(maxPath, lt + rt);
        return Math.max(lt, rt) + 1;
    }

    /**
     * 二叉树的层序遍历
     */
    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) {
            return res;
        }
        Deque<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                list.add(node.val);
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
            res.add(list);
        }
        return res;
    }

    /**
     * 将有序数组转换为二叉树
     */
    public TreeNode sortedArrayToBST(int[] nums) {
        return build(nums, 0, nums.length - 1);
    }

    private TreeNode build(int[] nums, int start, int end) {
        if (end < start) {
            return null;
        }
        int mid = (start + end) >> 1;
        TreeNode root = new TreeNode(nums[mid]);
        root.left = build(nums, start, mid - 1);
        root.right = build(nums, mid + 1, end);
        return root;
    }

    /**
     * 验证二叉搜索树
     */
    public boolean isValidBST(TreeNode root) {
        return valid(root);
    }

    private boolean valid(TreeNode node) {
        if (node == null) {
            return true;
        }
        TreeNode lt = node.left;
        while (lt != null && lt.right != null) {
            lt = lt.right;
        }
        if (lt != null && node.val <= lt.val) {
            return false;
        }
        TreeNode rt = node.right;
        while (rt != null && rt.left != null) {
            rt = rt.left;
        }
        if (rt != null && node.val >= rt.val) {
            return false;
        }
        return valid(node.left) && valid(node.right);
    }

    /**
     * 三个数的最大乘积
     */
    public int maximumProduct(int[] nums) {
        int k = 3;
        Arrays.sort(nums);
        int len = nums.length;
        boolean[] exist = new boolean[len];
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            if (!exist[i]) {
                list.add(nums[i]);
                exist[i] = true;
            }
            if (!exist[len - 1 - i]) {
                list.add(nums[len - i - 1]);
                exist[len - i - 1] = true;
            }
        }
        dfs(list, k, 0, 0, 1);
        return maxProd;
    }

    private int maxProd = Integer.MIN_VALUE;

    private void dfs(List<Integer> list, int k, int count, int index, int prod) {
        if (index >= list.size() || count >= k) {
            if (count == k) {
                maxProd = Math.max(maxProd, prod);
            }
            return;
        }
        dfs(list, k, count, index + 1, prod);
        int num = list.get(index);
        int prev = prod;
        prod *= num;
        dfs(list, k, count + 1, index + 1, prod);
        prod = prev;
    }

    /**
     * 二叉搜索树中第K小的元素
     */
    public int kthSmallest(TreeNode root, int k) {
        this.k = k;
        inorder(root);
        return res;
    }

    private int count = 0;

    private int k;

    private int res;

    private void inorder(TreeNode node) {
        if (node == null || count > k) {
            return;
        }
        inorder(node.left);
        count++;
        if (count == k) {
            res = node.val;
        }
        inorder(node.right);
    }

    /**
     * 二叉树的右视图
     */
    public List<Integer> rightSideView(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) {
            return res;
        }
        Deque<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                if (i == size - 1) {
                    res.add(node.val);
                }
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
        }
        return res;
    }

    /**
     * 二叉树展开为链表
     */
    public void flatten(TreeNode root) {
        TreeNode node = root;
        while (node != null) {
            if (node.left != null) {
                TreeNode lt = node.left;
                node.left = null;
                TreeNode rt = node.right;
                TreeNode t = lt;
                while (t != null && t.right != null) {
                    t = t.right;
                }
                t.right = rt;
                node.right = lt;
                t.right = rt;
            }
            node = node.right;
        }
    }

    /**
     * 从前序和中序遍历构造二叉树
     */
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) {
            map.put(inorder[i], i);
        }
        return build(preorder, inorder, map, 0, inorder.length - 1);
    }

    private int preorderIndex = 0;

    private TreeNode build(int[] preorder, int[] inorder, Map<Integer, Integer> map, int start, int end) {
        if (end < start) {
            return null;
        }
        int val = preorder[preorderIndex++];
        int idx = map.get(val);
        TreeNode node = new TreeNode(val);
        node.left = build(preorder, inorder, map, start, idx - 1);
        node.right = build(preorder, inorder, map, idx + 1, end);
        return node;
    }

    /**
     * 路径总和III
     * 做出来了，但不是最优的解法，最优的解法是前缀和
     */
    public int pathSum(TreeNode root, long targetSum) {
        if (root == null) {
            return res;
        }
        Deque<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                dfs(node, targetSum);
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
        }
        return pathNum;
    }

    private int pathNum = 0;

    private void dfs(TreeNode node, long target) {
        if (node == null) {
            return;
        }
        if (target == node.val) {
            pathNum++;
        }
        dfs(node.left, target - node.val);
        dfs(node.right, target - node.val);
    }

    /**
     * 路径总和III
     * 核心是用Map记录
     * 前缀和为i的路径数量j
     * 没做出来，没搞清楚最后一步的回溯的实际意义
     * 这个回溯是说，如果没有选择这个节点，那么其对应的前缀和路径就不应该加上
     * 比如[1,-2,-3]这个用例，如果不回溯就会在计算右子树的时候加上了左子树的路径
     */
    public int pathSumWithPrefixSumBacktracking(TreeNode root, long targetSum) {
        Map<Long, Integer> prefixSumCountMap = new HashMap<>();
        prefixSumCountMap.put(0l, 1);
        dfs(root, prefixSumCountMap, 0, targetSum);
        return pathNum;
    }

    private void dfs(TreeNode node, Map<Long, Integer> prefixSumCountMap, long prefixSum, long target) {
        if (node == null) {
            return;
        }
        prefixSum += node.val;
        if (prefixSumCountMap.containsKey(prefixSum - target)) {
            pathNum += prefixSumCountMap.get(prefixSum - target);
        }
        prefixSumCountMap.put(prefixSum, prefixSumCountMap.getOrDefault(prefixSum, 0) + 1);
        dfs(node.left, prefixSumCountMap, prefixSum, target);
        dfs(node.right, prefixSumCountMap, prefixSum, target);
        prefixSumCountMap.put(prefixSum, prefixSumCountMap.get(prefixSum) - 1);
    }

    /**
     * 二叉树的最近公共祖先
     */
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (dfs(p, q)) {
            return p;
        }
        if (dfs(q, p)) {
            return q;
        }
        TreeNode res = null;
        if (root == null) {
            return res;
        }
        Deque<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                if (dfs(node, p) && dfs(node, q)) {
                    res = node;
                }
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
        }
        return res;
    }

    private boolean dfs(TreeNode node, TreeNode target) {
        if (node == null) {
            return false;
        }
        if (node == target) {
            return true;
        }
        return dfs(node.left, target) || dfs(node.right, target);
    }

    /**
     * 二叉树中的最大路径和
     */
    public int maxPathSum(TreeNode root) {
        dfs(root);
        return maxPathSum;
    }

    private int maxPathSum = Integer.MIN_VALUE;

    private int dfs(TreeNode node) {
        if (node == null) {
            return 0;
        }
        int lt = dfs(node.left);
        int rt = dfs(node.right);
        maxPathSum = Math.max(Math.max(0, lt) + Math.max(0, rt) + node.val, maxPathSum);
        return Math.max(0, Math.max(lt, rt)) + node.val;
    }

    /**
     * 岛屿数量
     */
    public int numIslands(char[][] grid) {
        int row = grid.length;
        int col = grid[0].length;
        boolean[][] visited = new boolean[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if ((!visited[i][j]) && grid[i][j] == '1') {
                    islandCount++;
                    dfs(grid, visited, i, j);
                }
            }
        }
        return islandCount;
    }

    private int islandCount = 0;

    private void dfs(char[][] grid, boolean[][] visited, int i, int j) {
        int row = grid.length;
        int col = grid[0].length;
        if (!(i >= 0 && i < row && j >= 0 && j < col)) {
            return;
        }
        if (!visited[i][j] && grid[i][j] == '1') {
            visited[i][j] = true;
            dfs(grid, visited, i + 1, j);
            dfs(grid, visited, i, j + 1);
            dfs(grid, visited, i, j - 1);
            dfs(grid, visited, i - 1, j);
        }
    }

    /**
     * 腐烂的橘子
     */
    public int orangesRotting(int[][] grid) {
        Deque<int[]> queue = new ArrayDeque<>();
        int fresh = 0;
        int row = grid.length, col = grid[0].length;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (grid[i][j] == 1) {
                    fresh++;
                } else if (grid[i][j] == 2) {
                    queue.offer(new int[] { i, j });
                }
            }
        }
        if (fresh == 0) {
            return 0;
        }
        int minute = 1;
        while (!queue.isEmpty() && fresh > 0) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int[] index = queue.poll();
                int rowIndex = index[0], colIndex = index[1];
                if (valid(row, col, rowIndex - 1, colIndex) && grid[rowIndex - 1][colIndex] == 1) {
                    fresh--;
                    grid[rowIndex - 1][colIndex] = 2;
                    queue.offer(new int[] { rowIndex - 1, colIndex });
                }
                if (valid(row, col, rowIndex + 1, colIndex) && grid[rowIndex + 1][colIndex] == 1) {
                    fresh--;
                    grid[rowIndex + 1][colIndex] = 2;
                    queue.offer(new int[] { rowIndex + 1, colIndex });
                }
                if (valid(row, col, rowIndex, colIndex - 1) && grid[rowIndex][colIndex - 1] == 1) {
                    fresh--;
                    grid[rowIndex][colIndex - 1] = 2;
                    queue.offer(new int[] { rowIndex, colIndex - 1 });
                }
                if (valid(row, col, rowIndex, colIndex + 1) && grid[rowIndex][colIndex + 1] == 1) {
                    fresh--;
                    grid[rowIndex][colIndex + 1] = 2;
                    queue.offer(new int[] { rowIndex, colIndex + 1 });
                }
            }
            minute++;
        }
        return fresh == 0 ? minute : -1;
    }

    private boolean valid(int row, int col, int i, int j) {
        return (i >= 0 && i < row && j >= 0 && j < col);
    }

    /**
     * 课程表
     */
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        Map<Integer, List<Integer>> dependMap = new HashMap<>();
        int[] inDegree = new int[numCourses];
        for (int[] prerequisity : prerequisites) {
            dependMap.putIfAbsent(prerequisity[1], new ArrayList<>());
            dependMap.get(prerequisity[1]).add(prerequisity[0]);
            inDegree[prerequisity[0]]++;
        }
        Deque<Integer> queue = new ArrayDeque<>();
        for (int course = 0; course < numCourses; course++) {
            if (inDegree[course] == 0) {
                queue.offer(course);
            }
        }
        int count = 0;
        while (!queue.isEmpty()) {
            int course = queue.poll();
            for (int post : dependMap.getOrDefault(course, new ArrayList<>())) {
                inDegree[post]--;
                if (inDegree[post] == 0) {
                    queue.offer(post);
                }
            }
            count++;
        }
        return count == numCourses;
    }

    /**
     * 实现Trie（前缀树）
     */
    class Trie {

        public class Node {
            char ch;
            boolean isEnd;
            Map<Character, Node> next;

            public Node(char ch, boolean isEnd) {
                this.ch = ch;
                this.isEnd = isEnd;
                this.next = new HashMap<>();
            }
        }

        private Map<String, Boolean> map;

        private Node root;

        public Trie() {
            this.root = new Node('0', false);
            this.map = new HashMap<>();
        }

        public void insert(String word) {
            map.put(word, true);
            char[] chs = word.toCharArray();
            Node last = root;
            for (int i = 0; i < chs.length; i++) {
                char c = chs[i];
                Node node = last.next.get(c);
                if (node != null) {
                    node.isEnd = i == chs.length - 1 || node.isEnd;
                } else {
                    node = new Node(c, i == chs.length - 1);
                    last.next.put(c, node);
                }
                last = node;
            }
        }

        public boolean search(String word) {
            return map.containsKey(word);
        }

        public boolean startsWith(String prefix) {
            Node node = root;
            int len = prefix.length();
            for (int i = 0; i < len; i++) {
                char c = prefix.charAt(i);
                Node nextNode = node.next.get(c);
                if (nextNode == null) {
                    return false;
                }
                node = nextNode;
            }
            return true;
        }
    }

    /**
     * 全排列
     */
    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        dfs(res, new ArrayList<>(), new boolean[nums.length], nums);
        return res;
    }

    private void dfs(List<List<Integer>> res, List<Integer> list, boolean[] used, int[] nums) {
        if (list.size() == nums.length) {
            res.add(new ArrayList<>(list));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (used[i]) {
                continue;
            }
            used[i] = true;
            list.add(nums[i]);
            dfs(res, list, used, nums);
            list.remove(list.size() - 1);
            used[i] = false;
        }
    }

    /**
     * 子集
     */
    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        dfs(res, new ArrayList<>(), nums, 0);
        return res;
    }

    private void dfs(List<List<Integer>> res, List<Integer> list, int[] nums, int start) {
        if (start == nums.length) {
            res.add(new ArrayList<>(list));
            return;
        }
        dfs(res, list, nums, start + 1);
        list.add(nums[start]);
        dfs(res, list, nums, start + 1);
        list.remove(list.size() - 1);
    }

    /**
     * 电话号码的字母组合
     */
    public List<String> letterCombinations(String digits) {
        char[][] map = new char[][] {
                {},
                {},
                { 'a', 'b', 'c' },
                { 'd', 'e', 'f' },
                { 'g', 'h', 'i' },
                { 'j', 'k', 'l' },
                { 'm', 'n', 'o' },
                { 'p', 'q', 'r', 's' },
                { 't', 'u', 'v' },
                { 'w', 'x', 'y', 'z' }
        };
        List<String> res = new ArrayList<>();
        dfs(res, new StringBuilder(), map, digits, 0);
        return res;
    }

    private void dfs(List<String> res, StringBuilder sb, char[][] map, String digits, int start) {
        if (start == digits.length()) {
            if (!sb.isEmpty()) {
                res.add(sb.toString());
            }
            return;
        }
        char[] chs = map[digits.charAt(start) - '0'];
        for (int i = 0; i < chs.length; i++) {
            sb.append(chs[i]);
            dfs(res, sb, map, digits, start + 1);
            sb.setLength(sb.length() - 1);
        }
    }

    /**
     * 组合总和
     */
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> res = new ArrayList<>();
        dfs(res, new ArrayList<>(), candidates, target, 0);
        return res;
    }

    private void dfs(List<List<Integer>> res, List<Integer> list, int[] candidates, int target, int start) {
        if (start == candidates.length || target <= 0) {
            if (target == 0) {
                res.add(new ArrayList<>(list));
            }
            return;
        }
        if (target >= candidates[start]) {
            list.add(candidates[start]);
            dfs(res, list, candidates, target - candidates[start], start);
            list.remove(list.size() - 1);
        }
        dfs(res, list, candidates, target, start + 1);
    }

    /**
     * 括号生成
     */
    public List<String> generateParenthesis(int n) {
        List<String> list = new ArrayList<>();
        dfs(list, new StringBuilder(), n, 0, 0);
        return list;
    }

    private void dfs(List<String> res, StringBuilder sb, int n, int ltCount, int rtCount) {
        if (ltCount < rtCount || n == rtCount) {
            if (n == rtCount && rtCount == ltCount) {
                res.add(sb.toString());
            }
            return;
        }
        if (ltCount < n) {
            sb.append('(');
            dfs(res, sb, n, ltCount + 1, rtCount);
            sb.setLength(sb.length() - 1);
        }
        if (rtCount < n) {
            sb.append(')');
            dfs(res, sb, n, ltCount, rtCount + 1);
            sb.setLength(sb.length() - 1);
        }
    }

    /**
     * 单词搜索
     */
    public boolean exist(char[][] board, String word) {
        int row = board.length;
        int col = board[0].length;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (dfs(board, word, 0, i, j, new boolean[row][col])) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean dfs(char[][] board, String word, int index, int i, int j, boolean[][] used) {
        if (index == word.length()) {
            return true;
        }
        int row = board.length;
        int col = board[0].length;
        if (!(i >= 0 && i < row && j >= 0 && j < col)) {
            return false;
        }
        if (board[i][j] != word.charAt(index) || used[i][j]) {
            return false;
        }
        used[i][j] = true;
        boolean res = dfs(board, word, index + 1, i - 1, j, used) ||
                dfs(board, word, index + 1, i + 1, j, used) ||
                dfs(board, word, index + 1, i, j - 1, used) ||
                dfs(board, word, index + 1, i, j + 1, used);
        used[i][j] = false;
        return res;
    }

    /**
     * 分割回文串
     */
    public List<List<String>> partition(String s) {
        List<List<String>> res = new ArrayList<>();
        dfs(res, new ArrayList<>(), s, 0);
        return res;
    }

    private void dfs(List<List<String>> res, List<String> list, String s, int start) {
        if (start == s.length()) {
            res.add(new ArrayList<>(list));
            return;
        }
        for (int i = start; i < s.length(); i++) {
            if (valid(s, start, i)) {
                list.add(s.substring(start, i + 1));
                dfs(res, list, s, i + 1);
                list.remove(list.size() - 1);
            }
        }
    }

    private boolean valid(String s, int start, int end) {
        if (end < start) {
            return false;
        }
        while (end > start) {
            if (s.charAt(end) != s.charAt(start)) {
                return false;
            }
            end--;
            start++;
        }
        return true;
    }

    /**
     * N皇后
     */
    public List<List<String>> solveNQueens(int n) {
        char[][] board = new char[n][n];
        for (char[] line : board) {
            Arrays.fill(line, '.');
        }
        List<List<String>> res = new ArrayList<>();
        dfs(board, res, 0);
        return res;
    }

    private void dfs(char[][] board, List<List<String>> res, int rowIndex) {
        int row = board.length;
        int col = board[0].length;
        if (rowIndex == row) {
            List<String> list = new ArrayList<>();
            for (char[] line : board) {
                list.add(new String(line));
            }
            res.add(list);
            return;
        }
        for (int i = 0; i < col; i++) {
            if (!valid(board, rowIndex, i)) {
                continue;
            }
            board[rowIndex][i] = 'Q';
            dfs(board, res, rowIndex + 1);
            board[rowIndex][i] = '.';
        }
    }

    private boolean valid(char[][] board, int row, int col) {
        int rowNum = board.length;
        int colNum = board[0].length;
        if (!(row >= 0 && row < rowNum && col >= 0 && col < colNum)) {
            return false;
        }
        int i = row;
        while (i >= 0) {
            if (board[i--][col] == 'Q') {
                return false;
            }
        }
        int j = col;
        i = row;
        while (i >= 0 && j >= 0) {
            if (board[i--][j--] == 'Q') {
                return false;
            }
        }
        i = row;
        j = col;
        while (i >= 0 && j < colNum) {
            if (board[i--][j++] == 'Q') {
                return false;
            }
        }
        return true;
    }

    /**
     * 搜索插入位置
     */
    public int searchInsert(int[] nums, int target) {
        int lt = 0, rt = nums.length - 1;
        while (lt <= rt) {
            int mid = (lt + rt) >> 1;
            if (nums[mid] > target) {
                rt = mid - 1;
            } else if (nums[mid] < target) {
                lt = mid + 1;
            } else {
                return mid;
            }
        }
        return lt;
    }

    /**
     * 搜索二维矩阵
     */
    public boolean searchMatrix(int[][] matrix, int target) {
        int row = matrix.length;
        int col = matrix[0].length;
        int i = 0, j = col - 1;
        while (j >= 0 && i < row) {
            int num = matrix[i][j];
            if (num > target) {
                j--;
            } else if (num < target) {
                i++;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * 在排序数组中查找元素的第一个和最后一个位置
     */
    public int[] searchRange(int[] nums, int target) {
        int len = nums.length;
        int l = 0, r = len - 1;
        while (l <= r) {
            int mid = (l + r) >> 1;
            int num = nums[mid];
            if (target < num) {
                r = mid - 1;
            } else if (target > num) {
                l = mid + 1;
            } else {
                int first = mid, last = mid;
                while (first >= 1 && nums[first] == nums[first - 1]) {
                    first--;
                }
                while (last < len - 1 && nums[last] == nums[last + 1]) {
                    last++;
                }
                return new int[] { first, last };
            }
        }
        return new int[] { -1, -1 };
    }

    /**
     * 搜索旋转排序数组
     */
    public int search(int[] nums, int target) {
        // 4 5 6 7 0 1 2
        int lower = findTurnPoint(nums);
        int front = binarySearch(nums, target, 0, lower - 1);
        int back = binarySearch(nums, target, lower, nums.length - 1);
        return (front == -1 && back == -1) ? -1 : (front == -1 ? back : front);
    }

    private int findTurnPoint(int[] nums) {
        int lt = 0, rt = nums.length - 1;
        while (rt > lt) {
            int mid = (lt + rt) >> 1;
            if (nums[mid] > nums[lt]) {
                lt = mid;
            } else {
                rt = mid;
            }
        }
        return lt + 1;
    }

    private int binarySearch(int[] nums, int target, int start, int end) {
        while (start <= end) {
            int mid = (start + end) >> 1;
            int num = nums[mid];
            if (num < target) {
                start = mid + 1;
            } else if (num > target) {
                end = mid - 1;
            } else {
                return mid;
            }
        }
        return -1;
    }

    /**
     * 寻找旋转排序数组的最小值
     */
    public int findMin(int[] nums) {
        int lt = 0, rt = nums.length - 1;
        while (rt > lt) {
            int mid = (lt + rt) >> 1;
            if (nums[mid] > nums[lt]) {
                lt = mid;
            } else {
                rt = mid;
            }
        }
        return lt + 1 < nums.length ? Math.min(nums[lt + 1], nums[0]) : nums[0];
    }

    /**
     * 寻找两个正序数组的中位数
     * 没做出来
     * 这题自己把两种解法都做一遍
     * 第一种解法肯定还是按照归并排序那样
     * 第二种解法就是二分法，但是比较难掌握
     */
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int l1 = nums1.length, l2 = nums2.length;
        int count = 0;
        int idx1 = 0, idx2 = 0;
        // 1 3
        // 2
        // 1 2
        // 3 4
        int target = (l1 + l2 + 1) / 2;
        int last = -1;
        while (idx1 < l1 || idx2 < l2) {
            int num1 = idx1 < l1 ? nums1[idx1] : Integer.MAX_VALUE;
            int num2 = idx2 < l2 ? nums2[idx2] : Integer.MAX_VALUE;
            if (num1 < num2) {
                last = num1;
                idx1++;
                count++;
            } else {
                last = num2;
                idx2++;
                count++;
            }
            if (count == target) {
                if ((l1 + l2) % 2 == 1) {
                    return last;
                } else {
                    num1 = idx1 < l1 ? nums1[idx1] : Integer.MAX_VALUE;
                    num2 = idx2 < l2 ? nums2[idx2] : Integer.MAX_VALUE;
                    return (Math.min(num1, num2) + last) / 2.0;
                }
            }
        }
        return -1;
    }

    /**
     * 有效的括号
     */
    public boolean isValid(String s) {
        Map<Character, Character> map = new HashMap<>();
        map.put('}', '{');
        map.put(']', '[');
        map.put(')', '(');
        Deque<Character> stack = new ArrayDeque<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '}' || c == ']' || c == ')') {
                if (!stack.isEmpty() && stack.peek().equals(map.get(c))) {
                    stack.pop();
                } else {
                    return false;
                }
            } else {
                stack.push(c);
            }
        }
        return stack.isEmpty();
    }

    /**
     * 最小栈
     */
    class MinStack {

        private Deque<Integer> stack;

        private Deque<Integer> monotonicStack;

        public MinStack() {
            this.stack = new ArrayDeque<>();
            this.monotonicStack = new ArrayDeque<>();
        }

        public void push(int val) {
            stack.push(val);
            if (monotonicStack.isEmpty() || val <= monotonicStack.peek()) {
                monotonicStack.push(val);
            }
        }

        public void pop() {
            int val = stack.pop();
            if (!monotonicStack.isEmpty() && val == monotonicStack.peek()) {
                monotonicStack.pop();
            }
        }

        public int top() {
            return !stack.isEmpty() ? stack.peek() : -1;
        }

        public int getMin() {
            return monotonicStack.isEmpty() ? -1 : monotonicStack.peek();
        }
    }

    /**
     * 字符串解码
     */
    public String decodeString(String s) {
        Deque<Character> stack = new ArrayDeque<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '[') {
                if (stack.isEmpty() || !(stack.peek() >= '0' && stack.peek() <= '9')) {
                    stack.push('1');
                }
                stack.push(c);
            } else if (c == ']') {
                StringBuilder sb = new StringBuilder();
                while (!stack.isEmpty() && stack.peek() != '[') {
                    sb.append(stack.pop());
                }
                stack.pop();
                String t = sb.reverse().toString();
                sb.setLength(0);
                while (!stack.isEmpty() && stack.peek() >= '0' && stack.peek() <= '9') {
                    sb.append(stack.pop());
                }
                int time = Integer.parseInt(sb.reverse().toString());
                sb.setLength(0);
                for (int j = 0; j < time; j++) {
                    sb.append(t);
                }
                int size = sb.length();
                for (int j = 0; j < size; j++) {
                    stack.push(sb.charAt(j));
                }
                sb.setLength(0);
            } else {
                stack.push(c);
            }
        }
        StringBuilder sb = new StringBuilder();
        while (!stack.isEmpty()) {
            sb.append(stack.pop());
        }
        return sb.reverse().toString();
    }

    /**
     * 每日温度
     */
    public int[] dailyTemperatures(int[] temperatures) {
        Deque<Integer> stack = new ArrayDeque<>();
        int[] ans = new int[temperatures.length];
        for (int i = 0; i < temperatures.length; i++) {
            while (!stack.isEmpty() && temperatures[stack.peek()] < temperatures[i]) {
                int prev = stack.pop();
                ans[prev] = i - prev;
            }
            stack.push(i);
        }
        return ans;
    }

    /**
     * 柱状图中的最大矩形
     */
    public int largestRectangleArea(int[] heights) {
        Deque<Integer> stack = new ArrayDeque<>();
        int[] area = new int[heights.length];
        for (int i = 0; i < heights.length; i++) {
            while (!stack.isEmpty() && heights[stack.peek()] > heights[i]) {
                int j = stack.pop();
                area[j] = (i - j) * heights[j];
            }
            stack.push(i);
        }
        while (!stack.isEmpty()) {
            int i = heights.length;
            int j = stack.pop();
            area[j] = (i - j) * heights[j];
        }
        for (int i = heights.length - 1; i >= 0; i--) {
            while (!stack.isEmpty() && heights[stack.peek()] > heights[i]) {
                int j = stack.pop();
                area[j] += heights[j] * (j - i - 1);
            }
            stack.push(i);
        }
        while (!stack.isEmpty()) {
            int i = -1;
            int j = stack.pop();
            area[j] += heights[j] * (j - i - 1);
        }
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < area.length; i++) {
            max = Math.max(max, area[i]);
        }
        return max;
    }

    /**
     * 数组中的最大元素
     */
    public int findKthLargest(int[] nums, int k) {

    }
}
