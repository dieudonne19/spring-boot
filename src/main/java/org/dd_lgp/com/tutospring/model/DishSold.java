package org.dd_lgp.com.tutospring.model;

import lombok.*;

@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode
@ToString
public class DishSold {
    private String salesPoint;
    private Dish dish;
    private Double quantitySold;
    private Double totalAmount;

    public DishSold(){
        this.salesPoint = "Sabotsy Namehana";
    }
}
