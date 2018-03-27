package com.gautham.cricketstream;

import java.util.Random;

import com.gautham.cricketstream.constant.Player;
import com.gautham.cricketstream.model.Innings;

public class Simulator {
	private Player player;
	private static final Integer range = 100;

	public Simulator(Player player) {
		this.player = player;
	}

	public Innings start() {
		if (player != null) {
			System.out.println("Innings started for player " + player.getName());
			Innings innings = new Innings();
			Integer sixLimit = getSixLimit();

			while (true) {
				Integer random = new Random().nextInt(range);
				// if(random > )

				break;
			}
			return innings;
		} else {
			System.out.println("No player found to start the innings");
			return null;
		}
	}

	private Integer getSixLimit() {
		return null;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
}
