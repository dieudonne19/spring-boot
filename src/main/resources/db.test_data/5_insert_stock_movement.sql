INSERT INTO stock_movement (id, quantity, unit, movement_type, creation_datetime, id_ingredient)
VALUES (1, 10000, 'G', 'IN', '2025-02-01T08:00', 1),
       (2, 20, 'L', 'IN', '2025-02-01T08:00', 2),
       (3, 100, 'U', 'IN', '2025-02-01T08:00', 3),
       (4, 50, 'U', 'IN', '2025-02-01T08:00', 4)
on conflict do nothing;

INSERT INTO stock_movement (id, quantity, unit, movement_type, creation_datetime, id_ingredient)
VALUES (5, 10, 'U', 'OUT', '2025-02-02T10:00', 3),
       (6, 10, 'U', 'OUT', '2025-02-03T15:00', 3),
       (7, 20, 'U', 'OUT', '2025-02-05T16:00', 4)
on conflict do nothing;
