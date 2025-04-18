INSERT INTO dish_ingredient (id, id_dish, id_ingredient, required_quantity, unit)
VALUES (1, 1, 1, 100, 'G'),
       (2, 1, 2, 0.15, 'L'),
       (3, 1, 3, 1, 'U'),
       (4, 1, 4, 1, 'U') on conflict do nothing ;