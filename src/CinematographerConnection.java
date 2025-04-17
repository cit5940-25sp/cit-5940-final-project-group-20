public class CinematographerConnection implements ConnectionStrategy {
    @Override
    public boolean areConnected(Movie a, Movie b) {
        return a.getCinematographer().equals(b.getCinematographer());  // Check if cinematographers are the same
    }

    @Override
    public String getType() {
        return "Cinematographer";
    }
}
