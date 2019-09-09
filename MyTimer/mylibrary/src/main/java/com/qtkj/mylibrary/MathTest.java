package com.qtkj.mylibrary;

public class MathTest  {

	public static void main(String[] args){
		System.out.println(Math.sin(30));
		System.out.println(sin(30));
	}

	public static double sin(double angle){
		return Math.sin(angle * Math.PI / 180);
	}
}
