package com.rummy.web.dto;

public class PlayersScoresDto {
	private int player_id;
	private int score;
	
	public PlayersScoresDto() {
		
	}
	
	public PlayersScoresDto(int player_id, long score) {
		this.player_id = player_id;
		this.score = (int) score;
	}
	
	public int getPlayer_id() {
		return player_id;
	}
	public void setPlayer_id(int player_id) {
		this.player_id = player_id;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	
	
}
