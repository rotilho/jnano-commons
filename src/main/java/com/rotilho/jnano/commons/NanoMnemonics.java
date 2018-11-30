package com.rotilho.jnano.commons;

import com.rotilho.jnano.commons.exception.ActionNotSupportedException;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.NonNull;

import static java.util.Arrays.copyOf;
import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableMap;


public final class NanoMnemonics {
    private NanoMnemonics() {
    }

    @NonNull
    public static List<String> createBip39Mnemonic(@NonNull byte[] seed, @NonNull NanoMnemonicLanguage language) {
        Preconditions.checkSeed(seed);

        int seedLength = seed.length * 8;
        byte[] seedWithChecksum = copyOf(seed, seed.length + 1);
        seedWithChecksum[seed.length] = checksum(seed);

        int checksumLength = seedLength / 32;
        int mnemonicSentenceLength = (seedLength + checksumLength) / 11;

        try {
        	List<String> ret = new ArrayList<>();
        	for (int i = 0; i < mnemonicSentenceLength; i++) {
        		ret.add(language.getWord(next11Bits(seedWithChecksum, i * 11)));
        	}
        	return ret;
        } finally {
            NanoHelper.wipe(seedWithChecksum);
        }
    }


    @NonNull
    public static byte[] bip39ToSeed(@NonNull List<String> mnemonic, @NonNull NanoMnemonicLanguage language) {
        Preconditions.checkArgument(isValid(mnemonic, language), "Invalid mnemonic");
        byte[] seedWithChecksum = extractSeedWithChecksum(mnemonic, language);
        try {
            return extractSeed(seedWithChecksum);
        } finally {
            NanoHelper.wipe(seedWithChecksum);
        }
    }

    public static boolean isValid(@NonNull List<String> mnemonic, @NonNull NanoMnemonicLanguage language) {
        if (mnemonic.size() != 24 || !mnemonic.stream().allMatch(language::wordExists)) {
            return false;
        }

        byte[] seedWithChecksum = extractSeedWithChecksum(mnemonic, language);
        byte[] seed = extractSeed(seedWithChecksum);

        byte expectedChecksum = checksum(seed);
        try {
            return expectedChecksum == seedWithChecksum[seedWithChecksum.length - 1];
        } finally {
            NanoHelper.wipe(seedWithChecksum);
            NanoHelper.wipe(seed);
        }
    }

    private static byte[] extractSeedWithChecksum(List<String> mnemonic, NanoMnemonicLanguage language) {
        int mnemonicSentenceLength = mnemonic.size();

        int seedWithChecksumLength = mnemonicSentenceLength * 11;
        byte[] seedWithChecksum = new byte[(seedWithChecksumLength + 7) / 8];

        
        List<Integer> mnemonicIndexes = new ArrayList<>();
        for (String word: mnemonic) {
        	mnemonicIndexes.add(language.getIndex(word));
        }

        for (int i = 0; i < mnemonicSentenceLength; i++) {
        	writeNext11(seedWithChecksum, mnemonicIndexes.get(i), i * 11);
        }

        return seedWithChecksum;
    }

    private static byte[] extractSeed(byte[] seedWithChecksum) {
        return copyOf(seedWithChecksum, seedWithChecksum.length - 1);
    }

    private static byte checksum(final byte[] seed) {
        try {
            final byte[] hash = MessageDigest.getInstance("SHA-256").digest(seed);
            final byte firstByte = hash[0];
            Arrays.fill(hash, (byte) 0);
            return firstByte;
        } catch (NoSuchAlgorithmException e) {
            throw new ActionNotSupportedException("Seed generation not supported", e);
        }
    }

    private static int next11Bits(byte[] bytes, int offset) {
        final int skip = offset / 8;
        final int lowerBitsToRemove = (3 * 8 - 11) - (offset % 8);
        return (((int) bytes[skip] & 0xff) << 16 |
                ((int) bytes[skip + 1] & 0xff) << 8 |
                (lowerBitsToRemove < 8
                        ? (int) bytes[skip + 2] & 0xff
                        : 0)) >> lowerBitsToRemove & (1 << 11) - 1;
    }

    private static void writeNext11(byte[] bytes, int value, int offset) {
        int skip = offset / 8;
        int bitSkip = offset % 8;
        {//byte 0
            byte firstValue = bytes[skip];
            byte toWrite = (byte) (value >> (3 + bitSkip));
            bytes[skip] = (byte) (firstValue | toWrite);
        }

        {//byte 1
            byte valueInByte = bytes[skip + 1];
            final int i = 5 - bitSkip;
            byte toWrite = (byte) (i > 0 ? value << i : value >> -i);
            bytes[skip + 1] = (byte) (valueInByte | toWrite);
        }

        if (bitSkip >= 6) {//byte 2
            byte valueInByte = bytes[skip + 2];
            byte toWrite = (byte) (value << 13 - bitSkip);
            bytes[skip + 2] = (byte) (valueInByte | toWrite);
        }
    }


    public enum NanoMnemonicLanguage {
        ENGLISH("english.txt");

        private final List<String> dictionary;
        private final Map<String, Integer> dictionaryMap;

        NanoMnemonicLanguage(String fileName) {
            try {
                URL fileLocation = getClassLoader().getResource(fileName);
                this.dictionary = unmodifiableList(Files.readAllLines(Paths.get(fileLocation.toURI())));
                Map<String, Integer> tempDictionaryMap = new HashMap<>();
                for (String word: dictionary) {
                	tempDictionaryMap.put(word, dictionary.indexOf(word));
                }
                this.dictionaryMap = unmodifiableMap(tempDictionaryMap);
            } catch (Exception e) {
                throw new IllegalStateException("Could'nt read file " + fileName, e);
            }
        }

        private ClassLoader getClassLoader() {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            if (classLoader != null) {
                return classLoader;
            }
            return NanoMnemonicLanguage.class.getClassLoader();
        }

        public List<String> getDictionary() {
            return dictionary;
        }

        public Map<String, Integer> getDictionaryMap() {
            return dictionaryMap;
        }

        public String getWord(int index) {
            return dictionary.get(index);
        }

        public boolean wordExists(String word) {
            return dictionaryMap.containsKey(word);
        }

        public Integer getIndex(String word) {
            return dictionaryMap.get(word);
        }
    }
}
