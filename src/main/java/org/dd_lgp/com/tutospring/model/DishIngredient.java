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
}
