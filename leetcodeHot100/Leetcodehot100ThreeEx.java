package leetcodeHot100;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
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
     * 基于时间轮的滑动窗口限流
     */
    public class SlidingWindowRateLimiter {

        public class TimeWheel {
            private final int size; // 时间轮的槽数量
            private final int interval; // 每个槽对应的时间间隔，单位是毫秒
            private final AtomicInteger[] slots; // 存储每个槽内的请求计数
            private final ReentrantLock[] locks; // 每个槽的锁
            private int currentSlot = 0; // 当前时间轮槽的位置

            public TimeWheel(int size, int interval) {
                this.size = size;
                this.interval = interval;
                this.slots = new AtomicInteger[size];
                this.locks = new ReentrantLock[size];

                // 初始化每个槽
                for (int i = 0; i < size; i++) {
                    slots[i] = new AtomicInteger(0);
                    locks[i] = new ReentrantLock();
                }
            }

            // 获取当前时间轮位置的槽的计数
            public int getSlotCount() {
                return slots[currentSlot].get();
            }

            // 增加当前槽的计数
            public void addRequest() {
                slots[currentSlot].incrementAndGet();
            }

            // 每隔指定时间更新一次时间轮，模拟滑动
            public void tick() {
                currentSlot = (currentSlot + 1) % size;
            }

            // 获取指定槽的计数
            public int getCountAtSlot(int slot) {
                return slots[slot].get();
            }

            // 重置指定槽的计数
            public void resetSlot(int slot) {
                slots[slot].set(0);
            }

            public int getSize() {
                return size;
            }

            public int getInterval() {
                return interval;
            }

            public ReentrantLock getSlotLock(int slot) {
                return locks[slot];
            }
        }

        private final TimeWheel timeWheel;
        private final int maxRequests; // 允许的最大请求数

        public SlidingWindowRateLimiter(int maxRequests, int windowSize, int windowInterval) {
            this.maxRequests = maxRequests;
            this.timeWheel = new TimeWheel(windowSize, windowInterval);

            // 启动时间轮更新任务
            new Thread(() -> {
                while (true) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(windowInterval);
                        timeWheel.tick();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }).start();
        }

        // 判断是否允许请求
        public boolean allowRequest() {
            int currentSlotCount = timeWheel.getSlotCount();

            // 如果当前时间轮中的请求数已超过最大请求数，拒绝请求
            if (currentSlotCount >= maxRequests) {
                return false;
            }

            // 否则，允许请求，并且增加请求计数
            timeWheel.addRequest();
            return true;
        }
    }
}
