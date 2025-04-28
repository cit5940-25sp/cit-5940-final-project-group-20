public class DataCleaner {
    public static String clean(String origional){
        //replacing with plain letter e -> people can type it in
        String result = origional.replaceAll("u00e9", "e");
        result = result.replaceAll("u00c8", "E");
         result = result.replaceAll("u00e1", "a");
         result = result.replaceAll("u00f1", "n");
         result = result.replaceAll("u00f3", "o");



        return result;
    }

}
