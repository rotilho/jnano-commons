package com.rotilho.jnano.commons;

import java.nio.ByteBuffer;

import javax.annotation.Nonnull;

public final class NanoKeys {
    private NanoKeys() {
    }

    public static byte[] createPrivateKey(@Nonnull byte[] seed, int index) {
        Preconditions.checkArgument(NanoSeeds.isValid(seed), () -> "Invalid seed " + seed);
        Preconditions.checkArgument(index >= 0, () -> "Invalid index " + index);
        return Hashes.digest256(seed, ByteBuffer.allocate(4).putInt(index).array());
    }

    public static byte[] createPublicKey(@Nonnull byte[] privateKey) {
        return ED25519.createPublicKey(privateKey);
    }
}
