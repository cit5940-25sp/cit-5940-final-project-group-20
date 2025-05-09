/**
 * Defines a strategy for determining whether two movies are connected based on a specific criterion,
 * such as shared director, actor, genre, etc.
 * <p>
 * This interface is used in the Movie Name Game to evaluate valid connections between movie choices.
 */
public interface ConnectionStrategy {

    /**
     * Determines whether two movies are connected according to this strategy.
     * <p>
     * For example, two movies may be connected if they share the same director, actor, or genre.
     *
     * @param a the first movie
     * @param b the second movie
     * @return {@code true} if the movies are considered connected; {@code false} otherwise
     */
    boolean areConnected(Movie a, Movie b);

    /**
     * Returns a string describing the type of connection this strategy represents.
     * <p>
     * For example, "Director", "Actor", or "Genre".
     *
     * @return the name of the connection type
     */
    String getType();

    /**
     * Extracts the shared element (such as a common actor, genre, or director) that forms the basis
     * of the connection between two movies, if one exists.
     *
     * @param m1 the first movie
     * @param m2 the second movie
     * @return the shared element string if present; {@code null} if no connection is found
     */
    String getSharedElement(Movie m1, Movie m2);


}
