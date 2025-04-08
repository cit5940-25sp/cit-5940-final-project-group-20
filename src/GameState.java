import java.util.*;

public class GameState {
    private List<Player> players;
    private int currentPlayerIndex;
    private Movie currentMovie;
    private Set<Movie> playedMovies;
    private int round;
    private Map<String, Integer> connectionUsageCount;
    private WinConditionStrategy winCondition;
    private boolean isGameOver; // I added this to UML (wasnt originally in it)

    // Get whoever's turn it is right now
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    // Switch to the next player (loop around if needed)
    public void switchPlayer() {
        // to be implemented
    }

    // What movie are we currently building off of?
    public Movie getCurrentMovie() {
        return currentMovie;
    }

    // Check if the move is actually valid with the given connection
    public boolean isValidMovie(Movie from, Movie to, ConnectionStrategy connection) {
        // to be implemented
        return false;
    }

    // Update the game state when a new movie is successfully played
    public void updateState(Movie nextMovie, String connection) {
        // to be implemented
    }

    // Is the game finished based on win condition or other rules?
    public boolean isGameOver() {
        return isGameOver;
    }

    // How many rounds have passed so far?
    public int getRound() {
        return round;
    }

    // Which movies have already been used in the game?
    public Set<Movie> getPlayedMovies() {
        return playedMovies;
    }

    // How many times has this connection type been used?
    public int getConnectionUsage(String type) {
        if (connectionUsageCount.containsKey(type)) {
            return connectionUsageCount.get(type);
        }
        return 0;
    }

    // Increase the counter for this type of connection
    public void incrementConnectionUsage(String type) {
        connectionUsageCount.put(type, getConnectionUsage(type) + 1);
    }
}
