package org.dd_lgp.com.tutospring.endpoint;


import lombok.RequiredArgsConstructor;
import org.dd_lgp.com.tutospring.model.CreateDishIngredient;
import org.dd_lgp.com.tutospring.service.DishService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class DishRestController {
    private final DishService service;

    @GetMapping("/dishes")
    public Object getAllDishes(@RequestParam(name = "page") Integer page, @RequestParam(name = "pageSize") Integer pageSize) {
        return service.getAllDishes(page, pageSize);
    }

    @PutMapping("/dishes/{id}/ingredients")
    public Object insertDishIngredients(@PathVariable(name = "id") Long id, @RequestBody List<CreateDishIngredient> dishIngredients) {
        return service.insertDishIngredients(id, dishIngredients);
    }

    @GetMapping("/sales")
    public Object getSales() {
        return service.getSales();
    }
}
