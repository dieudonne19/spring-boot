package org.dd_lgp.com.tutospring.endpoint.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.dd_lgp.com.tutospring.model.Price;
import org.dd_lgp.com.tutospring.model.StockMovement;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.dd_lgp.com.tutospring.model.StockMovementType.IN;
import static org.dd_lgp.com.tutospring.model.StockMovementType.OUT;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class IngredientRest {
    private Long id;
    private String name;

    @JsonProperty("prices")
    private List<PriceRest> priceRests;

    @JsonProperty("stockMovements")
    private List<StockMovementRest> stockMovementRests;


    public List<PriceRest> addPrices(List<PriceRest> prices) {
        if (getPriceRests() == null || getPriceRests().isEmpty()) {
            return prices;
        }
        // prices.forEach(price -> price.setIngredient(this));
        getPriceRests().addAll(prices);
        return getPriceRests();
    }

    public Double getActualPrice() {
        return findActualPrice().orElse(new PriceRest(0.0)).getAmount();
    }

    public Double getAvailableQuantity() {
        return getAvailableQuantityAt(Instant.now());
    }

    public Double getPriceAt(LocalDate dateValue) {
        return findPriceAt(dateValue).orElse(new PriceRest(0.0)).getAmount();
    }

    public Double getAvailableQuantityAt(Instant datetime) {
        List<StockMovementRest> stockMovementsBeforeToday = stockMovementRests.stream()
                .filter(stockMovement ->
                        stockMovement.getCreationDatetime().isBefore(datetime)
                                || stockMovement.getCreationDatetime().equals(datetime))
                .toList();
        double quantity = 0.0;
        for (StockMovementRest stockMovement : stockMovementsBeforeToday) {
            if (IN.equals(stockMovement.getType())) {
                quantity += stockMovement.getQuantity();
            } else if (OUT.equals(stockMovement.getType())) {
                quantity -= stockMovement.getQuantity();
            }
        }
        return quantity;
    }

    private Optional<PriceRest> findPriceAt(LocalDate dateValue) {
        return priceRests.stream()
                .filter(price -> price.getDateValue().equals(dateValue))
                .findFirst();
    }

    private Optional<PriceRest> findActualPrice() {
        return priceRests.stream().max(Comparator.comparing(PriceRest::getDateValue));
    }
}
