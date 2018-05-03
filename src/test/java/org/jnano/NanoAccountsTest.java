package org.jnano;

import org.junit.Test;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;

public class NanoAccountsTest {
    private static final String SEED = "1234567890123456789012345678901234567890123456789012345678901234";
    private static final String ADDRESS = "xrb_3iwi45me3cgo9aza9wx5f7rder37hw11xtc1ek8psqxw5oxb8cujjad6qp9y";

    @Test
    public void shouldCreateAddress() {
        assertEquals(ADDRESS, NanoAccounts.createAddress(SEED, 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCreateAddressWhenSeedIsInvalid() {
        NanoAccounts.createAddress("", 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCreateAddressWhenIndexIsBelowZero() {
        NanoAccounts.createAddress(SEED, -1);
    }


    @Test
    public void shouldConvertPublicKeyToAddress() {
        // given
        byte[] publicKey = NanoAccounts.toPublicKey(ADDRESS);

        // then
        String address = NanoAccounts.toAddress(publicKey);

        // when
        assertEquals(ADDRESS, address);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotConvertPublicKeyToAddressWhenPublicKeyHaveInvalidLength() {
        NanoAccounts.toAddress(new byte[1]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotConvertAddressToPublicKeyWhenAddressIsInvalid() {
        NanoAccounts.toPublicKey(ADDRESS.substring(0, ADDRESS.length() - 1));
    }

    @Test
    public void shouldValidateAddress() {
        assertTrue(NanoAccounts.isValid(ADDRESS));
    }

    @Test
    public void shouldNotValidateAddress() {
        assertFalse(NanoAccounts.isValid(ADDRESS.replace("xrb_", "inv_")));
    }
}