package com.intuitcraft.leaderboard.services;

import com.intuitcraft.leaderboard.strategy.LeaderBoardStrategy;
import java.util.List;

import com.intuitcraft.leaderboard.exceptions.CacheInitializationException;
import com.intuitcraft.leaderboard.exceptions.CacheUpdateFailureException;


public interface CacheService<T> {
	public void initialize(int topN, List<T> data) throws CacheInitializationException;
	public void addtoCache(T data) throws CacheUpdateFailureException;
	public List<T> getTopNplayers();
	public void setStrategy(LeaderBoardStrategy leaderBoardStrategy)
			throws CacheUpdateFailureException;
}
