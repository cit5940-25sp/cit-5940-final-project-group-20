import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class TermTest {

    @org.junit.Test
    public void testCompareTo() {
        List<String> actors = new ArrayList<>();
        actors.add("Actor1");
        actors.add("Actor2");
        List<String> genres = new ArrayList<>();
        genres.add("Horror");
        Movie movie = new Movie("Harry Potter", 2000, actors, "Director", "Writer", "Cinematographer", "Composer", genres);
        Term t1 = new Term("hi",movie);
        Term t2 = new Term("hi",movie);
        assertEquals(0,t1.compareTo(t2));

    }



    @org.junit.Test
    public void testCompareToGreaterString() {
        List<String> actors = new ArrayList<>();
        actors.add("Actor1");
        actors.add("Actor2");
        List<String> genres = new ArrayList<>();
        genres.add("Horror");
        Movie movie = new Movie("Harry Potter", 2000, actors, "Director", "Writer", "Cinematographer", "Composer", genres);
        Movie movie1 = new Movie("Movie One", 2000, Arrays.asList("Actor A"), "Director A", "Writer A", "Cinematographer A", "Composer A", Arrays.asList("Action", "Thriller"));
        Term t1 = new Term("bye",movie);
        Term t2 = new Term("hi",movie1);
        assertTrue(t1.compareTo(t2) < 0);

    }


    @org.junit.Test
    public void testMovie() {
        List<String> actors = new ArrayList<>();
        actors.add("Actor1");
        actors.add("Actor2");
        List<String> genres = new ArrayList<>();
        genres.add("Horror");
        Movie movie = new Movie("Harry Potter", 2000, actors, "Director", "Writer", "Cinematographer", "Composer", genres);
        Term t1 = new Term("bye",movie);
        assertEquals(movie, t1.getMovie());
        String hopeful = "Harry Potter (2000)";
        assertEquals(hopeful, t1.toString());
    }

    @org.junit.Test
    public void testConstructorWithNullTerm() {
        Movie movie = new Movie("Title", 2000, Arrays.asList("Actor"), "Director", "Writer", "Cinematographer", "Composer", Arrays.asList("Genre"));
        try {
            new Term(null, movie);
            fail("Wrong illegal argument exception when null = 0");
        } catch (IllegalArgumentException e) {
            assertEquals("Illegal argument term = 0 ", e.getMessage());
        }
    }

    @org.junit.Test
    public void testGetTerm() {
        Movie movie = new Movie("Test", 2001, Arrays.asList("Actor"), "Director", "Writer", "Cinematographer", "Composer", Arrays.asList("Genre"));
        Term term = new Term("Search", movie);
        assertEquals("Search", term.getTerm());
    }

    //adding compare to
    @org.junit.Test
    public void testCompareToLessThan() {
        Movie movie = new Movie("M", 2000, Arrays.asList("A"), "D", "W", "C", "C", Arrays.asList("G"));
        Term t1 = new Term("abc", movie);
        Term t2 = new Term("bcd", movie);
        assertEquals(true, t1.compareTo(t2) < 0);
    }

    @org.junit.Test
    public void testCompareToGreaterThan() {
        Movie movie = new Movie("M", 2000, Arrays.asList("A"), "D", "W", "C", "C", Arrays.asList("G"));
        Term t1 = new Term("z", movie);
        Term t2 = new Term("y", movie);
        assertEquals(true, t1.compareTo(t2) > 0);
    }
}