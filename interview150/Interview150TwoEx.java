package interview150;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Interview150TwoEx {

    /**
     * 合并两个有序数组
     */
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int idx1 = m - 1;
        int idx2 = n - 1;
        int idx = m + n - 1;
        while (idx1 >= 0 || idx2 >= 0) {
            int num1 = idx1 >= 0 ? nums1[idx1] : Integer.MIN_VALUE;
            int num2 = idx2 >= 0 ? nums2[idx2] : Integer.MIN_VALUE;
            if (num1 > num2) {
                idx1--;
                nums1[idx] = num1;
            } else {
                idx2--;
                nums1[idx] = num2;
            }
            idx--;
        }
    }

    /**
     * 移除元素
     */
    public int removeElement(int[] nums, int val) {
        int fast = 0, slow = 0;
        while (fast < nums.length) {
            if (nums[fast] != val) {
                swap(nums, fast, slow++);
            }
            fast++;
        }
        return slow;
    }

    private void swap(int[] nums, int idx1, int idx2) {
        int t = nums[idx1];
        nums[idx1] = nums[idx2];
        nums[idx2] = t;
    }

    /**
     * 多数元素
     */
    public int majorityElement(int[] nums) {
        int count = 0;
        Integer winner = null;
        for (int num : nums) {
            if (winner == null) {
                winner = num;
            }
            if (winner == num) {
                count++;
            } else {
                count--;
                if (count == 0) {
                    winner = null;
                }
            }
        }
        return winner;
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
        if (end <= start) {
            return;
        }
        while (start < end) {
            swap(nums, start, end);
            start++;
            end--;
        }
    }

    /**
     * 买卖股票的最佳时机
     */
    public int maxProfit(int[] prices) {
        // dp[i][j] 到第i天为止，持有或不持有股票的最大收益
        int[][] dp = new int[prices.length][2];
        dp[0][0] = 0;
        dp[0][1] = -prices[0];
        for (int i = 1; i < prices.length; i++) {
            dp[i][0] = Math.max(dp[i - 1][0], dp[i - 1][1] + prices[i]);
            dp[i][1] = Math.max(dp[i - 1][1], -prices[i]);
        }
        return dp[prices.length - 1][0];
    }

    /**
     * 买卖股票的最佳时机
     * 这题和买卖股票的最佳时机II容易弄混
     * 区别是II可以买卖多次
     */
    public int maxProfitAnotherSolution(int[] prices) {
        int minPrice = Integer.MAX_VALUE;
        int maxProfit = 0;
        for (int price : prices) {
            if (minPrice > price) {
                minPrice = price;
            } else {
                maxProfit = Math.max(maxProfit, price - minPrice);
            }
        }
        return maxProfit;
    }

    /**
     * 分发糖果
     */
    public int candy(int[] ratings) {
        int len = ratings.length;
        int[] candy = new int[len];
        Arrays.fill(candy, 1);
        for (int i = 0; i < len - 1; i++) {
            if (ratings[i] < ratings[i + 1] && candy[i + 1] <= candy[i]) {
                candy[i + 1] = candy[i] + 1;
            }
        }
        for (int i = len - 1; i > 0; i--) {
            if (ratings[i - 1] > ratings[i] && candy[i - 1] <= candy[i]) {
                candy[i - 1] = candy[i] + 1;
            }
        }
        return Arrays.stream(candy).sum();
    }

    /**
     * 反转字符串中的单词
     */
    public String reverseWords(String s) {
        List<String> words = new ArrayList<>();
        int len = s.length();
        int fast = 0;
        while (fast < len && s.charAt(fast) == ' ') {
            fast++;
        }
        int slow = fast;
        while (fast < len) {
            if (s.charAt(fast) == ' ') {
                words.add(s.substring(slow, fast));
                do {
                    fast++;
                } while (fast < len && s.charAt(fast) == ' ');
                slow = fast;
                continue;
            }
            fast++;
        }
        if (slow < len && s.charAt(slow) != ' ') {
            words.add(s.substring(slow));
        }
        StringBuilder sb = new StringBuilder();
        for (int i = words.size() - 1; i >= 0; i--) {
            sb.append(words.get(i));
            if (i != 0) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    /**
     * 找出字符串中第一个匹配项的下标
     * 没做出来
     * 这个果然涉及KMP算法（其实也可以不涉及KMP，滑动窗口就可以）
     * 就是针对needle的[0, i]建立前缀表
     * 比如
     * a a b a d a a c a a e a c
     * a a e a c
     * 1 2 0 1 0
     */
    public int strStrError(String haystack, String needle) {
        int[] prefixTable = buildPrefixTable(needle);
        for (int i : prefixTable) {
            System.out.print(i + " ");
        }
        int idx = 0;
        int matchIdx = 0;
        int nLen = needle.length();
        int hLen = haystack.length();
        while (idx < hLen) {
            if (haystack.charAt(idx) == needle.charAt(matchIdx)) {
                matchIdx++;
                idx++;
                if (matchIdx == nLen) {
                    return idx - nLen + 1;
                }
                continue;
            }
            idx += prefixTable[matchIdx];
            matchIdx = 0;
        }
        return -1;
    }

    private int[] buildPrefixTableError(String needle) {
        int len = needle.length();
        int[] prefix = new int[len];
        for (int i = 0; i < len; i++) {
            int count = 0;
            int lt = 0, rt = i;
            while (lt <= rt) {
                if (needle.charAt(lt) != needle.charAt(rt)) {
                    break;
                }
                count++;
                lt++;
                rt--;
            }
            prefix[i] = count;
        }
        return prefix;
    }

    /**
     * 找出字符串中第一个匹配项的下标
     */
    public int strStrWithRollingWindow(String haystack, String needle) {
        int lt = 0;
        int hLen = haystack.length(), nLen = needle.length();
        while (lt <= (hLen - nLen)) {
            int rt = lt + nLen;
            if (haystack.substring(lt, rt).equals(needle)) {
                return lt;
            }
            lt++;
        }
        return -1;
    }
}
