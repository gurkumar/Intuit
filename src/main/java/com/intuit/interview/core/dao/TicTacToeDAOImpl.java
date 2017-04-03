package com.intuit.interview.core.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.intuit.interview.core.dao.rowmapper.TicTacToeGameRowMapper;
import com.intuit.interview.core.dao.rowmapper.TicTacToePlayerRowMapper;
import com.intuit.interview.core.model.Game;
import com.intuit.interview.core.model.Player;

@Repository
public class TicTacToeDAOImpl implements TicTacToeDAO{

	@Inject
	private JdbcTemplate jdbcTemplate;

    /**
     * {@link TicTacToeDAOImpl#insertInitialGameState(String, Integer[], Integer[], Integer, Integer[][])
     * inserts the initial game state.
     */
	@Override
	public Integer insertInitialGameState (String state, Integer[]rowData, Integer[]colData, Integer movesLeft, Integer[][] cells) {
		final String insertPlayerOneSql = "insert into tic_tac_toe_game (state,row_data,col_data,diagnol_col_1, diagnol_col_2,moves_left,game_size,available_cell_data) values(?,?,?,?,?,?,?,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
						PreparedStatement ps =
								connection.prepareStatement(insertPlayerOneSql, new String[] {"game_id"});
						ps.setString(1, state);
						ps.setObject(2, rowData);
						ps.setObject(3, colData);
						ps.setInt(4, 0);
						ps.setInt(5, 0);
						ps.setInt(6, movesLeft);
						ps.setInt(7, rowData.length);
						ps.setObject(8, cells);
						return ps;
					}
				},
				keyHolder);
		
		return keyHolder.getKey().intValue();
	}	
	
	/**
	 * {@link TicTacToeDAOImpl#insertInitialPlayerState(Integer, String, String, String) inserts the intial
	 * state and turn of the player along with the id information.
	 */
	@Override
	public void insertInitialPlayerState (Integer gameId, String playerIdOne, String playerIdTwo, String state) {
		final String insertPlayerTwoSql = "insert into tic_tac_toe_player (game_id,player_id,state,turn) values(?,?,?,?)";
		jdbcTemplate.update(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
						PreparedStatement ps =
								connection.prepareStatement(insertPlayerTwoSql);
						ps.setInt(1, gameId);
						ps.setString(2, playerIdOne);
						ps.setString(3, state);
						ps.setBoolean(4, true);
						return ps;
					}
				});
		jdbcTemplate.update(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
						PreparedStatement ps =
								connection.prepareStatement(insertPlayerTwoSql);
						ps.setInt(1, gameId);
						ps.setString(2, playerIdTwo);
						ps.setString(3, state);
						ps.setBoolean(4, false);
						return ps;
					}
				});
	}

	/**
	 * {@link TicTacToeDAOImpl#getGameData(Integer) gets the game and  player information.
	 */
	@Override
	public Game getGameData (Integer gameId) throws Exception{
		try {
		List<Player> players = new ArrayList<>();		
		String playerFindQuery = "SELECT * FROM tic_tac_toe_player WHERE game_id=?";
		Object[] params = new Object[1];
		params[0] = gameId; 
		players = this.jdbcTemplate.query(playerFindQuery, new TicTacToePlayerRowMapper(), params);
		String gameFindQuery = "SELECT * FROM tic_tac_toe_game WHERE game_id=?";
		params = new Object[1];
		params[0] = gameId; 
		Game game = this.jdbcTemplate.queryForObject (gameFindQuery, new TicTacToeGameRowMapper(), params);
		if (players != null && !players.isEmpty()) {
	        game.setPlayers(players);
		}  
		return game;
		} catch (Exception exception) {
			throw exception;
		}
	}

    /**
     * {@link TicTacToeDAOImpl#updateGameData(Integer, Integer[], Integer[], Integer, Integer, Integer[][])
     * updates the game data in terms of the moves played by the players.
     */
	@Override
	public void updateGameData (Integer gameId, Integer[] rows, Integer[] cols, Integer diagnolCol1, Integer diagnolCol2, Integer[][] cells) {
		String updateSql = "UPDATE tic_tac_toe_game SET row_data = ?, col_data = ?, diagnol_col_1 = ?, diagnol_col_2 = ?, available_cell_data = ? WHERE game_id = ?";
		jdbcTemplate.update(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
						PreparedStatement ps =
								connection.prepareStatement(updateSql);
						ps.setObject(1, rows);
						ps.setObject(2, cols);
						ps.setInt(3, diagnolCol1);
						ps.setInt(4, diagnolCol2);
						ps.setObject(5, cells);
						ps.setInt(6, gameId);
						return ps;
					}
				});
	}
	
	/**
	 * {@link TicTacToeDAOImpl#updateMovesLeftInGame(Integer, Integer) updates the number of moves left
	 * in the game based on the game size.
	 */
	@Override
	public void updateMovesLeftInGame (Integer gameId, Integer movesLeft) {
		String updateSql = "UPDATE tic_tac_toe_game set moves_left = ? WHERE game_id = ?";
		jdbcTemplate.update(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
						PreparedStatement ps =
								connection.prepareStatement(updateSql);
						ps.setInt(1, movesLeft);
						ps.setInt(2, gameId);
						return ps;
					}
				});
	}

	/**
	 * {@link TicTacToeDAOImpl#updatePlayerTurn(Integer, String, boolean)} updates the turn of the player to
	 * eithe true or false based on the current game standing.
	 */
	@Override
	public void updatePlayerTurn (Integer gameId, String playerId, boolean turn) {
		String updateSql = "UPDATE tic_tac_toe_player set turn = ? WHERE game_id = ? AND player_id=?";
		jdbcTemplate.update(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
						PreparedStatement ps =
								connection.prepareStatement(updateSql);
						ps.setBoolean(1, turn);
						ps.setInt(2, gameId);
						ps.setString(3, playerId);
						return ps;
					}
				});
		
	}
	
	/**
	 * {@link TicTacToeDAOImpl#updateGameState(Integer, String) updates the state of the game to either
	 * pending or complete state.
	 */
	@Override
	public void updateGameState (Integer gameId,String state) {
		String updateSql = "UPDATE tic_tac_toe_game set state = ? WHERE game_id = ?";
		jdbcTemplate.update(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
						PreparedStatement ps =
								connection.prepareStatement(updateSql);
						ps.setString(1, state);
						ps.setInt(2, gameId);
						return ps;
					}
				});
	}

	/**
	 * {@link TicTacToeDAOImpl#updatePlayerState(Integer, String, String) updates the state of the 
	 * player to WON,LOST,DRWA or draw depending on the outcome of the game.
	 */
	@Override
	public void updatePlayerState (Integer gameId, String playerId, String state) {
		String updateSql = "UPDATE tic_tac_toe_player set state = ? WHERE game_id = ? AND player_id=?";
		jdbcTemplate.update(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
						PreparedStatement ps =
								connection.prepareStatement(updateSql);
						ps.setString(1, state);
						ps.setInt(2, gameId);
						ps.setString(3, playerId);
						return ps;
					}
				});
	}
	
	/**
	 * {@link TicTacToeDAOImpl#getGameSize(Integer) gets the size of the game given the game id.
	 */
	@Override
	public Integer getGameSize (Integer gameId) throws Exception{
		String gameFindQuery = "SELECT game_size FROM tic_tac_toe_game WHERE game_id=?";
		Object[] params =  new Object[1];
		params[0] = gameId; 
		try {
	    Integer size = this.jdbcTemplate.queryForObject (gameFindQuery, params, Integer.class);
	    return size;
		}catch (Exception exception) {
			throw new Exception ("The game has not been intialized properly");
		}
	}	
}
