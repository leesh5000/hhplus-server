package kr.hhplus.be.server.common.domain.dto.response;

/**
 * 정책 : PageDetails 는 도메인 계층의 DTO 이지만, 페이지 정보 필드가 변경이 잦지 않으므로 예외적으로 Controller 계층에서도 사용하는 것을 허용한다.
 * @param page
 * @param size
 * @param totalElements
 * @param totalPages
 * @param content
 * @param <T>
 */
public record PageDetails<T>(
    int page,
    int size,
    Long totalElements,
    int totalPages,
    T content
) {
}
