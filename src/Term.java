
public class Term implements ITerm {


    String term;
    long weight;

    String movie;

    /**
     * Initialize a Term with a given query String and weight
     */
    public Term(String term, long weight) {
        if (term == null) {
            throw new IllegalArgumentException("Illegal argument term = 0 ");
        }
        if (weight < 0) {
            throw new IllegalArgumentException("Illegal argument weight negative ");
        }
        this.term = term;
        this.weight = weight;

    }

    @Override
    public int compareTo(ITerm that) {
        if (this.term.compareTo(that.getTerm()) == 0) {
            return Long.compare(this.weight, that.getWeight());
        } else {
            return this.term.compareTo(that.getTerm());
        }
    }

    @Override
    public String toString() {
        String s = "";
        s = s + this.weight;
        s = s + "\t" + term;
        return s;
    }

    @Override
    public long getWeight() {
        return weight;
    }

    @Override
    public String getTerm() {
        return term;
    }

    @Override
    public void setWeight(long weight) {
        this.weight = weight;
    }

    @Override
    public String setTerm(String term) {
        this.term = term;
        return term;
    }

}