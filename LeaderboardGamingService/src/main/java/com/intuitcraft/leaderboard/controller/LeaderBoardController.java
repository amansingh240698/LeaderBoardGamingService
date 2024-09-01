package com.intuitcraft.leaderboard.controller;

import com.intuitcraft.leaderboard.exceptions.InvalidLeaderBoardSizeException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.intuitcraft.leaderboard.entity.LeaderBoard;
import com.intuitcraft.leaderboard.entity.PlayerScore;
import com.intuitcraft.leaderboard.exceptions.LeaderboardNotInitializedException;
import com.intuitcraft.leaderboard.services.LeaderBoardService;

@RestController
public class LeaderBoardController {
	
	@Autowired
	LeaderBoardService leaderBoard;
	
	Logger logger = LoggerFactory.getLogger(LeaderBoardController.class);

	@GetMapping("/getTopScorers")
	public List<PlayerScore> getTopScorers() {
		try {
			return leaderBoard.getTopNPlayers();
		} catch (LeaderboardNotInitializedException e) {
			logger.error("Leaderboard not initialized - " + e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Please register/create LeaderBoard first");
		} catch (Exception e) {
			logger.error("Couldn't get top scores - " + e.getMessage());
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	@PostMapping("/createBoard")
	public ResponseEntity<Void> createBoard(@RequestBody LeaderBoard leaderBoard) {
		try {
			if (leaderBoard.getLeaderBoardSize() <= 0) {
				throw new InvalidLeaderBoardSizeException("LeaderBoardSize must be greater than zero.");
			}
			this.leaderBoard.createBoard(leaderBoard.getLeaderBoardSize());
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (InvalidLeaderBoardSizeException e) {
			logger.error("Invalid leaderboard size :{} " , e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		} catch (Exception e) {
			logger.error("Leaderboard creation failed " , e.getMessage());
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
}
