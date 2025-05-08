/**
 * Implements a connection strategy based on shared directors between two movies.
 * Two movies are considered connected if they have the same director.
 */
public class DirectorConnection implements ConnectionStrategy {
    /**
     * Determines whether two movies are connected by having the same director.
     * This comparison is case-insensitive.
     * Returns {@code true} if both movies list the same director and neither is {@code null}.
     *
     * @param a the first movie
     * @param b the second movie
     * @return {@code true} if the movies share the same director; {@code false} otherwise
     */
    @Override
    public boolean areConnected(Movie a, Movie b) {
        String directorA = a.getDirector();
        String directorB = b.getDirector();

        if (directorA == null || directorB == null) {
            return false;
        }

        return directorA.equalsIgnoreCase(directorB);
    }
    /**
     * Returns the type of connection this strategy represents.
     *
     * @return the string "Director"
     */
    @Override
    public String getType() {
        return "Director";
    }
}
