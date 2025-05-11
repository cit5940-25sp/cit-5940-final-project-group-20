/**
 * Implements a connection strategy based on shared composers between two movies.
 * Two movies are considered connected if they have the same composer.
 */

public class ComposerConnection implements ConnectionStrategy {

    /**
     * Determines whether two movies are connected by having the same composer.
     * Returns {@code true} if both movies list the same composer and neither is {@code null}.
     *
     * @param a the first movie
     * @param b the second movie
     * @return {@code true} if the movies share the same composer; {@code false} otherwise
     */
    @Override
    public boolean areConnected(Movie a, Movie b) {
        // extracts composers from both movies
        String composerA = a.getComposer();
        String composerB = b.getComposer();
        // makes sure composer field is valid
        if (composerA == null || composerB == null) {
            return false;
        }
        // returns if connection is valide!
        return composerA.equals(composerB);
    }

    /**
     * Returns the type of connection this strategy represents.
     *
     * @return the string "Composer"
     */
    @Override
    public String getType() {
        return "Composer";
    }



    @Override
    public String getSharedElement(Movie a, Movie b) {
        // returns composer to check if we have used it < 3 times
        String composerA = a.getComposer();
        String composerB = b.getComposer();
        return (composerA != null && composerB != null && composerA.equals(composerB)) ? composerA : null;
    }


}
