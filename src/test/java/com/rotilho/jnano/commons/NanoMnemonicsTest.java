package com.rotilho.jnano.commons;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.rotilho.jnano.commons.NanoHelper.toByteArray;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


public class NanoMnemonicsTest {
    private static final byte[] SEED = toByteArray("8FAB00B5EBFE073D5D8927345DEC6F397A49B73966A040148270EE97CA594341");
    private static final List<String> MNEMONIC = toList("moral fix coin subject there pact involve ceiling crowd urge bridge indicate pig swear tortoise staff divorce piano order tag lake coach artist denial");

    private static List<String> toList(String mnemonicStr) {
        return Pattern.compile(" ")
                .splitAsStream(mnemonicStr)
                .collect(Collectors.toList());
    }

    @Test
    public void shouldCreateMnemonic() {
        assertEquals(MNEMONIC, NanoMnemonics.createBip39Mnemonic(SEED, NanoMnemonics.NanoMnemonicLanguage.ENGLISH));
    }

    @Test
    public void shouldConvertMnemonicToSeed() {
        assertArrayEquals(SEED, NanoMnemonics.bip39ToSeed(MNEMONIC, NanoMnemonics.NanoMnemonicLanguage.ENGLISH));
    }

    @Test
    public void shouldCreateMnemonicAndConvertBackToSeed() {
        // given
        byte[] expectedSeed = NanoSeeds.generateSeed();

        // when
        List<String> mnemonic = NanoMnemonics.createBip39Mnemonic(expectedSeed, NanoMnemonics.NanoMnemonicLanguage.ENGLISH);
        byte[] seed = NanoMnemonics.bip39ToSeed(mnemonic, NanoMnemonics.NanoMnemonicLanguage.ENGLISH);

        // then
        assertArrayEquals(expectedSeed, seed);
    }

    @Test
    public void shouldNotValidateWhenMnemonicHaveInvalidSize() {
        assertFalse(NanoMnemonics.isValid(emptyList(), NanoMnemonics.NanoMnemonicLanguage.ENGLISH));
    }

    @Test
    public void shouldNotValidateWhenMnemonicHaveInvalidChecksum() {
        // given
        List<String> invalidMnemonic = new ArrayList<>(MNEMONIC);
        Collections.reverse(invalidMnemonic);

        // then
        assertFalse(NanoMnemonics.isValid(invalidMnemonic, NanoMnemonics.NanoMnemonicLanguage.ENGLISH));
    }

}