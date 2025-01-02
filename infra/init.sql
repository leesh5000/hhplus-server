-- 새로운 어드민 계정 생성
GRANT ALL PRIVILEGES ON *.* TO 'hhplus'@'%' WITH GRANT OPTION;

-- 새로운 데이터베이스 생성
DROP DATABASE IF EXISTS `hhplus`;
CREATE DATABASE `hhplus`;
USE `hhplus`;

-- 1) USERS 테이블
CREATE TABLE `users` (
                         `id` BIGINT NOT NULL AUTO_INCREMENT,
                         `name` VARCHAR(100) NOT NULL,
                         `balance` BIGINT NOT NULL DEFAULT 0,
                         PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2) BALANCE_HISTORY 테이블
CREATE TABLE `balance_history` (
                                   `id` BIGINT NOT NULL AUTO_INCREMENT,
                                   `user_id` BIGINT NOT NULL,
                                   `usage` BIGINT NOT NULL,
                                   `reason` VARCHAR(255) NOT NULL,
                                   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3) COUPON_MOLD 테이블
CREATE TABLE `coupon_mold` (
                               `id` BIGINT NOT NULL AUTO_INCREMENT,
                               `discount_amount` BIGINT NOT NULL,
                               `quantity` INT NOT NULL,
                               `expiration_date` DATE NOT NULL,
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4) USER_COUPON 테이블
CREATE TABLE `user_coupon` (
                               `id` BIGINT NOT NULL AUTO_INCREMENT,
                               `owner_id` BIGINT NOT NULL,
                               `coupon_mold_id` BIGINT NOT NULL,
                               `discount_amount` BIGINT NOT NULL,
                               `expiration_date` DATE NOT NULL,
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5) PRODUCT 테이블
CREATE TABLE `product` (
                           `id` BIGINT NOT NULL AUTO_INCREMENT,
                           `name` VARCHAR(100) NOT NULL,
                           `price` BIGINT NOT NULL,
                           `stock_quantity` INT NOT NULL,
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 6) ORDERS 테이블
-- 여기서는 state 를 ENUM 예시로 사용
CREATE TABLE `orders` (
                          `id` BIGINT NOT NULL AUTO_INCREMENT,
                          `total_price` BIGINT NOT NULL,
                          `state` ENUM('PAYMENT_WAITING', 'DELIVERY_COMPLETED', 'CANCELED') NOT NULL DEFAULT 'PAYMENT_WAITING',
                          `orderer_id` BIGINT NOT NULL,
                          `orderer_name` VARCHAR(100) NOT NULL,
                          PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 7) ORDER_ITEM 테이블
CREATE TABLE `order_item` (
                              `id` BIGINT NOT NULL AUTO_INCREMENT,
                              `order_id` BIGINT NOT NULL,
                              `product_id` BIGINT NOT NULL,
                              `price` BIGINT NOT NULL,
                              `quantity` INT NOT NULL,
                              PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
