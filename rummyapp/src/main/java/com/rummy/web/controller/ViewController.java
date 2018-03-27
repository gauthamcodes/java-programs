package com.rummy.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {
	@GetMapping(value="/home")
	public String home() {
		return "home";
	}	
	
	@GetMapping(value="/users")
	public String users() {
		return "users";
	}	
	
	@GetMapping(value="/members")
	public String members() {
		return "members";
	}	
	
	@GetMapping(value="/scores")
	public String scores() {
		return "scores";
	}	
}
