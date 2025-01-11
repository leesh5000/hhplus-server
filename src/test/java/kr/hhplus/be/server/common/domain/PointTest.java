package kr.hhplus.be.server.common.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("도메인 객체 단위 테스트 : 포인트")
class PointTest {

    @DisplayName("Long 값을 입력받아 객체 생성 가능해야 한다.")
    @Test
    void create() {
        Point point = new Point(1L);
        assertEquals(1L, point.amount());
    }

    @DisplayName("Integer 값을 입력받아 객체 생성 가능해야 한다.")
    @Test
    void createFromInteger() {
        Point point = new Point(1);
        assertEquals(1L, point.amount());
    }

    @DisplayName("금액을 더하면, 두 금액의 합을 반환한다.")
    @Test
    void add() {
        Point point = new Point(1L);
        Point result = point.add(new Point(2L));
        assertEquals(3L, result.amount());
    }

    @DisplayName("금액을 빼면, 두 금액의 차를 반환한다.")
    @Test
    void subtract() {
        Point point = new Point(3L);
        Point result = point.subtract(new Point(2L));
        assertEquals(1L, result.amount());
    }

    @DisplayName("금액을 곱하면, 금액을 곱한 결과를 반환한다.")
    @Test
    void multiply() {
        Point point = new Point(2L);
        Point result = point.multiply(3);
        assertEquals(6L, result.amount());
    }

    @DisplayName("금액이 음수일 경우, `IllegalArgumentException`이 발생한다.")
    @Test
    void illegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> new Point(-1L));
    }

}