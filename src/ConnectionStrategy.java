// We might need to expand on the methods here...

public interface ConnectionStrategy {

    // Check if two movies are connected based on this strategy (like same director, etc.)
    boolean areConnected(Movie a, Movie b);

    // Return the name/type of this connection (like "Director", "Actor", etc.)
    String getType();
}
