package com.rotilho.jnano.commons;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.annotation.Nonnull;

public final class NanoSeeds {

    private NanoSeeds() {
    }

    /**
     * Generate seed using SecureRandom
     *
     * @return random 32 bytes seed
     * @throws NanoSeeds.ActionNotSupportedException Strong SecureRandom is not available
     * @see SecureRandom#getInstanceStrong()
     */
    @Nonnull
    public static byte[] generateSeed() {
        try {
            SecureRandom secureRandom = SecureRandom.getInstanceStrong();
            byte[] seed = new byte[32];
            secureRandom.nextBytes(seed);
            return seed;
        } catch (NoSuchAlgorithmException e) {
            throw new NanoSeeds.ActionNotSupportedException("Seed generation not supported", e);
        }
    }

    public static boolean isValid(@Nonnull byte[] seed) {
        return seed.length == 32;
    }

    public static class ActionNotSupportedException extends RuntimeException {
        private ActionNotSupportedException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
