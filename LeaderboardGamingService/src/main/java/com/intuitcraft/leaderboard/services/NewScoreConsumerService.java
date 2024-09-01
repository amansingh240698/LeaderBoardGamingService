package com.intuitcraft.leaderboard.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.intuitcraft.leaderboard.constants.Constants;
import com.intuitcraft.leaderboard.entity.PlayerScore;

@Service
public class NewScoreConsumerService implements NewDataConsumerService<PlayerScore> {

	@Autowired
	ScoreIngestionService scoreIngestor;
	@Autowired
	private KafkaTemplate<String, PlayerScore> kafkaTemplate;
	Logger logger = LoggerFactory.getLogger(NewScoreConsumerService.class);

	@KafkaListener(topics = Constants.KAFKA_TOPIC, groupId = Constants.KAFKA_GROUP_ID)
	public void consumeDataFromQueue(PlayerScore newData) {
		try {
			scoreIngestor.publish(newData);
		} catch (Exception e) {
			logger.error("Could not publish new score ",  e);
			sendToDLQ(newData);
			return;
		}
		logger.debug("Published :{}" , newData);
	}

	private void sendToDLQ(PlayerScore message) {
		kafkaTemplate.send(Constants.DLQ_TOPIC, message);
	}

}
