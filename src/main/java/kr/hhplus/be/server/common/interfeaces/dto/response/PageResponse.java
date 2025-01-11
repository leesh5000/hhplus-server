package kr.hhplus.be.server.common.interfeaces.dto.response;

public record PageResponse<T>(
    int page,
    int size,
    int totalElements,
    int totalPages,
    T content
) {
}
