package org.dd_lgp.com.tutospring.endpoint.rest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dd_lgp.com.tutospring.model.Dish;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DishSoldRest {
    @JsonIgnore
    private Dish dish;
    private Double quantitySold;

    @JsonProperty("dishIdentifier")
    public Long getDishIdentifier() {
        return dish.getId();
    }

    @JsonProperty("dishName")
    public String getDishName() {
        return dish.getName();
    }
}
