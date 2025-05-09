import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class PlayerTest {

    private Player player1;
    private Player player2;
    private Movie movie1;
    private Movie movie2;

    @Before
    public void setUp() {
        player1 = new Player("Soojin");
        player2 = new Player("Zoe");

        List<String> dummyActors1 = Arrays.asList("actor1", "actor2");
        List<String> dummyGenres1 = Arrays.asList("Action", "Sci-Fi");

        List<String> dummyActors2 = Arrays.asList("actor3","actor4");
        List<String> dummyGenres2 = Arrays.asList("Sci-Fi", "Adventure");

        movie1 = new Movie(
                "movie1",
                1111,
                dummyActors1,
                "director",
                "writer",
                "cinematographer",
                "composer",
                dummyGenres1
        );

        movie2 = new Movie(
                "movie2",
                1111,
                dummyActors2,
                "director",
                "writer",
                "cinematographer",
                "composer",
                dummyGenres2
        );
    }

    @Test
    public void testGetName() {
        assertEquals("Soojin", player1.getName());
        assertEquals("Zoe", player2.getName());
    }

    @Test
    public void testInitialScoreIsZero() {
        assertEquals(0, player1.getScore());
        assertEquals(0, player2.getScore());
    }

    @Test
    public void testAddMovieIncrementsScore() {
        player1.addMovie(movie1);
        assertEquals(1, player1.getScore());
        assertTrue(player1.getMoviesPlayed().contains(movie1));
        assertEquals(0, player2.getScore());
        assertFalse(player2.getMoviesPlayed().contains(movie1));
    }

    @Test
    public void edgeCaseTestAddDuplicateMovieDoesNotIncrementScore() {
        player1.addMovie(movie1);
        player1.addMovie(movie1); // Duplicate
        assertEquals(1, player1.getScore());
    }
    @Test
    public void testAddTwoMovieIncrementScore() {
        player1.addMovie(movie1);
        player1.addMovie(movie2); // Duplicate
        assertEquals(2, player1.getScore());
    }

    @Test
    public void testGetMoviesPlayed() {
        player1.addMovie(movie1);
        player1.addMovie(movie2);
        List<Movie> movies = player1.getMoviesPlayed();

        assertEquals(2, movies.size());
        assertTrue(movies.contains(movie1));
        assertTrue(movies.contains(movie2));
    }

    @Test
    public void testSetAndGetWinCondition() {
        WinConditionStrategy defaultStrategy = new WinConditionStrategy() {
            @Override
            public boolean isSatisfied(Player p) {
                return true;
            }

            @Override
            public String getConditionType() {
                return "Genre";
            }
        };

        player1.setWinCondition(defaultStrategy);
        assertEquals(defaultStrategy, player1.getWinCondition());
    }

    @Test
    public void testHasWonReturnsFalseIfNoStrategySet() {
        assertFalse(player1.hasWon());
    }

    @Test
    public void testHasWonTrueAndFalse() {
        WinConditionStrategy Win = new WinConditionStrategy() {
            @Override
            public boolean isSatisfied(Player p) {
                return true;
            }

            @Override
            public String getConditionType() {
                return "AlwaysWin";
            }
        };

        player1.setWinCondition(Win);
        assertTrue(player1.hasWon());

        WinConditionStrategy Loose = new WinConditionStrategy() {
            @Override
            public boolean isSatisfied(Player p) {
                return false;
            }

            @Override
            public String getConditionType() {
                return "Loose";
            }
        };

        player2.setWinCondition(Loose);
        assertFalse(player2.hasWon());
    }

    @Test
    public void testResetScoreOnlyThoseResetted() {
        player1.addMovie(movie1);
        player2.addMovie(movie2);
        player1.resetScore();
        assertEquals(0, player1.getScore());
        assertEquals(1, player2.getScore());
    }

    @Test
    public void testGenreWinConditionSatisfied() {
        WinConditionStrategy genreWin = new GenreWinCondition("Sci-Fi", 1);
        player1.setWinCondition(genreWin);
        player1.addMovie(movie1);
        assertTrue(player1.hasWon());
    }

    @Test
    public void testGenreWinConditionNotSatisfied() {
        WinConditionStrategy genreWin = new GenreWinCondition("Sci-Fi", 2);
        player1.setWinCondition(genreWin);
        player1.addMovie(movie1);
        assertFalse(player1.hasWon());
    }
}
// test
