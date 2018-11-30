package com.rotilho.jnano.commons;

import com.rotilho.jnano.commons.exception.ActionNotSupportedException;

import java.security.SecureRandom;

import lombok.NonNull;

public final class NanoSeeds {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private NanoSeeds() {
    }

    /**
     * Generate seed using SecureRandom
     *
     * @return random 32 bytes seed
     * @throws ActionNotSupportedException Strong SecureRandom is not available
     * @see SecureRandom#getInstanceStrong()
     */
    @NonNull
    public static byte[] generateSeed() {
        byte[] seed = new byte[32];
        SECURE_RANDOM.nextBytes(seed);
        return seed;
    }

    public static boolean isValid(@NonNull byte[] seed) {
        return seed.length == 32;
    }
}
