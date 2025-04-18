package org.dd_lgp.com.tutospring.dao.operations;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.dd_lgp.com.tutospring.dao.DataSource;
import org.dd_lgp.com.tutospring.dao.PostgresNextReference;
import org.dd_lgp.com.tutospring.dao.mapper.OrderStatusMapper;
import org.dd_lgp.com.tutospring.model.*;
import org.dd_lgp.com.tutospring.service.exception.ServerException;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderStatusOperations implements CrudOperations<OrderStatus> {
    private final DataSource dataSource;
    private final OrderStatusMapper orderStatusMapper;
    private final PostgresNextReference postgresNextReference = new PostgresNextReference();

    @Override
    public List<OrderStatus> getAll(int page, int size) {
        return List.of();
    }

    @Override
    public OrderStatus findById(Long id) {
        return null;
    }

    @SneakyThrows
    public List<OrderStatus> findAllByOrderId(Long orderId) {
        List<OrderStatus> orderStatuses = new ArrayList<>();
        try(
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("select os.id, os.order_id, os.order_status, os.datetime from order_status os where os.order_id = ? order by os.datetime asc")
        )
        {
            statement.setLong(1, orderId);
            try (ResultSet rs = statement.executeQuery()){
                while (rs.next()) {
                    OrderStatus orderStatus = orderStatusMapper.apply(rs);
                    orderStatuses.add(orderStatus);
                }
            }catch (SQLException e){
                throw new ServerException(e);
            }
        }
        return orderStatuses;
    }

    @Override
    public List<OrderStatus> saveAll(List entities) {
        return List.of();
    }

    @SneakyThrows
    public List<OrderStatus> saveAll(Long orderId, List<OrderStatus> entities) {
        try (Connection connection = dataSource.getConnection()) {
            List<OrderStatus> orderStatuses = new ArrayList<>();
            for (OrderStatus entityToSave : entities) {
                System.out.println(Timestamp.valueOf(entityToSave.getDatetime()) + " order status date time");
                try (PreparedStatement statement =
                             connection.prepareStatement("insert into order_status (id, order_id, order_status, datetime) values (?, ?, cast(? as process_status), ?)"
                                     + " on conflict (id) do nothing"
                                     + " returning id, order_id, order_status, datetime")) {
                    Long id = entityToSave.getId() == null ? postgresNextReference.nextID("order_status", connection) : entityToSave.getId();
                    statement.setLong(1, id);
                    statement.setLong(2, orderId);
                    statement.setString(3, entityToSave.getOrderProcessStatus().name());
                    statement.setTimestamp(4, Timestamp.valueOf(entityToSave.getDatetime()));
                    ResultSet resultSet = statement.executeQuery();

                    if (resultSet.next()) {
                        OrderStatus savedOrderStatus = orderStatusMapper.apply(resultSet);
                        orderStatuses.add(savedOrderStatus);
                    }
                }

            }
            return orderStatuses;
        }
    }
}
