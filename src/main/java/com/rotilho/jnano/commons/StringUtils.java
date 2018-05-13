package com.rotilho.jnano.commons;

final class StringUtils {
    private StringUtils() {
    }

    public static String leftPad(String str, int size) {
        if (str.length() >= size) {
            return str;
        }

        StringBuilder builder = new StringBuilder();
        while (str.length() + builder.length() < size) {
            builder.append("0");
        }
        return builder.append(str).toString();
    }
}
