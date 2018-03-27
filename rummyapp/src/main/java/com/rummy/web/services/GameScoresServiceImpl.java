package com.rummy.web.services;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rummy.web.dao.GameMembersDao;
import com.rummy.web.dao.GameRoundsDao;
import com.rummy.web.dao.GameScoresDao;
import com.rummy.web.entity.GameMembers;
import com.rummy.web.entity.GameRounds;
import com.rummy.web.entity.GameScores;

@Service
public class GameScoresServiceImpl implements GameScoresService{
	
	@Autowired
	private GameScoresDao gameScoresDao;
	
	@Autowired
	private GameRoundsDao gameRoundsDao;
	
	@Autowired
	private GameMembersDao gameMembersDao;
	
	@Autowired
	protected EntityManager entityManager;
	
	public GameScores addScore(GameScores gameScores) {
		return gameScoresDao.save(gameScores);
	}
	
	public GameScores addScore(int round_id, int member_id, int score) {		
		GameMembers gameMembers = gameMembersDao.findOne(member_id);
		GameRounds gameRounds = gameRoundsDao.findOne(round_id);
		int updatedScore = gameMembers.getTotalScore() + score;
		if(updatedScore > gameRounds.getGame().getMaxScore()) {
			gameMembers.setActive(false);
		}
		gameMembers.setTotalScore(updatedScore);
		gameMembersDao.save(gameMembers);
		return gameScoresDao.save(new GameScores(
				gameRounds, 
				gameMembers, 
				score));	
	}
	
	@SuppressWarnings("unchecked")
	public List<GameScores> getByRoundId(int round_id) {
		return (List<GameScores>) entityManager.createNamedQuery("GameScores.getByRoundId")
				.setParameter("id", round_id)
				.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<GameScores> getByPlayerId(int player_id) {
		return (List<GameScores>) entityManager.createNamedQuery("GameScores.getByPlayerId")
				.setParameter("id", player_id)
				.getResultList();
	}
	
}
