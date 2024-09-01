package com.intuitcraft.leaderboard.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import com.intuitcraft.leaderboard.entity.PlayerScore;
import com.intuitcraft.leaderboard.exceptions.CacheInitializationException;
import com.intuitcraft.leaderboard.exceptions.CacheUpdateFailureException;
import com.intuitcraft.leaderboard.strategy.ScoreBasedStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

@ExtendWith(MockitoExtension.class)
class CacheServiceImplTest {

  @Mock
  private ScoreBasedStrategy mockStrategy;

  @InjectMocks
  private CacheServiceImpl cacheServiceImpl;

  @BeforeEach
  void setUp() {
    // Initialize any additional setup if needed
  }

  @Test
  void testInitialize() throws CacheInitializationException {
    List<PlayerScore> dataSet = Arrays.asList(
        new PlayerScore("player1", 10L),
        new PlayerScore("player2", 20L)
    );
    when(mockStrategy.getComparator()).thenReturn(Comparator.comparingLong(PlayerScore::getScore));
    cacheServiceImpl.initialize(2, dataSet);
    List<PlayerScore> topPlayers = cacheServiceImpl.getTopNplayers();
    assertThat(topPlayers).hasSize(2);
    assertThat(topPlayers).extracting(PlayerScore::getPlayerId)
        .containsExactlyInAnyOrder("player1", "player2");
  }

  @Test
  void testAddtoCache() throws CacheUpdateFailureException, CacheInitializationException {
    // Setup
    PlayerScore newScore = new PlayerScore("player1", 30L);
    PlayerScore existingScore = new PlayerScore("player1", 20L);
    when(mockStrategy.compare(newScore, existingScore)).thenReturn(1);
    when(mockStrategy.getComparator()).thenReturn(Comparator.comparingLong(PlayerScore::getScore));
    cacheServiceImpl.initialize(2, Collections.singletonList(existingScore));
    cacheServiceImpl.addtoCache(newScore);
    List<PlayerScore> topPlayers = cacheServiceImpl.getTopNplayers();
    assertThat(topPlayers).hasSize(1);
    assertThat(topPlayers.get(0).getScore()).isEqualTo(30L);
  }

  @Test
  void testGetTopNplayers() throws CacheInitializationException {
    List<PlayerScore> dataSet = Arrays.asList(
        new PlayerScore("player1", 10L),
        new PlayerScore("player2", 20L),
        new PlayerScore("player3", 30L)
    );
    when(mockStrategy.getComparator()).thenReturn(Comparator.comparingLong(PlayerScore::getScore));
    cacheServiceImpl.initialize(3, dataSet);
    List<PlayerScore> topPlayers = cacheServiceImpl.getTopNplayers();
    assertThat(topPlayers).hasSize(3);
    assertThat(topPlayers.get(0).getScore()).isEqualTo(30L);
    assertThat(topPlayers.get(1).getScore()).isEqualTo(20L);
  }

}
