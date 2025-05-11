import org.junit.Before;
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class MovieDatabaseTest {

    private MovieDatabase database;

    @Before
    public void setUp() {
        database = new MovieDatabase(); // loads real CSV data
    }

    @Test
    public void testMovieExistsInDatabase() {
        // Try a title you know exists in the CSV file
        Movie movie = database.getMovieByTitle("Avatar");
        assertNotNull("Expected Avatar to exist", movie);
        assertEquals("Avatar", movie.getTitle());
    }

    @Test
    public void testGetAllMoviesHasManyEntries() {
        Collection<Movie> all = database.getAllMovies();
        assertTrue("Should contain many movies", all.size() > 1000);
    }

    @Test
    public void testGetAllGenresIsPopulated() {
        Set<String> genres = database.getAllGenres();
        assertTrue("Genres should contain 'Action'", genres.contains("Action"));
        assertTrue("Should have more than 10 genres", genres.size() > 10);
    }

    @Test
    public void testGetAllActorsWithThreshold() {
        Set<String> actors = database.getAllActors();
        assertTrue("Expected at least one actor in 10+ movies", actors.size() > 0);
    }

    @Test
    public void testGetAllDirectorsWithThreshold() {
        Set<String> directors = database.getAllDirectors();
        assertTrue("Expected at least one director in 10+ movies", directors.size() > 0);
    }

    @Test
    public void testSearchSuggestions() {
        List<Movie> suggestions = database.searchSuggestions("Ava");
        assertTrue("Should return suggestions for 'Ava'", suggestions.size() > 0);
        assertTrue(suggestions.stream().anyMatch(m -> m.getTitle().toLowerCase().contains("ava")));
    }

    @Test
    public void testGetConnectedMoviesDummy() {
        Movie avatar = database.getMovieByTitle("Avatar");
        if (avatar == null) return; // avoid NPE if missing in CSV

        ConnectionStrategy dummy = new ConnectionStrategy() {
            @Override
            public boolean areConnected(Movie a, Movie b) {
                return false;
            }

            @Override
            public String getSharedElement(Movie a, Movie b) {
                return null;
            }

            @Override
            public String getType() {
                return "None";
            }
        };

        List<Movie> connected = database.getConnectedMovies(avatar, dummy);
        assertNotNull("Should return empty list", connected);
        assertTrue(connected.isEmpty());
    }
}
