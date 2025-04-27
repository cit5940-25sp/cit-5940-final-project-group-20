import java.util.*;

public class Player {
    private String name;
    private int score;
    private List<Movie> moviesPlayed; // list of movies player has played
    private WinConditionStrategy winCondition;

    // constructor. Initialize the player with their name
    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.moviesPlayed = new ArrayList<>();
        this.winCondition = null; // default not set
    }

    // Return player's name
    public String getName() {
        return name;
    }

    // get player's score
    public int getScore() {
        return score;
    }

    // Add a movie the player just played (only if it hasn't been played already)
    public void addMovie(Movie movie) {
        if (!moviesPlayed.contains(movie)) {  // Ensure movie isn't added twice
            moviesPlayed.add(movie);
            score++;  // Increase score whenever a new movie is added
        }
    }

    // Get all movies the player has played so far
    public List<Movie> getMoviesPlayed() {
        return moviesPlayed;
    }

    // set wincondition
    public void setWinCondition(WinConditionStrategy winCondition) {
        this.winCondition = winCondition;
    }

    // get win condition
    public WinConditionStrategy getWinCondition() {
        return this.winCondition;
    }


    // Check if this specifc player has won based on the win condition
    public boolean hasWon() {
        if (winCondition == null) {
            return false;
        }
        return winCondition.isSatisfied(this);
    }

    // reset the player's score (when start a new game)
    public void resetScore() {
        this.score = 0;
    }
}

