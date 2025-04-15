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

func Constructor() MinStack {
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
