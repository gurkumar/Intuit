package com.intuit.interview.core.dao;

import com.intuit.interview.core.model.Game;


public interface TicTacToeDAO {
	
	Integer insertInitialGameState (String state,Integer[]rowData, Integer[]colData, Integer movesLeft, Integer[][] cells);
	
	void insertInitialPlayerState (Integer gameId, String playerIdOne, String playerIdTwo, String state);
	
    Game getGameData (Integer gameId) throws Exception;
    
	void updateGameData (Integer gameId, Integer[] rows, Integer[] cols, Integer diagnolCol1, Integer diagnolCol2, Integer[][] cells);

    void updatePlayerTurn (Integer gameId, String playerId, boolean turn);
    
    void updateMovesLeftInGame (Integer gameId, Integer movesLeft);
    
    void updateGameState (Integer gameId, String state);
    
    void updatePlayerState (Integer gameId, String playerId, String state);
    
    Integer getGameSize (Integer gameId) throws Exception;
    
}