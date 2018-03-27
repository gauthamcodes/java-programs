package com.rummy.web.services;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import com.rummy.web.dao.GameMembersDao;
import com.rummy.web.entity.Game;
import com.rummy.web.entity.GameMembers;
import com.rummy.web.entity.Players;

@Service
public class GameMembersServiceImpl implements GameMembersService{

	@Autowired
	private GameMembersDao gameMembersDao;
	
	@Autowired
	protected EntityManager entityManager;
	
	public GameMembers save(Game game, Players players) {
		return gameMembersDao.save(new GameMembers(game, players));
	}	
	
	public GameMembers update(int id, int score) {
		GameMembers gameMembers = gameMembersDao.findOne(id);
		int updatedScore = (int) (gameMembers.getTotalScore() + score);
		gameMembers.setTotalScore(updatedScore);
		return gameMembersDao.save(gameMembers);
	}
	
	@Transactional
	@Modifying
	public int updateScore(int id, int score) {
		return entityManager.createNamedQuery("GameMembers.updateTotalScore")
			.setParameter("score", score)
			.setParameter("id", id)
			.executeUpdate();		
	}	
	
	public boolean isActive(int id) {
		GameMembers gameMembers = gameMembersDao.findOne(id);
		return gameMembers.getGame().getMaxScore() > gameMembers.getTotalScore();
	}
	
	@Transactional
	@Modifying
	public int updateActive(int id) {
		return entityManager.createNamedQuery("GameMembers.updateIsActive")
			.setParameter("active", isActive(id))
			.setParameter("id", id)
			.executeUpdate();
	}
	
	@Transactional
	@SuppressWarnings("unchecked")
	public List<GameMembers> getByGameId(int game_id) {
		return (List<GameMembers>) entityManager.createNamedQuery("GameMembers.getByGameId")
			.setParameter("id", game_id)
			.getResultList();		
	}
}
