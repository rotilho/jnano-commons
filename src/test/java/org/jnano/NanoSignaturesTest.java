package org.jnano;

import org.junit.Test;

import static org.junit.Assert.*;

public class NanoSignaturesTest {
    private static final String ADDRESS = "xrb_3i1aq1cchnmbn9x5rsbap8b15akfh7wj7pwskuzi7ahz8oq6cobd99d4r3b7";
    private static final String HASH = "AEC75F807DCE45AFA787DE7B395BE498A885525569DD614162E0C80FD4F27EE9";
    private static final String SIGNATURE = "1123C926EF53B0FFA3585D5F6FA17D05B2AAD486D28CBEED88837B83265F264CBAF3FEA78AF80AAB4C59740546B220ADBE207F6B800FFE864E0934E9C1078401";

    @Test
    public void shouldSign() {
        byte[] privateKey = DataUtils.toByteArray("9F0E444C69F77A49BD0BE89DB92C38FE713E0963165CCA12FAF5712D7657120F");
        byte[] publicKey = DataUtils.toByteArray("C008B814A7D269A1FA3C6528B19201A24D797912DB9996FF02A1FF356E45552B");

        // when
        String signed = NanoSignatures.sign(privateKey, publicKey, HASH);

        // then
        assertEquals(SIGNATURE, signed);
    }

    @Test
    public void shouldCreatePrivateAndPublicKeyFromSeedAndSign() {
        // given
        String seed = "0000000000000000000000000000000000000000000000000000000000000000";

        // when
        String signed = NanoSignatures.sign(seed, 0, HASH);

        // then
        assertEquals(SIGNATURE, signed);
    }

    @Test
    public void shouldExtractPublicKeyFromAddressAndSign() {
        // given
        byte[] privateKey = DataUtils.toByteArray("9F0E444C69F77A49BD0BE89DB92C38FE713E0963165CCA12FAF5712D7657120F");

        // when
        String signed = NanoSignatures.sign(privateKey, ADDRESS, HASH);

        // then
        assertEquals(SIGNATURE, signed);
    }

    @Test
    public void shouldValidateSignature() {
        assertTrue(NanoSignatures.isValid(ADDRESS, HASH, SIGNATURE));
    }

    @Test
    public void shouldNotValidateSignature() {
        assertFalse(NanoSignatures.isValid(ADDRESS, HASH, SIGNATURE.replaceAll("1", "2")));
    }
}