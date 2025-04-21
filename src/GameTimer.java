// Use Thread in java to make it run in background
public class GameTimer {
    private int duration;  // Duration of the timer in seconds (30 seconds per turn)
    private boolean timeUp;  // Flag to check if time is up
    private Thread timerThread;  // Thread that handles the countdown
    private long startTime;  // The time when the timer started
    private Runnable onTimeout;  // Callback function to be executed on timeout

    // Constructor to initialize the timer with a duration
    public GameTimer(int duration) {
        this.duration = duration;
        this.timeUp = false;
    }

    // Start the timer and begin counting down
    public void start(Runnable onTimeout, java.util.function.IntConsumer onTick) {
        this.onTimeout = onTimeout;
        this.timeUp = false;
        this.startTime = System.currentTimeMillis();  // Record the start time

        timerThread = new Thread(() -> {
            try {
                // Loop until the time runs out or the game is over
                while (!timeUp && (System.currentTimeMillis() - startTime) < duration * 1000) {
                    Thread.sleep(1000);  // Sleep for 1 second
                    int secondsRemaining = getTimeRemaining();

                    // call othe onTick function to update the UI via gameview
                    onTick.accept(secondsRemaining);

                    if (secondsRemaining == 0) {
                        timeUp = true;
                        onTimeout.run();  // Run the timeout event (e.g., switch player)
                    }

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        timerThread.start();  // Start the countdown in a separate thread
    }

    // Check if the timer has run out
    public boolean isTimeUp() {
        return timeUp;
    }

    // Get the time remaining in seconds
    public int getTimeRemaining() {
        long elapsedTime = System.currentTimeMillis() - startTime;  // Elapsed time in milliseconds
        int timeRemaining = (int) ((duration * 1000 - elapsedTime) / 1000);  // Convert to seconds
        return Math.max(timeRemaining, 0);  // Ensure time remaining doesn't go negative
    }
}