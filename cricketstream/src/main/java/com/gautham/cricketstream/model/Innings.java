package com.gautham.cricketstream.model;

import java.util.concurrent.ThreadLocalRandom;

import com.gautham.cricketstream.constant.Batsman;
import com.gautham.cricketstream.constant.Bowler;
import com.gautham.cricketstream.constant.Occurrence;
import com.gautham.cricketstream.constant.Wicket;

public class Innings {
	private Batsman batsman;
	private Integer ballsFaced;
	private Integer runScored;
	private Integer noOfSixes;
	private Integer noOfFours;
	private Integer noOfDoubles;
	private Integer noOfSingles;
	private Integer noOfMaidens;
	private Wicket wicket;
	private Boolean notOut;

	public Innings() {

	}

	public Innings(Batsman batsman) {
		this.batsman = batsman;
		this.ballsFaced = 0;
		this.runScored = 0;
		this.noOfDoubles = 0;
		this.noOfFours = 0;
		this.noOfSixes = 0;
		this.noOfSingles = 0;
		this.noOfMaidens = 0;
		this.notOut = true;
	}

	public Batsman getBatsman() {
		return batsman;
	}

	public void setBatsman(Batsman batsman) {
		this.batsman = batsman;
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

	public Wicket getWicket() {
		return wicket;
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

	public void setWicket(Wicket wicket) {
		this.wicket = wicket;
	}

	public Boolean getNotOut() {
		return notOut;
	}

	public void setNotOut(Boolean notOut) {
		this.notOut = notOut;
	}

	public Bowler bowl() {
		this.ballsFaced++;
		return Bowler.values()[ThreadLocalRandom.current().nextInt(0, Bowler.values().length)];
	}

	public void six() {
		this.runScored += Occurrence.SIX.getValue();
		this.noOfSixes++;
	}

	public void four() {
		this.runScored += Occurrence.FOUR.getValue();
		this.noOfFours++;
	}

	public void two() {
		this.runScored += Occurrence.DOUBLE.getValue();
		this.noOfDoubles++;
	}

	public void one() {
		this.runScored += Occurrence.SINGLE.getValue();
		this.noOfSingles++;
	}

	public void maiden() {
		this.noOfMaidens++;
	}

	public void wicket() {
		this.notOut = false;
	}

	@Override
	public String toString() {
		return "Innings [batsman=" + batsman + ", ballsFaced=" + ballsFaced + ", runScored=" + runScored
				+ ", noOfSixes=" + noOfSixes + ", noOfFours=" + noOfFours + ", noOfDoubles=" + noOfDoubles
				+ ", noOfSingles=" + noOfSingles + ", noOfMaidens=" + noOfMaidens + ", wicket=" + wicket + ", notOut="
				+ notOut + "]";
	}

}
