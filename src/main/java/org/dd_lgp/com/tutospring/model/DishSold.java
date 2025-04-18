package org.dd_lgp.com.tutospring.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode
@ToString
public class DishSold {
    private Dish dish;
    private Double quantitySold;
}
