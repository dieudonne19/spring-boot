package org.dd_lgp.com.tutospring.endpoint.rest;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class IngredientRest {
    private Long id;
    private String name;
    private List<PriceRest> priceRests;
    private List<StockMovementRest> stockMovementRests;
}
