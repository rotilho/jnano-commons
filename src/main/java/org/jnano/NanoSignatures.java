package org.jnano;

import javax.annotation.Nonnull;

public class NanoSignatures {
    private NanoSignatures() {
    }

    /**
     * Derive private key from seed, create public key and sign the block hash
     * <p>
     * IMPORTANT! This method is extremely slow and should be avoided
     *
     * @param seed
     * @param index
     * @param hash
     * @return
     */
    @Nonnull
    public static String sign(@Nonnull String seed, int index, @Nonnull String hash) {
        byte[] privateKey = Keys.generatePrivateKey(seed, index);
        byte[] publicKey = Keys.generatePublicKey(privateKey);
        return sign(privateKey, publicKey, hash);
    }

    /**
     * Extract public key from address and sign the block hash
     *
     * @param privateKey
     * @param address
     * @param hash
     * @return
     */
    @Nonnull
    public static String sign(@Nonnull byte[] privateKey, @Nonnull String address, @Nonnull String hash) {
        byte[] publicKey = NanoAccounts.toPublicKey(address);
        return sign(privateKey, publicKey, hash);
    }

    /**
     * Sign the block hash
     *
     * @param privateKey
     * @param publicKey
     * @param hash
     * @return
     */
    @Nonnull
    public static String sign(@Nonnull byte[] privateKey, @Nonnull byte[] publicKey, @Nonnull String hash) {
        byte[] signature = ED25519.signature(DataUtils.toByteArray(hash), privateKey, publicKey);
        return DataUtils.toHex(signature);
    }

}
