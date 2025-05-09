import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class CinematographerConnectionTest {

    private CinematographerConnection connection;
    private Movie movie1;
    private Movie movie2;
    private Movie movie3;

    @Before
    public void setUp() {
        connection = new CinematographerConnection();

        movie1 = new Movie(
                "Movie One", 2001,
                Arrays.asList("Actor A"),
                "Director A", "Writer A", "Cinematographer X", "Composer A",
                Arrays.asList("Action")
        );

        movie2 = new Movie(
                "Movie Two", 2005,
                Arrays.asList("Actor B"),
                "Director B", "Writer B", "Cinematographer X", "Composer B",
                Arrays.asList("Drama")
        );

        movie3 = new Movie(
                "Movie Three", 2010,
                Arrays.asList("Actor C"),
                "Director C", "Writer C", "Cinematographer Y", "Composer C",
                Arrays.asList("Thriller")
        );
    }

    @Test
    public void testAreConnectedSameCinematographer() {
        assertTrue(connection.areConnected(movie1, movie2));
    }

    @Test
    public void testAreNotConnectedDifferentCinematographer() {
        assertFalse(connection.areConnected(movie1, movie3));
    }

    @Test
    public void testAreConnectedNullCinematographerA() {
        Movie movieNullA = new Movie("Null A", 2020,
                Arrays.asList("A"), "D", "W", null, "C", Arrays.asList("Genre"));
        assertFalse(connection.areConnected(movieNullA, movie1));
    }

    @Test
    public void testAreConnectedNullCinematographerB() {
        Movie movieNullB = new Movie("Null B", 2020,
                Arrays.asList("A"), "D", "W", null, "C", Arrays.asList("Genre"));
        assertFalse(connection.areConnected(movie1, movieNullB));
    }

    @Test
    public void testGetType() {
        assertEquals("Cinematographer", connection.getType());
    }
}
