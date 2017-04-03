package com.intuit.interview.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GameDTO {
	@JsonProperty("gameId")
	private Integer gameId;
	@JsonProperty("playerOneId")
	private String playerOneId;
	@JsonProperty("playerTwoId")
	private String playerTwoId;
	public Integer getGameId() {
		return gameId;
	}
	public void setGameId(Integer gameId) {
		this.gameId = gameId;
	}
	public String getPlayerOneId() {
		return playerOneId;
	}
	public void setPlayerOneId(String playerOneId) {
		this.playerOneId = playerOneId;
	}
	public String getPlayerTwoId() {
		return playerTwoId;
	}
	public void setPlayerTwoId(String playerTwoId) {
		this.playerTwoId = playerTwoId;
	}
	
	
}
