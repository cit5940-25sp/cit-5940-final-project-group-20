import java.util.Arrays;
import java.util.List;

public class GameController {
    private GameView gameView;
    private GameState gameState;
    private MovieDatabase movieDatabase;
    private List<ConnectionStrategy> connectionStrategies;
    private WinConditionStrategy winConditionStrategy;
    private GameTimer gameTimer;

    public GameController(GameState gameState, GameView gameView, MovieDatabase movieDatabase,
                          List<ConnectionStrategy> connectionStrategies, WinConditionStrategy winConditionStrategy) {
        this.gameState = gameState;
        this.gameView = gameView;
        this.movieDatabase = movieDatabase;
        this.connectionStrategies = connectionStrategies;
        this.winConditionStrategy = winConditionStrategy;
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
        Movie startingMovie = movieDatabase.getMovieByTitle("Die Hard");
        gameState.initializeGame(players, startingMovie);

        // PLAYING GAME
        while (!gameState.isGameOver()) {
            handleTurn();
        }

        // GAME ENDS
        gameView.showWinner(gameState.getWinner());
    }

    // Called each turn to handle what a player does
    public void handleTurn() {
        Player currentPlayer = gameState.getCurrentPlayer();
        gameView.displayGameState(gameState); // display current game state


        gameTimer.start(() -> {
            gameView.showError("Time is up! switching to next player.");
            switchPlayer(); // switch player if the time is up
        }, secondsRemaining -> {
            /** implement timer visualization once we have Front End
            gameView.showTimer(secondsRemaining);
             */
        });


        // allow player to make multiple guesses within the time limit
        boolean isValidMove = false;

        // Show turn-start message via GameView
        gameView.showTurnStartMessage();

        while (!gameTimer.isTimeUp() && !isValidMove) {
            String movieTitle = gameView.getPlayerInput("\nEnter movie title: ");
            Movie nextMovie = movieDatabase.getMovieByTitle(movieTitle);

            if (nextMovie == null) {
                gameView.showError("Movie not found. Please try again.");
                continue; // allow retry within timer
            }

            // Check all strategies
            for (ConnectionStrategy connection : connectionStrategies) {
                if (connection.areConnected(gameState.getCurrentMovie(), nextMovie)) {
                    isValidMove = true;
                    gameState.updateState(nextMovie, connection.getType());
                    currentPlayer.addMovie(nextMovie);
                    break;
                }
            }

            if (!isValidMove) {
                gameView.showError("Invalid connection. Try again.");
            }
        }

        if (isValidMove) {
            if (currentPlayer.hasWon(winConditionStrategy)) {
                gameState.setGameOver(true);
                gameView.showWinner(currentPlayer);
            } else {
                switchPlayer();
            }
        }
    }

    // Switch to the next player
    public void switchPlayer() {
        gameState.switchPlayer();
    }
}

