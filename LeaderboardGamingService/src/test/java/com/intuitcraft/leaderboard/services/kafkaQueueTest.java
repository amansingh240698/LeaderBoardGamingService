package com.intuitcraft.leaderboard.services;

import static org.junit.Assert.fail;

import com.intuitcraft.leaderboard.LeaderboardApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.intuitcraft.leaderboard.entity.PlayerScore;
import com.intuitcraft.leaderboard.exceptions.MessageQueueFailureException;
import com.intuitcraft.leaderboard.services.client.NewDataProducerService;

@SpringBootTest(classes = LeaderboardApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class kafkaQueueTest {

	@Autowired
	NewDataProducerService<PlayerScore> producer;

	@Test
	public void test() {
		try {
			producer.addDataToQueue(new PlayerScore("GB", 100));
		} catch (MessageQueueFailureException e) {
			// TODO Auto-generated catch block
			fail(e.getMessage());
		}
	}
}
