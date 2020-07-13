package com.qtkj.mylibrary;

public class M01Order {

	public static int count;

	public static void main(String[] args) {
	}

	/**
	 * 快速排序
	 */
	public static void orderQuick(int[] array) {
		if (array.length == 0) {
			return;
		}
		orderQuick(array, 0, array.length - 1);
	}

	public static void orderQuickSuper(int[] array, int start, int end) {
		int i = start, j = end, a = array[start];
		while (i != j){
			while (array[j] > a && j > i){
				j--;
			}
			while (array[i] < a && j > i){
				i++;
			}
			if (array[i] == array[j] && j > i){
				i++;
			}else {
				int temp = array[j];
				array[j] = array[i];
				array[i] = temp;
			}
		}
		if (i - 1 > start) {
			orderQuickSuper(array, start, i - 1);
		}
		if (end - 1 - i > 0) {
			orderQuickSuper(array, i + 1, end);
		}
	}

	public static void orderQuick(int[] array, int start, int end) {
		if (end - start != 0) {
			int i = start, j = end, a = array[start];
			boolean isToLeft = true;
			while (i != j) {
				if (isToLeft) {
					if (a > array[j]) {
						array[i] = array[j];
						array[j] = a;
						i++;
						isToLeft = false;
					} else {
						j--;
					}
				} else {
					if (a < array[i]) {
						array[j] = array[i];
						array[i] = a;
						j--;
						isToLeft = true;
					} else {
						i++;
					}
				}
			}
			if (i - 1 > start) {
				orderQuick(array, start, i - 1);
			}
			if (end - 1 - i > 0) {
				orderQuick(array, i + 1, end);
			}
		}
	}

	public static void orderBubble(int[] array) {
		int temp;
		for (int i = 0; i < array.length - 1; i++) {
			for (int j = 0; j < array.length - 1; j++) {
				if (array[j] > array[j + 1]) {
					temp = array[j + 1];
					array[j + 1] = array[j];
					array[j] = temp;
				}
				count++;
			}
		}
	}
}
