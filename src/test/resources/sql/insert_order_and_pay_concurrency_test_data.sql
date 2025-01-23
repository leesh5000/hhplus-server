CREATE PROCEDURE InsertData()
BEGIN
    DECLARE i INT DEFAULT 1;

    WHILE i <= 50 DO
            -- USER 테이블에 데이터 삽입
            INSERT IGNORE INTO USER (id, name)
            VALUES (i, CONCAT('user', i));

            -- WALLET 테이블에 데이터 삽입
            INSERT IGNORE INTO WALLET (id, balance, user_id)
            VALUES (i, 50000, i);

            SET i = i + 1;
        END WHILE;
END;

-- 프로시저 호출
CALL InsertData();