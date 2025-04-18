package org.dd_lgp.com.tutospring.endpoint.rest;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dd_lgp.com.tutospring.model.DishOrderStatus;
import org.dd_lgp.com.tutospring.model.OrderProcessStatus;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DishOrderRest {
    private Long id;
    @JsonIgnore
    private DishRest dishRest;
    @JsonProperty("quantityOrdered")
    private Long dishQuantity;

    @JsonIgnore
    private List<DishOrderStatus> dishOrderStatuses;
    @JsonIgnore
    private LocalDateTime orderDatetime;

    public OrderProcessStatus getActualOrderStatus() {
        DishOrderStatus dishOrderStatus = this.dishOrderStatuses.stream().max(Comparator.comparing(DishOrderStatus::getDatetime)).orElse(new DishOrderStatus());
        return dishOrderStatus.getOrderProcessStatus();
    }

    @JsonProperty("name")
    public String getDishName() {
        return this.dishRest.getName();
    }
}
