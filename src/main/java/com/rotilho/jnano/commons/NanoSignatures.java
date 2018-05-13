package com.rotilho.jnano.commons;

import javax.annotation.Nonnull;

public final class NanoSignatures {
    private static final String SIGNATURE_REGEX = "([0-9A-Z]){128}";

    private NanoSignatures() {
    }

    /**
     * Sign the block hash
     */
    @Nonnull
    public static String sign(@Nonnull byte[] privateKey, @Nonnull String hash) {
        Preconditions.checkKey(privateKey);
        byte[] signature = ED25519.sign(DataUtils.toByteArray(hash), privateKey);
        return DataUtils.toHex(signature);
    }

    public static boolean isValid(@Nonnull String account, @Nonnull String hash, @Nonnull String signature) {
        byte[] publicKey = NanoAccounts.toPublicKey(account);
        return isValid(publicKey, hash, signature);
    }

    public static boolean isValid(@Nonnull byte[] publicKey, @Nonnull String hash, @Nonnull String signature) {
        checkHash(hash);
        return signature.matches(SIGNATURE_REGEX) && ED25519.verify(DataUtils.toByteArray(signature), DataUtils.toByteArray(hash), publicKey);
    }

    private static void checkHash(String hash) {
        Preconditions.checkArgument(NanoBlocks.isValid(hash), () -> "Invalid hash " + hash);
    }
}
