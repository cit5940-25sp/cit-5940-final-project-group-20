import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Cleaning class that cleans the data by replacing unicode with corresponding ASCII characters
 * and checking for unusual characters
 */
public class DataCleaner {
    /**
     * Cleans string by replacing a specific Unicode with the matching ASCII char
     *
     * @param origional the original string containing Unicode
     * @return a cleaned string that only has ASCII char
     */
    public static String clean(String origional){
        //replacing with plain letter e -> people can type it in
        String result = origional.replaceAll("u00e9", "e");
        result = result.replaceAll("u00c8", "E");
         result = result.replaceAll("u00e1", "a");
         result = result.replaceAll("u00f1", "n");
         result = result.replaceAll("u00f3", "o");
         //added onto additional testing
        result = result.replaceAll("u043D", "H");
        result = result.replaceAll("u0438", "n");
        result = result.replaceAll("u0433", "r");
        result = result.replaceAll("u0440", "p");
        result = result.replaceAll("u044C", "b");


        return result;
    }

    /**
     * Checks whether the input string contains additional unicode that fits into the pattern
     * "uXXXX" where X is a hexadecimal
     *
     * @param original the string to check
     * @return true if the string contains any unicode, false otherwise
     */
    public static boolean hasWeirdChar(String original) {
        Pattern p = Pattern.compile("u[0-9a-fA-F]{4}");
        Matcher m = p.matcher(original);
        return m.find();
    }


}
