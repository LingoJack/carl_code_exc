package string;

import java.util.Scanner;

public class string {

    /**
     * 反转字符串
     * 秒了
     * 这不就是最基本的双指针吗...
     */
    public void reverseString(char[] s) {
        int len = s.length;
        int lt = 0;
        int rt = len - 1;
        char temp = 0;
        while (lt < rt) {
            temp = s[lt];
            s[lt] = s[rt];
            s[rt] = temp;
            lt++;
            rt--;
        }
    }
    
    /**
     * 反转字符串 II
     * 需要注意的是防止字符数组越界
     * 对边界判断的能力还是太弱
     * 不过第一次做能想到在for表达式上做文章而不是用计数器，已经很不错了
     */
    public String reverseStr(String s, int k) {
        int len = s.length();
        char[] chars = s.toCharArray();
        char temp = 'a';

        // abcdefg k=2
        // lt = 0, rt = 0 + 2 - 1 = 1
        for(int i = 0; i <= len; i += (2*k)) {
            int lt = i;
            int rt = Math.min(lt + k - 1, len - 1);
            while (lt < rt) {
                temp = chars[lt];
                chars[lt] = chars[rt];
                chars[rt] = temp;
                lt++;
                rt--;
            }
        }

        return new String(chars);
    }

    /**
     * 替换数字 ACM模式
     * 一次过hhh
     */
    public static void replaceNumber() {
        Scanner sc = new Scanner(System.in);
        String s = sc.next();
        char[] chars = s.toCharArray();
        StringBuilder sb = new StringBuilder();
        for(char ch : chars) {
            if (ch == '0' || ch == '1' || ch == '2' || ch == '3' || ch == '4' || ch == '5' || ch == '6' || ch == '7' || ch == '8' || ch == '9') {
                sb.append("number");
            }
            else {
                sb.append(ch);
            }
        }
        System.out.println(sb.toString());
    }

    public static void main(String[] args) {
        replaceNumber();
    }

    /**
     * 翻转字符串里的单词
     */
    public String reverseWords(String s) {
        
        return null;
    }
}
