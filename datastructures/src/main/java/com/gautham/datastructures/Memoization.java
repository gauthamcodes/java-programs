package com.gautham.datastructures;

public class Memoization {
	
	private static int[] memory;
	
	public static void main(String[] args) {
		int num = 30;
		
		long startTime = System.nanoTime();
		for (int i = 0; i < num; i++) {
			System.out.print(fib(i) + " ");
		}
		long endTime = System.nanoTime();
		System.out.println();
		System.out.print(endTime-startTime);
		System.out.println();
		long startTime1 = System.nanoTime();
		memory = new int[num];
		for (int i = 0; i < num; i++) {
			System.out.print(memo(i) + " ");
		}
		long endTime1 = System.nanoTime();
		System.out.println();
		System.out.print(endTime1-startTime1);
	}
	
	private static int memo(int n) {
		if(n<2) {
			return n;
		}
		else {
			if(memory[n] != 0) {
				return memory[n];
			}
			else {
				memory[n] = memo(n-2) + memo(n-1);
				return memory[n];
			}
		}
		
	}

	private static int fib(int n) {
		if (n < 2) {
			return n;
		} else {
			return fib(n-2) + fib(n-1);
		}
	}
}
