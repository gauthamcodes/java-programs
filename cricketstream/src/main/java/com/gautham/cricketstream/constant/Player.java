package com.gautham.cricketstream.constant;

public enum Player {
	KOHLI("Virat Kohli", 86, 65, 75, 71, 93, 83, 95, 61, 79), 
	ABD("AB De Villiers", 86, 75, 64, 69, 93, 83, 95, 54, 85), 
	MAXWELL("Glenn Maxwell", 86, 75, 64, 69, 93, 83, 95, 54, 85), 
	STOKES("Ben Stokes", 86, 75, 64, 69, 93, 83, 95, 54, 85), 
	BRATHWAITE("Carlos Brathwaite", 86, 75, 64, 69, 93, 83, 95, 54, 85);

	//string
	private String name;
	
	//attributes out of 100
	private Integer skill;
	private Integer aggression;
	private Integer defense;
	private Integer strength;
	private Integer stamina;
	private Integer agility;
	private Integer temperment;
	private Integer luck;
	private Integer consistency;

	Player(String name, Integer skill, Integer aggression, Integer defense, Integer strength, Integer stamina,
			Integer agility, Integer temperment, Integer luck, Integer consistency) {
		this.name = name;
		this.skill = skill;
		this.aggression = aggression;
		this.defense = defense;
		this.strength = strength;
		this.stamina = stamina;
		this.agility = agility;
		this.temperment = temperment;
		this.luck = luck;
		this.consistency = consistency;
	}

	public String getName() {
		return name;
	}

	public Integer getSkill() {
		return skill;
	}

	public Integer getAggression() {
		return aggression;
	}

	public Integer getDefense() {
		return defense;
	}

	public Integer getStrength() {
		return strength;
	}

	public Integer getStamina() {
		return stamina;
	}

	public Integer getAgility() {
		return agility;
	}

	public Integer getTemperment() {
		return temperment;
	}

	public Integer getLuck() {
		return luck;
	}
	
	public Integer getConsistency() {
		return consistency;
	}

}
