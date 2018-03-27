package com.rummy.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rummy.web.dto.PlayersDto;
import com.rummy.web.entity.Players;
import com.rummy.web.services.PlayersService;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:56679")
@RequestMapping("/players")
public class PlayersController {
	
	@Autowired
	private PlayersService playersService;
	
	@RequestMapping(value = "/list")
	public List<PlayersDto> all() {
		List<PlayersDto> playersDto = new ArrayList<PlayersDto>();
		for(Players players:playersService.findAll()) {
			playersDto.add(new PlayersDto(players.getId(), players.getName(), players.getUserName()));
		}
		return playersDto;
	}
	
	@RequestMapping(value = "/availability/{user_name}")
	public boolean availability(@PathVariable String user_name) {
		for(Players players:playersService.findAll()) {
			if(players.getUserName().equals(user_name)) {
				return false;
			}
		}	
		return true;
	}
	
	
	@RequestMapping(value = "/create", method=RequestMethod.POST)
	public Map<String, String> create(@RequestBody PlayersDto playersDto) {
		Map<String, String> response = new HashMap<String, String>() ;
		for(Players players:playersService.findAll()) {
			if(players.getUserName().equals(playersDto.getUser_name())) {
				response.put("status", "failed");
				response.put("reason", "Username already taken.");
				return response;
			}
		}		
		playersService.save(new Players(playersDto.getName(), playersDto.getUser_name()));
		response.put("status", "success");
		return response;
	}
	
	@RequestMapping("/save")
	public void save(){
		Players players = new Players();
		players.setId(1);
		players.setName("Gautham");
		players.setUserName("gauti");
		playersService.save(players);
		
	}
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable Integer id) {
		playersService.deleteById(id);
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public void update(@RequestBody PlayersDto playersDto) {
		playersService.save(new Players(playersDto.getId(),playersDto.getName(),playersDto.getUser_name()));
	}
	
	@RequestMapping("/{id}")
	public PlayersDto get(@PathVariable String id) {
		Players players = playersService.findById(Integer.parseInt(id));
		return new PlayersDto(players.getId(), players.getName(), players.getUserName());
	}
	
	@RequestMapping("/hi")
	public String hi() {
		return "hi";
	}
}
