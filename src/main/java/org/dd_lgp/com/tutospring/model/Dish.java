package org.dd_lgp.com.tutospring.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode
@ToString
public class Dish {
    private Long id;
    private String name;

    @JsonProperty("ingredients")
    private List<DishIngredient> dishIngredients;
    @JsonProperty("actualPrice")
    private Double price;

    @JsonIgnore
    public Double getGrossMargin() {
        return getPrice() - getTotalIngredientsCost();
    }

    public Double getGrossMarginAt(LocalDate dateValue) {
        return getPrice() - getTotalIngredientsCostAt(dateValue);
    }

    @JsonIgnore
    public Double getTotalIngredientsCost() {
        return dishIngredients.stream()
                .map(dishIngredient -> {
                    Double actualPrice = dishIngredient.getIngredient().getActualPrice();
                    Double requiredQuantity = dishIngredient.getRequiredQuantity();
                    return actualPrice * requiredQuantity;
                })
                .reduce(0.0, Double::sum);
    }

    public Double getTotalIngredientsCostAt(LocalDate dateValue) {
        double cost = 0.0;
        for (DishIngredient dishIngredient : dishIngredients) {
            Double actualPrice = dishIngredient.getIngredient().getPriceAt(dateValue);
            Double requiredQuantity = dishIngredient.getRequiredQuantity();
            cost += actualPrice * requiredQuantity;
        }
        return cost;
    }

    public Double getAvailableQuantity() {
        List<Double> allQuantitiesPossible = new ArrayList<>();
        for (DishIngredient dishIngredient : dishIngredients) {
            Ingredient ingredient = dishIngredient.getIngredient();
            double quantityPossibleForThatIngredient = ingredient.getAvailableQuantity() / dishIngredient.getRequiredQuantity();
            double roundedQuantityPossible = Math.ceil(quantityPossibleForThatIngredient); // ceil for smallest
            allQuantitiesPossible.add(roundedQuantityPossible);
        }
        return allQuantitiesPossible.stream().min(Double::compare).orElse(0.0);
    }

    public Double getAvailableQuantityAt(Instant datetime) {
        List<Double> allQuantitiesPossible = new ArrayList<>();
        for (DishIngredient dishIngredient : dishIngredients) {
            Ingredient ingredient = dishIngredient.getIngredient();
            double quantityPossibleForThatIngredient = ingredient.getAvailableQuantityAt(datetime) / dishIngredient.getRequiredQuantity();
            double roundedQuantityPossible = Math.ceil(quantityPossibleForThatIngredient); // ceil for smallest
            allQuantitiesPossible.add(roundedQuantityPossible);
        }
        return allQuantitiesPossible.stream().min(Double::compare).orElse(0.0);
    }
}
