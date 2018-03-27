package com.rummy.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rummy.web.dto.GameDto;
import com.rummy.web.entity.Game;
import com.rummy.web.services.GameService;

@RestController
@RequestMapping("/game")
public class GameController {
	
	@Autowired
	private GameService gameService;
	
	@Value("${rummy.maxScore}")
	private String maxscore;
	
	@RequestMapping("/maxscore")
	public List<Integer> maxscore() {
		List<Integer> maxScoreList = new ArrayList<Integer>();
		for(String score:maxscore.split(",")) {
			maxScoreList.add(Integer.parseInt(score));
		}
		return maxScoreList;
	}
	
	@RequestMapping(value = "/create", method=RequestMethod.POST)
	public void create(@RequestBody GameDto gameDto) {
		gameService.createGame(gameDto.getMax_score());		
	}
	
	@RequestMapping("/create")
	public Map<String, Object> create() {
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("status", "success");
		response.put("game", gameService.createGame());
		return response;
	}
	
	@RequestMapping("/{id}")
	public Game get(@PathVariable String id) {
		return gameService.findById(Integer.parseInt(id));
	}
	
	@RequestMapping("/all")
	public List<Game> getAll() {
		return gameService.findAll();
	}
	
}
