// FRIDA - Feel Free to change anything as needed. 
// This is jst skeleton, based on the classes in UML. 

import java.util.*;

public class MovieDatabase {
    private Map<String, Movie> movies;
    private AutoCompleteIndex autocomplete;


    // Constructor...NOT SURE HOW YOU WILL EXTRACT YOUR DATA SO THIS iS REALLY UP TO YOU
    // You may want to store extracted movies in here, into a hashmap or smth and then 
    //... use that hashmap to work with the functions down below!! But idk whatever
    public MovieDatabase() {
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
