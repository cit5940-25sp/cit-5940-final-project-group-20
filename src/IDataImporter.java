import java.util.List;
import java.util.Map;

public interface IDataImporter {

    public Map<Integer, Movie> importDataMovie(String file);

    public Map<Integer, Movie> importDataCredit(String file, Map<Integer, Movie> movies);
}


