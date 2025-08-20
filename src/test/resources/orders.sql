INSERT INTO orders(ordered, required, customerId, status) VALUES
(2000-01-01, 2000-01-15, (select id from customers where name = 'test'), 'PROCESSING')