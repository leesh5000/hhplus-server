/* DELETE ALL DATA */
DELETE FROM USER WHERE true;
DELETE FROM WALLET WHERE true;
DELETE FROM WALLET_HISTORY WHERE true;
DELETE FROM COUPON WHERE true;
DELETE FROM COUPON_INVENTORY WHERE true;
DELETE FROM ISSUED_COUPON WHERE true;

/* INSERT DATA */
INSERT INTO USER (id, name) VALUES (1, 'user1');
INSERT INTO USER (id, name) VALUES (2, 'user2');
INSERT INTO USER (id, name) VALUES (3, 'user3');
INSERT INTO USER (id, name) VALUES (4, 'user4');
INSERT INTO USER (id, name) VALUES (5, 'user5');

INSERT INTO WALLET (id, balance, user_id) VALUES (1, 10000, 1);
INSERT INTO WALLET (id, balance, user_id) VALUES (2, 20000, 2);
INSERT INTO WALLET (id, balance, user_id) VALUES (3, 30000, 3);
INSERT INTO WALLET (id, balance, user_id) VALUES (4, 40000, 4);
INSERT INTO WALLET (id, balance, user_id) VALUES (5, 50000, 5);

INSERT INTO WALLET_HISTORY (id, amount, wallet_id) VALUES (1, 10000, 1);
INSERT INTO WALLET_HISTORY (id, amount, wallet_id) VALUES (2, 20000, 2);
INSERT INTO WALLET_HISTORY (id, amount, wallet_id) VALUES (3, 30000, 3);
INSERT INTO WALLET_HISTORY (id, amount, wallet_id) VALUES (4, 40000, 4);
INSERT INTO WALLET_HISTORY (id, amount, wallet_id) VALUES (5, 50000, 5);

INSERT INTO COUPON (id, name, discount_amount, expired_at) VALUES (1, 'coupon1', 1000, '2025-12-31 23:59:59');
INSERT INTO COUPON_INVENTORY (id, stock, coupon_id) VALUES (1, 10, 1);

INSERT INTO ISSUED_COUPON (id, used, user_id, coupon_id) VALUES (1, FALSE, 1, 1);

INSERT INTO PRODUCT (id, name, price) VALUES (1, 'product1', 10000);
INSERT INTO PRODUCT (id, name, price) VALUES (2, 'product2', 20000);
INSERT INTO PRODUCT (id, name, price) VALUES (3, 'product3', 30000);

INSERT INTO PRODUCT_INVENTORY (id, stock, product_id) VALUES (1, 10, 1);
INSERT INTO PRODUCT_INVENTORY (id, stock, product_id) VALUES (2, 10, 2);
INSERT INTO PRODUCT_INVENTORY (id, stock, product_id) VALUES (3, 10, 3);

INSERT INTO ORDERS (id, order_price, user_id) VALUES (1, 10000, 1);
INSERT INTO ORDERS (id, order_price, user_id) VALUES (2, 20000, 2);
INSERT INTO ORDERS (id, order_price, user_id) VALUES (3, 30000, 3);
INSERT INTO ORDER_PRODUCT (id, order_id, product_id, price, quantity) VALUES (1, 1, 1, 10000, 1);
INSERT INTO ORDER_PRODUCT (id, order_id, product_id, price, quantity) VALUES (2, 2, 2, 20000, 1);
INSERT INTO ORDER_PRODUCT (id, order_id, product_id, price, quantity) VALUES (3, 3, 3, 30000, 1);

INSERT INTO PAYMENT (id, order_id, amount, discount_amount) VALUES (1, 1, 10000, 0);
INSERT INTO PAYMENT (id, order_id, amount, discount_amount) VALUES (2, 2, 20000, 0);
INSERT INTO PAYMENT (id, order_id, amount, discount_amount) VALUES (3, 3, 30000, 0);