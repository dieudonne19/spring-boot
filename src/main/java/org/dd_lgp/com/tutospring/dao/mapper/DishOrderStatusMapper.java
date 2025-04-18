package org.dd_lgp.com.tutospring.dao.mapper;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.dd_lgp.com.tutospring.model.DishOrderStatus;
import org.dd_lgp.com.tutospring.model.OrderProcessStatus;
import org.dd_lgp.com.tutospring.model.OrderStatus;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class DishOrderStatusMapper implements Function<ResultSet, DishOrderStatus> {
    @Override
    @SneakyThrows
    public DishOrderStatus apply(ResultSet resultSet) {
        DishOrderStatus dishOrderStatus = new DishOrderStatus();
        dishOrderStatus.setId(resultSet.getLong("id"));
        dishOrderStatus.setOrderProcessStatus(OrderProcessStatus.valueOf(resultSet.getString("order_status")));
        dishOrderStatus.setDatetime(resultSet.getTimestamp("datetime").toLocalDateTime());

        return dishOrderStatus;
    }
}
