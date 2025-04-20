import java.io.Writer;
import java.util.Arrays;
import java.util.List;

public class MovieNameGame {
    public static void main(String[] args) {
        // create instances of components to run game
        GameView gameView = new GameView();
        GameState gameState = new GameState();
        MovieDatabase movieDatabase = new MovieDatabase(); // this is not populated with movie database yet
        List<ConnectionStrategy> connectionStrategies = Arrays.asList(
            new ActorConnection(),
            new DirectorConnection(),
            new WriterConnection(),
            new ComposerConnection(),
            new CinematographerConnection(),
            new GenreConnection() // Added Genre Connection 
        );


        // TO DO: Here make two WinConditions for each player!
        // Passed in from GameView
        WinConditionStrategy winConditionStrategy = new GenreWinCondition("Horror", 5); // default or example win condition

        // create the game controller instance
        GameController gameController = new GameController(
                gameState, gameView, movieDatabase, connectionStrategies, winConditionStrategy);

        // start the game
        gameController.startGame();
    }
}
