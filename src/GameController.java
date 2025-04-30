import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.googlecode.lanterna.input.KeyStroke;

public class GameController {
    private GameView gameView;
    private GameState gameState;
    private MovieDatabase movieDatabase;
    private List<ConnectionStrategy> connectionStrategies;
    private ScheduledExecutorService scheduler;

    private volatile boolean turnActive = false;
    private volatile int secondsRemaining = 30;
    private volatile boolean timeoutOccurred = false;
    private ScheduledFuture<?> timerTask; //

    public GameController(GameState gameState, GameView gameView, MovieDatabase movieDatabase,
                          List<ConnectionStrategy> connectionStrategies) {
        this.gameState = gameState;
        this.gameView = gameView;
        this.movieDatabase = movieDatabase;
        this.connectionStrategies = connectionStrategies;
        this.scheduler = Executors.newScheduledThreadPool(1);
    }

    public void startGame() {
        String[] playerNames = gameView.getPlayerNames();

        Player player1 = new Player(playerNames[0]);
        Player player2 = new Player(playerNames[1]);
        List<Player> players = Arrays.asList(player1, player2);

        player1.setWinCondition(gameView.getPlayersWinConditions(movieDatabase));
        player2.setWinCondition(gameView.getPlayersWinConditions(movieDatabase));

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
                        gameView.updatePlayerInput(inputBuffer.toString());
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

    // Terminates Game once time is up! 
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

    
    private void stopTimer() {
        turnActive = false;
        if (timerTask != null && !timerTask.isDone()) {
            timerTask.cancel(true);
        }
    }

    private void updateScreen() {
        gameView.updateTimerOnly(secondsRemaining);
    }

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

    private void endTurnAndSwitchPlayer() {
        gameState.switchPlayer();
        gameState.incrementRound();
    }
}
