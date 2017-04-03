package com.intuit.interview.core.biz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.intuit.interview.core.dao.TicTacToeDAO;
import com.intuit.interview.core.model.Cell;
import com.intuit.interview.core.model.Game;
import com.intuit.interview.core.model.Player;
import com.intuit.interview.rest.dto.PlayerId;
/**
 * {@link TicTacToe} is the core business logic layer which is the single point of entry into the DAO.
 * @author gkumar9
 *
 */
@Component
public class TicTacToe {

	@Inject
	TicTacToeDAO ticTacToeDAO;

	/**
	 * {@link TicTacToe#initGame(Integer)} calls the persistence layer to setup intial game size and 
	 * setup default game and player states and return the game id.
	 * @param gameSize
	 * @return
	 */
	public Game initGame(Integer gameSize) {
		Integer[] rowData = new Integer [gameSize];
		Arrays.fill(rowData, 0);
		Integer[] colData = new Integer [gameSize];
		Arrays.fill(colData, 0);
		Integer[][] cells = new Integer [gameSize][gameSize];
		for (int i=0;i<gameSize;i++) {
			for (int j=0;j<gameSize;j++) {
				cells[i][j] = -1;
			}
		}
		Integer gameId = ticTacToeDAO.insertInitialGameState(Game.State.PENDING.name(), rowData, colData, gameSize*gameSize, cells);
		ticTacToeDAO.insertInitialPlayerState (gameId, PlayerId.PLAYER_ONE_ID.getPlayerId(),
				PlayerId.PLAYER_TWO_ID.getPlayerId(), Player.State.PLAYING.name());
		List <Player> players = new ArrayList <> ();
		players.add(new Player(PlayerId.PLAYER_ONE_ID.getPlayerId()));
		players.add(new Player(PlayerId.PLAYER_TWO_ID.getPlayerId()));
		return gameId != null ? new Game(gameId, players) : null;
	}

	/**
	 * {@link TicTacToe#getCurrentGameSate(Integer)} gets the game and player data to be returned for 
	 * determination of the next move player id and winner player id.
	 * @param gameId
	 * @return
	 */
	public  Game  getCurrentGameSate (Integer gameId) throws Exception{
		try {
			return ticTacToeDAO.getGameData(gameId);
		} catch (Exception exception) {
			throw new Exception ("The game has not been initialized");
		}
	}

	/**
	 * {@link TicTacToe#playGame(Integer, String) plays the next move of the game.
	 * @param gameId
	 * @param playerId
	 * @throws Exception
	 */
	public boolean playGame (Integer gameId, String playerId)throws Exception{
		Game game = ticTacToeDAO.getGameData(gameId);
		if (game.getCells().length == 0 )
			throw new Exception (" no cells available to play");
		List<Cell>availableCells = getAvailableCells (game.getCells());
		List <Move> moves = new ArrayList <> ();
		Integer diagnolCol1 = game.getDiagnolCol1 ();
		Integer diagnolCol2 = game.getDiagnolCol2();
		Integer[] rows = game.getRowData();
		Integer[] cols = game.getColData();
		for (Cell cell : availableCells) {
			int rowIndex= cell.getRowIndex();
			int colIndex =cell.getColIndex();
			List<Cell>reducedAvailableCells = new ArrayList<>();
			reducedAvailableCells.addAll(availableCells);
			reducedAvailableCells.remove(cell);
			Integer diagnolCol1Val = diagnolCol1;
			Integer diagnolCol2Val = diagnolCol2;
			Integer[] rowsVal = new Integer [rows.length];
			Integer[] colsVal = new Integer [rows.length];
			for (int i=0;i<rowsVal.length;i++)
				rowsVal[i] = rows[i];
			for (int i=0;i<colsVal.length;i++)
				colsVal[i] = cols[i];
			Move move = minMax (playerId, rowsVal,colsVal,rowIndex,
					colIndex,diagnolCol1Val, diagnolCol2Val, reducedAvailableCells);
			if (move.getColIndex() == null || move.getRowIndex() == null) {
				move.setColIndex(colIndex);
				move.setRowIndex(rowIndex);
			}

			moves.add(move);
		}    
		if (moves != null && !moves.isEmpty()) {
			Move finalMove = moves.get(0);
			for (int i=0;i<moves.size();i++) {
				if (finalMove.getScore() < moves.get(i).getScore())
					finalMove = moves.get(i);
			}
			return playGame (game, playerId, finalMove.getRowIndex(), finalMove.getColIndex());
		} else
			throw new Exception ("proper move cannot be determined");

	}

