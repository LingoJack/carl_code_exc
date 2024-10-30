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
     * 不能使用split库函数
     * 感觉是你把逻辑写麻烦了，其实可以直接两个指针往后扫，然后逐步把单词从右开始添加到新数组
     * 用新的逻辑实现了，这击败率可以啊！
     * 执行用时1ms 击败100%
     * 消耗内存分布 41.76MB 击败70.99%
     * 这不是原地算法，如果希望原地的话，可以考虑先把整个字符数组反转过来，然后再单独扫一遍单词，把单词再反转一次
     */
    public String reverseWords(String s) {
        // the sky is blue
        // a b        c  d
        // 去除输入字符串首尾的空格
        s = s.trim();
        char[] chars = s.toCharArray();
        int len = chars.length;

        char[] res = new char[len];
        int fast = 0;
        int show = 0;
        int tail = len - 1;
        int cur = 0;

        while (fast < len && show < len) {
            // 找到单词的开始的位置
            while (show < len && chars[show] == ' ') {
                show++;
            }
            fast = show;
            
            // 找到单词结束的位置
            while (fast < len && chars[fast] != ' ' ) {
                fast++;
            }
            fast--;

            // 把单词迁移到新数组
            cur = fast;
            while (cur >= show) {
                res[tail] = chars[cur];
                tail--;
                cur--;
            }
            if (tail > 0) {
                res[tail] = ' ';
            }
            tail--;

            // 更新指针的位置
            show = fast + 1;
        }

        // 将结果字符串去除首尾的空格后返回
        return new String(res).trim();
    }

    /**
     * 右旋字符串
     * ACM模式
     * 要求是在chars上原地修改，可以通过多次反转来实现
     * time: 1445ms
     * memory: 13840kb
     */
    public static void rightRotateStr(String s, int n) {
        char[] chars = s.toCharArray();
        // a b c d e f g
        // g f e d c b a
        // f g a b c d e
        // 可以通过多次反转来实现
        // 2
        int len = s.length();
        reverseArray(chars, 0, len - 1);
        reverseArray(chars, 0, n - 1);
        reverseArray(chars, n, len - 1);
        System.out.println(new String(chars));
    }

    public static void reverseArray(char[] chars, int start, int end) {
        int lt = start;
        int rt = end;
        char t;
        while (lt < rt) {
            t = chars[lt];
            chars[lt] = chars[rt];
            chars[rt] = t;
            lt++;
            rt--;
        }
    }

    /**
     * 实现 strStr()
     */
    public int strStr(String haystack, String needle) {
       if (needle.length() == 0) {
            return 0;
       }

       // 构建前缀表
       int[] prefix = buildPrefixTable(needle);

        // 
        char[] pattern = needle.toCharArray();
        char[] str = haystack.toCharArray();

        int lt = 0;
        int rt = 0;

        while () {
            
        }


    }

}
