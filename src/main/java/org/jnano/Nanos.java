package org.jnano;

import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashMap;

import static org.jnano.Hex.*;

public final class Nanos {
    private static final char[] ACCOUNT_MAP = "13456789abcdefghijkmnopqrstuwxyz".toCharArray();

    private static final HashMap<String, Character> ACCOUNT_CHAR_TABLE = new HashMap<>();
    private static final HashMap<Character, String> ACCOUNT_BIN_TABLE = new HashMap<>();

    public static final String SEED_REGEX = "^[A-Z0-9]{64}$";
    public static final String ADDRESS_REGEX = "^(xrb_)[13456789abcdefghijkmnopqrstuwxyz]{60}$";

    static {
        //populate the ACCOUNT_CHAR_TABLE and ACCOUNT_BIN_TABLE
        for (int i = 0; i < ACCOUNT_MAP.length; i++) {
            String bin = Integer.toBinaryString(i);
            while (bin.length() < 5)
                bin = "0" + bin; //pad with 0
            ACCOUNT_CHAR_TABLE.put(bin, ACCOUNT_MAP[i]);
            ACCOUNT_BIN_TABLE.put(ACCOUNT_MAP[i], bin);
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
    public static String createAddress(String seed, int index) {
        if (seed == null || !seed.matches(SEED_REGEX)) {
            throw new IllegalArgumentException("Invalid seed " + seed);
        }

        if (index < 0) {
            throw new IllegalArgumentException("Invalid index " + index);
        }

        byte[] privateKey = Hash.digest256(
                toByteArray(seed), //add seed
                ByteBuffer.allocate(4).putInt(index).array() //and add index
        ); //digest 36 bytes into 32
        byte[] publicKey = ED25519.publickey(privateKey); //return the public key
        return toAddress(publicKey);
    }

    /**
     * Extract public key from a Address
     *
     * @param address
     * @return public key
     */
    public static byte[] toPublicKey(String address) {
        if (address == null || !address.matches(ADDRESS_REGEX)) {
            throw new IllegalArgumentException("Invalid address " + address);
        }

        String pub = address.substring(4, address.length() - 8);
        String checksum = address.substring(address.length() - 8);

        String pubBin = "";
        for (int i = 0; i < pub.length(); i++) {
            pubBin += ACCOUNT_BIN_TABLE.get(pub.charAt(i));
        }
        pubBin = pubBin.substring(4);

        String checkBin = "";
        for (int i = 0; i < checksum.length(); i++) {
            checkBin += ACCOUNT_BIN_TABLE.get(checksum.charAt(i));
        }

        String hat = Hex.toHex(checkBin);
        while (hat.length() < 10)
            hat = "0" + hat;

        byte[] checkHex = swapEndian(toByteArray(hat));


        String fallaciousalbatross = Hex.toHex(pubBin);
        while (fallaciousalbatross.length() < 64)
            fallaciousalbatross = "0" + fallaciousalbatross;

        byte[] publicKey = toByteArray(fallaciousalbatross);

        byte[] digest = Hash.digest(5, publicKey);

        if (!Arrays.equals(digest, checkHex)) {
            throw new IllegalArgumentException("Invalid checksum " + checksum);
        }

        return publicKey;
    }

    /**
     * Create address to a given public key
     *
     * @param publicKey
     * @return address
     */
    public static String toAddress(byte[] publicKey) {
        if (publicKey == null || publicKey.length != 32) {
            throw new IllegalArgumentException("Invalid public key" + Arrays.toString(publicKey));
        }

        String keyBinary = toBinary(toHex(publicKey)); //we get the address by picking
        //five bit (not byte!) chunks of the public key (in binary)

        byte[] digest = swapEndian(Hash.digest(5, publicKey)); //the original wallet flips it
        String bin = toBinary(toHex(digest)); //we get the checksum by, similarly
        //to getting the address, picking 5 bit chunks of the five byte digest

        //calculate the checksum:
        String checksum = ""; //string that we will populate with the checksum chars
        while (bin.length() < digest.length * 8)
            bin = "0" + bin; //leading zeroes are sometimes omitted (idk why)
        for (int i = 0; i < ((digest.length * 8) / 5); i++) {
            String fiveBit = bin.substring(i * 5, (i * 5) + 5);
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
