package com.rummy.web.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rummy.web.entity.GameRounds;

@Transactional
public interface GameRoundsDao extends JpaRepository<GameRounds, Integer>{

}
