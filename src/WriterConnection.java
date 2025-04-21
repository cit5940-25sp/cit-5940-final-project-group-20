public class WriterConnection implements ConnectionStrategy {
    @Override
    public boolean areConnected(Movie a, Movie b) {
        String writerA = a.getWriter();
        String writerB = b.getWriter();

        // if it is null, then just return false
        if (writerA == null || writerB == null) {
            return false;
        }
        return writerA.equals(writerB);
    }

    @Override
    public String getType() {
        return "Writer";
    }
}
