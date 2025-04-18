package org.dd_lgp.com.tutospring.dao.operations;

import lombok.RequiredArgsConstructor;
import org.dd_lgp.com.tutospring.dao.DataSource;
import org.dd_lgp.com.tutospring.dao.PostgresNextReference;
import org.dd_lgp.com.tutospring.dao.mapper.StockMovementMapper;
import org.dd_lgp.com.tutospring.endpoint.rest.StockMovementRest;
import org.dd_lgp.com.tutospring.model.StockMovement;
import org.dd_lgp.com.tutospring.model.StockMovementType;
import org.dd_lgp.com.tutospring.model.Unit;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.time.Instant.now;

@Repository
@RequiredArgsConstructor
public class StockOperations implements CrudOperations<StockMovement> {
    private final DataSource dataSource;
    private final StockMovementMapper mapper;
    final PostgresNextReference postgresNextReference = new PostgresNextReference();
    /*
    public StockRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }
     */

    @Override
    public List<StockMovement> getAll(int page, int size) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public StockMovement findById(Long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<StockMovement> saveAll(List<StockMovement> entities) {
        List<StockMovement> stockMovements = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement("insert into stock_movement (id, quantity, unit, movement_type, creation_datetime, id_ingredient) values (?, ?, cast(? as unit), cast(? as stock_movement_type), ?, ?)"
                             + " on conflict (id) do nothing"
                             + " returning id, quantity, unit, movement_type, creation_datetime, id_ingredient")) {
            entities.forEach(entityToSave -> {
                try {
                    Long id = entityToSave.getId() == null ? postgresNextReference.nextID("stock_movement", connection) : entityToSave.getId();
                    statement.setLong(1, id);
                    statement.setDouble(2, entityToSave.getQuantity());
                    statement.setString(3, entityToSave.getUnit().name());
                    statement.setString(4, entityToSave.getMovementType().name());
                    statement.setTimestamp(5, Timestamp.from(now()));
                    statement.setLong(6, entityToSave.getIngredient().getId());
                    statement.addBatch(); // group by batch so executed as one query in database
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    stockMovements.add(mapper.apply(resultSet));
                }
            }
            return stockMovements;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<StockMovement> findByIdIngredient(Long idIngredient) {
        List<StockMovement> stockMovements = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "select s.id, s.quantity, s.unit, s.movement_type, s.creation_datetime from stock_movement s"
                             + " join ingredient i on s.id_ingredient = i.id"
                             + " where s.id_ingredient = ?")) {
            statement.setLong(1, idIngredient);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    stockMovements.add(mapper.apply(resultSet));
                }
                return stockMovements;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}