package com.rotilho.jnano.commons;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NanoPOWsTest {
    private static final String HASH = "D1E6C3C6B7DF4485B9324AB4DE023B71B5E0CA9AC20616A6F6E80D15AD4CFAC6";

    @Test
    public void shouldPerformPOW() {
        String pow = NanoPOWs.perform(HASH);

        assertTrue(NanoPOWs.isValid(HASH, pow));
    }

    @Test
    public void shouldValidateHashPOW() {
        String pow = "8e8206e47e15b74b";

        assertTrue(NanoPOWs.isValid(HASH, pow));
    }

    @Test
    public void shouldNotValidateHashPow() {
        String pow = "be46b2e52b34f535";

        assertFalse(NanoPOWs.isValid(HASH, pow));
    }
}