import java.util.List;
import java.util.Map;

/**
 * Interface that is importing movie and credit data given to us in class
 */
public interface IDataImporter {

    /**
     * Imports movie data from tmdb_5000_movies.csv
     *
     * @param file containing the movie info
     * @return a map where the key is the movie ID and the value is the movie object
     */
    public Map<Integer, Movie> importDataMovie(String file);

    /**
     * Imports credit data from tmdb_5000_credits.csv
     *
     * @param file containing the credit data
     * @param movies the existing map of movies where the credits will be added to
     * @return the map of movies that got updated with the credit info
     */
    public Map<Integer, Movie> importDataCredit(String file, Map<Integer, Movie> movies);
}


