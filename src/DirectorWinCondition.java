public class DirectorWinCondition implements WinConditionStrategy {
    private String director;
    private int requiredCount;

    // constructor to set the director and required count
    public DirectorWinCondition(String director, int requiredCount) {
        this.director = director;
        this.requiredCount = requiredCount;
    }

    @Override
    public boolean isSatisfied(Player player) {
        int count = 0;
        for (Movie movie : player.getMoviesPlayed()) {
            if (director.equalsIgnoreCase(movie.getDirector())) {   
                count++;
            }
        }
        return count >= requiredCount; // returns true if required count is reached!
    }

    @Override
    public String getConditionType() {
        String pluralGrammar = (requiredCount > 1) ? "movies" : "movie"; 
        return "Play " + requiredCount + " " + pluralGrammar + " directed by " + director;
    }
}
