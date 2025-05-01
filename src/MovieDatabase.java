// FRIDA - Feel Free to change anything as needed. 
// This is jst skeleton, based on the classes in UML. 
// Soojin's Note -- I am just adding some Movies here for testing purupses


import java.util.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class MovieDatabase {
    private Map<String, Movie> movies;
    //changed to my autocomplete class
    private Autocomplete autocomplete;


    // Trying to import Frida's CSVImporter to create movie database out of CSV file
    public MovieDatabase() {
        // here I am adding some movies to mock testing
        this.movies = new HashMap<>();



        CSVDataImporter importer = new CSVDataImporter();
        //pass map into autocomplete after importing
        Map<Integer, Movie> imported = importer.importDataMovie("tmdb_5000_movies.csv");
        imported = importer.importDataCredit("tmdb_5000_credits.csv", imported);
        this.autocomplete = new Autocomplete();



        // Populate internal Map<String, Movie> using movie titles as keys
        for (Movie movie : imported.values()) {
            movies.put(movie.getTitle(), movie);
            autocomplete.addWord(movie.getSearchableTitle(), movie);
        }

        // Write all movie info to MovieDatabase.txt this
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("movieDatabase.txt"))) {
            for (Movie movie : imported.values()) {
                writer.write("Title: " + movie.getTitle() + "\n");
                writer.write("Release Year: " + movie.getReleaseYear() + "\n");
                writer.write("Actors: " + movie.getActors() + "\n");
                writer.write("Director: " + movie.getDirector() + "\n");
                writer.write("Writer: " + movie.getWriter() + "\n");
                writer.write("Cinematographer: " + movie.getCinematographer() + "\n");
                writer.write("Composer: " + movie.getComposer() + "\n");
                writer.write("Genres: " + movie.getGenres() + "\n");
                writer.write("--------------------------------------------------\n");

            }
            System.out.println("Movie information written to output.txt");
        } catch (IOException e) {
            System.err.println("Error writing to output.txt: " + e.getMessage());
        }

    }

    // Add a new movie to the database
    public void addMovie(Movie movie) {
    }

    // Look up a movie by exact title
    public Movie getMovieByTitle(String title) {
        return movies.get(title);
    }

    //AUTOCOMPLETE CHANGE- Frida

    // Give movie title suggestions based on what the user types
    public List<Movie> searchSuggestions(String prefix) {
        List<ITerm> suggestions = autocomplete.getSuggestions(prefix);
        List<Movie> movies = new ArrayList<>();
        for(ITerm s : suggestions){
            movies.add(s.getMovie());
        }
        return movies;
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

    // Gets all genres that are present in the data base!!
    public Set<String> getAllGenres() {
        Set<String> genresSet = new HashSet<>();
        
        // Looping through all movie objects
        for (Movie movie : getAllMovies()) {   

            // Looping through all genres associated with each movie
            for (String genre : movie.getGenres()) {
                genresSet.add(genre);
            }
            
        }

        return genresSet;
    }

    // Gets Actor that appear in 10+ movies! 
    public Set<String> getAllActors() {

        // Loop through all movies, all actors and count
        Map<String, Integer> actorCount = new HashMap<>();

        for (Movie movie : getAllMovies()) {
            if (movie.getActors() != null) {  // NULL CHECK AS SOME ACTORS for movies not listed!
                for (String actor : movie.getActors()) {
                    actorCount.put(actor, actorCount.getOrDefault(actor, 0) + 1);
                }
            }
        }

        // Looping through all actors and their counts, filtering only those 
        // appearing in >= 10 movies 
        Set<String> eligibleActors = new HashSet<>();

        for (Map.Entry<String, Integer> actor : actorCount.entrySet()) {
            if (actor.getValue() >= 10) {
                eligibleActors.add(actor.getKey());
            }
        }
        return eligibleActors;
    }

    // Gets directors that appear in 10+ movies! 
    // May have to add extra check if not all Movies have a director in data base etc...
    public Set<String> getAllDirectors() {

        // Loop through all movies, all dircetors and count
        Map<String, Integer> directorCount = new HashMap<>();
        for (Movie movie : getAllMovies()) {
            // Sets count for each director
            int count = directorCount.containsKey(movie.getDirector()) ? directorCount.get(movie.getDirector()) + 1 : 1;
            directorCount.put(movie.getDirector(), count);
        }

        // Looping through all directors and their counts, filtering only those 
        // appearing in >= 10 movies 
        Set<String> eligibleDirectors = new HashSet<>();

        for (Map.Entry<String, Integer> director : directorCount.entrySet()) {
            if (director.getKey() != null && director.getValue() >= 10 && !director.getKey().equals("null")) {
                eligibleDirectors.add(director.getKey());
            }
        }
        return eligibleDirectors;
    }

    // Helper Method that will initialise first movie!
    public Movie getValidStartingMovie(List<ConnectionStrategy> strategies) {
        
        List<Movie> allMovies = new ArrayList<>(getAllMovies());
        
        Collections.shuffle(allMovies); // randomises the order of the movies!
    
        for (Movie movie : allMovies) {
            
            if (movie == null) {
                continue;
            }
            
            if (movie.getTitle() == null || movie.getTitle().isEmpty()) {
                continue;
            }

            if (movie.getActors() == null || movie.getActors().isEmpty()) {
                continue;
            }

            if (movie.getDirector() == null || movie.getDirector().equals("null")) {
                continue;
            }
    
            for (Movie other : getAllMovies()) {
                
                if (other == null || other == movie) {
                    continue;
                }
    
                for (ConnectionStrategy strategy : strategies) {

                    if (strategy.areConnected(movie, other)) {

                        return movie; // Movie that is connected!! 
                    }
                }
            }
        }
    
        return null; // Just in case
    }
    

}
