package com.rotilho.jnano.commons;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertTrue;

public class SmokeTest {

    @Test
    public void test() {
        // create seed
        byte[] seed = NanoSeeds.generateSeed();
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
                NanoAmount.ofRaw("1"), // balance
                NanoAccounts.MAIN_NET_GENESIS_ACCOUNT // link: target address in this case
        );
        assertTrue(NanoBlocks.isValid(hash));

        // sign a block hash
        String signature = NanoSignatures.sign(privateKey, hash);
        assertTrue(NanoSignatures.isValid(account, hash, signature));
    }

}
