package com.rotilho.jnano.commons;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

import lombok.RequiredArgsConstructor;
import lombok.Value;

import static org.junit.Assert.assertEquals;

public class NanoAmountTest {

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowAmountLowerThanRaw() {
        NanoAmount.ofRaw(new BigDecimal("1.5"));
    }

    @RunWith(Parameterized.class)
    @RequiredArgsConstructor
    public static class NanoAmountConversionTest {

        private final TestCase testCase;

        @Parameterized.Parameters
        public static Collection<TestCase> cases() {
            return Arrays.asList(
                    new TestCase(NanoAmount::ofGiga, NanoAmount::toGiga, "1000000000000000000000000000000000"),
                    new TestCase(NanoAmount::ofNano, NanoAmount::toNano, "1000000000000000000000000000000"),
                    new TestCase(NanoAmount::ofKilo, NanoAmount::toKilo, "1000000000000000000000000000"),
                    new TestCase(NanoAmount::ofSmallNano, NanoAmount::toSmallNano, "1000000000000000000000000"),
                    new TestCase(NanoAmount::ofMilli, NanoAmount::toMilli, "1000000000000000000000"),
                    new TestCase(NanoAmount::ofMicro, NanoAmount::toMicro, "1000000000000000000"),
                    new TestCase(NanoAmount::ofRaw, NanoAmount::toRaw, "1")
            );
        }

        @Test
        public void test() {
            // given
            BigDecimal amount = BigDecimal.valueOf(1);

            // when
            NanoAmount nanoAmount = testCase.ofFunction.apply(amount);

            // then
            assertEquals(amount, testCase.toFunction.apply(nanoAmount));
            assertEquals(testCase.expectedRaw, nanoAmount.toString());
        }

        @Value
        private static class TestCase {
            private Function<BigDecimal, NanoAmount> ofFunction;
            private Function<NanoAmount, BigDecimal> toFunction;
            private String expectedRaw;
        }
    }
}