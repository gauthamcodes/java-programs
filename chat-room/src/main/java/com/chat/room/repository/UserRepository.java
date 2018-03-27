package com.chat.room.repository;

import java.util.List;

import com.chat.room.model.User;

public interface UserRepository{
	List<User> getAll();

	void save(User user);

	User getByUsername(String username);
}
