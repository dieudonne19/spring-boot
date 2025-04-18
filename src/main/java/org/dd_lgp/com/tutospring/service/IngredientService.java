package org.dd_lgp.com.tutospring.service;

import lombok.RequiredArgsConstructor;
import org.dd_lgp.com.tutospring.dao.mapper.IngredientMapper;
import org.dd_lgp.com.tutospring.endpoint.mapper.IngredientRestMapper;
import org.dd_lgp.com.tutospring.endpoint.mapper.PriceRestMapper;
import org.dd_lgp.com.tutospring.endpoint.mapper.StockMovementRestMapper;
import org.dd_lgp.com.tutospring.endpoint.rest.IngredientRest;
import org.dd_lgp.com.tutospring.endpoint.rest.PriceRest;
import org.dd_lgp.com.tutospring.endpoint.rest.StockMovementRest;
import org.dd_lgp.com.tutospring.model.Ingredient;
import org.dd_lgp.com.tutospring.dao.operations.IngredientOperations;
import org.dd_lgp.com.tutospring.model.Price;
import org.dd_lgp.com.tutospring.model.StockMovement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientService {
    private final IngredientOperations operation;
    private final IngredientRestMapper ingredientRestMapper;
    private final PriceRestMapper priceRestMapper;
    private final StockMovementRestMapper stockMovementRestMapper;


    public ResponseEntity<Object> getAllIngredients(Double minPriceFilter, Double maxPriceFilter, int page, int pageSize) {
        if (page < 1 || pageSize < 1) {
            return new ResponseEntity<>(page + "/" + pageSize + " must be a positive number", HttpStatus.BAD_REQUEST);
        }
        List<Ingredient> ingredientList = operation.getAll(page, pageSize);
        if (minPriceFilter == null) {
            ingredientList = ingredientList;
        } else {
            if (minPriceFilter < 1) {
                return new ResponseEntity<>("minPriceFilter must be a positive number", HttpStatus.BAD_REQUEST);
            }
            ingredientList = ingredientList.stream().filter(ingredient -> ingredient.getActualPrice() >= minPriceFilter).toList();
        }
        if (maxPriceFilter == null) {
            ingredientList = ingredientList;
        } else {
            if (maxPriceFilter < 1) {
                return new ResponseEntity<>("maxPriceFilter must be a positive number", HttpStatus.BAD_REQUEST);
            }
            ingredientList = ingredientList.stream().filter(ingredient -> ingredient.getActualPrice() <= maxPriceFilter).toList();
        }
        List<IngredientRest> ingredientRests = ingredientList.stream().map(ingredientRestMapper::toRest).toList();
        return ResponseEntity.ok().body(ingredientRests);
    }

    public ResponseEntity<Object> getIngredientById(Long id) {
        if (id < 1) {
            return new ResponseEntity<>(id + " must be a positive number", HttpStatus.BAD_REQUEST);
        }
        IngredientRest ingredient = ingredientRestMapper.toRest(operation.findById(id));
        return ResponseEntity.ok().body(ingredient);
    }

    public ResponseEntity<Object> createAllIngredients(List<Ingredient> entities) {
        if (entities.isEmpty()) {
            return new ResponseEntity<>("Please provide one or many ingredients", HttpStatus.BAD_REQUEST);
        }
        List<Ingredient> ingredients = operation.saveAll(entities);
        List<IngredientRest> ingredientRests = ingredients.stream().map(ingredientRestMapper::toRest).toList();
        return ResponseEntity.ok().body(ingredientRests);
    }


    public ResponseEntity<Object> createIngredientPrice(Long ingredientId, List<Price> entities) {
        if (entities.isEmpty()) {
            return new ResponseEntity<>("Please provide one or many price", HttpStatus.BAD_REQUEST);
        }
        List<Price> prices = operation.createIngredientPrice(ingredientId, entities);
        List<PriceRest> priceRest = prices.stream().map(priceRestMapper).toList();
        return ResponseEntity.ok().body(priceRest);
    }

    public ResponseEntity<Object> createIngredientStockMovement(Long ingredientId, List<StockMovement> entities) {
        if (entities.isEmpty()) {
            return new ResponseEntity<>("Please provide one or many price", HttpStatus.BAD_REQUEST);
        }
        List<StockMovement> stockMovements = operation.createIngredientStockMovement(ingredientId, entities);
        List<StockMovementRest> stockMovementRest = stockMovements.stream().map(stockMovementRestMapper::toRest).toList();
        return ResponseEntity.ok().body(stockMovementRest);
    }

    public ResponseEntity<Object> updateAllIngredients(List<Ingredient> entities) {
        if (entities.isEmpty()) {
            return new ResponseEntity<>("Please provide one or many ingredients", HttpStatus.BAD_REQUEST);
        }
        List<Ingredient> ingredients = operation.updateAll(entities);
        List<IngredientRest> ingredientRests = ingredients.stream().map(ingredientRestMapper::toRest).toList();
        return ResponseEntity.ok().body(ingredientRests);
    }
}
