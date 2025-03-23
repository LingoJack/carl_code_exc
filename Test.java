import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Test {

    /**
     * 多多的推箱子游戏
     */
    public boolean pushGame(String operations, int[] startPos) {
        int len = operations.length();
        int x = startPos[0], y = startPos[1];
        for (int i = 0; i < len; i++) {
            char op = operations.charAt(i);
            if (op == 'W') {
                y++;
            } else if (op == 'A') {
                x--;
            } else if (op == 'S') {
                y--;
            } else if (op == 'D') {
                x++;
            }
        }
        return x == 0 && y == 0;
    }

    /**
     * 多多买彩票
     * 关键是发现这个规律：
     * 大于100的数字都满足存在子串位数和为3的倍数
     */
    public int getLuckyNumCount(long start, long end) {
        int count = 0;
        for (long i = start; i <= end; i++) {
            if (isLuckyNum(i)) {
                count++;
            }
        }
        return count;
    }

    private boolean isLuckyNum(long num) {
        int len = 0;
        long repl = num;
        while (repl > 0) {
            repl /= 10;
            len++;
        }
        long[] prefixSum = new long[len];
        prefixSum[0] = num % 10;
        num /= 10;
        for (int i = 1; i < len - 1; i++) {
            prefixSum[i] = prefixSum[i - 1] + (num % 10);
            num /= 10;
        }
        for (int i = 0; i < prefixSum.length; i++) {
            if (prefixSum[i] % 3 == 0) {
                return true;
            }
            for (int j = i + 1; j < prefixSum.length; j++) {
                if ((prefixSum[j] - prefixSum[i]) % 3 == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        Test test = new Test();
        long[] nums = new long[] {};
        System.out.println(test.canSeePeopleNum(nums));
    }

    /**
     * 多多的排队
     */
    public long canSeePeopleNum(long[] heights) {
        if (heights == null || heights.length == 0) {
            return 0;
        }
        // 10 7 4 8 2 1
        long res = 0;
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 0; i < heights.length; i++) {
            while (!stack.isEmpty() && heights[stack.peek()] <= heights[i]) {
                long j = stack.pop();
                res += (i - j);
            }
            stack.push(i);
        }
        while (!stack.isEmpty()) {
            long j = stack.pop();
            res += (heights.length - 1 - j);
        }
        return res;
    }
}