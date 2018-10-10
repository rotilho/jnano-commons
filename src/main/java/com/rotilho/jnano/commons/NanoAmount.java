package com.rotilho.jnano.commons;

import java.math.BigDecimal;
import java.math.RoundingMode;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@Getter(AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class NanoAmount {
    @NonNull
    private final BigDecimal raw;

    public static NanoAmount of(@NonNull BigDecimal amount, @NonNull NanoUnit unit) {
        BigDecimal raw = amount.multiply(unit.getMultiplier());
        if (raw.signum() < 0) {
            throw new IllegalArgumentException("Amount(" + amount + ") can't be negative");
        }
        if (raw.stripTrailingZeros().scale() > 0) {
            throw new IllegalArgumentException("Amount(" + amount + ") have raw decimals");
        }
        return new NanoAmount(raw);
    }

    public static NanoAmount of(@NonNull String amount, @NonNull NanoUnit unit) {
        return of(new BigDecimal(amount), unit);
    }

    public static NanoAmount ofGiga(@NonNull BigDecimal amount) {
        return NanoAmount.of(amount, NanoUnit.GIGA);
    }

    public static NanoAmount ofGiga(@NonNull String amount) {
        return NanoAmount.of(amount, NanoUnit.GIGA);
    }

    public static NanoAmount ofNano(@NonNull BigDecimal amount) {
        return NanoAmount.of(amount, NanoUnit.NANO);
    }

    public static NanoAmount ofNano(@NonNull String amount) {
        return NanoAmount.of(amount, NanoUnit.NANO);
    }

    public static NanoAmount ofKilo(@NonNull BigDecimal amount) {
        return NanoAmount.of(amount, NanoUnit.KILO);
    }

    public static NanoAmount ofKilo(@NonNull String amount) {
        return NanoAmount.of(amount, NanoUnit.KILO);
    }

    public static NanoAmount ofSmallNano(@NonNull BigDecimal amount) {
        return NanoAmount.of(amount, NanoUnit.SMALL_NANO);
    }

    public static NanoAmount ofSmallNano(@NonNull String amount) {
        return NanoAmount.of(amount, NanoUnit.SMALL_NANO);
    }

    public static NanoAmount ofMilli(@NonNull BigDecimal amount) {
        return NanoAmount.of(amount, NanoUnit.MILLI);
    }

    public static NanoAmount ofMilli(@NonNull String amount) {
        return NanoAmount.of(amount, NanoUnit.MILLI);
    }

    public static NanoAmount ofMicro(@NonNull BigDecimal amount) {
        return NanoAmount.of(amount, NanoUnit.MICRO);
    }

    public static NanoAmount ofMicro(@NonNull String amount) {
        return NanoAmount.of(amount, NanoUnit.MICRO);
    }

    public static NanoAmount ofRaw(@NonNull BigDecimal amount) {
        return NanoAmount.of(amount, NanoUnit.RAW);
    }

    public static NanoAmount ofRaw(@NonNull String amount) {
        return NanoAmount.of(amount, NanoUnit.RAW);
    }

    public static NanoAmount ofByteArray(@NonNull byte[] amount) {
        return NanoAmount.of(new BigDecimal(NanoHelper.toBigInteger(amount)), NanoUnit.RAW);
    }

    public NanoAmount add(NanoAmount amount) {
        return NanoAmount.ofRaw(this.raw.add(amount.raw));
    }

    public NanoAmount subtract(@NonNull NanoAmount amount) {
        return NanoAmount.ofRaw(this.raw.subtract(amount.raw));
    }

    public NanoAmount multiply(@NonNull NanoAmount amount) {
        return NanoAmount.ofRaw(this.raw.multiply(amount.raw));
    }

    public NanoAmount divide(@NonNull NanoAmount amount, @NonNull RoundingMode roundingMode) {
        return NanoAmount.ofRaw(this.raw.divide(amount.raw, roundingMode));
    }

    public BigDecimal to(@NonNull NanoUnit unit) {
        BigDecimal amount = raw.divide(unit.getMultiplier());
        if (amount.stripTrailingZeros().scale() > 0) {
            return amount.stripTrailingZeros();
        }
        return amount.setScale(0, BigDecimal.ROUND_DOWN);
    }

    public BigDecimal toGiga() {
        return to(NanoUnit.GIGA);
    }

    public BigDecimal toNano() {
        return to(NanoUnit.NANO);
    }

    public BigDecimal toKilo() {
        return to(NanoUnit.KILO);
    }

    public BigDecimal toSmallNano() {
        return to(NanoUnit.SMALL_NANO);
    }

    public BigDecimal toMilli() {
        return to(NanoUnit.MILLI);
    }

    public BigDecimal toMicro() {
        return to(NanoUnit.MICRO);
    }

    public BigDecimal toRaw() {
        return to(NanoUnit.RAW);
    }

    public byte[] toByteArray() {
        return NanoHelper.toByteArray(raw.toBigInteger());
    }

    public String toString(@NonNull NanoUnit unit) {
        return to(unit).toString();
    }

    public String toString() {
        return toString(NanoUnit.RAW);
    }
}
