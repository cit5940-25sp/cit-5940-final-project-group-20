import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ActorConnection implements ConnectionStrategy {
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

    @Override
    public String getType() {
        return "Actor";
    }
}
