import java.util.Arrays;
import java.util.List;


public class MovieNameGame {
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

