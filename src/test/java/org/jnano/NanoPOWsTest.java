package org.jnano;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class NanoPOWsTest {


    @Test
    public void shouldPerformPOW() {
        // given
        String hash = "991CF190094C00F0B68E2E5F75F6BEE95A2E0BD93CEAA4A6734DB9F19B728948";

        // when
        String pow = NanoPOWs.perform(hash);

        // then
        assertTrue(NanoPOWs.isValid(hash, pow));
    }

    @Test
    public void shouldValidateHashPOW() {
        String hash = "8D21170F4F726B79FE42A7C80E5A2E1E933471F695EAEC1FF76E5AFD6A9CA09D";
        String pow = "8e8206e47e15b74b";

        assertTrue(NanoPOWs.isValid(hash, pow));
    }

}