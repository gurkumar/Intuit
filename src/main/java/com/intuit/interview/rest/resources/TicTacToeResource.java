package com.intuit.interview.rest.resources;

import javax.annotation.Resource;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.intuit.interview.rest.dto.GameDTO;
import com.intuit.interview.rest.dto.MoveDTO;
import com.intuit.interview.rest.dto.PlayerId;
import com.intuit.interview.rest.dto.StateDTO;
import com.intuit.interview.rest.dto.StatusDTO;
import com.intuit.interview.rest.services.api.TicTacToeService;

/**
 * {@link TicTacToeResource} is the entry point for all TicTacToe game endpoints exposed to the end players.
 * @author gkumar9
 *
 */
@Component
@Path("/game/tictactoe")
@Produces(MediaType.APPLICATION_JSON)
public class TicTacToeResource {

	private static Logger logger = LoggerFactory.getLogger(TicTacToeResource.class);

	@Resource
	private TicTacToeService ticTacToeService;

	/**
	 * {@link TicTacToeResource#startGame(Integer)} initializes the game with the game size
	 * @param size
	 * @return
	 * @throws Exception
	 */
	@POST
	@Path("/start/{size}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response startGame (@PathParam("size") Integer size) throws Exception{
		logger.info("initGame() is called:");
		GameDTO response = ticTacToeService.startGame(size);
		if (response != null) {
			return Response
					.ok(response)
					.build();
		} else {  
			throw new Exception ("The Tic Tac Toe game cannot be intialized");
		}     
	}

	/**
	 * {@link TicTacToeResource#getCurrentGameState(Integer) gets the current state of the game in terms of
	 * player id of the player with the next turn and player id of winner if the game has one.
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@GET
	@Path("/state/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCurrentGameState (@PathParam("id") Integer id) throws Exception{
		logger.info("getCurrentGameState() is called:");
		StateDTO response = ticTacToeService.getCurrentState(id);
		if (response != null) {
			return Response
					.ok(response)
					.build();
		} else {  
			throw new Exception ("the current state information of game with id " + id + " cannot be retrived");
		}    
		
	}

	/**
	 * {@link TicTacToeResource#playGame(Integer, MoveDTO) plays the current move of the player and
	 * returns whether the move is valid (the cell has not yet been played, game is still in play state)
	 * and if the move results in a win.
	 * @param id
	 * @param moveDTO
	 * @return
	 * @throws Exception
	 */
	@POST
	@Path("/play/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response playGame (@PathParam("id") Integer id, MoveDTO moveDTO) throws Exception{
		if (moveDTO != null) {
		StatusDTO response = ticTacToeService.playGame(id, moveDTO);
		return Response
				.ok(response)
				.build();
		}
		throw new Exception ("The request payload is not as expected");
	}

	@POST
	@Path("/playwithcomputer/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response playGameWithComputer (@PathParam("id") Integer id, MoveDTO moveDTO) throws Exception{
		if (moveDTO != null) {
		StatusDTO response = null;
		if (moveDTO.getPlayerId().equals(PlayerId.PLAYER_TWO_ID.getPlayerId())) {
		    if (moveDTO.getRowIndex() != null && moveDTO.getColumnIndex() != null)	
		        throw new Exception ("Computer will decide the move and not require move input");
			response = ticTacToeService.playGame(id,moveDTO.getPlayerId());
		} else {
			response = ticTacToeService.playGame(id, moveDTO);
		}	
		return Response
				.ok(response)
				.build();
		}
		throw new Exception ("The request payload is not as expected");
	}
}
