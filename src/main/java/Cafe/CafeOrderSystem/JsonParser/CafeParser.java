package Cafe.CafeOrderSystem.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class CafeParser {
    List<Parsers> cafeMenuParser;

    public CafeParser(){
        cafeMenuParser = new ArrayList<>();
    }

    public void openCafeShop(){
        cafeMenuParser.forEach(Parsers::startCollection);
    }

    public void addParser(Parsers parser){
        cafeMenuParser.add(parser);
    }

    public void closeShop(){
        cafeMenuParser.forEach(Parsers::endCollection);
    }

    public <T extends Parsers> T getParserType(Class<T> type){
        return cafeMenuParser.stream()
                .filter(type::isInstance)
                .map(type::cast)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("No parser found for type %s", type.getSimpleName())
                ));
    }
}
