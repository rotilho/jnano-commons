package org.jnano;

import java.util.HashMap;
import java.util.stream.Collectors;

import static java.lang.String.valueOf;

class AccountEncodes {
    private static final Alphabet ALPHABET = new Alphabet();

    private AccountEncodes() {
    }

    public static String decode(String encoded) {
        return encoded.chars()
                .mapToObj(c -> (char) c)
                .map(ALPHABET::getBinary)
                .collect(Collectors.joining());
    }

//    public static String encode(String decoded) {
//        decoded.chars()
//                .mapToObj(String::valueOf)
//                .map(ALPHABET::getCharacter)
//                .collect(Collectors.joining());
//    }


    private static class Alphabet {
        private static final char[] ACCOUNT_MAP = "13456789abcdefghijkmnopqrstuwxyz".toCharArray();

        private final HashMap<String, String> ACCOUNT_CHAR_TABLE = new HashMap<>();
        private final HashMap<String, String> ACCOUNT_BIN_TABLE = new HashMap<>();

        private Alphabet() {
            for (int i = 0; i < ACCOUNT_MAP.length; i++) {
                String binary = StringUtils.leftPad(Integer.toBinaryString(i), 5);
                String character = valueOf(ACCOUNT_MAP[i]);
                ACCOUNT_CHAR_TABLE.put(binary, character);
                ACCOUNT_BIN_TABLE.put(character, binary);
            }
        }

        private String getCharacter(char binary) {
            return ACCOUNT_CHAR_TABLE.get(valueOf(binary));
        }

        private String getBinary(char character) {
            return ACCOUNT_BIN_TABLE.get(valueOf(character));
        }
    }
}
