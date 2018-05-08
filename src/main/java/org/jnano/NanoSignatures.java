package org.jnano;

import javax.annotation.Nonnull;

public class NanoSignatures {
    private NanoSignatures() {
    }

    /**
     * Sign the block hash
     */
    @Nonnull
    public static String sign(@Nonnull byte[] privateKey, @Nonnull String hash) {
        byte[] signature = ED25519.sign(DataUtils.toByteArray(hash), privateKey);
        return DataUtils.toHex(signature);
    }

    public static boolean isValid(@Nonnull String address, @Nonnull String hash, @Nonnull String signature) {
        byte[] publicKey = NanoAccounts.toPublicKey(address);
        return isValid(publicKey, hash, signature);
    }

    public static boolean isValid(@Nonnull byte[] publicKey, @Nonnull String hash, @Nonnull String signature) {
        try {
            return ED25519.verify(DataUtils.toByteArray(signature), DataUtils.toByteArray(hash), publicKey);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
