package org.jnano;

import java.math.BigInteger;


//TODO add methods with padding
final class DataUtils {
    /**
     * An array of hex characters used by the {@link #toHex(byte[])}
     * method.
     */
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    private DataUtils() {
    }

    /**
     * Converts an array of bytes into their String representation.
     * Note that there are no "0x" prefixes, just the value of the
     * byte is included in the string. For example, if you passed a
     * byte array containing two bytes, [0xCA, 0xFE], the returned
     * string would be "CAFE". Bytes in the string will always be
     * uppercase.
     *
     * @param bytes An array of bytes to be represented as a string.
     * @return A string representation of the passed byte array.
     */
    protected static String toHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * Returns a string containing an array of bytes as a byte array.
     * This inverses the {@link #toHex(byte[])} method - if a
     * string containing "CAFEBABE" is passed to this method, it will
     * return a byte array containing: 0xCA, 0xFE, 0xBA, 0xBE.
     *
     * @param s A string containing hex bytes.
     * @return A byte array containing the bytes of a passed string.
     */
    protected static byte[] toByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * Converts a hex string into the appropriate binary values.
     * The actual value of the string's bytes in memory are not
     * what is converted to binary - but rather the bytes that
     * the string represents (contains). For example a string
     * passed to this method containing "CAFE" would return a
     * string containing "1100101011111110". However, it should
     * be noted that sometimes this method doesn't return all of
     * the required leading zeroes, so you may want to do some
     * validation yourself.
     *
     * @param hex A string containing valid hex characters.
     * @return A binary representation of the passed string.
     */
    protected static String toBinary(String hex) {
        String value = new BigInteger(hex, 16).toString(2);
        String formatPad = "%" + (hex.length() * 4) + "s";
        return (String.format(formatPad, value).replace(" ", ""));
    }

    protected static String toHex(String bin) {
        BigInteger b = new BigInteger(bin, 2);
        return b.toString(16).toUpperCase();
    }


    protected static String radix(BigInteger value) {
        return StringUtils.leftPad(value.toString(16).toUpperCase(), 32);
    }

    protected static byte[] reverse(byte[] b) {
        byte[] bb = new byte[b.length];
        for (int i = b.length; i > 0; i--) {
            bb[b.length - i] = b[i - 1];
        }
        return bb;
    }


}
