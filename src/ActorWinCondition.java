/**
 * A win condition strategy where a player must play a certain number of movies
 * that feature a specific actor.
 * <p>
 * For example, if the actor is "Tom Hanks" and the required count is 3, the
 * player wins after playing 3 or more movies featuring Tom Hanks!
 */
public class ActorWinCondition implements WinConditionStrategy {

    // private attributes for ActorWinCondition
    private String actor;
    private int requiredCount;

    /**
     * Constructs an ActorWinCondition with the specified actor and count.
     *
     * @param actor         the name of the actor required to win
     * @param requiredCount the number of times the actor must appear in played movies
     */
    public ActorWinCondition(String actor, int requiredCount) {
        this.actor = actor;
        this.requiredCount = requiredCount;
    }

    /**
     * Checks if the player has satisfied the win condition by playing enough movies
     * featuring the specified actor.
     *
     * @param player the player whose progress is checked
     * @return {@code true} if the condition is met; otherwise {@code false}
     */
    @Override
    public boolean isSatisfied(Player player) {
        int count = 0;
        for (Movie movie : player.getMoviesPlayed()) {
            if (movie.getActors().contains(actor)) {
                count++;
            }
        }
        return count >= requiredCount; // returns True if required count hit!
    }

    /**
     * Returns the type of win condition.
     *
     * @return the string "Actor"
     */
    @Override
    public String getConditionType() {
        return "Actor";
    }

    /**
     * Returns the name of the actor required to fulfill the condition.
     *
     * @return the actor's name
     */
    public String getActor() {
        return actor;
    }

    /**
     * Returns how many times the actor must appear for the condition to be satisfied.
     *
     * @return the required count
     */
    public int getRequiredCount() {
        return requiredCount;
    }
}