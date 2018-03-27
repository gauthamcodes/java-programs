package com.chat.room.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.chat.room.dto.LoginDto;
import com.chat.room.dto.UserDto;
import com.chat.room.exception.IncorrectPasswordException;
import com.chat.room.exception.UsernameNotAvailableException;
import com.chat.room.exception.UsernameNotFoundException;
import com.chat.room.model.User;
import com.chat.room.service.UserService;
import com.chat.room.utils.TokenGenerator;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TokenGenerator tokenGenerator;
	
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public List<User> listOfUsers(Principal principal) {
		//String username = SecurityContextHolder.getContext().getAuthentication().getName();	
		
		return userService.getAll();
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public LoginDto login(@RequestBody UserDto user) {		
		if(userService.isUsernameAvailable(user.getUsername())) {
			throw new UsernameNotFoundException(user.getUsername());
		}
		else {
			if(userService.isAuthenticated(user.getUsername(), user.getPassword())) {
				return new LoginDto(tokenGenerator.getToken(user.getUsername()));
			}
			else {
				throw new IncorrectPasswordException();
			}
		}		
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public void register(@RequestBody User user) {
		if(userService.isUsernameAvailable(user.getUsername()))
			userService.save(user);
		else
			throw new UsernameNotAvailableException(user.getUsername());
	}
	
	@RequestMapping(value = "/{username}", method = RequestMethod.GET)
	public User getByUsername(@PathVariable String username) {
		return userService.getByUsername(username);
	}
}
