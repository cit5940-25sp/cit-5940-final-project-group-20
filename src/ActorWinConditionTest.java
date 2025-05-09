import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;
import static org.junit.Assert.*;

public class ActorWinConditionTest {

    private Player player;
    private ActorWinCondition condition;

    @Before
    public void setUp() {
        player = new Player("Harry Smith");
        condition = new ActorWinCondition("Tom Hanks", 2);
    }

    @Test
    public void testConditionNotMetWithNoMovies() {
        assertFalse(condition.isSatisfied(player));
    }

    @Test
    public void testConditionNotMetWithOneMatchingMovie() {
        Movie movie = new Movie("Movie 1", 2000, Arrays.asList("Tom Hanks"), "", "", "", "", Collections.emptyList());
        player.addMovie(movie);
        assertFalse(condition.isSatisfied(player));
    }

    @Test
    public void testConditionMetExactly() {
        Movie m1 = new Movie("Movie 1", 2000, Arrays.asList("Tom Hanks"), "", "", "", "", Collections.emptyList());
        Movie m2 = new Movie("Movie 2", 2001, Arrays.asList("Tom Hanks", "Someone Else"), "", "", "", "", Collections.emptyList());
        player.addMovie(m1);
        player.addMovie(m2);
        assertTrue(condition.isSatisfied(player));
    }

    @Test
    public void testConditionMetAboveThreshold() {
        Movie m1 = new Movie("Movie 1", 2000, Arrays.asList("Tom Hanks"), "", "", "", "", Collections.emptyList());
        Movie m2 = new Movie("Movie 2", 2001, Arrays.asList("Tom Hanks"), "", "", "", "", Collections.emptyList());
        Movie m3 = new Movie("Movie 3", 2002, Arrays.asList("Tom Hanks"), "", "", "", "", Collections.emptyList());
        player.addMovie(m1);
        player.addMovie(m2);
        player.addMovie(m3);
        assertTrue(condition.isSatisfied(player));
    }

    @Test
    public void testConditionIgnoresOtherActors() {
        Movie m1 = new Movie("Movie 1", 2000, Arrays.asList("Some other irrelevant Actor"), "", "", "", "", Collections.emptyList());
        Movie m2 = new Movie("Movie 2", 2001, Arrays.asList("Another Actor"), "", "", "", "", Collections.emptyList());
        player.addMovie(m1);
        player.addMovie(m2);
        assertFalse(condition.isSatisfied(player));
    }

    @Test
    public void testGetters() {
        assertEquals("Tom Hanks", condition.getActor());
        assertEquals(2, condition.getRequiredCount());
        assertEquals("Actor", condition.getConditionType());
    }
}
