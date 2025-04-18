package org.dd_lgp.com.tutospring.endpoint.rest;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dd_lgp.com.tutospring.model.Ingredient;
import org.dd_lgp.com.tutospring.model.Unit;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DishIngredientRest {
    private Long id;
    private Double requiredQuantity;
    private Unit unit;
}
