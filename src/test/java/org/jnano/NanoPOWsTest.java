package org.jnano;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NanoPOWsTest {
    private static final String HASH = "8D21170F4F726B79FE42A7C80E5A2E1E933471F695EAEC1FF76E5AFD6A9CA09D";

    @Ignore
    @Test
    public void shouldPerformPOW() {
        String pow = NanoPOWs.perform(HASH);

        assertTrue(NanoPOWs.isValid(HASH, pow));
    }

    @Ignore
    @Test
    public void shouldValidateHashPOW() {
        String pow = "8e8206e47e15b74b";

        assertTrue(NanoPOWs.isValid(HASH, pow));
    }

    @Ignore
    @Test
    public void shouldNotValidateHashPow() {
        String pow = "be46b2e52b34f535";

        assertFalse(NanoPOWs.isValid(HASH, pow));
    }
}