import java.util.Arrays;
import java.util.List;
import java.util.Collection;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.googlecode.lanterna.input.KeyStroke;

/**
 * Controls the flow of the Movie Name Game, managing turns, input, timer, and game logic.
 * Coordinates between the {@code GameView}, {@code GameState}, {@code MovieDatabase},
 * and various {@code ConnectionStrategy} instances.
 * It's like the conductor of the game orchestrating the logic and functions!
 */
public class GameController {
    private GameView gameView;
    private GameState gameState;
    private MovieDatabase movieDatabase;
    private List<ConnectionStrategy> connectionStrategies;
    private ScheduledExecutorService scheduler;
    private Autocomplete autocomplete;

    private volatile boolean turnActive = false;
    private volatile int secondsRemaining = 30;
    private volatile boolean timeoutOccurred = false;
    private ScheduledFuture<?> timerTask; //


    /**
     * Constructs a GameController with the necessary components
     * and initializes autocomplete with movie titles
     *
     * @param gameState the shared game state
     * @param gameView the UI view to interact with players
     * @param movieDatabase the movie database used to validate and connect movies
     * @param connectionStrategies the list of strategies that define valid movie-to-movie connections
     */
    public GameController(GameState gameState, GameView gameView, MovieDatabase movieDatabase,
                          List<ConnectionStrategy> connectionStrategies) {
        this.gameState = gameState;
        this.gameView = gameView;
        this.movieDatabase = movieDatabase;
        this.autocomplete = new Autocomplete();
        Collection<Movie> allMovies = movieDatabase.getAllMovies();
        autocomplete.buildTrie(allMovies);

        this.connectionStrategies = connectionStrategies;
        this.scheduler = Executors.newScheduledThreadPool(1);
    }

