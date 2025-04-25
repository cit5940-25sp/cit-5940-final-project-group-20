import static org.junit.Assert.*;
import java.util.Arrays;
import org.junit.Test;

public class WinConditionTest {

    @Test
    public void testGenreWinConditionSatisfied() {
        // Setup dummy player
        Player player = new Player("Test Player");

        // Create dummy movies
        Movie movie1 = new Movie("Movie 1", 2020, null, null, null, null, null, Arrays.asList("Action", "Comedy"));
        Movie movie2 = new Movie("Movie 2", 2021, null, null, null, null, null, Arrays.asList("Action"));
        Movie movie3 = new Movie("Movie 3", 2022, null, null, null, null, null, Arrays.asList("Action", "Drama"));

        // Setup WinCondition: needs 3 Action movies
        WinConditionStrategy winCondition = new GenreWinCondition("Action", 3);

        // Add movies to player
        player.addMovie(movie1);
        player.addMovie(movie2);
        player.addMovie(movie3);

        // Test if win condition is satisfied
        assertTrue(winCondition.isSatisfied(player));
    }

    @Test
    public void testGenreWinConditionNotSatisfied() {
        // Setup dummy player
        Player player = new Player("Test Player");

        // Create dummy movies
        Movie movie1 = new Movie("Movie 1", 2020, null, null, null, null, null, Arrays.asList("Comedy"));
        Movie movie2 = new Movie("Movie 2", 2021, null, null, null, null, null, Arrays.asList("Drama"));

        // Setup WinCondition: needs 3 Action movies
        WinConditionStrategy winCondition = new GenreWinCondition("Action", 3);

        // Add movies to player
        player.addMovie(movie1);
        player.addMovie(movie2);

        // Test if win condition is NOT satisfied
        assertFalse(winCondition.isSatisfied(player));
    }
}
