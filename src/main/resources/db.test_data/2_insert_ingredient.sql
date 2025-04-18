INSERT INTO ingredient (id, name)
VALUES (1, 'saucisse'),
       (2, 'huile'),
       (3, 'oeuf'),
       (4, 'pain') on conflict do nothing ;