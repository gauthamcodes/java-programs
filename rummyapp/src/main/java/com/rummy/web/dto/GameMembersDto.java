package com.rummy.web.dto;

import java.util.ArrayList;
import java.util.List;

public class GameMembersDto {
	private Integer game_id;
	private List<Integer> members_id = new ArrayList<Integer>();
	
	public Integer getGame_id() {
		return game_id;
	}

	public void setGame_id(Integer game_id) {
		this.game_id = game_id;
	}

	public List<Integer> getMembers_id() {
		return members_id;
	}

	public void setMembers_id(List<Integer> members_id) {
		for(Integer id:members_id) {
			if(id != null)
				this.members_id.add(id);
		}
	}

	public String toString() {
		String membersId = "";
		for(Integer id:getMembers_id()) {
			membersId += id.toString() + " ";
		}
		return "{game_id = "+ game_id + ", members_id = [" + membersId + "]}";
	}
}
