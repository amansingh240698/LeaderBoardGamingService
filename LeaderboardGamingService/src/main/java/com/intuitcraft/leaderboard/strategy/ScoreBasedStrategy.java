package com.intuitcraft.leaderboard.strategy;

import java.util.Comparator;
import com.intuitcraft.leaderboard.entity.PlayerScore;

public class ScoreBasedStrategy implements LeaderBoardStrategy {
  @Override
  public int compare(PlayerScore a, PlayerScore b) {
    return Long.compare(a.getScore(), b.getScore());
  }

  @Override
  public Comparator<PlayerScore> getComparator() {
    return Comparator.comparingLong(PlayerScore::getScore);
  }
}