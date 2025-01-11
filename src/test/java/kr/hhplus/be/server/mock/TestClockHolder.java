package kr.hhplus.be.server.mock;

import kr.hhplus.be.server.common.domain.service.ClockHolder;

import java.time.LocalDateTime;

public class TestClockHolder implements ClockHolder {
    @Override
    public LocalDateTime now() {
        return null;
    }
}
