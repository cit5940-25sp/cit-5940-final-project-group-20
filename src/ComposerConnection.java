public class ComposerConnection implements ConnectionStrategy {
    @Override
    public boolean areConnected(Movie a, Movie b) {
        String composerA = a.getComposer();
        String composerB = b.getComposer();

        if (composerA == null || composerB == null) {
            return false;
        }

        return composerA.equals(composerB);
    }

    @Override
    public String getType() {
        return "Composer";
    }
}
