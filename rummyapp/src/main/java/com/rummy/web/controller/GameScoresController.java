package com.rummy.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rummy.web.services.GameScoresService;

@RestController
@RequestMapping("/scores")
public class GameScoresController {
	
	@Autowired
	private GameScoresService gameScoresService;
	
	@RequestMapping("/add")
	public Integer add() {
		return gameScoresService.addScore(1, 2, 40).getScore();
	}
}