	public boolean playGame (Integer gameId, String playerId, int rowIndex, int colIndex) throws Exception{
		Game game = ticTacToeDAO.getGameData(gameId);
		return playGame (game, playerId, rowIndex, colIndex);
	}

	private boolean playGame (Game game, String playerId, int rowIndex, int colIndex) throws Exception {
		if (game !=null && game.getPlayers() != null) {
			if (game.getState().equals(Game.State.COMPLETE.name()))
				throw new Exception ("The game is over");
			Player playingPlayer = null;
			Player nonPlayingPlayer = null;
			for (Player player : game.getPlayers() ) {
				if (player.getId().equals(playerId)) {
					playingPlayer = player;
				} else {
					nonPlayingPlayer = player;
				}
			}
			if (playingPlayer != null) {
				if (!playingPlayer.isTurn() || game.getMovesLeft() == 0) {
					throw new Exception ("The player with id " + playerId + " does not have the next turn in game with id  " + game.getGameId());
				} else {
					int val = PlayerId.getPlayerIdEnum(playerId).getPlayerValue();
					Integer[] rows = game.getRowData();
					Integer[] cols = game.getColData();
					Integer diagnolCol1 = game.getDiagnolCol1();
					Integer diagnolCol2 = game.getDiagnolCol2();
					boolean isCellAvailableToPlay = false;
					Integer [][] availableCells = game.getCells();
					if (availableCells[rowIndex][colIndex] == -1)
						isCellAvailableToPlay = true;

					if (!isCellAvailableToPlay)
						throw new Exception ("This cell has already been played. Please choose a different one");

					rows[rowIndex]+=val;
					cols[colIndex]+=val;
					if (rowIndex == colIndex) {
						diagnolCol1+=val;
					}
					if(colIndex==rows.length-rowIndex-1) {
						diagnolCol2+=val;
					}
					if (Math.abs (rows[rowIndex]) ==rows.length
							|| Math.abs(cols[colIndex]) == cols.length
							|| Math.abs(diagnolCol1) == rows.length
							|| Math.abs(diagnolCol2) == cols.length) {
						//player won the game.
						ticTacToeDAO.updatePlayerState(game.getGameId(), playerId, Player.State.WON.name());
						ticTacToeDAO.updatePlayerState(game.getGameId(), nonPlayingPlayer.getId(), Player.State.LOST.name());
						ticTacToeDAO.updateGameState(game.getGameId(), Game.State.COMPLETE.name());
						return true;
					}else {
						/**DETERMINE DRAW**/
						int movesLeft = game.getMovesLeft().intValue();
						if (--movesLeft == 0) {
							ticTacToeDAO.updatePlayerState(game.getGameId(), playerId, Player.State.DRAW.name());
							ticTacToeDAO.updatePlayerState(game.getGameId(), nonPlayingPlayer.getId(), Player.State.DRAW.name());
							ticTacToeDAO.updateGameState(game.getGameId(), Game.State.COMPLETE.name());
						} else {
							ticTacToeDAO.updatePlayerTurn (game.getGameId(), playerId,false);
							ticTacToeDAO.updatePlayerTurn (game.getGameId(), nonPlayingPlayer.getId(), true);
							ticTacToeDAO.updateMovesLeftInGame(game.getGameId(), movesLeft);
							availableCells [rowIndex][colIndex] = 0;
							ticTacToeDAO.updateGameData(game.getGameId(), rows, cols, diagnolCol1, diagnolCol2, availableCells);
						}  
						return false;
					}
				}
			} else {
				throw new Exception ("player with id " + playerId + " is not associated with the game with id " +game.getGameId());
			}
		}
		return false;
	}

	/**
	 * {@link TicTacToe#getGameSize(Integer) gets the size of the game given the game id.
	 * @param gameId
	 * @return
	 * @throws Exception
	 */
	public Integer getGameSize (Integer gameId) throws Exception{
		return ticTacToeDAO.getGameSize(gameId);
	}

