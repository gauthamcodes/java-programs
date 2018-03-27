package com.gautham.cricketstream.service;

import com.gautham.cricketstream.model.Innings;

public interface ProbabilityService {
	Double sixes(Innings innings);
	Double fours(Innings innings);
	Double doubles(Innings innings);
	Double singles(Innings innings);
	Double maidens(Innings innings);
	Double wickets(Innings innings);
}
