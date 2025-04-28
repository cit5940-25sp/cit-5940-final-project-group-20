import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Autocomplete implements IAutocomplete {
    private Node root;

    public Autocomplete() {
        this.root = new Node();
    }

    @Override
    public void addWord(String word, long weight) {
        //checking if word is valid -> letters only
        if (!this.validChars(word)) {
            return;
        }
        //finding place in tree root-> down
        Node currentnode = root;
        currentnode.setPrefixes((currentnode.getPrefixes() + 1));
        for (int i = 0; i < word.length(); i++) {
            char currentchar = word.charAt(i);
            int referenceindex = currentchar - 'a';
            // if no reference, create node
            if (currentnode.getReferences()[referenceindex] == null) {
                currentnode.getReferences()[referenceindex] = new Node();
            }
            //last letter in word
            if (i == word.length() - 1) {
                if (currentnode.getReferences()[referenceindex] == null) {
                    Node completeword = new Node(word, weight);
                    completeword.setWords(1);
                    currentnode.getReferences()[referenceindex] = completeword;
                } else {
                    currentnode.getReferences()[referenceindex].setWords(1);
                    Term t = new Term(word, weight);
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
            if (word.charAt(i) > 122 || word.charAt(i) < 97) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Node buildTrie(String filename, int k) {
        FileReader fileReader;
        try {
            fileReader = new FileReader(filename);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        //buffered reader to read from file
        BufferedReader buff = new BufferedReader(fileReader);
        while (true) {
            //reading next line in file
            String line;
            try {
                line = buff.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //checking if at the end of file
            if (line == null) {
                break;
            }
            //trim the white space in front and after string
            line = line.trim();
            line = line.toLowerCase();
            //splitting the line into 2 by single white space
            String[] parts = line.split("\\s+");
            //expect line to have 2 strings
            if (parts.length != 2) {
                continue;
            }
            //convert and use addWord
            long weight = Long.parseLong(parts[0]);
            String word = parts[1];
            word = word.toLowerCase();
            addWord(word, weight);

        }
        return this.root;
    }

    @Override
    public Node getSubTrie(String prefix) {
        Node currentnode = root;
        //prefix = prefix.toLowerCase();
        for (int i = 0; i < prefix.length(); i++) {
            char c = prefix.charAt(i);
            int referenceindex = c - 'a';
            if (referenceindex < 0 || referenceindex > 25) {
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
        /*Node currentnode = root;
        for(int i = 0; i < prefix.length(); i++){
            char c = prefix.charAt(i);
            int referenceindex = c - 'a';
            if(referenceindex < 0 || referenceindex > 25){
                return 0;
            }
            if(currentnode.getReferences()[referenceindex] == null){
                return 0;
            }
            currentnode = currentnode.getReferences()[referenceindex];
        }*/
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