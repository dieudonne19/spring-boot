
CREATE TABLE IF NOT EXISTS "dish_order_status"
(
    id                 bigserial primary key,
    dish_order_id           bigint references "dish_order" (id) not null,
    order_status       process_status,
    datetime timestamp
    );
