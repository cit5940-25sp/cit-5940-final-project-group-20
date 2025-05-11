import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class MovieTest {

    private Movie movie;
    private List<String> actors;
    private List<String> genres;

    @Before
    public void setUp() {
        actors = Arrays.asList("Actor One", "Actor Two");
        genres = Arrays.asList("Drama", "Adventure");

        movie = new Movie(
                "The Great Escape",
                1963,
                actors,
                "John Sturges",
                "James Clavell",
                "Daniel L. Fapp",
                "Elmer Bernstein",
                genres
        );
    }

    @Test
    public void testConstructorAndGetters() {
        assertEquals("The Great Escape", movie.getTitle());
        assertEquals(1963, movie.getReleaseYear());
        assertEquals(actors, movie.getActors());
        assertEquals(genres, movie.getGenres());
        assertEquals("John Sturges", movie.getDirector());
        assertEquals("James Clavell", movie.getWriter());
        assertEquals("Daniel L. Fapp", movie.getCinematographer());
        assertEquals("Elmer Bernstein", movie.getComposer());
    }

    @Test
    public void testSetActors() {
        List<String> newActors = Arrays.asList("New Actor");
        movie.setActors(newActors);
        assertEquals(newActors, movie.getActors());
    }

    @Test
    public void testSetDirector() {
        movie.setDirector("New Director");
        assertEquals("New Director", movie.getDirector());
    }

    @Test
    public void testSetWriter() {
        movie.setWriter("New Writer");
        assertEquals("New Writer", movie.getWriter());
    }

    @Test
    public void testSetCinematographer() {
        movie.setCinematographer("New Cinematographer");
        assertEquals("New Cinematographer", movie.getCinematographer());
    }

    @Test
    public void testSetComposer() {
        movie.setComposer("New Composer");
        assertEquals("New Composer", movie.getComposer());
    }

    @Test
    public void testGetSearchableTitleReturnsExactTitle() {
        assertEquals("The Great Escape", movie.getSearchableTitle());
    }

    @Test
    public void testToStringReturnsTitle() {
        assertEquals("The Great Escape", movie.toString());
    }
}
