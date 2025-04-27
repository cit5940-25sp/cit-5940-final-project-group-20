import java.util.*;

public class GameState {
    private List<Player> players; // list of players in the game
    private int currentPlayerIndex; // represents current player (0 for player1, 1 for player2)
    private Movie currentMovie; // movie that the current player has to connect to
    private Set<Movie> playedMovies; // set to track all played moview
    private int round; // how many rounds have we gone (current round number)
    private Map<String, Integer> connectionUsageCount; // connection usage count for each type (actor, director etc)
    private boolean isGameOver; // check if the game is over
    private List<String> connectionHistory; // track last few moves

    // construct and initialize
    public GameState() {
        this.players =  new ArrayList<>(); // only players are initialized once they type in names
        this.currentPlayerIndex = 0; // player 1 starts
        this.currentMovie = null;
        this.playedMovies = new HashSet<>(); // can't play movies that's been already played
        this.round = 1; // start from round 1
        this.connectionUsageCount = new HashMap<>();
        this.isGameOver = false;
    }

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

    // get current player
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    // get current movie
    public Movie getCurrentMovie() {
        return currentMovie;
    }

    // Switch to the next player
    public void switchPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size(); // toggle using modulo
    }

    // check if the game is over
    public void setGameOver(boolean gameOver) {
        this.isGameOver = gameOver;
    }

    // get the round number
    public int getRound() {
        return round;
    }


    // increment the round number after each turn
    public void incrementRound() {
        this.round++;
    }

    // add movie to the list of played movies
    public void addPlayedMovie(Movie movie) {
        playedMovies.add(movie);
    }

    // get all played movies
    public Set<Movie> getPlayedMovies() {
        return playedMovies;
    }

    // update the game state with next movie and connection type used
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

    public List<String> getRecentHistory() {
        return new ArrayList<>(connectionHistory);
    }

    // How many times has this connection type been used?
    public int getConnectionUsage(String connectionType) {
        return connectionUsageCount.getOrDefault(connectionType, 0);
    }

    // Increment the usage count for a connection type
    public void incrementConnectionUsage(String connectionType) {
        connectionUsageCount.put(connectionType, connectionUsageCount.getOrDefault(connectionType, 0) + 1);
    }

    // check if a player has won based on the win condition
    public boolean hasPlayerWon(Player player) {
        return player.hasWon(); // player knows its own win conditino
    }

    public List<Player> getPlayers() {
        return players;
    }


    // Is the game finished based on win condition or other rules?
    public boolean isGameOver() {
        return isGameOver;
    }

    // Get the winner of the game
    public Player getWinner() {
        for (Player player : players) {
            if (player.hasWon()) {
                return player;
            }
        }
        return null;  // No winner if game isn't over
    }

}
