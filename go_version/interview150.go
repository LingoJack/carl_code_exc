package main

import (
	"container/list"
	"fmt"
	"math"
	"slices"
	"sort"
	"strconv"
	"strings"
)

func main() {

}

// 合并两个有序数组
func mergeTwoOrderedArrays(nums1 []int, m int, nums2 []int, n int) {
	idx1, idx2 := m-1, n-1
	idx := m + n - 1
	for idx1 >= 0 || idx2 >= 0 {
		var num1, num2 int
		if idx1 < 0 {
			num1 = math.MinInt
		} else {
			num1 = nums1[idx1]
		}
		if idx2 < 0 {
			num2 = math.MinInt
		} else {
			num2 = nums2[idx2]
		}
		if num1 > num2 {
			nums1[idx] = num1
			idx1--
		} else {
			nums1[idx] = num2
			idx2--
		}
		idx--
	}
}

// 买卖股票的最佳时机
func maxProfit(prices []int) int {
	minPrice := math.MaxInt32
	maxProfit := 0
	for _, price := range prices {
		if price < minPrice {
			minPrice = price
		} else {
			maxProfit = max(maxProfit, price-minPrice)
		}
	}
	return maxProfit
}

// 移除元素
func removeElement(nums []int, val int) int {
	slow, fast := 0, 0
	len := len(nums)
	for fast < len {
		if nums[fast] != val {
			nums[slow], nums[fast] = nums[fast], nums[slow]
			slow++
		}
		fast++
	}
	return slow
}

// 买卖股票的最佳时机II
func maxProfitII(prices []int) int {
	profit := 0
	for idx, price := range prices[:len(prices)-1] {
		if prices[idx+1] > price {
			profit += prices[idx+1] - price
		}
	}
	return profit
}

// 跳跃游戏
func canJump(nums []int) bool {
	scope, idx := 0, 0
	for idx <= scope && idx < len(nums) {
		scope = max(scope, idx+nums[idx])
		if scope >= len(nums)-1 {
			return true
		}
	}
	return false
}

// 跳跃游戏II
func jump(nums []int) int {
	l := len(nums)
	farthest := make([]int, l)
	scope := 0
	for idx, val := range nums {
		scope = max(idx+val, scope)
		farthest[idx] = scope
	}
	count := 0
	scope = 0
	idx := 0
	for idx < l-1 {
		idx = farthest[idx]
		count++
	}
	return count
}

// 除自身以外数组的乘积
func productExceptSelf(nums []int) []int {
	prefixProd, suffixProd := make([]int, len(nums)), make([]int, len(nums))
	prefixProd[0] = nums[0]
	suffixProd[len(nums)-1] = nums[len(nums)-1]
	for i := 1; i < len(nums); i++ {
		prefixProd[i] = prefixProd[i-1] * nums[i]
	}
	for i := len(nums) - 2; i >= 0; i-- {
		suffixProd[i] = suffixProd[i+1] * nums[i]
	}
	res := make([]int, len(nums))
	for i := range nums {
		var prefix, suffix int
		if i == 0 {
			prefix = 1
			suffix = suffixProd[i+1]
		} else if i == len(nums)-1 {
			suffix = 1
			prefix = prefixProd[i-1]
		} else {
			prefix = prefixProd[i-1]
			suffix = suffixProd[i+1]
		}
		res[i] = prefix * suffix
	}
	return res
}

// 加油站
// 这个解法效率不高，最好的是计算diff数组，然后求最大子数组和
func canCompleteCircuit(gas []int, cost []int) int {
	l := len(gas)
	for start := range gas {
		if gas[start] <= cost[start] && start < l-1 {
			continue
		}
		curGas := 0
		canComplete := true
		for i := start; i < start+l; i++ {
			idx := i % l
			curGas += gas[idx]
			if curGas < cost[idx] {
				canComplete = false
				break
			}
			curGas -= cost[idx]
		}
		if canComplete {
			return start
		}
	}
	return -1
}

// 加油站
func canCompleteCircuitBetterSolution(gas []int, cost []int) int {
	l := len(gas)
	profit := make([]int, l)
	total := 0
	for i := range gas {
		profit[i] = gas[i] - cost[i]
		total += profit[i]
	}
	if total < 0 {
		return -1
	}
	sum := 0
	start := 0
	for i, v := range profit {
		if sum < 0 {
			sum = 0
			start = i
		}
		sum += v
	}
	return start
}

// 分发糖果
func candy(ratings []int) int {
	l, candy := len(ratings), make([]int, len(ratings))
	for i := range candy {
		candy[i] = 1
	}
	for i := 0; i < l-1; i++ {
		if ratings[i] < ratings[i+1] && candy[i] >= candy[i+1] {
			candy[i+1] = candy[i] + 1
		}
	}
	for i := l - 1; i > 0; i-- {
		if ratings[i-1] > ratings[i] && candy[i] >= candy[i-1] {
			candy[i-1] = candy[i] + 1
		}
	}
	res := 0
	for _, v := range candy {
		res += v
	}
	return res
}

