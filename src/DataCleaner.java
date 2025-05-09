import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataCleaner {
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

    public static boolean hasWeirdChar(String orgional){
        Pattern p = Pattern.compile("u0[0-9abcdefABCDEF]{3}");
        Matcher m = p.matcher(orgional);
        return (m.find());
    }


}
