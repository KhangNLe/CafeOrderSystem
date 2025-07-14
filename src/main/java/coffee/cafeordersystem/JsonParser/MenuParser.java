package coffee.cafeordersystem.JsonParser;



import java.util.*;
public class MenuParser {
    private MenuParser() {}

    public static void initializeMenuPastries(List<String> pastriesFilePaths) {
        for (String filePath : pastriesFilePaths) {
            PastriesParser.parsePastries(filePath);
        }
    }

    public static void initializeMenuBeverages(List<String> beverageFilePaths) {
        for (String filePath : beverageFilePaths) {

        }
    }
}
