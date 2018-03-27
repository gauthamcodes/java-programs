package com.rummy.web.dto;

public class PlayersDto {
	private int id;
	private String name;
	private String user_name;
	
	
	public PlayersDto() {
		
	}
	
	public PlayersDto(int id, String name, String user_name) {
		this.id = id;
		this.name = name;
		this.user_name = user_name;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	
	
}
