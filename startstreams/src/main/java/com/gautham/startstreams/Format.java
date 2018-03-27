package com.gautham.startstreams;

public enum Format {
	T20("T20 Cricket");
	
	private String format;
	
	Format(String format) {
		this.format = format;
	}
	
	public String getFormat() {
		return this.format;
	}
}
