package org.dd_lgp.com.tutospring.dao.operations;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.dd_lgp.com.tutospring.dao.DataSource;
import org.dd_lgp.com.tutospring.dao.mapper.DishOrderStatusMapper;
import org.dd_lgp.com.tutospring.dao.mapper.OrderStatusMapper;
import org.dd_lgp.com.tutospring.model.DishOrder;
import org.dd_lgp.com.tutospring.model.DishOrderStatus;
import org.dd_lgp.com.tutospring.model.Order;
import org.dd_lgp.com.tutospring.model.OrderStatus;
import org.dd_lgp.com.tutospring.service.exception.ServerException;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DishOrderStatusOperations implements CrudOperations<DishOrderStatus> {
    private final DataSource dataSource;
    private final DishOrderStatusMapper dishOrderStatusMapper;

    @Override
    public List<DishOrderStatus> getAll(int page, int size) {
        List<DishOrderStatus> dishOrderStatuses = new ArrayList<>();
        if (page < 1) {
            throw new IllegalArgumentException("page must be higher than " + page);
        }
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("select dos.id, dos.dish_order_id, dos.order_status, dos.datetime from dish_order_status dos"
                     + " limit ? offset ?")) {
            statement.setInt(1, page);
            statement.setInt(2, page * (size - 1));
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    DishOrderStatus dishOrderStatus = dishOrderStatusMapper.apply(resultSet);
                    dishOrderStatuses.add(dishOrderStatus);
                }
                return dishOrderStatuses;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DishOrderStatus findById(Long id) {
        return null;
    }

    @SneakyThrows
    public List<DishOrderStatus> findAllByDishOrderId(Long dishOrderId) {
        List<DishOrderStatus> dishOrderStatuses = new ArrayList<>();
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("select dos.id, dos.dish_order_id, dos.order_status, dos.datetime from dish_order_status dos where dos.dish_order_id = ?")
        ) {
            statement.setLong(1, dishOrderId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    DishOrderStatus orderStatus = dishOrderStatusMapper.apply(rs);
                    dishOrderStatuses.add(orderStatus);
                }
            } catch (SQLException e) {
                throw new ServerException(e);
            }
        }
        return dishOrderStatuses;
    }

    @Override
    public List<DishOrderStatus> saveAll(List entities) {
        return List.of();
    }
}
