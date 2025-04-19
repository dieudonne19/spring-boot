package org.dd_lgp.com.tutospring.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode
@ToString
public class DishIngredient {
    private Long id;
    private Ingredient ingredient;
    private Double requiredQuantity;
    private Unit unit;

    public DishIngredient setId(Long id) {
        this.id = id;
        return this;
    }

    public DishIngredient setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
        return this;
    }

    public DishIngredient setRequiredQuantity(Double requiredQuantity) {
        this.requiredQuantity = requiredQuantity;
        return this;
    }

    public DishIngredient setUnit(Unit unit) {
        this.unit = unit;
        return this;
    }
}
