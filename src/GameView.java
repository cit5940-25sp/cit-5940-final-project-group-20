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

public class GameView {
    private Screen screen;
    private TextGraphics graphics;

    private int totalWidth;
    private int halfWidth;
    private int thirdWidth;

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

    private void printString(int col, int row, String text) {
        graphics.putString(col, row, text);
        try {
            screen.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void showMessageAndPause(String message) {
        try {
            graphics.putString(0, 24, "                                                        "); // Clear old
            graphics.putString(0, 24, message); // Show message at row 15
            screen.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // changed to public!
    public void clearScreen() {
        try {
            screen.clear();
            screen.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeScreen() {
        try {
            screen.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public KeyStroke pollInput() {
        try {
            return screen.pollInput();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // update what the user is typing
    public void updatePlayerInput(String currentInput) {
        // Overwrite the input line
        printString(10, 27, "                                      "); // Clear old input
        printString(10, 27, currentInput); // Print new input
    }


    public void showWinner(Player player) {
        clearScreen();
        printString(0, 0, "Congratulations, " + player.getName() + "!");
        printString(0, 1, "You have won the game!");
    }

    // Helper: Print long text wrapped at maxWidth
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


    // Original getPlayerInput still works (for player name and win condition setup)
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

    public void displaySuggestions(List<String> suggestions) {
        int startRow = 28; // start just below input line
        int maxSuggestions = 5;
        int suggestionCol = 10; // align with user input start

        // Step 1: Clear previous suggestions
        for (int i = 0; i < maxSuggestions; i++) {
            printString(suggestionCol, startRow + i, " ".repeat(80));
        }

        // Step 2: Print new suggestions
        for (int i = 0; i < Math.min(suggestions.size(), maxSuggestions); i++) {
            printString(suggestionCol, startRow + i, "- " + suggestions.get(i));
        }
    }

    public String[] getPlayerNames() {
        clearScreen();
        printString(0, 0, "Hey there, welcome to Group 20's Movie Name Game!");
        printString(0, 2, "Please enter the names of the two players.");

        printString(0, 4, "Enter name for Player 1: ");
        String player1Name = getUserInput(25, 4);

        printString(0, 6, "Enter name for Player 2: ");
        String player2Name = getUserInput(25, 6);

        return new String[]{player1Name, player2Name};
    }

    public WinConditionStrategy getPlayersWinConditions(MovieDatabase movieDatabase) {
        clearScreen();
        printString(0, 0, "You have 3 win conditions to choose from:");
        printString(0, 2, "1. Genre");
        printString(0, 3, "2. Actor");
        printString(0, 4, "3. Director");
        printString(0, 6, "Enter choice (1-3): ");

        int winConditionChoice = 0;
        int choiceRow = 6;
        while (true) {
            printString(0, choiceRow, "Enter choice (1-3):                     ");
            String choiceInput = getUserInput(20, choiceRow);
            try {
                winConditionChoice = Integer.parseInt(choiceInput);
                if (winConditionChoice >= 1 && winConditionChoice <= 3) {
                    break;
                }
            } catch (NumberFormatException ignored) {}

            choiceRow += 2;
            printString(0, choiceRow, "Invalid input. Please enter 1, 2, or 3.");
        }

    
        int difficultyRow = choiceRow + 3;
        String difficulty = "";
        while (true) {
            printString(0, difficultyRow, "Difficulty? (easy, medium, hard):                     ");
            difficulty = getUserInput(35, difficultyRow).toLowerCase().trim();
            if (difficulty.equals("easy") || difficulty.equals("medium") || difficulty.equals("hard")) {
                break;
            }
            difficultyRow += 2;
            printString(0, difficultyRow, "Invalid input. Please choose again easy, medium, or hard.");
        }
        int count;
        switch (difficulty) {
            case "easy": count = 3; break;
            case "hard": count = 7; break;
            default: count = 5;
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

    // New function to initiate new Game instead of frozen terminal
    public boolean promptRestart() {
        printString(0, 20, "Would you like to Play again? (y/n): ");
        try {
            while (true) {
                KeyStroke key = screen.readInput(); // blocking
                if (key.getKeyType() == KeyType.Character) {
                    char c = key.getCharacter();
                    if (c == 'y' || c == 'Y') return true;
                    if (c == 'n' || c == 'N') return false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    



}
