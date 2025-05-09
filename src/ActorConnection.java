import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implements a connection strategy based on shared actors between two movies.
 * Two movies are considered connected if they share at least one actor.
 */
public class ActorConnection implements ConnectionStrategy {
    /**
     * Determines whether two movies are connected by a shared actor.
     * Returns true if there is at least one actor who appears in both movies.
     *
     * @param a the first movie
     * @param b the second movie
     * @return {@code true} if the movies share an actor; {@code false} otherwise
     */
    @Override
    public boolean areConnected(Movie a, Movie b) {
        List<String> actorsA = a.getActors();
        List<String> actorsB = b.getActors();

        if (actorsA == null || actorsB == null) {
            return false;
        }

        Set<String> actorSetA = new HashSet<>(actorsA);
        for (String actorB : actorsB) {
            if (actorSetA.contains(actorB)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns the type of connection this strategy represents.
     *
     * @return the string "Actor"
     */
    @Override
    public String getType() {
        return "Actor";
    }


    @Override
    public String getSharedElement(Movie a, Movie b) {
        List<String> actorsA = a.getActors();
        List<String> actorsB = b.getActors();

        if (actorsA == null || actorsB == null) {
            return null;
        }

        Set<String> actorSetA = new HashSet<>(actorsA);
        for (String actorB : actorsB) {
            if (actorSetA.contains(actorB)) {
                return actorB;
            }
        }

        return null;
    }
}
