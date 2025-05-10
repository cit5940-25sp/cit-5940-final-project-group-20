/**
 * Represents a strategy for determining whether a player has satisfied a specific win condition.
 * <p>
 * This interface allows for flexible definitions of different winning criteria,
 * such as collecting movies of a certain genre, involving a specific actor, director, etc.
 */
public interface WinConditionStrategy {

    /**
     * Determines whether the given player satisfies the win condition defined by this strategy.
     *
     * @param player the player whose movie list is being evaluated
     * @return {@code true} if the player has met the condition; {@code false} otherwise
     */
    boolean isSatisfied(Player player);

    /**
     * Returns the type of win condition this strategy represents.
     * For example, "Genre", "Actor", or "Director".
     *
     * @return a string representing the win condition type
     */
    String getConditionType();
}
