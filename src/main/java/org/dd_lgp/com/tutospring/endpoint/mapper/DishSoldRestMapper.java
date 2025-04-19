package org.dd_lgp.com.tutospring.endpoint.mapper;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.dd_lgp.com.tutospring.dao.mapper.DishSoldMapper;
import org.dd_lgp.com.tutospring.endpoint.rest.DishSoldRest;
import org.dd_lgp.com.tutospring.model.DishSold;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class DishSoldRestMapper implements Function<DishSold, DishSoldRest> {
    private final DishSoldMapper dishSoldMapper;

    @Override
    public DishSoldRest apply(DishSold dishSold) {
        return null;
    }

    @SneakyThrows
    public DishSoldRest toRest(DishSold dishSold){
        DishSoldRest dishSoldRest = new DishSoldRest();
        dishSoldRest.setQuantitySold(dishSold.getQuantitySold());
        dishSoldRest.setTotalAmount(dishSold.getTotalAmount());
        dishSoldRest.setDish(dishSold.getDish());
        // dishSoldRest.setSalesPoint(dishSold.getSalesPoint());

        return dishSoldRest;
    }
}
