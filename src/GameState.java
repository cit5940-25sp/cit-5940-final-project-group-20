import java.util.*;

/**
 * Represents a single state of the Movie Name Game.
 * Tracks players, the current movie, played movies, round number,
 * connection usage statistics, game progress, and recent connection history.
 */
public class GameState {
    private List<Player> players; // list of players in the game
    private int currentPlayerIndex; // represents current player (0 for player1, 1 for player2)
    private Movie currentMovie; // movie that the current player has to connect to
    private Set<Movie> playedMovies; // set to track all played moview
    private int round; // how many rounds have we gone (current round number)
    private Map<String, Integer> connectionUsageCount; // connection usage count for each type (actor, director etc)
    private boolean isGameOver; // check if the game is over
    private List<String> connectionHistory; // track last few moves

    /**
     * Constructs an initial game state with default values.
     * Players, played movies, connection counts, and connection history
     * are initialized to empty structures.
     */
    public GameState() {
        this.players =  new ArrayList<>(); // only players are initialized once they type in names
        this.currentPlayerIndex = 0; // player 1 starts
        this.currentMovie = null;
        this.playedMovies = new HashSet<>(); // can't play movies that's been already played
        this.round = 1; // start from round 1
        this.connectionUsageCount = new HashMap<>();
        this.isGameOver = false;
    }

    /**
     * Initializes a new game with the given players and starting movie.
     * Resets round count, connection usage, played movie list, and game-over status.
     *
     * @param players        the list of players participating
     * @param startingMovie  the first movie to start the game
     */
    public void initializeGame(List<Player> players, Movie startingMovie) {
        this.players = players;
        this.currentPlayerIndex = 0; // player1 starts
        this.currentMovie = startingMovie;
        this.playedMovies.clear(); // clear the list of playedMovies at the start
        this.round = 1; // start from round 1
        this.connectionUsageCount.clear(); // clear connection usage count
        this.isGameOver = false; // initially gameOver is false
        this.connectionHistory = new ArrayList<>();
    }

    /**
     * Returns the current player.
     *
     * @return the player whose turn it is
     */
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    /**
     * Returns the movie that the current player must connect to.
     *
     * @return the current movie in play
     */
    public Movie getCurrentMovie() {
        return currentMovie;
    }

    /**
     * Switches the turn to the next player.
     * Can be multiple players greater (not only for 2 players)
     */

    public void switchPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size(); // toggle using modulo
    }


    /**
     * Sets the game over state.
     *
     * @param gameOver {@code true} if the game is over, otherwise {@code false}
     */
    public void setGameOver(boolean gameOver) {
        this.isGameOver = gameOver;
    }

    /**
     * Returns the current round number.
     *
     * @return the round count
     */
    public int getRound() {
        return round;
    }


    /**
     * Increments the round number by one.
     */
    public void incrementRound() {
        this.round++;
    }

    /**
     * Adds a movie to the set of played movies.
     *
     * @param movie the movie to add
     */
    public void addPlayedMovie(Movie movie) {
        playedMovies.add(movie);
    }

    /**
     * Returns all movies that have been played so far.
     *
     * @return a set of played {@code Movie} objects
     */
    public Set<Movie> getPlayedMovies() {
        return playedMovies;
    }

    /**
     * Updates the game state by setting the next movie and recording the connection type.
     * Also adds the connection to history and updates connection usage.
     *
     * @param nextMovie      the next movie played
     * @param connectionType the connection type used to transition to the next movie
     */
    public void updateState(Movie nextMovie, String connectionType) {

        if (currentMovie != null && nextMovie != null) {
            String entry = currentMovie.getTitle() +
                     " → " + nextMovie.getTitle() +
                     " [" + connectionType + "]";
            connectionHistory.add(entry);

            // Keep only the most recent 5 entries
            if (connectionHistory.size() > 5) {
                connectionHistory.remove(0);
            }
        }
        currentMovie = nextMovie;
        incrementRound();
        incrementConnectionUsage(connectionType);
    }

    /**
     * Returns the list of the most recent movie connections.
     *
     * @return a list of connection history strings
     */
    public List<String> getRecentHistory() {
        return new ArrayList<>(connectionHistory);
    }

    /**
     * Returns how many times a given connection type has been used.
     *
     * @param connectionType the type of connection
     * @return the number of times this connection has been used
     */
    public int getConnectionUsage(String connectionType) {
        return connectionUsageCount.getOrDefault(connectionType, 0);
    }

    /**
     * Increments the count for a specific connection type.
     *
     * @param connectionType the connection type to increment
     */

    public void incrementConnectionUsage(String connectionType) {
        connectionUsageCount.put(connectionType, connectionUsageCount.getOrDefault(connectionType, 0) + 1);
    }

    /**
     * Checks whether a player has satisfied their win condition.
     *
     * @param player the player to check
     * @return {@code true} if the player has won; otherwise {@code false}
     */
    public boolean hasPlayerWon(Player player) {
        return player.hasWon(); // player knows its own win conditino
    }

    /**
     * Returns the list of players in the game.
     *
     * @return a list of {@code Player} objects
     */
    public List<Player> getPlayers() {
        return players;
    }


    /**
     * Returns whether the game is currently over.
     *
     * @return {@code true} if the game has ended; otherwise {@code false}
     */
    public boolean isGameOver() {
        return isGameOver;
    }

    /**
     * Returns the player who has won the game, if any.
     *
     * @return the winning player, or {@code null} if no one has won yet
     */
    public Player getWinner() {
        for (Player player : players) {
            if (player.hasWon()) {
                return player;
            }
        }
        return null;  // No winner if game isn't over
    }

}
