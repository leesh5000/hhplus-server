/* INSERT DATA */
INSERT INTO USER (id, name) VALUES (100, 'user1');
INSERT INTO WALLET (id, balance, user_id) VALUES (100, 135000, 100);
INSERT INTO WALLET_HISTORY (id, amount, wallet_id) VALUES (100, 25000, 100);

INSERT INTO COUPON (id, name, discount_amount, expired_at) VALUES (100, 'coupon1', 1000, '2025-12-31 23:59:59');
INSERT INTO COUPON_INVENTORY (id, stock, coupon_id) VALUES (100, 10, 100);

INSERT INTO ISSUED_COUPON (id, used, user_id, coupon_id) VALUES (100, FALSE, 100, 100);

INSERT INTO PRODUCT (id, name, price) VALUES (100, 'product1', 10000);
INSERT INTO PRODUCT (id, name, price) VALUES (101, 'product2', 20000);
INSERT INTO PRODUCT (id, name, price) VALUES (102, 'product3', 30000);

INSERT INTO PRODUCT_INVENTORY (id, stock, product_id) VALUES (100, 10, 100);
INSERT INTO PRODUCT_INVENTORY (id, stock, product_id) VALUES (101, 10, 101);
INSERT INTO PRODUCT_INVENTORY (id, stock, product_id) VALUES (102, 10, 102);
