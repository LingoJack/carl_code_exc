package hashtable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class hashtable {
    
    /**
     * 有效的字母异位词
     * 这是最直接的利用HashMap，但不是最巧妙的
     * 坏了，忘记java怎么操作str的char了...
     * Java的语法基础还是不太牢固，先前竟然不知道Map的equals方法已经被重写了
     */
    public boolean isAnagram(String s, String t) {
        int lenS = s.length();
        int lenT = t.length();

        // 如果长度不同，则直接返回 false
        if (lenS != lenT) {
            return false;
        }

        // 创建两个 HashMap 来存储字符及其出现次数
        Map<Character, Integer> mapS = new HashMap<>();
        Map<Character, Integer> mapT = new HashMap<>();

        // 统计字符串 s 中每个字符的出现次数
        for (int i = 0; i < lenS; i++) {
            char ch = s.charAt(i);
            mapS.put(ch, mapS.getOrDefault(ch, 0) + 1);
        }

        // 统计字符串 t 中每个字符的出现次数
        for (int i = 0; i < lenT; i++) {
            char ch = t.charAt(i);
            mapT.put(ch, mapT.getOrDefault(ch, 0) + 1);
        }

        // 比较两个 HashMap 是否相等
        return mapS.equals(mapT);
    }

    public boolean isAnagramWithArrayASCII(String s, String t) {
        int[] record = new int[26];

        for (int i = 0; i < s.length(); i++) {
            // 并不需要记住字符a的ASCII，只要求出一个相对数值就可以了
            record[s.charAt(i) - 'a']++;
        }

        for (int i = 0; i < t.length(); i++) {
            record[t.charAt(i) - 'a']--;
        }
        
        for (int count: record) {
            // record数组如果有的元素不为零0，说明字符串s和t 一定是谁多了字符或者谁少了字符。
            if (count != 0) { 
                return false;
            }
        }

        // record数组所有元素都为零0，说明字符串s和t是字母异位词
        return true; 
    }

    /**
     * 两个数组的交集
     * 这是最直接，也是最低效率的解法
     * 这里是直接暴力的两个HashMap统计元素出现的次数
     */
    public int[] intersection(int[] nums1, int[] nums2) {
        int len1 = nums1.length;
        int len2 = nums2.length;

        Map<Integer, Integer> map1 = new HashMap<>();
        Map<Integer, Integer> map2 = new HashMap<>();

        for(int i=0;i<len1;i++){
            int count = map1.getOrDefault(nums1[i], 0);
            count++;
            map1.put(nums1[i], count);
        }

        for(int i=0;i<len2;i++){
            int count = map2.getOrDefault(nums2[i], 0);
            count++;
            map2.put(nums2[i], count);
        }

        List<Integer> res = new ArrayList<>();

        for(Map.Entry<Integer, Integer> entry : map1.entrySet()){
            if (entry.getValue() >= 1 && map2.getOrDefault(entry.getKey(), 0) >= 1) {
                res.add(entry.getKey());
            }
        }

        return res.stream().mapToInt(i -> i).toArray();
    }

    /**
     * 两个数组的交集
     * 但是使用Stream API
     * 基础还是不太牢固，对于Java的集合体系提供的API了解不深，比如HashSet
     */
    public int[] intersectionWithStreamAPI(int[] nums1, int[] nums2) {
        // 使用 IntStream 和 boxed 将数组转换为 HashSet
        HashSet<Integer> set = IntStream.of(nums2)
                                        .boxed()
                                        .collect(Collectors.toCollection(HashSet::new));
        return Arrays.stream(nums1).boxed().filter(e -> set.contains(e)).distinct().mapToInt(e -> e).toArray();
    }

    /**
     * 快乐数
     * 可恶，初见竟无一点思路...
     * 快乐数示例：
     * 1^2 + 9^2 = 82
     * 8^2 + 2^2 = 68
     * 6^2 + 8^2 = 100
     * 1^2 + 0^2 + 0^2 = 1
     * 最后看答案了，用快慢指针来解决这个，这是人能想出来的吗？？？
     * 这点的关键是想到：
     * 如果一个数不是快乐数，那么它的平方和序列一定会循环？？？
     * 由于是循环的，那么fast绕一圈回来一定会追上show
     * 如果不是循环的，指 1，那么fast就会原地不动，show会追上fast
     * 关于“如果一个数不是快乐数，那么它的平方和序列一定会循环”的理由：
     * > 当你对一个整数进行“每个位上的数字平方和”的操作时，得到的结果是有限的。对于一个 k 位的整数 n，每位数字的最大值为 9，
     * > 因此每次操作后得到的新数最大不会超过 81k（因为 9^2 = 81）。这意味着无论初始数字多大，经过几次变换之后，结果将被限制在一个相对较小的范围内
     * > 这个过程生成的数字集合是有限的，并且每一步都是确定性的（给定一个输入，输出是固定的），如果我们不断重复这个过程而没有达到 1，
     * > 那么根据鸽巢原理（如果有 n 个容器和 n+1 个物品，至少有一个容器包含不止一个物品），最终一定会遇到之前出现过的数字，从而形成一个循环。
     * > 鸽巢原理的集合论表述：若A是n+1元集，B是n元集，则不存在从A到B的单射。（双射 = 单射 + 满射）
     */
    public boolean isHappy(int n) {
        int fast = n;
        int slow = n;
        do {
            slow=squareSum(slow);
            fast=squareSum(fast);
            fast=squareSum(fast);
            if (fast == 1) {
                return true;
            }
        } while (slow != fast);

        return fast == 1;
    }
    
    private int squareSum(int m){
        int squaresum=0;
        while (m != 0) {
           squaresum += (m % 10) * (m % 10);
            m /= 10;
        }
        return squaresum;
    }

    /**
     * 两数之和
     * 比较早想到了思路，就是实现起来有些细节卡顿了
     * 我想的这个解法时间复杂度蛮优秀的hhh
     */
    public int[] twoSum(int[] nums, int target) {
        // 这个Map用来存储下标，比如
        // 2 7 11 15 target=9
        // 那么遍历到2的时候，会以taget-2=7为key放入2的索引值，遍历每个元素时，先看看该map中以当前值为key有无value，有则取出
        // map的value加上当前key的索引组成的数组，即为target的两数之和的下标数组
        Map<Integer, Integer> idxs = new HashMap<>();
        for(int i=0;i<nums.length;i++) {
            if (idxs.get(nums[i]) != null) {
                return new int[]{i, idxs.get(nums[i])};
            }
            idxs.put(target - nums[i], i);
        }
        return new int[2];
    }

    /**
     * 四数相加II
     * 也是没想到思路，可是看了答案之后又觉得好简单...
     * 菜菜捞捞
     * 而且思路明明和上面的两数之和有点类似...
     */
    public int fourSumCount(int[] nums1, int[] nums2, int[] nums3, int[] nums4) {
        
        // 统计数组1和数组2的两数之和出现的次数的map
        Map<Integer, Integer> map = new HashMap<>();

        // 统计数组1、2的两数之和出现的次数
        for(int a : nums1) {
            for(int b : nums2) {
                int count = map.getOrDefault(a + b, 0);
                count++;
                map.put(a + b, count);
            }
        }

        // 解法数量
        int res = 0;

        // 计算数组3、4的两数之和，看看所需要的数组1、2的两数之和，如果出现了，就累加到count
        for(int c : nums3) {
            for(int d : nums4) {
                int need = 0 - c - d;
                Integer count = map.get(need);
                if (count != null) {
                    res += count;
                }
            }
        }

        return res;
    }

    /**
     * 赎金信
     * 同样是最直接的解法，时间复杂度和空间复杂度都不太优秀
     * 或许这里应该直接用数组，因为ASCII的大小都是比较限定的
     */
    public boolean canConstruct(String ransomNote, String magazine) {
        int lenR = ransomNote.length();
        int lenM = magazine.length();
        Map<Character, Integer> map = new HashMap<>(lenR);
        for(int i=0;i<lenR;i++){
            char ch = ransomNote.charAt(i);
            int count = map.getOrDefault(ch, 0);
            count++;
            map.put(ch, count);
        }
        
        for(int i=0;i<lenM;i++) {
            char ch = magazine.charAt(i);
            int count = map.getOrDefault(ch, 0);
            count--;
            map.put(ch, count);
        }

        for(int i=0;i<lenR;i++){
            char ch = ransomNote.charAt(i);
            if(map.get(ch) > 0) {
                return false;
            }
        }

        return true;
    }

    /**
     * 赎金信的数组解法
     */
    public boolean canConstructWithArray(String ransomNote, String magazine) {
        int lenR = ransomNote.length();
        int lenM = magazine.length();
        
        int[] chars = new int[26];
        // 这个赋值是多余的，int基本类型有默认值0
        // Arrays.fill(chars, 0);

        for(int i=0;i<lenR;i++){
            char ch = ransomNote.charAt(i);
            int index = ch - 'a';
            chars[index]++;
        }

        for(int i=0;i<lenM;i++) {
            char ch = magazine.charAt(i);
            int index = ch - 'a';
            chars[index]--;
        }

        for(int i=0;i<lenR;i++){
            char ch = ransomNote.charAt(i);
            int index = ch - 'a';
            if (chars[index] > 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 三数之和
     * 感觉这题和四数相加很像？？？
     * 既然要不重复的元组的话，我想加个Set
     * 时空间复杂度都糟糕透了
     * 比较好的思路应该是双指针的实现
     */
    public List<List<Integer>> threeSum(int[] nums) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            map.put(-nums[i], i);
        }

        List<List<Integer>> res = new ArrayList<>();
        Set<List<Integer>> set = new HashSet<>();

        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                int sum = nums[i] + nums[j];
                Integer val = map.get(sum);
                if (val != null && val != i && val != j) {
                    List<Integer> list = Arrays.asList(nums[i], nums[j], nums[val]);
                    Collections.sort(list);
                    if (!set.contains(list)) {
                        res.add(list);
                        set.add(list);
                    }
                }
            }
        }

        return res;
    }

    /**
     * 三数之和
     * 双指针的实现
     */
    public List<List<Integer>> threeSumWithDoublePointer(int[] nums) {
        
    }

}
