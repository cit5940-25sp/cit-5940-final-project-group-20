public interface WinConditionStrategy {
    
    // Check if a player meets this specific win condition
    boolean isSatisfied(Player player);

    // Return the type of the condition (e.g., "play 5 movies", "use 3 genres", etc.)
    String getConditionType();
}
