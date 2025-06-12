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
import java.util.stream.Collectors;

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
    public boolean searchMatrixAnother(int[][] matrix, int target) {
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
        maxDiameter = 0;
        dfs4DiameterOfBinaryTree(root);
        return maxDiameter;
    }

    private int maxDiameter;

    private int dfs4DiameterOfBinaryTree(TreeNode node) {
        if (node == null) {
            return 0;
        }
        int ltHeight = dfs4DiameterOfBinaryTree(node.left);
        int rtHeight = dfs4DiameterOfBinaryTree(node.right);
        maxDiameter = Math.max(maxDiameter, ltHeight + rtHeight);
        return Math.max(ltHeight, rtHeight) + 1;
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
     * 将有序数组转换为二叉搜索树
     */
    public TreeNode sortedArrayToBST(int[] nums) {
        return buildTree(nums, 0, nums.length - 1);
    }

    private TreeNode buildTree(int[] nums, int start, int end) {
        if (end < start) {
            return null;
        }
        int mid = (start + end) >> 1;
        TreeNode root = new TreeNode(nums[mid]);
        root.left = buildTree(nums, start, mid - 1);
        root.right = buildTree(nums, mid + 1, end);
        return root;
    }

    /**
     * 验证二叉树
     * 还有另外一种思路就是，二叉搜索树的中序遍历是严格递增的
     * 所以我们可以铜鼓二叉搜索树来验证BST
     */
    public boolean isValidBST(TreeNode root) {
        if (root == null) {
            return false;
        }
        return validBST(root);
    }

    private boolean validBST(TreeNode root) {
        if (root == null) {
            return true;
        }
        TreeNode node = null;
        if (root.left != null) {
            node = root.left;
            while (node.right != null) {
                node = node.right;
            }
            if (node.val >= root.val) {
                return false;
            }
        }
        if (root.right != null) {
            node = root.right;
            while (node.left != null) {
                node = node.left;
            }
            if (node.val <= root.val) {
                return false;
            }
        }
        return validBST(root.left) && validBST(root.right);
    }

    /**
     * 验证二叉搜索树
     */
    public boolean isValidBSTWithInorderTravsal(TreeNode root) {
        last = null;
        valid = true;
        inorderValid(root);
        return valid;
    }

    private void inorderValid(TreeNode node) {
        if (node == null || !valid) {
            return;
        }
        inorderValid(node.left);
        if (last != null && last >= node.val) {
            valid = false;
        }
        last = node.val;
        inorderValid(node.right);
    }

    private Integer last;

    private boolean valid;

    /**
     * 二叉搜索树中第K小的元素
     */
    public int kthSmallest(TreeNode root, int k) {
        this.count = 0;
        this.k = k;
        inorder4kthSmallest(root);
        return res;
    }

    private void inorder4kthSmallest(TreeNode node) {
        if (node == null || count > k) {
            return;
        }
        inorder4kthSmallest(node.left);
        count++;
        if (count == k) {
            res = node.val;
            return;
        }
        inorder4kthSmallest(node.right);
    }

    private int res;

    private int count;

    private int k;

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
                TreeNode right = node.right;
                TreeNode ltMax = node.left;
                while (ltMax != null && ltMax.right != null) {
                    ltMax = ltMax.right;
                }
                node.right = node.left;
                node.left = null;
                ltMax.right = right;
            }
            node = node.right;
        }
    }

    /**
     * 从前序和中序遍历序列构造二叉树
     */
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) {
            map.put(inorder[i], i);
        }
        return buildTree(preorder, map, 0, inorder.length - 1);
    }

    private int preorderIdx = 0;

    private TreeNode buildTree(int[] preorder, Map<Integer, Integer> map, int start, int end) {
        if (end < start) {
            return null;
        }
        TreeNode root = new TreeNode(preorder[preorderIdx]);
        preorderIdx++;
        int idx = map.get(root.val);
        root.left = buildTree(preorder, map, start, idx - 1);
        root.right = buildTree(preorder, map, idx + 1, end);
        return root;
    }

    /**
     * 路径总和III
     * 没做出来
     * 和前缀树有关
     * 这里和第三次的hot100练习一样，用的不是最佳解法
     * 这里我用二叉树的层序遍历来做
     * 确实很多东西，只有过了一段时间再来做，才知道哪些是真的会了，哪些是肌肉记忆
     */
    public int pathSum(TreeNode root, int targetSum) {
        if (root == null) {
            return 0;
        }
        Deque<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                startFromRoot(node, targetSum);
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
        }
        return pathCount;
    }

    private int pathCount = 0;

    private void startFromRoot(TreeNode root, long target) {
        if (root == null) {
            return;
        }
        if (root.val == target) {
            pathCount++;
        }
        startFromRoot(root.left, target - root.val);
        startFromRoot(root.right, target - root.val);
    }

    /**
     * 路径总和III
     */
    public int pathSumWithPrefixSumCountMap(TreeNode root, int targetSum) {
        Map<Long, Integer> map = new HashMap<>();
        map.put(0l, 1);
        dfs4PathSumWithPrefixSumCountMap(root, map, targetSum, 0);
        return count4PathSumWithPrefixSumCountMap;
    }

    private int count4PathSumWithPrefixSumCountMap = 0;

    private void dfs4PathSumWithPrefixSumCountMap(TreeNode node, Map<Long, Integer> map, int target, long curSum) {
        if (node == null) {
            return;
        }
        curSum += node.val;
        // i - x = target
        // x == i - target
        count4PathSumWithPrefixSumCountMap += map.getOrDefault(curSum - target, 0);
        map.put(curSum, map.getOrDefault(curSum, 0) + 1);
        dfs4PathSumWithPrefixSumCountMap(node.left, map, target, curSum);
        map.put(curSum, map.getOrDefault(curSum, 0) - 1);
        map.put(curSum, map.getOrDefault(curSum, 0) + 1);
        dfs4PathSumWithPrefixSumCountMap(node.right, map, target, curSum);
        map.put(curSum, map.getOrDefault(curSum, 0) - 1);
    }

    /**
     * 二叉树的最近公共祖先
     */
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (isAncestor(p, q)) {
            return p;
        } else if (isAncestor(q, p)) {
            return q;
        }
        if (root == null) {
            return null;
        }
        TreeNode res = null;
        Deque<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                if (isAncestor(node, p) && isAncestor(node, q)) {
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

    private boolean isAncestor(TreeNode ancestor, TreeNode node) {
        if (ancestor == null) {
            return false;
        }
        if (ancestor == node) {
            return true;
        }
        return isAncestor(ancestor.left, node) || isAncestor(ancestor.right, node);
    }

    /**
     * 二叉树中的最大路径和
     */
    public int maxPathSum(TreeNode root) {
        dfs4MaxPathSum(root);
        return maxPathSum;
    }

    private int maxPathSum = Integer.MIN_VALUE;

    private int dfs4MaxPathSum(TreeNode node) {
        if (node == null) {
            return 0;
        }
        int lt = dfs4MaxPathSum(node.left);
        int rt = dfs4MaxPathSum(node.right);
        maxPathSum = Math.max(maxPathSum, lt + node.val + rt);
        return Math.max(0, Math.max(lt, rt) + node.val);
    }

    /**
     * 岛屿数量
     */
    public int numIslands(char[][] grid) {
        int row = grid.length, col = grid[0].length;
        int count = 0;
        boolean[][] visited = new boolean[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (grid[i][j] == '1' && !visited[i][j]) {
                    count++;
                    dfs(grid, visited, i, j);
                }
            }
        }
        return count;
    }

    private void dfs(char[][] grid, boolean[][] visited, int rowIdx, int colIdx) {
        int row = grid.length, col = grid[0].length;
        if (!(rowIdx >= 0 && rowIdx < row && colIdx >= 0 && colIdx < col)) {
            return;
        }
        if (visited[rowIdx][colIdx]) {
            return;
        }
        visited[rowIdx][colIdx] = true;
        if (grid[rowIdx][colIdx] == '1') {
            dfs(grid, visited, rowIdx + 1, colIdx);
            dfs(grid, visited, rowIdx - 1, colIdx);
            dfs(grid, visited, rowIdx, colIdx + 1);
            dfs(grid, visited, rowIdx, colIdx - 1);
        }
    }

    /**
     * 腐烂的橘子
     */
    public int orangesRotting(int[][] grid) {
        int minute = -1;
        Deque<int[]> queue = new ArrayDeque<>();
        int row = grid.length, col = grid[0].length;
        int freshCount = 0;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (grid[i][j] == 2) {
                    queue.offer(new int[] { i, j });
                } else if (grid[i][j] == 1) {
                    freshCount++;
                }
            }
        }
        if (freshCount == 0) {
            return 0;
        }
        int[][] dirs = new int[][] {
                { 1, 0 },
                { -1, 0 },
                { 0, 1 },
                { 0, -1 }
        };
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int[] idx = queue.poll();
                int x = idx[0], y = idx[1];
                for (int[] dir : dirs) {
                    int rowIdx = x + dir[0], colIdx = y + dir[1];
                    if (validScope(grid, row, col, rowIdx, colIdx)) {
                        queue.offer(new int[] { rowIdx, colIdx });
                        freshCount--;
                    }
                }
            }
            minute++;
        }
        return freshCount == 0 ? minute : -1;
    }

    private boolean validScope(int[][] grid, int row, int col, int rowIdx, int colIdx) {
        boolean res = (rowIdx >= 0 && rowIdx < row && colIdx >= 0 && colIdx < col) && (grid[rowIdx][colIdx] == 1);
        if (res) {
            grid[rowIdx][colIdx] = 2;
        }
        return res;
    }

    /**
     * 课程表
     */
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        Map<Integer, List<Integer>> map = new HashMap<>();
        int[] in = new int[numCourses];
        for (int[] rel : prerequisites) {
            in[rel[0]]++;
            List<Integer> list = map.getOrDefault(rel[1], new ArrayList<>());
            ;
            list.add(rel[0]);
            map.put(rel[1], list);
        }
        Deque<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < numCourses; i++) {
            if (in[i] == 0) {
                queue.offer(i);
            }
        }
        int valid = 0;
        while (!queue.isEmpty()) {
            Integer course = queue.poll();
            valid++;
            List<Integer> list = map.getOrDefault(course, new ArrayList<>());
            for (int nextCourse : list) {
                in[nextCourse]--;
                if (in[nextCourse] == 0) {
                    queue.offer(nextCourse);
                }
            }
        }
        return valid == numCourses;
    }

    /**
     * 全排列
     */
    public List<List<Integer>> permute(int[] nums) {
        int len = nums.length;
        boolean[] used = new boolean[len];
        List<List<Integer>> res = new ArrayList<>();
        dfs(res, new ArrayList<>(), used, nums);
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

    private void dfs(List<List<Integer>> res, List<Integer> list, int[] nums, int idx) {
        if (idx == nums.length) {
            res.add(new ArrayList<>(list));
            return;
        }
        dfs(res, list, nums, idx + 1);
        list.add(nums[idx]);
        dfs(res, list, nums, idx + 1);
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
        dfs(res, new StringBuilder(), digits, map, 0);
        return res;
    }

    private void dfs(List<String> res, StringBuilder sb, String digit, char[][] map, int idx) {
        if (digit.equals("")) {
            return;
        }
        int len = digit.length();
        if (idx == len) {
            res.add(sb.toString());
            return;
        }
        char[] chs = map[digit.charAt(idx) - '0'];
        for (char c : chs) {
            int repl = sb.length();
            sb.append(c);
            dfs(res, sb, digit, map, idx + 1);
            sb.setLength(repl);
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

    private void dfs(List<List<Integer>> res, List<Integer> list, int[] candidates, int target, int idx) {
        if (target == 0) {
            res.add(new ArrayList<>(list));
            return;
        }
        if (idx == candidates.length) {
            return;
        }
        if (target >= candidates[idx]) {
            list.add(candidates[idx]);
            dfs(res, list, candidates, target - candidates[idx], idx);
            list.remove(list.size() - 1);
        }
        dfs(res, list, candidates, target, idx + 1);
    }

    /**
     * 括号生成
     */
    public List<String> generateParenthesis(int n) {
        List<String> res = new ArrayList<>();
        dfs(res, new StringBuilder(), n, n);
        return res;
    }

    private void dfs(List<String> res, StringBuilder sb, int ltCount, int rtCount) {
        if (ltCount == 0 && rtCount == 0) {
            res.add(sb.toString());
            return;
        }
        if (ltCount > 0) {
            sb.append('(');
            dfs(res, sb, ltCount - 1, rtCount);
            sb.setLength(sb.length() - 1);
        }
        if (rtCount > ltCount) {
            sb.append(')');
            dfs(res, sb, ltCount, rtCount - 1);
            sb.setLength(sb.length() - 1);
        }
    }

    /**
     * 单词搜索
     */
    public boolean exist(char[][] board, String word) {
        int row = board.length, col = board[0].length;
        boolean[][] visited = new boolean[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (dfs(board, word, 0, i, j, visited)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean dfs(char[][] board, String word, int idx, int rowIdx, int colIdx, boolean[][] visited) {
        int len = word.length();
        int row = board.length, col = board[0].length;
        if (idx == len) {
            return true;
        }
        if (!(rowIdx >= 0 && rowIdx < row && colIdx >= 0 && colIdx < col)) {
            return false;
        }
        if (visited[rowIdx][colIdx]) {
            return false;
        }
        if (board[rowIdx][colIdx] != word.charAt(idx)) {
            return false;
        }
        visited[rowIdx][colIdx] = true;
        boolean res = dfs(board, word, idx + 1, rowIdx + 1, colIdx, visited)
                || dfs(board, word, idx + 1, rowIdx - 1, colIdx, visited)
                || dfs(board, word, idx + 1, rowIdx, colIdx + 1, visited)
                || dfs(board, word, idx + 1, rowIdx, colIdx - 1, visited);
        visited[rowIdx][colIdx] = false;
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
        int len = s.length();
        if (start == len) {
            res.add(new ArrayList<>(list));
            return;
        }
        for (int end = start; end < len; end++) {
            if (valid(s, start, end)) {
                list.add(s.substring(start, end + 1));
                dfs(res, list, s, end + 1);
                list.remove(list.size() - 1);
            }
        }
    }

    private boolean valid(String s, int start, int end) {
        while (start < end) {
            if (s.charAt(start) != s.charAt(end)) {
                return false;
            }
            start++;
            end--;
        }
        return true;
    }

    /**
     * N皇后
     */
    public List<List<String>> solveNQueens(int n) {
        boolean[][] board = new boolean[n][n];
        List<List<String>> res = new ArrayList<>();
        dfs(res, board, 0);
        return res;
    }

    private void dfs(List<List<String>> res, boolean[][] board, int rowIdx) {
        int row = board.length, col = board[0].length;
        if (rowIdx == row) {
            StringBuilder sb = new StringBuilder();
            List<String> list = new ArrayList<>();
            for (int i = 0; i < row; i++) {
                sb.setLength(0);
                for (int j = 0; j < col; j++) {
                    sb.append(board[i][j] ? 'Q' : '.');
                }
                list.add(sb.toString());
            }
            res.add(list);
            return;
        }
        for (int i = 0; i < col; i++) {
            if (valid(board, rowIdx, i)) {
                board[rowIdx][i] = true;
                dfs(res, board, rowIdx + 1);
                board[rowIdx][i] = false;
            }
        }
    }

    private boolean valid(boolean[][] board, int i, int j) {
        int row = board.length, col = board[0].length;
        if (!(i >= 0 && i < row && j >= 0 && j < col)) {
            return false;
        }
        int x = i, y = j;
        while (x >= 0) {
            if (board[x][y]) {
                return false;
            }
            x--;
        }
        x = i;
        y = j;
        while ((x >= 0 && y >= 0)) {
            if (board[x][y]) {
                return false;
            }
            x--;
            y--;
        }
        x = i;
        y = j;
        while ((x >= 0 && y < col)) {
            if (board[x][y]) {
                return false;
            }
            x--;
            y++;
        }
        return true;
    }

    /**
     * 搜索插入位置
     */
    public int searchInsert(int[] nums, int target) {
        int len = nums.length;
        int lt = 0, rt = len - 1;
        while (lt <= rt) {
            int mid = (lt + rt) >> 1;
            if (nums[mid] < target) {
                lt = mid + 1;
            } else if (nums[mid] > target) {
                rt = mid - 1;
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
        int row = matrix.length, col = matrix[0].length;
        int rowIdx = 0, colIdx = col - 1;
        while (rowIdx >= 0 && rowIdx < row && colIdx >= 0 && colIdx < col) {
            int num = matrix[rowIdx][colIdx];
            if (num > target) {
                colIdx--;
            } else if (num < target) {
                rowIdx++;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * 在排序数组中的查找元素的第一个和最后一个位置
     */
    public int[] searchRange(int[] nums, int target) {
        int len = nums.length;
        if (len == 0) {
            return new int[] { -1, -1 };
        }
        int lt = bs(nums, target);
        int rt = bs(nums, target + 1);
        return new int[] { (lt < len && nums[lt] == target ? lt : -1),
                (rt - 1 >= 0 && nums[rt - 1] == target ? rt - 1 : -1) };
    }

    private int bs(int[] nums, int target) {
        int len = nums.length;
        int lt = 0, rt = len - 1;
        while (lt <= rt) {
            int mid = (lt + rt) >> 1;
            int num = nums[mid];
            if (num > target) {
                rt = mid - 1;
            } else if (num < target) {
                lt = mid + 1;
            } else {
                rt = mid - 1;
            }
        }
        return lt;
    }

    /**
     * 搜索旋转排序数组
     * 没做出来，不会找转折点
     * 思路差不多，关键是用rt和mid去比
     */
    public int search(int[] nums, int target) {
        int turnPoint = findTurnPoint(nums);
        int lt = binarySearch(nums, 0, turnPoint - 1, target);
        int rt = binarySearch(nums, turnPoint, nums.length - 1, target);
        return lt == -1 ? rt : lt;
    }

    private int findTurnPoint(int[] nums) {
        int len = nums.length;
        // 找转折点，其实就是找值最小的点
        int lt = 0, rt = len - 1;
        while (lt < rt) {
            int mid = (lt + rt) >> 1;
            int num = nums[mid];
            if (nums[rt] >= num) {
                rt = mid;
            } else {
                lt = mid + 1;
            }
        }
        return lt;
    }

    private int binarySearch(int[] nums, int start, int end, int target) {
        int lt = start, rt = end;
        while (lt <= rt) {
            int mid = (lt + rt) >> 1;
            if (nums[mid] < target) {
                lt = mid + 1;
            } else if (nums[mid] > target) {
                rt = mid - 1;
            } else {
                return mid;
            }
        }
        return -1;
    }

    /**
     * 寻找旋转排序数组中的最小值
     */
    public int findMin(int[] nums) {
        int len = nums.length;
        int lt = 0, rt = len - 1;
        while (lt < rt) {
            int mid = (lt + rt) >> 1;
            if (nums[mid] < nums[rt]) {
                rt = mid;
            } else if (nums[mid] > nums[rt]) {
                lt = mid + 1;
            } else {
                lt = mid;
            }
        }
        return nums[lt];
    }

    /**
     * 有效的括号
     */
    public boolean isValid(String s) {
        Map<Character, Character> map = new HashMap<>();
        map.put(')', '(');
        map.put('}', '{');
        map.put(']', '[');
        Deque<Character> stack = new ArrayDeque<>();
        for (char c : s.toCharArray()) {
            if (c == '(' || c == '{' || c == '[') {
                stack.push(c);
                continue;
            }
            if (stack.isEmpty() || stack.pop() != map.get(c)) {
                return false;
            }
        }
        return stack.isEmpty();
    }

    /**
     * 最小栈
     */
    class MinStack {

        private Deque<Integer> stack;

        private Deque<Integer> minStack;

        public MinStack() {
            this.stack = new ArrayDeque<>();
            this.minStack = new ArrayDeque<>();
        }

        public void push(int val) {
            stack.push(val);
            if (minStack.isEmpty() || val <= minStack.peek()) {
                minStack.push(val);
            }
        }

        public void pop() {
            int val = stack.pop();
            if (minStack.peek() == val) {
                minStack.pop();
            }
        }

        public int top() {
            return stack.peek();
        }

        public int getMin() {
            return minStack.peek();
        }
    }

    /**
     * 字符串解码
     */
    public String decodeString(String s) {
        Deque<Integer> numStack = new ArrayDeque<>();
        Deque<Character> charStack = new ArrayDeque<>();
        StringBuilder sb = new StringBuilder();
        char[] chs = s.toCharArray();
        for (int i = 0; i < chs.length; i++) {
            char c = chs[i];
            if (c == ']') {
                while (!charStack.isEmpty() && charStack.peek() != '[') {
                    sb.append(charStack.pop());
                }
                sb.reverse();
                charStack.pop();
                Integer times = numStack.isEmpty() ? 1 : numStack.pop();
                String repl = sb.toString();
                sb.setLength(0);
                sb.repeat(repl, times);
                for (char nc : sb.toString().toCharArray()) {
                    charStack.push(nc);
                }
                sb.setLength(0);
            } else if (c >= '0' && c <= '9') {
                int repl = i;
                if (i + 1 < chs.length && chs[i + 1] >= '0' && chs[i + 1] <= '9') {
                    continue;
                }
                while (repl >= 0 && chs[repl] >= '0' && chs[repl] <= '9') {
                    sb.append(chs[repl]);
                    repl--;
                }
                String numStr = sb.reverse().toString();
                numStack.push(Integer.parseInt(numStr));
                sb.setLength(0);
            } else {
                charStack.push(c);
            }
        }
        charStack.forEach(c -> sb.append(c));
        return sb.reverse().toString();
    }

    /**
     * 每日温度
     */
    public int[] dailyTemperatures(int[] temperatures) {
        int len = temperatures.length;
        int[] res = new int[len];
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 0; i < len; i++) {
            while (!stack.isEmpty() && temperatures[stack.peek()] < temperatures[i]) {
                int coolerDayIdx = stack.pop();
                res[coolerDayIdx] = i - coolerDayIdx;
            }
            stack.push(i);
        }
        return res;
    }

    /**
     * 柱状图中的最大矩形
     */
    public int largestRectangleArea(int[] heights) {
        Deque<Integer> stack = new ArrayDeque<>();
        int len = heights.length;
        int[] res = new int[len];
        for (int i = 0; i < len; i++) {
            while (!stack.isEmpty() && heights[stack.peek()] > heights[i]) {
                int j = stack.pop();
                int w = i - j;
                res[j] += w * heights[j];
            }
            stack.push(i);
        }
        while (!stack.isEmpty()) {
            int j = stack.pop();
            int w = len - j;
            res[j] += w * heights[j];
        }
        for (int i = len - 1; i >= 0; i--) {
            while (!stack.isEmpty() && heights[stack.peek()] > heights[i]) {
                int j = stack.pop();
                int w = j - i - 1;
                res[j] += w * heights[j];
            }
            stack.push(i);
        }
        while (!stack.isEmpty()) {
            int j = stack.pop();
            int w = j - (-1) - 1;
            res[j] += w * heights[j];
        }
        int max = 0;
        for (int num : res) {
            max = Math.max(max, num);
        }
        return max;
    }

    /**
     * 数组中第K个最大元素
     */
    public int findKthLargest(int[] nums, int k) {
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>();
        for (int num : nums) {
            if (priorityQueue.size() >= k) {
                if (num <= priorityQueue.peek()) {
                    continue;
                }
                priorityQueue.poll();
            }
            priorityQueue.offer(num);
        }
        return priorityQueue.peek();
    }

    /**
     * 前K个高频元素
     */
    public int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int num : nums) {
            map.put(num, map.getOrDefault(num, 0) + 1);
        }
        PriorityQueue<Map.Entry<Integer, Integer>> priorityQueue = new PriorityQueue<>((a, b) -> {
            return Integer.compare(a.getValue(), b.getValue());
        });
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (priorityQueue.size() >= k) {
                if (entry.getValue() <= priorityQueue.peek().getValue()) {
                    continue;
                }
                priorityQueue.poll();
            }
            priorityQueue.offer(entry);
        }
        int[] res = new int[k];
        for (int i = k - 1; i >= 0; i--) {
            res[i] = priorityQueue.poll().getKey();
        }
        return res;
    }

    /**
     * 数据流的中位数
     */
    class MedianFinder {

        private PriorityQueue<Integer> lowwer;

        private PriorityQueue<Integer> upper;

        public MedianFinder() {
            this.lowwer = new PriorityQueue<>((a, b) -> Integer.compare(b, a));
            this.upper = new PriorityQueue<>((a, b) -> Integer.compare(a, b));
        }

        public void addNum(int num) {
            if (lowwer.isEmpty() || num <= lowwer.peek()) {
                lowwer.offer(num);
                if (lowwer.size() > upper.size() + 1) {
                    upper.offer(lowwer.poll());
                }
            } else {
                upper.offer(num);
                if (lowwer.size() < upper.size()) {
                    lowwer.offer(upper.poll());
                }
            }
        }

        public double findMedian() {
            return (upper.size() + lowwer.size()) % 2 == 0 ? (upper.peek() + lowwer.peek()) / 2.0 : lowwer.peek();
        }
    }

    /**
     * 买卖股票的最佳时机
     * 没做出最优解
     */
    public int maxProfit(int[] prices) {
        int len = prices.length;
        int[][] dp = new int[len][2];
        dp[0][1] = -prices[0];
        for (int i = 1; i < len; i++) {
            dp[i][0] = Math.max(dp[i - 1][0], dp[i - 1][1] + prices[i]);
            dp[i][1] = Math.max(dp[i - 1][1], -prices[i]);
        }
        return dp[len - 1][0];
    }

    /**
     * 买卖股票的最佳时机
     * 最优解
     */
    public int maxProfitAwesomeSolution(int[] prices) {
        int minPrice = Integer.MAX_VALUE;
        int maxProfit = 0;
        for (int price : prices) {
            if (price < minPrice) {
                minPrice = price;
            } else {
                maxProfit = Math.max(price - minPrice, maxProfit);
            }
        }
        return maxProfit;
    }

    /**
     * 跳跃游戏
     */
    public boolean canJump(int[] nums) {
        int maxScope = 0;
        for (int i = 0; i <= maxScope; i++) {
            maxScope = Math.max(maxScope, i + nums[i]);
            if (maxScope >= nums.length - 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * 跳跃游戏II
     * 非最优解
     * 这个是dp解法
     */
    public int jump(int[] nums) {
        int len = nums.length;
        int[] dp = new int[len];
        Arrays.fill(dp, len + 1);
        dp[0] = 0;
        for (int i = 0; i < len; i++) {
            for (int delta = 0; delta <= nums[i]; delta++) {
                if (i + delta >= len) {
                    continue;
                }
                dp[i + delta] = Math.min(dp[i] + 1, dp[i + delta]);
            }
        }
        return dp[len - 1];
    }

    /**
     * 跳跃游戏II
     * 最优解
     * 计算个格子所能到达的最远的地方
     * 然后由于每一step都至少走一步，所以直接遍历到最远的直到可达到最后一个就是最快的
     */
    public int jumpBetterSolution(int[] nums) {
        int[] farest = new int[nums.length];
        int max = 0;
        for (int i = 0; i < nums.length; i++) {
            max = Math.max(max, i + nums[i]);
            farest[i] = max;
        }
        int step = 0;
        int scope = 0;
        while (scope < nums.length - 1) {
            step++;
            scope = farest[scope];
        }
        return step;
    }

    /**
     * 划分字母区间
     */
    public List<Integer> partitionLabels(String s) {
        int len = s.length();
        int[] lastIdx = new int[26];
        char[] chs = s.toCharArray();
        for (int i = 0; i < len; i++) {
            lastIdx[chs[i] - 'a'] = i;
        }
        int scope = 0;
        int start = 0;
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            if (scope == 0) {
                start = i;
            }
            scope = Math.max(scope, lastIdx[chs[i] - 'a']);
            if (i == scope) {
                res.add(scope - start + 1);
                scope = 0;
            }
        }
        return res;
    }

    /**
     * 爬楼梯
     */
    public int climbStairs(int n) {
        int[] record = new int[n + 1];
        Arrays.fill(record, -1);
        return climbStairs(record, n);
    }

    public int climbStairs(int[] record, int n) {
        if (n == 1 || n == 2) {
            return n;
        }
        if (record[n] >= 0) {
            return record[n];
        }
        int res = climbStairs(record, n - 1) + climbStairs(record, n - 2);
        record[n] = res;
        return res;
    }

    /**
     * 杨辉三角
     */
    public List<List<Integer>> generate(int numRows) {
        int[][] dp = new int[numRows][numRows];
        List<List<Integer>> res = new ArrayList<>();
        dp[0][0] = 1;
        res.add(List.of(1));
        for (int i = 1; i < numRows; i++) {
            List<Integer> list = new ArrayList<>();
            for (int j = 0; j <= i; j++) {
                dp[i][j] = (j >= 1 ? dp[i - 1][j - 1] : 0) + dp[i - 1][j];
                list.add(dp[i][j]);
            }
            res.add(list);
        }
        return res;
    }

    /**
     * 打家劫舍
     */
    public int rob(int[] nums) {
        int len = nums.length;
        int[] dp = new int[len];
        dp[0] = nums[0];
        if (len < 2) {
            return dp[len - 1];
        }
        dp[1] = Math.max(nums[0], nums[1]);
        for (int i = 2; i < len; i++) {
            dp[i] = Math.max(dp[i - 1], dp[i - 2] + nums[i]);
        }
        return dp[len - 1];
    }

    /**
     * 完全平方数
     */
    public int numSquares(int n) {
        int[] dp = new int[n + 1];
        for (int i = 1; i < n + 1; i++) {
            dp[i] = n + 1;
            for (int j = 1; j * j <= i; j++) {
                dp[i] = Math.min(dp[i], dp[i - j * j] + 1);
            }
        }
        return dp[n];
    }

    /**
     * 单词拆分
     */
    public boolean wordBreak(String s, List<String> wordDict) {
        int size = wordDict.size();
        int len = s.length();
        boolean[] dp = new boolean[len + 1];
        dp[0] = true;
        for (int i = 1; i < len + 1; i++) {
            for (int j = 0; j < size; j++) {
                String word = wordDict.get(j);
                int wLen = word.length();
                int end = i - 1;
                int start = end - wLen + 1;
                dp[i] = dp[i] || (i - wLen >= 0 && dp[i - wLen] && word.equals(s.substring(start, start + wLen)));
            }
        }
        return dp[len];
    }

    /**
     * 最长递增子序列
     */
    public int lengthOfLIS(int[] nums) {
        List<Integer> list = new ArrayList<>();
        for (int num : nums) {
            int size = list.size();
            int insertPos = bs(list, num);
            if (insertPos >= size) {
                list.add(num);
            } else {
                list.set(insertPos, num);
            }
        }
        return list.size();
    }

    private int bs(List<Integer> list, int target) {
        int lt = 0, rt = list.size() - 1;
        while (lt <= rt) {
            int mid = (lt + rt) >> 1;
            int num = list.get(mid);
            if (num > target) {
                rt = mid - 1;
            } else if (num < target) {
                lt = mid + 1;
            } else {
                rt = mid - 1;
            }
        }
        return lt;
    }

    /**
     * 零钱兑换
     * 通过这题可以看出来01背包和完全背包你已经很不熟了
     */
    public int coinChange(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1);
        dp[0] = 0;
        for (int i = 1; i < amount + 1; i++) {
            for (int j = 0; j < coins.length; j++) {
                dp[i] = Math.min(dp[i], (i - coins[j] >= 0 ? dp[i - coins[j]] + 1 : (amount + 1)));
            }
        }
        return dp[amount] == amount + 1 ? -1 : dp[amount];
    }

    /**
     * 乘积最大子数组
     */
    public int maxProduct(int[] nums) {
        int len = nums.length;
        int[] maxDp = new int[len];
        int[] minDp = new int[len];
        maxDp[0] = nums[0];
        minDp[0] = nums[0];
        int max = nums[0];
        for (int i = 1; i < len; i++) {
            maxDp[i] = Math.max(nums[i], Math.max(maxDp[i - 1] * nums[i], minDp[i - 1] * nums[i]));
            minDp[i] = Math.min(nums[i], Math.min(maxDp[i - 1] * nums[i], minDp[i - 1] * nums[i]));
            max = Math.max(max, maxDp[i]);
        }
        return max;
    }

    /**
     * 分割等和子集
     * 没有做到最优的空间复杂度
     * 这题还可以用记忆化递归做
     */
    public boolean canPartition(int[] nums) {
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        if (sum % 2 == 1) {
            return false;
        }
        int target = sum >> 1;
        int len = nums.length;
        boolean[][] dp = new boolean[target + 1][len];
        for (int i = 0; i < len; i++) {
            dp[0][i] = true;
            if (nums[i] <= target) {
                dp[nums[i]][i] = true;
            }
        }
        for (int i = 1; i < target + 1; i++) {
            for (int j = 0; j < nums.length; j++) {
                int num = nums[j];
                dp[i][j] = dp[i][j - 1] || (i >= num && dp[i - num][j - 1]);
            }
        }
        return dp[target][len - 1];
    }

    /**
     * 最长有效括号
     */
    public int longestValidParentheses(String s) {
        int len = s.length();
        // 以s[i]结尾的最长有效括号长度为dp[i]
        int[] dp = new int[len];
        int max = 0;
        for (int i = 1; i < len; i++) {
            char c = s.charAt(i);
            if (c == '(') {
                dp[i] = 0;
                continue;
            }
            char lastCh = s.charAt(i - 1);
            if (lastCh == '(') {
                // ()
                dp[i] = (i - 2 >= 0 ? dp[i - 2] : 0) + 2;
            } else {
                // ((())
                int lastMatchIdx = i - dp[i - 1] - 1;
                if (lastMatchIdx >= 0 && s.charAt(lastMatchIdx) == '(') {
                    dp[i] = 2 + dp[i - 1] + (lastMatchIdx - 1 >= 0 ? dp[lastMatchIdx - 1] : 0);
                }
            }
            max = Math.max(max, dp[i]);
        }
        return max;
    }

    /**
     * 不同路径
     */
    public int uniquePaths(int m, int n) {
        int[][] dp = new int[m][n];
        for (int i = 0; i < m; i++) {
            dp[i][0] = 1;
        }
        for (int i = 0; i < n; i++) {
            dp[0][i] = 1;
        }
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
            }
        }
        return dp[m - 1][n - 1];
    }

    /**
     * 最小路径和
     */
    public int minPathSum(int[][] grid) {
        int row = grid.length, col = grid[0].length;
        int[][] dp = new int[row + 1][col + 1];
        for (int i = 0; i < row; i++) {
            dp[i][0] = (i >= 1 ? dp[i - 1][0] : 0) + grid[i][0];
        }
        for (int i = 0; i < col; i++) {
            dp[0][i] = (i >= 1 ? dp[0][i - 1] : 0) + grid[0][i];
        }
        for (int i = 1; i < row; i++) {
            for (int j = 1; j < col; j++) {
                dp[i][j] = Math.min(dp[i - 1][j], dp[i][j - 1]) + grid[i][j];
            }
        }
        return dp[row - 1][col - 1];
    }

    /**
     * 最长回文子串
     */
    public String longestPalindrome(String s) {
        int len = s.length();
        boolean[][] dp = new boolean[len][len];
        for (int i = 0; i < len; i++) {
            dp[i][i] = true;
        }
        int offset = 0;
        int maxSize = 1;
        for (int size = 2; size <= len; size++) {
            for (int start = 0; start + size - 1 < len; start++) {
                int end = start + size - 1;
                if (size == 2) {
                    dp[start][end] = s.charAt(start) == s.charAt(end);
                } else {
                    dp[start][end] = (s.charAt(start) == s.charAt(end)) && dp[start + 1][end - 1];
                }
                if (dp[start][end] && size > maxSize) {
                    maxSize = size;
                    offset = start;
                }
            }
        }
        return s.substring(offset, offset + maxSize);
    }

    /**
     * 最长公共子序列
     */
    public int longestCommonSubsequence(String text1, String text2) {
        int l1 = text1.length(), l2 = text2.length();
        int[][] dp = new int[l1 + 1][l2 + 1];
        for (int i = 1; i < l1 + 1; i++) {
            for (int j = 1; j < l2 + 1; j++) {
                dp[i][j] = (text1.charAt(i - 1) == text2.charAt(j - 1) ? (1 + dp[i - 1][j - 1])
                        : Math.max(dp[i - 1][j], dp[i][j - 1]));
            }
        }
        return dp[l1][l2];
    }

    /**
     * 编辑距离
     */
    public int minDistance(String word1, String word2) {
        int l1 = word1.length(), l2 = word2.length();
        int[][] dp = new int[l1 + 1][l2 + 1];
        for(int i = 0; i < l2 + 1; i++) {
            dp[0][i] = i;
        }
        for(int i = 0; i < l1 + 1; i++) {
            dp[i][0] = i;
        }
        for(int i = 1; i < l1 + 1; i++) {
            for(int j = 1; j < l2 + 1; j++) {
                dp[i][j] = (word1.charAt(i  -1) == word2.charAt(j - 1)) ? (dp[i - 1][j - 1]) : (Math.min(dp[i - 1][j], Math.min(dp[i][j - 1], dp[i - 1][j - 1])) + 1);
            }
        }
        return dp[l1][l2];
    }

    /**
     * 只出现一次的数字
     */
    public int singleNumber(int[] nums) {
       
    }
}