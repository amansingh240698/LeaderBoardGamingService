package com.intuitcraft.leaderboard.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.intuitcraft.leaderboard.LeaderboardApplication;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.intuitcraft.leaderboard.entity.PlayerScore;
import com.intuitcraft.leaderboard.exceptions.CacheInitializationException;
import com.intuitcraft.leaderboard.exceptions.DatabaseStorageException;
import com.intuitcraft.leaderboard.exceptions.LeaderboardNotInitializedException;
import com.intuitcraft.leaderboard.exceptions.LeaderboardUpdateFailureException;
import com.intuitcraft.leaderboard.repository.PlayerScoreRepository;

@SpringBootTest(classes = LeaderboardApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class ScoreIngestorTest {
	@Autowired
  LeaderBoardService leaderBoard;
	@Autowired
	ScoreIngestionServiceImpl scoreIngestor;
	@Autowired
  PlayerScoreRepository scoreRepository;
	
	@Test
	public void test() {
		try {
			try {
				try {
					scoreIngestor.publish(new PlayerScore("OP", 700));
				} catch (DatabaseStorageException e) {
					fail(e.getMessage());
				}
			} catch (LeaderboardUpdateFailureException e) {
				fail(e.getMessage());
			}
			for (PlayerScore p : leaderBoard.getTopNPlayers())
				assertEquals(p, new PlayerScore("OP", 700));
			try {
				leaderBoard.createBoard(3);
			} catch (CacheInitializationException e) {
				fail(e.getMessage());
			}
			try {
				try {
					scoreIngestor.publish(new PlayerScore("OP", 600));
					scoreIngestor.publish(new PlayerScore("GB", 700));
					PlayerScore[] outputList = { new PlayerScore("OP", 700), new PlayerScore("GB", 700), new PlayerScore("IS", 500)};
					int i = 0;
					for (PlayerScore p : leaderBoard.getTopNPlayers()) {
						assertEquals(p, outputList[i++]);
					}
				} catch (DatabaseStorageException e) {
					fail(e.getMessage());
				}
			} catch (LeaderboardUpdateFailureException e) {
				fail(e.getMessage());
			}
			
		
			for (PlayerScore p : leaderBoard.getTopNPlayers())
				System.out.println(p);
		} catch (LeaderboardNotInitializedException e) {
			fail(e.getMessage());
		}	
	}
	
	@After
	public void tearDown() {
		scoreRepository.deleteAll();
	}

}