// 接雨水
func trapAnotherSolution(height []int) int {
	stack := list.New()
	res := 0
	for i, h := range height {
		for stack.Len() > 0 && height[stack.Back().Value.(int)] < h {
			top := stack.Remove(stack.Back()).(int)
			if stack.Len() == 0 {
				break
			}
			left := stack.Back().Value.(int)
			width := i - left - 1
			boundedHeight := min(height[left], h) - height[top]
			res += width * boundedHeight
		}
		stack.PushBack(i)
	}
	return res
}

// 接雨水
func trap(height []int) int {
	stack := []int{}
	res := 0
	for i, h := range height {
		for len(stack) != 0 && height[stack[len(stack)-1]] < h {
			idx := stack[len(stack)-1]
			stack = stack[:len(stack)-1]
			if len(stack) == 0 {
				break
			}
			front := stack[len(stack)-1]
			res += (min(height[front], h) - height[idx]) * (i - front - 1)
		}
		stack = append(stack, i)
	}
	return res
}

// 反转字符串中的单词
func reverseWords(s string) string {
	var words []string
	sLen := len(s)
	fast := 0
	for fast < sLen && s[fast] == ' ' {
		fast++
	}
	slow := fast
	for fast < sLen {
		if s[fast] == ' ' {
			words = append(words, s[slow:fast])
			for fast < sLen && s[fast] == ' ' {
				fast++
			}
			slow = fast
		} else {
			fast++
		}
	}
	if slow < sLen && s[slow] != ' ' {
		words = append(words, s[slow:])
	}
	var sb strings.Builder
	for i := len(words) - 1; i >= 0; i-- {
		sb.WriteString(words[i])
		if i != 0 {
			sb.WriteString(" ")
		}
	}
	return sb.String()
}

func reverse(s string) string {
	chs := []byte(s)
	lt, rt := 0, len(s)-1
	for lt < rt {
		chs[lt], chs[rt] = s[rt], s[lt]
		lt++
		rt--
	}
	return s
}

// 找出字符串中第一个匹配项的下标
func strStr(haystack string, needle string) int {
	i, match := 0, 0
	partialMatchTable := preprocess(needle)
	for i < len(haystack) {
		if haystack[i] == needle[match] {
			match++
			if match == len(needle) {
				return i - len(needle) + 1
			}
			i++
			continue
		}
		if match > 0 {
			match = partialMatchTable[match-1]
		} else {
			i++
		}
	}
	return -1
}

func preprocess(needle string) []int {
	l := len(needle)
	table := make([]int, l)
	idx, matchIdx := 1, 0
	for idx < len(needle) {
		if needle[idx] == needle[matchIdx] {
			matchIdx++
			table[idx] = matchIdx
			idx++
			continue
		}
		if matchIdx > 0 {
			matchIdx = table[matchIdx-1]
		} else {
			table[idx] = 0
			idx++
		}
	}
	return table
}

// 盛最多水的容器
func maxArea(height []int) int {
	lt, rt := 0, len(height)-1
	res := 0
	for lt < rt {
		w := rt - lt
		h := min(height[lt], height[rt])
		res = max(res, w*h)
		if height[lt] < height[rt] {
			lt++
		} else {
			rt--
		}
	}
	return res
}

// 三数之和
func threeSum(nums []int) [][]int {
	sort.Ints(nums)
	res := make([][]int, 0)
	first := 0
	for first < len(nums)-2 {
		second, third := first+1, len(nums)-1
		for second < third {
			sum := nums[first] + nums[second] + nums[third]
			if sum == 0 {
				res = append(res, []int{nums[first], nums[second], nums[third]})
				second++
				for second < third && nums[second] == nums[second-1] {
					second++
				}
				third--
				for second < third && nums[third] == nums[third+1] {
					third--
				}
			} else if sum < 0 {
				second++
			} else {
				third--
			}
		}
		first++
		for first > 0 && first < len(nums)-2 && nums[first] == nums[first-1] {
			first++
		}
	}
	return res
}

// 长度最小的子数组
func minSubArrayLen(target int, nums []int) int {
	lt, rt, sum, res, hasFound := 0, 0, 0, len(nums), false
	for rt < len(nums) {
		sum += nums[rt]
		for sum >= target {
			hasFound = true
			res = min(res, rt-lt+1)
			sum -= nums[lt]
			lt++
		}
		rt++
	}
	if !hasFound {
		return 0
	}
	return res
}

// 无重复字符的最长子串
func lengthOfLongestSubstring(s string) int {
	charMap := make(map[rune]bool)
	runes := []rune(s)
	lt, rt, len, max := 0, 0, len(runes), 0
	for rt < len {
		for charMap[runes[rt]] {
			delete(charMap, runes[lt])
			lt++
		}
		charMap[runes[rt]] = true
		if max < rt-lt+1 {
			max = rt - lt + 1
		}
		rt++
	}
	return max
}

