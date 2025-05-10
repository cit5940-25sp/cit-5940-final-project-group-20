/**
 * Checks if a player wins by playing a certain number of movies in a specific genre.
 */
public class GenreWinCondition implements WinConditionStrategy {

    private String genre;
    private int requiredCount;

    /**
     * Creates a new GenreWinCondition.
     *
     * @param genre the genre to check for
     * @param requiredCount how many movies of the genre are needed to win
     */
    public GenreWinCondition(String genre, int requiredCount) {
        this.genre = genre;
        this.requiredCount = requiredCount;
    }

    /**
     * Checks if the player has played enough movies in the specified genre.
     *
     * @param player the player to check
     * @return true if the player meets the win condition, false otherwise
     */
    @Override
    public boolean isSatisfied(Player player) {
        // count how many movies player has played that match the required genre
        int genreCount = 0;
        for (Movie movie : player.getMoviesPlayed()) {
            if (movie.getGenres().contains(genre)) {
                genreCount++;
            }
        }
        // player wins if they played the required number of movies in this genre
        return genreCount >= requiredCount;
    }

    /**
     * Gets the type of this win condition.
     *
     * @return "Genre"
     */
    @Override
    public String getConditionType() {
        return "Genre";
        // only return TYPE not formatted sentence because
        // we want to keep MVC model where prints happen inside View class
    }

    /**
     * Gets the target genre for this condition.
     *
     * @return the genre
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Gets how many movies in the genre are needed to win.
     *
     * @return the required count
     */
    public int getRequiredCount() {
        return requiredCount;
    }
}
