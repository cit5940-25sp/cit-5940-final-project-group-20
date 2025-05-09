import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.screen.TerminalScreen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * VIEW PART OF MVC model
 * Manages the text-based user interface for the Movie Name Game using the Lanterna library.
 * Displays player information, game state, timer, suggestions, and handles user input.
 * All of those that has to do with "interfacing with user via text"
 */
public class GameView {
    private Screen screen;
    private TextGraphics graphics;

    private int totalWidth;
    private int halfWidth;
    private int thirdWidth;

    /**
     * Constructs the game view and initializes the terminal screen and graphics.
     */
    public GameView() {
        try {
            Terminal terminal = new DefaultTerminalFactory()
                    .setInitialTerminalSize(new TerminalSize(120, 40))  // columns x rows
                    .createTerminal();

            screen = new TerminalScreen(terminal);
            screen.startScreen();
            graphics = screen.newTextGraphics();

            TerminalSize size = screen.getTerminalSize();
            this.totalWidth = size.getColumns();
            this.halfWidth = totalWidth / 2;
            this.thirdWidth = totalWidth / 3;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prints a string at the specified column and row in the terminal.
     * Automatically refreshes the screen after printing.
     *
     * @param col  the column at which to begin printing
     * @param row  the row at which to begin printing
     * @param text the text string to print
     */
    private void printString(int col, int row, String text) {
        graphics.putString(col, row, text);
        try {
            screen.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays a short message near the bottom of the screen and pauses.
     *
     * @param message the message to display
     */
    public void showMessageAndPause(String message) {
        try {
            graphics.putString(0, 24, "                                                        "); // Clear old
            graphics.putString(0, 24, message); // Show message at row 15
            screen.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Clears the screen.
     */
    public void clearScreen() {
        try {
            screen.clear();
            screen.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the terminal screen and releases resources.
     */
    public void closeScreen() {
        try {
            screen.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Non-blocking poll for user keyboard input.
     *
     * @return the KeyStroke pressed or {@code null} if no input
     */
    public KeyStroke pollInput() {
        try {
            return screen.pollInput();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Updates the player input line with the current text.
     *
     * @param currentInput the input typed by the player
     */
    public void updatePlayerInput(String currentInput) {
        // Overwrite the input line
        printString(10, 27, "                                      "); // Clear old input
        printString(10, 27, currentInput); // Print new input
    }

    /**
     * Displays the winning player's name and a win message.
     *
     * @param player the player who won
     */
    public void showWinner(Player player) {
        clearScreen();

        printString(4, 2,  "╔══════════════════════════════════════════════════════╗");
        printString(4, 3,  "║                  CONGRATULATIONS!                    ║");
        printString(4, 4,  "╚══════════════════════════════════════════════════════╝");

        String nameLine = "Winner: " + player.getName().toUpperCase();
        printString(10, 6, nameLine);

        printString(10, 8, "You have won the Movie Name Game.");
        printString(10, 10, "Thanks for playing!");
    }

    /**
     * Prints a long string wrapped across multiple lines
     * constrained to a specified maximum width.
     * This effor was done for visibility/usability for the players
     *
     * @param startCol  the column to start printing from
     * @param startRow  the row to start printing from
     * @param label     the label prefix to prepend to the text
     * @param text      the full text to wrap
     * @param maxWidth  the maximum number of characters per line
     * @return the new row number after the wrapped text
     */
    private int printWrappedText(int startCol, int startRow, String label, String text, int maxWidth) {
        String fullText = label + text;
        int currentRow = startRow;
        while (fullText.length() > maxWidth) {
            printString(startCol, currentRow++, fullText.substring(0, maxWidth));
            fullText = fullText.substring(maxWidth);
        }
        printString(startCol, currentRow++, fullText);
        return currentRow; // Return the new row after printing
    }

    /**
     * Renders the  game interface, including player information, current movie details,
     * round number, recent connections, and the user input prompt.
     * <p>
     * This method is responsible for visually presenting the entire state of the game
     * each turn and is typically called at the start of a player's turn.
     *
     * @param gameState the current state of the game containing player data, the current movie,
     *                  round count, and connection history
     */
    public void displayGameState(GameState gameState) {
        clearScreen();

        int padding = 2;
        int round = gameState.getRound();

        Player player1 = gameState.getPlayers().get(0);
        Player player2 = gameState.getPlayers().get(1);

        String separatorLine = "=".repeat(halfWidth) + "|" + "=".repeat(halfWidth);

        // === Top Half: Players and Progress ===
        printString(0, 0, separatorLine);

        String player1Header = "Player 1";
        String player2Header = "Player 2";
        int player1StartCol = (halfWidth - player1Header.length()) / 2;
        int player2StartCol = halfWidth + (halfWidth - player2Header.length()) / 2;

        printString(player1StartCol, 1, player1Header);
        printString(player2StartCol, 1, player2Header);

        printString(0, 2, separatorLine);

        // Player 1 Info (left side)
        printString(padding, 4, player1.getName());
        printString(padding, 5, "To Win: " + getWinConditionDescription(player1));
        printString(padding, 6, "Progress: " + getProgress(player1));

        // Player 2 Info (right side)
        int player2Col = halfWidth + padding;
        printString(player2Col, 4, player2.getName());
        printString(player2Col, 5, "To Win: " + getWinConditionDescription(player2));
        printString(player2Col, 6, "Progress: " + getProgress(player2));


        // === Middle Section: Game Board Info ===
        int y = 8;
        printString(0, y, separatorLine);

        String leftBoardHeader = "Game Board - Movie Info";
        String rightBoardHeader = "Recent Connections";
        int leftHeaderStart = (halfWidth - leftBoardHeader.length()) / 2;
        int rightHeaderStart = halfWidth + (halfWidth - rightBoardHeader.length()) / 2;

        printString(leftHeaderStart, y + 1, leftBoardHeader);
        printString(rightHeaderStart, y + 1, rightBoardHeader);

        printString(0, y + 2, separatorLine);

        int leftCol = 0;
        int rightCol = halfWidth + 2;
        int leftY = y + 4;
        int rightY = y + 4;

        Player currentPlayer = gameState.getCurrentPlayer();
        Movie currentMovie = gameState.getCurrentMovie();

        // Left side - Movie Info
        leftY = printWrappedText(leftCol + padding, leftY, "Current Movie: ", currentMovie.getTitle() + " (" + currentMovie.getReleaseYear() + ")", halfWidth - padding - 2);
        leftY = printWrappedText(leftCol + padding, leftY, "Genres: ", String.join(", ", currentMovie.getGenres()), halfWidth - padding - 2);
        leftY = printWrappedText(leftCol + padding, leftY, "Director: ", currentMovie.getDirector(), halfWidth - padding - 2);
        leftY = printWrappedText(leftCol + padding, leftY, "Writer: ", currentMovie.getWriter(), halfWidth - padding - 2);
        leftY = printWrappedText(leftCol + padding, leftY, "Cinematographer: ", currentMovie.getCinematographer(), halfWidth - padding - 2);
        leftY = printWrappedText(leftCol + padding, leftY, "Composer: ", currentMovie.getComposer(), halfWidth - padding - 2);

        // Limit actors to max 3 lines
        List<String> actors = currentMovie.getActors();
        
        if (actors == null) {
            actors = new ArrayList<>(); // PREVENTING CRASH IF POINTING TO NULL!!
        }

        String actorList = String.join(", ", actors);
        int maxActorsWidth = halfWidth - padding - 2;
        int actorLinesPrinted = 0;
        int actorStart = 0;

        while (actorStart < actorList.length() && actorLinesPrinted < 3) {
            int end = Math.min(actorStart + maxActorsWidth, actorList.length());
            printString(leftCol + padding, leftY++, (actorLinesPrinted == 0 ? "Actors: " : "       ") + actorList.substring(actorStart, end));
            actorStart = end;
            actorLinesPrinted++;
        }
        if (actorStart < actorList.length()) {
            printString(leftCol + padding, leftY++, "..."); // indicate overflow
        }

        // Right side - Recent Connections
        List<String> history = gameState.getRecentHistory();
        if (history.isEmpty()) {
            printString(rightCol + padding, rightY++, "(No connections yet)");
        } else {
            for (String entry : history) {
                rightY = printWrappedText(rightCol + padding, rightY, "", entry, halfWidth - padding - 2);
            }
        }

        // === Bottom Section: Typing Input and Timer ===
        int bottomStartY = 22;
        printString(0, bottomStartY, separatorLine);

        // Timer (separate line, centered)
        String timerInfo = ""; // (initial placeholder, later dynamically updated)
        int timerCol = (totalWidth - timerInfo.length()) / 2;
        printString(timerCol, bottomStartY + 1, timerInfo);

        // Round info + Typing prompt together in one line
        String roundInfo = "Round: " + round;
        String typingPrompt = currentPlayer.getName() + ", make a guess...";
        String spacer = "          "; // 10 spaces between
        String combinedPromptLine = roundInfo + spacer + typingPrompt;
        int combinedPromptCol = (totalWidth - combinedPromptLine.length()) / 2;
        printString(combinedPromptCol, bottomStartY + 2, combinedPromptLine);
    }

    /**
     * Updates and redraws the countdown timer on the screen.
     * <p>
     * Clears the previous timer display and shows the updated number of seconds remaining,
     * centered horizontally at a fixed row on the screen.
     *
     * @param secondsRemaining the number of seconds left in the current player's turn
     */
    public void updateTimerOnly(int secondsRemaining) {
        try {
            String timerText = "Timer: " + secondsRemaining + "s";
            int timerStartCol = (totalWidth - timerText.length()) / 2;
            int timerRow = 22; //

            // Clear old timer (overwrite with spaces)
            graphics.putString(timerStartCol, timerRow, " ".repeat(timerText.length()));

            // Write updated timer
            graphics.putString(timerStartCol, timerRow, timerText);

            screen.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Returns a string description of the player's current win condition.
     *
     * @param player the player whose win condition is being described
     * @return formatted win condition string
     */
    private String getWinConditionDescription(Player player) {
        WinConditionStrategy condition = player.getWinCondition();
        if (condition instanceof GenreWinCondition) {
            GenreWinCondition g = (GenreWinCondition) condition;
            return "Find " + g.getRequiredCount() + " " + g.getGenre() + " movies";
        } else if (condition instanceof ActorWinCondition) {
            ActorWinCondition a = (ActorWinCondition) condition;
            return "Find " + a.getRequiredCount() + " movies with " + a.getActor();
        } else if (condition instanceof DirectorWinCondition) {
            DirectorWinCondition d = (DirectorWinCondition) condition;
            return "Find " + d.getRequiredCount() + " movies directed by " + d.getDirector();
        } else {
            return condition.getConditionType();
        }
    }
    /**
     * Returns a string representing the player's current progress toward their win condition.
     *
     * @param player the player whose progress is being calculated
     * @return a string in the form "x/y" indicating progress
     */
    private String getProgress(Player player) {
        WinConditionStrategy condition = player.getWinCondition();
        if (condition instanceof GenreWinCondition) {
            GenreWinCondition g = (GenreWinCondition) condition;
            int count = 0;
            for (Movie m : player.getMoviesPlayed()) {
                if (m.getGenres().contains(g.getGenre())) count++;
            }
            return count + "/" + g.getRequiredCount();
        } else if (condition instanceof ActorWinCondition) {
            ActorWinCondition a = (ActorWinCondition) condition;
            int count = 0;
            for (Movie m : player.getMoviesPlayed()) {
                if (m.getActors().contains(a.getActor())) count++;
            }
            return count + "/" + a.getRequiredCount();
        } else if (condition instanceof DirectorWinCondition) {
            DirectorWinCondition d = (DirectorWinCondition) condition;
            int count = 0;
            for (Movie m : player.getMoviesPlayed()) {
                if (d.getDirector().equalsIgnoreCase(m.getDirector())) count++;
            }
            return count + "/" + d.getRequiredCount();
        } else {
            return "-";
        }
    }


    /**
     * Captures user input from the terminal until Enter is pressed.
     * Handles character entry and backspacing, and updates the screen in real-time.
     *
     * @param startCol the column where input should be displayed
     * @param startRow the row where input should be displayed
     * @return the completed input string entered by the user
     */
    private String getUserInput(int startCol, int startRow) {
        StringBuilder input = new StringBuilder();
        try {
            KeyStroke keyStroke;
            while (true) {
                keyStroke = screen.readInput(); // blocking for name input only
                switch (keyStroke.getKeyType()) {
                    case Enter:
                        return input.toString();
                    case Character:
                        input.append(keyStroke.getCharacter());
                        printString(startCol, startRow, input.toString());
                        break;
                    case Backspace:
                        if (input.length() > 0) {
                            input.deleteCharAt(input.length() - 1);
                            printString(startCol, startRow, input.toString() + " ");
                            printString(startCol, startRow, input.toString());
                        }
                        break;
                    default:
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Displays a list of autocomplete suggestions beneath the input line.
     * <p>
     * Clears any previously shown suggestions and prints up to five new ones, aligned with the user input.
     *
     * @param suggestions the list of suggested movie titles to display
     */
    public void displaySuggestions(List<String> suggestions) {
        int startRow = 28; // start just below input line
        int maxSuggestions = 5;
        int suggestionCol = 10; // align with user input start

        // 1) Clear previous suggestions
        for (int i = 0; i < maxSuggestions; i++) {
            printString(suggestionCol, startRow + i, " ".repeat(80));
        }

        // 2) Print new suggestions
        for (int i = 0; i < Math.min(suggestions.size(), maxSuggestions); i++) {
            printString(suggestionCol, startRow + i, "- " + suggestions.get(i));
        }
    }
    /**
     * Prompts the users to enter names for Player 1 and Player 2 via the terminal interface.
     * <p>
     * Displays input fields and collects the names entered by the users.
     *
     * @return an array of two strings where index 0 is Player 1's name and index 1 is Player 2's name
     */
    public String[] getPlayerNames() {
        clearScreen();

        printString(6, 3, "╔══════════════════════════════════════════════════════════════╗");
        printString(6, 4, "║       Hey there, welcome to Group 20's Movie Name Game!      ║");
        printString(6, 5, "╚══════════════════════════════════════════════════════════════╝");

        printString(6, 7, "Please enter the names of the two players.");

        printString(6, 9, "Enter name for Player 1: ");
        String player1Name = getUserInput(34, 9); // adjusted x for alignment

        printString(6, 11, "Enter name for Player 2: ");
        String player2Name = getUserInput(34, 11); // adjusted y to avoid overlap

        return new String[] { player1Name, player2Name };
    }


    /**
     * Centers a string within a specified width by adding space padding to the left and right.
     * <p>
     * If the string is longer than the desired width, it is truncated. If it's shorter,
     * it will be centered with even spacing on both sides (or one extra space on the right if needed).
     * This is useful for aligning text inside a fixed-width box in terminal UIs.
     *
     * @param text  the text to be centered
     * @param width the total width to center the text within
     * @return a string of exactly {@code width} characters, with the input text centered
     */
    private String centerInBox(String text, int width) {
        int pad = width - text.length();

        if (pad <= 0) {
            return text.substring(0, width); // truncate if too long
        }
        int left = pad / 2;
        int right = pad - left;
        return " ".repeat(left) + text + " ".repeat(right);
    }

    /**
     * Prompts the player to choose a win condition (Genre, Actor, or Director) and a difficulty level.
     * <p>
     * The selected difficulty determines how many matching movies are required to win the game.
     * A random value is selected from the movie database for the chosen win condition type.
     *
     * @param movieDatabase the database of available movies used to generate condition options
     * @return a {@link WinConditionStrategy} representing the player's chosen win condition
     */
    public WinConditionStrategy getPlayersWinConditions(Player player, MovieDatabase movieDatabase) {
        clearScreen();

        // Ensures your title line is centered and fits exactly
        String title = player.getName() + " - Choose Your Win Condition!";
        String centered = centerInBox(title, 66);
        printString(2, 1, "╔" + "═".repeat(66) + "╗");
        printString(2, 2, "║" + centered + "║");
        printString(2, 3, "╚" + "═".repeat(66) + "╝");


        printString(4, 5, "You have 3 win conditions to choose from:");
        printString(6, 7, "1. Genre");
        printString(6, 8, "2. Actor");
        printString(6, 9, "3. Director");


        int choiceRow = 11;
        int winConditionChoice = choiceRow + 1;

        while (true) {
            printString(4, choiceRow, "Enter choice (1-3):                           ");
            String choiceInput = getUserInput(25, choiceRow);
            try {
                winConditionChoice = Integer.parseInt(choiceInput);
                if (winConditionChoice >= 1 && winConditionChoice <= 3) {
                    break;
                }
            } catch (NumberFormatException ignored) {}

            choiceRow += 2;
            printString(4, choiceRow, "Invalid input! Please enter 1, 2, or 3.");
            choiceRow += 2;
        }

        int difficultyRow = choiceRow + 3;
        String difficulty = "";

        int difficultyChoice = 0;

        printString(4, difficultyRow, "Select difficulty level:");
        printString(6, difficultyRow + 1, "1. Easy");
        printString(6, difficultyRow + 2, "2. Medium");
        printString(6, difficultyRow + 3, "3. Hard");

        int difficultyInputRow = difficultyRow + 5;

        while (true) {
            printString(4, difficultyInputRow, "Enter choice (1-3): ");
            String choice = getUserInput(25, difficultyInputRow).trim();
            try {
                difficultyChoice = Integer.parseInt(choice);
                if (difficultyChoice >= 1 && difficultyChoice <= 3) {
                    break;
                }
            } catch (NumberFormatException ignored) {}

            difficultyInputRow += 2;
            printString(4, difficultyInputRow, "Invalid input! Please enter 1, 2, or 3.");
            difficultyInputRow += 2;
        }

        int count;
        switch (difficultyChoice) {
            case 1:
                count = 3;
                break;

            case 2:
                count = 5;
                break;

            case 3:
                count = 7;
                break;

            default:
                count = 5;
        }

        WinConditionStrategy winCondition;

        switch (winConditionChoice) {
            case 1: {
                List<String> genres = new ArrayList<>(movieDatabase.getAllGenres());
                String randomGenre = genres.get(ThreadLocalRandom.current().nextInt(genres.size()));
                winCondition = new GenreWinCondition(randomGenre, count);
                break;
            }
            case 2: {
                List<String> actors = new ArrayList<>(movieDatabase.getAllActors());
                String randomActor = actors.get(ThreadLocalRandom.current().nextInt(actors.size()));
                winCondition = new ActorWinCondition(randomActor, count);
                break;
            }
            case 3: {
                List<String> directors = new ArrayList<>(movieDatabase.getAllDirectors());
                String randomDirector = directors.get(ThreadLocalRandom.current().nextInt(directors.size()));
                winCondition = new DirectorWinCondition(randomDirector, count);
                break;
            }
            default: {
                winCondition = new GenreWinCondition("Action", count);
                break;
            }
        }

        return winCondition;
    }

    /**
     * Prompts the player to decide whether to restart the game after it ends.
     * <p>
     * Displays a message asking for 'y' or 'n' input and waits for the player to respond.
     *
     * @return {@code true} if the player chooses to play again ('y' or 'Y'),
     *         {@code false} if the player chooses not to ('n' or 'N') or if an error occurs
     */
    public boolean promptRestart() {
        final int promptRow = 20;
        int inputRow = promptRow + 4;
        int errorRow = promptRow + 2;
        final int inputCol = 6; // after '> '


        while (true) {
            // Clear all 3 lines
            printString(0, promptRow, " ".repeat(80));
            printString(0, inputRow, " ".repeat(80));
            //printString(0, errorRow, " ".repeat(80));

            // Prompt
            printString(4, promptRow, "Would you like to play again? (y/n), then press Enter:");

            // Print "> " input label
            printString(4, inputRow, "> ");

            // User input begins after "> "
            String input = getUserInput(inputCol, inputRow).trim().toLowerCase();

            if (input.equals("y")) return true;
            if (input.equals("n")) return false;

            // Show error message below
            printString(4, errorRow, "Invalid input! Please enter 'y' or 'n'.");

        }
    }

    /**
     * Displays a thank you message at the end of the game,
     * using a stylized frame and pausing briefly before closing the terminal.
     */
    /**
     * Displays a visually styled thank-you message using ASCII art.
     */
    public void showThankYouMessage() {
        clearScreen();

        String[] message = {
                "╔════════════════════════════════════════════════════════════════════════╗",
                "║                                                                        ║",
                "║          ████████╗██╗  ██╗ █████╗ ███╗   ██╗██╗  ██╗███████╗           ║",
                "║          ╚══██╔══╝██║  ██║██╔══██╗████╗  ██║██║ ██╔╝██╔════╝           ║",
                "║             ██║   ███████║███████║██╔██╗ ██║█████╔╝ ███████╗           ║",
                "║             ██║   ██╔══██║██╔══██║██║╚██╗██║██╔═██╗   ╔══██║           ║",
                "║             ██║   ██║  ██║██║  ██║██║ ╚████║██║  ██║███████╗           ║",
                "║             ╚═╝   ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═══╝╚═╝  ╚═╝╚══════╝           ║",
                "║                                                                        ║",
                "║             THANK YOU FOR PLAYING OUR MOVIE NAME GAME! (:              ║",
                "║         We hope you had fun and discovered some great films.           ║",
                "║                                                                        ║",
                "╚════════════════════════════════════════════════════════════════════════╝"
    };

        int startRow = 8;

        for (int i = 0; i < message.length; i++) {
            int msgLength = message[i].length();
            int startCol = (totalWidth - msgLength) / 2;
            printString(startCol, startRow + i, message[i]);
        }

        try {
            Thread.sleep(9000); // Show message for 9 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
