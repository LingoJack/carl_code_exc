package leetcodeHot100;

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
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import leetcodeHot100.LeetcodeHot100FourEx.copyRandomListSolution.Node;
import upgrade_exc.dfs_exc;

public class LeetcodeHot100FourEx {

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
        Map<String, List<String>> map = new HashMap<>();
        for (String s : strs) {
            char[] chs = s.toCharArray();
            Arrays.sort(chs);
            String sortedString = new String(chs);
            List<String> list = map.getOrDefault(sortedString, new ArrayList<>());
            list.add(s);
            map.put(sortedString, list);
        }
        return map.values().stream().collect(Collectors.toList());
    }

    public List<List<String>> groupAnagramsV2(String[] strs) {
        return Arrays
                .stream(strs)
                .collect(Collectors
                        .groupingBy(s -> s
                                .chars()
                                .sorted()
                                .collect(StringBuilder::new, (sb, c) -> sb.append((char) c), StringBuilder::append)
                                .toString()))
                .values()
                .stream()
                .collect(Collectors.toList());
    }

    /**
     * 最长连续子序列
     */
    public int longestConsecutive(int[] nums) {
        Set<Integer> set = new HashSet<>();
        int max = 0;
        for (int num : nums) {
            set.add(num);
        }
        // 注意这里是set的遍历，不是nums
        for (int num : set) {
            if (set.contains(num - 1)) {
                continue;
            }
            int count = 0;
            while (set.contains(num)) {
                num++;
                count++;
            }
            max = Math.max(max, count);
        }
        return max;
    }

    public int longestConsecutiveV2(int[] nums) {
        Set<Integer> set = new HashSet<>();
        int[] max = new int[1];
        Arrays.stream(nums).forEach(num -> set.add(num));
        set.stream().forEach(num -> {
            if (set.contains(num - 1)) {
                return;
            }
            int count = 0;
            while (set.contains(num)) {
                num++;
                count++;
            }
            max[0] = Math.max(max[0], count);
        });
        return max[0];
    }

    /**
     * 移动零
     */
    public void moveZeroes(int[] nums) {
        int slow = 0, fast = 0;
        while (fast < nums.length) {
            if (nums[fast] != 0) {
                swap(nums, slow, fast);
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
     * 盛最多水的容器
     */
    public int maxArea(int[] height) {
        int lt = 0, rt = height.length - 1;
        int max = 0;
        while (lt < rt) {
            int wid = rt - lt;
            int hei = Math.min(height[lt], height[rt]);
            max = Math.max(max, wid * hei);
            if (height[lt] < height[rt]) {
                lt++;
            } else {
                rt--;
            }
        }
        return max;
    }

    /**
     * 三数之和
     */
    public List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);
        int len = nums.length;
        List<List<Integer>> res = new ArrayList<>();
        int first = 0;
        while (first < len) {
            int target = -nums[first];
            int second = first + 1;
            int third = len - 1;
            while (second < third) {
                int sum = nums[second] + nums[third];
                if (sum < target) {
                    second++;
                    while (second < len - 1 && second > first && nums[second] == nums[second - 1]) {
                        second++;
                    }
                } else if (sum > target) {
                    third--;
                    while (third > 1 && third < len - 1 && nums[third] == nums[third + 1]) {
                        third--;
                    }
                } else {
                    res.add(List.of(nums[first], nums[second], nums[third]));
                    second++;
                    while (second < len - 1 && second > first && nums[second] == nums[second - 1]) {
                        second++;
                    }
                }
            }
            first++;
            while (first < len && first > 0 && nums[first] == nums[first - 1]) {
                first++;
            }
        }
        return res;
    }

    /**
     * 接雨水
     * 单调栈解法
     */
    public int trap(int[] height) {
        Deque<Integer> stack = new ArrayDeque<>();
        int res = 0;
        for (int i = 0; i < height.length; i++) {
            while (!stack.isEmpty() && height[stack.peek()] < height[i]) {
                int j = stack.pop();
                if (stack.isEmpty()) {
                    break;
                }
                int k = stack.peek();
                int wid = i - k - 1;
                int hei = Math.min(height[i], height[k]) - height[j];
                res += wid * hei;
            }
            stack.push(i);
        }
        return res;
    }

    /**
     * 找到字符串中所有字母异位词
     * 这次写太久了
     * 最近算法水平下降的太厉害了
     * 才仅仅一个月没有刷
     * 果然之前还是靠的肌肉记忆？
     * 今天是2025.5.14，上一次提交已经是2025.2.28了
     * 之前是用动态的滑动窗口做的，缩小窗口的逻辑是窗口的大小大于等于pLen
     * 现在使用的是固定滑窗的逻辑
     */
    public List<Integer> findAnagrams(String s, String p) {
        int sLen = s.length(), pLen = p.length();
        Map<Character, Integer> need = new HashMap<>();
        Map<Character, Integer> have = new HashMap<>();
        for (char c : p.toCharArray()) {
            need.put(c, need.getOrDefault(c, 0) + 1);
        }
        int valid = 0;
        int lt = 0, rt = 0;
        List<Integer> res = new ArrayList<>();
        for (; rt < pLen && rt < sLen; rt++) {
            char c = s.charAt(rt);
            if (!need.containsKey(c)) {
                continue;
            }
            have.put(c, have.getOrDefault(c, 0) + 1);
            if (need.get(c).equals(have.get(c))) {
                valid++;
                if (valid == need.size()) {
                    res.add(lt);
                }
            }
        }
        while (rt < sLen) {
            char rc = s.charAt(rt++);
            if (need.containsKey(rc)) {
                have.put(rc, have.getOrDefault(rc, 0) + 1);
                if (need.get(rc).equals(have.get(rc))) {
                    valid++;
                }
            }
            char lc = s.charAt(lt++);
            if (need.containsKey(lc)) {
                if (need.get(lc).equals(have.get(lc))) {
                    valid--;
                }
                have.put(lc, have.getOrDefault(lc, 0) - 1);
            }
            if (valid == need.size()) {
                res.add(lt);
            }
        }
        return res;
    }

    /**
     * 和为K的子数组
     */
    public int subarraySum(int[] nums, int k) {
        int len = nums.length;
        int[] prefix = new int[len];
        prefix[0] = nums[0];
        for (int i = 1; i < nums.length; i++) {
            prefix[i] = prefix[i - 1] + nums[i];
        }
        // 前缀和及其出现的次数
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, 1);
        int count = 0;
        for (int i = 0; i < prefix.length; i++) {
            // prefix[i] - x = k
            count += map.getOrDefault(prefix[i] - k, 0);
            map.put(prefix[i], map.getOrDefault(prefix[i], 0) + 1);
        }
        return count;
    }

    /**
     * 滑动窗口的最大值
     * 这题的核心还是要意识到：
     * 单调队列维护的是当前窗口以及之前的窗口内的最大值
     */
    public int[] maxSlidingWindow(int[] nums, int k) {
        int len = nums.length;
        Deque<Integer> queue = new ArrayDeque<>();
        int[] res = new int[(len - 1) - (k - 1) + 1];
        for (int i = 0; i < len; i++) {
            // 维持单调队列的单调递减的特性
            while (!queue.isEmpty() && nums[queue.peekLast()] <= nums[i]) {
                queue.pollLast();
            }
            queue.offerLast(i);
            // 判断是否在窗口范围内
            if (i >= k - 1) {
                while (i - queue.peekFirst() + 1 > k) {
                    queue.pollFirst();
                }
                res[i - k + 1] = nums[queue.peek()];
            }
        }
        return res;
    }

    /**
     * 最大子数组和
     */
    public int maxSubArray(int[] nums) {
        int max = nums[0];
        int sum = 0;
        for (int num : nums) {
            if (sum < 0) {
                sum = 0;
            }
            sum += num;
            max = Math.max(max, sum);
        }
        return max;
    }

    /**
     * 合并区间
     */
    public int[][] merge(int[][] intervals) {
        PriorityQueue<int[]> priorityQueue = new PriorityQueue<>((a, b) -> {
            return a[0] == b[0] ? Integer.compare(a[1], b[1]) : Integer.compare(a[0], b[0]);
        });
        for (int[] interval : intervals) {
            priorityQueue.offer(interval);
        }
        Deque<int[]> stack = new ArrayDeque<>();
        while (!priorityQueue.isEmpty()) {
            int[] interval = priorityQueue.poll();
            if (!stack.isEmpty()) {
                int[] last = stack.peek();
                if (last[1] >= interval[0]) {
                    stack.pop();
                    interval = new int[] { last[0], Math.max(last[1], interval[1]) };
                }
            }
            stack.push(interval);
        }
        int size = stack.size();
        int[][] res = new int[size][2];
        for (int i = 0; i < size; i++) {
            res[i] = stack.pop();
        }
        return res;
    }

    /**
     * 轮转数组
     */
    public void rotate(int[] nums, int k) {
        // 1 2 3 4 5 6 7 k=3
        // 5 6 7 1 2 3 4
        int len = nums.length;
        k %= len;
        reverse(nums, 0, len - 1);
        reverse(nums, 0, k - 1);
        reverse(nums, k, len - 1);
    }

    private void reverse(int[] nums, int start, int end) {
        int lt = start, rt = end;
        while (lt < rt) {
            int t = nums[lt];
            nums[lt] = nums[rt];
            nums[rt] = t;
            lt++;
            rt--;
        }
    }

    /**
     * 除自身以外的数组的乘积
     */
    public int[] productExceptSelf(int[] nums) {
        int len = nums.length;
        int[] prefix = new int[len];
        int[] suffix = new int[len];
        prefix[0] = nums[0];
        for (int i = 1; i < len; i++) {
            prefix[i] = prefix[i - 1] * nums[i];
        }
        suffix[len - 1] = nums[len - 1];
        for (int i = len - 2; i >= 0; i--) {
            suffix[i] = suffix[i + 1] * nums[i];
        }
        int[] res = new int[len];
        for (int i = 0; i < len; i++) {
            int lt = i == 0 ? 1 : prefix[i - 1];
            int rt = (i == len - 1) ? 1 : suffix[i + 1];
            res[i] = lt * rt;
        }
        return res;
    }

    /**
     * 缺失的第一个正数
     */
    public int firstMissingPositive(int[] nums) {
        int len = nums.length;
        boolean[] exist = new boolean[len + 1];
        for (int num : nums) {
            if (num < len + 1 && num > 0) {
                exist[num] = true;
            }
        }
        for (int i = 1; i < len + 1; i++) {
            if (!exist[i]) {
                return i;
            }
        }
        return len + 1;
    }

    /**
     * 矩阵置零
     */
    public void setZeroes(int[][] matrix) {
        int row = matrix.length;
        int col = matrix[0].length;
        boolean[] zeroRow = new boolean[row];
        boolean[] zeroCol = new boolean[col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (matrix[i][j] == 0) {
                    zeroRow[i] = true;
                    zeroCol[j] = true;
                }
            }
        }
        for (int i = 0; i < row; i++) {
            if (zeroRow[i]) {
                for (int j = 0; j < col; j++) {
                    matrix[i][j] = 0;
                }
            }
        }
        for (int j = 0; j < col; j++) {
            if (zeroCol[j]) {
                for (int i = 0; i < row; i++) {
                    matrix[i][j] = 0;
                }
            }
        }
    }

    /**
     * 螺旋矩阵
     */
    public List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> res = new ArrayList<>();
        int row = matrix.length, col = matrix[0].length;
        int upBound = 0, downBound = row - 1;
        int ltBound = 0, rtBound = col - 1;
        int count = row * col;
        int rowIdx = 0;
        int colIdx = 0;
        while (true) {
            for (int i = ltBound; i <= rtBound; i++) {
                res.add(matrix[rowIdx][i]);
                colIdx = i;
                if (--count == 0) {
                    return res;
                }
            }
            upBound++;
            for (int i = upBound; i <= downBound; i++) {
                res.add(matrix[i][colIdx]);
                rowIdx = i;
                if (--count == 0) {
                    return res;
                }
            }
            rtBound--;
            for (int i = rtBound; i >= ltBound; i--) {
                res.add(matrix[rowIdx][i]);
                colIdx = i;
                if (--count == 0) {
                    return res;
                }
            }
            downBound--;
            for (int i = downBound; i >= upBound; i--) {
                res.add(matrix[i][colIdx]);
                rowIdx = i;
                if (--count == 0) {
                    return res;
                }
            }
            ltBound++;
        }
    }

    /**
     * 旋转图像
     */
    public void rotate(int[][] matrix) {
        int row = matrix.length, col = matrix[0].length;
        for (int j = 0; j < col; j++) {
            for (int i = 0; 2 * i < row; i++) {
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
        int row = matrix.length, col = matrix[0].length;
        int rowIdx = 0, colIdx = col - 1;
        while (true) {
            if (!(rowIdx >= 0 && rowIdx < row && colIdx >= 0 && colIdx < col)) {
                break;
            }
            int val = matrix[rowIdx][colIdx];
            if (val > target) {
                colIdx--;
            } else if (val < target) {
                rowIdx++;
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
        int lenA = 0, lenB = 0;
        ListNode node = headA;
        while (node != null) {
            lenA++;
            node = node.next;
        }
        node = headB;
        while (node != null) {
            lenB++;
            node = node.next;
        }
        ListNode slowHead, longHead;
        if (lenA > lenB) {
            slowHead = headB;
            longHead = headA;
        } else {
            slowHead = headA;
            longHead = headB;
        }
        int diff = Math.abs(lenA - lenB);
        while (diff > 0) {
            longHead = longHead.next;
            diff--;
        }
        while (slowHead != null && longHead != null) {
            if (slowHead == longHead) {
                return slowHead;
            }
            slowHead = slowHead.next;
            longHead = longHead.next;
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
     */
    public boolean isPalindrome(ListNode head) {
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }
        ListNode last = null;
        ListNode secondHead = slow;
        while (secondHead != null) {
            ListNode next = secondHead.next;
            secondHead.next = last;
            last = secondHead;
            secondHead = next;
        }
        ListNode match1 = last;
        ListNode match2 = head;
        while (match1 != match2 && match1 != null && match2 != null) {
            if (match1.val != match2.val) {
                return false;
            }
            match1 = match1.next;
            match2 = match2.next;
        }
        return true;
    }

    /**
     * 环形链表
     */
    public boolean hasCycle(ListNode head) {
        ListNode slow = head, fast = head;
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
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
            if (slow == fast) {
                ListNode node = head;
                while (node != slow) {
                    node = node.next;
                    slow = slow.next;
                }
                return node;
            }
        }
        return null;
    }

    /**
     * 合并两个有序链表
     */
    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        ListNode dummy = new ListNode();
        ListNode last = dummy;
        while (list1 != null || list2 != null) {
            int v1 = list1 == null ? Integer.MAX_VALUE : list1.val;
            int v2 = list2 == null ? Integer.MAX_VALUE : list2.val;
            if (v2 < v1) {
                last.next = list2;
                last = list2;
                list2 = list2.next;
            } else {
                last.next = list1;
                last = list1;
                list1 = list1.next;
            }
        }
        return dummy.next;
    }

    /**
     * 两数相加
     */
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode();
        ListNode node = dummy;
        ListNode n1 = l1, n2 = l2;
        int carry = 0;
        while (n1 != null || n2 != null || carry != 0) {
            int o1 = n1 == null ? 0 : n1.val;
            int o2 = n2 == null ? 0 : n2.val;
            int sum = o1 + o2 + carry;
            carry = sum / 10;
            sum %= 10;
            ListNode newNode = new ListNode(sum);
            node.next = newNode;
            node = newNode;
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
        int count = 0;
        ListNode slow = head, fast = head;
        while (fast != null) {
            fast = fast.next;
            count++;
        }
        ListNode dummy = new ListNode(0, head);
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
     * 做太久了
     * Tag: Redo
     */
    public ListNode swapPairs(ListNode head) {
        ListNode dummy = new ListNode(0, head);
        ListNode prevGroupEnd = dummy;
        int k = 2;
        ListNode groupStart = head;
        // 1 2 3 4
        // s
        // f
        while (groupStart != null) {
            ListNode slow = groupStart, fast = groupStart;
            int count = 0;
            ListNode groupEnd = null;
            while (count < k && fast != null) {
                groupEnd = fast;
                fast = fast.next;
                count++;
            }
            if (count < k) {
                break;
            }
            ListNode nextGroupStart = fast;
            ListNode last = null;
            while (slow != nextGroupStart) {
                ListNode next = slow.next;
                slow.next = last;
                last = slow;
                slow = next;
            }
            prevGroupEnd.next = groupEnd;
            groupStart.next = nextGroupStart;
            prevGroupEnd = groupStart;
            groupStart = nextGroupStart;
        }
        return dummy.next;
    }

    /**
     * K个一组翻转链表
     */
    public ListNode reverseKGroup(ListNode head, int k) {
        ListNode dummy = new ListNode(0, head);
        ListNode prevGroupEnd = dummy;
        ListNode groupStart = head;
        // 1 2 3 4
        // s
        // f
        while (groupStart != null) {
            ListNode slow = groupStart, fast = groupStart;
            int count = 0;
            ListNode groupEnd = null;
            while (count < k && fast != null) {
                groupEnd = fast;
                fast = fast.next;
                count++;
            }
            if (count < k) {
                break;
            }
            ListNode nextGroupStart = fast;
            ListNode last = null;
            while (slow != nextGroupStart) {
                ListNode next = slow.next;
                slow.next = last;
                last = slow;
                slow = next;
            }
            prevGroupEnd.next = groupEnd;
            groupStart.next = nextGroupStart;
            prevGroupEnd = groupStart;
            groupStart = nextGroupStart;
        }
        return dummy.next;
    }

    /**
     * 随机链表的复制
     */
    public class copyRandomListSolution {
        class Node {
            int val;
            Node next;
            Node random;

            public Node(int val) {
                this.val = val;
                this.next = null;
                this.random = null;
            }

            public Node copyRandomList(Node head) {
                Map<Node, Node> map = new HashMap<>();
                Node node = head;
                while (node != null) {
                    map.put(node, new Node(node.val));
                    node = node.next;
                }
                node = head;
                while (node != null) {
                    Node mirrorNode = map.get(node);
                    mirrorNode.next = map.get(node.next);
                    mirrorNode.random = map.get(node.random);
                    node = node.next;
                }
                return map.get(head);
            }
        }
    }

    /**
     * 排序链表
     */
    public ListNode sortList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode[] spilt = spilt(head);
        ListNode lt = sortList(spilt[0]);
        ListNode rt = sortList(spilt[1]);
        ListNode dummy = new ListNode();
        ListNode last = dummy;
        while (lt != null || rt != null) {
            int lv = lt == null ? Integer.MAX_VALUE : lt.val;
            int rv = rt == null ? Integer.MAX_VALUE : rt.val;
            if (lv < rv) {
                last.next = lt;
                last = lt;
                lt = lt.next;
            } else {
                last.next = rt;
                last = rt;
                rt = rt.next;
            }
        }
        return dummy.next;
    }

    private ListNode[] spilt(ListNode head) {
        if (head == null) {
            return null;
        }
        ListNode slow = head, fast = head;
        while (fast.next != null && fast.next.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }
        // 1 2 3 4
        // s
        // f
        ListNode secondHead = slow.next;
        slow.next = null;
        return new ListNode[] { head, secondHead };
    }

    /**
     * 合并K个升序链表
     */
    public ListNode mergeKLists(ListNode[] lists) {
        PriorityQueue<ListNode> priorityQueue = new PriorityQueue<>((a, b) -> Integer.compare(a.val, b.val));
        for (ListNode head : lists) {
            if (head == null) {
                continue;
            }
            priorityQueue.offer(head);
        }
        ListNode dummy = new ListNode();
        ListNode last = dummy;
        while (!priorityQueue.isEmpty()) {
            ListNode node = priorityQueue.poll();
            if (node.next != null) {
                priorityQueue.offer(node.next);
            }
            last.next = node;
            last = node;
        }
        return dummy.next;
    }

    /**
     * LRU缓存
     */
    class LRUCache {

        private class Node {
            private int key;
            private int val;
            private Node next;
            private Node prev;

            public Node(int key, int val) {
                this.key = key;
                this.val = val;
            }
        }

        private Map<Integer, Node> map;

        private Node head;

        private Node tail;

        private int capacity;

        public LRUCache(int capacity) {
            this.map = new HashMap<>();
            this.capacity = capacity;
            this.head = new Node(0, 0);
            this.tail = new Node(0, 0);
            this.head.next = this.tail;
            this.tail.prev = this.head;
        }

        public int get(int key) {
            if (map.containsKey(key)) {
                Node node = map.get(key);
                node.prev.next = node.next;
                node.next.prev = node.prev;
                node.next = null;
                node.prev = null;
                node.next = this.head.next;
                node.prev = this.head;
                this.head.next.prev = node;
                this.head.next = node;
                return node.val;
            }
            return -1;
        }

        public void put(int key, int value) {
            if (map.containsKey(key)) {
                Node node = map.get(key);
                node.prev.next = node.next;
                node.next.prev = node.prev;
                node.next = null;
                node.prev = null;
                node.next = this.head.next;
                node.prev = this.head;
                this.head.next.prev = node;
                this.head.next = node;
                node.val = value;
                return;
            }
            Node node = new Node(key, value);
            node.next = this.head.next;
            node.prev = this.head;
            this.head.next.prev = node;
            this.head.next = node;
            map.put(key, node);
            if (map.size() > this.capacity) {
                Node removedNode = this.tail.prev;
                removedNode.prev.next = removedNode.next;
                removedNode.next.prev = removedNode.prev;
                removedNode.next = null;
                removedNode.prev = null;
                map.remove(removedNode.key);
            }
        }
    }

    /**
     * 二叉树的中序遍历
     */
    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        inorder(root, list);
        return list;
    }

    private void inorder(TreeNode node, List<Integer> list) {
        if (node == null) {
            return;
        }
        inorder(node.left, list);
        list.add(node.val);
        inorder(node.right, list);
    }

    /**
     * 二叉树的最大深度
     */
    public int maxDepth(TreeNode root) {
        return getHeight(root);
    }

    private int getHeight(TreeNode node) {
        if (node == null) {
            return 0;
        }
        return Math.max(getHeight(node.left), getHeight(node.right)) + 1;
    }

    /**
     * 翻转二叉树
     */
    public TreeNode invertTree(TreeNode root) {
        if (root == null) {
            return null;
        }
        TreeNode t = root.left;
        root.left = root.right;
        root.right = t;
        invertTree(root.left);
        invertTree(root.right);
        return root;
    }

    /**
     * 对称二叉树
     */
    public boolean isSymmetric(TreeNode root) {
        if (root == null) {
            return false;
        }
        return valid(root.left, root.right);
    }

    private boolean valid(TreeNode lt, TreeNode rt) {
        if (lt == null && rt == null) {
            return true;
        }
        if (lt != null && rt != null) {
            if (lt.val != rt.val) {
                return false;
            }
            return valid(lt.right, rt.left) && valid(lt.left, rt.right);
        }
        return false;
    }

    /**
     * 二叉树的直径
     */
    public int diameterOfBinaryTree(TreeNode root) {
        
    }
}
