package leetcodeHot100;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;

public class Sorter {

    public static void main(String[] args) {
        int[] nums = new int[] { 1, 2, 5, 4, 3 };
        Sorter sorter = new Sorter();
        nums = sorter.heapSort(nums);
        for (int num : nums) {
            System.out.print(num + " ");
        }
    }

    /**
     * 快排
     * 10min
     */
    public int[] quickSort(int[] nums) {
        // 1 3 2 6 4
        // l r
        quickSortHelper(nums, 0, nums.length - 1);
        return nums;
    }

    private void quickSortHelper(int[] nums, int start, int end) {
        if (end > start) {
            int mid = partitionQuickSort(nums, start, end);
            quickSortHelper(nums, start, mid - 1);
            quickSortHelper(nums, mid + 1, end);
        }
    }

    private int partitionQuickSort(int[] nums, int start, int end) {
        int pivot = end;
        int slow = start;
        int fast = start;
        while (fast < end) {
            if (nums[pivot] > nums[fast]) {
                swap(nums, slow, fast);
                slow++;
            }
            fast++;
        }
        swap(nums, slow, pivot);
        return slow;
    }

    private void swap(int[] nums, int a, int b) {
        int t = nums[a];
        nums[a] = nums[b];
        nums[b] = t;
    }

    /**
     * 归并排序
     * 递归写法
     */
    public int[] mergeSortWithRecursion(int[] nums) {
        if (nums == null || nums.length <= 1) {
            return nums;
        }
        int[] res = new int[nums.length];
        System.arraycopy(nums, 0, res, 0, nums.length);
        partition(res, 0, nums.length - 1);
        return res;
    }

    /**
     * 分割数组，并递归地对每个分割后的子数组进行排序
     */
    private void partition(int[] nums, int start, int end) {
        if (start >= end) {
            return;
        }
        int mid = start + (end - start) / 2; // 防止计算时溢出
        partition(nums, start, mid);
        partition(nums, mid + 1, end);
        merge(nums, start, mid, end);
    }

    /**
     * 合并两个有序的子数组
     */
    private void merge(int[] nums, int start, int mid, int end) {
        int[] temp = new int[end - start + 1];
        int i = start, j = mid + 1, k = 0;
        // 比较左右两部分的元素，按顺序添加到临时数组中
        while (i <= mid && j <= end) {
            if (nums[i] <= nums[j]) {
                temp[k++] = nums[i++];
            } else {
                temp[k++] = nums[j++];
            }
        }
        // 将左边剩余的元素添加到临时数组中
        while (i <= mid) {
            temp[k++] = nums[i++];
        }
        // 将右边剩余的元素添加到临时数组中
        while (j <= end) {
            temp[k++] = nums[j++];
        }
        // 把临时数组中的元素复制回原数组
        System.arraycopy(temp, 0, nums, start, temp.length);
    }

    /**
     * 归并排序
     * 迭代版本
     */
    public int[] mergeSortWithIterate(int[] nums) {
        if (nums == null || nums.length <= 1) {
            return nums;
        }
        int[] res = Arrays.copyOf(nums, nums.length);
        int[] aux = new int[nums.length];
        for (int step = 1; step < nums.length; step *= 2) {
            for (int start = 0; start < nums.length; start += 2 * step) {
                int mid = Math.min(start + step, nums.length);
                int end = Math.min(start + 2 * step, nums.length);
                mergeForIterate(res, aux, start, mid, end);
            }
            System.arraycopy(aux, 0, res, 0, nums.length);
        }
        return res;
    }

    private void mergeForIterate(int[] nums, int[] aux, int start, int mid, int end) {
        int left = start, right = mid, cur = start;
        while (left < mid && right < end) {
            if (nums[left] <= nums[right]) {
                aux[cur++] = nums[left++];
            } else {
                aux[cur++] = nums[right++];
            }
        }
        while (left < mid) {
            aux[cur++] = nums[left++];
        }
        while (right < end) {
            aux[cur++] = nums[right++];
        }
    }

