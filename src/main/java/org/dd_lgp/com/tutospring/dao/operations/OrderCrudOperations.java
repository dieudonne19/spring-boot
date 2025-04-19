package org.dd_lgp.com.tutospring.dao.operations;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.dd_lgp.com.tutospring.dao.DataSource;
import org.dd_lgp.com.tutospring.dao.PostgresNextReference;
import org.dd_lgp.com.tutospring.dao.mapper.OrderMapper;
import org.dd_lgp.com.tutospring.model.*;
import org.dd_lgp.com.tutospring.service.exception.ServerException;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Repository
@RequiredArgsConstructor
public class OrderCrudOperations implements CrudOperations<Order> {
    private final DataSource dataSource;
    private final OrderMapper orderMapper;
    private final OrderStatusOperations orderStatusOperations;
    private final PostgresNextReference postgresNextReference = new PostgresNextReference();
    private final DishOrderCrudOperations dishOrderCrudOperations;

    @Override
    public List<Order> getAll(int page, int size) {
        if (page < 1) {
            throw new IllegalArgumentException("page must be higher than " + page);
        }
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("select o.id, o.reference, o.destination, o.creation_date_time from order o "
                     + "join dish_order do on do.id_order = o.id limit ? offset ?")) {
            statement.setInt(1, page);
            statement.setInt(2, page * (size - 1));
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return null;
                }
            }
            throw new RuntimeException("Not finished");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Order findById(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("select o.id, o.reference, o.destination, o.creation_date_time from order o where id = ?")) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return orderMapper.apply(resultSet);
                }
            }
            throw new RuntimeException("Order.id=" + id + " not found");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    @Override
    public List<Order> saveAll(List<Order> entitiesToSave) {
        List<Order> orders = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("insert into \"order\" (id, reference, destination, creation_date_time) values (?, ?, ?, ?)"
                     + " on conflict (id) do nothing"
                     + " returning id, reference, destination, creation_date_time")) {
            entitiesToSave.forEach(entity -> {
                try {
                    long id = entity.getId() == null ? postgresNextReference.nextID("\"order\"", connection) : entity.getId();
                    statement.setLong(1, id);
                    statement.setString(2, entity.getReference());
                    statement.setString(3, entity.getDestination());
                    statement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
                    statement.addBatch(); // group by batch so executed as one query in database
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    /*
                    entitiesToSave.forEach(entity -> {
                        dishOrderCrudOperations.saveAll(entity.getDishOrders());
                    });
                     */
                    Order savedOrder = orderMapper.apply(resultSet);
                    orderStatusOperations.saveAll(savedOrder.getId(), orderStatuses());
                    orders.add(savedOrder);
                }
            }
            return orders;
        }
    }

    public Order save(Order entityToSave) {
        List<Order> Orders = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("insert into \"order\" (id, reference, destination, creation_date_time) "
                     + "values (?, ?, ?, ?)"
                     + "on conflict (id) do update set reference=excluded.reference "
                     + "returning id, reference, destination, creation_date_time")) {
            try {
                statement.setLong(1, entityToSave.getId());
                statement.setString(2, entityToSave.getReference());
                statement.setString(3, entityToSave.getDestination());
                statement.setTimestamp(4, Timestamp.valueOf(entityToSave.getCreationDatetime()));
                statement.addBatch(); // group by batch so executed as one query in database

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try (ResultSet resultSet = statement.executeQuery()) {

                List<DishOrder> dishOrders = dishOrderCrudOperations.saveAll(entityToSave.getDishOrders());
                if (resultSet.next()) {
                    Order orderCreated = orderMapper.apply(resultSet);
                    orderCreated.setDishOrders(dishOrders);
                    orderCreated.setOrderStatuses(orderStatuses());
                    Orders.add(orderCreated);
                }
            }
            return Orders.getFirst();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    public Order getOrderByReference(String reference) {
        Order order = null;
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("select o.id, o.reference, o.destination, o.creation_date_time from \"order\" o where reference = ?")
        ) {
            statement.setString(1, reference);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    order = orderMapper.apply(rs);
                    List<OrderStatus> orderStatuses = orderStatusOperations.findAllByOrderId(order.getId());
                    List<DishOrder> dishOrders = dishOrderCrudOperations.findAllByOrderId(order.getId());
                    order.setOrderStatuses(orderStatuses);
                    order.setDishOrders(dishOrders);
                }
            } catch (SQLException e) {
                throw new ServerException(e);
            }
        }
        return order;
    }

    @SneakyThrows
    public Order createOrderByReference(String reference) {
        Order order = null;
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("insert into \"order\" (id, reference, destination, creation_date_time) values (?, ?, ?, ?)"
                        + " on conflict (id) do nothing"
                        + " returning id, reference, destination, creation_date_time")
        ) {
            Order existingOrder = this.getOrderByReference(reference);
            if (existingOrder != null) {
                throw new ServerException("Order with reference " + reference + " already exists");
            }

            long id = postgresNextReference.nextID("\"order\"", connection);
            statement.setLong(1, id);
            statement.setString(2, reference);
            statement.setString(3, "...");
            statement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    order = orderMapper.apply(rs);
                    List<OrderStatus> orderStatuses = orderStatusOperations.findAllByOrderId(order.getId());
                    List<DishOrder> dishOrders = dishOrderCrudOperations.findAllByOrderId(order.getId());
                    order.setOrderStatuses(orderStatuses);
                    order.setDishOrders(dishOrders);
                }
            } catch (SQLException e) {
                throw new ServerException(e);
            }
        }
        return order;
    }

    @SneakyThrows
    public Order updateDishesStatus(String reference, Long dishId, UpdateDishOrderStatus entity) {
        List<Order> orders = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("insert into dish_order_status (id, dish_order_id, order_status, datetime) values (?, ?, cast(? as process_status), ?)"
                     + " on conflict (id) do update set datetime=excluded.datetime"
                     + " returning id, dish_order_id, order_status, datetime")) {
            try {
                long id = postgresNextReference.nextID("dish_order_status", connection);
                Order order = this.getOrderByReference(reference);
                List<DishOrder> dishOrdersMatchedWithDishIDAndReference = order.getDishOrders().stream().filter(dishOrder -> {
                    return dishOrder.getDish().getId().equals(dishId) && order.getReference().equals(reference);
                }).toList();
                statement.setLong(1, id);
                statement.setLong(2, dishOrdersMatchedWithDishIDAndReference.getFirst().getId());
                statement.setString(3, entity.getStatus().name());
                statement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
                orders.add(order);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
            }
            return orders.getFirst();
        }
    }


    private List<OrderStatus> orderStatuses() {
        List<OrderStatus> orderStatuses = new ArrayList<>();

        OrderStatus actualStatus = new OrderStatus();
        actualStatus.setOrderProcessStatus(OrderProcessStatus.CREATED);
        actualStatus.setDatetime(LocalDateTime.now());

        orderStatuses.add(actualStatus);

        return orderStatuses;
    }

}
