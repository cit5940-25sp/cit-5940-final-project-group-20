public class ActorConnection implements ConnectionStrategy {
    @Override
    public boolean areConnected(Movie a, Movie b) {
        for (String actorA : a.getActors()) {
            for (String actorB : b.getActors()) {
                if (actorA.equals(actorB)) {
                    return true; // found common actor
                }
            }
        }
        return false; // no common actor found
    }

    @Override
    public String getType() {
        return "Actor";
    }
}
