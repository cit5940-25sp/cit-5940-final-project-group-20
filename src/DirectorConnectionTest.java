import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class DirectorConnectionTest {

    private DirectorConnection connection;
    private Movie movie1;
    private Movie movie2;
    private Movie movie3;

    @Before
    public void setUp() {
        connection = new DirectorConnection();

        movie1 = new Movie(
                "Movie One", 2000,
                Arrays.asList("Actor A"),
                "Christopher Nolan", "Writer A", "Cinematographer A", "Composer A",
                Arrays.asList("Action")
        );

        movie2 = new Movie(
                "Movie Two", 2005,
                Arrays.asList("Actor B"),
                "CHRISTOPHER NOLAN", "Writer B", "Cinematographer B", "Composer B",
                Arrays.asList("Drama")
        );

        movie3 = new Movie(
                "Movie Three", 2010,
                Arrays.asList("Actor C"),
                "Quentin Tarantino", "Writer C", "Cinematographer C", "Composer C",
                Arrays.asList("Thriller")
        );
    }

    @Test
    public void testAreConnectedSameDirectorCaseInsensitive() {
        assertTrue(connection.areConnected(movie1, movie2));
    }

    @Test
    public void testAreNotConnectedDifferentDirector() {
        assertFalse(connection.areConnected(movie1, movie3));
    }

    @Test
    public void testAreConnectedNullDirectorA() {
        Movie movieWithNull = new Movie(
                "Movie Null A", 2020,
                Arrays.asList("Actor Z"),
                null, "Writer Z", "Cinematographer Z", "Composer Z",
                Arrays.asList("Genre")
        );
        assertFalse(connection.areConnected(movieWithNull, movie1));
    }

    @Test
    public void testAreConnectedNullDirectorB() {
        Movie movieWithNull = new Movie(
                "Movie Null B", 2021,
                Arrays.asList("Actor Q"),
                null, "Writer Q", "Cinematographer Q", "Composer Q",
                Arrays.asList("Genre")
        );
        assertFalse(connection.areConnected(movie1, movieWithNull));
    }

    @Test
    public void testGetType() {
        assertEquals("Director", connection.getType());
    }

    @Test
    public void testGetSharedElementReturnsDirectorNameIfMatched() {
        // Should return "Christopher Nolan" (caseinsensitive match)
        String shared = connection.getSharedElement(movie1, movie2);
        assertEquals("Christopher Nolan", shared);
    }

}
