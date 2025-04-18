INSERT INTO price (id, amount, date_value, id_ingredient)
VALUES (1, 20, '2025-03-15', 1),
       (2, 10000, '2025-03-15', 2),
       (3, 1000, '2025-03-15', 3),
       (4, 1000, '2025-03-15', 4)
on conflict do nothing;