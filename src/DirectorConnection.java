public class DirectorConnection implements ConnectionStrategy {
    @Override
    public boolean areConnected(Movie a, Movie b) {
        return a.getDirector().equals(b.getDirector());  // Check if directors are the same
    }

    @Override
    public String getType() {
        return "Director";
    }
}
