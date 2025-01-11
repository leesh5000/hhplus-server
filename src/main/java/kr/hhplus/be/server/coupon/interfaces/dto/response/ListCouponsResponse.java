package kr.hhplus.be.server.coupon.interfaces.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.hhplus.be.server.coupon.domain.service.dto.response.UserCouponsDetail;

import java.time.LocalDateTime;

public record ListCouponsResponse(
        String couponId,
        String couponName,
        Long discountAmount,
        Boolean used,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
        LocalDateTime usedAt,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
        LocalDateTime expiredAt,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
        LocalDateTime receivedAt
) {
        public static ListCouponsResponse from(UserCouponsDetail result) {
                return new ListCouponsResponse(
                        result.couponId().toString(),
                        result.couponName(),
                        result.discountAmount(),
                        result.used(),
                        result.usedAt(),
                        result.expiredAt(),
                        result.receivedAt()
                );
        }
}
