import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import org.mockito.stubbing.OngoingStubbing;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.util.Collections;
import java.lang.reflect.Method;
import java.util.List;
import java.lang.reflect.Field;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;


import static org.junit.Assert.*;

/**
 * Mockito is a mocking framework for Java for unit testing!
 * I downloaded to simulate user input related testing and
 * GUI related stuff
 */
public class GameViewTest {

    private GameView gameView;
    private Screen screenMock;
    private TextGraphics graphicsMock;

    @Before
    public void setup() throws IOException {
        gameView = Mockito.spy(new GameView());
        screenMock = mock(Screen.class);
        graphicsMock = mock(TextGraphics.class);

        // this is to inject mocks
        when(gameView.pollInput()).thenReturn(null);
    }

    private void injectMockKeyInputs(GameView view, List<KeyStroke> inputs) throws Exception {
        Screen mockScreen = mock(Screen.class);
        TextGraphics mockGraphics = mock(TextGraphics.class);

        Field screenField = GameView.class.getDeclaredField("screen");
        Field graphicsField = GameView.class.getDeclaredField("graphics");

        screenField.setAccessible(true);
        graphicsField.setAccessible(true);

        screenField.set(view, mockScreen);
        graphicsField.set(view, mockGraphics);

        OngoingStubbing<KeyStroke> stubbing = when(mockScreen.readInput());
        for (KeyStroke k : inputs) {
            stubbing = stubbing.thenReturn(k);
        }
    }

    @Test
    public void testGetUserInput_typingText() throws Exception {
        GameView view = new GameView();
        List<KeyStroke> keys = Arrays.asList(
                new KeyStroke('S', false, false),
                new KeyStroke('o', false, false),
                new KeyStroke('o', false, false),
                new KeyStroke('j', false, false),
                new KeyStroke('i', false, false),
                new KeyStroke('n', false, false),
                new KeyStroke(KeyType.Enter)
        );

        injectMockKeyInputs(view, keys);

        Method method = GameView.class.getDeclaredMethod("getUserInput", int.class, int.class);
        method.setAccessible(true);

        String input = (String) method.invoke(view, 0, 0);
        assertEquals("Soojin", input);
    }

    @Test
    public void testGetPlayersWinConditions_genreEasy() throws Exception {
        GameView view = new GameView();

        // Inputs: "1" (Genre), then "1" (Easy)
        List<KeyStroke> keys = Arrays.asList(
                new KeyStroke('1', false, false),
                new KeyStroke(KeyType.Enter),
                new KeyStroke('1', false, false),
                new KeyStroke(KeyType.Enter)
        );

        injectMockKeyInputs(view, keys);

        Player player = mock(Player.class);
        when(player.getName()).thenReturn("Tester");

        MovieDatabase db = mock(MovieDatabase.class);
        when(db.getAllGenres()).thenReturn(Set.of("Action"));

        WinConditionStrategy result = view.getPlayersWinConditions(player, db);
        assertTrue(result instanceof GenreWinCondition);

        GenreWinCondition genre = (GenreWinCondition) result;
        assertEquals("Action", genre.getGenre());
        assertEquals(3, genre.getRequiredCount()); // Easy = 3
    }


    @Test
    public void testPrintString() {
        gameView.printString(1, 1, "This is group 20");
        verify(gameView).printString(1, 1, "This is group 20");
    }

    @Test
    public void testShowMessageAndPause() {
        gameView.showMessageAndPause("Paused!");
        verify(gameView).showMessageAndPause("Paused!");
    }

    @Test
    public void testClearScreen() {
        gameView.clearScreen();
        verify(gameView).clearScreen();
    }

    @Test
    public void testCloseScreen() {
        gameView.closeScreen();
        verify(gameView).closeScreen();
    }

    @Test
    public void testPollInput() {
        when(gameView.pollInput()).thenReturn(null);
        assertNull(gameView.pollInput());
    }

    @Test
    public void testUpdatePlayerInput() {
        doNothing().when(gameView).updatePlayerInput(anyString());
        gameView.updatePlayerInput("Test Writing Something");
        verify(gameView).updatePlayerInput("Test Writing Something");
    }

