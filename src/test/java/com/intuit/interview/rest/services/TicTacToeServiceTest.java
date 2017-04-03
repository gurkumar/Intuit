package com.intuit.interview.rest.services;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.mockito.Mockito.doReturn;

import java.util.ArrayList;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.intuit.interview.core.biz.TicTacToe;
import com.intuit.interview.core.model.Game;
import com.intuit.interview.core.model.Player;
import com.intuit.interview.rest.dto.GameDTO;
import com.intuit.interview.rest.dto.PlayerId;
import com.intuit.interview.rest.dto.StateDTO;
import com.intuit.interview.rest.services.impl.TicTacToeServiceImpl;

public class TicTacToeServiceTest {

	@InjectMocks
	private TicTacToeServiceImpl ticTacToeService;

	@Mock
	private TicTacToe ticTacToe;

	@BeforeTest
	public void setup() {
	        MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testStartGame () {
		final int gameSize = 3;
		final Integer expectedGameId = 1;
		final String playerIdOne = PlayerId.PLAYER_ONE_ID.getPlayerId();
		final String playerIdTwo = PlayerId.PLAYER_TWO_ID.getPlayerId();
		List <Player> players = new ArrayList <> ();
		Player playerOne = new Player (playerIdOne);
		players.add(playerOne);
		Player playerTwo = new Player (playerIdTwo);
		players.add(playerTwo);
		Game game = new Game (1);
		game.setPlayers(players);
		doReturn (game).when (ticTacToe).initGame(gameSize);
		GameDTO gameDTO = ticTacToeService.startGame(gameSize);
		assertNotNull (gameDTO);
		assertEquals ( expectedGameId, gameDTO.getGameId());
		assertEquals (playerIdOne, gameDTO.getPlayerOneId());
		assertEquals (playerIdTwo, gameDTO.getPlayerTwoId());
	}
	
	@Test
	public void testGetCurrentStatePlayerIdOfWinner () throws Exception{
		final Integer expectedGameId = 1; 
		 Game game = new Game (1);
		 game.setState(Game.State.COMPLETE.name());
		 List <Player> players = new ArrayList <> ();
		 Player playerOne = new Player (PlayerId.PLAYER_ONE_ID.getPlayerId());
		 playerOne.setState(Player.State.WON.name());
		 players.add(playerOne);
		 Player playerTwo = new Player (PlayerId.PLAYER_TWO_ID.getPlayerId());
		 playerTwo.setState(Player.State.LOST.name());
		 players.add(playerTwo);
		 game.setPlayers(players);
		 doReturn (game).when (ticTacToe).getCurrentGameSate(expectedGameId);
		 StateDTO stateDTO  = ticTacToeService.getCurrentState(expectedGameId);		
		 assertNotNull (stateDTO);
		 assertEquals (PlayerId.PLAYER_ONE_ID.getPlayerId(), stateDTO.getPlayerIdOfWinner());
	}
	
	@Test
	public void testGetCurrentStatePlayerIdOfPlayerWithNextTurn () throws Exception{
		final Integer expectedGameId = 1; 
		 Game game = new Game (1);
		 game.setState(Game.State.PENDING.name());
		 List <Player> players = new ArrayList <> ();
		 Player playerOne = new Player (PlayerId.PLAYER_ONE_ID.getPlayerId());
		 playerOne.setTurn(true);
		 players.add(playerOne);
		 Player playerTwo = new Player (PlayerId.PLAYER_TWO_ID.getPlayerId());
		 playerTwo.setTurn(false);
		 players.add(playerTwo);
		 game.setPlayers(players);
		 doReturn (game).when (ticTacToe).getCurrentGameSate(expectedGameId);
		 StateDTO stateDTO  = ticTacToeService.getCurrentState(expectedGameId);		
		 assertNotNull (stateDTO);
		 assertEquals (PlayerId.PLAYER_ONE_ID.getPlayerId(), stateDTO.getPlayerIdOfNextTurn());
	}
	
}
