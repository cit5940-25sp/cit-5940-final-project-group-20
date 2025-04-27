public class ActorWinCondition implements WinConditionStrategy {
    private String actor;
    private int requiredCount;

    // constructor to set the actor and required count
    public ActorWinCondition(String actor, int requiredCount) {
        this.actor = actor;
        this.requiredCount = requiredCount;
    }

    @Override

    public boolean isSatisfied(Player player) {
        int count = 0;
        for (Movie movie : player.getMoviesPlayed()) {
            if (movie.getActors().contains(actor)) {
                count++;
            }
        }
        return count >= requiredCount; // returns True if required count hit!
    }

    @Override
    public String getConditionType() {
        return "Actor";
    }
    public String getActor() {
        return actor;
    }
    public int getRequiredCount() {
        return requiredCount;
    }

}