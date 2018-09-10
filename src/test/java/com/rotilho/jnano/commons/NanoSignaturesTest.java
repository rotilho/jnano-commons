package com.rotilho.jnano.commons;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NanoSignaturesTest {
    private static final String ACCOUNT = "xrb_3i1aq1cchnmbn9x5rsbap8b15akfh7wj7pwskuzi7ahz8oq6cobd99d4r3b7";
    private static final String HASH = "AEC75F807DCE45AFA787DE7B395BE498A885525569DD614162E0C80FD4F27EE9";
    private static final String SIGNATURE = "1123C926EF53B0FFA3585D5F6FA17D05B2AAD486D28CBEED88837B83265F264CBAF3FEA78AF80AAB4C59740546B220ADBE207F6B800FFE864E0934E9C1078401";

    @Test
    public void shouldExtractPublicKeyFromAccountAndSign() {
        // given
        byte[] privateKey = NanoHelper.toByteArray("9F0E444C69F77A49BD0BE89DB92C38FE713E0963165CCA12FAF5712D7657120F");

        // when
        String signed = NanoSignatures.sign(privateKey, HASH);

        // then
        assertEquals(SIGNATURE, signed);
    }

    @Test
    public void shouldValidateSignature() {
        assertTrue(NanoSignatures.isValid(ACCOUNT, HASH, SIGNATURE));
    }

    @Test
    public void shouldNotValidateWhenSignatureIsInvalid() {
        assertFalse(NanoSignatures.isValid(ACCOUNT, HASH, SIGNATURE.substring(2)));
    }

    @Test
    public void shouldNotValidateWhenSignatureDoesNotMatch() {
        String wrongSignature = "9F0C933C8ADE004D808EA1985FA746A7E95BA2A38F867640F53EC8F180BDFE9E2C1268DEAD7C2664F356E37ABA362BC58E46DBA03E523A7B5A19E4B6EB12BB02";

        assertFalse(NanoSignatures.isValid(ACCOUNT, HASH, wrongSignature));
    }
}