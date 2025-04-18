public interface WinConditionStrategy {
    
    // Check if a player meets this specific win condition
    boolean isSatisfied(Player player);

    // Return the type of the condition
    String getConditionType();
}
