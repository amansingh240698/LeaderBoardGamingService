package com.intuitcraft.leaderboard.services;

import java.util.List;

import com.intuitcraft.leaderboard.entity.PlayerScore;
import com.intuitcraft.leaderboard.exceptions.CacheInitializationException;
import com.intuitcraft.leaderboard.exceptions.LeaderboardNotInitializedException;
import com.intuitcraft.leaderboard.exceptions.LeaderboardUpdateFailureException;

public interface LeaderBoardService {
	public void createBoard(int topN) throws CacheInitializationException, LeaderboardNotInitializedException;
	public List<PlayerScore> getTopNPlayers() throws LeaderboardNotInitializedException;
	public void publish(PlayerScore newScore) throws LeaderboardUpdateFailureException;
}
