package org.jnano;

import com.rfksystems.blake2b.security.Blake2bProvider;

import java.nio.ByteBuffer;
import java.security.Security;

import static org.jnano.DataUtils.toByteArray;

final class Keys {
    static {
        Security.addProvider(new Blake2bProvider());
    }

    private Keys() {
    }

    public static byte[] generatePublicKey(byte[] privateKey) {
        return ED25519.publickey(privateKey);
    }

    public static byte[] generatePrivateKey(String seed, int index) {
        return Hashes.digest256(toByteArray(seed), ByteBuffer.allocate(4).putInt(index).array());
    }
}
