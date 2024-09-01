package com.intuitcraft.leaderboard.controller.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.intuitcraft.leaderboard.entity.PlayerScore;
import com.intuitcraft.leaderboard.services.ScoreIngestionService;

@RestController
public class GameController {

	@Autowired
	ScoreIngestionService scoreIngestor;
	
	Logger logger = LoggerFactory.getLogger(GameController.class);
	
	@PostMapping("/updateScore")
	public void updateScore(@RequestBody PlayerScore newScore) {
		try {
			scoreIngestor.publish(newScore);
		} catch (Exception e) {
			logger.error("Leaderboard Update failed - " + e.getMessage());
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
}
