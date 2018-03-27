package com.rummy.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rummy.web.dto.GameMembersDto;
import com.rummy.web.dto.MembersDto;
import com.rummy.web.entity.Game;
import com.rummy.web.entity.Players;
import com.rummy.web.services.GameMembersService;
import com.rummy.web.services.GameService;
import com.rummy.web.services.PlayersService;

@RestController
@RequestMapping("/members")
public class GameMembersController {
	
	@Autowired
	private GameMembersService gameMembersService;
	
	@Autowired
	private GameService gameService;
	
	@Autowired
	private PlayersService playersService;
	
	
	@RequestMapping(value = "/{game_id}", method=RequestMethod.GET)
	public List<MembersDto> list(@PathVariable String game_id) {
		List<MembersDto> members = new ArrayList<MembersDto>();
		/*gameMembersService.getByGameId(Integer.parseInt(game_id)).forEach(item -> {
			members.add(new MembersDto(item.getId(), item.getPlayers().getName(), item.getTotalScore()));
		});	*/	
		return members;
	}
	
	@RequestMapping(value = "/add", method=RequestMethod.POST)
	public void add(@RequestBody GameMembersDto gameMembersDto) {
		Game game = gameService.findById(gameMembersDto.getGame_id());
		for(Integer id:gameMembersDto.getMembers_id()) {
			Players players = playersService.findById(id);
			gameMembersService.save(game, players);
		}
	}
	
	@RequestMapping(value = "/update", method=RequestMethod.GET)
	public int update() {
		return gameMembersService.updateActive(1);
	}
	
	
	@RequestMapping(value = "/active/{member_id}") 
	public boolean active(@PathVariable String member_id) {
		return gameMembersService.isActive(Integer.parseInt(member_id));
	}
	
	
}
