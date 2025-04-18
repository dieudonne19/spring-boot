package org.dd_lgp.com.tutospring.endpoint.mapper;

import org.dd_lgp.com.tutospring.endpoint.rest.IngredientRest;
import org.dd_lgp.com.tutospring.model.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class IngredientRestMapper implements Function<Ingredient, IngredientRest> {
    @Autowired
    private PriceRestMapper priceRestMapper;
    @Autowired
    private StockMovementRestMapper stockMovementRestMapper;

    @Override
    public IngredientRest apply(Ingredient ingredient) {
        return null;
    }

    public IngredientRest toRest(Ingredient ingredient) {
        IngredientRest ingredientRest = new IngredientRest();
        ingredientRest.setId(ingredient.getId());
        ingredientRest.setName(ingredient.getName());
        ingredientRest.setPriceRests(ingredient.getPrices().stream().map(price -> priceRestMapper.apply(price)).toList());
        ingredientRest.setStockMovementRests(ingredient.getStockMovements().stream().map(stockMovement -> stockMovementRestMapper.toRest(stockMovement)).toList());
        return ingredientRest;
    }
}
