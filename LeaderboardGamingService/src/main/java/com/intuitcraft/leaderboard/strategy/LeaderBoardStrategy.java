package com.intuitcraft.leaderboard.strategy;

import java.util.Comparator;

import com.intuitcraft.leaderboard.entity.PlayerScore;

public interface LeaderBoardStrategy {
	int compare(PlayerScore a, PlayerScore b);
	Comparator<PlayerScore> getComparator();
}