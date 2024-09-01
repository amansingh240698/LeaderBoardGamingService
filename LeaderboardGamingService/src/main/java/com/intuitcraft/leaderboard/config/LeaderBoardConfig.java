package com.intuitcraft.leaderboard.config;

import com.intuitcraft.leaderboard.services.CacheServiceImpl;
import com.intuitcraft.leaderboard.strategy.LeaderBoardStrategy;
import com.intuitcraft.leaderboard.strategy.ScoreBasedStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LeaderBoardConfig {

  @Bean
  public LeaderBoardStrategy defaultStrategy() {
    return new ScoreBasedStrategy();
  }

  @Bean
  public CacheServiceImpl cacheService(LeaderBoardStrategy strategy) {
    return new CacheServiceImpl(strategy);
  }
}
