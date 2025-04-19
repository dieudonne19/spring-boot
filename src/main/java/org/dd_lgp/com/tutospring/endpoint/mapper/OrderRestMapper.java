package org.dd_lgp.com.tutospring.endpoint.mapper;

import lombok.RequiredArgsConstructor;
import org.dd_lgp.com.tutospring.endpoint.rest.OrderRest;
import org.dd_lgp.com.tutospring.endpoint.rest.PriceRest;
import org.dd_lgp.com.tutospring.model.Order;
import org.dd_lgp.com.tutospring.model.Price;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class OrderRestMapper implements Function<Order, OrderRest> {
    private final DishOrderRestMapper dishOrderRestMapper;

    @Override
    public OrderRest apply(Order order) {
        OrderRest orderRest = new OrderRest();

        orderRest.setId(order.getId());
        orderRest.setReference(order.getReference());
        orderRest.setDestination(order.getDestination());
        orderRest.setCreationDatetime(order.getCreationDatetime());

        if (order.getOrderStatuses() != null) {
            orderRest.setOrderStatuses(order.getOrderStatuses());
        }

        if (order.getDishOrders() != null) {
            orderRest.setDishOrders(order.getDishOrders().stream().map(dishOrderRestMapper::toRest).toList());
        }

        return orderRest;
    }
}
