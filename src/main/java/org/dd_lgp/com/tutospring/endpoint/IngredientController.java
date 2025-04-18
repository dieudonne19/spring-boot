package org.dd_lgp.com.tutospring.endpoint;

import org.dd_lgp.com.tutospring.model.Ingredient;
import org.dd_lgp.com.tutospring.model.Price;
import org.dd_lgp.com.tutospring.model.StockMovement;
import org.dd_lgp.com.tutospring.service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/ingredients")
public class IngredientController {
    private final IngredientService ingredientService;

    @Autowired
    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping("")
    public Object getIngredients(@RequestParam(name = "minPriceFilter", required = false) Double minPriceFilter, @RequestParam(name = "maxPriceFilter", required = false) Double maxPriceFilter, @RequestParam(name = "page") Integer page, @RequestParam(name = "pageSize") Integer pageSize) {
        return ingredientService.getAllIngredients(minPriceFilter, maxPriceFilter, page, pageSize);
    }

    @GetMapping("/{id}")
    public Object getIngredientById(@PathVariable(name = "id") Long id) {
        return ingredientService.getIngredientById(id);
    }

    @PostMapping("")
    public Object createManyIngredients(@RequestBody List<Ingredient> ingredients) {
        return ingredientService.createAllIngredients(ingredients);
    }

    @PutMapping("")
    public Object updateManyIngredients(@RequestBody List<Ingredient> ingredients) {
        return ingredientService.updateAllIngredients(ingredients);
    }

    @PutMapping("/{ingredientId}/prices")
    public Object createIngredientPrices(@RequestBody List<Price> prices, @PathVariable(name = "ingredientId") Long ingredientId) {
        return ingredientService.createIngredientPrice(ingredientId, prices);
    }

    @PutMapping("/{ingredientId}/stockMovements")
    public Object updateIngredientStockMovements(@RequestBody List<StockMovement> stockMovements, @PathVariable(name = "ingredientId") Long ingredientId) {
        return ingredientService.createIngredientStockMovement(ingredientId, stockMovements);
    }
}
