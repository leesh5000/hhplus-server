-- 30명의 유저와 지갑 데이터 생성
INSERT INTO USER (id, name) VALUES (101, 'user1');
INSERT INTO WALLET (id, balance, user_id) VALUES (101, 10000, 101);

INSERT INTO USER (id, name) VALUES (102, 'user2');
INSERT INTO WALLET (id, balance, user_id) VALUES (102, 20000, 102);

INSERT INTO USER (id, name) VALUES (103, 'user3');
INSERT INTO WALLET (id, balance, user_id) VALUES (103, 30000, 103);

INSERT INTO USER (id, name) VALUES (104, 'user4');
INSERT INTO WALLET (id, balance, user_id) VALUES (104, 40000, 104);

INSERT INTO USER (id, name) VALUES (105, 'user5');
INSERT INTO WALLET (id, balance, user_id) VALUES (105, 50000, 105);

INSERT INTO USER (id, name) VALUES (106, 'user6');
INSERT INTO WALLET (id, balance, user_id) VALUES (106, 60000, 106);

INSERT INTO USER (id, name) VALUES (107, 'user7');
INSERT INTO WALLET (id, balance, user_id) VALUES (107, 70000, 107);

INSERT INTO USER (id, name) VALUES (108, 'user8');
INSERT INTO WALLET (id, balance, user_id) VALUES (108, 80000, 108);

INSERT INTO USER (id, name) VALUES (109, 'user9');
INSERT INTO WALLET (id, balance, user_id) VALUES (109, 90000, 109);

INSERT INTO USER (id, name) VALUES (110, 'user10');
INSERT INTO WALLET (id, balance, user_id) VALUES (110, 100000, 110);

INSERT INTO USER (id, name) VALUES (111, 'user11');
INSERT INTO WALLET (id, balance, user_id) VALUES (111, 110000, 111);

INSERT INTO USER (id, name) VALUES (112, 'user12');
INSERT INTO WALLET (id, balance, user_id) VALUES (112, 120000, 112);

INSERT INTO USER (id, name) VALUES (113, 'user13');
INSERT INTO WALLET (id, balance, user_id) VALUES (113, 130000, 113);

INSERT INTO USER (id, name) VALUES (114, 'user14');
INSERT INTO WALLET (id, balance, user_id) VALUES (114, 140000, 114);

INSERT INTO USER (id, name) VALUES (115, 'user15');
INSERT INTO WALLET (id, balance, user_id) VALUES (115, 150000, 115);

INSERT INTO USER (id, name) VALUES (116, 'user16');
INSERT INTO WALLET (id, balance, user_id) VALUES (116, 160000, 116);

INSERT INTO USER (id, name) VALUES (117, 'user17');
INSERT INTO WALLET (id, balance, user_id) VALUES (117, 170000, 117);

INSERT INTO USER (id, name) VALUES (118, 'user18');
INSERT INTO WALLET (id, balance, user_id) VALUES (118, 180000, 118);

INSERT INTO USER (id, name) VALUES (119, 'user19');
INSERT INTO WALLET (id, balance, user_id) VALUES (119, 190000, 119);

INSERT INTO USER (id, name) VALUES (120, 'user20');
INSERT INTO WALLET (id, balance, user_id) VALUES (120, 200000, 120);

INSERT INTO USER (id, name) VALUES (121, 'user21');
INSERT INTO WALLET (id, balance, user_id) VALUES (121, 210000, 121);

INSERT INTO USER (id, name) VALUES (122, 'user22');
INSERT INTO WALLET (id, balance, user_id) VALUES (122, 220000, 122);

INSERT INTO USER (id, name) VALUES (123, 'user23');
INSERT INTO WALLET (id, balance, user_id) VALUES (123, 230000, 123);

INSERT INTO USER (id, name) VALUES (124, 'user24');
INSERT INTO WALLET (id, balance, user_id) VALUES (124, 240000, 124);

INSERT INTO USER (id, name) VALUES (125, 'user25');
INSERT INTO WALLET (id, balance, user_id) VALUES (125, 250000, 125);

INSERT INTO USER (id, name) VALUES (126, 'user26');
INSERT INTO WALLET (id, balance, user_id) VALUES (126, 260000, 126);

INSERT INTO USER (id, name) VALUES (127, 'user27');
INSERT INTO WALLET (id, balance, user_id) VALUES (127, 270000, 127);

INSERT INTO USER (id, name) VALUES (128, 'user28');
INSERT INTO WALLET (id, balance, user_id) VALUES (128, 280000, 128);

INSERT INTO USER (id, name) VALUES (129, 'user29');
INSERT INTO WALLET (id, balance, user_id) VALUES (129, 290000, 129);

INSERT INTO USER (id, name) VALUES (130, 'user30');
INSERT INTO WALLET (id, balance, user_id) VALUES (130, 300000, 130);

-- 재고가 50인 쿠폰 데이터 생성
INSERT INTO COUPON (id, name, discount_amount, expired_at) VALUES (101, 'coupon1', 1000, '2025-12-31 23:59:59');
INSERT INTO COUPON_INVENTORY (id, stock, coupon_id) VALUES (101, 50, 101);