// 最小覆盖子串
func minWindow(s string, t string) string {
	need, have := make(map[rune]int), make(map[rune]int)
	sRunes, tRunes := []rune(s), []rune(t)
	for _, r := range tRunes {
		need[r]++
	}
	lt, rt := 0, 0
	valid := 0
	start, minLen := -1, math.MaxInt
	for rt < len(sRunes) {
		rc := sRunes[rt]
		if _, exist := need[rc]; exist {
			have[rc]++
			if have[rc] == need[rc] {
				valid++
			}
		}
		for valid >= len(need) {
			if minLen > rt-lt+1 {
				minLen = rt - lt + 1
				start = lt
			}
			lc := sRunes[lt]
			if _, exist := need[lc]; exist {
				if have[lc] == need[lc] {
					valid--
				}
				have[lc]--
			}
			lt++
		}
		rt++
	}
	if start == -1 {
		return ""
	}
	return s[start : start+minLen]
}

// 螺旋矩阵
func spiralOrder(matrix [][]int) []int {
	row, col := len(matrix), len(matrix[0])
	res := make([]int, 0)
	rowIdx, colIdx := 0, 0
	upBound, downBound, leftBound, rightBound := 0, row-1, 0, col-1
	count, total := 0, row*col
	for count < total {
		for i := leftBound; count < total && i <= rightBound; i++ {
			res = append(res, matrix[rowIdx][i])
			colIdx = i
			count++
		}
		upBound++
		for i := upBound; count < total && i <= downBound; i++ {
			res = append(res, matrix[i][colIdx])
			rowIdx = i
			count++
		}
		rightBound--
		for i := rightBound; count < total && i >= leftBound; i-- {
			res = append(res, matrix[rowIdx][i])
			colIdx = i
			count++
		}
		downBound--
		for i := downBound; count < total && i >= upBound; i-- {
			res = append(res, matrix[i][colIdx])
			rowIdx = i
			count++
		}
		leftBound++
	}
	return res
}

// 旋转图像
func rotate(nums [][]int) {
	row, col := len(nums), len(nums[0])
	for i := 0; 2*i < col; i++ {
		for j := 0; j < col; j++ {
			nums[i][j], nums[row-1-i][j] = nums[row-1-i][j], nums[i][j]
		}
	}
	for i := 0; i < row; i++ {
		for j := 0; j < i; j++ {
			nums[i][j], nums[j][i] = nums[j][i], nums[i][j]
		}
	}
}

// 矩阵置零
func setZeroes(matrix [][]int) {
	row, col := len(matrix), len(matrix[0])
	zeroRows, zeroCols := make([]bool, row), make([]bool, col)
	for i, row := range matrix {
		for j, v := range row {
			if v == 0 {
				zeroRows[i] = true
				zeroCols[j] = true
			}
		}
	}
	for i, b := range zeroRows {
		if b {
			for j := 0; j < col; j++ {
				matrix[i][j] = 0
			}
		}
	}
	for j, b := range zeroCols {
		if b {
			for i := 0; i < row; i++ {
				matrix[i][j] = 0
			}
		}
	}
}

// 赎金信
// 做出来了，但是效率不高
// 由于都是小写字母，所以可以直接用数组来表示
func canConstruct(ransomNote string, magazine string) bool {
	valid := 0
	need, have := make(map[rune]int), make(map[rune]int)
	for _, v := range []rune(ransomNote) {
		need[v]++
	}
	for _, v := range []rune(magazine) {
		if _, exist := need[v]; exist {
			have[v]++
			if have[v] == need[v] {
				valid++
			}
		}
	}
	return valid >= len(need)
}

// 赎金信
// 数组写法
func canConstructWithArray(ransomNote string, magazine string) bool {
	count := [26]int{}
	for i := 0; i < len(ransomNote); i++ {
		count[ransomNote[i]-'a']++
	}
	for i := 0; i < len(magazine); i++ {
		count[magazine[i]-'a']--
	}
	for _, v := range count {
		if v > 0 {
			return false
		}
	}
	return true
}

// 有效的字母异位词
func isAnagram(s string, t string) bool {
	count := make([]int, 26)
	for _, c := range []byte(s) {
		count[c-'a']++
	}
	for _, c := range []byte(t) {
		count[c-'a']--
	}
	for _, v := range count {
		if v != 0 {
			return false
		}
	}
	return true
}

// 字母异位词分组
func groupAnagram(strs []string) [][]string {
	anagramListMap := make(map[string][]string)
	for _, s := range strs {
		chs := []byte(s)
		slices.Sort(chs)
		sorted := string(chs)
		anagramListMap[sorted] = append(anagramListMap[sorted], s)
	}
	// return slices.Collect(maps.Values(anagramListMap))
	res := make([][]string, 0)
	for _, list := range anagramListMap {
		res = append(res, list)
	}
	return res
}

// 两数之和
func twoSum(nums []int, target int) []int {
	m := make(map[int]int)
	for i, v := range nums {
		if _, ok := m[target-v]; ok {
			return []int{m[target-v], i}
		}
		m[v] = i
	}
	return nil
}

