CREATE TABLE IF NOT EXISTS "order"
(
    id                 bigserial primary key,
    "reference"        varchar(255) not null,
    destination        varchar(255),
    creation_date_time timestamp
);
