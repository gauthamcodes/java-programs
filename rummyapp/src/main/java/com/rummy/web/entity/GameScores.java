package com.rummy.web.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "game_scores")
@NamedQueries({
	@NamedQuery(name="GameScores.getByRoundId", 
			query="select gs from GameScores gs where gs.gameRounds.id = :id"),
	@NamedQuery(name="GameScores.getByPlayerId",
			query="SELECT gs FROM GameScores gs where gs.gameMembers.id = :id")
})
public class GameScores {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "round_id",referencedColumnName="id")
	private GameRounds gameRounds;
	

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "member_id",referencedColumnName="id")
	private GameMembers gameMembers;
	
	@Column(name = "score")
	private int score;
	
	public GameScores() {
		
	}
	
	public GameScores(GameRounds gameRounds, GameMembers gameMembers, int score) {
		this.gameRounds = gameRounds;
		this.gameMembers = gameMembers;
		this.score = score;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public GameRounds getGameRounds() {
		return gameRounds;
	}

	public void setGameRounds(GameRounds gameRounds) {
		this.gameRounds = gameRounds;
	}

	public GameMembers getGameMembers() {
		return gameMembers;
	}

	public void setGameMembers(GameMembers gameMembers) {
		this.gameMembers = gameMembers;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	
}	
