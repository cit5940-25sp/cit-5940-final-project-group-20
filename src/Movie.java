import java.util.*;

/**
 * Represents a movie with all it's attributes like title,
 * release year, cast, crew, and genres etc.
 */
public class Movie {
    private String title;
    private int releaseYear;
    private List<String> genres;
    private List<String> actors;
    private String director;
    private String writer;
    private String cinematographer;
    private String composer;

    /**
     * Constructs a Movie with the given details.
     *
     * @param title            the movie title
     * @param releaseYear      the release year
     * @param actors           list of actors
     * @param director         the director
     * @param writer           the writer
     * @param cinematographer  the cinematographer
     * @param composer         the composer
     * @param genres           list of genres
     */
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

    /**
     * Gets the movie title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns a version of the title suitable for searching.
     *
     * @return searchable title
     */
    public String getSearchableTitle(){
        String searchableTitle = new String(this.title);
        return searchableTitle;
    }

    /**
     * Gets the release year of the movie.
     *
     * @return the release year
     */
    public int getReleaseYear() {
        return releaseYear;
    }

    /**
     * Gets the list of genres.
     *
     * @return list of genres
     */
    public List<String> getGenres() {
        return genres;
    }

    /**
     * Gets the list of actors.
     *
     * @return list of actors
     */
    public List<String> getActors() {
        return actors;
    }

    /**
     * Sets the list of actors.
     *
     * @param actors new list of actors
     */
    public void setActors(List<String> actors) {
        this.actors = actors;
    }

    /**
     * Sets the director.
     *
     * @param director the director
     */
    public void setDirector(String director) {
        this.director = director;
    }

    /**
     * Sets the writer.
     *
     * @param writer the writer
     */
    public void setWriter(String writer) {
        this.writer = writer;
    }

    /**
     * Sets the cinematographer.
     *
     * @param cinematographer the cinematographer
     */
    public void setCinematographer(String cinematographer) {
        this.cinematographer = cinematographer;
    }

    /**
     * Sets the composer.
     *
     * @param composer the composer
     */
    public void setComposer(String composer) {
        this.composer = composer;
    }

    /**
     * Gets the director.
     *
     * @return the director
     */
    public String getDirector() {
        return director;
    }

    /**
     * Gets the writer.
     *
     * @return the writer
     */
    public String getWriter() {
        return writer;
    }

    /**
     * Gets the cinematographer.
     *
     * @return the cinematographer
     */
    public String getCinematographer() {
        return cinematographer;
    }

    /**
     * Gets the composer.
     *
     * @return the composer
     */
    public String getComposer() {
        return composer;
    }

    /**
     * Returns the title as the string representation of the movie.
     *
     * @return movie title
     */
    @Override
    public String toString() {
        return title;
    }
}

