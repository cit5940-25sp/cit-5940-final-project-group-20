import java.util.HashSet;
import java.util.Set;

public  class GenreConnection implements ConnectionStrategy {

    // Assuming that one movie has multiple genres
    @Override
    public boolean areConnected(Movie a, Movie b) {
        Set<String> genresB = new HashSet<>(b.getGenres());

        for (String genreA : a.getGenres()) {
            if (genresB.contains(genreA)) {
                return true;
            }
        }

        return false; // not connected by genre
    }

    @Override
    public String getType() {
        return "Genre";
    } 
}
