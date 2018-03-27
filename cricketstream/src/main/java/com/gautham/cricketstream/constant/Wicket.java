package com.gautham.cricketstream.constant;

public enum Wicket {
	BOWLED("Bowled"), CATCH("Catch"), CB("Caught & Bowled"), LBW("LBW"), RUNOUT("Run out"), STUMPED("Stumped");

	private String name;

	Wicket(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
}
