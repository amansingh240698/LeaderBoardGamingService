package com.intuitcraft.leaderboard.services;

import com.intuitcraft.leaderboard.entity.PlayerScore;
import com.intuitcraft.leaderboard.exceptions.DatabaseStorageException;

public interface ScoreIngestionToStorage {
	public void publishToStore(PlayerScore newScore) throws DatabaseStorageException;
}
