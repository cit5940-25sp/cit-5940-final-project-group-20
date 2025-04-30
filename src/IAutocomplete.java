import java.util.Collection;
import java.util.List;

/**
 * @author ericfouh
 */
public interface IAutocomplete
{

    /**
     * Adds a new word with its associated weight to the Trie.
     * If the word contains an invalid character, simply do nothing.
     *
     * @param word the word to be added to the Trie
     * @param movie the movie that word belongs to
     */
    public void addWord(String word, Movie movie);



    /**
     * Initializes the Trie
     *
     * @param movies Collection of movies - all the movies we want to be able to autocomplete for
     * @return the root of the Trie You might find the readLine() method in
     *         BufferedReader useful in this situation as it will allow you to
     *         read a file one line at a time.
     */
    public Node buildTrie(Collection<Movie> movies);


    /**
     * @param prefix
     * @return the root of the subTrie corresponding to the last character of
     *         the prefix. If the prefix is not represented in the trie, return null.
     */
    public Node getSubTrie(String prefix);


    /**
     * @param prefix
     * @return the number of words that start with prefix.
     */
    public int countPrefixes(String prefix);


    /**
     * This method should not throw an exception.
     * Make sure that you preserve encapsulation by returning a list of copies of the
     * original Terms; otherwise, the user might be able to change the structure of your
     * Trie based on the values returned.
     *
     * @param prefix
     * @return a List containing all the ITerm objects with query starting with
     *         prefix. Return an empty list if there are no ITerm object starting
     *         with prefix.
     */
    public List<ITerm> getSuggestions(String prefix);

}
