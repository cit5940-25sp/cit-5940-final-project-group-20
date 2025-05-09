
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
public class NodeTest {
    @org.junit.Test
    public void testNodeDefaultConstructor() {
        Node t = new Node();
        assertEquals(256,t.getReferences().length);
        assertEquals(0,t.getWords());
        assertEquals(0,t.getPrefixes());
    }

    @org.junit.Test
    public void testSetAndGetWord() {
        Node t = new Node();
        t.setWords(4);
        assertEquals(4, t.getWords());
    }

    @org.junit.Test
    public void testSetAndGetPrefix() {
        Node t = new Node();
        t.setPrefixes(4);
        assertEquals(4, t.getPrefixes());
    }

    @org.junit.Test
    public void testCopyTerm() {
        List<String> actors = new ArrayList<>();
        actors.add("Actor1");
        actors.add("Actor2");
        List<String> genres = new ArrayList<>();
        genres.add("Horror");
        Movie movie = new Movie("Harry Potter", 2000, actors, "Director", "Writer", "Cinematographer", "Composer", genres);
        Node t = new Node("hi", movie);
        Term copy = t.createCopyTerm();
        assertEquals("hi", copy.getTerm());
    }
}