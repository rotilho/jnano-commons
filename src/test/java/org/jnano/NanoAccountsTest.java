package org.jnano;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class NanoAccountsTest {
    private static final byte[] PRIVATE_KEY = NanoKeys.createPrivateKey("1234567890123456789012345678901234567890123456789012345678901234", 0);
    private static final String ACCOUNT = "nano_3iwi45me3cgo9aza9wx5f7rder37hw11xtc1ek8psqxw5oxb8cujjad6qp9y";
    private static final String OLD_ACCOUNT = "xrb_3iwi45me3cgo9aza9wx5f7rder37hw11xtc1ek8psqxw5oxb8cujjad6qp9y";
    private static final List<String> ACCOUNTS = Arrays.asList(ACCOUNT, OLD_ACCOUNT);

    @Test
    public void shouldCreateAccount() {
        assertEquals(ACCOUNT, NanoAccounts.createAccount(PRIVATE_KEY));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotConvertAccountToPublicKeyWhenAccountIsInvalid() {
        NanoAccounts.toPublicKey(ACCOUNT.substring(0, ACCOUNT.length() - 1));
    }

    @Test
    public void shouldValidateAccount() {
        for (String account : ACCOUNTS) {
            assertTrue(NanoAccounts.isValid(account));
        }
    }

    @Test
    public void shouldValidateOldAccount() {
        assertTrue(NanoAccounts.isValid(OLD_ACCOUNT));
    }

    @Test
    public void shouldNotValidateAccount() {
        assertFalse(NanoAccounts.isValid(ACCOUNT.replace("nano_", "inv_")));
    }
}