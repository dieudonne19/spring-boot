package org.dd_lgp.com.tutospring.dao.mapper;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.dd_lgp.com.tutospring.dao.operations.DishCrudOperations;
import org.dd_lgp.com.tutospring.dao.operations.OrderCrudOperations;
import org.dd_lgp.com.tutospring.model.*;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class DishOrderMapper implements Function<ResultSet, DishOrder> {

    private final DishCrudOperations dishCrudOperations;

    @Override
    @SneakyThrows
    public DishOrder apply(ResultSet resultSet) {
        Dish dish = dishCrudOperations.findById(resultSet.getLong("id_dish"));
        DishOrder dishOrder = new DishOrder();
        List<DishOrderStatus> defaultDishOrderStatus = defaultDishOrderStatus();


        dishOrder.setId(resultSet.getLong("id"));
        dishOrder.setDish(dish);
        dishOrder.setDishQuantity(resultSet.getLong("dish_quantity"));
        dishOrder.setOrderDatetime(resultSet.getTimestamp("order_date_time").toLocalDateTime());
        dishOrder.setDishOrderStatuses(defaultDishOrderStatus);

        return dishOrder;
    }



    private List<DishOrderStatus> defaultDishOrderStatus() {
        List<DishOrderStatus> dishOrderStatuses = new ArrayList<>();

        DishOrderStatus dishOrderStatus = new DishOrderStatus();
        dishOrderStatus.setOrderProcessStatus(OrderProcessStatus.CREATED);
        dishOrderStatus.setDatetime(LocalDateTime.now());

        dishOrderStatuses.add(dishOrderStatus);

        return dishOrderStatuses;
    }
}
