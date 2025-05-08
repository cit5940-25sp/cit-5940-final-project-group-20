import java.util.Arrays;
import java.util.List;

/**
 * This is the entry point for the Movie Name Game.
 * <p>
 * This class initializes the game state, user interface (GameView),
 * movie database, and all connection strategies. It manages the main game loop,
 * allowing the user to play/simulate the game multiple rounds.
 */
public class MovieNameGame {
    /**
     * Main method to launch the Movie Name Game.
     * <p>
     * It sets up all required components and handles the control flow
     * of the game, including error handling and restarting the game upon user input.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        GameView view = new GameView();
        try {
            boolean playAgain;
            do {
                try {
                    GameState gameState = new GameState();
                    MovieDatabase movieDatabase = new MovieDatabase();
                    List<ConnectionStrategy> connectionStrategies = Arrays.asList(
                        new ActorConnection(),
                        new DirectorConnection(),
                        new WriterConnection(),
                        new ComposerConnection(),
                        new CinematographerConnection(),
                        new GenreConnection()
                    );

                    GameController gameController = new GameController(
                            gameState, view, movieDatabase, connectionStrategies);

                    gameController.startGame();
                } catch (Exception e) {
                    e.printStackTrace();  // if error
                    break; // Exit the loop so we don't restart in a broken state
                }

                playAgain = view.promptRestart();
            } while (playAgain);
        } finally {
            view.closeScreen();  // restores terminal
        }
    }
}