    /**
     * 堆排序
     * 1. 父子关系
     * 2. 插入的时候，
     * 3. 取出的时候，
     */
    public int[] heapSort(int[] nums) {
        for (int i = (nums.length - 1) / 2; i >= 0; i--) {
            heapify(nums, i, nums.length);
        }
        for (int i = nums.length - 1; i >= 0; i--) {
            swap(nums, i, 0);
            heapify(nums, 0, i);
        }
        return nums;
    }

    private void heapify(int[] data, int index, int curHeapSize) {
        int ltSon = 2 * index + 1;
        int rtSon = 2 * index + 2;
        int largestIndex = index;
        if (rtSon < curHeapSize && data[rtSon] > data[largestIndex]) {
            largestIndex = rtSon;
        }
        if (ltSon < curHeapSize && data[ltSon] > data[largestIndex]) {
            largestIndex = ltSon;
        }
        if (largestIndex != index) {
            swap(data, largestIndex, index);
            heapify(data, largestIndex, curHeapSize);
        }
    }

    private void upCheck(int[] data, int index) {
        int parent = (index - 1) / 2;
        if (parent < 0) {
            return;
        }
        if (data[parent] < data[index]) {
            swap(data, parent, index);
            upCheck(data, parent);
        }
    }

    class MaxHeap {

        private ArrayList<Integer> heap;

        public MaxHeap() {
            this.heap = new ArrayList<>();
        }

        // 插入一个新元素到堆中
        public void offer(int val) {
            heap.add(val);
            upCheck(heap.size() - 1);
        }

        // 移除并返回堆顶的最大元素
        public int removeMax() {
            if (heap.isEmpty()) {
                throw new NoSuchElementException("Heap is empty");
            }
            int max = heap.get(0);
            if (heap.size() > 1) {
                heap.set(0, heap.remove(heap.size() - 1)); // 将最后一个元素移到根部，并减少堆的大小
                heapify(0);
            } else {
                heap.clear(); // 如果堆中只有一个元素，移除后清空列表
            }
            return max;
        }

        // 构建最大堆，通常在初始化时调用
        public void buildHeap(ArrayList<Integer> data) {
            heap = new ArrayList<>(data); // 复制输入数据到堆列表
            for (int i = (heap.size() - 2) / 2; i >= 0; i--) {
                heapify(i);
            }
        }

        // 堆化操作，确保从给定索引开始满足最大堆属性
        private void heapify(int index) {
            int largestIndex = index;
            int left = 2 * index + 1;
            int right = 2 * index + 2;

            if (left < heap.size() && heap.get(left) > heap.get(largestIndex)) {
                largestIndex = left;
            }

            if (right < heap.size() && heap.get(right) > heap.get(largestIndex)) {
                largestIndex = right;
            }

            if (largestIndex != index) {
                swap(index, largestIndex);
                heapify(largestIndex); // 递归地堆化受影响的子树
            }
        }

        // 向上检查并调整堆，用于插入新元素后的调整
        private void upCheck(int index) {
            int parent = (index - 1) / 2;
            if (parent >= 0 && heap.get(parent) < heap.get(index)) {
                swap(parent, index);
                upCheck(parent);
            }
        }

        // 交换两个元素的位置
        private void swap(int i, int j) {
            int temp = heap.get(i);
            heap.set(i, heap.get(j));
            heap.set(j, temp);
        }

        // 打印堆的内容，用于调试
        public void printHeap() {
            System.out.println(heap.toString());
        }

        // 获取堆的大小
        public int size() {
            return heap.size();
        }

        // 检查堆是否为空
        public boolean isEmpty() {
            return heap.isEmpty();
        }

        // 查看堆顶元素而不移除它
        public Integer peek() {
            if (isEmpty()) {
                return null;
            }
            return heap.get(0);
        }
    }

}
