package com.rummy.web.dao;

import java.util.List;

import com.rummy.web.entity.Game;

public interface GameJDBCDao {
	
	List<Game> getAll();
}
