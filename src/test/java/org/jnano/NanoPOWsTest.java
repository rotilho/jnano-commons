package org.jnano;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class NanoPOWsTest {
    private static final String HASH = "8D21170F4F726B79FE42A7C80E5A2E1E933471F695EAEC1FF76E5AFD6A9CA09D";

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
}