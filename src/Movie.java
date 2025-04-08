import java.util.*;

public class Movie {
    private int id;
    private String title;
    private int releaseYear;
    private List<String> genres;
    private List<String> actors;
    private String director;
    private String writer;
    private String cinematographer;
    private String composer;

    // constructor can be added later

    public String getTitle() {
        return title;
    }

    public List<String> getGenres() {
        return genres;
    }

    public List<String> getActors() {
        return actors;
    }

    public String getDirector() {
        return director;
    }

    public String getWriter() {
        return writer;
    }

    public String getCinematographer() {
        return cinematographer;
    }

    public String getComposer() {
        return composer;
    }

    // Check if this movie connects to another movie via a specific connection type
    public boolean hasConnection(Movie other, ConnectionStrategy connection) {
        // to be implemented
        return false;
    }
}

