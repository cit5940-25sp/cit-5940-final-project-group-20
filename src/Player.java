import java.util.*;
/**
 * Class for a players in the Movie Name Game.
 * Each player has a name, score, a list of movies they have played,
 * and a win condition strategy that defines how they can win.
 */
public class Player {
    private String name;
    private int score;
    private List<Movie> moviesPlayed; // list of movies player has played
    private WinConditionStrategy winCondition;

    /**
     * Constructs a new Player with the name user typed in
     * Initializes the score to 0 and the list of played movies to empty.
     *
     * @param name the name of the player
     */

    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.moviesPlayed = new ArrayList<>();
        this.winCondition = null; // default not set
    }

    /**
     * Get the name of the player.
     *
     * @return the player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the current score of the player.
     *
     * @return the player's score
     */
    public int getScore() {
        return score;
    }

    /**
     * Adds a movie to the player's list of played movies.
     * If the movie has not already been played, it is added
     * and the score is incremented.
     *
     * @param movie the movie to add
     */

    public void addMovie(Movie movie) {
        if (!moviesPlayed.contains(movie)) {  // make sure user can't add same movie twice
            moviesPlayed.add(movie);
            score++;  // add score if user adds movie
        }
    }

    /**
     * Returns the list of movies the player has played.
     *
     * @return a list of {@code Movie} objects played by the player
     */
    public List<Movie> getMoviesPlayed() {
        return moviesPlayed;
    }

    /**
     * Sets the win condition strategy for the player.
     *
     * @param winCondition the win condition strategy to assign
     */

    public void setWinCondition(WinConditionStrategy winCondition) {
        this.winCondition = winCondition;
    }

    /**
     * Returns the current win condition strategy of the player.
     *
     * @return the player's win condition strategy
     */
    public WinConditionStrategy getWinCondition() {
        return this.winCondition;
    }


    /**
     * Checks if the player has satisfied their win condition.
     *
     * @return {@code true} if the win condition is satisfied, {@code false} otherwise
     */
    public boolean hasWon() {
        if (winCondition == null) {
            return false;
        }
        return winCondition.isSatisfied(this);
    }

    /**
     * Resets the player's score to 0.
     * Handy when restarting the game
     */

    public void resetScore() {
        this.score = 0;
    }
}

