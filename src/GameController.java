import java.util.Arrays;
import java.util.List;

public class GameController {
    private GameView gameView;
    private GameState gameState;
    private MovieDatabase movieDatabase;
    private List<ConnectionStrategy> connectionStrategies;
    // private WinConditionStrategy winConditionStrategy;
    private GameTimer gameTimer;

    public GameController(GameState gameState, GameView gameView, MovieDatabase movieDatabase,
                          List<ConnectionStrategy> connectionStrategies) {
        this.gameState = gameState;
        this.gameView = gameView;
        this.movieDatabase = movieDatabase;
        this.connectionStrategies = connectionStrategies;
        // this.winConditionStrategy = winConditionStrategy;
        this.gameTimer = new GameTimer(30); // set the timer to 30 sec for each turn
    }

    // Start the game
    public void startGame() {
        // START GAME
        String[] playerNames = gameView.getPlayerNames(); // get names as an array

        // initialize players
        String player1Name = playerNames[0];
        String player2Name = playerNames[1];
        Player player1 = new Player(player1Name);
        Player player2 = new Player(player2Name);

        // store player info here
        List<Player> players = Arrays.asList(player1, player2);

        // Initialize the game state with players and a starting movie
        // now its hardcoded but we will need some randomization based on
        // winning strategy -- choose a movie that could allow both players' win strategy
        // some concerns I have are:
        // some movies in the database have (null) in this case some users might get stuck?

        // Initialising Winning Strategy for each Player
        WinConditionStrategy winConPlayer1 = gameView.getPlayersWinConditions(movieDatabase);
        WinConditionStrategy winConPlayer2 = gameView.getPlayersWinConditions(movieDatabase);
        
        // Add each win condition in GameState for each player
        gameState.setWinConditionPlayer(player1, winConPlayer1);
        gameState.setWinConditionPlayer(player2, winConPlayer2);


        Movie startingMovie = movieDatabase.getMovieByTitle("Die Hard");
        
        gameState.initializeGame(players, startingMovie);

        // show initial display game state
        gameView.displayGameState(gameState);


        // Start playing game
        while (!gameState.isGameOver()) {
            // continue to switch turn once the game start
            // until the game ends
            handleTurn();
        }

        // GAME ENDS
        gameView.showWinner(gameState.getWinner());
    }

    // Called each turn to handle what a player does
    public void handleTurn() {
        Player currentPlayer = gameState.getCurrentPlayer();


        gameTimer.start(() -> {
            gameView.showMessage("Time is up! switching to next player.");
            endTurnAndSwitchPlayer();
        }, secondsRemaining -> {
            /** implement timer visualization once we have Front End
             gameView.showTimer(secondsRemaining);
             */
        });

        // Prompt the player for one guess only
        String movieTitle = gameView.getPlayerInput("");
        Movie nextMovie = movieDatabase.getMovieByTitle(movieTitle);

        if (nextMovie == null) {
            gameView.showMessage("Movie not found. Turn skipped.");
            endTurnAndSwitchPlayer();  // End turn on invalid title
            return;
        }
        boolean isValidMove = false;

        // Check all connection types
        for (ConnectionStrategy connection : connectionStrategies) {
            if (connection.areConnected(gameState.getCurrentMovie(), nextMovie)) {
                isValidMove = true;
                gameState.updateState(nextMovie, connection.getType());
                currentPlayer.addMovie(nextMovie);
                break;
            }
        }

        if (isValidMove) {
            if (gameState.hasPlayerWon(currentPlayer)) {
                gameState.setGameOver(true);
                gameView.showWinner(currentPlayer);
            } else {
                endTurnAndSwitchPlayer();  // Valid move, but game continues
            }
        } else {
            gameView.showError("Invalid connection. Turn skipped.");
            endTurnAndSwitchPlayer();  // Invalid move ends the turn
        }
    }

    private void endTurnAndSwitchPlayer() {
        gameState.switchPlayer();
        gameState.incrementRound();
        gameView.displayGameState(gameState);
        handleTurn();
    }

}

