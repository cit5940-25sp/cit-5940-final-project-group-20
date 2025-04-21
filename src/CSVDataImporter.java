import com.opencsv.CSVReader;
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
            // Read and print the header
            String[] header = reader.readNext();
            if (header != null) {
                System.out.println("\nHeader: " + String.join(", ", header));
            }

            // Read and print each line
            String[] nextLine;
            int rowNumber = 1;

            while ((nextLine = reader.readNext()) != null) {

                /*for (int i = 0; i < nextLine.length; i++) {
                    System.out.println("  " + header[i] + ": " + nextLine[i]);
                }*/
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
                Date date = null;
                try {
                    date = parser.parse(releasedate);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                int releaseYear = date.getYear() + 1900;
                List<String> genres = new ArrayList<>();
                for (int i = 0; i < genrearray.length(); i++) {
                    String genrename = ((JSONObject) genrearray.get(i)).getString("name");
                    genres.add(genrename);
                }
                Movie movie = new Movie(title, releaseYear, null, null,
                        null, null, null, genres);

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
            String[] header = reader.readNext();
            if (header != null) {
                System.out.println("\nHeader: " + String.join(", ", header));
            }

            // Read and print each line
            String[] nextLine;
            int rowNumber = 1;

            while ((nextLine = reader.readNext()) != null) {
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
                    String crewname = ((JSONObject) crewarray.get(i)).getString("name");
                    String crewjob = ((JSONObject) crewarray.get(i)).getString("job");
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


    private boolean hasHeader() {

        return false;
    }
}

