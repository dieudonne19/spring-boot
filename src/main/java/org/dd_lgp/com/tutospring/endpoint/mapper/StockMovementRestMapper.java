package org.dd_lgp.com.tutospring.endpoint.mapper;

import org.dd_lgp.com.tutospring.endpoint.rest.StockMovementRest;
import org.dd_lgp.com.tutospring.model.StockMovement;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class StockMovementRestMapper implements Function<StockMovementRest, StockMovement> {
    @Override
    public StockMovement apply(StockMovementRest stockMovementRest) {
        return null;
    }

    public StockMovementRest toRest(StockMovement stockMovement) {
        StockMovementRest stockMovementRest = new StockMovementRest();
        stockMovementRest.setId(stockMovement.getId());
        stockMovementRest.setQuantity(stockMovement.getQuantity());
        stockMovementRest.setUnit(stockMovement.getUnit());
        stockMovementRest.setType(stockMovement.getMovementType());
        stockMovementRest.setCreationDatetime(stockMovement.getCreationDatetime());
        return stockMovementRest;
    }
}
