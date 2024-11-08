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
        LinkedList<Character> stack = new LinkedList<>();
        char[] str = s.toCharArray();
        for(int i = 0; i< str.length; i++) {
            switch (str[i]) {
                case '{':
                    stack.push(str[i]);
                    break;
                case '[':
                    stack.push(str[i]);
                    break;
                case '(':
                    stack.push(str[i]);
                    break;
                case '}':
                    if (stack.peek() != null && stack.peek().equals('{')) {
                        stack.poll();
                    }
                    else {
                        return false;
                    }
                    break;
                case ']':
                    if (stack.peek() != null && stack.peek().equals('[')) {
                        stack.poll();
                    }
                    else {
                        return false;
                    }
                    break;
                case ')':
                    if (stack.peek() != null && stack.peek().equals('(')) {
                        stack.poll();
                    }
                    else {
                        return false;
                    }
                    break;
                default:
                    break;
            }
        }
        return stack.isEmpty();
    }

    /**
     * 删除字符串中的所有相邻重复项
     * 对于堆栈,使用ArrayDeque可能是比LinkedList更好的选择
     * 呃...俄罗斯方块 or 消消乐
     */
    public String removeDuplicates(String s) {
        ArrayDeque<Character> stack = new ArrayDeque<>();
        StringBuilder sb = new StringBuilder();
        char[] str = s.toCharArray();
        for(int i = 0; i < str.length; i++) {
            char ch = str[i];
            if (stack.peek() == null || ch != stack.peek()) {
                stack.push(ch);
            }
            else {
                stack.poll();
                continue;
            }
        }

        for(Character ch : stack) {
            sb.append(ch);
        }

        return sb.reverse().toString();
    }

    /**
     * 逆波兰表达式求值
     * 有点像后缀表达式
     */
    public int evalRPN(String[] tokens) {
        ArrayDeque<Integer> stack = new ArrayDeque<>();
        int num1, num2, val;
        for (String token : tokens) {
            switch (token) {
                case "+":
                    num2 = stack.poll();
                    num1 = stack.poll();
                    stack.push(num1 + num2);
                    break;
                case "-":
                    num2 = stack.poll();
                    num1 = stack.poll();
                    stack.push(num1 - num2);
                    break;
                case "*":
                    num2 = stack.poll();
                    num1 = stack.poll();
                    stack.push(num1 * num2);
                    break;
                case "/":
                    num2 = stack.poll();
                    num1 = stack.poll();
                    if (num2 == 0) {
                        throw new ArithmeticException("Division by zero");
                    }
                    stack.push(num1 / num2);
                    break;
                default:
                    val = Integer.parseInt(token);
                    stack.push(val);
                    break;
            }
        }
        return stack.poll();
    }

    /**
     * 滑动窗口最大值
     * 这是我做随想录遇到第一个的困难级别的题目...
     * mad，理解错题目意思了做了半天，我理解成求滑动窗口和的最大值了
     * 如果是这种理解，这道题的难度应该只有medium
     */
    public int[] maxSlidingWindowError(int[] nums, int k) {
        if (nums.length <= k) {
            int sum = 0;
            for(int num : nums) {
                sum += num;
            }
            return new int[]{sum};
        }

        ArrayDeque<Integer> queue = new ArrayDeque<>(k + 1);
        int sum = 0;
        for(int i = 0; i < k; i++) {
            sum += nums[i];
            queue.offer(nums[i]);
        }

        int[] res = new int[nums.length - 2];
        res[0] = sum;
        int lastIntervalSum = sum;
        

        // 1,3,-1,-3,5,3,6,7   k=3   

        for(int i = 1; i < nums.length - 2; i++){
            int out = nums[i - 1];
            int in = nums[i + 2];
            lastIntervalSum = lastIntervalSum - out + in;
            res[i] = Math.max(res[i - 1], lastIntervalSum);
        }

        return res;
    }

    /**
     * 滑动窗口最大值
     * 这是我做随想录遇到第一个的困难级别的题目...
     * 老实说我不会，难道每个滑动窗口都要维护一个大顶堆吗？
     * 好家伙，暴力解还超时了
     * 等等，我是不是可以同时维护窗口内第一大和第二大的值
     * 放弃了，看题解是用的单调队列解法
     */
    public int[] maxSlidingWindow(int[] nums, int k) {
        // 单调队列,内部放的是元素索引
        ArrayDeque<Integer> deque = new ArrayDeque<>();
        
        int len = nums.length;
        
        int[] res = new int[len - k + 1];
        
        int idx = 0;
        
        for(int i = 0; i < len; i++) {
            // 在[i - k + 1, i] 中选到最大值
            // 队列头结点需要在[i - k + 1, i]范围内，不符合则要弹出
            while(!deque.isEmpty() && deque.peek() < i - k + 1){
                deque.poll();
            }

            // 要保证单调性，就要保证每次放进去的数字要比末尾的都大，否则也弹出
            while(!deque.isEmpty() && nums[deque.peekLast()] < nums[i]) {
                deque.pollLast();
            }

            deque.offer(i);

            // 因为单调，当i增长到符合第一个k范围的时候，每滑动一步都将队列头节点放入结果就行了
            if(i >= k - 1){
                res[idx++] = nums[deque.peek()];
            }
        }
        return res;
    }


    

}