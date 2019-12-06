package com.qtkj.mylibrary;

import java.util.Scanner;

/**
 * 一个级数为n的台阶，每次你可以爬1,2或者3级台阶，请问爬完整个n级台阶有多少种方法？
 */
public class Footstep {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int sum = - 1;
		while (sum != 0) {
			sum = Integer.parseInt(sc.next());
			System.out.println(computeStep(sum));
		}
	}

	private static int computeStep(int sum) {
		if (sum <= 0) {
			return 0;
		} else {
			return computeStepReal(sum, "0");
		}
	}

	private static int computeStepReal(int sum, String s) {
		if (sum == 0) {
			System.out.println(s);
			return 1;
		}
		int temp = 0;
		if (sum - 3 >= 0) {
			temp += computeStepReal(sum - 3, s + ",3");
		}
		if (sum - 2 >= 0) {
			temp += computeStepReal(sum - 2, s + ",2");
		}
		temp += computeStepReal(sum - 1, s + ",1");
		return temp;
	}


}
