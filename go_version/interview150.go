package main

import (
	"container/list"
	"fmt"
	"math"
	"strings"
)

func main() {
	fmt.Println("Hello world")
}

// 合并两个有序数组
func merge(nums1 []int, m int, nums2 []int, n int) {
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
