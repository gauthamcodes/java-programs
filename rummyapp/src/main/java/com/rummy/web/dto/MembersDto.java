package com.rummy.web.dto;

public class MembersDto {
	private int id;
	private String name;
	private int totalScore;
	private boolean isActive;
	
	
	
	
	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public MembersDto(int id, String name, int totalScore, boolean isActive) {
		this.id = id;
		this.name = name;
		this.totalScore = totalScore;
		this.isActive = isActive;
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
	
	
	public int getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}
	
	
}
