-- 20개 상품 데이터 등록
INSERT INTO PRODUCT (id, name, price) VALUES (101, 'product1', 10000);
INSERT INTO PRODUCT_INVENTORY (id, stock, product_id) VALUES (101, 1320, 101);

INSERT INTO PRODUCT (id, name, price) VALUES (102, 'product2', 20000);
INSERT INTO PRODUCT_INVENTORY (id, stock, product_id) VALUES (102, 1210, 102);

INSERT INTO PRODUCT (id, name, price) VALUES (103, 'product3', 30000);
INSERT INTO PRODUCT_INVENTORY (id, stock, product_id) VALUES (103, 1350, 103);

INSERT INTO PRODUCT (id, name, price) VALUES (104, 'product4', 40000);
INSERT INTO PRODUCT_INVENTORY (id, stock, product_id) VALUES (104, 610, 104);

INSERT INTO PRODUCT (id, name, price) VALUES (105, 'product5', 50000);
INSERT INTO PRODUCT_INVENTORY (id, stock, product_id) VALUES (105, 710, 105);

INSERT INTO PRODUCT (id, name, price) VALUES (106, 'product6', 60000);
INSERT INTO PRODUCT_INVENTORY (id, stock, product_id) VALUES (106, 910, 106);

INSERT INTO PRODUCT (id, name, price) VALUES (107, 'product7', 70000);
INSERT INTO PRODUCT_INVENTORY (id, stock, product_id) VALUES (107, 810, 107);

INSERT INTO PRODUCT (id, name, price) VALUES (108, 'product8', 80000);
INSERT INTO PRODUCT_INVENTORY (id, stock, product_id) VALUES (108, 1210, 108);

INSERT INTO PRODUCT (id, name, price) VALUES (109, 'product9', 90000);
INSERT INTO PRODUCT_INVENTORY (id, stock, product_id) VALUES (109, 103, 109);

INSERT INTO PRODUCT (id, name, price) VALUES (110, 'product10', 100000);
INSERT INTO PRODUCT_INVENTORY (id, stock, product_id) VALUES (110, 140, 110);

INSERT INTO PRODUCT (id, name, price) VALUES (111, 'product11', 110000);
INSERT INTO PRODUCT_INVENTORY (id, stock, product_id) VALUES (111, 910, 111);

INSERT INTO PRODUCT (id, name, price) VALUES (112, 'product12', 120000);
INSERT INTO PRODUCT_INVENTORY (id, stock, product_id) VALUES (112, 610, 112);

INSERT INTO PRODUCT (id, name, price) VALUES (113, 'product13', 130000);
INSERT INTO PRODUCT_INVENTORY (id, stock, product_id) VALUES (113, 1430, 113);

INSERT INTO PRODUCT (id, name, price) VALUES (114, 'product14', 140000);
INSERT INTO PRODUCT_INVENTORY (id, stock, product_id) VALUES (114, 11230, 114);

INSERT INTO PRODUCT (id, name, price) VALUES (115, 'product15', 150000);
INSERT INTO PRODUCT_INVENTORY (id, stock, product_id) VALUES (115, 9910, 115);

INSERT INTO PRODUCT (id, name, price) VALUES (116, 'product16', 160000);
INSERT INTO PRODUCT_INVENTORY (id, stock, product_id) VALUES (116, 910, 116);

INSERT INTO PRODUCT (id, name, price) VALUES (117, 'product17', 170000);
INSERT INTO PRODUCT_INVENTORY (id, stock, product_id) VALUES (117, 101, 117);

INSERT INTO PRODUCT (id, name, price) VALUES (118, 'product18', 180000);
INSERT INTO PRODUCT_INVENTORY (id, stock, product_id) VALUES (118, 2310, 118);

INSERT INTO PRODUCT (id, name, price) VALUES (119, 'product19', 190000);
INSERT INTO PRODUCT_INVENTORY (id, stock, product_id) VALUES (119, 310, 119);

INSERT INTO PRODUCT (id, name, price) VALUES (120, 'product20', 200000);
INSERT INTO PRODUCT_INVENTORY (id, stock, product_id) VALUES (120, 150, 120);

-- 주문 데이터 등록

-- OrderPrice 는 OrderProduct 들의 price * quantity 의 합
-- 2024-12-22 12:35:00에 ID가 101인 사용자가 ID가 101인 상품을 5개와 ID가 115인 상품을 1개 주문
INSERT INTO ORDERS (id, order_price, user_id, ordered_at) VALUES (101, 200000, 101, '2024-12-22 12:35:00');
INSERT INTO ORDER_PRODUCT (id, order_id, product_id, price, quantity) VALUES (101, 101, 101, 10000, 5);
INSERT INTO ORDER_PRODUCT (id, order_id, product_id, price, quantity) VALUES (102, 101, 115, 150000, 1);

-- 2024-12-23 06:21:00에 ID가 102인 사용자가 ID가 102인 상품을 3개와 ID가 116인 상품을 2개 주문, 주문 가격은 (상품 가격 * 개수)의 합
INSERT INTO ORDERS (id, order_price, user_id, ordered_at) VALUES (102, 380000, 102, '2024-12-23 06:21:00');
INSERT INTO ORDER_PRODUCT (id, order_id, product_id, price, quantity) VALUES (103, 102, 102, 20000, 3);
INSERT INTO ORDER_PRODUCT (id, order_id, product_id, price, quantity) VALUES (104, 102, 116, 160000, 2);

-- 2024-12-23 09:45:00에 ID가 103인 사용자가 ID가 103인 상품을 2개와 ID가 117인 상품을 1개 주문
INSERT INTO ORDERS (id, order_price, user_id, ordered_at) VALUES (103, 230000, 103, '2024-12-23 09:45:00');
INSERT INTO ORDER_PRODUCT (id, order_id, product_id, price, quantity) VALUES (105, 103, 103, 30000, 2);
INSERT INTO ORDER_PRODUCT (id, order_id, product_id, price, quantity) VALUES (106, 103, 117, 170000, 1);

-- 2024-12-23 11:30:00에 ID가 104인 사용자가 ID가 104인 상품을 6개와 ID가 118인 상품을 12개 주문
INSERT INTO ORDERS (id, order_price, user_id, ordered_at) VALUES (104, 2400000, 104, '2024-12-23 11:30:00');
INSERT INTO ORDER_PRODUCT (id, order_id, product_id, price, quantity) VALUES (107, 104, 104, 40000, 6);
INSERT INTO ORDER_PRODUCT (id, order_id, product_id, price, quantity) VALUES (108, 104, 118, 180000, 12);

-- 2024-12-23 14:00:00에 ID가 105인 사용자가 ID가 105인 상품을 1개와 ID가 119인 상품을 1개 주문
INSERT INTO ORDERS (id, order_price, user_id, ordered_at) VALUES (105, 240000, 105, '2024-12-23 14:00:00');
INSERT INTO ORDER_PRODUCT (id, order_id, product_id, price, quantity) VALUES (109, 105, 105, 50000, 1);
INSERT INTO ORDER_PRODUCT (id, order_id, product_id, price, quantity) VALUES (110, 105, 119, 190000, 1);