    @Test
    public void testDisplaySuggestionsPrintsSuggestions() {
        gameView.displaySuggestions(Arrays.asList("Tiny", "Tie", "Toy"));
        verify(gameView).displaySuggestions(anyList());
    }

    @Test
    public void testDisplaySuggestionsPrintsEmptySuggestions() {
        gameView.displaySuggestions(Collections.emptyList());
        verify(gameView).displaySuggestions(Collections.emptyList());
    }

    @Test
    public void testShowWinnerDisplaysCorrectMessages() {
        Player mockPlayer = mock(Player.class);
        when(mockPlayer.getName()).thenReturn("Soojin");

        gameView.showWinner(mockPlayer);

        verify(gameView).clearScreen();
        verify(gameView).printString(4, 2, "╔══════════════════════════════════════════════════════╗");
        verify(gameView).printString(4, 3, "║                  CONGRATULATIONS!                    ║");
        verify(gameView).printString(4, 4, "╚══════════════════════════════════════════════════════╝");
        verify(gameView).printString(10, 6, "Winner: SOOJIN");
        verify(gameView).printString(10, 8, "You have won the Movie Name Game.");
        verify(gameView).printString(10, 10, "Thanks for playing!");
    }

    @Test
    public void testPrintWrappedTextWrapsCorrectly() throws Exception {
        doNothing().when(gameView).printString(anyInt(), anyInt(), anyString());

        // This is to access private methods
        Method printWrappedText = GameView.class.getDeclaredMethod(
                "printWrappedText",
                int.class, int.class, String.class, String.class, int.class
        );
        printWrappedText.setAccessible(true);

        int startCol = 0;
        int startRow = 10;
        String label = "Note: ";
        String text = "I wrote a message so that it should definitely wrap.";
        int maxWidth = 20;

        int finalRow = (int) printWrappedText.invoke(gameView, startCol, startRow, label, text, maxWidth);

        // if the printString method was called at least twice, meaning wrapped
        verify(gameView, atLeast(2)).printString(eq(startCol), anyInt(), anyString());
        assertTrue(finalRow > startRow);
    }

    @Test
    public void testDisplayGameState_printsExpectedSections() {
        GameState mockState = mock(GameState.class);
        Player mockP1 = mock(Player.class);
        Player mockP2 = mock(Player.class);
        Player mockCurrent = mock(Player.class);
        Movie mockMovie = mock(Movie.class);

        when(mockState.getRound()).thenReturn(5);
        when(mockState.getPlayers()).thenReturn(Arrays.asList(mockP1, mockP2));
        when(mockState.getCurrentPlayer()).thenReturn(mockCurrent);
        when(mockState.getCurrentMovie()).thenReturn(mockMovie);
        when(mockState.getRecentHistory()).thenReturn(Collections.emptyList());

        when(mockP1.getName()).thenReturn("Soojin");
        when(mockP2.getName()).thenReturn("Zoe");
        when(mockCurrent.getName()).thenReturn("Soojin");

        WinConditionStrategy genreCondition = mock(WinConditionStrategy.class);
        when(genreCondition.getConditionType()).thenReturn("Genre");
        when(mockP1.getWinCondition()).thenReturn(genreCondition);
        when(mockP2.getWinCondition()).thenReturn(genreCondition);

        when(mockMovie.getTitle()).thenReturn("Favorite Movie");
        when(mockMovie.getReleaseYear()).thenReturn(1999);
        when(mockMovie.getGenres()).thenReturn(List.of("Genre1", "Genre2"));
        when(mockMovie.getDirector()).thenReturn("Director A");
        when(mockMovie.getWriter()).thenReturn("Writer A");
        when(mockMovie.getCinematographer()).thenReturn("Cine A");
        when(mockMovie.getComposer()).thenReturn("Composer A");
        when(mockMovie.getActors()).thenReturn(List.of("Actor A", "Actor B", "Actor C"));

        gameView.displayGameState(mockState);

        verify(gameView).clearScreen();
        verify(gameView, atLeast(1)).printString(anyInt(), anyInt(), contains("Player 1"));
        verify(gameView, atLeast(1)).printString(anyInt(), anyInt(), contains("Player 2"));
        verify(gameView, atLeast(1)).printString(anyInt(), anyInt(), contains("Round:"));
        verify(gameView, atLeast(1)).printString(anyInt(), anyInt(), contains("make a guess"));
        verify(gameView, atLeast(1)).printString(anyInt(), anyInt(), contains("Favorite Movie"));
    }

