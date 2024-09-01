package com.intuitcraft.leaderboard.services;

import java.util.Comparator;
import java.util.List;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.intuitcraft.leaderboard.constants.Constants;
import com.intuitcraft.leaderboard.entity.PlayerScore;
import com.intuitcraft.leaderboard.exceptions.CacheInitializationException;
import com.intuitcraft.leaderboard.exceptions.LeaderboardNotInitializedException;
import com.intuitcraft.leaderboard.exceptions.LeaderboardUpdateFailureException;
import com.intuitcraft.leaderboard.repository.PlayerScoreRepository;

@Service
public class LeaderBoardServiceImpl implements LeaderBoardService {

	@Autowired
	@Qualifier("cacheServiceImpl")
	private CacheService<PlayerScore> cache;

	@Autowired
	private PlayerScoreRepository scoreRepository;

	@Autowired
	private ScoreIngestionToLeaderBoards scoreIngestor;

	private boolean leaderBoardInitialized;

	private Logger logger = LoggerFactory.getLogger(LeaderBoardServiceImpl.class);

	@PostConstruct
	public void createBoard() throws LeaderboardNotInitializedException {
		initializeBoard(Constants.DEFAULT_LEADERBOARD_SIZE);
	}

	private void initializeBoard(int topN) throws LeaderboardNotInitializedException {
		try {
			List<PlayerScore> allScores = scoreRepository.findAll();
			cache.initialize(topN, allScores);
			scoreIngestor.registerLeaderBoard(this);
			leaderBoardInitialized = true;
		} catch (CacheInitializationException e) {
			logger.error("Leader Board Initialization Failed - " + e.getMessage());
			throw new LeaderboardNotInitializedException(e.getMessage());
		}
	}

	public void createBoard(int topN) throws LeaderboardNotInitializedException {
		initializeBoard(topN);
	}

	public List<PlayerScore> getTopNPlayers() throws LeaderboardNotInitializedException {
		if (!leaderBoardInitialized) {
			logger.error("Leader Board Not Initialized - Cannot retrieve top players");
			throw new LeaderboardNotInitializedException("LeaderBoard not yet initialized");
		}
		try {
			return cache.getTopNplayers();
		} catch (Exception e) {
			logger.error("Cache access failed, falling back to MySQL", e);
			return getTopNPlayersFromMySQL();
		}
	}

	private List<PlayerScore> getTopNPlayersFromMySQL() {
		try {
			List<PlayerScore> allScores = scoreRepository.findAll();
			allScores.sort(Comparator.comparingLong(PlayerScore::getScore).reversed());
			return allScores.size() > Constants.DEFAULT_LEADERBOARD_SIZE
					? allScores.subList(0, Constants.DEFAULT_LEADERBOARD_SIZE)
					: allScores;
		} catch (Exception e) {
			logger.error("Failed to fetch top players from MySQL", e);
			throw new RuntimeException("Failed to fetch top players from MySQL", e);
		}
	}

	public void publish(PlayerScore newScore) throws LeaderboardUpdateFailureException {
		try {
			cache.addtoCache(newScore);
		} catch (Exception e) {
			logger.error("Leader Board Update failed - ", e);
		}
	}
}
