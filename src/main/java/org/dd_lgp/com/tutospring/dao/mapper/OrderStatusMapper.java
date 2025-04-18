package org.dd_lgp.com.tutospring.dao.mapper;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.dd_lgp.com.tutospring.model.Order;
import org.dd_lgp.com.tutospring.model.OrderProcessStatus;
import org.dd_lgp.com.tutospring.model.OrderStatus;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class OrderStatusMapper implements Function<ResultSet, OrderStatus> {

    @Override
    @SneakyThrows
    public OrderStatus apply(ResultSet resultSet) {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setId(resultSet.getLong("id"));
        orderStatus.setOrderProcessStatus(OrderProcessStatus.valueOf(resultSet.getString("order_status")));
        orderStatus.setDatetime(resultSet.getTimestamp("datetime").toLocalDateTime());

        return orderStatus;
    }
}
