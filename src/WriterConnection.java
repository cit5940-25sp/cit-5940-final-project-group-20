public class WriterConnection implements ConnectionStrategy {
    @Override
    public boolean areConnected(Movie a, Movie b) {
        return a.getWriter().equals(b.getWriter());  // Check if writers are the same
    }

    @Override
    public String getType() {
        return "Writer";
    }
}
