-- 새로운 어드민 계정 생성
GRANT ALL PRIVILEGES ON *.* TO 'hhplus'@'%' WITH GRANT OPTION;

-- 새로운 데이터베이스 생성
DROP DATABASE IF EXISTS `hhplus`;
CREATE DATABASE `hhplus`;
USE `hhplus`;

-- USER table
CREATE TABLE USER (
                      id BIGINT NOT NULL AUTO_INCREMENT,
                      name VARCHAR(100) NOT NULL,
                      created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      last_modified_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                      PRIMARY KEY (id)
);

-- USER_BALANCE table
CREATE TABLE USER_BALANCE (
                      id BIGINT NOT NULL AUTO_INCREMENT,
                      balance BIGINT DEFAULT 0,
                      user_id BIGINT NOT NULL,
                      created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      last_modified_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                      PRIMARY KEY (id)
);

-- COUPON_MOLD table
CREATE TABLE COUPON_MOLD (
                     id BIGINT NOT NULL AUTO_INCREMENT,
                     name VARCHAR(100) NOT NULL,
                     discount_amount BIGINT NOT NULL,
                     expiration_date DATETIME NOT NULL,
                     created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                     last_modified_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                     PRIMARY KEY (id)
);

-- COUPON_MOLD_INVENTORY table
CREATE TABLE COUPON_MOLD_INVENTORY (
                   id BIGINT NOT NULL AUTO_INCREMENT,
                   stock INT NOT NULL,
                   coupon_mold_id BIGINT NOT NULL,
                   created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                   last_modified_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                   PRIMARY KEY (id)
);

-- USER_COUPON table
CREATE TABLE USER_COUPON (
                     id BIGINT NOT NULL AUTO_INCREMENT,
                     used BOOLEAN DEFAULT FALSE,
                     used_at DATETIME,
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
                     name VARCHAR(100) NOT NULL,
                     price BIGINT NOT NULL,
                     created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                     last_modified_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                     PRIMARY KEY (id)
);

-- PRODUCT_INVENTORY table
CREATE TABLE PRODUCT_INVENTORY (
                   id BIGINT NOT NULL AUTO_INCREMENT,
                   stock INT NOT NULL,
                   product_id BIGINT NOT NULL,
                   created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                   last_modified_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                   PRIMARY KEY (id)
);

-- ORDERS table
CREATE TABLE ORDERS (
                    id BIGINT NOT NULL AUTO_INCREMENT,
                    total_price BIGINT NOT NULL,
                    state ENUM('PAYMENT_WAITING', 'DELIVERY_COMPLETED', 'CANCELED') NOT NULL,
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
                    price BIGINT NOT NULL,
                    quantity INT NOT NULL,
                    discount_amount BIGINT NOT NULL,
                    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                    last_modified_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    PRIMARY KEY (id)
);

-- PAYMENT table
CREATE TABLE PAYMENT (
                     id BIGINT NOT NULL AUTO_INCREMENT,
                     order_id BIGINT NOT NULL,
                     user_coupon_id BIGINT,
                     amount BIGINT NOT NULL,
                     discount_amount BIGINT NOT NULL,
                     status ENUM('PENDING', 'COMPLETED', 'FAILED') NOT NULL,
                     paid_at DATETIME,
                     created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                     last_modified_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                     PRIMARY KEY (id)
);