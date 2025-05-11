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

/**
 * Reads movie and credit data from CSV files and takes out what is needed
 * Implements the {@link IDataImporter} interface to build a map with movie and ID
 */

public class CSVDataImporter implements IDataImporter {

    /**
     * Gets the movie data, such as title, genres, release year from a CSV file
     *
     * @param file the movie CSV file
     * @return a map of movie ID to {@link Movie} objects
     */
    @Override
    public Map<Integer, Movie> importDataMovie(String file) {
        Map<Integer, Movie> hash = new HashMap<>();
        try {
            //trying to read and print the header
            String[] header;
            CSVReader reader = new CSVReader(new FileReader(file));
            //try catch to get all the exceptions
            try {
                try {
                    header = reader.readNext();
                } catch (CsvValidationException e) {
                    throw new RuntimeException(e);
                }
                //if header is not null, print it out
                if (header != null) {
                    System.out.println("\nHeader: " + String.join(", ", header));
                }
                //catching another exception
            } catch (IOException e) {
                System.err.println("CSV validation error while reading header: " + e.getMessage());
                e.printStackTrace();
                return hash;
            }

            String[] nextLine;
            int rowNumber = 1;

            //reading the CSV file line by line
            while (true) {
                try {
                    try {
                        //trying for the next line
                        nextLine = reader.readNext();
                    } catch (CsvValidationException e) {
                        throw new RuntimeException(e);
                    }
                    //leave loop if end of file
                    if (nextLine == null) break;
                    //catching another exception
                } catch (IOException e) {
                    System.err.println("CSV validation error while reading row " + rowNumber + ": " + e.getMessage());
                    e.printStackTrace();
                    rowNumber++;
                    continue;
                }
                //getting the genre
                String genrejson = nextLine[1];
                JSONArray genrearray;
                try {
                    //using JSON to parse the genre
                    genrearray = new JSONArray(genrejson);
                }
                //catching an exception
                catch (JSONException e) {
                    System.err.println("Skipping row " + rowNumber + " due to invalid genre JSON: " + genrejson);
                    rowNumber++;
                    continue;
                }
                //not expecting any wording problems so no additional checking

                //getting the title of movie
                String title = nextLine[17];
                //cleaning title
                title = DataCleaner.clean(title);
                //checking for weird chars
                if(DataCleaner.hasWeirdChar(title)){
                    continue;
                }
                //getting the releasedate
                String releasedate = nextLine[11];
                if (releasedate.length() == 0) {
                    rowNumber++;
                    continue;
                }

                //getting the movie ID
                int id = Integer.parseInt(nextLine[3]);
                //formating the releasedate
                SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
                Date date;
                try {
                    date = parser.parse(releasedate);
                    //catch another exception
                } catch (ParseException e) {
                    System.err.println("Skipping row " + rowNumber + " due to invalid date: " + releasedate);
                    rowNumber++;
                    continue;
                }
                //just release year
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int releaseYear = calendar.get(Calendar.YEAR);
                //creating a list to store genres in movie
                List<String> genres = new ArrayList<>();
                for (int i = 0; i < genrearray.length(); i++) {
                    String genrename = ((JSONObject) genrearray.get(i)).getString("name");
                    //clean genre name
                    genres.add(DataCleaner.clean(genrename));
                }

                //used for testing
                //System.out.println(title);
                //movie object constructed
                Movie movie = new Movie(DataCleaner.clean(title), releaseYear, null, null, null, null, null, genres);
                hash.put(id, movie);
                rowNumber++;
            }

            //catch another exception
        } catch (IOException e) {
            System.err.println("Error reading the CSV file: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return hash;
    }

    /**
     * Imports cast and crew information from tmbd_5000_credits.csv and adds to existing movie map
     *
     * @param file   the CSV file
     * @param movies the already there map of movies
     * @return the updated map with cast and crew data
     */
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
                //catching for exceptions
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
                    //catching another exception
                } catch (IOException e) {
                    System.err.println("CSV validation error while reading row " + rowNumber + ": " + e.getMessage());
                    e.printStackTrace();
                    rowNumber++;
                    continue;
                }

                int id;
                try {
                    //getting movie id
                    id = Integer.parseInt(nextLine[0]);
                } catch (NumberFormatException e) {
                    System.err.println("Skipping credit row " + rowNumber + " due to invalid ID: " + nextLine[0]);
                    continue;
                }

                //checking that movie ID is real
                if (!movies.containsKey(id)) {
                    System.err.println("Skipping credit row " + rowNumber + " - Movie ID " + id + " not found in movie map.");
                    continue;
                }
                String castjson = nextLine[2];
                JSONArray castarray;
                //System.out.println("Movie id " + id);
                try {
                    //getting the cast information from the column
                    castarray = new JSONArray(castjson);
                } catch (org.json.JSONException e) {
                    movies.remove(id);
                    continue;
                }

                //initializing the list for actors - empty
                List<String> actors = new ArrayList<>();

                //iterating through castarray
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

                //getting the crew information
                String crewjson = nextLine[3];
                JSONArray crewarray;
                try {
                    crewarray = new JSONArray(crewjson);
                    //catching the exception
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

                //looping through each crew member and add to it if it is the certain crew like director, writer, cinematographer,...
                for (int i = 0; i < crewarray.length(); i++) {
                    //cleaning
                    String crewname = DataCleaner.clean(((JSONObject) crewarray.get(i)).getString("name"));
                    String crewjob = DataCleaner.clean(((JSONObject) crewarray.get(i)).getString("job"));
                    //if weird char skip
                    if(DataCleaner.hasWeirdChar(crewname)){
                        continue;
                    }
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
                    }
                }
                //add the cast and crew information to the movie object
                movies.get(id).setActors(actors);
                movies.get(id).setDirector(director);
                movies.get(id).setWriter(writer);
                movies.get(id).setCinematographer(cinematographer);
                movies.get(id).setComposer(composer);

                rowNumber++;
            }

            //catch the exception
        } catch (IOException e) {
            System.err.println("Error reading the CSV file: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return movies;
    }
}

