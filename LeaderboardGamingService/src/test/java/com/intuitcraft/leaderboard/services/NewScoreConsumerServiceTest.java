package com.intuitcraft.leaderboard.services;

import com.intuitcraft.leaderboard.entity.PlayerScore;
import com.intuitcraft.leaderboard.exceptions.DatabaseStorageException;
import com.intuitcraft.leaderboard.exceptions.LeaderboardUpdateFailureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.*;

public class NewScoreConsumerServiceTest {

  @Mock
  private ScoreIngestionService scoreIngestor;
  @Mock
  private KafkaTemplate<String, PlayerScore> kafkaTemplate;

  @InjectMocks
  private NewScoreConsumerService newScoreConsumerService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testConsumeDataFromQueue_Success()
      throws Exception {
    PlayerScore playerScore = new PlayerScore();
    playerScore.setPlayerId("player1");
    playerScore.setScore(100l);
    doNothing().when(scoreIngestor).publish(playerScore);
    newScoreConsumerService.consumeDataFromQueue(playerScore);
    verify(scoreIngestor, times(1)).publish(playerScore);
  }

  @Test
  public void testConsumeDataFromQueue_Exception()
      throws Exception {
    PlayerScore playerScore = new PlayerScore();
    playerScore.setPlayerId("player1");
    playerScore.setScore(100l);
    doThrow(new RuntimeException("Publish failed")).when(scoreIngestor).publish(playerScore);
    newScoreConsumerService.consumeDataFromQueue(playerScore);
    verify(scoreIngestor, times(1)).publish(playerScore);
  }
}
