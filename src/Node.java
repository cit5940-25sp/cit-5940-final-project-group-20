public class Node {

    private Term term;
    private int words;
    private int prefixes;
    private Node[] references;


    public Node() {
        //ASCII
        this.references = new Node[256];
    }


    public Node(String query, Movie movie) {
        this.term = new Term(query, movie);
        this.references = new Node[256];
    }

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }

    public int getWords() {
        return words;
    }

    public void setWords(int words) {
        this.words = words;
    }

    public int getPrefixes() {
        return prefixes;
    }

    public void setPrefixes(int prefixes) {
        this.prefixes = prefixes;
    }

    public Node[] getReferences() {
        return references;
    }

    public void setReferences(Node[] references) {
        this.references = references;
    }
    public Term createCopyTerm() {
        Term tnew = new Term(this.term.getTerm(), this.term.getMovie());
        return tnew;
    }
}