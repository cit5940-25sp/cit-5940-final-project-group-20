import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvMalformedLineException;
import com.opencsv.exceptions.CsvValidationException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CSVDataImporter implements IDataImporter {

    @Override
    public Map<Integer, Movie> importDataMovie(String file) {
        Map<Integer, Movie> hash = new HashMap<>();
        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            String[] header;
            try {
                try {
                    header = reader.readNext();
                } catch (CsvValidationException e) {
                    throw new RuntimeException(e);
                }
                if (header != null) {
                    System.out.println("\nHeader: " + String.join(", ", header));
                }
            } catch (IOException e) {
                System.err.println("CSV validation error while reading header: " + e.getMessage());
                e.printStackTrace();
                return hash;
            }

            String[] nextLine;
            int rowNumber = 1;

            while (true) {
                try {
                    try {
                        nextLine = reader.readNext();
                    } catch (CsvValidationException e) {
                        throw new RuntimeException(e);
                    }
                    if (nextLine == null) break;
                } catch (IOException e) {
                    System.err.println("CSV validation error while reading row " + rowNumber + ": " + e.getMessage());
                    e.printStackTrace();
                    rowNumber++;
                    continue;
                }

                String genrejson = nextLine[1];
                JSONArray genrearray;
                try {
                    genrearray = new JSONArray(genrejson);
                } catch (JSONException e) {
                    System.err.println("Skipping row " + rowNumber + " due to invalid genre JSON: " + genrejson);
                    rowNumber++;
                    continue;
                }


                String title = nextLine[17];
                String releasedate = nextLine[11];
                if (releasedate.length() == 0) {
                    rowNumber++;
                    continue;
                }

                int id = Integer.parseInt(nextLine[3]);
                SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
                Date date;
                try {
                    date = parser.parse(releasedate);
                } catch (ParseException e) {
                    System.err.println("Skipping row " + rowNumber + " due to invalid date: " + releasedate);
                    rowNumber++;
                    continue;
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int releaseYear = calendar.get(Calendar.YEAR);
                List<String> genres = new ArrayList<>();
                for (int i = 0; i < genrearray.length(); i++) {
                    String genrename = ((JSONObject) genrearray.get(i)).getString("name");
                    genres.add(DataCleaner.clean(genrename));
                }

                //used for testing
                //System.out.println(title);
                Movie movie = new Movie(DataCleaner.clean(title), releaseYear, null, null, null, null, null, genres);
                hash.put(id, movie);
                rowNumber++;
            }

        } catch (IOException e) {
            System.err.println("Error reading the CSV file: " + e.getMessage());
            e.printStackTrace();
        }
        return hash;
    }


    public Map<Integer, Movie> importDataCredit(String file, Map<Integer, Movie> movies) {
        try (CSVReader reader = new CSVReader(new FileReader(file))) {

            // Read and print the header
            String[] header;
            try {
                try {
                    header = reader.readNext();
                } catch (CsvValidationException e) {
                    throw new RuntimeException(e);
                }
                if (header != null) {
                    System.out.println("\nHeader: " + String.join(", ", header));
                }
            } catch (IOException e) {
                System.err.println("CSV validation error while reading header: " + e.getMessage());
                e.printStackTrace();
                return movies;
            }

            // Read and print each line
            String[] nextLine;
            int rowNumber = 1;

            while (true) {
                try {
                    try {
                        nextLine = reader.readNext();
                    } catch (CsvValidationException e) {
                        //right now skipping if error is being thrown
                        continue;
                    } catch (CsvMalformedLineException e) {
                        //right now skipping if error is being thrown
                        continue;
                    }

                    if (nextLine == null) break;
                } catch (IOException e) {
                    System.err.println("CSV validation error while reading row " + rowNumber + ": " + e.getMessage());
                    e.printStackTrace();
                    rowNumber++;
                    continue;
                }

                int id = Integer.parseInt(nextLine[0]);
                try {
                    id = Integer.parseInt(nextLine[0]);
                } catch (NumberFormatException e) {
                    System.err.println("Skipping credit row " + rowNumber + " due to invalid ID: " + nextLine[0]);
                    continue;
                }

                if (!movies.containsKey(id)) {
                    System.err.println("Skipping credit row " + rowNumber + " - Movie ID " + id + " not found in movie map.");
                    continue;
                }
                String castjson = nextLine[2];
                JSONArray castarray;
                //System.out.println("Movie id " + id);
                try {
                    castarray = new JSONArray(castjson);
                } catch (org.json.JSONException e) {
                    movies.remove(id);
                    continue;
                }

                List<String> actors = new ArrayList<>();

                for (int i = 0; i < castarray.length(); i++) {
                    String castname = ((JSONObject) castarray.get(i)).getString("name");
                    //cleaning
                    castname = DataCleaner.clean(castname);
                    //cleaning for additional chars
                    if(DataCleaner.hasWeirdChar(castname)){
                        continue;
                    }
                    actors.add(castname);
                }

                String crewjson = nextLine[3];
                JSONArray crewarray;
                try {
                    crewarray = new JSONArray(crewjson);
                } catch (JSONException e) {
                    System.err.println("Skipping row " + rowNumber + " due to invalid crew JSON: " + crewjson);
                    movies.remove(id);
                    rowNumber++;
                    continue;
                }
                String director = null;
                String writer = null;
                String cinematographer = null;
                String composer = null;

                for (int i = 0; i < crewarray.length(); i++) {
                    String crewname = DataCleaner.clean(((JSONObject) crewarray.get(i)).getString("name"));
                    String crewjob = DataCleaner.clean(((JSONObject) crewarray.get(i)).getString("job"));
                    if (crewjob.equalsIgnoreCase("director")) {
                        director = crewname;
                        continue;
                    }
                    if (crewjob.equalsIgnoreCase("writer")) {
                        writer = crewname;
                        continue;
                    }
                    if (crewjob.equalsIgnoreCase("cinematographer")) {
                        cinematographer = crewname;
                        continue;
                    }
                    if (crewjob.equalsIgnoreCase("composer")) {
                        composer = crewname;
                        continue;
                    }
                }
                movies.get(id).setActors(actors);
                movies.get(id).setDirector(director);
                movies.get(id).setWriter(writer);
                movies.get(id).setCinematographer(cinematographer);
                movies.get(id).setComposer(composer);

                rowNumber++;
            }

        } catch (IOException e) {
            System.err.println("Error reading the CSV file: " + e.getMessage());
            e.printStackTrace();
        }
        return movies;
    }
}

