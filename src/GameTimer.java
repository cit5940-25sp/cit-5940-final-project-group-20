public class GameTimer {
    private int duration;
    private boolean timeUp;
    private Thread timerThread;
    private boolean running;
    private long startTime;

    // Constructor to set how long the timer should run
    public GameTimer(int duration) {
        this.duration = duration;
        this.timeUp = false;
        this.running = false;
    }

    // Starts the timer and runs the given code if time runs out
    public void start(Runnable onTimeout) {
        // to be implemented
    }

    // Cancel the timer if the game ends early or something
    public void cancel() {
        // to be implemented
    }

    // Check if the time is up
    public boolean isTimeUp() {
        return timeUp;
    }

    // Get how many seconds are left (or 0 if time's up)
    public int getTimeRemaining() {
        // to be implemented
        return 0;
    }
}
