package org.dd_lgp.com.tutospring.endpoint.mapper;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.dd_lgp.com.tutospring.dao.mapper.DishSoldMapper;
import org.dd_lgp.com.tutospring.endpoint.rest.DishSoldRest;
import org.dd_lgp.com.tutospring.endpoint.rest.DishSoldRestWithSalesPoint;
import org.dd_lgp.com.tutospring.model.DishSold;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class DishSoldRestWithSalesPointMapper implements Function<DishSold, DishSoldRestWithSalesPoint> {
    private final DishSoldMapper dishSoldMapper;

    @Override
    public DishSoldRestWithSalesPoint apply(DishSold dishSold) {
        return null;
    }

    @SneakyThrows
    public DishSoldRestWithSalesPoint toRest(DishSold dishSold){
        DishSoldRestWithSalesPoint dishSoldRest = new DishSoldRestWithSalesPoint();
        dishSoldRest.setSalesPoint(dishSold.getSalesPoint());
        dishSoldRest.setQuantitySold(dishSold.getQuantitySold());
        dishSoldRest.setTotalAmount(dishSold.getTotalAmount());
        dishSoldRest.setDish(dishSold.getDish());

        return dishSoldRest;
    }
}
