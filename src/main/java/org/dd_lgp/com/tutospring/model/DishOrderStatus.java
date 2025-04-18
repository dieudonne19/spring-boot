package org.dd_lgp.com.tutospring.model;

import lombok.*;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString

public class DishOrderStatus {
    private Long id;
    private OrderProcessStatus orderProcessStatus;
    private LocalDateTime datetime;
}
