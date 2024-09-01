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
import com.intuitcraft.leaderboard.repository.PlayerScoreRepository;

@SpringBootTest(classes = LeaderboardApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class LeaderBoardServiceTest {

	@Autowired
  LeaderBoardService leaderBoard;
	
	@Autowired
  PlayerScoreRepository scoreRepository;
	
	@Test
	public void test() {
		try {
			int i = 0;
			PlayerScore[] outputList = { new PlayerScore("OP", 700), new PlayerScore("RP", 200), new PlayerScore("GB", 100)};
			leaderBoard.createBoard(3);
			for (PlayerScore p : leaderBoard.getTopNPlayers()) {
				assertEquals(p, outputList[i++]);
			}
				
			leaderBoard.publish(new PlayerScore("GB", 5000));
			outputList[0] = new PlayerScore("GB", 5000);
			outputList[1] = new PlayerScore("IS", 500);
			outputList[2] = new PlayerScore("RP", 200);
			i = 0;
			Thread.sleep(20);
			for (PlayerScore p : leaderBoard.getTopNPlayers()) {
				assertEquals(p, outputList[i++]);
			}
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@After
	public void tearDown() {
		try {
			Thread.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		scoreRepository.deleteAll();
	}

}
