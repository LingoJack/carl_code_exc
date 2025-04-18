package main

type RandomNode struct {
	Val    int
	Next   *RandomNode
	Random *RandomNode
}

type Node struct {
	Val   int
	Left  *Node
	Right *Node
	Next  *Node
}
