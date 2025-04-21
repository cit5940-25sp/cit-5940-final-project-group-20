import java.util.HashSet;
import java.util.List;
import java.util.Set;

public  class GenreConnection implements ConnectionStrategy {

    // Assuming that one movie has multiple genres
    @Override
    public boolean areConnected(Movie a, Movie b) {
        List<String> genresA = a.getGenres();
        List<String> genresB = b.getGenres();

        if (genresA == null || genresB == null) {
            return false;
        }

        Set<String> genreSetB = new HashSet<>(genresB);
        for (String genre : genresA) {
            if (genreSetB.contains(genre)) {
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