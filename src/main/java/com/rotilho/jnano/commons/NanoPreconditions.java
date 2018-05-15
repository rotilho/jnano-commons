package com.rotilho.jnano.commons;

import java.util.function.Supplier;

public final class NanoPreconditions {
    private NanoPreconditions() {
    }

    public static void checkArgument(boolean expression, Supplier<String> supplier) {
        if (!expression) {
            throw new IllegalArgumentException(supplier.get());
        }
    }

    public static void checkHash(byte[] hash) {
        checkArgument(hash.length == 32, () -> "Invalid hash length");
    }

    public static void checkSignature(byte[] signature) {
        checkArgument(signature.length == 64, () -> "Invalid signature length");
    }

    public static void checkKey(byte[] key) {
        checkArgument(key.length == 32, () -> "Invalid key length");
    }
}
