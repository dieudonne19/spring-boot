package org.dd_lgp.com.tutospring.endpoint.rest;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
public class PriceRest {
    private Long id;
    private Double amount;
    private LocalDate dateValue;
}
