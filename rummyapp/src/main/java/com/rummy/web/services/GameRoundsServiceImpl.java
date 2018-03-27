package com.rummy.web.services;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rummy.web.dao.GameRoundsDao;
import com.rummy.web.entity.Game;
import com.rummy.web.entity.GameRounds;

@Service
public class GameRoundsServiceImpl implements GameRoundsService {
	
	@Autowired
	private GameRoundsDao gameRoundsDao;
	

	@Autowired
	protected EntityManager entityManager;
	
	public GameRounds add(Game game) {
		int custom_id = getRoundsCount(game.getId()) + 1;
		System.out.println(custom_id);
		return gameRoundsDao.save(new GameRounds(game,custom_id));
	}	
	
	public Integer getRoundsCount(int id) {
		return (int) (long) entityManager.createNamedQuery("GameRounds.getRoundsCountByGameId", Long.class)
			.setParameter("id", id)
			.getSingleResult();		
	}
	
	
	@SuppressWarnings("unchecked")
	public List<GameRounds> getByGameId(int game_id) {
		return (List<GameRounds>) entityManager.createNamedQuery("GameRounds.getByGameId")
				.setParameter("id", game_id)
				.getResultList();
	}
}
