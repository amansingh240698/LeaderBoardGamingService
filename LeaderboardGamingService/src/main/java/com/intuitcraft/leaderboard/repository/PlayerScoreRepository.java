package com.intuitcraft.leaderboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.intuitcraft.leaderboard.entity.PlayerScore;

@Repository
public interface PlayerScoreRepository extends JpaRepository<PlayerScore, String>{

}
