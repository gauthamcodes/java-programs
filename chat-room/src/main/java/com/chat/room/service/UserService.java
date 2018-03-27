package com.chat.room.service;

import java.util.List;

import com.chat.room.model.User;

public interface UserService {
	
	List<User> getAll();

	void save(User user);
	
	boolean isUsernameAvailable(String username);

	boolean isAuthenticated(String username, String password);

	User getByUsername(String username);
}