    /**
     * Starts the game setup and first player turn.
     * Prompts for player names and win conditions,
     * initializes game state, and launches the first round.
     */
    public void startGame() {
        String[] playerNames = gameView.getPlayerNames();

        Player player1 = new Player(playerNames[0]);
        Player player2 = new Player(playerNames[1]);
        List<Player> players = Arrays.asList(player1, player2);


        // ADDED PLAYER1, 2
        player1.setWinCondition(gameView.getPlayersWinConditions(player1, movieDatabase));
        player2.setWinCondition(gameView.getPlayersWinConditions(player2, movieDatabase));

        Movie startingMovie = movieDatabase.getValidStartingMovie(connectionStrategies);
        
        // shouldnt ever occur but just in case
        if (startingMovie == null) {
            gameView.showMessageAndPause("There is no valid starting movie");
            return;
        }

        gameState.initializeGame(players, startingMovie);
        // whichever the initial movie is set, it should be added to playedmovies
        gameState.addPlayedMovie(startingMovie);

        startTurn();
    }
    /**
     * Begins a player's turn,
     * handling input, suggestions, timeout, and move validation.
     * Then continues until the game ends keep changing the turn
     */
    private void startTurn() {
        if (gameState.isGameOver()) {
            return;
        }

        Player currentPlayer = gameState.getCurrentPlayer();
        gameView.displayGameState(gameState);

        secondsRemaining = 30;
        turnActive = true;
        timeoutOccurred = false;

        startTimer();

        StringBuilder inputBuffer = new StringBuilder();

        while (turnActive && !gameState.isGameOver()) {
            KeyStroke keyStroke = gameView.pollInput();

            if (keyStroke != null) {
                switch (keyStroke.getKeyType()) {
                    case Character:
                        inputBuffer.append(keyStroke.getCharacter());
                        String currentInput = inputBuffer.toString();
                        gameView.updatePlayerInput(currentInput);
                        // display auto complete suggestions
                        List<ITerm> suggestions = autocomplete.getSuggestions(currentInput);
                        List<String> topSuggestions = new ArrayList<>();
                        for (int i = 0; i < Math.min(5, suggestions.size()); i++) {
                            topSuggestions.add(suggestions.get(i).toString());
                        }
                        gameView.displaySuggestions(topSuggestions);
                        break;
                    case Enter:
                        String submitted = inputBuffer.toString().trim();
                        inputBuffer.setLength(0);
                        gameView.updatePlayerInput("");
                        handlePlayerMove(submitted);
                        break;
                    case Backspace:
                        if (inputBuffer.length() > 0) {
                            inputBuffer.deleteCharAt(inputBuffer.length() - 1);
                            gameView.updatePlayerInput(inputBuffer.toString());
                        }
                        break;
                    default:
                        break;
                }
            }

            if (timeoutOccurred) {
                break; // clean exit after timeout
            }

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // after exiting loop (timeout or valid move), if still playing
        if (!gameState.isGameOver()) {
            endTurnAndSwitchPlayer();
            startTurn();
        }
    }
    /**
     * Starts the countdown timer for the current player's turn.
     * Ends the turn if the time reaches zero.
     */
    private void startTimer() {
        if (timerTask != null && !timerTask.isDone()) {
            timerTask.cancel(true);
        }

        timerTask = scheduler.scheduleAtFixedRate(() -> {
            if (turnActive && secondsRemaining > 0) {
                secondsRemaining--;
                try {
                    updateScreen();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                if (secondsRemaining == 0) {
                    timeout();
                }
            }
        }, 1, 1, TimeUnit.SECONDS);
    }
    /**
     * Handles a timeout when a player fails to make a move in time.
     * Immediately ends the game and declares the other player as winner.
     */
    private void timeout() {
        stopTimer();
        timeoutOccurred = true;
        turnActive = false;

        try {
            Player loser = gameState.getCurrentPlayer();    // selects current player to become the loser!
            List<Player> players = gameState.getPlayers();
            Player winner = (players.get(0) == loser) ? players.get(1) : players.get(0);
            gameState.setGameOver(true);
            gameView.showWinner(winner);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Stops the active timer task.
     */
    private void stopTimer() {
        turnActive = false;
        if (timerTask != null && !timerTask.isDone()) {
            timerTask.cancel(true);
        }
    }

    /**
     * Updates only the timer display on the game screen.
     */

    private void updateScreen() {
        gameView.updateTimerOnly(secondsRemaining);
    }

    /**
     * Processes the player's movie guess
     * and validates it using the defined connection strategies.
     * If the move is valid and not previously used, the game state is updated accordingly.
     *
     * @param movieTitle the title of the movie guessed by the player
     */
    private void handlePlayerMove(String movieTitle) {
        Movie nextMovie = movieDatabase.getMovieByTitle(movieTitle);

        if (nextMovie == null) {
            gameView.showMessageAndPause("Movie not found. Try again!");
            return;
        }

        // also check if the movie is already used!
        if (gameState.getPlayedMovies().contains(nextMovie)) {
            gameView.showMessageAndPause("This movie has already been used!");
            return;
        }


        boolean isValidMove = false;
        for (ConnectionStrategy connection : connectionStrategies) {
            if (connection.areConnected(gameState.getCurrentMovie(), nextMovie)) {
                isValidMove = true;
                Player currentPlayer = gameState.getCurrentPlayer();
                gameState.addPlayedMovie(nextMovie);
                gameState.updateState(nextMovie, connection.getType());
                currentPlayer.addMovie(nextMovie);

                if (currentPlayer.hasWon()) {
                    gameState.setGameOver(true);
                    gameView.showWinner(currentPlayer);
                    return;
                }
                break;
            }
        }

        if (isValidMove) {
            stopTimer();
            gameView.showMessageAndPause("Nice move! Connected");
            endTurnAndSwitchPlayer();
            startTurn();
        } else {
            gameView.showMessageAndPause("Invalid connection. Try again.");
        }
    }
    /**
     * Ends the current player's turn, switches to the next player,
     * and increments the round.
     */
    private void endTurnAndSwitchPlayer() {
        gameState.switchPlayer();
        gameState.incrementRound();
    }
}
