package doublepointer;

public class doublepointer {
    /**
     * 移除元素
     * 这题是之前在数组篇里面做过的，这里当是重温一遍
     * 思路就是双指针，快指针去找不符合的条件的，然后和慢指针交换
     * 这样就把所有符合条件的都换到前面来了
     * 这次再写一次，秒杀了
     */
    public int removeElement(int[] nums, int val) {
        int fast = 0;
        int slow = 0;
        
        // 3,2,2,3  val = 3
        //   f
        // s
        // 2 3 2 3
        //     f
        //   s
        // 
        int temp = 0;
        while (fast != nums.length) {
            if (nums[fast] != val) {
                temp = nums[fast];
                nums[fast] = nums[slow];
                nums[slow] = temp;

                slow++;
            }
            fast++;
        }

        return slow;
    }

}
