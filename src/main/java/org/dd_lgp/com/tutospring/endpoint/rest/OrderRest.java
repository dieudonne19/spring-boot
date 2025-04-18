package org.dd_lgp.com.tutospring.endpoint.rest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dd_lgp.com.tutospring.model.DishOrderStatus;
import org.dd_lgp.com.tutospring.model.Order;
import org.dd_lgp.com.tutospring.model.OrderProcessStatus;
import org.dd_lgp.com.tutospring.model.OrderStatus;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderRest {
    private Long id;
    @JsonIgnore
    private String reference;
    @JsonSetter("dishes")
    private List<DishOrderRest> dishOrders;
    @JsonIgnore
    private List<OrderStatus> orderStatuses;
    @JsonIgnore
    private String destination;
    @JsonIgnore
    private LocalDateTime creationDatetime;

    public OrderProcessStatus getActualStatus() {
        boolean isAllCreated = this.dishOrders.stream().allMatch(dishOrderRest -> dishOrderRest.getActualOrderStatus().equals(OrderProcessStatus.CREATED));
        boolean isAllConfirmed = this.dishOrders.stream().allMatch(dishOrderRest -> dishOrderRest.getActualOrderStatus().equals(OrderProcessStatus.CONFIRMED));
        boolean isAllPreparing = this.dishOrders.stream().allMatch(dishOrderRest -> dishOrderRest.getActualOrderStatus().equals(OrderProcessStatus.IN_PROGRESS));
        boolean isAllFinished = this.dishOrders.stream().allMatch(dishOrderRest -> dishOrderRest.getActualOrderStatus().equals(OrderProcessStatus.FINISHED));
        boolean isAllDelivered = this.dishOrders.stream().allMatch(dishOrderRest -> dishOrderRest.getActualOrderStatus().equals(OrderProcessStatus.DELIVERED));

        OrderStatus orderStatus = new OrderStatus();

        if (isAllCreated) {
            orderStatus.setOrderProcessStatus(OrderProcessStatus.CREATED);
            this.orderStatuses.add(orderStatus);
        } else if (isAllConfirmed) {
            orderStatus.setOrderProcessStatus(OrderProcessStatus.CONFIRMED);
            this.orderStatuses.add(orderStatus);
        } else if (isAllPreparing) {
            orderStatus.setOrderProcessStatus(OrderProcessStatus.IN_PROGRESS);
            this.orderStatuses.add(orderStatus);
        } else if (isAllFinished) {
            orderStatus.setOrderProcessStatus(OrderProcessStatus.FINISHED);
            this.orderStatuses.add(orderStatus);
        } else if (isAllDelivered) {
            orderStatus.setOrderProcessStatus(OrderProcessStatus.DELIVERED);
            this.orderStatuses.add(orderStatus);
        }
        // OrderStatus orderStatus = this.orderStatuses.stream().max(Comparator.comparing(OrderStatus::getDatetime)).orElse(new OrderStatus());
        return this.orderStatuses.getLast().getOrderProcessStatus();
    }

    public Double getTotalAmount() {
        return this.dishOrders.stream()
                .map(dishOrder -> {
                    Double actualDishPrice = dishOrder.getDishRest().getPrice();
                    Long requiredDishQuantity = dishOrder.getDishQuantity();
                    return actualDishPrice * requiredDishQuantity;
                })
                .reduce(0.0, Double::sum);
    }
}
