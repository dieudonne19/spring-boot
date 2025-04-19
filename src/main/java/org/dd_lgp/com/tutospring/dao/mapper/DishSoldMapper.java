package org.dd_lgp.com.tutospring.dao.mapper;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.dd_lgp.com.tutospring.dao.operations.DishCrudOperations;
import org.dd_lgp.com.tutospring.model.Dish;
import org.dd_lgp.com.tutospring.model.DishSold;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.function.Function;


@Component
@RequiredArgsConstructor
public class DishSoldMapper implements Function<ResultSet, DishSold> {
    @Override
    @SneakyThrows
    public DishSold apply(ResultSet resultSet) {
        DishSold dishSold = new DishSold();

        dishSold.setQuantitySold(resultSet.getDouble("quantity_sold"));
        dishSold.setTotalAmount(resultSet.getDouble("total_amount"));
        return dishSold;
    }
}
