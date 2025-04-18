package org.dd_lgp.com.tutospring.dao.mapper;


import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.dd_lgp.com.tutospring.dao.PostgresNextReference;
import org.dd_lgp.com.tutospring.dao.operations.IngredientOperations;
import org.dd_lgp.com.tutospring.model.Dish;
import org.dd_lgp.com.tutospring.model.DishIngredient;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.List;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class DishMapper implements Function<ResultSet, Dish> {
    private final IngredientOperations ingredientOperations;

    @Override
    @SneakyThrows
    public Dish apply(ResultSet resultSet) {
        Long idDish = resultSet.getLong("id");

        Dish dish = new Dish();
        dish.setId(idDish);
        dish.setName(resultSet.getString("name"));
        dish.setPrice(resultSet.getDouble("price"));
        List<DishIngredient> dishIngredients = ingredientOperations.findByDishId(idDish);
        dish.setDishIngredients(dishIngredients);

        return dish;
    }
}
