/**
 * Creates a term for the autocomplete - reworked from autocomplete homework
 * Each term is associated with one movie and the term itself is a string
 * Implements the ITerm {@code ITerm} interface
 */
public class Term implements ITerm {

    //search term
    String term;
    //movie
    Movie movie;

    /**
     * Constructs a Term with term and movie
     *
     * @param term  the text of the term we are looking for - testing for null
     * @param movie the Movie object related to term - testing for null
     * @throws IllegalArgumentException if term or movie is null
     */
    public Term(String term, Movie movie) {
        if (term == null) {
            throw new IllegalArgumentException("Illegal argument term = 0 ");
        }
        if (movie == null) {
            throw new IllegalArgumentException("Illegal argument movie empty ");
        }

        this.term = term;
        this.movie = movie;
    }

    /**
     * Compares this term with another term, specifcally the string, lexicographically
     *
     * @param that the other term we compare it to
     * @return a negative integer, zero, or a positive integer as this term
     * is less than, equal to, or greater than the given term (parameter)
     */
    @Override
    public int compareTo(ITerm that) {
        return this.term.compareTo(that.getTerm());
    }

    /**
     * Returns a string printing of the term
     *
     * @return the formatted string
     */
    @Override
    public String toString() {
        return movie.getTitle() + " " + "(" + movie.getReleaseYear() + ")";
    }

    /**
     * Returns the Movie related to this term
     *
     * @return the related Movie object
     */
    @Override
    public Movie getMovie() {
        return movie;
    }

    /**
     * Returns the string(text) of the term
     *
     * @return the term
     */
    @Override
    public String getTerm() {
        return term;
    }
}