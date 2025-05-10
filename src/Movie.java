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
    public Movie(String title, int releaseYear, List<String> actors, String director,
                 String writer, String cinematographer, String composer, List<String> genres) {
        this.title = title;
        this.releaseYear = releaseYear;
        this.actors = actors;
        this.director = director;
        this.writer = writer;
        this.cinematographer = cinematographer;
        this.composer = composer;
        this.genres = genres;
    }

    public String getTitle() {
        return title;
    }

    //clean and remove title for autocomplete
    public String getSearchableTitle(){
        String searchableTitle = new String(this.title);
        //took this out so no problem with searching title with "the"
//        if(searchableTitle.toLowerCase().startsWith("the ")){
//            searchableTitle = searchableTitle.substring(4);
//        }
//        if(searchableTitle.toLowerCase().startsWith("a ")){
//            searchableTitle = searchableTitle.substring(2);
//        }
        return searchableTitle;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public List<String> getGenres() {
        return genres;
    }

    public List<String> getActors() {
        return actors;
    }

    public void setActors(List<String> actors) {
        this.actors = actors;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public void setCinematographer(String cinematographer) {
        this.cinematographer = cinematographer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
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

    @Override
    public String toString() {
        return title;
    }


    // Check if this movie connects to another movie via a specific connection type
    public boolean hasConnection(Movie other, ConnectionStrategy connection) {
        // to be implemented
        return false;
    }
}

