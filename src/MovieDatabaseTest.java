import java.util.List;
import java.util.Set;

public class MovieDatabaseTest {

    public static void main(String[] args) {
        MovieDatabase movieDatabase = new MovieDatabase();

        System.out.println("Total number of movies loaded: " + movieDatabase.getAllMovies().size());

        // Test genres
        Set<String> genres = movieDatabase.getAllGenres();
        System.out.println("\nGenres:");
        for (String genre : genres) {
            System.out.println(genre);
        }

        // Test actors
        Set<String> actors = movieDatabase.getAllActors();
        System.out.println("\nActors with at least 10 movies:");
        for (String actor : actors) {
            System.out.println(actor);
        }

        // Test directors
        Set<String> directors = movieDatabase.getAllDirectors();
        System.out.println("\nDirectors:");
        for (String director : directors) {
            System.out.println(director);
        }

        // Test getting a movie by title
        Movie dieHard = movieDatabase.getMovieByTitle("Die Hard");
        if (dieHard != null) {
            System.out.println("\nFound movie: " + dieHard.getTitle() + " (" + dieHard.getReleaseYear() + ")");
        } else {
            System.out.println("\nMovie 'Die Hard' not found!");
        }
    }
}
