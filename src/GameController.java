import java.util.List;

public class GameController {
    private GameView view;
    private GameState gameState;
    private MovieDatabase movieDatabase;
    private ConnectionStrategy connectionFactory;
    private WinConditionStrategy winConditionFactory;
    private List<Player> players;
    private GameTimer gameTimer;

    // Start the game (maybe show start screen, initialize stuff)
    public void startGame() {
        // to be implemented
    }

    // Called each turn to handle what a player does
    public void handleTurn() {
        // to be implemented
    }

    // Try to play a movie with a certain connection (e.g., same actor, genre, etc.)
    public void handleMove(String movieTitle, String connectionType) {
        // to be implemented
    }

    // Check if one movie is actually connected to another with the given connection type
    public boolean validateMovie(Movie from, Movie to, ConnectionStrategy connection) {
        // to be implemented
        return false;
    }

    // Go to the next player's turn
    public void switchPlayer() {
        // to be implemented
    }

    // Ask the user (or randomly pick?) the win condition
    public WinConditionStrategy promptWithCondition() {
        // to be implemented
        return null;
    }

    // Start the game timer for a turn
    public void startTimer(Runnable onTimeout) {
        // to be implemented
    }

    // Cancel the timer if move is made in time
    public void cancelTimer() {
        // to be implemented
    }
}
