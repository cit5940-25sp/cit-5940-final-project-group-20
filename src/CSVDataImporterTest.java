
import static org.junit.Assert.*;

import java.io.*;
import java.util.*;
public class CSVDataImporterTest {

    @org.junit.Test
    public void importData() {
        CSVDataImporter importer = new CSVDataImporter();
        Map<Integer,Movie> temp =  importer.importDataMovie("tmdb_5000_movies.csv");
        assertTrue(temp.containsKey(767));
        Movie t = temp.get(767);
        assertEquals("Harry Potter and the Half-Blood Prince", t.getTitle());
        assertEquals(2009, t.getReleaseYear());
    }

    @org.junit.Test
    public void importDataNoFile() {
        try {
            CSVDataImporter importer = new CSVDataImporter();
            Map<Integer, Movie> temp = importer.importDataMovie("wrong.csv");
            fail("expected fail");
        }
        catch(RuntimeException e){
            assertTrue(e.getMessage().contains("No such file or directory"));
        }

    }

    @org.junit.Test
    public void importCreditNoFile() {
        try {
            CSVDataImporter importer = new CSVDataImporter();
            HashMap<Integer, Movie> movie = new HashMap<>();
            Map<Integer, Movie> temp = importer.importDataCredit("wrong.csv", movie);
            fail("expected fail");
        }
        catch(RuntimeException e){
            assertTrue(e.getMessage().contains("No such file or directory"));
        }

    }

    @org.junit.Test
    public void importDataEmptyFile() {
            CSVDataImporter importer = new CSVDataImporter();
            Map<Integer, Movie> temp = importer.importDataMovie("testempty.csv");
            assertTrue(temp.isEmpty());
    }

    @org.junit.Test
    public void importDataInvalidGenreFile() {
        CSVDataImporter importer = new CSVDataImporter();
        Map<Integer, Movie> temp = importer.importDataMovie("testgenre.csv");
        assertTrue(temp.isEmpty());
    }

    @org.junit.Test
    public void importDataInvalidDateFile() {
        CSVDataImporter importer = new CSVDataImporter();
        Map<Integer, Movie> temp = importer.importDataMovie("testdate.csv");
        assertTrue(temp.isEmpty());
    }

    @org.junit.Test
    public void importDataInvalidCastFile() {
        CSVDataImporter importer = new CSVDataImporter();
        HashMap<Integer, Movie> movies = new HashMap<>();
        Movie movie = new Movie("Avatar", 2000, new ArrayList<>(), "Director", "Writer", "Cinematographer", "Composer", new ArrayList<>());
        movies.put(19995, movie);
        assertFalse(movies.isEmpty());
        Map<Integer, Movie> temp = importer.importDataCredit("testcast.csv", movies);
        assertTrue(temp.isEmpty());
    }

    @org.junit.Test
    public void importDataInvalidCrewFile() {
        CSVDataImporter importer = new CSVDataImporter();
        HashMap<Integer, Movie> movies = new HashMap<>();
        Movie movie = new Movie("Avatar", 2000, new ArrayList<>(), "Director", "Writer", "Cinematographer", "Composer", new ArrayList<>());
        movies.put(19995, movie);
        assertFalse(movies.isEmpty());
        Map<Integer, Movie> temp = importer.importDataCredit("testcrew.csv", movies);
        assertTrue(temp.isEmpty());
    }
    @org.junit.Test
    public void importDataNoRealIDFile() {
        CSVDataImporter importer = new CSVDataImporter();
        HashMap<Integer, Movie> movies = new HashMap<>();
        Movie movie = new Movie("Avatar", 2000, new ArrayList<>(), "Director", "Writer", "Cinematographer", "Composer", new ArrayList<>());
        movies.put(19995, movie);
        assertFalse(movies.isEmpty());
        Map<Integer, Movie> temp = importer.importDataCredit("testnorealID.csv", movies);
        assertFalse(temp.isEmpty());
    }

    @org.junit.Test
    public void importCredit() {
        CSVDataImporter importer = new CSVDataImporter();
        Map<Integer, Movie> temp = importer.importDataMovie("tmdb_5000_movies.csv");
        Map<Integer, Movie> movies = importer.importDataCredit("tmdb_5000_credits.csv", temp);
        Movie t = temp.get(767);
        assertEquals("David Yates", t.getDirector());
        //assertEquals("", t.getReleaseYear());
    }

    @org.junit.Test
    public void TestDataCleaner() {
        assertEquals("Renee Zellweger", DataCleaner.clean("Renu00e9e Zellweger"));
    }


    @org.junit.Test
    public void testspecialchars() {
        CSVDataImporter importer = new CSVDataImporter();
        Map<Integer, Movie> temp = importer.importDataMovie("tmdb_5000_movies.csv");
        Map<Integer, Movie> movies = importer.importDataCredit("tmdb_5000_credits.csv", temp);
        Movie t = temp.get(16911);
        assertEquals("The Inhabited Island", t.getTitle());
        assertEquals(2008, t.getReleaseYear());
        List<String> actors = t.getActors();
        assertTrue(!actors.isEmpty());
    }
}