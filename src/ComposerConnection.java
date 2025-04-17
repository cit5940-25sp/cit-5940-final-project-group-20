public class ComposerConnection implements ConnectionStrategy {
    @Override
    public boolean areConnected(Movie a, Movie b) {
        return a.getComposer().equals(b.getComposer());  // Check if composers are the same
    }

    @Override
    public String getType() {
        return "Composer";
    }
}

