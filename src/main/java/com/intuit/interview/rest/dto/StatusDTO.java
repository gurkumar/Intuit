package com.intuit.interview.rest.dto;

public class StatusDTO {
	private boolean validMove;
	private boolean won;
	public boolean isValidMove() {
		return validMove;
	}
	public void setValidMove(boolean validMove) {
		this.validMove = validMove;
	}
	public boolean isWon() {
		return won;
	}
	public void setWon(boolean won) {
		this.won = won;
	}

}
