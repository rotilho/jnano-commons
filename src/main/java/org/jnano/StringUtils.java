package org.jnano;

final class StringUtils {
    private StringUtils() {
    }

    public static String leftPad(String str, int size) {
        if (str.length() >= size) {
            return str;
        }
        return String.format("%0" + size + "d", Integer.parseInt(str));
    }
}
