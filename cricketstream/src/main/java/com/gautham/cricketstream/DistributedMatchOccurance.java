package com.gautham.cricketstream;

import java.util.HashMap;
import java.util.Map;

import com.gautham.cricketstream.constant.Occurrence;

public class DistributedMatchOccurance {

    private Map<Occurrence, Double> distribution;
    private double distSum;

    public DistributedMatchOccurance() {
        distribution = new HashMap<>();
    }

    public void addNumber(Occurrence value, double distribution) {
    	System.out.println(value + " -------- " + distribution);
        if (this.distribution.get(value) != null) {
            distSum -= this.distribution.get(value);
        }
        this.distribution.put(value, distribution);
        distSum += distribution;
    }

    public Occurrence getDistributedRandomNumber() {
        double rand = Math.random();
        double ratio = 1.0f / distSum;
        double tempDist = 0;
        for (Occurrence i : distribution.keySet()) {
            tempDist += distribution.get(i);
            if (rand / ratio <= tempDist) {
                return i;
            }
        }
        return null;
    }

}