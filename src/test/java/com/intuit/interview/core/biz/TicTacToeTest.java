package com.intuit.interview.core.biz;

import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Matchers.anyObject;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.intuit.interview.core.dao.TicTacToeDAOImpl;
import com.intuit.interview.core.model.Game;
import com.intuit.interview.core.model.Player;
import com.intuit.interview.rest.dto.PlayerId;

public class TicTacToeTest {

	@InjectMocks
	private TicTacToe ticTacToe;

	@Mock
	private TicTacToeDAOImpl ticTacToeDAO;

	@BeforeTest
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testInitGame () {
		final int gameSize = 3;
		final Integer expectedGameId = 1;
		doReturn(1).when(ticTacToeDAO).insertInitialGameState(anyObject(), anyObject(), anyObject(), anyObject(), anyObject());
		Game game = ticTacToe.initGame(gameSize);
		assertNotNull (game);
		assertEquals (expectedGameId, game.getGameId());
	}

	@Test
	public void testGetGameState () throws Exception{
		Game game = new Game (1);
		doReturn (game).when(ticTacToeDAO).getGameData(1);
		Game actualGame = ticTacToe.getCurrentGameSate(1);
		assertNotNull (actualGame);
		final Integer expectedGameId = 1;
		assertEquals (expectedGameId, actualGame.getGameId());		
	}

	@Test
	public void testPlayGame ()throws Exception{
		Game game = new Game (1);
		game.setState(Game.State.PENDING.name());
		game.setMovesLeft(4);
		List <Player> players = new ArrayList <> ();
		Player playerOne = new Player (PlayerId.PLAYER_ONE_ID.getPlayerId());
		playerOne.setTurn(false);
		players.add(playerOne);
		Player playerTwo = new Player (PlayerId.PLAYER_TWO_ID.getPlayerId());
		playerTwo.setTurn(true);
		players.add(playerTwo);
		game.setPlayers(players);
		Integer[][]cells = {{0,0,0},{-1,0,-1},{-1,-1,0}};
		game.setCells(cells);
		Integer[] rowData = new Integer [3];
		Arrays.fill(rowData, 0);
		Integer[] colData = new Integer [3];
		Arrays.fill(colData, 0);
		game.setRowData(rowData);
		game.setColData(colData);
		game.setDiagnolCol1(0);
		game.setDiagnolCol2(0);
		doReturn (game).when(ticTacToeDAO).getGameData(1);
		boolean isWinningMove = ticTacToe.playGame(1, PlayerId.PLAYER_TWO_ID.getPlayerId());
		assertFalse (isWinningMove);
	}		
}
