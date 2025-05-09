import java.util.Comparator;

/**
 * Shows a searchable term related to movie - reworked from autocomplete homework
 * Terms can be compared in 2 ways, lexicographically or by a prefix
 * This interface is implemented by class Term {@code Term}
 *
 */
public interface ITerm
        extends Comparable<ITerm> {

    /**
     * Returns comparator that compares terms with their prefix length
     * Terms are compared by lexicographic order by the first characters of string
     * If a term is shorter than the parameter the full string is used
     *
     * @param r the number of chars used for comparator
     * @return a Comparator that compares terms with prefix order
     * @throws IllegalArgumentException if r is negative
     */
    public static Comparator<ITerm> byPrefixOrder(int r) {
        if (r < 0) {
            throw new IllegalArgumentException("cannot sort by negative r");
        }
        return new ByPrefixOrder(r);
    }

    /**
     * A comparator that compares the terms by the first number of {@code r} characters in the strings
     */
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


    /**
     * Compares this term with the specified that term looking for the order
     * Returns a negative integer, zero, or a positive integer as this term is less than,
     * equal to, or greater than the that term in lexicographical order
     *
     * @param that the other ITerm to we compare it to
     * @return an int that shows the result of comparing the terms lexicographically
     */
    public int compareTo(ITerm that);

    /**
     * Returns a string of term
     *
     * @return string of the term
     */
    public String toString();

    /**
     * Returns Movie object related to this term
     *
     * @return the related Movie
     */
    public Movie getMovie();

    /**
     * Returns the term string
     *
     * @return the term string
     */
    public String getTerm();


}

