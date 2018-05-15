package com.rotilho.jnano.commons;

import java.util.function.Supplier;

final class Preconditions {
    private Preconditions() {
    }

    static void checkArgument(boolean expression, Supplier<String> supplier) {
        if (!expression) {
            throw new IllegalArgumentException(supplier.get());
        }
    }

    static void checkHash(byte[] hash) {
        checkArgument(hash.length == 32, () -> "Invalid hash length");
    }

    static void checkSignature(byte[] signature) {
        checkArgument(signature.length == 64, () -> "Invalid signature length");
    }

    static void checkKey(byte[] key) {
        checkArgument(key.length == 32, () -> "Invalid key length");
    }
}
