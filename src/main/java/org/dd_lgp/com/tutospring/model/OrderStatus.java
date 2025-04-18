package org.dd_lgp.com.tutospring.model;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode
@ToString

public class OrderStatus {
    private Long id;
    private OrderProcessStatus orderProcessStatus;
    private LocalDateTime datetime;
}
