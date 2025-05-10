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


    /**
     * Returns the shared director name if both movies are connected via director.
     * If the movies have the same director,
     * method returns director's name.
     * Otherwise, it returns {@code null}.
     *
     * @param a the first movie
     * @param b the second movie
     * @return the shared director's name if connected; {@code null} otherwise
     */
    @Override
    public String getSharedElement(Movie a, Movie b) {
        String dirA = a.getDirector();
        String dirB = b.getDirector();
        return (dirA != null && dirB != null && dirA.equalsIgnoreCase(dirB)) ? dirA : null;
    }
}
