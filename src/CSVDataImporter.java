import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class CSVDataImporter implements IDataImporter{

    @Override
    public List<Movie> importData(String file) {
        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            // Read and print the header
            String[] header = reader.readNext();
            if (header != null) {
                System.out.println("\nHeader: " + String.join(", ", header));
            }

            // Read and print each line
            String[] nextLine;
            int rowNumber = 1;

            System.out.println("\nData read from CSV file:");
            while ((nextLine = reader.readNext()) != null) {
                System.out.println("Row " + rowNumber + ":");
                /*for (int i = 0; i < nextLine.length; i++) {
                    System.out.println("  " + header[i] + ": " + nextLine[i]);
                }*/
                String genrejson = nextLine[1];
                JSONArray genrearray = new JSONArray(genrejson);

                System.out.println(((JSONObject)genrearray.get(0)).getString("name"));
                rowNumber++;
                System.out.println();
            }

        } catch (IOException | CsvValidationException e) {
            System.err.println("Error reading the CSV file: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private boolean hasHeader(){
        return false;
    }
}

