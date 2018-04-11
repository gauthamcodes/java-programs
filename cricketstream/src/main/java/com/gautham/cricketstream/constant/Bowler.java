package com.gautham.cricketstream.constant;

public enum Bowler {
	WORST(0.1, 0.15, 0.25, 0.30, 0.19, 0.01),
	POOR(0.08, 0.12, 0.21, 0.26, 0.31, 0.02), 
	MEDIUM(0.06, 0.09, 0.18, 0.23, 0.41, 0.03), 
	GOOD(0.04, 0.06, 0.16, 0.17, 0.53, 0.04),
	BEST(0.02, 0.03, 0.12, 0.14, 0.64, 0.05);

	private Double six;
	private Double four;
	private Double two;
	private Double one;
	private Double maiden;
	private Double wicket;

	Bowler(Double six, Double four, Double two, Double one, Double maiden, Double wicket) {
		this.six = six;
		this.four = four;
		this.two = two;
		this.one = one;
		this.maiden = maiden;
		this.wicket = wicket;
	}

	public Double getSix() {
		return six;
	}

	public Double getFour() {
		return four;
	}

	public Double getTwo() {
		return two;
	}

	public Double getOne() {
		return one;
	}

	public Double getMaiden() {
		return maiden;
	}

	public Double getWicket() {
		return wicket;
	}

}
