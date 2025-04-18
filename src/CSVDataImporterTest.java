public class CSVDataImporterTest {

    @org.junit.Test
    public void importData() {
        CSVDataImporter importer = new CSVDataImporter();
        importer.importData("tmdb_5000_movies.csv");
    }
}