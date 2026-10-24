package com.zerooversell.reservation.utils;

import java.time.Instant;

public final class ClockProvider {
    private ClockProvider() {}
    public static Instant now() { return Instant.now(); }
}
