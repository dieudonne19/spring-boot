package org.dd_lgp.com.tutospring.endpoint.mapper;


import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.dd_lgp.com.tutospring.dao.mapper.DishIngredientMapper;
import org.dd_lgp.com.tutospring.endpoint.rest.DishRest;
import org.dd_lgp.com.tutospring.endpoint.rest.IngredientRest;
import org.dd_lgp.com.tutospring.model.Dish;
import org.dd_lgp.com.tutospring.model.Ingredient;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class DishRestMapper implements Function<Ingredient, IngredientRest> {
    private final DishIngredientRestMapper dishIngredientRestMapper;

    @Override
    public IngredientRest apply(Ingredient ingredient) {
        return null;
    }

    @SneakyThrows
    public DishRest toRest(Dish dish) {
        DishRest dishRest = new DishRest();
        dishRest.setId(dish.getId());
        dishRest.setName(dish.getName());
        dishRest.setDishIngredientRests(dish.getDishIngredients().stream().map(dishIngredientRestMapper::toRest).toList());
        dishRest.setPrice(dish.getPrice());

        return dishRest;
    }
}
