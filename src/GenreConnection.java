import java.util.HashSet;
import java.util.List;
import java.util.Set;
/**
 * A connection strategy that determines whether two movies are connected by genre.
 * <p>
 * Two movies are considered connected if they share at least one genre in common.
 */
public  class GenreConnection implements ConnectionStrategy {

    /**
     * Checks whether two movies are connected based on shared genres.
     * <p>
     * If either movie has a {@code null} genre list, the method returns {@code false}.
     *
     * @param a the first movie
     * @param b the second movie
     * @return {@code true} if the movies share at least one genre; {@code false} otherwise
     */
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

    /**
     * Returns the type of connection this strategy represents.
     *
     * @return the string "Genre"
     */
    @Override
    public String getType() {
        return "Genre";
    }
}