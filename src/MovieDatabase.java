// FRIDA - Feel Free to change anything as needed. 
// This is jst skeleton, based on the classes in UML. 
// Soojin's Note -- I am just adding some Movies here for testing purupses


import java.util.*;

public class MovieDatabase {
    private Map<String, Movie> movies;
    private AutoCompleteIndex autocomplete;


    // Constructor...NOT SURE HOW YOU WILL EXTRACT YOUR DATA SO THIS iS REALLY UP TO YOU
    // You may want to store extracted movies in here, into a hashmap or smth and then 
    //... use that hashmap to work with the functions down below!! But idk whatever
    public MovieDatabase() {
        // here I am adding some movies to mock testing
        this.movies = new HashMap<>();
        // add some movies here
        movies.put("Die Hard", new Movie("Die Hard", 1988, Arrays.asList("Bruce Willis", "Alan Rickman"),
                "John McTiernan", "Steven E. de Souza", "Jan de Bont", "Michael Kamen", Arrays.asList("Action", "Thriller")));
        movies.put("Terminator", new Movie("Terminator", 1984, Arrays.asList("Arnold Schwarzenegger"),
                "James Cameron", "James Cameron", "Adam Greenberg", "Brad Fiedel", Arrays.asList("Action", "Sci-Fi")));
    }

    // Add a new movie to the database
    public void addMovie(Movie movie) {
    }

    // Look up a movie by exact title
    public Movie getMovieByTitle(String title) {
        return movies.get(title);
    }

    // Give movie title suggestions based on what the user types
    public List<String> searchSuggestions(String prefix) {
        List<String> placeholder = new ArrayList<>();
        return placeholder;
    }

    // Return movies connected to a given movie with a certain type of connection
    public List<Movie> getConnectedMovies(Movie movie, ConnectionStrategy connection) {
        List<Movie> placeholder = new ArrayList<>();
        return placeholder;
    }

    // Get all movies stored in the database
    public Collection<Movie> getAllMovies() {
        return movies.values();
    }
}
