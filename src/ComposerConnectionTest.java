import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class ComposerConnectionTest {

    private ComposerConnection connection;
    private Movie movie1;
    private Movie movie2;
    private Movie movie3;

    @Before
    public void setUp() {
        connection = new ComposerConnection();

        movie1 = new Movie(
                "Movie One", 2000,
                Arrays.asList("Actor A"),
                "Director A", "Writer A", "Cinematographer A", "Composer X",
                Arrays.asList("Action")
        );

        movie2 = new Movie(
                "Movie Two", 2005,
                Arrays.asList("Actor B"),
                "Director B", "Writer B", "Cinematographer B", "Composer X",
                Arrays.asList("Drama")
        );

        movie3 = new Movie(
                "Movie Three", 2010,
                Arrays.asList("Actor C"),
                "Director C", "Writer C", "Cinematographer C", "Composer Y",
                Arrays.asList("Thriller")
        );
    }

    @Test
    public void testAreConnectedSameComposer() {
        assertTrue(connection.areConnected(movie1, movie2));
    }

    @Test
    public void testAreNotConnectedDifferentComposer() {
        assertFalse(connection.areConnected(movie1, movie3));
    }

    @Test
    public void testAreConnectedNullComposerA() {
        Movie nullComposerA = new Movie(
                "Null Composer A", 2020,
                Arrays.asList("Actor Z"),
                "Director Z", "Writer Z", "Cinematographer Z", null,
                Arrays.asList("Genre")
        );

        assertFalse(connection.areConnected(nullComposerA, movie1));
    }

    @Test
    public void testAreConnectedNullComposerB() {
        Movie nullComposerB = new Movie(
                "Null Composer B", 2021,
                Arrays.asList("Actor Q"),
                "Director Q", "Writer Q", "Cinematographer Q", null,
                Arrays.asList("Genre")
        );

        assertFalse(connection.areConnected(movie1, nullComposerB));
    }

    @Test
    public void testGetType() {
        assertEquals("Composer", connection.getType());
    }
}
