package org.dd_lgp.com.tutospring.endpoint.rest;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dd_lgp.com.tutospring.model.DishIngredient;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DishRest {
    private Long id;
    private String name;
    @JsonIgnore
    private List<DishIngredientRest> dishIngredientRests;
    @JsonIgnore
    private Double price;
}
