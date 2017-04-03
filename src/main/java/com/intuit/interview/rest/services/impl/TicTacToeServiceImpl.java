package com.intuit.interview.rest.services.impl;


import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.intuit.interview.core.biz.TicTacToe;
import com.intuit.interview.core.model.Game;
import com.intuit.interview.core.model.Player;
import com.intuit.interview.rest.dto.GameDTO;
import com.intuit.interview.rest.dto.MoveDTO;
import com.intuit.interview.rest.dto.PlayerId;
import com.intuit.interview.rest.dto.StateDTO;
import com.intuit.interview.rest.dto.StatusDTO;
import com.intuit.interview.rest.services.api.TicTacToeService;

/**
 * {@link TicTacToeServiceImpl} has service methods consumed by the REST end point resource. Service layer
 * calls into the core business layer for the business logic.
 * @author gkumar9
 *
 */
@Service
public class TicTacToeServiceImpl implements TicTacToeService {

	@Inject
	TicTacToe ticTacToe;
 
	/**
	 * {@link TicTacToeServiceImpl#startGame(Integer) intialized the game.
	 */
	@Override
	public GameDTO startGame (Integer gameSize) {
		 Game game = ticTacToe.initGame (gameSize);
		 if (game != null)
		    return convertFromGameToGameDTO (game);
		 return null;
	}

	/**
	 * {@link TicTacToeServiceImpl#getCurrentState(Integer) gets the current game state and based off that
	 * determines the player id of the next move and player id of the winner.
	 */
	@Override
	public StateDTO getCurrentState(Integer gameId) throws Exception{
	 Game game = ticTacToe.getCurrentGameSate(gameId);
	 if (game != null) {
		StateDTO stateDTO = new StateDTO ();
		if (game.getState().equals(Game.State.COMPLETE.name())) {
			String playerIdOfWinner = null;
			if (game.getPlayers() != null && !game.getPlayers().isEmpty()) {
		 	    for (Player player : game.getPlayers()) {
					if (player.getState().equals(Player.State.WON.name())) { 
					    playerIdOfWinner = player.getId();
			        } else if (player.getState().equals(Player.State.DRAW.name())) {
			        	if (playerIdOfWinner != null)
			        	  playerIdOfWinner = playerIdOfWinner + player.getId();
			        	else
			        		playerIdOfWinner =  player.getId() + ",";
			        }
				} 
			}	
			stateDTO.setPlayerIdOfWinner(playerIdOfWinner);
			
		} else {
			if (game.getPlayers() != null && !game.getPlayers().isEmpty()) {
			    for (Player player : game.getPlayers()) {
			    	if (player.isTurn()) {
			    	  stateDTO.setPlayerIdOfNextTurn(player.getId());
			    	  break;
			    	}  
			    }
			}	
		
		}
		return stateDTO;
	  } else throw new Exception ("The game with id " + gameId + " is not start up properly");
	}
	
	/**
	 * {@link TicTacToeServiceImpl#playGame(Integer, MoveDTO) plays the move and determines the status of the
	 * game.
	 */
	@Override
	public StatusDTO playGame (Integer gameId, MoveDTO moveDTO) throws Exception{
		Integer rowIndex =  moveDTO.getRowIndex();
		Integer columnIndex = moveDTO.getColumnIndex();
		if (rowIndex <0 || columnIndex <0)
			throw new Exception ("row or column index is not valid");
		Integer size = ticTacToe.getGameSize(gameId);
		if (rowIndex >= size || columnIndex >=size) {
			throw new Exception ("row or column index is not valid");
		}
		boolean isWon = ticTacToe.playGame(gameId, moveDTO.getPlayerId(), moveDTO.getRowIndex(), moveDTO.getColumnIndex());
		StatusDTO statusDTO = new StatusDTO ();
		statusDTO.setValidMove(true);
		statusDTO.setWon(isWon);
		return statusDTO;
	}
	
	@Override
	public StatusDTO playGame (Integer gameId, String playerId) throws Exception{
		boolean isWon = ticTacToe.playGame(gameId, playerId);
		StatusDTO statusDTO = new StatusDTO ();
		statusDTO.setValidMove(true);
		statusDTO.setWon(isWon);
		return statusDTO;
	}
	
	private GameDTO convertFromGameToGameDTO (Game game) {
		GameDTO gameDTO = new GameDTO ();
		gameDTO.setGameId(game.getGameId());
		for (Player player: game.getPlayers()) {
			if (player.getId().equals(PlayerId.PLAYER_ONE_ID.getPlayerId())) {
				gameDTO.setPlayerOneId(player.getId());
			} else if (player.getId().equals(PlayerId.PLAYER_TWO_ID.getPlayerId())) {
				gameDTO.setPlayerTwoId(player.getId());
			}
		}
		return gameDTO;
	}
}
