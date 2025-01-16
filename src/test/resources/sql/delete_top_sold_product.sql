-- 1) 먼저 ORDER_PRODUCT 테이블 데이터 삭제
DELETE FROM ORDER_PRODUCT
WHERE id IN (101, 102, 103, 104, 105, 106, 107, 108, 109, 110);

-- 2) 다음으로 ORDERS 테이블 데이터 삭제
DELETE FROM ORDERS
WHERE id IN (101, 102, 103, 104, 105);

-- 3) PRODUCT_INVENTORY 테이블 데이터 삭제
DELETE FROM PRODUCT_INVENTORY
WHERE id IN (
             101, 102, 103, 104, 105,
             106, 107, 108, 109, 110,
             111, 112, 113, 114, 115,
             116, 117, 118, 119, 120
    );

-- 4) 마지막으로 PRODUCT 테이블 데이터 삭제
DELETE FROM PRODUCT
WHERE id IN (
             101, 102, 103, 104, 105,
             106, 107, 108, 109, 110,
             111, 112, 113, 114, 115,
             116, 117, 118, 119, 120
    );
