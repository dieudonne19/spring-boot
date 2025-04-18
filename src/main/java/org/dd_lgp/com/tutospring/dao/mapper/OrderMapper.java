package org.dd_lgp.com.tutospring.dao.mapper;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.dd_lgp.com.tutospring.dao.operations.DishOrderCrudOperations;
import org.dd_lgp.com.tutospring.dao.operations.OrderStatusOperations;
import org.dd_lgp.com.tutospring.model.Order;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class OrderMapper implements Function<ResultSet, Order> {
    @Override
    @SneakyThrows
    public Order apply(ResultSet resultSet) {
        Order order = new Order();

        order.setId(resultSet.getLong("id"));
        order.setReference(resultSet.getString("reference"));
        order.setDestination(resultSet.getString("destination"));
        order.setCreationDatetime(resultSet.getTimestamp("creation_date_time").toLocalDateTime());
        return order;
    }
}
