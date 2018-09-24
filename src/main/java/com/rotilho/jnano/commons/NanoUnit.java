package com.rotilho.jnano.commons;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NanoUnit {
    GIGA("Gnano", new BigDecimal("1000000000000000000000000000000000")),
    NANO("Nano", new BigDecimal("1000000000000000000000000000000")),
    KILO("knano", new BigDecimal("1000000000000000000000000000")),
    SMALL_NANO("nano", new BigDecimal("1000000000000000000000000")),
    MILLI("mnano", new BigDecimal("1000000000000000000000")),
    MICRO("unano", new BigDecimal("1000000000000000000")),
    RAW("raw", new BigDecimal("1"));

    private final String prefix;
    private final BigDecimal multiplier;
}
