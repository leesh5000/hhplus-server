-- Insert user with max balance
INSERT INTO USER (id, name) VALUES (100, 'user1');
INSERT INTO WALLET (id, balance, user_id)VALUES (100, 1000000000000, 100);