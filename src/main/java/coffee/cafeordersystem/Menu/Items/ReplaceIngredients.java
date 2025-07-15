package coffee.cafeordersystem.Menu.Items;

import coffee.cafeordersystem.CatalogItems.Ingredients;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ReplaceIngredients (
   Ingredients replaceWith
){}
