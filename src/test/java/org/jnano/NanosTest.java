package org.jnano;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.security.Security;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;


public class NanosTest {
    private static final String SEED = "1234567890123456789012345678901234567890123456789012345678901234";
    private static final String ADDRESS = "xrb_3iwi45me3cgo9aza9wx5f7rder37hw11xtc1ek8psqxw5oxb8cujjad6qp9y";

    private String strongAlgorithms;

    @Before
    public void setUp() {
        strongAlgorithms = Security.getProperty("securerandom.strongAlgorithms");
    }

    @After
    public void tearDown() {
        Security.setProperty("securerandom.strongAlgorithms", strongAlgorithms);
    }


    @Test
    public void shouldGenerateSeedWhenAlgorithmIsAvailable() {
        assertTrue(Nanos.generateSeed().matches(Nanos.SEED_REGEX));
    }

    @Test(expected = Nanos.ActionNotSupportedException.class)
    public void shouldNotGenerateSeedWhenAlogirithmIsNotAvailable() {
        Security.setProperty("securerandom.strongAlgorithms", "");

        Nanos.generateSeed();
    }

    @Test
    public void shouldCreateAddress() {
        assertEquals(ADDRESS, Nanos.createAddress(SEED, 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCreateAddressWhenSeedIsInvalid() {
        Nanos.createAddress("", 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCreateAddressWhenIndexIsBelowZero() {
        Nanos.createAddress(SEED, -1);
    }


    @Test
    public void shouldConvertPublicKeyToAddress() {
        // given
        byte[] publicKey = Nanos.toPublicKey(ADDRESS);

        // then
        String address = Nanos.toAddress(publicKey);

        // when
        assertEquals(ADDRESS, address);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotConvertPublicKeyToAddressWhenPublicKeyHaveInvalidLength() {
        Nanos.toAddress(new byte[1]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotConvertAddressToPublicKeyWhenAddressIsInvalid() {
        Nanos.toPublicKey(ADDRESS.substring(0, ADDRESS.length() - 1));
    }

}