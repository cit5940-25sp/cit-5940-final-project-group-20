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

        List<String> actors1 = Arrays.asList("Actor A", "Actor B");
        List<String> genres1 = Arrays.asList("Sci-Fi", "Adventure");

        List<String> actors2 = Arrays.asList("Actor C");
        List<String> genres2 = Arrays.asList("Drama");

        movie1 = new Movie("Movie One", 2000, actors1, "Director X", "Writer X", "Cinematographer X", "Composer X", genres1);
        movie2 = new Movie("Movie Two", 2010, actors2, "Director Y", "Writer Y", "Cinematographer Y", "Composer Y", genres2);

        player1 = new Player("Zoe");
        player2 = new Player("Frida");

        List<Player> players = Arrays.asList(player1, player2);
        gameState.initializeGame(players, movie1);
    }

    @Test
    public void testInitializeGameResetsAllFields() {
        gameState.addPlayedMovie(movie2);
        gameState.incrementConnectionUsage("Actor");
        gameState.setGameOver(true);
        gameState.updateState(movie2, "Actor");

        List<Player> newPlayers = Arrays.asList(new Player("A"), new Player("B"));
        Movie newStart = new Movie("New Movie", 1999, Collections.emptyList(), "", "", "", "", Collections.emptyList());

        gameState.initializeGame(newPlayers, newStart);

        assertEquals(newStart, gameState.getCurrentMovie());
        assertEquals(1, gameState.getRound());
        assertFalse(gameState.isGameOver());
        assertTrue(gameState.getPlayedMovies().isEmpty());
        assertEquals(0, gameState.getConnectionUsage("Actor"));
        assertEquals(0, gameState.getRecentHistory().size());
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

    //This is the connection testing that we are still a bit confused about
    @Test
    public void testConnectionUsage() {
        assertTrue(gameState.canUseConnection("Genre"));
        assertEquals(0, gameState.getConnectionUsage("Genre"));

        gameState.incrementConnectionUsage("Genre");
        gameState.incrementConnectionUsage("Genre");
        gameState.incrementConnectionUsage("Genre");
        //cannot use same connection more than 3 times
        assertEquals(3, gameState.getConnectionUsage("Genre"));
        assertFalse(gameState.canUseConnection("Genre"));
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
