package com.rummy.web.services;

import java.util.List;

import com.rummy.web.entity.Game;
import com.rummy.web.entity.GameRounds;

public interface GameRoundsService {
	GameRounds add(Game game);
	Integer getRoundsCount(int game_id);
	List<GameRounds> getByGameId(int game_id);
}
