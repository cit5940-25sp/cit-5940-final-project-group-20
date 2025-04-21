public class DirectorConnection implements ConnectionStrategy {
    @Override
    public boolean areConnected(Movie a, Movie b) {
        String directorA = a.getDirector();
        String directorB = b.getDirector();

        if (directorA == null || directorB == null) {
            return false;
        }

        return directorA.equalsIgnoreCase(directorB);
    }

    @Override
    public String getType() {
        return "Director";
    }
}
