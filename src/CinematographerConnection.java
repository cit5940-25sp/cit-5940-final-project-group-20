public class CinematographerConnection implements ConnectionStrategy {
    @Override
    public boolean areConnected(Movie a, Movie b) {
        String cinematographerA = a.getCinematographer();
        String cinematographerB = b.getCinematographer();

        if (cinematographerA == null || cinematographerB == null) {
            return false;
        }

        return cinematographerA.equals(cinematographerB);
    }

    @Override
    public String getType() {
        return "Cinematographer";
    }
}
