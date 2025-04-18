package org.dd_lgp.com.tutospring.dao.mapper;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.dd_lgp.com.tutospring.dao.operations.PriceOperations;
import org.dd_lgp.com.tutospring.dao.operations.StockOperations;
import org.dd_lgp.com.tutospring.model.Ingredient;
import org.dd_lgp.com.tutospring.model.Price;
import org.dd_lgp.com.tutospring.model.StockMovement;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class IngredientMapper implements Function<ResultSet, Ingredient> {
    private final PriceOperations priceOperations;
    private final StockOperations stockOperations;

    @Override
    @SneakyThrows
    public Ingredient apply(ResultSet resultSet) {
        Long idIngredient = resultSet.getLong("id");

        List<Price> ingredientPrices = priceOperations.findByIdIngredient(idIngredient);
        List<StockMovement> ingredientStockMovements = stockOperations.findByIdIngredient(idIngredient);

        Ingredient ingredient = new Ingredient();
        ingredient.setId(idIngredient);
        ingredient.setName(resultSet.getString("name"));
        ingredient.setPrices(ingredientPrices);
        ingredient.setStockMovements(ingredientStockMovements);

        return ingredient;
    }
}
