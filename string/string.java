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
        for (int i = 0; i <= len; i += (2 * k)) {
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
        for (char ch : chars) {
            if (ch == '0' || ch == '1' || ch == '2' || ch == '3' || ch == '4' || ch == '5' || ch == '6' || ch == '7'
                    || ch == '8' || ch == '9') {
                sb.append("number");
            } else {
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
        // a b c d
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
            while (fast < len && chars[fast] != ' ') {
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
     * 找出字符串中第一个匹配项的下标
     * 实现 strStr()
     * 没做出来
     * 这个涉及到KMP算法，关键是要求前缀表
     * 你要弄清楚前缀表的概念，并且要掌握求前缀表的逻辑
     * 这道题不是我自己写出来的
     */
    public int strStr(String haystack, String needle) {
        if (needle.isEmpty()) {
            return 0;
        }
        if (haystack.isEmpty() || haystack.length() < needle.length()) {
            return -1;
        }
        int[] prefix = buildPrefixTable(needle);
        // j是指向待匹配的模板的，即needle
        int j = 0;
        // aabaaabaaac
        // i
        // aabaaac
        // j
        // aabaaac
        // 0101220
        for (int i = 0; i < haystack.length(); i++) {
            while (j > 0 && haystack.charAt(i) != needle.charAt(j)) {
                j = prefix[j - 1]; // 使用前缀表跳过重复匹配
            }
            if (haystack.charAt(i) == needle.charAt(j)) {
                j++;
            }
            if (j == needle.length()) {
                return i - j + 1; // 找到匹配位置
            }
        }
        return -1;
    }

    /**
     * KMP前缀表的构建是一个难点
     * 一个串中查找是否出现过另一个串，这是KMP的看家本领
     * todo 需要理解构建的逻辑
     */
    private int[] buildPrefixTable(String s) {
        char[] str = s.toCharArray();
        int[] prefix = new int[str.length];
        int j = 0;
        prefix[0] = 0;
        // a b e a b f
        // j
        // i
        // 0 0 0 1 2 0
        // 计算字符串每一位的最长前后缀
        for (int i = 1; i < str.length; i++) {
            while (j > 0 && str[j] != str[i]) {
                // 回退到上一次匹配的前缀的前一个位置
                j = prefix[j - 1];
            }
            // str[i] == str[j] or j == 0
            if (str[j] == str[i]) {
                j++;
            }
            prefix[i] = j;
        }
        return prefix;
    }

    /**
     * 重复的子字符串
     * 这个感觉有点像构建KMP的前缀表的逻辑
     * 字符串应该是我的弱点
     * 这个是大神的顶级妙解
     * 思路解析如下：
     * 假设判断字符串s（n个字符构成）是不是由重复子串构成的，我们先将两个字串拼成一个大的字符串：S = s + s（S 有2n个字符）；
     * 字符串拼接的最底层思想应该是使用字符串移位来判断字符串是否由重复子串（循环节）构成，
     * 因为S这个大的字符串 其实包含了字符串s的所有移位字符串。令字符串字符索引从0开始，[m,n]表示S中索引为m到索引为n的这一段字符，索引为闭区间，
     * 则[0,n-1]即S的前一半字符表示原始字符串s；[1,n]表示s右移1位的状态；[2,n+1]表示s右移2位的状态；······；[n,2n-1]即S的后半拉字符串可以理解为s移n位的状态，此时s已经移回了原始的状态了；
     * 此外我们不难知道如果一个没有循环节的字符串在移位时必须要右移n次他才能移回它的原始状态，而有循环节的字符串最多只需要n/2次（只有两个循环节）；
     * 所以当S砍去首尾字符时，对于没有循环节的字符串s，相当于砍去了[0,n-1]的原始状态和[n,2n-1]这个移位n次的又回归原始的状态，
     * 我们在[1,2n-2]范围内只能找到s移位1，2，···，n-1位时的状态，
     * 所以在[1,2n-2]内是不存在无循环节的s的；但是对于有循环节的s来说，n/2 <= n-1，所以一定存在至少一个移位状态为s，
     * 即最少存在一个s（其实对于有循环节的s来说，不考虑移位状态我们也能明白[1,2n-2]
     * 内一定至少有一个s，例如对于有两个循环节的s：组成S的前一个s的后一半字符 + 组成S的后一个s的前一半的字符 == s；三个循环节的s更不用说了)；
     */
    public boolean repeatedSubstringPattern(String s) {
        String str = s + s;
        return str.substring(1, str.length() - 1).contains(s);
    }
}
