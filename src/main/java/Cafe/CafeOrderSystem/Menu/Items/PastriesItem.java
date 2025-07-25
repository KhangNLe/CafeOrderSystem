package Cafe.CafeOrderSystem.Menu.Items;

import Cafe.CafeOrderSystem.CatalogItems.MenuType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PastriesItem(
        String id,
        String name,
        MenuType type,
        PastriesCost cost
){
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PastriesItem{");
        sb.append("id='").append(id).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", typeName='").append(type).append('\'');
        sb.append(", cost=").append(cost);
        sb.append('}');
        return sb.toString();
    }
}