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
@Table(name = "game_rounds")
@NamedQueries({
		@NamedQuery(name = "GameRounds.getRoundsCountByGameId", 
		query = "select count(*) from GameRounds where game.id = :id"),
		@NamedQuery(name = "GameRounds.getByGameId",
		query = "select gr from GameRounds gr where game.id = :id order by gr.customId")
})
public class GameRounds {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "game_id", referencedColumnName = "id")
	private Game game;
	
	@Column(name = "custom_id")
	private int customId;
	
	
	public GameRounds() {
		
	}
	
	public GameRounds(Game game, int customId) {
		this.game = game;
		this.customId = customId;
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
	

	public int getCustomId() {
		return customId;
	}

	public void setCustomId(int customId) {
		this.customId = customId;
	}

}
