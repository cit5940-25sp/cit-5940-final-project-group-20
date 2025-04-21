public class GenreConnection implements ConnectionStrategy {
    @Override
    public boolean areConnected(Movie a, Movie b) {
        for (String genreA : a.getGenres()) {
            if (b.getGenres().contains(genreA)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getType() {
        return "Genre";
    }
}