// 快乐数
func isHappy(n int) bool {
	exist := make(map[int]bool)
	sum := 0
	for {
		digit := (n % 10)
		sum += digit * digit
		n /= 10
		if n == 0 {
			if exist[sum] {
				return false
			} else if sum == 1 {
				return true
			}
			exist[sum] = true
			n = sum
			sum = 0
		}
	}
}

// 最长连续序列
func longestConsecutive(nums []int) int {
	exist := make(map[int]bool)
	for _, num := range nums {
		exist[num] = true
	}
	res := 0
	for num := range exist {
		if exist[num-1] {
			continue
		}
		count, repl := 0, num
		for exist[repl] {
			repl++
			count++
		}
		res = max(res, count)
	}
	return res
}

// 合并区间
func merge(intervals [][]int) [][]int {
	sort.Slice(intervals, func(i, j int) bool {
		if intervals[i][0] != intervals[j][0] {
			return intervals[i][0] < intervals[j][0]
		}
		return intervals[i][1] < intervals[j][1]
	})
	stack := make([][]int, 0)
	for _, interval := range intervals {
		if len(stack) != 0 {
			last := stack[len(stack)-1]
			if last[1] >= interval[0] {
				stack = stack[:len(stack)-1]
				stack = append(stack, []int{last[0], max(last[1], interval[1])})
				continue
			}
		}
		stack = append(stack, interval)
	}
	return stack
}

// 用最少数量的箭引爆气球
func findMinArrowShots(points [][]int) int {
	sort.Slice(points, func(i, j int) bool {
		if points[i][0] != points[j][0] {
			return points[i][0] < points[j][0]
		}
		return points[i][1] <= points[j][1]
	})
	count := 0
	edge := math.MinInt32
	for _, point := range points {
		if edge < point[0] {
			count++
			edge = point[1]
		}
		edge = min(edge, point[1])
	}
	return count
}

// 有效的括号
func isValid(s string) bool {
	stack := make([]byte, 0)
	match := make(map[byte]byte)
	match[']'] = '['
	match['}'] = '{'
	match[')'] = '('
	for _, c := range []byte(s) {
		if _, e := match[c]; e {
			if len(stack) == 0 || stack[len(stack)-1] != match[c] {
				return false
			}
			stack = stack[:len(stack)-1]
			continue
		}
		stack = append(stack, c)
	}
	return len(stack) == 0
}

// 最小栈
type MinStack struct {
	stack    []int
	minStack []int
}

func MinStackConstructor() MinStack {
	return MinStack{
		stack:    make([]int, 0),
		minStack: make([]int, 0),
	}
}

func (this *MinStack) Push(val int) {
	this.stack = append(this.stack, val)
	if len(this.minStack) == 0 || this.minStack[len(this.minStack)-1] >= val {
		this.minStack = append(this.minStack, val)
	}
}

func (this *MinStack) Pop() {
	val := this.stack[len(this.stack)-1]
	this.stack = this.stack[:len(this.stack)-1]
	if val == this.minStack[len(this.minStack)-1] {
		this.minStack = this.minStack[:len(this.minStack)-1]
	}
}

func (this *MinStack) Top() int {
	return this.stack[len(this.stack)-1]
}

func (this *MinStack) GetMin() int {
	return this.minStack[len(this.minStack)-1]
}

// 了解Go中slice的复制原理
func GoSliceCopyTest() {
	arr := []int{
		1, 2, 3, 4, 5,
	}
	for _, num := range arr {
		fmt.Print(num, " ")
	}
	fmt.Println("brr: ")
	brr := arr[:len(arr)-1]
	brr[0] = -1
	for _, num := range brr {
		fmt.Print(num, " ")
	}
	fmt.Println("arr: ")
	for _, num := range arr {
		fmt.Print(num, " ")
	}
}

// LCR120.寻找文件副本
func findRepeatDocument(documents []int) int {
	exist := make([]bool, len(documents))
	for _, document := range documents {
		if exist[document] {
			return document
		}
		exist[document] = true
	}
	return -1
}

// 逆波兰表达式求值
func evalRPN(tokens []string) int {
	stack := make([]int, 0)
	for _, token := range tokens {
		if token == "+" || token == "-" || token == "*" || token == "/" {
			num1 := stack[len(stack)-1]
			stack = stack[:len(stack)-1]
			num2 := stack[len(stack)-1]
			stack = stack[:len(stack)-1]
			num := 0
			switch token {
			case "+":
				num = num1 + num2
			case "-":
				num = num2 - num1
			case "*":
				num = num1 * num2
			case "/":
				num = num2 / num1
			}
			stack = append(stack, num)
			continue
		}
		num, _ := strconv.Atoi(token)
		stack = append(stack, num)
	}
	return stack[len(stack)-1]
}

// 环形链表
func hasCycle(head *ListNode) bool {
	slow, fast := head, head
	for fast != nil && fast.Next != nil {
		fast = fast.Next.Next
		slow = slow.Next
		if fast == slow {
			return true
		}
	}
	return false
}

