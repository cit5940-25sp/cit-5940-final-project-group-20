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

        // start the timer for the player's turn
        gameTimer.start(() -> {
            gameView.showError("time is up! switching to next player.");
            switchPlayer(); // switch player if the time is up
        });

        // allow player to make multiple guesses within the time limit
        boolean isValidMove = false;
        while (!gameTimer.isTimeUp() && !isValidMove) {
            // Get the player's input (movie title)
            System.out.print("\rTime remaining: " + gameTimer.getTimeRemaining() + " seconds"); // Overwrite timer on same line
            // Flush the output to ensure the timer prints immediately
            System.out.flush();


            // Get the player's input on the next line
            String movieTitle = gameView.getPlayerInput("\nEnter movie title: "); // Input on new line after timer
            Movie nextMovie = movieDatabase.getMovieByTitle(movieTitle);

            if (nextMovie == null) {
                gameView.showError("Movie not found. Please try again.");
                continue; // as long as 30 sec not over, users can make more guesses
            }

            // check all possible connection types to see if the movie is valid
            for (ConnectionStrategy connection : connectionStrategies) {
                if (connection.areConnected(gameState.getCurrentMovie(), nextMovie)) {
                    // If a valid connection is found, mark move as valid
                    isValidMove = true;
                    gameState.updateState(nextMovie, connection.getType());  // Update the game state
                    currentPlayer.addMovie(nextMovie); // add the movie to the player's list and increment score
                    break;
                }
            }

            if (!isValidMove) {
                gameView.showError("Invalid connection. Try again.");
            }

        }

        if (isValidMove) {
            // If the move is valid, check for win condition
            if (currentPlayer.hasWon(winConditionStrategy)) {
                gameState.setGameOver(true);
                gameView.showWinner(currentPlayer);  // Show winner if player wins
            } else {
                switchPlayer();  // Switch to the next player
            }
        }
    }

    // Switch to the next player
    public void switchPlayer() {
        gameState.switchPlayer();
    }
}

