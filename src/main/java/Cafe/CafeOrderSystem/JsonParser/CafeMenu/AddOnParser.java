package Cafe.CafeOrderSystem.JsonParser.CafeMenu;

import Cafe.CafeOrderSystem.JsonParser.ItemsParser;
import Cafe.CafeOrderSystem.JsonParser.JsonCollection;
import Cafe.CafeOrderSystem.Menu.Items.CustomItem;

public class AddOnParser extends JsonCollection<CustomItem> {
    private AddOnParser(ItemsParser parser, String dirPath, Class<CustomItem> itemClass) {
        super(parser, dirPath, itemClass);
    }

    public static AddOnParser create(ItemsParser parser, String dirPath,
                                     Class<CustomItem> itemClass) {
        return new AddOnParser(parser, dirPath, itemClass);
    }
}
