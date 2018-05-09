package org.jnano;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.annotation.Nonnull;

import static org.jnano.DataUtils.toHex;

public final class NanoSeeds {
    public static final String SEED_REGEX = "^[A-Z0-9]{64}$";

    public NanoSeeds(){
    }

    /**
     * Generate seed using SecureRandom
     *
     * @return random 32 bytes seed
     * @throws NanoSeeds.ActionNotSupportedException Strong SecureRandom is not available
     * @see SecureRandom#getInstanceStrong()
     */
    @Nonnull
    public static String generateSeed() {
        try {
            SecureRandom secureRandom = SecureRandom.getInstanceStrong();
            byte[] seed = new byte[32];
            secureRandom.nextBytes(seed);
            return toHex(seed);
        } catch (NoSuchAlgorithmException e) {
            throw new NanoSeeds.ActionNotSupportedException("Seed generation not supported", e);
        }
    }

    public static boolean isValid(@Nonnull String seed) {
        return seed.matches(SEED_REGEX);
    }

    public static class ActionNotSupportedException extends RuntimeException {
        private ActionNotSupportedException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
