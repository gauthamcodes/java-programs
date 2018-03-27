package com.rummy.web.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rummy.web.entity.GameMembers;

@Transactional
public interface GameMembersDao extends JpaRepository<GameMembers, Integer>{
	
}
