package com.qtkj.mylibrary;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 一个包含n个整数的数组list,判断list中是否存在三个元素a,b,c，使得a+b+c=0？
 * 找到所有满足条件且不重复的三元组
 */
public class A11SumOfThreeNum {

	public static void main(String[] args) {

		int[] list = {1, 1, 3, - 3, - 2, 0, 2, - 2, - 3, 1, 1, 0, 0, - 4, 2};
		ArrayList<int[]> result = getSumOfThreeNum(list, 0);
		for (int i = 0; i < result.size(); i++) {
			System.out.println(Arrays.toString(result.get(i)));
		}
	}

	private static ArrayList<int[]> getSumOfThreeNum(int[] list, int target) {
		ArrayList<int[]> result = new ArrayList<>();
		M01Order.orderQuick(list);
		System.out.println(Arrays.toString(list));
		for (int i = 0; i < list.length - 2; i++) {
			if (i > 0 && list[i] == list[i - 1]) {
				continue;
			}
			for (int j = i + 1; j < (list.length - 1); j++) {
				if (j > i + 1 && list[j] == list[j - 1]) {
					continue;
				}
				int remain = target - list[i] - list[j];
				if (hasNum(list, remain, j + 1, list.length - 1)) {
					result.add(new int[]{list[i], list[j], remain});
				}
			}
		}

		return result;
	}

	private static boolean hasNum(int[] list, int remain, int start, int end) {
		if (start == end) {
			return list[start] == remain;
		} else if (start < end) {
			if (list[(start + end) / 2] == remain) {
				return true;
			} else if (list[(start + end) / 2] > remain) {
				return hasNum(list, remain, start, (start + end) / 2 - 1);
			} else {
				return hasNum(list, remain, (start + end) / 2 + 1, end);
			}
		} else {
			return false;
		}
	}
}
