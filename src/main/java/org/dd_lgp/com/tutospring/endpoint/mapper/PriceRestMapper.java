package org.dd_lgp.com.tutospring.endpoint.mapper;

import org.dd_lgp.com.tutospring.endpoint.rest.PriceRest;
import org.dd_lgp.com.tutospring.model.Price;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class PriceRestMapper implements Function<Price, PriceRest> {
    @Override
    public PriceRest apply(Price price) {
        return new PriceRest(price.getId(), price.getAmount(), price.getDateValue());
    }
}
