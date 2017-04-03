package com.intuit.interview.rest.dto;

public class StateDTO {
	private String playerIdOfNextTurn;
	private String playerIdOfWinner;
	
	public StateDTO () {}
	
	public StateDTO (String playerIdOfNextTurn, String playerIdOfWinner) {
		this.playerIdOfNextTurn = playerIdOfNextTurn;
		this.playerIdOfWinner = playerIdOfWinner;
	}
	
	public String getPlayerIdOfNextTurn() {
		return playerIdOfNextTurn;
	}
	
	public void setPlayerIdOfNextTurn(String playerIdOfNextTurn) {
		this.playerIdOfNextTurn = playerIdOfNextTurn;
	}

    public String getPlayerIdOfWinner() {
		return playerIdOfWinner;
	}
    
	public void setPlayerIdOfWinner(String playerIdOfWinner) {
		this.playerIdOfWinner = playerIdOfWinner;
	}


}
