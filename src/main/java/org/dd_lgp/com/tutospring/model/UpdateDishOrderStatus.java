package org.dd_lgp.com.tutospring.model;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class UpdateDishOrderStatus {
    OrderProcessStatus status;
}
