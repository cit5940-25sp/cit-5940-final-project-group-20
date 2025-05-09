
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

//import static org.junit.Assert.*;

public class AutocompleteTest {
    @org.junit.Test
    public void testValidChars() {
        Autocomplete auto = new Autocomplete();
        assertTrue(auto.validChars("hi"));
    }

    @org.junit.Test
    public void testAddWord() {
        List<String> actors = new ArrayList<>();
        actors.add("Actor1");
        actors.add("Actor2");
        List<String> genres = new ArrayList<>();
        genres.add("Horror");
        Movie movie = new Movie("Harry Potter", 2000, actors, "Director", "Writer", "Cinematographer", "Composer", genres);
        //autocomplete object
        Autocomplete auto = new Autocomplete();
        //call addword method, some string and weight
        auto.addWord("hello",movie);
        // assertions on autocomplete
        assertNotEquals(null,auto.getRoot().getReferences()[104]);
    }

    @org.junit.Test
    public void testBuildTrie() {
        Autocomplete auto = new Autocomplete();
        CSVDataImporter importer = new CSVDataImporter();
        Map<Integer, Movie> moviemap = importer.importDataMovie("tmdb_5000_movies.csv");
        auto.buildTrie(moviemap.values());
        assertNotEquals(null,auto.getRoot().getReferences()[97]);
        assertNotEquals(null,auto.getRoot().getReferences()[112]);
        assertNull(auto.getRoot().getReferences()[95]);
    }

    @org.junit.Test
    public void testGetSubTrie() {
        Autocomplete auto = new Autocomplete();
        CSVDataImporter importer = new CSVDataImporter();
        Map<Integer, Movie> moviemap = importer.importDataMovie("tmdb_5000_movies.csv");
        auto.buildTrie(moviemap.values());
        assertNull(auto.getSubTrie("abcdefgh"));
        assertNotEquals(null,auto.getSubTrie("Star"));
    }

    @org.junit.Test
    public void testGetSuggestions() {
        Autocomplete auto = new Autocomplete();
        CSVDataImporter importer = new CSVDataImporter();
        Map<Integer, Movie> moviemap = importer.importDataMovie("tmdb_5000_movies.csv");
        auto.buildTrie(moviemap.values());

        List<ITerm> suggestions;
        suggestions = auto.getSuggestions("Harry Potter");assertEquals(6, suggestions.size());
        assertEquals("Harry Potter and the Chamber of Secrets", suggestions.get(0).getTerm());
    }
}