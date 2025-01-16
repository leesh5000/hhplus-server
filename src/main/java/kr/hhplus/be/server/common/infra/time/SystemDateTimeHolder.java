package kr.hhplus.be.server.common.infra.time;

import kr.hhplus.be.server.common.domain.service.DateTimeHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SystemDateTimeHolder implements DateTimeHolder {
    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}
