public interface WinConditionStrategy {
    
    // Check if a player meets this specific win condition
    boolean isSatisfied(Player player);

    // What's the name of the condition (e.g., "play 5 movies", "use 3 genres", etc.)
    String getConditionType();

    // If this condition is targeting a specific value (like "Genre: Action"), return it!
    String getLockedTarget();

    // If this condition needs a certain number (e.g., 3 actors, 5 movies), return that
    int getRequiredCount();
}
