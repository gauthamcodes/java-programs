package com.rummy.web.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.rummy.web.entity.Game;
import com.rummy.web.mapper.GameMapper;

@Transactional
@Repository
public class GameJDBCDaoImpl implements GameJDBCDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public List<Game> getAll() {
		String sql = "SELECT * FROM game";
		return jdbcTemplate.query(sql, new GameMapper());
	}
}
