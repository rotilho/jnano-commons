package com.rotilho.jnano.commons;

import java.util.HashMap;

import static com.rotilho.jnano.commons.NanoHelper.leftPad;
import static java.lang.String.valueOf;

final class NanoAccountEncodes {
    private static final Alphabet ALPHABET = new Alphabet();

    private NanoAccountEncodes() {
    }

    static String decode(String encoded) {
    	StringBuilder sb = new StringBuilder();
    	for (int i = 0; i < encoded.length(); i++) {
    		sb.append(ALPHABET.getBinary(encoded.charAt(i)));
    	}
    	return sb.toString();
    }
  
    static String encode(String decoded) {
        int codeSize = 5;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < decoded.length(); i += codeSize) {
        	sb.append(ALPHABET.getCharacter(decoded.substring(i, i + codeSize)));
        }
        return sb.toString();
    }


    private static class Alphabet {
        private static final char[] ACCOUNT_MAP = "13456789abcdefghijkmnopqrstuwxyz".toCharArray();

        private final HashMap<String, String> CHARACTER_TABLE = new HashMap<>();
        private final HashMap<Character, String> BINARY_TABLE = new HashMap<>();

        private Alphabet() {
            for (int i = 0; i < ACCOUNT_MAP.length; i++) {
                String binary = leftPad(Integer.toBinaryString(i), 5);
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
