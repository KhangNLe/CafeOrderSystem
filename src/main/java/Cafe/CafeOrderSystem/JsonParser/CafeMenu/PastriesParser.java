package Cafe.CafeOrderSystem.JsonParser.CafeMenu;

import Cafe.CafeOrderSystem.JsonParser.ItemsParser;
import Cafe.CafeOrderSystem.JsonParser.JsonCollection;
import Cafe.CafeOrderSystem.Menu.Items.PastriesItem;

public class PastriesParser extends JsonCollection<PastriesItem> {
    private PastriesParser(ItemsParser parser, String dirPath, Class<PastriesItem> itemClass) {
        super(parser, dirPath, itemClass);
    }

    public static PastriesParser create(ItemsParser parser, String dirPath,
                                        Class<PastriesItem> itemClass) {
        return new PastriesParser(parser, dirPath, itemClass);
    }
}
