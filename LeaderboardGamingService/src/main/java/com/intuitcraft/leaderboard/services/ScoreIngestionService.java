package com.intuitcraft.leaderboard.services;

import com.intuitcraft.leaderboard.entity.PlayerScore;
import com.intuitcraft.leaderboard.exceptions.DatabaseStorageException;
import com.intuitcraft.leaderboard.exceptions.LeaderboardUpdateFailureException;

public interface ScoreIngestionService {
	public void publish(PlayerScore newScore) throws LeaderboardUpdateFailureException, DatabaseStorageException;
}
