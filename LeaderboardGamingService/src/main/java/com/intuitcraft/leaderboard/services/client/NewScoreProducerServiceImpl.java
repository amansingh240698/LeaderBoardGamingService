package com.intuitcraft.leaderboard.services.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.intuitcraft.leaderboard.constants.Constants;
import com.intuitcraft.leaderboard.entity.PlayerScore;
import com.intuitcraft.leaderboard.exceptions.MessageQueueFailureException;

@Service
public class NewScoreProducerServiceImpl implements NewDataProducerService<PlayerScore> {
	
	Logger logger = LoggerFactory.getLogger(NewScoreProducerServiceImpl.class);
	
	@Autowired
	private KafkaTemplate<String, PlayerScore> kafkaTemplate;

	public void addDataToQueue(PlayerScore newScore) throws MessageQueueFailureException {
		try {
			kafkaTemplate.send(Constants.KAFKA_TOPIC, newScore);
		} catch (Exception e) {
			logger.error("Send message failed - " + e.getMessage());
			throw new MessageQueueFailureException(e.getMessage());
		}
		
	}

}
