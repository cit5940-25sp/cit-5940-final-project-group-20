import java.util.Scanner;

public class GameView {
    private Scanner scanner;

    // construct to initialize the scanner for user input
    public GameView() {
        this.scanner = new Scanner(System.in);
    }

    // Show the start screen (show basic instruction of the game and instructions)
    public String[] getPlayerNames() {
        System.out.println("Hey there, welcome to Group 20's Movie Name Game");
        System.out.println("Please enter the names of the two players.");

        // get player 1's name
        System.out.print("Enter name for Player 1: ");
        String player1Name = scanner.nextLine();

        // get player 2's name
        System.out.print("Enter name for Player 2: ");
        String player2Name = scanner.nextLine();

        // return both names as an array
        return new String[] { player1Name, player2Name };
    }

    public WinConditionStrategy getPlayersWinConditions() {
        System.out.println("You have 3 win conditions to choose from! How would you like to beat your opponent?");
        System.out.println("1. Genre");
        System.out.println("2. Actor ");
        System.out.println("3. Director");

        int winConditionChoice = scanner.nextInt(); // may need more validation for user input

        scanner.nextLine(); // move to nextLine

        // OPTIONAL IMPLEMENTATION: Choosing 3 difference levels of difficulties
        System.out.println("How difficult do you want to make the Battle?! (easy, medium, hard)");
        
        // clean user input 
        // needs more validation
        String difficulty = scanner.nextLine().toLowerCase().trim();

        int count; // sets the count for the WinCondition!

        if (difficulty.equals("easy")) {
            count = 3;
        } else if (difficulty.equals("hard")) {
            count = 7;
        } else {
            count = 5; // default to medium
        }

        // TO DO: Add real logic later--check with others
        return new GenreWinCondition("Horror", count); // default for now
    }

    // Display current game state to the user
    public void displayGameState(GameState gameState) {
        Player currentPlayer = gameState.getCurrentPlayer();
        Movie currentMovie = gameState.getCurrentMovie();
        int round = gameState.getRound();
        System.out.println("\n--- Game State ---");
        System.out.println("Round: " + round);
        System.out.println("Current Player: " + currentPlayer.getName());
        System.out.println("Current Movie: " + currentMovie.getTitle());
        System.out.println("Movies Played: " + gameState.getPlayedMovies());
        System.out.println("Score: " + currentPlayer.getScore());
        System.out.println("-------------------");
    }

    // Ask user for movie name input
    public String getPlayerInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine(); // read and return user's input
    }

    // Show any error messages (like invalid movie, wrong connection, etc.)
    public void showError(String message) {
        System.out.println("\nError: " + message);
    }

    // Show the winner (called when someone meets the win condition)
    public void showWinner(Player player) {
        System.out.println("\nCongratulations, " + player.getName() + "!");
        System.out.println("You have won the game!");
    }

    // Display time left on screen (could be seconds or countdown)
    public void showTimer(int secondsRemaining) {
        System.out.println("Time remaining: " + secondsRemaining + " seconds");
    }
}
