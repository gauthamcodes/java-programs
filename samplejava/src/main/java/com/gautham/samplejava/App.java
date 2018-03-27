package com.gautham.samplejava;

import java.util.stream.IntStream;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		System.out.println("Number 27 is prime number or not :" + isPrime(27));
	}

	private static boolean isPrime(int number) {
		return number > 1 && IntStream.range(2, number).noneMatch(index -> number % index == 0);
	}
}
