package com.intuit.interview.core.dao.rowmapper;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.doReturn;

import java.sql.Array;
import java.sql.ResultSet;

import org.testng.annotations.Test;

import com.intuit.interview.core.model.Game;

public class TicTacToeGameRowMapperTest {
	
	@Test
	public void testTicTacToeGameRowMapper () throws Exception{
		final Integer gameId = 1;
		final Integer [] rowData = {1,1,0};
		final Integer [] colData = {0,1,0};
		final Integer diagnolCol1 = 1;
		final Integer diagnolCol2 = 3;
		final String state = Game.State.COMPLETE.name();
		final Integer movesLeft = 9;
		final Integer gameSize = 3;
		final Integer[][] cellData = {{-1,0,-1},{0,0,-1},{-1,-1,0}};
		
		ResultSet resultSetMock = mock(ResultSet.class);
		doReturn (gameId).when (resultSetMock).getInt("game_id");
		Array rowDataMock = mock (Array.class);
		doReturn (rowDataMock).when(resultSetMock).getArray("row_data");
		doReturn (rowData).when (rowDataMock).getArray();
		Array colDataMock = mock (Array.class);
		doReturn (colDataMock).when(resultSetMock).getArray("col_data");
		doReturn (colData).when (colDataMock).getArray();
		doReturn (diagnolCol1).when (resultSetMock).getInt("diagnol_col_1");
		doReturn (diagnolCol2).when (resultSetMock).getInt("diagnol_col_2");
		doReturn (state).when (resultSetMock).getString("state");
		doReturn (movesLeft).when (resultSetMock).getInt("moves_left");
		doReturn (gameSize).when (resultSetMock).getInt("game_size");
		Array cellsMock = mock (Array.class);
		doReturn (cellsMock).when(resultSetMock).getArray("available_cell_data");
		doReturn (cellData).when(cellsMock).getArray();
		
		TicTacToeGameRowMapper ticTacToeGameRowMapper = new TicTacToeGameRowMapper ();
		Game game = ticTacToeGameRowMapper.mapRow(resultSetMock, 1);
		assertNotNull (game);
		assertEquals (gameId, game.getGameId());
		assertEquals (rowData, game.getRowData());
		assertEquals (colData, game.getColData());
		assertEquals (diagnolCol1, game.getDiagnolCol1());
		assertEquals (diagnolCol2, game.getDiagnolCol2());
		assertEquals (state, game.getState());
		assertEquals (movesLeft, game.getMovesLeft());
		assertEquals (gameSize, game.getGameSize());
		assertEquals (cellData, game.getCells());
	}

}
