package org.dd_lgp.com.tutospring.endpoint.mapper;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.dd_lgp.com.tutospring.endpoint.rest.DishOrderRest;
import org.dd_lgp.com.tutospring.endpoint.rest.DishRest;
import org.dd_lgp.com.tutospring.endpoint.rest.DishSoldRest;
import org.dd_lgp.com.tutospring.model.DishSold;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class DishSoldRestMapper implements Function<DishSold, DishSoldRest> {
    @Override
    public DishSoldRest apply(DishSold dishSold) {
        return null;
    }

    @SneakyThrows
    public DishSoldRest toRest(DishSold dishSold){
        DishSoldRest dishSoldRest = new DishSoldRest();
        dishSoldRest.setQuantitySold(dishSold.getQuantitySold());
        dishSoldRest.setDish(dishSold.getDish());

        return dishSoldRest;
    }
}
