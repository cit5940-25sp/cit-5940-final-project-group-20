import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Implements autocomplete using a trie (prefix tree)
 *
 * This class supports insertion of words associated with Movie objects
 * and provides efficient prefix-based lookup and suggestion functionality.
 * It is case-insensitive and ignores invalid characters (non-ASCII).
 */
public class Autocomplete implements IAutocomplete {
    private Node root;

    public Autocomplete() {
        this.root = new Node();
    }

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
     * Returns root node of trie
     *
     * @return root node
     */
    public Node getRoot() {
        return this.root;
    }

    /**
     * Checks if a word is valid with only ASCII chars
     *
     * @param word the string to check
     * @return true if all chars are valid otherwise false
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
     * Builds the trie from a collection of Movie objects.
     * Each movie's searchable title is added to the trie.
     *
     * @param movies the collection of movies to add
     * @return the root node of the completed trie
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
     * Retrieves the sub-trie rooted at the end of the given prefix.
     *
     * Returns null if the prefix is not found or contains invalid characters.
     *
     * @param prefix the prefix to search for
     * @return the node representing the end of the prefix path, or null if not found
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
     * Retrieves all complete words in the trie that start with the given prefix.
     *
     * @param prefix the prefix to search for
     * @return a list of ITerm suggestions matching the prefix
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
     * Recursively traverses the trie from the given node to collect complete terms.
     *
     * @param currentnode  the current node in traversal
     * @param suggestions  the list to accumulate matching terms
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