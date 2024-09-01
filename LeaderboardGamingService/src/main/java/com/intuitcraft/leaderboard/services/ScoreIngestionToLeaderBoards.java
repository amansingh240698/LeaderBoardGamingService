package com.intuitcraft.leaderboard.services;

import com.intuitcraft.leaderboard.entity.PlayerScore;
import com.intuitcraft.leaderboard.exceptions.LeaderboardUpdateFailureException;

public interface ScoreIngestionToLeaderBoards {
	public void registerLeaderBoard(LeaderBoardService leaderBoard);
	public void publishToLeaderBoards(PlayerScore newScore) throws LeaderboardUpdateFailureException;
}
