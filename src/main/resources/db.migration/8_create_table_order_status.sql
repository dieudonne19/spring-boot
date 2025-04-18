do
$$
begin
        if not exists(select from pg_type where typname = 'process_status') then
create type process_status as enum ('CREATED', 'CONFIRMED', 'IN_PROGRESS', 'FINISHED', 'DELIVERED');
end if;
end
$$;


CREATE TABLE IF NOT EXISTS "order_status"
(
    id                 bigserial primary key,
    order_id           bigint references "order" (id) not null,
    order_status       process_status,
    datetime timestamp
);
