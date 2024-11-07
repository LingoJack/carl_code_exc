package stack_and_queue;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;

public class stack_and_queue {
    /**
     * 用栈实现队列
     * 这题十分考察对栈和队列的掌握程度
     */
    class MyQueue {
        
        private LinkedList<Integer> stackIn; // 入栈
        private LinkedList<Integer> stackOut; // 出栈

        public MyQueue() {
            this.stackIn = new LinkedList<>();
            this.stackOut = new LinkedList<>();
        }
        
        // 将元素 x 推到队列的末尾
        public void push(int x) {
            stackIn.push(x);
        }

        // 从队列的开头移除并返回元素
        public int pop() {
            if (stackOut.isEmpty()) {
                transfer();
            }
            return stackOut.isEmpty() ? -1 : stackOut.pop(); // 如果为空则返回 -1
        }
        
        // 返回队列开头的元素
        public int peek() {
            if (stackOut.isEmpty()) {
                transfer();
            }
            return stackOut.isEmpty() ? -1 : stackOut.peek(); // 如果为空则返回 -1
        }
        
        // 如果队列为空，返回 true ；否则，返回 false
        public boolean empty() {
            return stackIn.isEmpty() && stackOut.isEmpty();
        }

        // 将 stack0 中的元素倒序转移到 stack1 中
        private void transfer() {
            while (!stackIn.isEmpty()) {
                stackOut.push(stackIn.pop());
            }
        }
    }

    /**
     * 用一个队列实现栈
     */
    class MyStack {

        private Queue<Integer> queue;

        public MyStack() {
            this.queue = new LinkedList<>();
        }
        
        // 将元素 x 压入栈顶
        public void push(int x) {
            int size = queue.size();
            queue.add(x);
            // 将队列中的前面所有元素移到队列末尾
            for (int i = 0; i < size; i++) {
                queue.add(queue.poll());
            }
        }
        
        // 移除并返回栈顶元素
        public int pop() {
            return queue.poll();
        }
        
        // 获取栈顶元素
        public int top() {
            return queue.peek();
        }
        
        // 如果栈是空的，返回 true；否则，返回 false
        public boolean empty() {
            return queue.isEmpty();
        }
    }

    /**
     * 有效的括号
     * 感觉这个和编译原理的过程有点像，就是入栈，匹配就出栈
     */
    public boolean isValid(String s) {
        LinkedList<Character> queue = new LinkedList<>();
        char[] str = s.toCharArray();
        for(int i = 0; i< str.length; i++) {
            switch (str[i]) {
                case '{':
                    queue.push(str[i]);
                    break;
                case '[':
                    queue.push(str[i]);
                    break;
                case '(':
                    queue.push(str[i]);
                    break;
                case '}':
                    if (queue.peek() != null && queue.peek().equals('{')) {
                        queue.poll();
                    }
                    else {
                        return false;
                    }
                    break;
                case ']':
                    if (queue.peek() != null && queue.peek().equals('[')) {
                        queue.poll();
                    }
                    else {
                        return false;
                    }
                    break;
                case ')':
                    if (queue.peek() != null && queue.peek().equals('(')) {
                        queue.poll();
                    }
                    else {
                        return false;
                    }
                    break;
                default:
                    break;
            }
        }
        return queue.isEmpty();
    }

    /**
     * 删除字符串中的所有相邻重复项
     * 对于堆栈,使用ArrayDeque可能是比LinkedList更好的选择
     * 呃...俄罗斯方块 or 消消乐
     */
    public String removeDuplicates(String s) {
        ArrayDeque<Character> queue = new ArrayDeque<>();
        StringBuilder sb = new StringBuilder();
        char[] str = s.toCharArray();
        for(int i = 0; i < str.length; i++) {
            char ch = str[i];
            if (queue.peek() == null || ch != queue.peek()) {
                queue.push(ch);
            }
            else {
                queue.poll();
                continue;
            }
        }

        for(Character ch : queue) {
            sb.append(ch);
        }

        return sb.reverse().toString();
    }

    /**
     * 逆波兰表达式求值
     */
    public int evalRPN(String[] tokens) {
        int res = 0;
        
        return res;
    }

}
