import java.util.Collection;
import java.util.List;

/**
 * Interface for autocomplete that is using a trie - reworked on the homework Autocomplete structure
 * Includes methods for building the Trie, get the SubTrie(adding words), and retrieving suggestions
 *
 */
public interface IAutocomplete
{

    /**
     * Adds a word with the matching movie into the trie struct
     *
     * @param word  the word that will be added to trie
     * @param movie the movie connected to the word
     */
    public void addWord(String word, Movie movie);


    /**
     * Builds tire from a collection of movies
     *
     * @param movies a collection of movies that can be indexed to
     * @return the root node of trie that was made
     */
    public Node buildTrie(Collection<Movie> movies);


    /**
     * Gets the SubTrie from a given prefix
     *
     * @param prefix the parameter, prefix, to search for in the Trie
     * @return the node of the SubTrie or {@code null} if the prefix is not found
     */
    public Node getSubTrie(String prefix);



    /**
     * Returns list of autocomplete suggestions for the input, the prefix
     *
     * @param prefix the prefix to autocomplete
     * @return a list of ITerm objects that start with the given prefix or empty list if no matches are found
     */
    public List<ITerm> getSuggestions(String prefix);

}
