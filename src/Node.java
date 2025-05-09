/**
 * Creates a node for autocomplete using trie - reworked from autocomplete homework
 * Each node can contain 4 things; a term, word count, prefixes and references
 */
public class Node {

    private Term term;
    private int words;
    private int prefixes;
    private Node[] references;


    /**
     * Constructs a node with references for all 256 ASCII chars
     */
    public Node() {
        //ASCII used here
        this.references = new Node[256];
    }

    /**
     * Constructs a node that stores a complete term and the reference
     *
     * @param query the complete word related to the node
     * @param movie the movie associated with the term
     */
    public Node(String query, Movie movie) {
        this.term = new Term(query, movie);
        this.references = new Node[256];
    }

    /**
     * Sets the term stored in the node
     *
     * @param term the term object where it is supposed to be stored
     */
    public void setTerm(Term term) {
        this.term = term;
    }

    /**
     * Returns the number of complete words
     *
     * @return the number of words
     */
    public int getWords() {
        return words;
    }

    /**
     * Sets the number of complete words
     *
     * @param words the number of words
     */
    public void setWords(int words) {
        this.words = words;
    }

    /**
     * Gets the number of prefixes
     *
     * @return the number of prefixes
     */
    public int getPrefixes() {
        return prefixes;
    }

    /**
     * Sets the number of prefixes
     *
     * @param prefixes the number of prefixes
     */
    public void setPrefixes(int prefixes) {
        this.prefixes = prefixes;
    }

    /**
     * Returns array of nodes references for the node
     *
     * @return an array of Node references
     */
    public Node[] getReferences() {
        return references;
    }

    /**
     * Creates and returns deep copy of  term stored in the node
     *
     * @return a newly created Term object with the same data
     */
    public Term createCopyTerm() {
        Term tnew = new Term(this.term.getTerm(), this.term.getMovie());
        return tnew;
    }
}