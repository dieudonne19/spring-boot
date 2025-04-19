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
        List<OrderStatus> orderStatuses = new ArrayList<>();
        if (page < 1) {
            throw new IllegalArgumentException("page must be higher than " + page);
        }
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("select os.id, os.order_id, os.order_status, os.datetime from order_status os"
                     + " limit ? offset ?")) {
            statement.setInt(1, page);
            statement.setInt(2, page * (size - 1));
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    OrderStatus orderStatus = orderStatusMapper.apply(resultSet);
                    orderStatuses.add(orderStatus);
                }
                return orderStatuses;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

    @SneakyThrows
    public List<OrderStatus> getOrderStatusByDishOrderId(String dishOrderId) {
        List<OrderStatus> orderStatuses = new ArrayList<OrderStatus>();
        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement statement = conn.prepareStatement("SELECT id, dish_order_id, order_status, datetime FROM dish_order_status WHERE dish_order_id=?");
        ) {
            statement.setString(1, dishOrderId);
            try (
                    ResultSet rs = statement.executeQuery();
            ) {

                while (rs.next()) {

                    orderStatuses.add(orderStatusMapper.apply(rs));
                }
            }

        }
        return orderStatuses;
    }
}
