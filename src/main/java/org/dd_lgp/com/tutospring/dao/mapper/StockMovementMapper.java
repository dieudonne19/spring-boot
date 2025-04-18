package org.dd_lgp.com.tutospring.dao.mapper;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.dd_lgp.com.tutospring.model.StockMovement;
import org.dd_lgp.com.tutospring.model.StockMovementType;
import org.dd_lgp.com.tutospring.model.Unit;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class StockMovementMapper implements Function<ResultSet, StockMovement> {
    @Override
    @SneakyThrows
    public StockMovement apply(ResultSet resultSet) {
        StockMovement stockMovement = new StockMovement();
        stockMovement.setId(resultSet.getLong("id"));
        stockMovement.setQuantity(resultSet.getDouble("quantity"));
        stockMovement.setMovementType(StockMovementType.valueOf(resultSet.getString("movement_type")));
        stockMovement.setUnit(Unit.valueOf(resultSet.getString("unit")));
        stockMovement.setCreationDatetime(resultSet.getTimestamp("creation_datetime").toInstant());
        return stockMovement;
    }
}
