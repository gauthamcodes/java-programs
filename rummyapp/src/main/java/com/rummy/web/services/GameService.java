package com.rummy.web.services;

import java.util.List;

import com.rummy.web.entity.Game;

public interface GameService {
	public Game createGame();
	public Game createGame(long maxScore);
	public Game findById(int id);
	public List<Game> findAll();
}
