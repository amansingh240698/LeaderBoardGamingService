package com.intuitcraft.leaderboard.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.intuitcraft.leaderboard.entity.PlayerScore;
import com.intuitcraft.leaderboard.exceptions.DatabaseStorageException;
import com.intuitcraft.leaderboard.exceptions.LeaderboardUpdateFailureException;
import com.intuitcraft.leaderboard.repository.PlayerScoreRepository;

@Service
public class ScoreIngestionServiceImpl implements ScoreIngestionToLeaderBoards,
		ScoreIngestionToStorage,
		ScoreIngestionService {
	
	List<LeaderBoardService> leaderBoards = new ArrayList<LeaderBoardService>();
	
	@Autowired
	PlayerScoreRepository scoreRepository;

	public void publishToStore(PlayerScore newScore) throws DatabaseStorageException {
		try {
			Optional<PlayerScore> scoreAlreadyPresent = scoreRepository.findById(newScore.getPlayerId());
			if (scoreAlreadyPresent.isPresent() && scoreAlreadyPresent.get().getScore() >= newScore.getScore()) {
				return;
			}
			scoreRepository.save(newScore);
		} catch (Exception e) {
			throw new DatabaseStorageException("Could not publish data to storage");
		}
		
	}

	public void registerLeaderBoard(LeaderBoardService leaderBoard) {
		leaderBoards.add(leaderBoard);
	}

	public void publishToLeaderBoards(PlayerScore newScore) throws LeaderboardUpdateFailureException {
		for (LeaderBoardService leaderBoard : leaderBoards) {
			leaderBoard.publish(newScore);
		}
	}


	public void publish(PlayerScore newScore) throws LeaderboardUpdateFailureException, DatabaseStorageException {
		publishToStore(newScore);
		publishToLeaderBoards(newScore);
	}

}
