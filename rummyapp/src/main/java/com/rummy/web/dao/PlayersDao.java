package com.rummy.web.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rummy.web.entity.Players;

@Transactional
public interface PlayersDao extends JpaRepository<Players, Integer>{

}
