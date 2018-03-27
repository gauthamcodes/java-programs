package com.rummy.web.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="game")
public class Game {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name ="max_score")
	private long maxScore;
	
	@Column(name="winner")
	private int winner;
	
	

	public Game(long maxScore) {
		this.createdDate = new Date();
		this.maxScore = maxScore;
	}
	

	public Game() {
		// TODO Auto-generated constructor stub
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	public long getMaxScore() {
		return maxScore;
	}


	public void setMaxScore(long maxScore) {
		this.maxScore = maxScore;
	}


	public int getWinner() {
		return winner;
	}


	public void setWinner(int winner) {
		this.winner = winner;
	}

	
	
}
