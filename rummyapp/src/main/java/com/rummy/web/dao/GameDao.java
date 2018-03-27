package com.rummy.web.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rummy.web.entity.Game;

@Transactional
public interface GameDao extends JpaRepository<Game, Integer>{

}
