-- 새로운 어드민 계정 생성
GRANT ALL PRIVILEGES ON *.* TO 'hhplus'@'%' WITH GRANT OPTION;

-- 새로운 데이터베이스 생성
DROP DATABASE IF EXISTS `hhplus`;
CREATE DATABASE `hhplus`;
USE `hhplus`;

-- USER table
CREATE TABLE USER (
                      id BIGINT NOT NULL AUTO_INCREMENT,
                      name VARCHAR(100) NOT NULL COMMENT '사용자 이름',
                      created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      last_modified_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                      PRIMARY KEY (id)
);

-- USER_BALANCE table
CREATE TABLE USER_BALANCE (
                      id BIGINT NOT NULL AUTO_INCREMENT,
                      balance BIGINT DEFAULT 0 COMMENT '사용자 잔액',
                      user_id BIGINT NOT NULL COMMENT '사용자 ID',
                      created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      last_modified_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                      PRIMARY KEY (id)
);

-- COUPON_MOLD table
CREATE TABLE COUPON_MOLD (
                     id BIGINT NOT NULL AUTO_INCREMENT,
                     name VARCHAR(100) NOT NULL COMMENT '쿠폰 이름',
                     discount_amount BIGINT NOT NULL COMMENT '할인 금액',
                     expiration_date DATETIME NOT NULL COMMENT '만료일',
                     created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                     last_modified_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                     PRIMARY KEY (id)
);

-- COUPON_MOLD_INVENTORY table
CREATE TABLE COUPON_MOLD_INVENTORY (
                   id BIGINT NOT NULL AUTO_INCREMENT,
                   stock INT NOT NULL COMMENT '재고',
                   coupon_mold_id BIGINT NOT NULL,
                   created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                   last_modified_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                   PRIMARY KEY (id)
);

-- USER_COUPON table
CREATE TABLE USER_COUPON (
                     id BIGINT NOT NULL AUTO_INCREMENT,
                     used BOOLEAN DEFAULT FALSE COMMENT '사용 여부',
                     used_at DATETIME COMMENT '사용 일시',
                     user_id BIGINT NOT NULL,
                     coupon_mold_id BIGINT NOT NULL,
                     payment_id BIGINT,
                     created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                     last_modified_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                     PRIMARY KEY (id)
);

-- PRODUCT table
CREATE TABLE PRODUCT (
                     id BIGINT NOT NULL AUTO_INCREMENT,
                     name VARCHAR(100) NOT NULL COMMENT '상품 이름',
                     price BIGINT NOT NULL COMMENT '상품 가격',
                     created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                     last_modified_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                     PRIMARY KEY (id)
);

-- PRODUCT_INVENTORY table
CREATE TABLE PRODUCT_INVENTORY (
                   id BIGINT NOT NULL AUTO_INCREMENT,
                   stock INT NOT NULL COMMENT '재고',
                   product_id BIGINT NOT NULL,
                   created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                   last_modified_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                   PRIMARY KEY (id)
);

-- ORDER table
CREATE TABLE ORDERS (
                    id BIGINT NOT NULL AUTO_INCREMENT,
                    total_price BIGINT NOT NULL COMMENT '총 가격',
                    state ENUM('PAYMENT_WAITING', 'DELIVERY_COMPLETED', 'CANCELED') NOT NULL COMMENT '주문 상태',
                    user_id BIGINT NOT NULL,
                    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                    last_modified_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    PRIMARY KEY (id)
);

-- ORDER_PRODUCT table
CREATE TABLE ORDER_PRODUCT (
                    id BIGINT NOT NULL AUTO_INCREMENT,
                    order_id BIGINT NOT NULL,
                    product_id BIGINT NOT NULL,
                    price BIGINT NOT NULL COMMENT '주문 상품 가격',
                    quantity INT NOT NULL COMMENT '주문 상품 수량',
                    discount_amount BIGINT NOT NULL COMMENT '할인 금액',
                    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                    last_modified_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    PRIMARY KEY (id)
);

-- PAYMENT table
CREATE TABLE PAYMENT (
                     id BIGINT NOT NULL AUTO_INCREMENT,
                     order_id BIGINT NOT NULL,
                     user_coupon_id BIGINT,
                     amount BIGINT NOT NULL COMMENT '결제 금액',
                     discount_amount BIGINT NOT NULL COMMENT '할인 금액',
                     status ENUM('PENDING', 'COMPLETED', 'FAILED') NOT NULL COMMENT '결제 상태',
                     paid_at DATETIME COMMENT '결제 일시',
                     created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                     last_modified_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                     PRIMARY KEY (id)
);