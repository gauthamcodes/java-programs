package com.gautham.startstreams;

public enum Innings {
	RUNS("Run"),BALLS("Balls"),TIME("Played on");
	
	private String field;
	
	Innings(String field) {
		this.field = field;
	}
	
	public String getField() {
		return this.field;
	}
}
