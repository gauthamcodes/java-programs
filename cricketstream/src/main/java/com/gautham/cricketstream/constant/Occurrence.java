package com.gautham.cricketstream.constant;

public enum Occurrence {
	SIX(6), FOUR(4), DOUBLE(2), SINGLE(1), MAIDEN(0), WICKET(-1);

	// value
	private Integer value;

	Occurrence(Integer value) {
		this.value = value;
	}

	public Integer getValue() {
		return this.value;
	}
}
