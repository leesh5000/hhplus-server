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
INSERT INTO PRODUCT (id, name, price) VALUES (4, 'product4', 40000);
INSERT INTO PRODUCT (id, name, price) VALUES (5, 'product5', 50000);
INSERT INTO PRODUCT (id, name, price) VALUES (6, 'product6', 60000);
INSERT INTO PRODUCT (id, name, price) VALUES (7, 'product7', 70000);
INSERT INTO PRODUCT (id, name, price) VALUES (8, 'product8', 80000);
INSERT INTO PRODUCT (id, name, price) VALUES (9, 'product9', 90000);
INSERT INTO PRODUCT (id, name, price) VALUES (10, 'product10', 100000);
INSERT INTO PRODUCT (id, name, price) VALUES (11, 'product11', 110000);
INSERT INTO PRODUCT (id, name, price) VALUES (12, 'product12', 120000);
INSERT INTO PRODUCT (id, name, price) VALUES (13, 'product13', 130000);
INSERT INTO PRODUCT (id, name, price) VALUES (14, 'product14', 140000);
INSERT INTO PRODUCT (id, name, price) VALUES (15, 'product15', 150000);
INSERT INTO PRODUCT (id, name, price) VALUES (16, 'product16', 160000);
INSERT INTO PRODUCT (id, name, price) VALUES (17, 'product17', 170000);
INSERT INTO PRODUCT (id, name, price) VALUES (18, 'product18', 180000);
INSERT INTO PRODUCT (id, name, price) VALUES (19, 'product19', 190000);
INSERT INTO PRODUCT (id, name, price) VALUES (20, 'product20', 200000);

INSERT INTO PRODUCT_INVENTORY (id, stock, product_id) VALUES (1, 10, 1);
INSERT INTO PRODUCT_INVENTORY (id, stock, product_id) VALUES (2, 10, 2);
INSERT INTO PRODUCT_INVENTORY (id, stock, product_id) VALUES (3, 10, 3);
INSERT INTO PRODUCT_INVENTORY (id, stock, product_id) VALUES (4, 10, 4);
INSERT INTO PRODUCT_INVENTORY (id, stock, product_id) VALUES (5, 10, 5);
INSERT INTO PRODUCT_INVENTORY (id, stock, product_id) VALUES (6, 10, 6);
INSERT INTO PRODUCT_INVENTORY (id, stock, product_id) VALUES (7, 10, 7);
INSERT INTO PRODUCT_INVENTORY (id, stock, product_id) VALUES (8, 10, 8);
INSERT INTO PRODUCT_INVENTORY (id, stock, product_id) VALUES (9, 10, 9);
INSERT INTO PRODUCT_INVENTORY (id, stock, product_id) VALUES (10, 10, 10);
INSERT INTO PRODUCT_INVENTORY (id, stock, product_id) VALUES (11, 10, 11);
INSERT INTO PRODUCT_INVENTORY (id, stock, product_id) VALUES (12, 10, 12);
INSERT INTO PRODUCT_INVENTORY (id, stock, product_id) VALUES (13, 10, 13);
INSERT INTO PRODUCT_INVENTORY (id, stock, product_id) VALUES (14, 10, 14);
INSERT INTO PRODUCT_INVENTORY (id, stock, product_id) VALUES (15, 10, 15);
INSERT INTO PRODUCT_INVENTORY (id, stock, product_id) VALUES (16, 10, 16);
INSERT INTO PRODUCT_INVENTORY (id, stock, product_id) VALUES (17, 10, 17);
INSERT INTO PRODUCT_INVENTORY (id, stock, product_id) VALUES (18, 10, 18);
INSERT INTO PRODUCT_INVENTORY (id, stock, product_id) VALUES (19, 10, 19);
INSERT INTO PRODUCT_INVENTORY (id, stock, product_id) VALUES (20, 10, 20);

INSERT INTO ORDERS (id, order_price, user_id) VALUES (1, 10000, 1);
INSERT INTO ORDERS (id, order_price, user_id) VALUES (2, 20000, 2);
INSERT INTO ORDERS (id, order_price, user_id) VALUES (3, 30000, 3);
INSERT INTO ORDER_PRODUCT (id, order_id, product_id, price, quantity) VALUES (1, 1, 1, 10000, 1);
INSERT INTO ORDER_PRODUCT (id, order_id, product_id, price, quantity) VALUES (2, 2, 2, 20000, 1);
INSERT INTO ORDER_PRODUCT (id, order_id, product_id, price, quantity) VALUES (3, 3, 3, 30000, 1);

INSERT INTO PAYMENT (id, order_id, amount, discount_amount) VALUES (1, 1, 10000, 0);
INSERT INTO PAYMENT (id, order_id, amount, discount_amount) VALUES (2, 2, 20000, 0);
INSERT INTO PAYMENT (id, order_id, amount, discount_amount) VALUES (3, 3, 30000, 0);