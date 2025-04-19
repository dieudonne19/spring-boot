package org.dd_lgp.com.tutospring.endpoint.rest;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.dd_lgp.com.tutospring.model.DishSold;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode
@ToString
public class BestSalesRest {    private LocalDateTime updatedAt;

    @JsonProperty("sales")
    private List<DishSoldRest> dishSoldRests;
}
