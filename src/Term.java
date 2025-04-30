
public class Term implements ITerm {


    String term;

    Movie movie;


    /**
     * Initialize a Term with a given query String and weight
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

    @Override
    public int compareTo(ITerm that) {
        return this.term.compareTo(that.getTerm());
    }

    @Override
    public String toString() {
        return movie.getTitle() + " " + "(" + movie.getReleaseYear()+ ")";
    }

    @Override
    public Movie getMovie() {
        return movie;
    }

    @Override
    public String getTerm() {
        return term;
    }

    @Override
    public String setTerm(String term) {
        this.term = term;
        return term;
    }

    @Override
    public Movie setMovie(Movie movie) {
        this.movie = movie;
        return this.movie;
    }

}