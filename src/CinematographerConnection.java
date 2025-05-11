/**
 * Implements a connection strategy based on shared cinematographers between two movies.
 * Two movies are considered connected if they have the same cinematographer.
 */
public class CinematographerConnection implements ConnectionStrategy {
    /**
     * Determines whether two movies are connected by having the same cinematographer.
     * Returns {@code true} if both movies list the same cinematographer and neither is {@code null}.
     *
     * @param a the first movie
     * @param b the second movie
     * @return {@code true} if the movies share the same cinematographer; {@code false} otherwise
     */
    @Override
    public boolean areConnected(Movie a, Movie b) {
        // extracts cinamatographers from movie a and b
        String cinematographerA = a.getCinematographer();
        String cinematographerB = b.getCinematographer();

        // makes sure cinamatographers have a valid field in databas
        if (cinematographerA == null || cinematographerB == null) {
            return false;
        }
        // returns if connection is true or false!
        return cinematographerA.equals(cinematographerB);
    }

    /**
     * Returns the type of connection this strategy represents.
     *
     * @return the string "Cinematographer"
     */
    public String getType() {
        return "Cinematographer";
    }


    @Override
    public String getSharedElement(Movie a, Movie b) {
        // extracts the connected cinematographer to check if connection valid (under 3 times)
        String ca = a.getCinematographer();
        String cb = b.getCinematographer();
        return (ca != null && cb != null && ca.equals(cb)) ? ca : null;
    }


}
