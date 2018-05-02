package org.jnano;

import com.rfksystems.blake2b.Blake2b;

import java.util.stream.Stream;

final class Hashes {
    private static final int DIGEST_256 = 256 / 8;

    private Hashes() {
    }

    public static byte[] digest256(byte[]... bytes) {
        return digest(DIGEST_256, bytes);
    }

    public static byte[] digest(int digestSize, byte[]... byteArrays) {
        Blake2b blake2b = new Blake2b(null, digestSize, null, null);

        Stream.of(byteArrays).forEach(byteArray -> blake2b.update(byteArray, 0, byteArray.length));

        byte[] output = new byte[digestSize];
        blake2b.digest(output, 0);
        return output;
    }
}
