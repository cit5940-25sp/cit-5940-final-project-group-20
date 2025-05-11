/**
 * Represents a win condition based on movies directed by a specific director.
 * A player satisfies this condition if they have played at least a specified number
 * of movies that share the given director's name.
 */
public class DirectorWinCondition implements WinConditionStrategy {
    // private field initialisation
    private String director;
    private int requiredCount;

    /**
     * Constructs a {@code DirectorWinCondition} with a specific director and count requirement.
     *
     * @param director       the name of the director that qualifies towards the win condition
     * @param requiredCount  the number of movies needed with that director to win
     */
    public DirectorWinCondition(String director, int requiredCount) {
        this.director = director;
        this.requiredCount = requiredCount;
    }

    /**
     * Checks whether the player has satisfied this director-based win condition.
     * <p>
     * The condition is met if the number of movies the player has played with
     * the target director is greater than or equal to the required count.
     *
     * @param player the player whose movies are being evaluated
     * @return {@code true} if the condition is satisfied, {@code false} otherwise
     */
    @Override
    public boolean isSatisfied(Player player) {
        int count = 0;
        // keeps count of all movies user played and increments each time movie played with given director
        for (Movie movie : player.getMoviesPlayed()) {
            if (director.equalsIgnoreCase(movie.getDirector())) {   
                count++;
            }
        }
        // returns if target hit!
        return count >= requiredCount; // returns true if required count is reached!
    }

    /**
     * Returns the type of win condition represented by this strategy.
     *
     * @return the string "Director"
     */
    @Override
    public String getConditionType() {
        return "Director";
    }

    /**
     * Returns the name of the director associated with this win condition.
     *
     * @return the director's name
     */
    public String getDirector() {
        return director;
    }

    /**
     * Returns the number of qualifying movies required to satisfy this condition.
     *
     * @return the required movie count
     */
    public int getRequiredCount() {
        return requiredCount;
    }
}
