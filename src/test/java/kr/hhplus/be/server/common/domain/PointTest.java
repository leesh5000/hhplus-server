package kr.hhplus.be.server.common.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("도메인 객체 단위 테스트 : 포인트")
class PointTest {

    @DisplayName("값이 같으면, Integer 객체와도 equals 비교가 가능해야 한다.")
    @Test
    void equals() {
        Point point = new Point(1L);
        Assertions.assertThat(point).isEqualTo(1L);
    }

    @DisplayName("값이 같으면, Long 객체와도 equals 비교가 가능해야 한다.")
    @Test
    void equalsLong() {
        Point point = new Point(1L);
        Assertions.assertThat(point).isEqualTo(1L);
    }

    @DisplayName("Long 값을 입력받아 객체 생성 가능해야 한다.")
    @Test
    void create() {
        Point point = new Point(1L);
        assertEquals(1L, point.toLong());
    }

    @DisplayName("Integer 값을 입력받아 객체 생성 가능해야 한다.")
    @Test
    void createFromInteger() {
        Point point = new Point(1);
        assertEquals(1L, point.toLong());
    }

    @DisplayName("금액을 더하면, 두 금액의 합을 반환한다.")
    @Test
    void add() {
        Point point = new Point(1L);
        Point result = point.add(new Point(2L));
        assertEquals(3L, result.toLong());
    }

    @DisplayName("금액을 빼면, 두 금액의 차를 반환한다.")
    @Test
    void subtract() {
        Point point = new Point(3L);
        Point result = point.subtract(new Point(2L));
        assertEquals(1L, result.toLong());
    }

    @DisplayName("금액을 곱하면, 금액을 곱한 결과를 반환한다.")
    @Test
    void multiply() {
        Point point = new Point(2L);
        Point result = point.multiply(3);
        assertEquals(6L, result.toLong());
    }

}