    @Test
    public void testUpdateTimerOnly_updatesCorrectly() throws Exception {
        // add mock screen and text graphics
        Field screenField = GameView.class.getDeclaredField("screen");
        Field graphicsField = GameView.class.getDeclaredField("graphics");
        Field widthField = GameView.class.getDeclaredField("totalWidth");

        screenField.setAccessible(true);
        graphicsField.setAccessible(true);
        widthField.setAccessible(true);

        screenField.set(gameView, screenMock);
        graphicsField.set(gameView, graphicsMock);
        widthField.setInt(gameView, 80);

        // sample countdown value
        gameView.updateTimerOnly(7);

        // this is how the timer should be calculated
        String timerText = "Timer: 7s";
        int expectedCol = (80 - timerText.length()) / 2;

        verify(graphicsMock).putString(expectedCol, 22, " ".repeat(timerText.length()));
        verify(graphicsMock).putString(expectedCol, 22, timerText);
        verify(screenMock).refresh();
    }

    @Test
    public void testGetWinConditionDescriptionGenre() throws Exception {
        GameView view = new GameView();
        Player player = mock(Player.class);
        GenreWinCondition condition = new GenreWinCondition("Comedy", 3);
        when(player.getWinCondition()).thenReturn(condition);

        Method method = GameView.class.getDeclaredMethod("getWinConditionDescription", Player.class);
        method.setAccessible(true);

        String result = (String) method.invoke(view, player);
        assertEquals("Find 3 Comedy movies", result);
    }

    @Test
    public void testGetProgressGenre() throws Exception {
        GameView view = new GameView();
        Player player = mock(Player.class);
        GenreWinCondition condition = new GenreWinCondition("Comedy", 3);
        when(player.getWinCondition()).thenReturn(condition);

        Movie m1 = mock(Movie.class);
        Movie m2 = mock(Movie.class);
        Movie m3 = mock(Movie.class);
        when(m1.getGenres()).thenReturn(List.of("Comedy"));
        when(m2.getGenres()).thenReturn(List.of("Comedy"));
        when(m3.getGenres()).thenReturn(List.of("Drama"));
        when(player.getMoviesPlayed()).thenReturn(Arrays.asList(m1, m2, m3));

        Method method = GameView.class.getDeclaredMethod("getProgress", Player.class);
        method.setAccessible(true);

        String result = (String) method.invoke(view, player);
        assertEquals("2/3", result);
    }

    @Test
    public void testCenterInBoxTruncatesIfTooLong() throws Exception {
        GameView view = new GameView();
        Method method = GameView.class.getDeclaredMethod("centerInBox", String.class, int.class);
        method.setAccessible(true);

        String result = (String) method.invoke(view, "Soojin likes to write very long", 10);
        assertEquals("Soojin lik", result);
    }

    @Test
    public void testCenterInBoxCentersShortText() throws Exception {
        GameView view = new GameView();
        Method method = GameView.class.getDeclaredMethod("centerInBox", String.class, int.class);
        method.setAccessible(true);

        String result = (String) method.invoke(view, "Soojin", 10);
        assertEquals("  Soojin  ", result);
    }

    @Test
    public void testPromptRestartYesInputReturnsTrue() throws Exception {
        GameView view = new GameView();

        List<KeyStroke> keys = Arrays.asList(
                new KeyStroke('y', false, false),
                new KeyStroke(KeyType.Enter)
        );

        injectMockKeyInputs(view, keys);

        boolean result = view.promptRestart();
        assertTrue(result);
    }

    @Test
    public void testPromptRestartNoInputReturnsFalse() throws Exception {
        GameView view = new GameView();

        List<KeyStroke> keys = Arrays.asList(
                new KeyStroke('n', false, false),
                new KeyStroke(KeyType.Enter)
        );

        injectMockKeyInputs(view, keys);

        boolean result = view.promptRestart();
        assertFalse(result);
    }



    @Test
    public void testShowThankYouMessage() {
        gameView.showThankYouMessage();
        verify(gameView).showThankYouMessage();
    }
}
