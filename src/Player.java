import java.util.*;

public class Player {
    private String name;
    private int score;
    private List<Movie> moviesPlayed;

    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.moviesPlayed = new ArrayList<>();
    }

    // Return player's name
    public String getName() {
        return name;
    }

    // Add a movie the player just played
    public void addMovie(Movie movie) {
        moviesPlayed.add(movie);
    }

    // Get all movies the player has played so far
    public List<Movie> getMoviesPlayed() {
        return moviesPlayed;
    }

    // Check if this specifc player has won based on the win condition
    public boolean hasWon(WinConditionStrategy winCondition) {
        // to be implemented
        return false;
    }
}

