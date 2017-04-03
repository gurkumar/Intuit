package com.intuit.interview.core.model;

import java.util.List;


public class Game {

	public enum State {
		PENDING,COMPLETE;

	}

	private Integer gameId;
	private List<Player> players;
	private String state;
	private Integer[] rowData;
	private Integer[] colData;
	private Integer diagnolCol1;
	private Integer diagnolCol2;
	private Integer movesLeft;
	private Integer gameSize;
	private Integer[][] cells;
	
	public Game () {}

	public Game (Integer gameId) {
		this.gameId = gameId;
	}
	
	public Game (Integer gameId, List<Player>players) {
		this (gameId);
		this.players = players;
	}

	public Integer getGameId() {
		return gameId;
	}

	public List<Player> getPlayers() {
		return this.players;
	}
	
	public void setPlayers (List<Player> players) {
		this.players = players;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Integer[] getRowData() {
		return rowData;
	}

	public void setRowData(Integer[] rowData) {
		this.rowData = rowData;
	}

	public Integer[] getColData() {
		return colData;
	}

	public void setColData(Integer[] colData) {
		this.colData = colData;
	}

	public Integer getDiagnolCol1() {
		return diagnolCol1;
	}

	public void setDiagnolCol1(Integer diagnolCol1) {
		this.diagnolCol1 = diagnolCol1;
	}

	public Integer getDiagnolCol2() {
		return diagnolCol2;
	}

	public void setDiagnolCol2(Integer diagnolCol2) {
		this.diagnolCol2 = diagnolCol2;
	}

	public Integer getMovesLeft() {
		return movesLeft;
	}

	public void setMovesLeft(Integer movesLeft) {
		this.movesLeft = movesLeft;
	}

	public Integer getGameSize() {
		return gameSize;
	}

	public void setGameSize(Integer gameSize) {
		this.gameSize = gameSize;
	}

	public Integer[][] getCells() {
		return cells;
	}

	public void setCells(Integer[][] cells) {
		this.cells = cells;
	}
	
}
