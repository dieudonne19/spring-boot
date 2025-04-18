package org.dd_lgp.com.tutospring.endpoint.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dd_lgp.com.tutospring.model.Ingredient;
import org.dd_lgp.com.tutospring.model.StockMovementType;
import org.dd_lgp.com.tutospring.model.Unit;

import java.time.Instant;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StockMovementRest {
    private Long id;
    private Double quantity;
    private Unit unit;
    private StockMovementType type;
    private Instant creationDatetime;
}
