package com.intuit.interview.rest.services.api;

import com.intuit.interview.rest.dto.GameDTO;
import com.intuit.interview.rest.dto.MoveDTO;
import com.intuit.interview.rest.dto.StateDTO;
import com.intuit.interview.rest.dto.StatusDTO;

public interface TicTacToeService {

	GameDTO startGame (Integer gameSize);
	
	StateDTO getCurrentState (Integer gameId) throws Exception;
	
	StatusDTO playGame (Integer gameId, MoveDTO moveDTO) throws Exception;
	
	StatusDTO playGame (Integer gameId, String playerId) throws Exception;
	
}
