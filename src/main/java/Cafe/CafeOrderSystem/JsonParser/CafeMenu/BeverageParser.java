package Cafe.CafeOrderSystem.JsonParser.CafeMenu;

import Cafe.CafeOrderSystem.JsonParser.ItemsParser;
import Cafe.CafeOrderSystem.JsonParser.JsonCollection;
import Cafe.CafeOrderSystem.Menu.Items.BeverageItem;

public class BeverageParser extends JsonCollection<BeverageItem> {
    private BeverageParser(ItemsParser parser, String dirPath, Class<BeverageItem> itemClass) {
        super(parser, dirPath, itemClass);
    }

    public static BeverageParser create(String dirPath) {
        return new BeverageParser(new ItemsParser(), dirPath, BeverageItem.class);
    }
}
