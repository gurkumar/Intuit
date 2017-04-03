Tic Tac Toe game is played between 2 players where each player takes turn to mark a cell in a 2 dimensional table space. Each player is assigned an id and take turns to play. The player which gets 3 consecutive entries
in the table space either vertically, horizontal or diagonally wins.
The following APIs are used to play this game

The below set of APIs are for game where both the players will enter the move to be played
1) http://localhost:8080/game/tictactoe/start/${size} - This is POST API Call to initialize the game and return the game Id and the player ids.Each play and state API need to use the game id to execute.
${size} is the size of the 2 dimensional table.
2) http://localhost:8080/game/tictactoe/play/${gameId} - This is the POST API Call to make a move. 
This API will return error in the following scenarios
   a) if the player with wrong turn makes a move
   b) if the game has not been initialized
   c) if the cell that has already been played is sent by the player
The response of this API
   a) This the move played is valid
   b) if this move was the winning move    
3) http://localhost:8080/game/tictactoe/state/${gameId} gets the current state of the game
   a) player id of the winner if there is one
   b) player id of the player with next turn.
One additonal API is for the game between computer and player. Computer is assumed to make the second move with Id "O". 
4) http://localhost:8080/game/tictactoe/playwithcomputer/${gameId}

Please note the following
1) The init API gives the user to enter the game size. For the commonly played size of 3, a 3X3 matrix is created.
2) Player Ids are assigned by default and assumed that player 1 has id "X" and player 2 has id "O".

