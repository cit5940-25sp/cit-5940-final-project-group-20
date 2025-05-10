import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Implements autocomplete using trie - reworked version from the Homework Autocomplete
 *
 * The class supports prefix-based lookup and suggestion functionality using {@link Movie} objects
 * It is case-insensitive and uses at the 256 ASCII chars
 * Implements the ITerm {@code ITerm} interface
 */
public class Autocomplete implements IAutocomplete {
    private Node root;

    /**
     * Constructs an empty Autocomplete trie with a newly created root node
     */
    public Autocomplete() {
        this.root = new Node();
    }

    /**
     * Adds a new word associated with movie to the trie structure
     * But if it has non-valid Chars it is being ignored
     *
     * @param word  the word to add
     * @param movie the movie connected to word
     */
    @Override
    public void addWord(String word, Movie movie) {
        //checking if word is valid -> letters only
        if (!this.validChars(word)) {
            return;
        }
        //finding place in tree root-> down
        Node currentnode = root;
        currentnode.setPrefixes((currentnode.getPrefixes() + 1));
        for (int i = 0; i < word.length(); i++) {
            char currentchar = Character.toLowerCase(word.charAt(i));
            //need to be able to match lower and upper case harry
            int referenceindex = currentchar;
            // if no reference, create node
            if (currentnode.getReferences()[referenceindex] == null) {
                currentnode.getReferences()[referenceindex] = new Node();
            }
            //last letter in word
            if (i == word.length() - 1) {
                if (currentnode.getReferences()[referenceindex] == null) {
                    Node completeword = new Node(word, movie);
                    completeword.setWords(1);
                    currentnode.getReferences()[referenceindex] = completeword;
                } else {
                    currentnode.getReferences()[referenceindex].setWords(1);
                    Term t = new Term(word, movie);
                    currentnode.getReferences()[referenceindex].setTerm(t);
                }
            }
            //moving forward
            currentnode = currentnode.getReferences()[referenceindex];
            currentnode.setPrefixes((currentnode.getPrefixes() + 1));
        }


    }
    /**
     * Returns the root node of the created trie
     *
     * @return the root node
     */
    public Node getRoot() {
        return this.root;
    }

    /**
     * Checks if a word contains valid Chars (all 256 ASCII)
     *
     * @param word the word we are checking
     * @return {@code true} if all chars are valid 256 ASCII, {@code false} otherwise
     */
    public boolean validChars(String word) {
        for (int i = 0; i < word.length(); i++) {
            //ASCII
            if (word.charAt(i) > 256 || word.charAt(i) < 0) {
                System.err.println("unexpected char " + word.charAt(i));
                return false;
            }
        }
        return true;
    }

    /**
     * Builds trie by adding searchable titles from a movie collection
     *
     * @param movies the collection of movies to add
     * @return the root node of created trie
     */
    @Override
    public Node buildTrie(Collection<Movie> movies) {

        for (Movie m : movies) {
            if (m == null) {
                continue;
            }
            addWord(m.getSearchableTitle(), m);
        }
        return this.root;
    }

    /**
     * Returns the node matching to the last char of the given prefix
     * Returns {@code null} if in the prefix there are unvalid chars
     *
     * @param prefix the prefix to look up
     * @return the currentnode or {@code null} if not found
     */

    @Override
    public Node getSubTrie(String prefix) {
        Node currentnode = root;
        for (int i = 0; i < prefix.length(); i++) {
            char c = Character.toLowerCase(prefix.charAt(i));
            int referenceindex = c;
            //checks for valid ASCII if not return null
            if (referenceindex < 0 || referenceindex > 256) {
                return null;
            }
            if (currentnode.getReferences()[referenceindex] == null) {
                return null;
            }
            currentnode = currentnode.getReferences()[referenceindex];
        }
        return currentnode;
    }



    /**
     * Returns list of autocomplete suggestions for a Sting prefix
     * If there are no matches, empty list will be returned
     *
     * @param prefix the prefix to search for
     * @return a list of {@link ITerm} objects that have matching prefix
     */
    @Override
    public List<ITerm> getSuggestions(String prefix) {
        List<ITerm> suggestions = new ArrayList<>();
        //current node starting point
        Node currentnode = getSubTrie(prefix);
        //suggestions will be empty as no matches found
        if (currentnode == null) {
            return suggestions;
        }
        helperFunction(currentnode, suggestions);

        return suggestions;
    }

    /**
     * Recursive function that adds together complete terms from trie
     *
     * @param currentnode the current node
     * @param suggestions the list that we want to populate with terms that match
     */
    private void helperFunction(Node currentnode,List<ITerm> suggestions) {
        //checking if currentnode points to a complete word
        if (currentnode.getWords() == 1) {
            //add word to suggestion list
            suggestions.add(currentnode.createCopyTerm());
        }
        for (Node child: currentnode.getReferences()) {
            if (child == null) {
                continue;
            }
            helperFunction(child, suggestions);

        }
    }
}