package org.dd_lgp.com.tutospring.dao.operations;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.dd_lgp.com.tutospring.dao.DataSource;
import org.dd_lgp.com.tutospring.dao.PostgresNextReference;
import org.dd_lgp.com.tutospring.dao.mapper.DishOrderMapper;
import org.dd_lgp.com.tutospring.model.*;
import org.dd_lgp.com.tutospring.service.exception.ServerException;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DishOrderCrudOperations implements CrudOperations<DishOrder> {
    private final DataSource dataSource;
    private final DishOrderMapper dishOrderMapper;
    private final PostgresNextReference postgresNextReference = new PostgresNextReference();
    private final DishOrderStatusOperations dishOrderStatusOperations;
    private final OrderStatusOperations orderStatusOperations;

    @Override
    public List<DishOrder> getAll(int page, int size) {
        List<DishOrder> dishOrders = new ArrayList<>();
        if (page < 1) {
            throw new IllegalArgumentException("page must be higher than " + page);
        }
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("select d_o.id, d_o.id_dish, d_o.id_order, d_o.dish_quantity from dish_order d_o"
                     + " limit ? offset ?")) {
            statement.setInt(1, page);
            statement.setInt(2, page * (size - 1));
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    DishOrder dishOrder = dishOrderMapper.apply(resultSet);
                    List<DishOrderStatus> dishOrderStatuses = dishOrderStatusOperations.findAllByDishOrderId(dishOrder.getId());
                    dishOrder.setDishOrderStatuses(dishOrderStatuses);

                    dishOrders.add(dishOrder);
                }
                return dishOrders;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DishOrder findById(Long id) {
        return null;
    }

    public List<DishOrder> findAllByOrderId(Long id) {
        List<DishOrder> dishOrders = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("select d_o.id, d_o.id_order, d_o.id_dish, d_o.dish_quantity, d_o.order_date_time from dish_order d_o "
                     + "where id_order = ?")) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    DishOrder dishOrder = dishOrderMapper.apply(resultSet);
                    List<DishOrderStatus> dishOrderStatuses = dishOrderStatusOperations.findAllByDishOrderId(dishOrder.getId());
                    dishOrder.setDishOrderStatuses(dishOrderStatuses);

                    dishOrders.add(dishOrder);
                }
                return dishOrders;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // SELECT d_o.id_dish as dish_identifier, d.name, sum(d_o.dish_quantity) as quantity_sold FROM dish_order d_o join public.dish_order_status dos on d_o.id = dos.dish_order_id join public.dish d on d_o.id_dish = d.id where dos.order_status = 'DELIVERED' group by id_dish, d.name order by quantity_sold desc;

    @SneakyThrows
    public List<DishOrder> modifyAll(List<DishOrder> dishOrderToSave) {
        List<DishOrder> dishOrders = new ArrayList<>();
        try (
                Connection connection = dataSource.getConnection()
        ) {
            dishOrderToSave.forEach(dishOrder -> {
                try (
                        PreparedStatement statement = connection.prepareStatement("INSERT INTO dish_order(id, order_id, dish_id, quantity) VALUES (?,?,?,?) ON CONFLICT (id) DO UPDATE SET quantity=excluded.quantity RETURNING id,quantity,order_id,dish_id")
                ) {
                    long id = dishOrder.getId() == null ? postgresNextReference.nextID("dish_order", connection) : dishOrder.getId();

                    statement.setLong(1, id);
                    statement.setLong(2, dishOrder.getOrder().getId());
                    statement.setLong(3, dishOrder.getDish().getId());
                    statement.setLong(4, dishOrder.getDishQuantity());
                    OrderStatus created = new OrderStatus();
                    created.setOrderProcessStatus(OrderProcessStatus.CREATED);
                    created.setDatetime(LocalDateTime.now());
                    try (ResultSet rs = statement.executeQuery()) {
                        if (rs.next()) {
                            dishOrders.add(dishOrderMapper.apply(rs));
                        }
                        orderStatusOperations.saveAll(dishOrder.getOrder().getId(), List.of(created));
                    } catch (SQLException e) {
                        throw new ServerException(e);
                    }

                } catch (SQLException e) {
                    throw new ServerException(e);
                }
            });
        }
        return dishOrders;
    }

    @Override
    @SneakyThrows
    public List<DishOrder> saveAll(List<DishOrder> entities) {
        List<DishOrder> dishOrders = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement =
                         connection.prepareStatement("insert into dish_order (id, id_dish, id_order, dish_quantity, order_date_time) values (?, ?, ?, ?, ?)"
                                 + " on conflict (id) do update set dish_quantity=excluded.dish_quantity"
                                 + " returning id, id_dish, id_order, dish_quantity, order_date_time")) {
                entities.forEach(entityToSave -> {
                    try {
                        Long id = entityToSave.getId() == null ? postgresNextReference.nextID("dish_order", connection) : entityToSave.getId();
                        statement.setLong(1, id);
                        statement.setLong(2, entityToSave.getDish().getId());
                        statement.setLong(3, entityToSave.getOrder().getId());
                        statement.setLong(4, entityToSave.getDishQuantity());
                        statement.setTimestamp(5, Timestamp.valueOf(entityToSave.getOrderDatetime()));
                        statement.addBatch(); // group by batch so executed as one query in database

                        try (ResultSet resultSet = statement.executeQuery()) {
                            while (resultSet.next()) {
                                DishOrder dishOrder = dishOrderMapper.apply(resultSet);
                                dishOrders.add(dishOrder);
                            }
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
                return dishOrders;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    public List<DishOrder> saveAll(Long idDish, Long idOrder, List<DishOrder> entities) {
        List<DishOrder> dishOrders = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement =
                         connection.prepareStatement("insert into dish_order (id, id_dish, id_order, dish_quantity, order_date_time) values (?, ?, ?, ?, ?)"
                                 + " on conflict (id) do update set dish_quantity=excluded.dish_quantity"
                                 + " returning id, id_dish, id_order, dish_quantity, order_date_time")) {
                entities.forEach(entityToSave -> {
                    try {
                        Long id = entityToSave.getId() == null ? postgresNextReference.nextID("dish_order", connection) : entityToSave.getId();
                        statement.setLong(1, id);
                        statement.setLong(2, idDish);
                        statement.setLong(3, idOrder);
                        statement.setLong(4, entityToSave.getDishQuantity());
                        statement.setTimestamp(5, Timestamp.valueOf(entityToSave.getOrderDatetime()));
                        statement.addBatch(); // group by batch so executed as one query in database

                        try (ResultSet resultSet = statement.executeQuery()) {
                            while (resultSet.next()) {
                                dishOrders.add(dishOrderMapper.apply(resultSet));
                            }
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
                return dishOrders;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
