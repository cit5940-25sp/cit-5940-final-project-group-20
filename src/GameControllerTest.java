import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import java.util.*;



// Doesnt use assertions but instead verification because we are not
// asserting a state, but verifying interactions using mockito!!

/**
 * Unit tests for the {@link GameController} class using JUnit and Mockito.
 *
 * This test class verifies the behavior of GameController methods such as:
 * - game start logic
 * - handling valid/invalid movie inputs
 * - handling already-played movies
 * - winning condition when a player meets their goal
 *
 * We use Mockito to "mock" or fake the behavior of dependencies like GameView, GameState, MovieDatabase, etc.
 * Because they are dynamic and rely on the execution of these classes which means it would be hard to test otherwise.
 */
public class GameControllerTest {
    // Mocks for all the other classes GameController depends on
    private GameView gameView;
    private GameState gameState;
    private MovieDatabase movieDatabase;
    private List<ConnectionStrategy> strategies;
    private GameController controller;

    // Mock player and movie data used across tests
    private Player player1;
    private Player player2;
    private Movie startMovie;
    private Movie nextMovie;

    /**
     * Runs before each test method. Sets up the mocked dependencies and a fresh GameController.
     */
    @Before
    public void setUp() {
        // Creating mocks for all dependencies (Gameview, data base etc so we don't actually have to load)
        gameView = mock(GameView.class);
        gameState = mock(GameState.class);
        movieDatabase = mock(MovieDatabase.class);
        strategies = new ArrayList<>();

        // Creating mocks for all dependencies
        controller = new GameController(gameState, gameView, movieDatabase, strategies);

        // Creating mock players and movies for test data
        player1 = mock(Player.class);
        player2 = mock(Player.class);

        // Dummy movie data with actual constructor since it's not mocked
        startMovie = new Movie("Start", 2000, List.of("Actor A"), "", "", "", "", List.of("Action"));
        nextMovie = new Movie("Next", 2001, List.of("Actor B"), "", "", "", "", List.of("Action"));
    }

    /**
     * Tests that startGame() correctly initializes players and the game state.
     */
    @Test
    public void testStartGameInitializesState() {
        // Mock user input and a valid starting movie
        when(gameView.getPlayerNames()).thenReturn(new String[]{"Soojin", "Frida"});
        when(gameView.getPlayersWinConditions(any(), any())).thenReturn(mock(WinConditionStrategy.class));
        when(movieDatabase.getValidStartingMovie(any())).thenReturn(startMovie);
        when(movieDatabase.getAllMovies()).thenReturn(Set.of(startMovie));

        // Using actual real GameState to verify state setup
        GameController controller = new GameController(new GameState(), gameView, movieDatabase, strategies);
        controller.startGame();

        // Verify calls were made
        verify(gameView).getPlayerNames();
        verify(movieDatabase).getValidStartingMovie(any());
    }

    /**
     * Tests that startGame() shows a message and stops if no starting movie is found.
     */
    @Test
    public void testStartGameWithNoStartingMovie() {
        when(gameView.getPlayerNames()).thenReturn(new String[]{"Soojin", "Frida"});
        when(movieDatabase.getValidStartingMovie(any())).thenReturn(null);
        when(movieDatabase.getAllMovies()).thenReturn(Set.of());

        GameController controller = new GameController(new GameState(), gameView, movieDatabase, strategies);
        controller.startGame();

        verify(gameView).showMessageAndPause(contains("no valid starting movie"));
    }

    /**
     * Tests that if a user enters an invalid movie title (not in DB), a warning is shown.
     */
    @Test
    public void testHandleInvalidMovie() throws Exception {
        when(movieDatabase.getMovieByTitle("BadMovie")).thenReturn(null);
        callPrivateHandle(controller, "BadMovie");
        verify(gameView).showMessageAndPause(contains("not found"));
    }

    /**
     * Tests that the controller rejects a movie that has already been used in the game.
     */
    @Test
    public void testHandleAlreadyPlayedMovie() throws Exception {
        when(movieDatabase.getMovieByTitle("Start")).thenReturn(startMovie);
        when(gameState.getPlayedMovies()).thenReturn(Set.of(startMovie));
        callPrivateHandle(controller, "Start");
        verify(gameView).showMessageAndPause(contains("already been used"));
    }

    /**
     * Tests that a valid movie move that meets a winning condition triggers a win.
     */
    @Test
    public void testHandleValidMoveAndWin() throws Exception {
        // Setup: Player enters a valid, connected movie that hasn't been played
        when(movieDatabase.getMovieByTitle("Next")).thenReturn(nextMovie);
        when(gameState.getPlayedMovies()).thenReturn(Set.of());
        when(gameState.getCurrentMovie()).thenReturn(startMovie);
        when(gameState.getCurrentPlayer()).thenReturn(player1);
        when(player1.hasWon()).thenReturn(true);
        when(gameState.canUseSpecificConnection(any(), any())).thenReturn(true);

        // Creating mock connection strategy that says "Start" and "Next" are connected
        ConnectionStrategy strategy = mock(ConnectionStrategy.class);
        when(strategy.getType()).thenReturn("Genre");
        when(strategy.areConnected(any(), any())).thenReturn(true);
        when(strategy.getSharedElement(any(), any())).thenReturn("Action");
        strategies.add(strategy); // Add to the strategy list used by the controller

        // Simulate the move
        callPrivateHandle(controller, "Next");

        // Check that the winner was shown and the game ended.
        verify(gameView).showWinner(player1);
        verify(gameState).setGameOver(true);
    }

    /**
     * Helper method that uses reflection to call the private handlePlayerMove() method.
     */
    private void callPrivateHandle(GameController controller, String title) throws Exception {
        var method = GameController.class.getDeclaredMethod("handlePlayerMove", String.class);
        method.setAccessible(true);
        method.invoke(controller, title);
    }

}
