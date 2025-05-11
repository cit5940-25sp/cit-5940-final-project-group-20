import java.util.*;
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
    private ScheduledFuture<?> timerTask; // scheduled task for countdown timer

    /**
     * Constructs a GameController with the necessary components
     * and initializes autocomplete with movie titles
     */
    public GameController(GameState gameState, GameView gameView, MovieDatabase movieDatabase,
                          List<ConnectionStrategy> connectionStrategies) {
        this.gameState = gameState;
        this.gameView = gameView;
        this.movieDatabase = movieDatabase;
        this.autocomplete = new Autocomplete();
        Collection<Movie> allMovies = movieDatabase.getAllMovies(); // populate autocomplete with all movies
        autocomplete.buildTrie(allMovies);

        this.connectionStrategies = connectionStrategies;
        this.scheduler = Executors.newScheduledThreadPool(1); // single-thread scheduler for timer
    }

    /**
     * Starts the game setup and first player turn.
     * Prompts for player names and win conditions,
     * initializes game state, and launches the first round.
     */
    public void startGame() {
        gameView.welcomeMessage();
        String[] playerNames = gameView.getPlayerNames();
        Player player1 = new Player(playerNames[0]);
        Player player2 = new Player(playerNames[1]);
        List<Player> players = Arrays.asList(player1, player2);

        // get win conditions from UI for both players
        player1.setWinCondition(gameView.getPlayersWinConditions(player1, movieDatabase));
        player2.setWinCondition(gameView.getPlayersWinConditions(player2, movieDatabase));

        Movie startingMovie = movieDatabase.getValidStartingMovie(connectionStrategies);

        // fallback in case no valid starting movie is found
        if (startingMovie == null) {
            gameView.showMessageAndPause("There is no valid starting movie");
            return;
        }

        gameState.initializeGame(players, startingMovie);
        gameState.addPlayedMovie(startingMovie); // mark as played immediately

        startTurn();
    }

    /**
     * Begins a player's turn,
     * handling input, suggestions, timeout, and move validation.
     */
    private void startTurn() {
        if (gameState.isGameOver()) return;

        Player currentPlayer = gameState.getCurrentPlayer();
        gameView.displayGameState(gameState); // show current status

        secondsRemaining = 30;
        turnActive = true;
        timeoutOccurred = false;

        startTimer(); // begin countdown

        StringBuilder inputBuffer = new StringBuilder(); // store typed input

        while (turnActive && !gameState.isGameOver()) {
            KeyStroke keyStroke = gameView.pollInput(); // listen for keystrokes

            if (keyStroke != null) {
                switch (keyStroke.getKeyType()) {
                    case Character:
                        inputBuffer.append(keyStroke.getCharacter()); // append typed char
                        break;
                    case Backspace:
                        if (inputBuffer.length() > 0) {
                            inputBuffer.deleteCharAt(inputBuffer.length() - 1); // remove last char
                        }
                        break;
                    case Enter:
                        String submitted = inputBuffer.toString().trim();
                        inputBuffer.setLength(0); // clear buffer
                        gameView.updatePlayerInput(""); // clear GUI
                        gameView.displaySuggestions(Collections.emptyList()); // clear suggestions
                        handlePlayerMove(submitted); // validate input
                        continue; // start loop again
                    default:
                        break;
                }

                String currentInput = inputBuffer.toString();
                gameView.updatePlayerInput(currentInput); // show typed input live

                if (currentInput.trim().isEmpty()) {
                    gameView.displaySuggestions(Collections.emptyList()); // no suggestions
                } else {
                    List<ITerm> suggestions = autocomplete.getSuggestions(currentInput.trim());
                    List<String> topSuggestions = new ArrayList<>();
                    for (int i = 0; i < Math.min(5, suggestions.size()); i++) {
                        topSuggestions.add(suggestions.get(i).toString()); // show top 5 suggestions
                    }
                    gameView.displaySuggestions(topSuggestions);
                }
            }

            if (timeoutOccurred) break;

            try {
                Thread.sleep(50); // small delay to avoid tight loop
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        if (!gameState.isGameOver()) {
            endTurnAndSwitchPlayer();
            startTurn(); // next player's turn
        }
    }

    /**
     * Starts the countdown timer for the current player's turn.
     * Ends the turn if the time reaches zero.
     */
    private void startTimer() {
        if (timerTask != null && !timerTask.isDone()) {
            timerTask.cancel(true); // cancel previous timer if still running
        }

        timerTask = scheduler.scheduleAtFixedRate(() -> {
            if (turnActive && secondsRemaining > 0) {
                secondsRemaining--;
                try {
                    updateScreen(); // update timer on screen
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    Thread.sleep(10); // slight pause
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                if (secondsRemaining == 0) {
                    timeout(); // force timeout
                }
            }
        }, 1, 1, TimeUnit.SECONDS); // start after 1 sec, repeat every 1 sec
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
            Player loser = gameState.getCurrentPlayer(); // current player ran out of time
            List<Player> players = gameState.getPlayers();
            Player winner = (players.get(0) == loser) ? players.get(1) : players.get(0); // pick other player
            gameState.setGameOver(true);
            gameView.showWinner(winner); // announce winner
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
     */
    private void handlePlayerMove(String movieTitle) {
        Movie nextMovie = movieDatabase.getMovieByTitle(movieTitle);

        if (nextMovie == null) {
            gameView.showMessageAndPause("Movie not found. Try again!");
            return;
        }

        // check if already played
        if (gameState.getPlayedMovies().contains(nextMovie)) {
            gameView.showMessageAndPause("This movie has already been used!");
            return;
        }

        boolean isValidMove = false;
        boolean connectionOverused = false;
        String specificValue = null;
        String connectType = null;

        for (ConnectionStrategy connection : connectionStrategies) {
            connectType = connection.getType();

            if (connection.areConnected(gameState.getCurrentMovie(), nextMovie)) {
                specificValue = connection.getSharedElement(gameState.getCurrentMovie(), nextMovie);

                if (!gameState.canUseSpecificConnection(connectType, specificValue)) {
                    connectionOverused = true;
                    break; // don't allow overused connections
                }

                isValidMove = true;

                gameState.incrementSpecificConnectionUsage(connectType, specificValue);
                Player currentPlayer = gameState.getCurrentPlayer();
                gameState.addPlayedMovie(nextMovie);
                gameState.updateState(nextMovie, connection.getType());
                currentPlayer.addMovie(nextMovie);

                if (currentPlayer.hasWon()) {
                    stopTimer();
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
        } else if (connectionOverused) {
            gameView.printString(0, 24, " ".repeat(120)); // clear prompt line
            gameView.showMessageAndPause("The connection '" + specificValue + "' (" + connectType + ") has already been used 3 times!");
        } else {
            gameView.printString(0, 24, " ".repeat(120)); // clear prompt line
            gameView.showMessageAndPause("Invalid connection. Try again.");
        }
    }

    /**
     * Ends the current player's turn, switches to the next player,
     * and increments the round.
     */
    private void endTurnAndSwitchPlayer() {
        gameState.switchPlayer();
    }
}

