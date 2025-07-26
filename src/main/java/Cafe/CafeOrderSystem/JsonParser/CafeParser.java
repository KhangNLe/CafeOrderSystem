package Cafe.CafeOrderSystem.JsonParser;


import java.util.ArrayList;
import java.util.List;

public class CafeParser {
    List<Parsers> cafeMenuParser;
    ItemsParser parser;

    public CafeParser(){
        parser = new ItemsParser();
        cafeMenuParser = new ArrayList<>();
    }

    public void openCafeShop(){
        cafeMenuParser.forEach(Parsers::startCollection);
    }

    public void addParser(Parsers parser){
        cafeMenuParser.add(parser);
    }

}