// 两数相加
func addTwoNumbers(l1 *ListNode, l2 *ListNode) *ListNode {
	carry := 0
	node1, node2 := l1, l2
	num1, num2 := 0, 0
	dummy := &ListNode{}
	last := dummy
	for node1 != nil || node2 != nil || carry != 0 {
		if node1 == nil {
			num1 = 0
		} else {
			num1 = node1.Val
			node1 = node1.Next
		}
		if node2 == nil {
			num2 = 0
		} else {
			num2 = node2.Val
			node2 = node2.Next
		}
		num := num1 + num2 + carry
		carry = num / 10
		num = num % 10
		node := &ListNode{
			Val:  num,
			Next: nil,
		}
		last.Next = node
		last = node
	}
	return dummy.Next
}

// 合并两个有序链表
func mergeTwoLists(list1 *ListNode, list2 *ListNode) *ListNode {
	n1, n2 := list1, list2
	dummy := &ListNode{}
	last := dummy
	v1, v2 := 0, 0
	for n1 != nil || n2 != nil {
		if n1 == nil {
			v1 = math.MaxInt32
		} else {
			v1 = n1.Val
		}
		if n2 == nil {
			v2 = math.MaxInt32
		} else {
			v2 = n2.Val
		}
		if v1 < v2 {
			last.Next = n1
			last = n1
			n1 = n1.Next
		} else {
			last.Next = n2
			last = n2
			n2 = n2.Next
		}
	}
	return dummy.Next
}

// 随机链表的复制
func copyRandomList(head *RandomNode) *RandomNode {
	m := make(map[*RandomNode]*RandomNode)
	node := head
	for node != nil {
		newNode := &RandomNode{
			Val: node.Val,
		}
		m[node] = newNode
		node = node.Next
	}
	node = head
	for node != nil {
		newNode := m[node]
		newNode.Random = m[node.Random]
		newNode.Next = m[node.Next]
		node = node.Next
	}
	return m[head]
}

// 反转链表II
func reverseBetween(head *ListNode, left int, right int) *ListNode {
	count := 0
	dummy := &ListNode{Next: head}
	var lt, rt *ListNode
	last := dummy
	node := dummy
	for count < left {
		last = node
		node = node.Next
		count++
	}
	lt = node
	for count < right {
		node = node.Next
		count++
	}
	rt = node
	prevEnd, nextStart := last, rt.Next
	node = lt
	last = nil
	for node != nextStart {
		next := node.Next
		node.Next = last
		last = node
		node = next
	}
	prevEnd.Next = rt
	lt.Next = nextStart
	return dummy.Next
}

// K个一组反转链表
func reverseKGroup(head *ListNode, k int) *ListNode {
	dummy := &ListNode{Next: head}
	slow := head
	prevGourpEnd := dummy
	for slow != nil {
		count := 0
		fast := slow
		for count < k && fast != nil {
			fast = fast.Next
			count++
		}
		if count < k {
			break
		}
		nextGroupStart := fast
		newHead, newTail := reverseListNodes(slow, fast)
		newTail.Next = nextGroupStart
		prevGourpEnd.Next = newHead
		prevGourpEnd = newTail
		slow = nextGroupStart
	}
	return dummy.Next
}

// 反转链表并返回反转后的头尾节点，左闭右开
func reverseListNodes(head *ListNode, tail *ListNode) (newHead *ListNode, newTail *ListNode) {
	node := head
	var last *ListNode
	for node != tail {
		next := node.Next
		node.Next = last
		last = node
		node = next
	}
	newTail = head
	newHead = last
	return
}

// 删除链表的倒数第N个结点
func removeNthFromEnd(head *ListNode, n int) *ListNode {
	dummy := &ListNode{Next: head}
	len := 0
	slow, fast := head, head
	for fast != nil {
		fast = fast.Next
		len++
	}
	last := dummy
	for len != n {
		last = slow
		slow = slow.Next
		len--
	}
	last.Next = slow.Next
	return dummy.Next
}

// 删除排序链表中的重复元素II
func deleteDuplicates(head *ListNode) *ListNode {
	dummy := &ListNode{Next: head}
	node, last := head, dummy
	for node != nil {
		next := node.Next
		duplicated := false
		for next != nil && next.Val == node.Val {
			node.Next = next.Next
			next = node.Next
			duplicated = true
		}
		if duplicated {
			last.Next = node.Next
		} else {
			last = node
		}
		node = node.Next
	}
	return dummy.Next
}

// LRU缓存
type LRUCache struct {
	cache    map[int]*CacheEntry
	head     *CacheEntry
	tail     *CacheEntry
	capacity int
}

type CacheEntry struct {
	key  int
	val  int
	next *CacheEntry
	prev *CacheEntry
}

func Constructor(capacity int) LRUCache {
	head := CacheEntry{}
	tail := CacheEntry{}
	head.next = &tail
	tail.prev = &head
	return LRUCache{
		cache:    make(map[int]*CacheEntry),
		head:     &head,
		tail:     &tail,
		capacity: capacity,
	}
}

