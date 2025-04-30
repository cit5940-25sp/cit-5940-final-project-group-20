import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
            char currentchar = word.charAt(i);
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

    public Node getRoot() {
        return this.root;
    }

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

    @Override
    public Node buildTrie(Collection<Movie> movies) {

        for(Movie m : movies){
            if (m == null) {
                continue;
            }
            addWord(m.getSearchableTitle(), m);
        }
        return this.root;
    }

    @Override
    public Node getSubTrie(String prefix) {
        Node currentnode = root;
        //prefix = prefix.toLowerCase();
        for (int i = 0; i < prefix.length(); i++) {
            char c = prefix.charAt(i);
            int referenceindex = c;
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

    @Override
    public int countPrefixes(String prefix) {
        if (getSubTrie(prefix) == null) {
            return 0;
        }
        return getSubTrie(prefix).getPrefixes();
    }


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

    //helper function
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