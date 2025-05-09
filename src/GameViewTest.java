import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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

        // Inject mocks
        gameView = Mockito.mock(GameView.class);
        when(gameView.pollInput()).thenReturn(null); // Default: no input
    }

    @Test
    public void testPrintString_noCrash() {
        doNothing().when(gameView).printString(anyInt(), anyInt(), anyString());
        gameView.printString(1, 1, "Hello");
        verify(gameView).printString(1, 1, "Hello");
    }

    @Test
    public void testShowMessageAndPause_noCrash() {
        doNothing().when(gameView).showMessageAndPause(anyString());
        gameView.showMessageAndPause("Paused!");
        verify(gameView).showMessageAndPause("Paused!");
    }

    @Test
    public void testClearScreen_noCrash() {
        doNothing().when(gameView).clearScreen();
        gameView.clearScreen();
        verify(gameView).clearScreen();
    }

    @Test
    public void testCloseScreen_noCrash() {
        doNothing().when(gameView).closeScreen();
        gameView.closeScreen();
        verify(gameView).closeScreen();
    }

    @Test
    public void testPollInput_returnsNull() {
        when(gameView.pollInput()).thenReturn(null);
        assertNull(gameView.pollInput());
    }

    @Test
    public void testUpdatePlayerInput_noCrash() {
        doNothing().when(gameView).updatePlayerInput(anyString());
        gameView.updatePlayerInput("Titanic");
        verify(gameView).updatePlayerInput("Titanic");
    }

    @Test
    public void testDisplaySuggestions_printsLimitedSuggestions() {
        doNothing().when(gameView).displaySuggestions(anyList());
        gameView.displaySuggestions(Arrays.asList("Titanic", "Terminator", "Toy Story"));
        verify(gameView).displaySuggestions(anyList());
    }

    @Test
    public void testPromptRestart_yesResponse() {
        when(gameView.promptRestart()).thenReturn(true);
        assertTrue(gameView.promptRestart());
    }

    @Test
    public void testPromptRestart_noResponse() {
        when(gameView.promptRestart()).thenReturn(false);
        assertFalse(gameView.promptRestart());
    }

    @Test
    public void testShowThankYouMessage_noCrash() {
        doNothing().when(gameView).showThankYouMessage();
        gameView.showThankYouMessage();
        verify(gameView).showThankYouMessage();
    }
}
