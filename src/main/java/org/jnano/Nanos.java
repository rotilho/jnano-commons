package org.jnano;

import javax.annotation.Nonnull;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import static org.jnano.Hexes.*;
import static org.jnano.Preconditions.checkArgument;

public final class Nanos {
    public static final String SEED_REGEX = "^[A-Z0-9]{64}$";
    public static final String ADDRESS_REGEX = "^(xrb_)[13456789abcdefghijkmnopqrstuwxyz]{60}$";

    private Nanos() {
    }

    /**
     * Generate seed using SecureRandom
     *
     * @return random 32 bytes seed
     * @throws ActionNotSupportedException Strong SecureRandom is not available
     * @see SecureRandom#getInstanceStrong()
     */
    @Nonnull
    public static String generateSeed() {
        try {
            SecureRandom sr = SecureRandom.getInstanceStrong();
            byte[] seed = new byte[32];
            sr.nextBytes(seed);
            return toHex(seed);
        } catch (NoSuchAlgorithmException e) {
            throw new ActionNotSupportedException("Seed generation not supported", e);
        }
    }

    /**
     * Deterministically create address from a seed in a given index
     *
     * @param seed
     * @param index
     * @return Nano address (xrb_1111111111111111111111111111111111111111111111111111hifc8npp)
     */
    @Nonnull
    public static String createAddress(@Nonnull String seed, int index) {
        checkArgument(seed.matches(SEED_REGEX), () -> "Invalid seed " + seed);
        checkArgument(index >= 0, () -> "Invalid index " + index);

        byte[] privateKey = Hashes.digest256(toByteArray(seed), ByteBuffer.allocate(4).putInt(index).array()); //digest 36 bytes into 32
        byte[] publicKey = PublicKeys.generate(privateKey);
        return toAddress(publicKey);
    }

    /**
     * Extract public key from a Address
     *
     * @param address
     * @return public key
     */
    @Nonnull
    public static byte[] toPublicKey(@Nonnull String address) {
        checkArgument(address.matches(ADDRESS_REGEX), () -> "Invalid address " + address);

        String pub = address.substring(4, address.length() - 8);
        String checksum = address.substring(address.length() - 8);

        String pubbinary = AddressEncodes.decode(pub).substring(4);
        String checkbinary = AddressEncodes.decode(checksum);
        String hat = StringUtils.leftPad(Hexes.toHex(checkbinary), 10);
        String fallaciousalbatross = StringUtils.leftPad(Hexes.toHex(pubbinary), 64);

        byte[] publicKey = toByteArray(fallaciousalbatross);
        byte[] checkHex = swapEndian(toByteArray(hat));
        byte[] digest = Hashes.digest(5, publicKey);

        checkArgument(Arrays.equals(digest, checkHex), () -> "Invalid checksum " + checksum);

        return publicKey;
    }

    /**
     * Create address to a given public key
     *
     * @param publicKey
     * @return address
     */
    @Nonnull
    public static String toAddress(@Nonnull byte[] publicKey) {
        checkArgument(publicKey.length == 32, () -> "Invalid public key" + Arrays.toString(publicKey));

        String keyBinary = StringUtils.leftPad(toBinary(toHex(publicKey)), 260); //we get the address by picking
        //five bit (not byte!) chunks of the public key (in binary)

        byte[] digest = swapEndian(Hashes.digest(5, publicKey)); //the original wallet flips it
        String binary = StringUtils.leftPad(toBinary(toHex(digest)), digest.length * 8); //we get the checksum by, similarly

        String checksum = AddressEncodes.encode(binary);
        String account = AddressEncodes.encode(keyBinary);

        //return the address prefixed with xrb_ and suffixed with
        return "xrb_" + account + checksum;
    }

    public static String hash(byte[]... byteArrays) {
        return Hexes.toHex(Hashes.digest256(byteArrays));
    }

    private static byte[] swapEndian(byte[] b) {
        byte[] bb = new byte[b.length];
        for (int i = b.length; i > 0; i--) {
            bb[b.length - i] = b[i - 1];
        }
        return bb;
    }

    public static class ActionNotSupportedException extends RuntimeException {
        private ActionNotSupportedException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
