import java.util.Comparator;

/**
 * @author ericfouh
 */
public interface ITerm
        extends Comparable<ITerm> {



    /**
     * Compares the two terms in lexicographic order but using only the first r
     * characters of each query.
     *
     * @param r
     * @return comparator Object
     */
    public static Comparator<ITerm> byPrefixOrder(int r) {
        if (r < 0) {
            throw new IllegalArgumentException("cannot sort by negative r");
        }
        return new ByPrefixOrder(r);
    }

    public class ByPrefixOrder implements Comparator<ITerm> {
        int r;
        public ByPrefixOrder(int r) {
            this.r = r;
        }
        @Override
        public int compare(ITerm o1, ITerm o2) {
            int length1 =  Math.min(o1.getTerm().length(), r);
            int length2 =  Math.min(o2.getTerm().length(), r);

            String s1 = o1.getTerm().substring(0,length1);
            String s2 = o2.getTerm().substring(0,length2);
            return s1.compareTo(s2);

        }
    }


    // Compares the two terms in lexicographic order by query.
    public int compareTo(ITerm that);



    public String toString();

    // Required getters.
    public Movie getMovie();
    public String getTerm();

    // Required setters (mostly for autograding purposes)
    public Movie setMovie(Movie movie);
    public String setTerm(String term);

}

