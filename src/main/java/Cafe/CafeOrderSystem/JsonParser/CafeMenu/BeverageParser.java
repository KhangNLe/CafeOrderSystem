package Cafe.CafeOrderSystem.JsonParser.CafeMenu;

import Cafe.CafeOrderSystem.JsonParser.ItemsParser;
import Cafe.CafeOrderSystem.JsonParser.JsonCollection;
import Cafe.CafeOrderSystem.Menu.Items.BeverageItem;

public class BeverageParser extends JsonCollection<BeverageItem> {
    private BeverageParser(ItemsParser parser, String dirPath, Class<BeverageItem> itemClass) {
        super(parser, dirPath, itemClass);
    }

    public static BeverageParser create(ItemsParser parser, String dirPath,
                                        Class<BeverageItem> itemClass) {
        return new BeverageParser(parser, dirPath, itemClass);
    }
}
