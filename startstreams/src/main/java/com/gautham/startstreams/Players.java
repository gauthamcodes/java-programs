package com.gautham.startstreams;

public enum Players {
	VK("Virat Kohli"),ABD("AB De Villers"),BEN("Ben Stokes"),CBR("Carlos Brathwaite"),DW("David Warner");
	
	private String name;
	
	Players(String playerName) {
		this.name = playerName;
	}
	
	public String getName() {
		return this.name;
	}
}
