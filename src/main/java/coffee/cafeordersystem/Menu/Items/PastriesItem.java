package coffee.cafeordersystem.Menu.Items;

import coffee.cafeordersystem.CatalogItems.Pastries;
import coffee.cafeordersystem.Ingredients.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PastriesItem(
        String id, String name, Pastries type, PastriesCost cost
){

}
