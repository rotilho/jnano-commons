package com.rotilho.jnano.commons;

final class Preconditions {
    private Preconditions() {
    }

    static void checkArgument(boolean expression, String msg) {
        if (!expression) {
            throw new IllegalArgumentException(msg);
        }
    }

    static void checkHash(byte[] hash) {
        checkArgument(hash.length == 32, "Invalid hash length");
    }

    static void checkSignature(byte[] signature) {
        checkArgument(signature.length == 64, "Invalid signature length");
    }

    static void checkKey(byte[] key) {
        checkArgument(key.length == 32, "Invalid key length");
    }


    static void checkSeed(byte[] seed) {
        checkArgument(seed.length == 32, "Invalid seed length");
    }
}
