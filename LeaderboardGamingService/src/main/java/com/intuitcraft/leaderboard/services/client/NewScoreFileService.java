package com.intuitcraft.leaderboard.services.client;

import com.intuitcraft.leaderboard.entity.PlayerScore;
import com.intuitcraft.leaderboard.exceptions.MessageQueueFailureException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class NewScoreFileService {

    @Autowired
    private NewScoreProducerServiceImpl scoreProducerService;

    private static final Logger logger = LoggerFactory.getLogger(NewScoreFileService.class);
    private static final String FILE_NAME = "scores.txt";

    @PostConstruct
    public void init() {
        processFile();
    }

    @Scheduled(fixedRate = 30000) 
    public void processFileAndPublishToKafka() {
        processFile();
    }

    private void processFile() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new ClassPathResource(FILE_NAME).getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String playerId = parts[0];
                    Long score;
                    try {
                        score = Long.parseLong(parts[1]);
                    } catch (NumberFormatException e) {
                        logger.error("Invalid score format for player " + playerId + " - " + parts[1]);
                        continue;
                    }

                    PlayerScore newScore = new PlayerScore(playerId, score);
                    try {
                        scoreProducerService.addDataToQueue(newScore);
                    } catch (MessageQueueFailureException e) {
                        logger.error("Failed to publish score for player " + playerId + " - " + e.getMessage());
                    }
                } else {
                    logger.error("Invalid line format - " + line);
                }
            }
        } catch (IOException e) {
            logger.error("Failed to read file - " + e.getMessage());
        }
    }
}
