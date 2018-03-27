package com.rummy.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rummy.web.entity.GameRounds;
import com.rummy.web.services.GameRoundsService;
import com.rummy.web.services.GameService;

@RestController
@RequestMapping("/rounds")
public class GameRoundsController {
	
	@Autowired
	private GameRoundsService gameRoundsService;
	
	@Autowired
	private GameService gameService;
	
	@RequestMapping("/create/{game_id}")
	public GameRounds create(@PathVariable int game_id) {		
		return gameRoundsService.add(gameService.findById(game_id));
	}	
	
	@RequestMapping("/count/{game_id}")
	public Integer count(@PathVariable String game_id) {
		return gameRoundsService.getRoundsCount(Integer.parseInt(game_id));
		//return gameRoundsService.getRoundsCount(game_id);
	}
}
