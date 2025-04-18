INSERT INTO dish_order (id, id_dish, id_order, dish_quantity, order_date_time)
VALUES (1, 1, 1, 2, current_timestamp),
       (2, 1, 2, 1, current_timestamp),
       (3, 1, 1, 5, current_timestamp)
on conflict do nothing;
