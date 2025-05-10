import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;
import static org.junit.Assert.*;

/**
 * Unit tests for {@code GenreWinCondition}, which checks if a player
 * has played enough movies in a specific genre to satisfy a win condition.
 */
public class GenreWinConditionTest {

    private Player player;
    private GenreWinCondition condition;

    @Before
    public void setUp() {
        player = new Player("Soojin");
        condition = new GenreWinCondition("Action", 2);
    }

    @Test
    public void testConditionNotMetWithNoMovies() {
        assertFalse(condition.isSatisfied(player));
    }

    @Test
    public void testConditionNotMetWithOneMatchingMovie() {
        Movie movie = new Movie("Action Movie", 2010, Collections.emptyList(), "", "", "", "", Arrays.asList("Action"));
        player.addMovie(movie);
        assertFalse(condition.isSatisfied(player));
    }

    @Test
    public void testConditionMetExactly() {
        Movie m1 = new Movie("Action 1", 2010, Collections.emptyList(), "", "", "", "", Arrays.asList("Action"));
        Movie m2 = new Movie("Action 2", 2012, Collections.emptyList(), "", "", "", "", Arrays.asList("Action", "Drama"));
        player.addMovie(m1);
        player.addMovie(m2);
        assertTrue(condition.isSatisfied(player));
    }

    @Test
    public void testConditionMetAboveThreshold() {
        Movie m1 = new Movie("Action 1", 2010, Collections.emptyList(), "", "", "", "", Arrays.asList("Action"));
        Movie m2 = new Movie("Action 2", 2012, Collections.emptyList(), "", "", "", "", Arrays.asList("Action"));
        Movie m3 = new Movie("Action 3", 2014, Collections.emptyList(), "", "", "", "", Arrays.asList("Action"));
        player.addMovie(m1);
        player.addMovie(m2);
        player.addMovie(m3);
        assertTrue(condition.isSatisfied(player));
    }

    @Test
    public void testConditionIgnoresOtherGenres() {
        Movie m1 = new Movie("Comedy Movie", 2010, Collections.emptyList(), "", "", "", "", Arrays.asList("Comedy"));
        Movie m2 = new Movie("Drama Movie", 2012, Collections.emptyList(), "", "", "", "", Arrays.asList("Drama"));
        player.addMovie(m1);
        player.addMovie(m2);
        assertFalse(condition.isSatisfied(player));
    }

    @Test
    public void testGetters() {
        assertEquals("Action", condition.getGenre());
        assertEquals(2, condition.getRequiredCount());
        assertEquals("Genre", condition.getConditionType());
    }
}
