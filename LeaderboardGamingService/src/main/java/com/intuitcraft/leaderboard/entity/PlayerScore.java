package com.intuitcraft.leaderboard.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PLAYERSCORE")
public class PlayerScore {
	@Id
	@Column(name = "player_id")
	private String playerId;
	@Column(name = "score")
	private Long score;

	public PlayerScore() {
	}

	public PlayerScore(String playerId, long score) {
		setPlayerId(playerId);
		setScore(score);
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		if (playerId == null || playerId.trim().isEmpty()) {
			throw new IllegalArgumentException("Player ID cannot be null or empty");
		}
		this.playerId = playerId;
	}

	public Long getScore() {
		return score;
	}

	public void setScore(Long score) {
		if (score == null || score < 0) {
			throw new IllegalArgumentException("Score must be non-negative");
		}
		this.score = score;
	}

	@Override
	public String toString() {
		return "{" + playerId + " " + score + "}";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		PlayerScore that = (PlayerScore) o;
		return this.playerId.equals(that.playerId) && this.score.equals(that.score);
	}

	@Override
	public int hashCode() {
		// Ensure hashCode is consistent with equals
		int result = playerId.hashCode();
		result = 31 * result + score.hashCode();
		return result;
	}
}
