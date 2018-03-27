package com.rummy.web.services;

import java.util.List;

import com.rummy.web.entity.Game;
import com.rummy.web.entity.GameMembers;
import com.rummy.web.entity.Players;

public interface GameMembersService {
	GameMembers save(Game game, Players players);
	int updateScore(int id, int score);
	boolean isActive(int id);
	int updateActive(int id);
	List<GameMembers> getByGameId(int game_id);
	GameMembers update(int id, int score);
}
