package org.dd_lgp.com.tutospring.endpoint;


import lombok.RequiredArgsConstructor;
import org.dd_lgp.com.tutospring.model.Order;
import org.dd_lgp.com.tutospring.model.UpdateDishOrderStatus;
import org.dd_lgp.com.tutospring.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("")
    public Object createManyOrders(@RequestBody List<Order> orders) {
        return orderService.createAllOrders(orders);
    }

    @GetMapping("/{reference}")
    public Object getOrdersByReference(@PathVariable(name = "reference") String reference){
        return orderService.getOrderByReference(reference);
    }

    @PostMapping("/{reference}")
    public Object createOrderByReference(@PathVariable(name = "reference") String reference){
        return orderService.createOrderByReference(reference);
    }

    @PutMapping("/{reference}/dishes/{dishId}")
    public Object updateDishStatus(@PathVariable(name = "reference") String reference, @PathVariable(name = "dishId") Long dishId, @RequestBody UpdateDishOrderStatus entity) {
        return orderService.updateDishesStatus(reference, dishId, entity);
    }
}
