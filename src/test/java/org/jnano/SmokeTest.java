package org.jnano;

import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;

import static org.junit.Assert.assertTrue;

public class SmokeTest {
    private static final String PREVIOUS = "597395E83BD04DF8EF30AF04234EAAFE0606A883CF4AEAD2DB8196AAF5C4444F";

    @Test
    public void test() {
        // create seed
        String seed = NanoSeeds.generateSeed();
        assertTrue(NanoSeeds.isValid(seed));

        // create private key
        byte[] privateKey = NanoKeys.createPrivateKey(seed, 0);
        byte[] publicKey = NanoKeys.createPublicKey(privateKey);

        // create account
        String account = NanoAccounts.createAccount(publicKey);
        assertTrue(NanoAccounts.isValid(account));

        // convert account to publicKey
        assertTrue(Arrays.equals(NanoAccounts.toPublicKey(account), publicKey));

        // create block hash
        String hash = NanoBlocks.hashStateBlock(
                account, // account
                NanoBlocks.MAIN_NET_GENESIS, //previous block
                account, // representative
                BigInteger.ONE, // balance
                NanoAccounts.MAIN_NET_GENESIS_ACCOUNT // link: target address in this case
        );
        assertTrue(NanoBlocks.isValid(hash));

        // sign a block hash
        String signature = NanoSignatures.sign(privateKey, hash);
        assertTrue(NanoSignatures.isValid(account, hash, signature));
    }

}
