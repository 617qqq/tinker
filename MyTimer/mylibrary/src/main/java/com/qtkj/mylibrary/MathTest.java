package com.qtkj.mylibrary;

public class MathTest {

	public static void main(String[] args) {
//		testSin();
		testMove();
//		System.out.println(Integer.toBinaryString(Integer.MIN_VALUE));
//		System.out.println(Integer.MIN_VALUE);
//		System.out.println(0b10000000000000000000000000000001);
//		System.out.println(0b11111111111111111111111111111110);
	}

	private static void testMove() {
		int size = - 1737754967;
		System.out.println(Integer.toBinaryString(size));
		System.out.println(Integer.toBinaryString(size - 1));
		System.out.println(Integer.toBinaryString(- size));
		System.out.println(Integer.toBinaryString(- size).length());

		System.out.println(Integer.toBinaryString(~ MODE_MASK));
		System.out.println(Integer.toBinaryString(size & ~ MODE_MASK));

		int mode = 1;
		System.out.println("mode:                  " + Integer.toBinaryString(mode));
		System.out.println("MODE_MASK:             " + Integer.toBinaryString(MODE_MASK));
		System.out.println("mode & MODE_MASK:      " + Integer.toBinaryString(mode << MODE_SHIFT & MODE_MASK));

		int seq = makeSeq(size, mode);
		System.out.println(seq);
		System.out.println(Integer.toBinaryString(seq));
		System.out.println(getId(seq));

		int code = getRequestCode(seq);
		System.out.println(Integer.toBinaryString(code));
		System.out.println(code);
	}

	private static final int MODE_SHIFT = 25;
	private static final int MODE_MASK = 63 << MODE_SHIFT;

	private static int makeSeq(int id, int requestCode) {
		return (id & ~ MODE_MASK) | (requestCode << MODE_SHIFT & MODE_MASK);
	}

	private static int getId(int seq) {
		return seq & ~ MODE_MASK;
	}

	private static int getRequestCode(int seq) {
		return (seq & MODE_MASK) >> MODE_SHIFT;
	}

	private static void testSin() {
		System.out.println(Math.sin(30));
		System.out.println(sin(30));
	}

	private static double sin(double angle) {
		return Math.sin(angle * Math.PI / 180);
	}
}
