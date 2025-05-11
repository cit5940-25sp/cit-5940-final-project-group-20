import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class WriterConnectionTest {

    private WriterConnection connection;
    private Movie movie1;
    private Movie movie2;
    private Movie movie3;

    @Before
    public void setUp() {
        connection = new WriterConnection();

        movie1 = new Movie(
                "Movie One", 2030,
                Arrays.asList("Actor A"),
                "Director A", "Soojin", "Cinematographer A", "Composer A",
                Arrays.asList("Horror", "Thriller")
        );

        movie2 = new Movie(
                "Movie Two", 2030,
                Arrays.asList("Actor B"),
                "Director B", "Soojin", "Cinematographer B", "Composer B",
                Arrays.asList("Horror")
        );

        movie3 = new Movie(
                "Movie Three", 2030,
                Arrays.asList("Actor C"),
                "Director C", "Zoe", "Cinematographer C", "Composer C",
                Arrays.asList("Drama")
        );
    }

    @Test
    public void testAreConnectedSameWriter() {
        assertTrue(connection.areConnected(movie1, movie2));
    }

    @Test
    public void testAreNotConnectedDifferentWriters() {
        assertFalse(connection.areConnected(movie1, movie3));
    }

    @Test
    public void testAreConnectedWriterNullA() {
        Movie movieNullA = new Movie(
                "Null A", 2020,
                Arrays.asList("Actor X"),
                "Director X", null, "Cinematographer X", "Composer X",
                Arrays.asList("Genre")
        );
        assertFalse(connection.areConnected(movieNullA, movie1));
    }

    @Test
    public void testAreConnectedWriterNullB() {
        Movie movieNullB = new Movie(
                "Null B", 2021,
                Arrays.asList("Actor Y"),
                "Director Y", null, "Cinematographer Y", "Composer Y",
                Arrays.asList("Genre")
        );
        assertFalse(connection.areConnected(movie1, movieNullB));
    }

    @Test
    public void testGetType() {
        assertEquals("Writer", connection.getType());
    }

    @Test
    public void testGetSharedElementReturnsWriterIfMatched() {
        // Shared writer: "Soojin"
        String shared = connection.getSharedElement(movie1, movie2);
        assertEquals("Soojin", shared);
    }


}
