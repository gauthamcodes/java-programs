package com.gautham.cricketstream;

import java.util.concurrent.ThreadLocalRandom;

import com.gautham.cricketstream.constant.Batsman;
import com.gautham.cricketstream.constant.Occurrence;
import com.gautham.cricketstream.constant.Wicket;
import com.gautham.cricketstream.model.Innings;

public class Simulator {

	private Innings innings;

	public Simulator() {
		this.innings = new Innings(Batsman.values()[ThreadLocalRandom.current().nextInt(0, Batsman.values().length)]);
	}

	public Innings getInnings() {
		Integer maxBalls = ThreadLocalRandom.current().nextInt(0, 150);
		while (true) {
			play();
			if (!innings.getNotOut() || innings.getBallsFaced() == maxBalls) {
				if (!innings.getNotOut()) {
					innings.setWicket(Wicket.values()[ThreadLocalRandom.current().nextInt(0, Wicket.values().length)]);
				}
				break;
			}
		}
		return innings;
	}

	private void play() {
		Occurrence occurrence = Distribution.getOccurrence(innings.getBatsman(), innings.bowl());
		switch (occurrence) {
		case SIX:
			innings.six();
			break;
		case FOUR:
			innings.four();
			break;
		case DOUBLE:
			innings.two();
			break;
		case SINGLE:
			innings.one();
			break;
		case MAIDEN:
			innings.maiden();
			break;
		case WICKET:
			innings.wicket();
			break;
		default:
			break;
		}
	}
}
