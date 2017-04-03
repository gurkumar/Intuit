package com.intuit.interview.core.dao.rowmapper;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.intuit.interview.core.model.Game;

public class TicTacToeGameRowMapper implements RowMapper<Game>{

	public Game mapRow(ResultSet rs, int rowNum) throws SQLException {
		Integer gameId = rs.getInt("game_id");
		Array rowData = rs.getArray("row_data");
		Integer [] rows = (Integer[])rowData.getArray();
		Array colData = rs.getArray("col_data");
		Integer [] cols = (Integer[])colData.getArray();
		Integer diagnolCol1 = rs.getInt("diagnol_col_1");
		Integer diagnolCol2 = rs.getInt("diagnol_col_2");
		String state = rs.getString("state");
		Integer movesLeft = rs.getInt("moves_left");
		Integer gameSize = rs.getInt("game_size");
		Array cells = rs.getArray("available_cell_data");
	    Integer[][] cellData = (Integer[][])cells.getArray();
		Game game = new Game (gameId);
		game.setRowData(rows);
		game.setColData(cols);
		game.setDiagnolCol1(diagnolCol1);
		game.setDiagnolCol2(diagnolCol2);
		game.setState(state);
		game.setMovesLeft(movesLeft);
		game.setGameSize(gameSize);
		game.setCells(cellData);
		return game;
	}

}
