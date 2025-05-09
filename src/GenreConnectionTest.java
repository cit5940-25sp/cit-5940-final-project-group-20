import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

public class GenreConnectionTest {

    private GenreConnection connection;
    private Movie movie1;
    private Movie movie2;
    private Movie movie3;

    @Before
    public void setUp() {
        connection = new GenreConnection();

        movie1 = new Movie(
                "Movie One", 2000,
                Arrays.asList("Actor A"),
                "Director A", "Writer A", "Cinematographer A", "Composer A",
                Arrays.asList("Action", "Thriller")
        );

        movie2 = new Movie(
                "Movie Two", 2005,
                Arrays.asList("Actor B"),
                "Director B", "Writer B", "Cinematographer B", "Composer B",
                Arrays.asList("Thriller", "Drama")
        );

        movie3 = new Movie(
                "Movie Three", 2010,
                Arrays.asList("Actor C"),
                "Director C", "Writer C", "Cinematographer C", "Composer C",
                Arrays.asList("Comedy", "Romance")
        );
    }

    @Test
    public void testAreConnectedSharedGenre() {
        // Shared genre: "Thriller"
        assertTrue(connection.areConnected(movie1, movie2));
    }

    @Test
    public void testAreNotConnectedNoSharedGenre() {
        assertFalse(connection.areConnected(movie1, movie3));
    }

    @Test
    public void testAreConnectedNullGenresA() {
        Movie nullGenresA = new Movie(
                "Null Genres A", 2020,
                Arrays.asList("Actor X"),
                "Director X", "Writer X", "Cinematographer X", "Composer X",
                null
        );
        assertFalse(connection.areConnected(nullGenresA, movie1));
    }

    @Test
    public void testAreConnectedNullGenresB() {
        Movie nullGenresB = new Movie(
                "Null Genres B", 2021,
                Arrays.asList("Actor Y"),
                "Director Y", "Writer Y", "Cinematographer Y", "Composer Y",
                null
        );
        assertFalse(connection.areConnected(movie1, nullGenresB));
    }

    @Test
    public void testAreConnectedEmptyGenreList() {
        Movie emptyGenres = new Movie(
                "Empty Genres", 2022,
                Arrays.asList("Actor Z"),
                "Director Z", "Writer Z", "Cinematographer Z", "Composer Z",
                Collections.emptyList()
        );
        assertFalse(connection.areConnected(movie1, emptyGenres));
    }

    @Test
    public void testGetType() {
        assertEquals("Genre", connection.getType());
    }
}