	/**
	 * {@link TicTacToe#minMax(String, Integer[], Integer[], int, int, int, int, List) implements the 
	 * min max algorithm which works by constructing a game tree for the remaining open cells.
	 * for each open cell, all possible moves are constructed by players taking turns and a score is
	 * calculated and passed to the parent. out of all the children scores, the highest score move
	 * is played by the computer.
	 * Lets say that there are 4 open cells left and it is computers (player id=O) turn, than there will be
	 * 4 children as the player O can occupy any one of the 4 spots. Once the move is made by computer,
	 * the remaining cells are analyzed by player X. if in any of the remaining cells, the move by 
	 * player X wins the game, that move has the score of -10. Similarly if move by player O (computer)
	 * wins the game, that move  has score of +10 and if the game ends in draw after consecutive turns and 
	 * no playing cells left that move has a score of 0. The parent subtree decides based on the returned
	 * scores by children whether it picks up max score or min score based on the player's turn. if it is
	 * player X turn min score is picked to minimize the damage and if it is player O's turn max score is picked
	 * to maximize the probability of winning.
	 * @param playerId
	 * @param rows
	 * @param cols
	 * @param rowIndex
	 * @param colIndex
	 * @param diagnolCol1
	 * @param diagnolCol2
	 * @param availableCells
	 * @return
	 */
	public Move minMax (String playerId, Integer[] rows, Integer[] cols, int rowIndex, int colIndex, int diagnolCol1,
			int diagnolCol2, List<Cell>availableCells) {
		int val = PlayerId.getPlayerIdEnum(playerId).getPlayerValue();
		rows[rowIndex]+=val;
		cols[colIndex]+=val;
		if (rowIndex == colIndex) {
			diagnolCol1+=val;
		}
		if(colIndex==rows.length-rowIndex-1) {
			diagnolCol2+=val;
		}
		if (Math.abs (rows[rowIndex]) ==rows.length
				|| Math.abs(cols[colIndex]) == cols.length
				|| Math.abs(diagnolCol1) == rows.length
				|| Math.abs(diagnolCol2) == cols.length)  {
			//base condition if the player wins
			Move move = new Move ();
			if (playerId.equals(PlayerId.PLAYER_TWO_ID.getPlayerId())) {
				move.setScore(10);
			} else  {
				move.setScore(-10);
			}	
			return move;
		} else {
			if (availableCells.isEmpty()) {
				//draw
				Move move = new Move ();
				move.setScore(0);
				return move;
			}
			String nextPlayerId = playerId.equalsIgnoreCase(PlayerId.PLAYER_ONE_ID.getPlayerId()) ? PlayerId.PLAYER_TWO_ID.getPlayerId()
					:PlayerId.PLAYER_ONE_ID.getPlayerId();
			List <Move> moves = new ArrayList <>();
			for (Cell cell : availableCells) {
				List<Cell>reducedAvailableCells = new ArrayList<>();
				reducedAvailableCells.addAll(availableCells);
				reducedAvailableCells.remove(cell);
				Integer diagnolCol1Val = diagnolCol1;
				Integer diagnolCol2Val = diagnolCol2;
				Integer[] rowsVal = new Integer [rows.length];
				Integer[] colsVal = new Integer [rows.length];
				for (int i=0;i<rowsVal.length;i++)
					rowsVal[i] = rows[i];
				for (int i=0;i<colsVal.length;i++)
					colsVal[i] = cols[i];
				Move move = minMax (nextPlayerId, rowsVal, colsVal, cell.getRowIndex(), cell.getColIndex(), diagnolCol1Val,diagnolCol2Val,
						reducedAvailableCells);
				move.setColIndex(colIndex);
				move.setRowIndex(rowIndex);
				moves.add(move);

			}   
			if (nextPlayerId.equals(PlayerId.PLAYER_TWO_ID.getPlayerId())) {
				//choose the max score move
				Move maxScoreMove = moves.get(0);
				for (int i=1;i<moves.size();i++) {
					if (maxScoreMove.getScore() < moves.get(i).getScore()) {
						maxScoreMove = moves.get(i);
					}
				}
				return maxScoreMove;
			} else {
				//choose the min score move
				Move minScoreMove = moves.get(0);
				for (int i=1;i<moves.size();i++) {
					if (minScoreMove.getScore() > moves.get(i).getScore()) {
						minScoreMove = moves.get(i);
					}
				}
				return minScoreMove;
			}
		}	

	}


	private static class Move {
		private Integer rowIndex;
		private Integer colIndex;
		private Integer score;
		public Integer getRowIndex() {
			return rowIndex;
		}
		public void setRowIndex(Integer rowIndex) {
			this.rowIndex = rowIndex;
		}
		public Integer getColIndex() {
			return colIndex;
		}
		public void setColIndex(Integer colIndex) {
			this.colIndex = colIndex;
		}
		public Integer getScore() {
			return score;
		}
		public void setScore(Integer score) {
			this.score = score;
		}
	}

	private  List<Cell> getAvailableCells (Integer[][]cells) {
		List<Cell> availableCells = new ArrayList <> ();
		for (int i=0 ;i<cells.length;i++) {
			for (int j=0;j<cells.length;j++) {
				if (cells[i][j] == -1)
					availableCells.add(new Cell (i,j));
			}
		}
		return availableCells;
	}

}
