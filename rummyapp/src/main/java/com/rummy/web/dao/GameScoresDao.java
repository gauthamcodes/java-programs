package com.rummy.web.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rummy.web.entity.GameScores;

@Transactional
public interface GameScoresDao extends JpaRepository<GameScores,Integer>{
	
}
