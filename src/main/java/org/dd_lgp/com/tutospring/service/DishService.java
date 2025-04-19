package org.dd_lgp.com.tutospring.service;


import lombok.RequiredArgsConstructor;
import org.dd_lgp.com.tutospring.dao.operations.DishCrudOperations;
import org.dd_lgp.com.tutospring.endpoint.mapper.DishRestMapper;
import org.dd_lgp.com.tutospring.endpoint.mapper.DishSoldRestMapper;
import org.dd_lgp.com.tutospring.endpoint.mapper.DishSoldRestWithSalesPointMapper;
import org.dd_lgp.com.tutospring.endpoint.rest.DishSoldRest;
import org.dd_lgp.com.tutospring.endpoint.rest.DishSoldRestWithSalesPoint;
import org.dd_lgp.com.tutospring.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DishService {
    private final DishRestMapper dishRestMapper;
    private final DishCrudOperations dishCrudOperations;
    private final DishSoldRestMapper dishSoldRestMapper;
    private final DishSoldRestWithSalesPointMapper dishSoldRestWithSalesPointMapper;


    public ResponseEntity<Object> getAllDishes(int page, int pageSize) {
        if (page < 1 || pageSize < 1) {
            return new ResponseEntity<>(page + "/" + pageSize + " must be a positive number", HttpStatus.BAD_REQUEST);
        }
        List<Dish> dishList = dishCrudOperations.getAll(page, pageSize);

        // List<DishRest> dishRests = dishList.stream().map(dishRestMapper::toRest).toList();
        return ResponseEntity.ok().body(dishList);
    }

    public ResponseEntity<Object> insertDishIngredients(Long idDish, List<CreateDishIngredient> entities) {
        Dish dish = dishCrudOperations.insertDishIngredient(idDish, entities);

        // List<DishRest> dishRests = dishList.stream().map(dishRestMapper::toRest).toList();
        return ResponseEntity.ok().body(dish);
    }

    public ResponseEntity<Object> getSales() {
        List<DishSold> dishSolds = dishCrudOperations.getSales();
        List<DishSoldRest> dishSoldRests = dishSolds.stream().map(dishSoldRestMapper::toRest).toList();

        return ResponseEntity.ok().body(dishSoldRests);
    }

    public ResponseEntity<Object> getSalesWithSalesPoint() {
        List<DishSold> dishSolds = dishCrudOperations.getSales();
        List<DishSoldRestWithSalesPoint> dishSoldRestWithSalesPoint = dishSolds.stream().map(dishSoldRestWithSalesPointMapper::toRest).toList();

        return ResponseEntity.ok().body(dishSoldRestWithSalesPoint);
    }
}
