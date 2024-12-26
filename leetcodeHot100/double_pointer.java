package leetcodeHot100;

public class double_pointer {
    /**
     * 盛最多水的容器
     * 思路接近，但是错了
     * 本题的核心是想到根据柱子的高度来判断，也就是每次都移动较小的柱子才有可能增加高度
     * 就这样遍历直到左右两边柱子相等；
     * 而我的错误做法是每次都判断移动左右柱子后的面积，然后根据面积来移动左右柱子
     * 错误的原因是，我这么做，其实是贪心的，但是可能会丢失潜在的最大面积
     */
    public int maxArea(int[] height) {
        int rt = height.length - 1;
        int lt = 0;
        int res = calcArea(height, lt, rt);
        while (rt > lt) {
            if (height[rt] > height[lt]) {
                lt++;
            }
            else {
                rt--;
            }
            res = Math.max(res, calcArea(height, lt, rt));
        }
        return res;
    }

    private int calcArea(int[] height, int lt, int rt) {
        int w = rt - lt;
        int h = Math.min(height[lt], height[rt]);
        return w * h;
    }
}
