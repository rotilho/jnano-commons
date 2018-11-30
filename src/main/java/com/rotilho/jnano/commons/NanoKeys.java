package com.rotilho.jnano.commons;

import java.nio.ByteBuffer;

import lombok.NonNull;

public final class NanoKeys {
    private NanoKeys() {
    }

    public static byte[] createPrivateKey(@NonNull byte[] seed, int index) {
        Preconditions.checkArgument(NanoSeeds.isValid(seed), "Invalid seed " + seed);
        Preconditions.checkArgument(index >= 0, "Invalid index " + index);
        return Hashes.digest256(seed, ByteBuffer.allocate(4).putInt(index).array());
    }

    public static byte[] createPublicKey(@NonNull byte[] privateKey) {
        return ED25519.createPublicKey(privateKey);
    }
}
