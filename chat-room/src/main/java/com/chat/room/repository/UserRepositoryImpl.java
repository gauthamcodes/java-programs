package com.chat.room.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.chat.room.model.User;
import com.chat.room.repository.util.UserMapper;

@Repository
public class UserRepositoryImpl implements UserRepository {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public List<User> getAll() {
		List<User> users = jdbcTemplate.query("select * from user", new UserMapper());
		return users;
	}

	@Override
	public void save(User user) {
		jdbcTemplate.update("insert into user (name, username, password) values (?, ?, ?)",
				user.getName(), user.getUsername(), user.getPassword());
	}

	@Override
	public User getByUsername(String username) {
		User user = jdbcTemplate.queryForObject("select * from user where username = ?", new Object[] {username}, new UserMapper());
		return user;
	}
}
