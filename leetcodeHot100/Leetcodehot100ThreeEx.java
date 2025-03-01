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
    public boolean searchMatrix(int[][] matrix, int target) {
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
     * 做出来了，但还不是最快的解法，之前的做法更快，就是反转链表
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

    }

    private boolean valid(TreeNode node) {
        if (node == null) {
            return true;
        }
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
}
