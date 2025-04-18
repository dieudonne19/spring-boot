package org.dd_lgp.com.tutospring.dao.operations;


import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.dd_lgp.com.tutospring.dao.DataSource;
import org.dd_lgp.com.tutospring.dao.PostgresNextReference;
import org.dd_lgp.com.tutospring.dao.mapper.DishIngredientMapper;
import org.dd_lgp.com.tutospring.dao.mapper.IngredientMapper;
import org.dd_lgp.com.tutospring.dao.mapper.PriceMapper;
import org.dd_lgp.com.tutospring.dao.mapper.StockMovementMapper;
import org.dd_lgp.com.tutospring.model.DishIngredient;
import org.dd_lgp.com.tutospring.model.Ingredient;
import org.dd_lgp.com.tutospring.model.Price;
import org.dd_lgp.com.tutospring.model.StockMovement;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.time.Instant.now;

@Repository
@RequiredArgsConstructor
public class IngredientOperations implements CrudOperations<Ingredient> {
    private final DataSource dataSource;
    private final PriceOperations priceOperations;
    private final StockOperations stockOperations;
    private final PriceMapper priceMapper;
    private final IngredientMapper ingredientMapper;
    private final StockMovementMapper stockMovementMapper;
    private final DishIngredientMapper dishIngredientMapper;
    final PostgresNextReference postgresNextReference = new PostgresNextReference();

    @Override
    public List<Ingredient> getAll(int page, int size) {
        List<Ingredient> ingredients = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("select i.id, i.name from ingredient i limit ? offset ?")) {
            statement.setInt(1, size);
            statement.setInt(2, size * (page - 1));
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    ingredients.add(ingredientMapper.apply(resultSet));
                }
            }
            return ingredients;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Ingredient findById(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("select i.id, i.name, di.id as dish_ingredient_id, di.required_quantity, di.unit from ingredient i"
                     + " join dish_ingredient di on i.id = di.id_ingredient"
                     + " where i.id = ?")) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return ingredientMapper.apply(resultSet);
                }
                throw new RuntimeException("Ingredient.id=" + id + " not found");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @SneakyThrows
    @Override
    public List<Ingredient> saveAll(List<Ingredient> entities) {
        try (Connection connection = dataSource.getConnection()) {
            List<Ingredient> ingredients = new ArrayList<>();
            for (Ingredient entityToSave : entities) {
                try (PreparedStatement statement =
                             connection.prepareStatement("insert into ingredient (id, name) values (?, ?)"
                                     + " on conflict (id) do update set name=excluded.name"
                                     + " returning id, name")) {
                    long id = entityToSave.getId() == null ? postgresNextReference.nextID("ingredient", connection) : entityToSave.getId();
                    statement.setLong(1, id);
                    statement.setString(2, entityToSave.getName());
                    ResultSet resultSet = statement.executeQuery();

                    if (resultSet.next()) {
                        Ingredient savedIngredient = ingredientMapper.apply(resultSet);

                        List<Price> prices = priceOperations.saveAll(savedIngredient.getPrices());
                        List<StockMovement> stockMovements = stockOperations.saveAll(savedIngredient.getStockMovements());
                        savedIngredient.setPrices(prices);
                        savedIngredient.setStockMovements(stockMovements);
                        ingredients.add(savedIngredient);
                    }
                }

            }
            return ingredients;
        }
    }

    @SneakyThrows
    public Ingredient save(String name) {
        try (Connection connection = dataSource.getConnection()) {
            Ingredient ingredient = new Ingredient();
            try (PreparedStatement statement =
                         connection.prepareStatement("insert into ingredient (id, name) values (?, ?)"
                                 + " on conflict (id) do update set name=excluded.name"
                                 + " returning id, name")) {
                long id = postgresNextReference.nextID("ingredient", connection);
                statement.setLong(1, id);
                statement.setString(2, name);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    Ingredient savedIngredient = ingredientMapper.apply(resultSet);
                    ingredient.setId(savedIngredient.getId());
                    ingredient.setName(savedIngredient.getName());
                }
            }
            return ingredient;
        }
    }


    @SneakyThrows
    public List<Ingredient> updateAll(List<Ingredient> entities) {
        List<Ingredient> ingredients = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement =
                         connection.prepareStatement("update ingredient set name  = ?"
                                 + " where id = ?"
                                 + " returning id, name")) {
                for (Ingredient entityToSave : entities) {
                    try {
                        statement.setString(1, entityToSave.getName());
                        statement.setLong(2, entityToSave.getId());
                        statement.addBatch(); // group by batch so executed as one query in database
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        ingredients.add(ingredientMapper.apply(resultSet));
                    }
                }
                return ingredients;
            }
        }
    }

    @SneakyThrows
    public List<Price> createIngredientPrice(Long ingredientId, List<Price> prices) {
        List<Price> savedPrices = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement =
                         connection.prepareStatement("insert into price (id, amount, date_value, id_ingredient)"
                                 + " values (?, ?, ?, ?) on conflict (id) do nothing"
                                 + " returning id, amount, date_value, id_ingredient")) {

                for (Price entityToSave : prices) {
                    try {
                        long id = entityToSave.getId() == null ? postgresNextReference.nextID("price", connection) : entityToSave.getId();
                        statement.setLong(1, id);
                        statement.setDouble(2, entityToSave.getAmount());
                        statement.setDate(3, Date.valueOf(entityToSave.getDateValue()));
                        statement.setLong(4, ingredientId);
                        statement.addBatch(); // group by batch so executed as one query in database
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        savedPrices.add(priceMapper.apply(resultSet));
                    }
                }
                return savedPrices;
            }
        }
    }

    @SneakyThrows
    public List<StockMovement> createIngredientStockMovement(Long ingredientId, List<StockMovement> stockMovements) {
        List<StockMovement> savedStockMovements = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement =
                         connection.prepareStatement("insert into stock_movement (id, quantity, unit, movement_type, creation_datetime, id_ingredient)"
                                 + " values (?, ?, cast(? as unit), cast(? as stock_movement_type), ?, ?) on conflict (id) do nothing"
                                 + " returning id, quantity, unit, movement_type, creation_datetime, id_ingredient")) {

                for (StockMovement entityToSave : stockMovements) {
                    try {
                        long id = entityToSave.getId() == null ? postgresNextReference.nextID("stock_movement", connection) : entityToSave.getId();
                        statement.setLong(1, id);
                        statement.setDouble(2, entityToSave.getQuantity());
                        statement.setString(3, entityToSave.getUnit().name());
                        statement.setString(4, entityToSave.getMovementType().name());
                        statement.setTimestamp(5, Timestamp.from(now()));
                        statement.setLong(6, ingredientId);
                        statement.addBatch(); // group by batch so executed as one query in database
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        savedStockMovements.add(stockMovementMapper.apply(resultSet));
                    }
                }
                return savedStockMovements;
            }
        }
    }


    public List<DishIngredient> findByDishId(Long dishId) {
        List<DishIngredient> dishIngredients = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("select i.id, i.name, di.id as dish_ingredient_id, di.required_quantity, di.unit, di.id_ingredient from ingredient i"
                     + " join dish_ingredient di on i.id = di.id_ingredient"
                     + " where di.id_dish = ?")) {
            statement.setLong(1, dishId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    DishIngredient dishIngredient = dishIngredientMapper.apply(resultSet);
                    Ingredient ingredient = this.findById(resultSet.getLong("id_ingredient"));
                    dishIngredient.setIngredient(ingredient);

                    dishIngredients.add(dishIngredient);
                }
                return dishIngredients;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
