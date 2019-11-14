package com.qtkj.mylibrary;

public class AngleTest {

	public static void main(String[] args){
	    testTan();
	}

	private static void testTan() {
		int x = 4;
		int y = 3;
		System.out.println(Math.tan (x/y));
		System.out.println(Math.atan(x/y));
		System.out.println(Math.tan (y/x));
		System.out.println(Math.atan(y/x));
		
		System.out.println();
	}
}
