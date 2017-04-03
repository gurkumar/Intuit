package com.intuit.interview.rest.dto;

public enum PlayerId {
		PLAYER_ONE_ID("X",1),
		PLAYER_TWO_ID ("O",-1);
		
	private final String playerId;
	private final Integer playerValue;

	private PlayerId (String playerId, Integer playerValue) {
		this.playerId = playerId;
		this.playerValue = playerValue;
	}

	public String getPlayerId () {
		return this.playerId;
	}

	public Integer getPlayerValue () {
		return this.playerValue;
	}
	
	public static PlayerId getPlayerIdEnum (String playerId) {
		for (PlayerId playerIdEnum : values()) {
			if (playerIdEnum.getPlayerId().equals(playerId))
				return playerIdEnum;
		}
		throw new IllegalArgumentException ("PlayerId " + playerId + " not supported");
	}
	
}
	

