package com.rotilho.jnano.commons;

import java.nio.ByteBuffer;

import javax.annotation.Nonnull;

import static com.rotilho.jnano.commons.DataUtils.toByteArray;

public final class NanoKeys {
    private NanoKeys() {
    }

    public static byte[] createPrivateKey(@Nonnull String seed, int index) {
        NanoPreconditions.checkArgument(NanoSeeds.isValid(seed), () -> "Invalid seed " + seed);
        NanoPreconditions.checkArgument(index >= 0, () -> "Invalid index " + index);
        return Hashes.digest256(toByteArray(seed), ByteBuffer.allocate(4).putInt(index).array());
    }

    public static byte[] createPublicKey(@Nonnull byte[] privateKey) {
        return ED25519.createPublicKey(privateKey);
    }
}
