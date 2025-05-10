import org.junit.Before;
import org.junit.Test;
import java.util.Collections;
import static org.junit.Assert.*;

/**
 * Unit tests for {@code DirectorWinCondition}, which checks if a player
 * has played enough movies by a specific director to satisfy a win condition.
 */
public class DirectorWinConditionTest {

    private Player player;
    private DirectorWinCondition condition;

    @Before
    public void setUp() {
        player = new Player("Frida");
        condition = new DirectorWinCondition("Christopher Nolan", 2);
    }

    @Test
    public void testConditionNotMetWithNoMovies() {
        assertFalse(condition.isSatisfied(player));
    }

    @Test
    public void testConditionNotMetWithOneMatchingMovie() {
        Movie movie = new Movie("Inception", 2010, Collections.emptyList(),
                "Christopher Nolan", "", "", "", Collections.emptyList());
        player.addMovie(movie);
        // still false as only 1 connection
        assertFalse(condition.isSatisfied(player));
    }

    @Test
    public void testConditionMetExactly() {
        Movie m1 = new Movie("Inception", 2010, Collections.emptyList(),
                "Christopher Nolan", "", "", "", Collections.emptyList());
        Movie m2 = new Movie("Interstellar", 2014, Collections.emptyList(),
                "Christopher Nolan", "", "", "", Collections.emptyList());
        player.addMovie(m1);
        player.addMovie(m2);
        // 2nd connection made, so should be true now!
        assertTrue(condition.isSatisfied(player));
    }

    @Test
    public void testConditionMetAboveThreshold() {
        Movie m1 = new Movie("Inception", 2010, Collections.emptyList(),
                "Christopher Nolan", "", "", "", Collections.emptyList());
        Movie m2 = new Movie("Interstellar", 2014, Collections.emptyList(),
                "Christopher Nolan", "", "", "", Collections.emptyList());
        Movie m3 = new Movie("Tenet", 2020, Collections.emptyList(),
                "Christopher Nolan", "", "", "", Collections.emptyList());
        player.addMovie(m1);
        player.addMovie(m2);
        player.addMovie(m3);
        // still true as > 2 connections
        assertTrue(condition.isSatisfied(player));
    }

    @Test
    public void testConditionOtherDirectors() {
        Movie m1 = new Movie("Movie 1", 2000, Collections.emptyList(),
                "Quentin Tarantino", "", "", "", Collections.emptyList());
        Movie m2 = new Movie("Movie 2", 2001, Collections.emptyList(),
                "Martin Scorsese", "", "", "", Collections.emptyList());
        player.addMovie(m1);
        player.addMovie(m2);
        assertFalse(condition.isSatisfied(player));
    }

    @Test
    public void testGetters() {
        assertEquals("Christopher Nolan", condition.getDirector());
        assertEquals(2, condition.getRequiredCount());
        assertEquals("Director", condition.getConditionType());
    }
}
