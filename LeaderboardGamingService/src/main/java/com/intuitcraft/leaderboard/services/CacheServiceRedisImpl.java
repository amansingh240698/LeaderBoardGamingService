package com.intuitcraft.leaderboard.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intuitcraft.leaderboard.entity.PlayerScore;
import com.intuitcraft.leaderboard.strategy.LeaderBoardStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service("cacheServiceRedisImpl")
public class CacheServiceRedisImpl implements CacheService<PlayerScore> {

  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  private static final String LEADERBOARD_KEY = "leaderboard";
  private static final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void initialize(int topN, List<PlayerScore> dataSet) {
    try {
      ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
      for (PlayerScore playerScore : dataSet) {
        String json = objectMapper.writeValueAsString(playerScore);
        valueOps.set(LEADERBOARD_KEY + ":" + playerScore.getPlayerId(), json);
      }
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Failed to serialize player score", e);
    } catch (RedisConnectionFailureException e) {
      throw new RuntimeException("Redis connection failed during initialization", e);
    } catch (Exception e) {
      throw new RuntimeException("Failed to initialize cache", e);
    }
  }

  @Override
  public void addtoCache(PlayerScore score) {
    try {
      ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
      String json = objectMapper.writeValueAsString(score);
      valueOps.set(LEADERBOARD_KEY + ":" + score.getPlayerId(), json);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Failed to serialize player score", e);
    } catch (RedisConnectionFailureException e) {
      throw new RuntimeException("Redis connection failed during update", e);
    } catch (Exception e) {
      throw new RuntimeException("Failed to update cache", e);
    }
  }

  @Override
  public List<PlayerScore> getTopNplayers() {
    List<PlayerScore> topPlayers = new ArrayList<>();
    try {
      ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
      Set<String> keys = redisTemplate.keys(LEADERBOARD_KEY + ":*");

      if (keys == null || keys.isEmpty()) {
        throw new RuntimeException("No data found in Redis cache");
      }

      for (String key : keys) {
        String json = valueOps.get(key);
        if (json == null) {
          continue;
        }
        try {
          PlayerScore playerScore = objectMapper.readValue(json, PlayerScore.class);
          topPlayers.add(playerScore);
        } catch (IOException e) {
          // Log and handle the exception if needed
        }
      }
      topPlayers.sort(Comparator.comparingLong(PlayerScore::getScore).reversed());

    } catch (RedisConnectionFailureException e) {
      throw new RuntimeException("Redis connection failed during retrieval", e);
    } catch (Exception e) {
      throw new RuntimeException("Failed to get top players from cache", e);
    }

    return topPlayers;
  }

  @Override
  public void setStrategy(LeaderBoardStrategy newStrategy) {
    // Implement strategy change if needed
  }
}
