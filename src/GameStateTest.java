import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class GameStateTest {

    private GameState gameState;
    private Player player1;
    private Player player2;
    private Movie movie1;
    private Movie movie2;

    @Before
    public void setUp() {
        gameState = new GameState();

        List<String> actors1 = Arrays.asList("Tom Hanks", "Actor B");
        List<String> genres1 = Arrays.asList("Sci-Fi", "Adventure");

        List<String> actors2 = Arrays.asList("Actor C", "Tom Hanks");
        List<String> genres2 = Arrays.asList("Drama", "Adventure");

        movie1 = new Movie("Movie One", 2000, actors1, "Director X", "Writer X", "Cinematographer X", "Composer X", genres1);
        movie2 = new Movie("Movie Two", 2010, actors2, "Director Y", "Writer Y", "Cinematographer Y", "Composer Y", genres2);

        player1 = new Player("Zoe");
        player2 = new Player("Frida");

        List<Player> players = Arrays.asList(player1, player2);
        gameState.initializeGame(players, movie1);
    }

    // Testing that we limit a SPECIFIC actor to only be used 3 times!
    @Test
    public void testSpecificActorConnection() {
        String connectionType = "Actor";
        String actor = "Tom Hanks";
        assertTrue(gameState.canUseSpecificConnection(connectionType, actor));
        // Increment/Use 3 times
        for (int i = 0; i < 3; i++) {
            gameState.incrementSpecificConnectionUsage(connectionType, actor);
        }
        // should now be blocked
        assertFalse(gameState.canUseSpecificConnection("Actor", "Tom Hanks"));
    }

    // Testing that we limit a SPECIFIC Director to only be used 3 times!
    @Test
    public void testSpecificDirectorConnection() {
        String connectionType = "Director";
        String director = "Christopher Nolan";
        assertTrue(gameState.canUseSpecificConnection(connectionType, director));
        // Increment/Use 3 times
        for (int i = 0; i < 3; i++) {
            gameState.incrementSpecificConnectionUsage(connectionType, director);
        }
        // should now be blocked
        assertFalse(gameState.canUseSpecificConnection(connectionType, director));
    }

    // Testing that we limit a SPECIFIC Genre to only be used 3 times!
    @Test
    public void testSpecificGenreUsageConnection() {
        String connectionType = "Genre";
        String genreValue = "Action";
        assertTrue(gameState.canUseSpecificConnection(connectionType, genreValue));
        // Increment/Use 3 times
        for (int i = 0; i < 3; i++) {
            gameState.incrementSpecificConnectionUsage(connectionType, genreValue);
        }
        // should now be blocked
        assertFalse(gameState.canUseSpecificConnection(connectionType, genreValue));
    }


    @Test
    public void testInitializeGameResetsAllFields() {
        gameState.addPlayedMovie(movie2);
        gameState.incrementSpecificConnectionUsage("Genre", "Drama");
        gameState.setGameOver(true);
        gameState.updateState(movie2, "Genre");

        List<Player> newPlayers = Arrays.asList(new Player("A"), new Player("B"));
        Movie newStart = new Movie("New Movie", 1999, Collections.emptyList(), "", "", "", "", Collections.emptyList());

        gameState.initializeGame(newPlayers, newStart);

        assertEquals(newStart, gameState.getCurrentMovie());
        assertEquals(1, gameState.getRound());
        assertFalse(gameState.isGameOver());
        assertTrue(gameState.getPlayedMovies().isEmpty());
        assertEquals(0, gameState.getRecentHistory().size());
        assertTrue(gameState.canUseSpecificConnection("Genre", "Drama"));
    }


    @Test
    public void testSwitchPlayer() {
        assertEquals(player1, gameState.getCurrentPlayer());
        gameState.switchPlayer();
        assertEquals(player2, gameState.getCurrentPlayer());
        gameState.switchPlayer();
        assertEquals(player1, gameState.getCurrentPlayer()); // wraps around
    }

    @Test
    public void testIncrementRound() {
        int initialRound = gameState.getRound();
        gameState.incrementRound();
        assertEquals(initialRound + 1, gameState.getRound());
    }

    @Test
    public void testAddPlayedMovie() {
        gameState.addPlayedMovie(movie1);
        assertTrue(gameState.getPlayedMovies().contains(movie1));
    }



    @Test
    public void testUpdateStateAndHistory() {
        gameState.updateState(movie2, "Actor");
        assertEquals(movie2, gameState.getCurrentMovie());
        assertEquals(2, gameState.getRound());

        List<String> history = gameState.getRecentHistory();
        assertEquals(1, history.size());
        assertTrue(history.get(0).contains("Movie One → Movie Two [Actor]"));
    }

    @Test
    public void testConnectionHistoryLimit() {
        for (int i = 1; i <= 6; i++) {
            Movie next = new Movie("Movie " + i, 2000 + i, Collections.singletonList("A"), "D", "W", "C", "M", Arrays.asList("Genre"));
            gameState.updateState(next, "Actor");
        }
        List<String> history = gameState.getRecentHistory();
        assertEquals(5, history.size()); // max 5 recent entries
    }

    @Test
    public void testSetAndCheckGameOver() {
        assertFalse(gameState.isGameOver());
        gameState.setGameOver(true);
        assertTrue(gameState.isGameOver());
    }

    @Test
    public void testHasPlayerWon() {
        WinConditionStrategy alwaysWin = new WinConditionStrategy() {
            public boolean isSatisfied(Player p) {
                return true;
            }
            public String getConditionType() {
                return "Genre";
            }
        };
        player1.setWinCondition(alwaysWin);
        assertTrue(gameState.hasPlayerWon(player1));
    }

    @Test
    public void testGetWinner() {
        assertNull(gameState.getWinner());
        WinConditionStrategy genreWin = new WinConditionStrategy() {
            public boolean isSatisfied(Player p) {
                return true;
            }
            public String getConditionType() {
                return "Genre";
            }
        };
        player2.setWinCondition(genreWin);
        assertEquals(player2, gameState.getWinner());
    }
}
