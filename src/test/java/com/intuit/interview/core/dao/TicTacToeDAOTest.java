package com.intuit.interview.core.dao;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.intuit.interview.core.dao.rowmapper.TicTacToeGameRowMapper;
import com.intuit.interview.core.dao.rowmapper.TicTacToePlayerRowMapper;
import com.intuit.interview.core.model.Game;
import com.intuit.interview.core.model.Player;
import com.intuit.interview.rest.dto.PlayerId;

public class TicTacToeDAOTest {

	@InjectMocks
	private TicTacToeDAOImpl ticTacToeDAOImpl;

	@Mock
	private JdbcTemplate jdbcTemplate;


	@BeforeTest
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetGameData () throws Exception{
		List <Player> players = new ArrayList <> ();
		Player playerOne = new Player (PlayerId.PLAYER_ONE_ID.getPlayerId());
		players.add(playerOne);
		Player playerTwo = new Player (PlayerId.PLAYER_TWO_ID.getPlayerId());
		players.add(playerTwo);
		doReturn (players).when(jdbcTemplate).query(anyString(), any(TicTacToePlayerRowMapper.class), anyObject());
        Game game = new Game (1);
		doReturn (game).when(jdbcTemplate).queryForObject (anyString(),any(TicTacToeGameRowMapper.class), anyObject());
		Game actualGameData =ticTacToeDAOImpl.getGameData (1);
		assertNotNull (actualGameData);
		final Integer expectedGameId = 1;
		final String expectedPlayerOneId = PlayerId.PLAYER_ONE_ID.getPlayerId();
		final String expectedPlayerTwoId = PlayerId.PLAYER_TWO_ID.getPlayerId();
		assertEquals (expectedGameId, actualGameData.getGameId());
		assertEquals (expectedPlayerOneId, actualGameData.getPlayers().get(0).getId());
		assertEquals (expectedPlayerTwoId, actualGameData.getPlayers().get(1).getId());
	}
}
