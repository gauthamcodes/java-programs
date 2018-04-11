package com.gautham.cricketstream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;

import com.gautham.cricketstream.constant.Batsman;
import com.gautham.cricketstream.constant.Bowler;
import com.gautham.cricketstream.constant.Occurrence;

public class Distribution {
	public static Occurrence getOccurrence(Batsman batsman, Bowler bowler) {
		Map<Occurrence, Double> items = new HashMap<>();
		List<Pair<Occurrence, Double>> operations = new ArrayList<>();
		Double total = 0.0;
		for (Occurrence occurrence : Occurrence.values()) {
			Double distribution = getDistribution(occurrence, batsman, bowler);
			items.put(occurrence, distribution);
			total += distribution;
		}
		for (Occurrence occurrence : items.keySet()) {
			operations.add(new Pair<Occurrence, Double>(occurrence, items.get(occurrence) / total));
		}

		EnumeratedDistribution<Occurrence> distribution = new EnumeratedDistribution<Occurrence>(operations);
		return distribution.sample();
	}

	private static Double getDistribution(Occurrence occurrence, Batsman batsman, Bowler bowler) {
		Double probability = 0.0;
		Double total = 0.0;
		switch (occurrence) {
		case SIX:
			total += batsman.getSkill();
			total += batsman.getAggression();
			total += batsman.getStrength();
			total += batsman.getTemperment();
			total += batsman.getLuck();
			probability = (total / 5) * bowler.getSix();
			break;
		case FOUR:
			total += batsman.getSkill();
			total += batsman.getConsistency();
			total += batsman.getDefense();
			total += batsman.getAggression();
			probability = (total / 4) * bowler.getFour();
			break;
		case DOUBLE:
			total += batsman.getStamina();
			total += batsman.getAgility();
			total += batsman.getSkill();
			probability = (total / 3) * bowler.getTwo();
			break;
		case SINGLE:
			total += batsman.getDefense();
			total += batsman.getConsistency();
			total += batsman.getAgility();
			probability = (total / 3) * bowler.getOne();
			break;
		case MAIDEN:
			total += batsman.getDefense();
			total += batsman.getConsistency();
			probability = (total / 2) * bowler.getMaiden();
			break;
		case WICKET:
			total += (1 - batsman.getSkill());
			total += (1 - batsman.getDefense());
			total += (1 - batsman.getConsistency());
			total += batsman.getTemperment();
			total += batsman.getLuck();
			probability = (total / 4) * bowler.getWicket();
		default:
			break;
		}
		return probability;
	}
}
