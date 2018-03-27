package com.rummy.web.services;

import java.util.List;

import com.rummy.web.entity.Players;

public interface PlayersService {
	void save(Players players);
	List<Players> findAll();
	Players findById(int id);
	void deleteById(int id);
}
