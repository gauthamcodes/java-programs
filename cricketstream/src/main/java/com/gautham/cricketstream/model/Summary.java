package com.gautham.cricketstream.model;

public class Summary {
	private Integer totalInnings;
	private Integer notOuts;
	private Integer totalRuns;
	private Integer fours;
	private Integer sixes;
	private Integer highScore;
	private Integer ballsFaced;
	private Double average;
	private Double strikeRate;
	private Integer halfCenturies;
	private Integer centuries;

	public Summary() {
		this.totalInnings = 0;
		this.notOuts = 0;
		this.totalRuns = 0;
		this.fours = 0;
		this.sixes = 0;
		this.highScore = 0;
		this.ballsFaced = 0;
		this.average = 0.0;
		this.strikeRate = 0.0;
		this.halfCenturies = 0;
		this.centuries = 0;
	}

	public Integer getTotalInnings() {
		return totalInnings;
	}

	public void setTotalInnings(Integer totalInnings) {
		this.totalInnings = totalInnings;
	}

	public Integer getNotOuts() {
		return notOuts;
	}

	public void setNotOuts(Integer notOuts) {
		this.notOuts = notOuts;
	}

	public Integer getTotalRuns() {
		return totalRuns;
	}

	public void setTotalRuns(Integer totalRuns) {
		this.totalRuns = totalRuns;
	}

	public Integer getFours() {
		return fours;
	}

	public void setFours(Integer fours) {
		this.fours = fours;
	}

	public Integer getSixes() {
		return sixes;
	}

	public void setSixes(Integer sixes) {
		this.sixes = sixes;
	}

	public Integer getHighScore() {
		return highScore;
	}

	public void setHighScore(Integer highScore) {
		this.highScore = highScore;
	}

	public Integer getBallsFaced() {
		return ballsFaced;
	}

	public void setBallsFaced(Integer ballsFaced) {
		this.ballsFaced = ballsFaced;
	}

	public Double getAverage() {
		return average;
	}

	public void setAverage(Double average) {
		this.average = average;
	}

	public Double getStrikeRate() {
		return strikeRate;
	}

	public void setStrikeRate(Double strikeRate) {
		this.strikeRate = strikeRate;
	}

	public Integer getHalfCenturies() {
		return halfCenturies;
	}

	public void setHalfCenturies(Integer halfCenturies) {
		this.halfCenturies = halfCenturies;
	}

	public Integer getCenturies() {
		return centuries;
	}

	public void setCenturies(Integer centuries) {
		this.centuries = centuries;
	}

	@Override
	public String toString() {
		return "Summary [totalInnings=" + totalInnings + ", notOuts=" + notOuts + ", totalRuns=" + totalRuns
				+ ", fours=" + fours + ", sixes=" + sixes + ", highScore=" + highScore + ", ballsFaced=" + ballsFaced
				+ ", average=" + average + ", strikeRate=" + strikeRate + ", halfCenturies=" + halfCenturies
				+ ", centuries=" + centuries + "]";
	}

}
