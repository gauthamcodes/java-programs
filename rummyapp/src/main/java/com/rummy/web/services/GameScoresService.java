package com.rummy.web.services;

import java.util.List;

import com.rummy.web.entity.GameScores;

public interface GameScoresService {
	GameScores addScore(int round_id, int member_id, int score);
	List<GameScores> getByRoundId(int round_id);
	List<GameScores> getByPlayerId(int player_id);
}
