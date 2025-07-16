package Cafe.CafeOrderSystem.Menu.Items;

import Cafe.CafeOrderSystem.CatalogItems.Pastries;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PastriesItem(
        String id, String name, Pastries type, PastriesCost cost
){

}
