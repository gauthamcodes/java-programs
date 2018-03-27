package com.rummy.web.dto;

import java.util.ArrayList;
import java.util.List;

public class GameStatusDto {
	public int winner;
	public boolean isComplete;	
	public List<MembersDto> members = new ArrayList<MembersDto>();
	
	public int getWinner() {
		return winner;
	}
	public void setWinner(int winner) {
		this.winner = winner;
	}
	public boolean isComplete() {
		return isComplete;
	}
	public void setComplete(boolean isComplete) {
		this.isComplete = isComplete;
	}
	public List<MembersDto> getMembers() {
		return members;
	}
	public void setMembers(List<MembersDto> members) {
		this.members = members;
	}
	
	
}
