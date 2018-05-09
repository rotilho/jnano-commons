package org.jnano;

import javax.annotation.Nonnull;
import java.nio.ByteBuffer;

import static org.jnano.DataUtils.toByteArray;
import static org.jnano.Preconditions.checkArgument;

public final class NanoKeys {
    private NanoKeys() {
    }

    public static byte[] createPrivateKey(@Nonnull String seed, int index) {
        checkArgument(NanoSeeds.isValid(seed), () -> "Invalid seed " + seed);
        checkArgument(index >= 0, () -> "Invalid index " + index);
        return Hashes.digest256(toByteArray(seed), ByteBuffer.allocate(4).putInt(index).array());
    }

    public static byte[] createPublicKey(@Nonnull byte[] privateKey) {
        return ED25519.createPublicKey(privateKey);
    }
}
