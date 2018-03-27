package com.rummy.web.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.rummy.web.entity.Game;

public class GameMapper implements RowMapper<Game>{

	@Override
	public Game mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		Game game = new Game();
		game.setId(rs.getInt("id"));
		game.setMaxScore(rs.getInt("max_score"));
		game.setCreatedDate(rs.getDate("created_date"));
		game.setWinner(rs.getInt("winner"));
		return game;
	}

}
