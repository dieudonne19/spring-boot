package org.dd_lgp.com.tutospring.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@EqualsAndHashCode
@ToString

public class DishOrder {
    private Long id;
    private Order order;
    private Dish dish;
    private Long dishQuantity;
    private List<DishOrderStatus> dishOrderStatuses;
    private LocalDateTime orderDatetime;

    public OrderProcessStatus getActualStatus() {
        DishOrderStatus recentDishOrderStatus = this.dishOrderStatuses.stream().max(Comparator.comparing(DishOrderStatus::getDatetime)).orElse(new DishOrderStatus());
        boolean isAllCreated = this.dishOrderStatuses.stream().allMatch(dishOrderStatus -> dishOrderStatus.getOrderProcessStatus().equals(OrderProcessStatus.CREATED));
        boolean isAllConfirmed = this.dishOrderStatuses.stream().allMatch(dishOrderStatus -> dishOrderStatus.getOrderProcessStatus().equals(OrderProcessStatus.CONFIRMED));
        boolean isAllPreparing = this.dishOrderStatuses.stream().allMatch(dishOrderStatus -> dishOrderStatus.getOrderProcessStatus().equals(OrderProcessStatus.IN_PROGRESS));
        boolean isAllFinished = this.dishOrderStatuses.stream().allMatch(dishOrderStatus -> dishOrderStatus.getOrderProcessStatus().equals(OrderProcessStatus.FINISHED));
        boolean isAllDelivered = this.dishOrderStatuses.stream().allMatch(dishOrderStatus -> dishOrderStatus.getOrderProcessStatus().equals(OrderProcessStatus.DELIVERED));

        OrderStatus orderStatus = new OrderStatus();

        if (isAllCreated) {
            orderStatus.setOrderProcessStatus(OrderProcessStatus.CREATED);
        }
        else if (isAllConfirmed) {
            orderStatus.setOrderProcessStatus(OrderProcessStatus.CONFIRMED);
        }
        else if (isAllPreparing) {
            orderStatus.setOrderProcessStatus(OrderProcessStatus.IN_PROGRESS);
        }
        else if (isAllFinished) {
            orderStatus.setOrderProcessStatus(OrderProcessStatus.FINISHED);
        }
        else if (isAllDelivered) {
            orderStatus.setOrderProcessStatus(OrderProcessStatus.DELIVERED);
        } else orderStatus.setOrderProcessStatus(recentDishOrderStatus.getOrderProcessStatus());
        return orderStatus.getOrderProcessStatus();
    }

    private DishOrderStatus updateStatus() {
        OrderProcessStatus dishActualStatus = this.getActualStatus();

        DishOrderStatus actualStatus = new DishOrderStatus();

        switch (dishActualStatus) {
            case CREATED -> {
                actualStatus.setOrderProcessStatus(OrderProcessStatus.CONFIRMED);
                actualStatus.setDatetime(LocalDateTime.now());
            }
            case CONFIRMED -> {
                actualStatus.setOrderProcessStatus(OrderProcessStatus.IN_PROGRESS);
                actualStatus.setDatetime(LocalDateTime.now());
            }
            case IN_PROGRESS -> {
                actualStatus.setOrderProcessStatus(OrderProcessStatus.FINISHED);
                actualStatus.setDatetime(LocalDateTime.now());
            }
            case FINISHED -> {
                actualStatus.setOrderProcessStatus(OrderProcessStatus.DELIVERED);
                actualStatus.setDatetime(LocalDateTime.now());
            }
            default -> {
                return actualStatus;
            }
        }

        this.dishOrderStatuses.add(actualStatus);
        return actualStatus;
    }
}
