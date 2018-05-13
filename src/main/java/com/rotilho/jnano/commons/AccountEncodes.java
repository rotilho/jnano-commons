package com.rotilho.jnano.commons;

import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.valueOf;

final class AccountEncodes {
    private static final Alphabet ALPHABET = new Alphabet();

    private AccountEncodes() {
    }

    public static String decode(String encoded) {
        return encoded.chars()
                .mapToObj(c -> (char) c)
                .map(ALPHABET::getBinary)
                .collect(Collectors.joining());
    }

    public static String encode(String decoded) {
        int codeSize = 5;
        return Stream.iterate(0, i -> i + codeSize)
                .map(i -> decoded.substring(i, i + codeSize))
                .map(ALPHABET::getCharacter)
                .limit(decoded.length() / codeSize)
                .collect(Collectors.joining());
    }


    private static class Alphabet {
        private static final char[] ACCOUNT_MAP = "13456789abcdefghijkmnopqrstuwxyz".toCharArray();

        private final HashMap<String, String> CHARACTER_TABLE = new HashMap<>();
        private final HashMap<Character, String> BINARY_TABLE = new HashMap<>();

        private Alphabet() {
            for (int i = 0; i < ACCOUNT_MAP.length; i++) {
                String binary = StringUtils.leftPad(Integer.toBinaryString(i), 5);
                CHARACTER_TABLE.put(binary, valueOf(ACCOUNT_MAP[i]));
                BINARY_TABLE.put(ACCOUNT_MAP[i], binary);
            }
        }

        private String getCharacter(String binary) {
            return CHARACTER_TABLE.get(binary);
        }

        private String getBinary(char character) {
            return BINARY_TABLE.get(character);
        }
    }
}
