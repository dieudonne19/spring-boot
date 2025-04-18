INSERT INTO "order" (id, reference, destination, creation_date_time)
VALUES (1, 'd-1', 'analamahitsy', current_timestamp),
       (2, 'd-2', 'By-pass', '2025-03-22T08:00')
on conflict do nothing;
