public class GenreWinCondition implements WinConditionStrategy {
    private String genre;
    private int requiredCount;

    // constructor to set the genre and required count
    public GenreWinCondition(String genre, int requiredCount) {
        this.genre = genre;
        this.requiredCount = requiredCount;
    }

    @Override
    public boolean isSatisfied(Player player) {
        // count how many movies player has played that match the required genre
        int genreCount = 0;
        for (Movie movie : player.getMoviesPlayed()) {
            if (movie.getGenres().contains(genre)) {
                genreCount++;
            }
        }
        // player wins if they played the required number of movies in this genre
        return genreCount >= requiredCount;
    }

    @Override
    public String getConditionType() {
        return "Genre: " + genre;
    }
}