func (this *LRUCache) Get(key int) int {
	entry, exist := this.cache[key]
	if !exist {
		return -1
	}
	entry.prev.next = entry.next
	entry.next.prev = entry.prev
	entry.prev = nil
	entry.next = nil
	entry.next = this.head.next
	this.head.next.prev = entry
	entry.prev = this.head
	this.head.next = entry
	return entry.val
}

func (this *LRUCache) Put(key int, value int) {
	entry, exist := this.cache[key]
	if exist {
		entry.prev.next = entry.next
		entry.next.prev = entry.prev
		entry.next = nil
		entry.prev = nil
		this.head.next.prev = entry
		entry.next = this.head.next
		this.head.next = entry
		entry.prev = this.head
		entry.val = value
		return
	}
	entry = &CacheEntry{
		key: key,
		val: value,
	}
	this.cache[key] = entry
	entry.next = this.head.next
	entry.prev = this.head
	this.head.next.prev = entry
	this.head.next = entry
	if this.capacity < len(this.cache) {
		removedEntry := this.tail.prev
		removedEntry.prev.next = removedEntry.next
		removedEntry.next.prev = removedEntry.prev
		removedEntry.next = nil
		removedEntry.prev = nil
		delete(this.cache, removedEntry.key)
	}
}

// 二叉树的最大深度
func maxDepth(root *TreeNode) int {
	if root == nil {
		return 0
	}
	return max(maxDepth(root.Left), maxDepth(root.Right)) + 1
}

// 相同的树
func isSameTree(p *TreeNode, q *TreeNode) bool {
	if p == nil && q == nil {
		return true
	}
	if p != nil && q != nil && (p.Val == q.Val) {
		return isSameTree(p.Left, q.Left) && isSameTree(p.Right, q.Right)
	}
	return false
}

// 翻转二叉树
func invertTree(node *TreeNode) *TreeNode {
	if node == nil {
		return node
	}
	node.Left, node.Right = node.Right, node.Left
	invertTree(node.Left)
	invertTree(node.Right)
	return node
}

// 对称二叉树
func isSymmetric(root *TreeNode) bool {
	if root == nil {
		return true
	}
	return check(root.Left, root.Right)
}

func check(lt *TreeNode, rt *TreeNode) bool {
	if lt == nil && rt == nil {
		return true
	} else if lt != nil && rt != nil {
		if lt.Val == rt.Val {
			return check(lt.Left, rt.Right) && check(lt.Right, rt.Left)
		}
		return false
	}
	return false
}

// 从前序和中序遍历构造二叉树
func buildTreeFromInorderAndPreorder(preorder []int, inorder []int) *TreeNode {
	preorderIdx = 0
	inorderIdxMap := make(map[int]int)
	for i, v := range inorder {
		inorderIdxMap[v] = i
	}
	return getRootFromInorderScopeAndPreorderIdx(preorder, inorder, inorderIdxMap, 0, len(inorder)-1)
}

var preorderIdx int

func getRootFromInorderScopeAndPreorderIdx(preorder []int, inorder []int, inorderIdxMap map[int]int, start int, end int) *TreeNode {
	if end < start {
		return nil
	}
	root := &TreeNode{Val: preorder[preorderIdx]}
	preorderIdx++
	inorderIdx := inorderIdxMap[root.Val]
	root.Left = getRootFromInorderScopeAndPreorderIdx(preorder, inorder, inorderIdxMap, start, inorderIdx-1)
	root.Right = getRootFromInorderScopeAndPreorderIdx(preorder, inorder, inorderIdxMap, inorderIdx+1, end)
	return root
}

// 从中序和后序遍历构造二叉树
// 本题和“从中序和先序遍历构造二叉树”的区别是子树的构造先后顺序
func buildTreeFromInorderAndPostorder(inorder []int, postorder []int) *TreeNode {
	postorderIdx = len(postorder) - 1
	inorderIdxMap := make(map[int]int)
	for i, v := range inorder {
		inorderIdxMap[v] = i
	}
	return getRootFromInorderScopeAndPostorderIdx(postorder, inorder, inorderIdxMap, 0, len(inorder)-1)
}

var postorderIdx int

func getRootFromInorderScopeAndPostorderIdx(postorder []int, inorder []int, inorderIdxMap map[int]int, start int, end int) *TreeNode {
	if end < start {
		return nil
	}
	root := &TreeNode{Val: postorder[postorderIdx]}
	postorderIdx--
	inorderIdx := inorderIdxMap[root.Val]
	root.Right = getRootFromInorderScopeAndPostorderIdx(postorder, inorder, inorderIdxMap, inorderIdx+1, end)
	root.Left = getRootFromInorderScopeAndPostorderIdx(postorder, inorder, inorderIdxMap, start, inorderIdx-1)
	return root
}

