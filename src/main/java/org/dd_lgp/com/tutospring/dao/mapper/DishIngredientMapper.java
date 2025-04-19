package org.dd_lgp.com.tutospring.dao.mapper;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.dd_lgp.com.tutospring.model.DishIngredient;
import org.dd_lgp.com.tutospring.model.Unit;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.function.Function;


@Component
@RequiredArgsConstructor
public class DishIngredientMapper implements Function<ResultSet, DishIngredient> {

    @Override
    @SneakyThrows
    public DishIngredient apply(ResultSet resultSet) {

        DishIngredient dishIngredient = new DishIngredient();
        dishIngredient
                .setId(resultSet.getLong("id"))
                .setRequiredQuantity(resultSet.getDouble("required_quantity"));
        if (resultSet.getString("unit") != null) {
            dishIngredient.setUnit(Unit.valueOf(resultSet.getString("unit")));
        }

        return dishIngredient;
    }


}
