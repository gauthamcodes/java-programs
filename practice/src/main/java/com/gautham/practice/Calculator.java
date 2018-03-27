package com.gautham.practice;

public class Calculator {
	
	private CalculatorService service;
	
	public Calculator() {
		
	}
	
	public Calculator(CalculatorService service) {
		this.service = service;
	}
	
	public int perform(int i, int j) {
		return service.add(i, j);
	}
}
