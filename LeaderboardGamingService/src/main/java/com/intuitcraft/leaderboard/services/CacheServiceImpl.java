package com.intuitcraft.leaderboard.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.intuitcraft.leaderboard.strategy.LeaderBoardStrategy;
import com.intuitcraft.leaderboard.entity.PlayerScore;
import com.intuitcraft.leaderboard.exceptions.CacheInitializationException;
import com.intuitcraft.leaderboard.exceptions.CacheUpdateFailureException;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class CacheServiceImpl implements CacheService<PlayerScore> {

    private int topN;
    private PriorityQueue<PlayerScore> minHeap;
    private Map<String, PlayerScore> playerToScore;
    private LeaderBoardStrategy strategy;

    private static final Logger logger = LoggerFactory.getLogger(CacheServiceImpl.class);

    public CacheServiceImpl(LeaderBoardStrategy strategy) {
        this.strategy = strategy;
    }

    public void initialize(int topN, List<PlayerScore> dataSet) throws CacheInitializationException {
        this.topN = topN;
        try {
            minHeap = new PriorityQueue<>(strategy.getComparator());
            playerToScore = new ConcurrentHashMap<>();
            for (PlayerScore score : dataSet) {
                addtoCache(score);
            }
        } catch (Exception e) {
            logger.error("Failed to initialize cache - " + e.getMessage());
            throw new CacheInitializationException("Failed to initialize cache",
                (JsonProcessingException) e);
        }
    }

    public void addtoCache(PlayerScore score) throws CacheUpdateFailureException {
        try {
            PlayerScore existingScore = playerToScore.get(score.getPlayerId());
            if (existingScore != null) {
                if (strategy.compare(score, existingScore) > 0) {
                    logger.debug("Updating " + existingScore.getPlayerId() + " to " + score.getScore());
                    minHeap.remove(existingScore);
                    playerToScore.put(score.getPlayerId(), score);
                    minHeap.add(score);
                }
            } else {
                if (minHeap.size() < topN) {
                    minHeap.add(score);
                    playerToScore.put(score.getPlayerId(), score);
                } else if (strategy.compare(score, minHeap.peek()) > 0) {
                    PlayerScore removedScore = minHeap.poll();
                    minHeap.add(score);
                    playerToScore.remove(removedScore.getPlayerId());
                    playerToScore.put(score.getPlayerId(), score);
                }
            }
        } catch (Exception e) {
            logger.error("Failed to update cache - " + e.getMessage());
            throw new CacheUpdateFailureException(e.getMessage());
        }
    }

    public List<PlayerScore> getTopNplayers() {
        List<PlayerScore> res = new ArrayList<>(minHeap);
        res.sort(strategy.getComparator().reversed());
        return res;
    }

    @Override
    public void setStrategy(LeaderBoardStrategy newStrategy) throws CacheUpdateFailureException {
        this.strategy = newStrategy;
        Comparator<PlayerScore> newComparator = strategy.getComparator();
        List<PlayerScore> currentScores = new ArrayList<>(playerToScore.values());
        minHeap = new PriorityQueue<>(topN, newComparator);
        for (PlayerScore score : currentScores) {
            addtoCache(score);
        }
    }
}
