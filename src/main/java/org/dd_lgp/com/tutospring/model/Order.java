package org.dd_lgp.com.tutospring.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode
@ToString

public class Order {
    private Long id;
    private String reference;
    private List<DishOrder> dishOrders;
    private List<OrderStatus> orderStatuses;
    private String destination;
    private LocalDateTime creationDatetime;

    public OrderProcessStatus getActualStatus() {
        OrderStatus orderStatus = this.orderStatuses.stream().max(Comparator.comparing(OrderStatus::getDatetime)).orElse(new OrderStatus());
        return orderStatus.getOrderProcessStatus();
    }

    public DishOrderStatus updateStatus() {
        List<OrderProcessStatus> dishActualStatus = this.dishOrders.stream().map(DishOrder::getActualStatus).toList();

        DishOrderStatus actualStatus = new DishOrderStatus();

        boolean isAllMatched = dishActualStatus.stream().allMatch(orderStatus -> {
            boolean status = true;
            switch (orderStatus) {
                case CREATED, CONFIRMED, IN_PROGRESS, FINISHED, DELIVERED -> {
                    actualStatus.setOrderProcessStatus(orderStatus);
                    actualStatus.setDatetime(LocalDateTime.now());
                }
                default -> status = false;
            }
            return status;
        });
        if (!isAllMatched) {
            throw new RuntimeException("Invalid PROCESS");
        }
        this.checkStatus(actualStatus);
        return actualStatus;
    }

    public void checkStatus(DishOrderStatus dishOrderStatus) {
        OrderProcessStatus orderStatus = this.getActualStatus();
        if (!orderStatus.equals(dishOrderStatus.getOrderProcessStatus())) {
            OrderStatus newStatus = new OrderStatus();

            newStatus.setOrderProcessStatus(dishOrderStatus.getOrderProcessStatus());
            newStatus.setDatetime(dishOrderStatus.getDatetime());
            this.orderStatuses.add(newStatus);
        }
    }

    public Double getTotalAmount() {
        return this.dishOrders.stream()
                .map(dishOrder -> {
                    Double actualDishPrice = dishOrder.getDish().getPrice();
                    Long requiredDishQuantity = dishOrder.getDishQuantity();
                    return actualDishPrice * requiredDishQuantity;
                })
                .reduce(0.0, Double::sum);
    }
}
