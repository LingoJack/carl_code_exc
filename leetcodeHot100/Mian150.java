package leetcodeHot100;

import java.util.ArrayList;
import java.util.List;

public class Mian150 {

    /**
     * 二叉搜索树迭代器
     */
    class BSTIterator {

        private int idx;

        private List<Integer> list;

        public BSTIterator(TreeNode root) {
            this.list = new ArrayList<>();
            this.idx = -1;
            inorder(root);
        }

        private void inorder(TreeNode node) {
            if (node == null) {
                return;
            }
            inorder(node.left);
            list.add(node.val);
            inorder(node.right);
        }

        public int next() {
            idx++;
            return list.get(idx);
        }

        public boolean hasNext() {
            return idx + 1 < list.size();
        }
    }

    /**
     * 只出现一次的数字II
     * 没做出来
     * 将每个数想象成32位的二进制，
     * 对于每一位的二进制的1和0累加起来必然是3N或者3N+1，
     * 为3N代表目标值在这一位没贡献，3N+1代表目标值在这一位有贡献(=1)，
     * 然后将所有有贡献的位|起来就是结果
     */
    public int singleNumber(int[] nums) {
        int ret = 0;
        for (int i = 0; i < 32; i++) {
            int mask = 1 << i;
            int cnt = 0;
            for (int j = 0; j < nums.length; j++) {
                if ((nums[j] & mask) != 0) {
                    cnt++;
                }
            }
            if (cnt % 3 != 0) {
                ret |= mask;
            }
        }
        return ret;
    }
}
