package com.gautham.samplejava;

public class RotateArray {
	public static void main(String[] args) {
		RotateArray rotate = new RotateArray();
		int[] arr = new int[] { 1, 2, 3, 4, 5, 6 };
		print(arr);
		int[] newArr = rotate.perform(arr, 7);
		print(newArr);
	}

	private int[] perform(int[] arr, int position) {
		position = position%arr.length;
		int i, j, temp;
		for (j = 0; j < position; j++) {
			temp = arr[0];
			for (i = 0; i < arr.length - 1; i++) {
				arr[i] = arr[i + 1];
			}
			arr[arr.length - 1] = temp;
		}
		return arr;
	}

	private static void print(int[] arr) {		
		for (int i = 0; i < arr.length; i++)
			System.out.print(arr[i] + " ");
		
		System.out.println();
	}
}
