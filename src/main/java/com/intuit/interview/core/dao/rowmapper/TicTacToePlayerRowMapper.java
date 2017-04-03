package com.intuit.interview.core.dao.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.intuit.interview.core.model.Player;

public class TicTacToePlayerRowMapper implements RowMapper<Player>{

	public Player mapRow(ResultSet rs, int rowNum) throws SQLException {
		String playerId = rs.getString("player_id");
		String state = rs.getString("state");
		boolean turn = rs.getBoolean("turn");
		
		Player player = new Player (playerId);
		player.setState(state);
		player.setTurn(turn);
		return player;
	}

}
