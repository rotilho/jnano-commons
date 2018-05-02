package org.jnano;

import javax.annotation.Nonnull;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashMap;

import static org.jnano.Hex.*;
import static org.jnano.Preconditions.checkArgument;

public final class Nanos {
    private static final char[] ACCOUNT_MAP = "13456789abcdefghijkmnopqrstuwxyz".toCharArray();

    private static final HashMap<String, Character> ACCOUNT_CHAR_TABLE = new HashMap<>();
    private static final HashMap<Character, String> ACCOUNT_BIN_TABLE = new HashMap<>();

    public static final String SEED_REGEX = "^[A-Z0-9]{64}$";
    public static final String ADDRESS_REGEX = "^(xrb_)[13456789abcdefghijkmnopqrstuwxyz]{60}$";

    static {
        //populate the ACCOUNT_CHAR_TABLE and ACCOUNT_BIN_TABLE
        for (int i = 0; i < ACCOUNT_MAP.length; i++) {
            String binary = Integer.toBinaryString(i);
            while (binary.length() < 5)
                binary = "0" + binary; //pad with 0
            ACCOUNT_CHAR_TABLE.put(binary, ACCOUNT_MAP[i]);
            ACCOUNT_BIN_TABLE.put(ACCOUNT_MAP[i], binary);
        }
    }

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

        byte[] privateKey = Hashes.digest256(
                toByteArray(seed), //add seed
                ByteBuffer.allocate(4).putInt(index).array() //and add index
        ); //digest 36 bytes into 32
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

        String pubbinary = "";
        for (int i = 0; i < pub.length(); i++) {
            pubbinary += ACCOUNT_BIN_TABLE.get(pub.charAt(i));
        }
        pubbinary = pubbinary.substring(4);

        String checkbinary = "";
        for (int i = 0; i < checksum.length(); i++) {
            checkbinary += ACCOUNT_BIN_TABLE.get(checksum.charAt(i));
        }

        String hat = Hex.toHex(checkbinary);
        while (hat.length() < 10)
            hat = "0" + hat;

        byte[] checkHex = swapEndian(toByteArray(hat));


        String fallaciousalbatross = Hex.toHex(pubbinary);
        while (fallaciousalbatross.length() < 64)
            fallaciousalbatross = "0" + fallaciousalbatross;

        byte[] publicKey = toByteArray(fallaciousalbatross);

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

        String keyBinary = toBinary(toHex(publicKey)); //we get the address by picking
        //five bit (not byte!) chunks of the public key (in binary)

        byte[] digest = swapEndian(Hashes.digest(5, publicKey)); //the original wallet flips it
        String binary = toBinary(toHex(digest)); //we get the checksum by, similarly
        //to getting the address, picking 5 bit chunks of the five byte digest

        //calculate the checksum:
        String checksum = ""; //string that we will populate with the checksum chars
        while (binary.length() < digest.length * 8)
            binary = "0" + binary; //leading zeroes are sometimes omitted (idk why)
        for (int i = 0; i < ((digest.length * 8) / 5); i++) {
            String fiveBit = binary.substring(i * 5, (i * 5) + 5);
            checksum += ACCOUNT_CHAR_TABLE.get(fiveBit);//go through the [40] bits in
            //our digest and turn each five into a char using the accountCharTable
        }

        //calculate the address
        String account = ""; //string to populate with address chars
        while (keyBinary.length() < 260) //binary for address should always be 260 bits
            keyBinary = "0" + keyBinary; //so pad it if it isn't
        for (int i = 0; i < keyBinary.length(); i += 5) {
            String fiveBit = keyBinary.substring(i, i + 5);
            account += ACCOUNT_CHAR_TABLE.get(fiveBit); //go through the 260 bits that
            //represent our public key five bits at a time and convert each five bits
            //into a char that is retrieved from the accountCharTable
        }

        //return the address prefixed with xrb_ and suffixed with
        return "xrb_" + account + checksum;
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
