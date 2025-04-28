
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

}