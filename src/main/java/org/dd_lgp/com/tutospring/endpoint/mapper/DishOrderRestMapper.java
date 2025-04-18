package org.dd_lgp.com.tutospring.endpoint.mapper;

import lombok.RequiredArgsConstructor;
import org.dd_lgp.com.tutospring.endpoint.rest.DishOrderRest;
import org.dd_lgp.com.tutospring.model.DishOrder;
import org.dd_lgp.com.tutospring.model.Order;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class DishOrderRestMapper implements Function<DishOrder, DishOrderRest> {
    private final DishRestMapper dishRestMapper;

    public DishOrderRest toRest(DishOrder dishOrder) {
        DishOrderRest dishOrderRest = new DishOrderRest();
        dishOrderRest.setId(dishOrder.getId());
        dishOrderRest.setDishQuantity(dishOrder.getDishQuantity());
        dishOrderRest.setDishRest(dishRestMapper.toRest(dishOrder.getDish()));
        dishOrderRest.setDishOrderStatuses(dishOrder.getDishOrderStatuses());
        dishOrderRest.setOrderDatetime(dishOrder.getOrderDatetime());

        return dishOrderRest;
    }

    @Override
    public DishOrderRest apply(DishOrder dishOrder) {
        return null;
    }
}
