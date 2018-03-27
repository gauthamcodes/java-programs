package com.gautham.cricketstream;

import com.gautham.cricketstream.constant.Occurrence;
import com.gautham.cricketstream.constant.Player;
import com.gautham.cricketstream.model.Innings;
import com.gautham.cricketstream.service.ProbabilityService;
import com.gautham.cricketstream.service.impl.IProbabilityService;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws InterruptedException
    {
    	ProbabilityService service = null;
    	
    	Innings innings = new Innings();
    	innings.setPlayer(Player.KOHLI);
    	innings.setBallsFaced(0);
    	innings.setRunScored(0);
    	
    	while(true) {
    		service = new IProbabilityService();
    		
    		Double power = Math.random();
    		Double difficulty = Math.random();
    		innings.setPower(power);
    		innings.setDifficulty(difficulty);
    		for(Occurrence occ:Occurrence.values()) {
    			service.doubles(innings);
    		}
    		innings.bowled();
    	}
    	
    }
}
