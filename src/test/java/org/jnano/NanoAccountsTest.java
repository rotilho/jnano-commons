package org.jnano;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NanoAccountsTest {
    private static final String SEED = "1234567890123456789012345678901234567890123456789012345678901234";
    private static final String ADDRESS = "nano_3iwi45me3cgo9aza9wx5f7rder37hw11xtc1ek8psqxw5oxb8cujjad6qp9y";
    private static final String OLD_ADDRESS = "xrb_3iwi45me3cgo9aza9wx5f7rder37hw11xtc1ek8psqxw5oxb8cujjad6qp9y";
    private static final List<String> ADDRESSES = Arrays.asList(ADDRESS, OLD_ADDRESS);

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
        for (String address : ADDRESSES) {
            // given
            byte[] publicKey = NanoAccounts.toPublicKey(address);

            // then
            String extractedAddress = NanoAccounts.toAddress(publicKey);

            // when
            assertEquals(ADDRESS, extractedAddress);
        }
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
        ADDRESSES.forEach(address -> assertTrue(NanoAccounts.isValid(ADDRESS)));
    }

    @Test
    public void shouldValidateOldAddress() {
        assertTrue(NanoAccounts.isValid(OLD_ADDRESS));
    }

    @Test
    public void shouldNotValidateAddress() {
        assertFalse(NanoAccounts.isValid(ADDRESS.replace("nano_", "inv_")));
    }
}