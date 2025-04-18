package org.dd_lgp.com.tutospring.model;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode
@ToString
public class CreateDishIngredient {
    private Long id;
    private String name;
    private Double requiredQuantity;
}
