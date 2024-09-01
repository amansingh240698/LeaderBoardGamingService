package com.intuitcraft.leaderboard.entity;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class LeaderBoard {
	@NotBlank(message = "Board ID cannot be blank")
	@NotNull(message = "Leaderboard ID cannot be null")
	private String boardID;

	@NotNull(message = "Leaderboard size cannot be null")
	@Min(value = 1, message = "Leaderboard size must be greater than 0")
	private Integer leaderBoardSize;
	
	public LeaderBoard() {
		super();
	}
	public LeaderBoard(String boardID, Integer leaderBoardSize) {
		super();
		this.boardID = boardID;
		this.leaderBoardSize = leaderBoardSize;
	}
	public String getBoardID() {
		return boardID;
	}
	public void setBoardID(String boardID) {
		this.boardID = boardID;
	}
	public Integer getLeaderBoardSize() {
		return leaderBoardSize;
	}
	public void setLeaderBoardSize(Integer leaderBoardSize) {
		this.leaderBoardSize = leaderBoardSize;
	}
}
