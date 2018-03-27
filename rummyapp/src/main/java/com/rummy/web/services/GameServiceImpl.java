package com.rummy.web.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.rummy.web.dao.GameDao;
import com.rummy.web.dao.GameJDBCDao;
import com.rummy.web.entity.Game;

@Service
public class GameServiceImpl implements GameService{
	
	@Autowired
	private GameJDBCDao gameJDBCDao;
	
	@Autowired
	private GameDao gameDao;
	
	
	@Value("${rummy.default.maxScore}")
	private long defaultMaxScore;
	
	public Game createGame(long maxScore) {
		return gameDao.save(new Game(maxScore));
	}
	
	public Game createGame() {
		return gameDao.save(new Game(defaultMaxScore));
	}
	
	public Game findById(int id) {
		return gameDao.findOne(id);
	}
	
	public List<Game> findAll() {
		return gameJDBCDao.getAll();
	}
	
}
