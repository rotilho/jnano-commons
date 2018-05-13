package com.rotilho.jnano.commons;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class NanoAccountsTest {
    private static final String SEED = "1234567890123456789012345678901234567890123456789012345678901234";
    private static final String ACCOUNT = "nano_3iwi45me3cgo9aza9wx5f7rder37hw11xtc1ek8psqxw5oxb8cujjad6qp9y";
    private static final String OLD_ACCOUNT = "xrb_3iwi45me3cgo9aza9wx5f7rder37hw11xtc1ek8psqxw5oxb8cujjad6qp9y";
    private static final List<String> ACCOUNTS = Arrays.asList(ACCOUNT, OLD_ACCOUNT);

    @Test
    public void shouldCreateAccount() {
        // given
        byte[] privateKey = NanoKeys.createPrivateKey(SEED, 0);
        byte[] publicKey = NanoKeys.createPublicKey(privateKey);

        // when
        String account = NanoAccounts.createAccount(publicKey);

        // then
        assertEquals(ACCOUNT, account);
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