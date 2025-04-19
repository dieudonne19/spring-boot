package org.dd_lgp.com.tutospring.service;

import lombok.RequiredArgsConstructor;
import org.dd_lgp.com.tutospring.dao.operations.OrderCrudOperations;
import org.dd_lgp.com.tutospring.endpoint.mapper.OrderRestMapper;
import org.dd_lgp.com.tutospring.endpoint.rest.OrderRest;
import org.dd_lgp.com.tutospring.model.Order;
import org.dd_lgp.com.tutospring.model.UpdateDishOrderStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderCrudOperations orderCrudOperations;
    private final OrderRestMapper orderRestMapper;


    public ResponseEntity<Object> createAllOrders(List<Order> entities) {
        if (entities.isEmpty()) {
            return new ResponseEntity<>("Please provide one or many orders", HttpStatus.BAD_REQUEST);
        }
        List<Order> orders = orderCrudOperations.saveAll(entities);
        List<OrderRest> ordersRest = orders.stream().map(orderRestMapper).toList();
        return ResponseEntity.ok().body(ordersRest);
    }

    public ResponseEntity<Object> getOrderByReference(String reference) {
        if (reference == null) {
            return new ResponseEntity<>("Reference must be defined", HttpStatus.BAD_REQUEST);
        }
        Order order = orderCrudOperations.getOrderByReference(reference);
        OrderRest orderRest = orderRestMapper.apply(order);
        System.out.println(orderRest.getActualStatus());
        return ResponseEntity.ok().body(orderRest);
    }

    public ResponseEntity<Object> updateDishesStatus(String reference, Long dishId, UpdateDishOrderStatus entity) {
        if (reference == null) {
            return new ResponseEntity<>("Reference must be defined", HttpStatus.BAD_REQUEST);
        }
        if (dishId < 0) {
            return new ResponseEntity<>("Dish id must be a positive integer", HttpStatus.BAD_REQUEST);
        }
        Order order = orderCrudOperations.updateDishesStatus(reference, dishId, entity);
        OrderRest orderRest = orderRestMapper.apply(order);
        return ResponseEntity.ok().body(orderRest);
    }

    public ResponseEntity<Object> createOrderByReference(String reference){
        if (reference == null) {
            return new ResponseEntity<>("Reference must be defined", HttpStatus.BAD_REQUEST);
        }
        Order order = orderCrudOperations.createOrderByReference(reference);
        // OrderRest orderRest = orderRestMapper.apply(order);
        return ResponseEntity.ok().body(order);
    }
}
