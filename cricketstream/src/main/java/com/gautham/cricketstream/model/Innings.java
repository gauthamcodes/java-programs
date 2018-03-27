package com.gautham.cricketstream.model;

import com.gautham.cricketstream.constant.Player;

public class Innings {
	private Player player;
	private Double power;
	private Double difficulty;
	private Integer ballsFaced;
	private Integer runScored;
	private Integer noOfSixes;
	private Integer noOfFours;
	private Integer noOfDoubles;
	private Integer noOfSingles;
	private Integer noOfMaidens;
	private String wicketType;

	public Player getPlayer() {
		return player;
	}

	public Double getPower() {
		return power;
	}

	public Double getDifficulty() {
		return difficulty;
	}

	public Integer getBallsFaced() {
		return ballsFaced;
	}

	public Integer getRunScored() {
		return runScored;
	}

	public Integer getNoOfSixes() {
		return noOfSixes;
	}

	public Integer getNoOfFours() {
		return noOfFours;
	}

	public Integer getNoOfDoubles() {
		return noOfDoubles;
	}

	public Integer getNoOfSingles() {
		return noOfSingles;
	}

	public Integer getNoOfMaidens() {
		return noOfMaidens;
	}

	public String getWicketType() {
		return wicketType;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void setPower(Double power) {
		this.power = power;
	}

	public void setDifficulty(Double difficulty) {
		this.difficulty = difficulty;
	}

	public void setBallsFaced(Integer ballsFaced) {
		this.ballsFaced = ballsFaced;
	}

	public void setRunScored(Integer runScored) {
		this.runScored = runScored;
	}

	public void setNoOfSixes(Integer noOfSixes) {
		this.noOfSixes = noOfSixes;
	}

	public void setNoOfFours(Integer noOfFours) {
		this.noOfFours = noOfFours;
	}

	public void setNoOfDoubles(Integer noOfDoubles) {
		this.noOfDoubles = noOfDoubles;
	}

	public void setNoOfSingles(Integer noOfSingles) {
		this.noOfSingles = noOfSingles;
	}

	public void setNoOfMaidens(Integer noOfMaidens) {
		this.noOfMaidens = noOfMaidens;
	}

	public void setWicketType(String wicketType) {
		this.wicketType = wicketType;
	}
	
	public void bowled() {
		this.ballsFaced++;
	}

}
