package com.chat.room.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chat.room.model.User;
import com.chat.room.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public List<User> getAll() {
		return userRepository.getAll();
	}

	@Override
	public void save(User user) {
		userRepository.save(user);
	}
	
	
	public boolean isUsernameAvailable(String username) {
		List<User> users = getAll();
		for(User user:users) {
			if(user.getUsername().equals(username)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean isAuthenticated(String username, String password) {
		List<User> users = getAll();
		for(User user:users) {
			if(user.getUsername().equals(username) && user.getPassword().equals(password)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public User getByUsername(String username) {
		// TODO Auto-generated method stub
		return userRepository.getByUsername(username);
	}
}
