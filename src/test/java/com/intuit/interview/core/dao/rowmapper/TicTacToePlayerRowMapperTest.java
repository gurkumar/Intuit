package com.intuit.interview.core.dao.rowmapper;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

import java.sql.ResultSet;

import org.testng.annotations.Test;

import com.intuit.interview.core.model.Player;
import com.intuit.interview.rest.dto.PlayerId;

public class TicTacToePlayerRowMapperTest {
	
	@Test
	public void testTicTacToePlayerRowMapper () throws Exception{
		ResultSet resultSetMock = mock(ResultSet.class);
		final String playerId = PlayerId.PLAYER_TWO_ID.getPlayerId();
		String state = Player.State.WON.name();
		boolean turn = false;
		
	    doReturn (playerId).when (resultSetMock).getString("player_id");
	    doReturn (state).when (resultSetMock).getString("state");
	    doReturn (turn).when (resultSetMock).getBoolean("turn");
	    
		TicTacToePlayerRowMapper ticTacToePlayerRowMapper = new TicTacToePlayerRowMapper ();
		Player player = ticTacToePlayerRowMapper.mapRow(resultSetMock, 1);
		assertNotNull (player);
		assertEquals (playerId, player.getId());
		assertEquals (state, player.getState());
		assertEquals (turn, player.isTurn());
	}

}