// 填充每个节点的下一个右侧节点指针
func connect(root *Node) *Node {
	stack := make([]*Node, 0)
	if root == nil {
		return nil
	}
	stack = append(stack, root)
	for len(stack) > 0 {
		size := len(stack)
		var last *Node = nil
		for i := 0; i < size; i++ {
			node := stack[0]
			stack = stack[1:]
			if last != nil {
				last.Next = node
			}
			last = node
			if node.Left != nil {
				stack = append(stack, node.Left)
			}
			if node.Right != nil {
				stack = append(stack, node.Right)
			}
		}
	}
	return root
}

// 二叉树展开为链表
func flatten(root *TreeNode) {
	node := root
	for node != nil {
		if node.Left != nil {
			left := node.Left
			node.Left = nil
			tail := left
			for tail.Right != nil {
				tail = tail.Right
			}
			tail.Right = node.Right
			node.Right = left
		}
		node = node.Right
	}
}

// 路径总和
func hasPathSum(root *TreeNode, targetSum int) bool {
	if root == nil {
		return false
	}
	return (root.Val == targetSum && root.Left == nil && root.Right == nil) ||
		hasPathSum(root.Left, targetSum-root.Val) ||
		hasPathSum(root.Right, targetSum-root.Val)
}

// 求根节点到叶子节点数字之和
func sumNumbers(root *TreeNode) int {
	sum := 0
	var dfs func(node *TreeNode, pathSum int)
	dfs = func(node *TreeNode, pathSum int) {
		if node.Left == nil && node.Right == nil {
			sum += node.Val + pathSum
			return
		}
		pathSum = (pathSum + node.Val) * 10
		if node.Left != nil {
			dfs(node.Left, pathSum)
		}
		if node.Right != nil {
			dfs(node.Right, pathSum)
		}
	}
	dfs(root, 0)
	return sum
}

// 二叉树中的最大路径和
func maxPathSum(root *TreeNode) int {
	maxSum := math.MinInt32
	var dfs func(node *TreeNode) int
	dfs = func(node *TreeNode) int {
		if node == nil {
			return 0
		}
		lt := max(0, dfs(node.Left))
		rt := max(0, dfs(node.Right))
		maxSum = max(maxSum, lt+rt+node.Val)
		return node.Val + max(lt, rt)
	}
	dfs(root)
	return maxSum
}

// 完全二叉树的节点个数
func countNodes(root *TreeNode) int {
	var dfs func(node *TreeNode)
	count := 0
	dfs = func(node *TreeNode) {
		if node == nil {
			return
		}
		count++
		dfs(node.Left)
		dfs(node.Right)
	}
	dfs(root)
	return count
}

// 二叉树的最近公共祖先
// 层序遍历的解法
func lowestCommonAncestor(root, p, q *TreeNode) *TreeNode {
	var isAncestor func(ancestor *TreeNode, node *TreeNode) bool
	isAncestor = func(ancestor, node *TreeNode) bool {
		if ancestor == nil {
			return false
		}
		if ancestor == node {
			return true
		}
		return isAncestor(ancestor.Left, node) || isAncestor(ancestor.Right, node)
	}
	if isAncestor(p, q) {
		return p
	}
	if isAncestor(q, p) {
		return q
	}
	var ancestor *TreeNode
	queue := make([]*TreeNode, 0)
	if root == nil {
		return nil
	}
	queue = append(queue, root)
	for len(queue) > 0 {
		l := len(queue)
		for i := 0; i < l; i++ {
			node := queue[0]
			queue = queue[1:]
			if isAncestor(node, p) && isAncestor(node, q) {
				ancestor = node
			}
			if node.Left != nil {
				queue = append(queue, node.Left)
			}
			if node.Right != nil {
				queue = append(queue, node.Right)
			}
		}
	}
	return ancestor
}

// 二叉树的最近公共祖先
func lowestCommonAncestorDFS(root, p, q *TreeNode) *TreeNode {
	if root == nil {
		return nil
	}
	if root == p || root == q {
		return root
	}
	lt := lowestCommonAncestorDFS(root.Left, p, q)
	rt := lowestCommonAncestorDFS(root.Right, p, q)
	if lt == nil && rt == nil {
		return nil
	} else if lt != nil && rt != nil {
		return root
	}
	if rt == nil {
		return lt
	} else {
		return rt
	}
}

// 二叉树的右视图
func rightSideView(root *TreeNode) []int {
	queue := make([]*TreeNode, 0)
	if root == nil {
		return nil
	}
	res := make([]int, 0)
	queue = append(queue, root)
	for len(queue) > 0 {
		size := len(queue)
		for i := 0; i < size; i++ {
			node := queue[0]
			queue = queue[1:]
			if node.Left != nil {
				queue = append(queue, node.Left)
			}
			if node.Right != nil {
				queue = append(queue, node.Right)
			}
			if i == size-1 {
				res = append(res, node.Val)
			}
		}
	}
	return res
}

