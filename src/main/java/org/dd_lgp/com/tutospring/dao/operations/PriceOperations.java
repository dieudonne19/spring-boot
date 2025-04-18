package org.dd_lgp.com.tutospring.dao.operations;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.dd_lgp.com.tutospring.dao.DataSource;
import org.dd_lgp.com.tutospring.dao.PostgresNextReference;
import org.dd_lgp.com.tutospring.dao.mapper.PriceMapper;
import org.dd_lgp.com.tutospring.model.Ingredient;
import org.dd_lgp.com.tutospring.model.Price;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PriceOperations implements CrudOperations<Price> {
    private final DataSource dataSource;
    private final PriceMapper priceMapper;
    final PostgresNextReference postgresNextReference = new PostgresNextReference();
    /*
    public PriceRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

     */

    @Override
    public List<Price> getAll(int page, int size) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Price findById(Long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @SneakyThrows
    @Override
    public List<Price> saveAll(List<Price> entities) {
        List<Price> prices = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement("insert into price (id, amount, date_value, id_ingredient) values (?, ?, ?, ?)"
                             + " on conflict (id) do nothing"
                             + " returning id, amount, date_value, id_ingredient");) {
            entities.forEach(entityToSave -> {
                try {
                    Long id = entityToSave.getId() == null ? postgresNextReference.nextID("price", connection) : entityToSave.getId();
                    statement.setLong(1, id);
                    statement.setDouble(2, entityToSave.getAmount());
                    statement.setDate(3, Date.valueOf(entityToSave.getDateValue()));
                    statement.setLong(4, entityToSave.getIngredient().getId());
                    statement.addBatch(); // group by batch so executed as one query in database
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    prices.add(priceMapper.apply(resultSet));
                }
            }
            return prices;
        }
    }

    public List<Price> findByIdIngredient(Long idIngredient) {
        List<Price> prices = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("select p.id, p.amount, p.date_value from price p"
                     + " join ingredient i on p.id_ingredient = i.id"
                     + " where p.id_ingredient = ?")) {
            statement.setLong(1, idIngredient);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Price price = priceMapper.apply(resultSet);
                    prices.add(price);
                }
                return prices;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @SneakyThrows
    public List<Price> updateIngredientPrice(Long ingredientId, List<Price> prices) {
        List<Price> savedPrices = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement =
                         connection.prepareStatement("update price set amount  = ?, date_value = ?"
                                 + " where 1 = 1 and id = ? and id_ingredient = ?"
                                 + " returning id, amount, date_value, id_ingredient")) {

                for (Price entityToSave : prices) {
                    try {
                        statement.setDouble(1, entityToSave.getAmount());
                        statement.setTimestamp(2, Timestamp.valueOf(entityToSave.getDateValue().toString()));
                        statement.setLong(3, entityToSave.getId());
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

}
