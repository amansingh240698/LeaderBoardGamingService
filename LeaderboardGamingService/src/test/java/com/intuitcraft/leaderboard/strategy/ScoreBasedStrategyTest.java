package com.intuitcraft.leaderboard.strategy;

import static org.assertj.core.api.Assertions.assertThat;

import com.intuitcraft.leaderboard.entity.PlayerScore;
import java.util.Comparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ScoreBasedStrategyTest {

  private ScoreBasedStrategy scoreBasedStrategyUnderTest;

  @BeforeEach
  void setUp() {
    scoreBasedStrategyUnderTest = new ScoreBasedStrategy();
  }

  @Test
  void testCompare() {
    final PlayerScore a = new PlayerScore("player1", 100L);
    final PlayerScore b = new PlayerScore("player2", 10L);
    final int result = scoreBasedStrategyUnderTest.compare(a, b);
    assertThat(result).isEqualTo(1);
  }

}
