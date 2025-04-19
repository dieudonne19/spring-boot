package org.dd_lgp.com.tutospring.dao.operations;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.dd_lgp.com.tutospring.dao.DataSource;
import org.dd_lgp.com.tutospring.dao.PostgresNextReference;
import org.dd_lgp.com.tutospring.dao.mapper.DishIngredientMapper;
import org.dd_lgp.com.tutospring.dao.mapper.DishMapper;
import org.dd_lgp.com.tutospring.dao.mapper.DishSoldMapper;
import org.dd_lgp.com.tutospring.model.*;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Repository
@RequiredArgsConstructor
public class DishCrudOperations implements CrudOperations<Dish> {
    private final DataSource dataSource;
    private final DishMapper dishMapper;
    private final DishIngredientMapper dishIngredientMapper;
    private final DishSoldMapper dishSoldMapper;
    private final IngredientOperations ingredientOperations;
    private final PostgresNextReference postgresNextReference = new PostgresNextReference();

    @Override
    public List<Dish> getAll(int page, int size) {
        List<Dish> dishes = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("select d.id, d.name, d.price from dish d limit ? offset ?")) {
            statement.setInt(1, size);
            statement.setInt(2, size * (page - 1));
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    dishes.add(dishMapper.apply(resultSet));
                }
            }
            return dishes;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<DishSold> getSales() {
        List<DishSold> dishSolds = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT sum(d.price * d_o.dish_quantity) as total_amount, d_o.id_dish as dish_identifier, " +
                             "d.name, sum(d_o.dish_quantity) as quantity_sold FROM dish_order d_o " +
                             "join public.dish_order_status dos on d_o.id = dos.dish_order_id " +
                             "join public.dish d on d_o.id_dish = d.id where dos.order_status = 'DELIVERED' " +
                             "group by id_dish, d.name, d.price order by quantity_sold desc, d.price desc")) {
            // statement.setLong(1, top);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    DishSold dishSold = dishSoldMapper.apply(resultSet);
                    Dish dish = this.findById(resultSet.getLong("dish_identifier"));
                    dishSold.setDish(dish);
                    dishSolds.add(dishSold);
                }
            }
            return dishSolds;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Dish findById(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("select d.id, d.name, d.price from dish d where id = ?")) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return dishMapper.apply(resultSet);
                }
            }
            throw new RuntimeException("Dish.id=" + id + " not found");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    @Override
    public List<Dish> saveAll(List<Dish> entities) {
        List<Dish> dishes = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement =
                         connection.prepareStatement("insert into dish (id, name, price) values (?, ?, ?)"
                                 + " on conflict (id) do update set name=excluded.name, price=excluded.price"
                                 + " returning id, name, price")) {
                entities.forEach(entityToSave -> {
                    try {
                        statement.setLong(1, entityToSave.getId());
                        statement.setString(2, entityToSave.getName());
                        statement.setDouble(3, entityToSave.getPrice());
                        statement.addBatch(); // group by batch so executed as one query in database
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        dishes.add(dishMapper.apply(resultSet));
                    }
                }
                return dishes;
            }
        }
    }

    @SneakyThrows
    public Dish insertDishIngredient(Long idDish, List<CreateDishIngredient> entities) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement =
                         connection.prepareStatement("insert into dish_ingredient (id, id_dish, id_ingredient, required_quantity) values (?, ?, ?, ?)"
                                 + " on conflict (id) do nothing"
                                 + " returning id, id_dish, id_ingredient, required_quantity, unit")) {
                entities.forEach(entityToSave -> {
                    try {
                        long id = entityToSave.getId() == null ? postgresNextReference.nextID("dish_ingredient", connection) : entityToSave.getId();
                        Ingredient createdIngredient = ingredientOperations.save(entityToSave.getName());
                        statement.setLong(1, id);
                        statement.setLong(2, idDish);
                        statement.setLong(3, createdIngredient.getId());
                        statement.setDouble(4, entityToSave.getRequiredQuantity());
                        statement.addBatch(); // group by batch so executed as one query in database
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
                try (ResultSet resultSet = statement.executeQuery()) {
                /*
                    while (resultSet.next()) {
                        DishIngredient dishIngredient = dishIngredientMapper.apply(resultSet);
                        Ingredient ingredient = ingredientOperations.findById(resultSet.getLong("id_ingredient"));
                        dishIngredient.setIngredient(ingredient);
                        dishIngredients.add(dishIngredient);

                    }
                 */
                    return this.findById(idDish);
                }
                // return dishIngredients;
            }
        }
    }
}
