import java.util.HashSet;
import java.util.Set;


public class ActorConnection implements ConnectionStrategy {
    @Override
    public boolean areConnected(Movie a, Movie b) {

        // Make set to reduce look up time/avoid nested forloop
        Set<String> actorsA = new HashSet<>(a.getActors());
  
        for (String actorB : b.getActors()) {
            if (actorsA.contains(actorB)) {
                return true;
            }
        }
        return false; // no common actor found
    }

    @Override
    public String getType() {
        return "Actor";
    }
}
