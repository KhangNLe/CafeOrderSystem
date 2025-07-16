package Cafe.CafeOrderSystem.Menu.Items;

import Cafe.CafeOrderSystem.CatalogItems.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BeverageItem(
        String id,
        String name,
        MenuType type,
        Map<BeverageSize, BeverageCost> cost
){
        @Override
        public String toString() {
                StringBuilder sb = new StringBuilder();
                sb.append("BeverageItem{");
                sb.append("id='").append(id).append('\'');
                sb.append(", name='").append(name).append('\'');
                sb.append(", typeName='").append(type).append('\'');
                cost.forEach((beverageSize, beverageCost) -> {
                        sb.append(", beverageSize=").append(beverageSize);
                        sb.append(", beverageCost=").append(beverageCost);
                });
                sb.append('}');
                return sb.toString();
        }
}