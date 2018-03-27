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
@Table(name = "game_members")
@NamedQueries({
		@NamedQuery(name = "GameMembers.updateTotalScore", 
		query = "UPDATE GameMembers gm SET gm.totalScore = :score WHERE gm.id = :id"),
		@NamedQuery(name = "GameMembers.updateIsActive",
		query = "UPDATE GameMembers gm SET gm.isActive = :active WHERE gm.id = :id"),
		@NamedQuery(name = "GameMembers.getByGameId",
		query = "SELECT gm from GameMembers gm WHERE gm.game.id = :id")
})
public class GameMembers {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "game_id", referencedColumnName = "id")
	private Game game;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "players_id", referencedColumnName = "id")
	private Players players;
	
	@Column(name = "total_score")
	private int totalScore;
	
	@Column(name = "is_active")
	private boolean isActive;
	
	
	

	public GameMembers() {
		
	}
	
	public GameMembers(Game game, Players players) {
		this.game = game;
		this.players = players;
		this.isActive = true;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public Players getPlayers() {
		return players;
	}

	public void setPlayers(Players players) {
		this.players = players;
	}
	

	public int getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
}
