package com.rummy.web.services;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import com.rummy.web.dao.PlayersDao;
import com.rummy.web.entity.Players;

@Service
public class PlayersServiceImpl implements PlayersService {

	@Autowired
	private PlayersDao playersDao;
	
	@Autowired
	protected EntityManager entityManager;

	private Sort sortByNameAsc() {
		return new Sort(Sort.Direction.ASC, "name");
	}

	public void save(Players players) {
		playersDao.save(players);
	}

	public List<Players> findAll() {
		return playersDao.findAll(sortByNameAsc());
	}

	public Players findById(int id) {
		return playersDao.findOne(id);
	}
	
	@Transactional
	@Modifying
	public void deleteById(int id) {
		entityManager.remove(findById(id));
	}
}
