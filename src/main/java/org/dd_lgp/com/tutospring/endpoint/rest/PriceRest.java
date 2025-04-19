package org.dd_lgp.com.tutospring.endpoint.rest;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import static java.time.LocalDate.now;

@AllArgsConstructor
@Getter
@Setter
public class PriceRest {
    private Long id;
    @JsonProperty("price")
    private Double amount;
    private LocalDate dateValue;

    public PriceRest(Double amount) {
        this.amount = amount;
        this.dateValue = now();
    }
}