// 二叉树的层平均值
func averageOfLevels(root *TreeNode) []float64 {
	res := make([]float64, 0)
	if root == nil {
		return res
	}
	queue := make([]*TreeNode, 0)
	queue = append(queue, root)
	for len(queue) > 0 {
		size := len(queue)
		sum := float64(0)
		for i := 0; i < size; i++ {
			node := queue[0]
			queue = queue[1:]
			sum += float64(node.Val)
			if node.Left != nil {
				queue = append(queue, node.Left)
			}
			if node.Right != nil {
				queue = append(queue, node.Right)
			}
		}
		res = append(res, sum/float64(size))
	}
	return res
}

// 二叉树的层序遍历
func levelOrder(root *TreeNode) [][]int {
	res := make([][]int, 0)
	if root == nil {
		return res
	}
	queue := make([]*TreeNode, 0)
	queue = append(queue, root)
	for len(queue) > 0 {
		size := len(queue)
		list := make([]int, 0)
		for i := 0; i < size; i++ {
			node := queue[0]
			queue = queue[1:]
			list = append(list, node.Val)
			if node.Left != nil {
				queue = append(queue, node.Left)
			}
			if node.Right != nil {
				queue = append(queue, node.Right)
			}
		}
		res = append(res, list)
	}
	return res
}

// 二叉搜索树的最小绝对差
func getMinimumDifference(root *TreeNode) int {
	res := math.MaxInt32
	var dfs func(node *TreeNode)
	dfs = func(node *TreeNode) {
		if node == nil {
			return
		}
		if node.Left != nil {
			ltTail := node.Left
			for ltTail.Right != nil {
				ltTail = ltTail.Right
			}
			diff := math.Abs(float64(node.Val - ltTail.Val))
			res = min(res, int(diff))
			dfs(node.Left)
		}
		if node.Right != nil {
			rtHead := node.Right
			for rtHead.Left != nil {
				rtHead = rtHead.Left
			}
			diff := math.Abs(float64(node.Val - rtHead.Val))
			res = min(res, int(diff))
			dfs(node.Right)
		}
	}
	dfs(root)
	return res
}

// 二叉搜索树中第K小的元素
func kthSmallest(root *TreeNode, k int) int {
	res := 0
	var dfs func(node *TreeNode)
	dfs = func(node *TreeNode) {
		if node == nil {
			return
		}
		dfs(node.Left)
		k--
		if k == 0 {
			res = node.Val
		}
		dfs(node.Right)
	}
	dfs(root)
	return res
}

// 验证二叉搜索树
func isValidBST(root *TreeNode) bool {
	if root == nil {
		return true
	}
	lt := root.Left
	for lt != nil && lt.Right != nil {
		lt = lt.Right
	}
	if lt != nil && lt.Val >= root.Val {
		return false
	}
	rt := root.Right
	for rt != nil && rt.Left != nil {
		rt = rt.Left
	}
	if rt != nil && rt.Val <= root.Val {
		return false
	}
	return isValidBST(root.Left) && isValidBST(root.Right)
}

// 岛屿数量
func numIslands(grid [][]byte) int {
	count := 0
	row, col := len(grid), len(grid[0])
	var dfs func(grid [][]byte, rowIdx, colIdx int)
	dfs = func(grid [][]byte, rowIdx, colIdx int) {
		if !(rowIdx >= 0 && rowIdx < row && colIdx >= 0 && colIdx < col) {
			return
		}
		if grid[rowIdx][colIdx] != '1' {
			return
		}
		grid[rowIdx][colIdx] = '0'
		dfs(grid, rowIdx-1, colIdx)
		dfs(grid, rowIdx+1, colIdx)
		dfs(grid, rowIdx, colIdx-1)
		dfs(grid, rowIdx, colIdx+1)
	}
	for i := 0; i < row; i++ {
		for j := 0; j < col; j++ {
			if grid[i][j] == '0' {
				continue
			}
			count++
			dfs(grid, i, j)
		}
	}
	return count
}

func canFinish(numCourses int, prerequisites [][]int) bool {
	// 课程表
	succeedCoursesMap := make(map[int][]int)
	inDegrees := make([]int, numCourses)
	for _, rel := range prerequisites {
		inDegrees[rel[0]]++
		succeedCoursesMap[rel[1]] = append(succeedCoursesMap[rel[1]], rel[0])
	}
	queue := make([]int, 0)
	for course, inDegree := range inDegrees {
		if inDegree == 0 {
			queue = append(queue, course)
		}
	}
	count := 0
	for len(queue) > 0 {
		course := queue[0]
		queue = queue[1:]
		count++
		if list, exist := succeedCoursesMap[course]; exist {
			for _, succeed := range list {
				inDegrees[succeed]--
				if inDegrees[succeed] == 0 {
					queue = append(queue, succeed)
				}
			}
		}
	}
	return count == numCourses
}

// 实现Trie（前缀树）
type Trie struct {
}

func Constructor() Trie {

}

func (this *Trie) Insert(word string) {

}

func (this *Trie) Search(word string) bool {

}

func (this *Trie) StartsWith(prefix string) bool {

}
