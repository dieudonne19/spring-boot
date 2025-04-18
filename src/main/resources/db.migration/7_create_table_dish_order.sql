CREATE TABLE IF NOT EXISTS dish_order
(
    id              bigserial primary key,
    id_dish         bigint references dish (id),
    id_order        bigint references "order" (id),
    dish_quantity   numeric,
    order_date_time timestamp
);
