package org.dd_lgp.com.tutospring.endpoint.mapper;


import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.dd_lgp.com.tutospring.endpoint.rest.DishIngredientRest;
import org.dd_lgp.com.tutospring.model.DishIngredient;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class DishIngredientRestMapper implements Function<DishIngredient, DishIngredientRest> {
    private final IngredientRestMapper ingredientRestMapper;

    @Override
    public DishIngredientRest apply(DishIngredient dishIngredient) {
        return null;
    }

    @SneakyThrows
    public DishIngredientRest toRest(DishIngredient dishIngredient) {
        DishIngredientRest dishIngredientRest = new DishIngredientRest();
        dishIngredientRest.setId(dishIngredient.getId());
        dishIngredientRest.setUnit(dishIngredient.getUnit());
        dishIngredientRest.setRequiredQuantity(dishIngredient.getRequiredQuantity());
        return dishIngredientRest;
    }
}
