import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;
import static org.junit.Assert.*;

public class ActorConnectionTest {

    private ActorConnection actorConnection;
    private Movie movieA;
    private Movie movieB;
    private Movie movieC;

    @Before
    public void setUp() {
        actorConnection = new ActorConnection();

        movieA = new Movie(
                "Movie A", 2000,
                Arrays.asList("Actor 1", "Actor 2"),
                "Director A", "Writer A", "Cinematographer A", "Composer A",
                Arrays.asList("Action")
        );

        movieB = new Movie(
                "Movie B", 2005,
                Arrays.asList("Actor 2", "Actor 3"),
                "Director B", "Writer B", "Cinematographer B", "Composer B",
                Arrays.asList("Drama")
        );

        movieC = new Movie(
                "Movie C", 2010,
                Arrays.asList("Actor X", "Actor Y"),
                "Director C", "Writer C", "Cinematographer C", "Composer C",
                Arrays.asList("Thriller")
        );
    }

    @Test
    public void testAreConnectedWithSharedActor() {
        assertTrue(actorConnection.areConnected(movieA, movieB)); // Actor 2 shared
    }

    @Test
    public void testAreConnectedWithNoSharedActor() {
        assertFalse(actorConnection.areConnected(movieA, movieC)); // no overlap
    }

    @Test
    public void testAreConnectedWhenOneListIsEmpty() {
        Movie movieEmpty = new Movie(
                "Empty", 2020,
                Collections.emptyList(),
                "Dir", "Wr", "Cine", "Comp",
                Arrays.asList("Genre")
        );
        assertFalse(actorConnection.areConnected(movieA, movieEmpty));
    }

    @Test
    public void testAreConnectedWhenOneListIsNull() {
        Movie movieNullActors = new Movie(
                "Null", 2020,
                null,
                "Dir", "Wr", "Cine", "Comp",
                Arrays.asList("Genre")
        );
        assertFalse(actorConnection.areConnected(movieA, movieNullActors));
        assertFalse(actorConnection.areConnected(movieNullActors, movieA));
    }

    @Test
    public void testGetType() {
        assertEquals("Actor", actorConnection.getType());
    }


    @Test
    public void testGetSharedElementWithCommonActor() {
        // Actor 2 is common in movieA and movieB
        String shared = actorConnection.getSharedElement(movieA, movieB);
        assertEquals("Actor 2", shared);
    }


    @Test
    public void testGetSharedElementWithNullActorList() {
        Movie nullActorMovie = new Movie(
                "NullActor", 2021,
                null, "Dir", "Wr", "Cine", "Comp",
                Arrays.asList("Genre")
        );
        assertNull(actorConnection.getSharedElement(movieA, nullActorMovie));
        assertNull(actorConnection.getSharedElement(nullActorMovie, movieA));
    }


}


