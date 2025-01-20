package leetcodeHot100;

import java.util.ArrayDeque;
import java.util.Deque;

public class stack {

    /**
     * 字符串解码
     */
    public String decodeString(String s) {
        StringBuilder temp = new StringBuilder();
        Deque<Character> stack = new ArrayDeque<>();
        int len = s.length();
        for (int i = 0; i < len; i++) {
            Character ch = s.charAt(i);
            if (ch == ']') {
                while (!stack.isEmpty() && stack.peek() != '[') {
                    temp.append(stack.pop());
                }
                stack.pop();
                String t = temp.reverse().toString();
                temp.setLength(0);
                while (stack.peek() != null && stack.peek() >= '0' && stack.peek() <= '9') {
                    temp.append(stack.pop());
                }
                int time = Integer.parseInt(temp.reverse().toString());
                temp.setLength(0);
                for (int j = 0; j < time; j++) {
                    for (char c : t.toCharArray()) {
                        stack.push(c);
                    }
                }
            } else {
                if (ch == '[' && !(stack.peek() >= '0' && stack.peek() <= '9')) {
                    stack.push('1');
                }
                stack.push(ch);
            }
        }
        temp.setLength(0);
        while (!stack.isEmpty()) {
            temp.append(stack.pop());
        }
        return temp.reverse().toString();
    }

    /**
     * 有效的括号
     */
    public boolean isValid(String s) {
        Deque<Character> stack = new ArrayDeque<Character>();
        for (char c : s.toCharArray()) {
            if (c == '[') {
                stack.push(c);
            } else if (c == ']') {
                if (!stack.isEmpty() && stack.peek() == '[') {
                    stack.pop();
                } else {
                    return false;
                }
            } else if (c == '{') {
                stack.push(c);
            } else if (c == '}') {
                if (!stack.isEmpty() && stack.peek() == '{') {
                    stack.pop();
                } else {
                    return false;
                }
            } else if (c == '(') {
                stack.push(c);
            } else if (c == ')') {
                if (!stack.isEmpty() && stack.peek() == '(') {
                    stack.pop();
                } else {
                    return false;
                }
            }
        }
        return stack.isEmpty();
    }

    /**
     * 最小栈
     * 比较简单，就是一个普通栈加一个单调栈
     */
    class MinStack {

        private Deque<Integer> stack;

        private Deque<Integer> monotonicStack;

        public MinStack() {
            stack = new ArrayDeque<>();
            monotonicStack = new ArrayDeque<>();
        }

        public void push(int val) {
            stack.push(val);
            if (monotonicStack.isEmpty() || val <= monotonicStack.peek()) {
                monotonicStack.push(val);
            }
        }

        public void pop() {
            int val = stack.pop();
            if (val == monotonicStack.peek()) {
                monotonicStack.pop();
            }
        }

        public int top() {
            int val = stack.peek();
            return val;
        }

        public int getMin() {
            return monotonicStack.peek();
        }
    }
}
