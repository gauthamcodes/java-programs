package com.rummy.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rummy.web.dto.GameScoresDto;
import com.rummy.web.dto.GameStatusDto;
import com.rummy.web.dto.MembersDto;
import com.rummy.web.entity.GameScores;
import com.rummy.web.services.GameRoundsService;
import com.rummy.web.services.GameScoresService;

@RestController
@RequestMapping("/scoreboard")
public class MainController {
	
	
	@Autowired
	private GameRoundsService gameRoundsService;
	
	@Autowired
	private GameScoresService gameScoresService;
	
	private int noOfActivePlayers = 0;
	
	@RequestMapping(value = "/add", method=RequestMethod.POST)
	public GameStatusDto addScore(@RequestBody GameScoresDto gameScoresDto) {
		noOfActivePlayers = 0;
		GameStatusDto gameStatus = new GameStatusDto();
		gameScoresDto.getPlayers()
			.forEach(players -> {
				GameScores gameScores = gameScoresService.addScore(gameScoresDto.getRound_id(), players.getPlayer_id(), players.getScore());
				gameStatus.getMembers().add(new MembersDto(players.getPlayer_id(), gameScores.getGameMembers().getPlayers().getName(), 
						gameScores.getGameMembers().getTotalScore(), gameScores.getGameMembers().isActive()));
			});	
		gameStatus.getMembers().forEach(item -> {
			if(item.isActive()) {
				noOfActivePlayers = noOfActivePlayers + 1;
			}
		});
		
		return gameStatus;
	}
	
	@RequestMapping("/{game_id}")
	public Map<String, Object> details(@PathVariable String game_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		gameRoundsService.getByGameId(Integer.parseInt(game_id))
			.forEach(gameRounds -> {
				Map<String, Object> object = new HashMap<String, Object>();				
				gameScoresService.getByRoundId(gameRounds.getId())
					.forEach(gameScores -> {
						object.put(gameScores.getGameMembers().getPlayers().getName(), 
								gameScores.getScore());
					});
				response.put("Round "+gameRounds.getCustomId(), 
						object);
			});
		return response;
	}

}
