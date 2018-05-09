package org.jnano;

import javax.annotation.Nonnull;

import static org.jnano.Preconditions.checkHash;
import static org.jnano.Preconditions.checkKey;

public final class NanoSignatures {
    public static final String SIGNATURE_REGEX = "([0-9A-Z]){128}";

    private NanoSignatures() {
    }

    /**
     * Sign the block hash
     */
    @Nonnull
    public static String sign(@Nonnull byte[] privateKey, @Nonnull String hash) {
        checkKey(privateKey);
        checkHash(hash);
        byte[] signature = ED25519.sign(DataUtils.toByteArray(hash), privateKey);
        return DataUtils.toHex(signature);
    }

    public static boolean isValid(@Nonnull String address, @Nonnull String hash, @Nonnull String signature) {
        byte[] publicKey = NanoAccounts.toPublicKey(address);
        return isValid(publicKey, hash, signature);
    }

    public static boolean isValid(@Nonnull byte[] publicKey, @Nonnull String hash, @Nonnull String signature) {
        checkHash(hash);
        return signature.matches(SIGNATURE_REGEX) && ED25519.verify(DataUtils.toByteArray(signature), DataUtils.toByteArray(hash), publicKey);
    }
}
