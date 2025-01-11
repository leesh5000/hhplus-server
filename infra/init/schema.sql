-- USER table
CREATE TABLE USER (
                      id BIGINT NOT NULL AUTO_INCREMENT,
                      name VARCHAR(100) NOT NULL COMMENT '사용자 이름',
                      created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      last_modified_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                      PRIMARY KEY (id)
);

-- WALLET table
CREATE TABLE WALLET (
                        id BIGINT NOT NULL AUTO_INCREMENT,
                        balance BIGINT DEFAULT 0 COMMENT '사용자 잔액',
                        user_id BIGINT NOT NULL COMMENT '사용자 ID',
                        created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        last_modified_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        PRIMARY KEY (id)
);

-- WALLET_HISTORY table
CREATE TABLE WALLET_HISTORY (
                                id BIGINT NOT NULL AUTO_INCREMENT,
                                amount BIGINT NOT NULL COMMENT '금액',
                                wallet_id BIGINT NOT NULL COMMENT '지갑 ID',
                                created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                last_modified_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                PRIMARY KEY (id)
);

-- COUPON table
CREATE TABLE COUPON (
                        id BIGINT NOT NULL AUTO_INCREMENT,
                        name VARCHAR(100) NOT NULL COMMENT '쿠폰 이름',
                        discount_amount BIGINT NOT NULL COMMENT '할인 금액',
                        expired_at DATETIME NOT NULL COMMENT '만료일',
                        created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        last_modified_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        PRIMARY KEY (id)
);

-- COUPON_INVENTORY table
CREATE TABLE COUPON_INVENTORY (
                                  id BIGINT NOT NULL AUTO_INCREMENT,
                                  stock INT NOT NULL COMMENT '재고',
                                  coupon_id BIGINT NOT NULL,
                                  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  last_modified_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                  PRIMARY KEY (id)
);

-- ISSUED_COUPON table
CREATE TABLE ISSUED_COUPON (
                               id BIGINT NOT NULL AUTO_INCREMENT,
                               used BOOLEAN DEFAULT FALSE COMMENT '사용 여부',
                               used_at DATETIME COMMENT '사용 일시',
                               user_id BIGINT NOT NULL,
                               coupon_id BIGINT NOT NULL,
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
                        order_price BIGINT NOT NULL COMMENT '주문 가격',
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
                               created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               last_modified_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                               PRIMARY KEY (id)
);

-- PAYMENT table
CREATE TABLE PAYMENT (
                         id BIGINT NOT NULL AUTO_INCREMENT,
                         order_id BIGINT NOT NULL,
                         issued_coupon_id BIGINT,
                         amount BIGINT NOT NULL COMMENT '결제 금액',
                         discount_amount BIGINT NOT NULL COMMENT '할인 금액',
                         paid_at DATETIME COMMENT '결제 일시',
                         created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         last_modified_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         PRIMARY KEY (id)
);
