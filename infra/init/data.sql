INSERT INTO USER (name) VALUES ('user1');
INSERT INTO USER (name) VALUES ('user2');
INSERT INTO USER (name) VALUES ('user3');
INSERT INTO USER (name) VALUES ('user4');
INSERT INTO USER (name) VALUES ('user5');

INSERT INTO WALLET (balance, user_id) VALUES (10000, 1);
INSERT INTO WALLET (balance, user_id) VALUES (20000, 2);
INSERT INTO WALLET (balance, user_id) VALUES (30000, 3);
INSERT INTO WALLET (balance, user_id) VALUES (40000, 4);
INSERT INTO WALLET (balance, user_id) VALUES (50000, 5);

INSERT INTO WALLET_HISTORY (amount, wallet_id) VALUES (10000, 1);
INSERT INTO WALLET_HISTORY (amount, wallet_id) VALUES (20000, 2);
INSERT INTO WALLET_HISTORY (amount, wallet_id) VALUES (30000, 3);
INSERT INTO WALLET_HISTORY (amount, wallet_id) VALUES (40000, 4);
INSERT INTO WALLET_HISTORY (amount, wallet_id) VALUES (50000, 5);

INSERT INTO COUPON (name, discount_amount, expired_at) VALUES ('coupon1', 1000, '2025-12-31 23:59:59');
INSERT INTO COUPON_INVENTORY (stock, coupon_id) VALUES (10, 1);

INSERT INTO ISSUED_COUPON (used, user_id, coupon_id) VALUES (FALSE, 1, 1);

INSERT INTO PRODUCT (name, price) VALUES ('product1', 10000);
INSERT INTO PRODUCT (name, price) VALUES ('product2', 20000);
INSERT INTO PRODUCT (name, price) VALUES ('product3', 30000);

INSERT INTO PRODUCT_INVENTORY (stock, product_id) VALUES (10, 1);
INSERT INTO PRODUCT_INVENTORY (stock, product_id) VALUES (10, 2);
INSERT INTO PRODUCT_INVENTORY (stock, product_id) VALUES (10, 3);

INSERT INTO ORDERS (order_price, user_id) VALUES (10000, 1);
INSERT INTO ORDERS (order_price, user_id) VALUES (20000, 2);
INSERT INTO ORDERS (order_price, user_id) VALUES (30000, 3);
INSERT INTO ORDER_PRODUCT (order_id, product_id, price, quantity) VALUES (1, 1, 10000, 1);
INSERT INTO ORDER_PRODUCT (order_id, product_id, price, quantity) VALUES (2, 2, 20000, 1);
INSERT INTO ORDER_PRODUCT (order_id, product_id, price, quantity) VALUES (3, 3, 30000, 1);

INSERT INTO PAYMENT (order_id, amount, discount_amount) VALUES (1, 10000, 0);
INSERT INTO PAYMENT (order_id, amount, discount_amount) VALUES (2, 20000, 0);
INSERT INTO PAYMENT (order_id, amount, discount_amount) VALUES (3, 30000, 0);