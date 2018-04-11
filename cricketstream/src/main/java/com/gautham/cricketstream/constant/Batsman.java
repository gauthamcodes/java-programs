package com.gautham.cricketstream.constant;

public enum Batsman {
	KOHLI("Virat Kohli", 
			0.96, //skill
			0.82, //aggression
			0.89, //defence
			0.76, //strength
			0.98, //stamina
			0.84, //agility
			0.88, //temperment
			0.72, //luck
			0.90), //consistency 
	ABD("AB De Villiers", 
			0.90, //skill
			0.92, //aggression
			0.72, //defence
			0.90, //strength
			0.90, //stamina
			0.84, //agility
			0.65, //temperment
			0.62, //luck
			0.78), //consistency 
	MAXWELL("Glenn Maxwell", 
			0.76, //skill
			0.95, //aggression
			0.62, //defence
			0.86, //strength
			0.85, //stamina
			0.73, //agility
			0.75, //temperment
			0.34, //luck
			0.69), //consistency 
	STOKES("Ben Stokes", 
			0.84, //skill
			0.67, //aggression
			0.45, //defence
			0.82, //strength
			0.61, //stamina
			0.53, //agility
			0.35, //temperment
			0.76, //luck
			0.85), //consistency 
	BRATHWAITE("Carlos Brathwaite", 
			0.80, //skill
			0.85, //aggression
			0.65, //defence
			0.98, //strength
			0.62, //stamina
			0.53, //agility
			0.25, //temperment
			0.54, //luck
			0.69); //consistency 

	//string
	private String name;
	
	//attributes out of 100 
	private Double skill;
	private Double aggression;
	private Double defense;
	private Double strength;
	private Double stamina;
	private Double agility;
	private Double temperment;
	private Double luck;
	private Double consistency;

	Batsman(String name, Double skill, Double aggression, Double defense, Double strength, Double stamina,
			Double agility, Double temperment, Double luck, Double consistency) {
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

	public Double getSkill() {
		return skill;
	}

	public Double getAggression() {
		return aggression;
	}

	public Double getDefense() {
		return defense;
	}

	public Double getStrength() {
		return strength;
	}

	public Double getStamina() {
		return stamina;
	}

	public Double getAgility() {
		return agility;
	}

	public Double getTemperment() {
		return temperment;
	}

	public Double getLuck() {
		return luck;
	}
	
	public Double getConsistency() {
		return consistency;
	}

}
