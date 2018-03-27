package com.rummy.web.dto;

import java.util.List;

public class GameScoresDto {
	private int round_id;
	private List<PlayersScoresDto> players;
	
	
	public int getRound_id() {
		return round_id;
	}
	public void setRound_id(int round_id) {
		this.round_id = round_id;
	}
	public List<PlayersScoresDto> getPlayers() {
		return players;
	}
	public void setPlayers(List<PlayersScoresDto> players) {
		this.players = players;
	}
	
	
}
