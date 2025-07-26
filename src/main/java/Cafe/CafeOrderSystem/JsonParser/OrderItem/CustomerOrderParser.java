package Cafe.CafeOrderSystem.JsonParser.OrderItem;

import Cafe.CafeOrderSystem.JsonParser.ItemsParser;
import Cafe.CafeOrderSystem.JsonParser.JsonCollection;
import Cafe.CafeOrderSystem.Orders.CustomerOrder;

public class CustomerOrderParser extends JsonCollection<CustomerOrder> {
    private CustomerOrderParser(ItemsParser parser, String dirPath, Class<CustomerOrder> itemClass) {
        super(parser, dirPath, itemClass);
    }

    public static CustomerOrderParser create(ItemsParser parser, String dirPath,
                                             Class<CustomerOrder> itemClass) {
        return new CustomerOrderParser(parser, dirPath, itemClass);
    }

    public void completeOrder(CustomerOrder order){
        super.addObject(order);
    }
}
